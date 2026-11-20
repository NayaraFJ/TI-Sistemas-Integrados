"""Regressões CT-07/08 em 8081 e schema QA; iniciar API com Hikari maximum-pool-size=5."""
import concurrent.futures, threading, time
from pathlib import Path

source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index("suite=[")],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())

def notifications(ticket,event):
    raw=sql("SELECT JSON_OBJECT('id',n.id,'userId',n.user_id,'summary',n.summary) FROM notifications n WHERE n.ticket_id='"+ticket['id']+"' AND n.event_type='"+event+"'")
    return [json.loads(line) for line in raw.splitlines() if line]

def notification_regressions():
    tp=new_type(True,True);t=create_ticket(tp);start(t);triage(t,tp)
    action(manager,t,'wait',{'reason':'Motivo de aguardo distinto'})
    complement='Informacao recebida exclusiva 4921';reason='Conferencia suficiente exclusiva 8732'
    action(c,t,'comments',{'body':complement,'effectiveResponse':False},201)
    action(staff,t,'resume',{'reason':reason})
    resumed=detail(t);notes=notifications(t,'TICKET_RESUMED')
    check('Retomada restaura execução e prazo',resumed['ticket']['status']=='EXECUTION' and bool(resumed['resolutionDueAt']),resumed['resolutionDueAt'])
    expected_due=resumed['resolutionDueAt'][:10]
    check('Cada aviso contém informação recebida e confirmação distintas, fase e data do prazo',bool(notes) and all(all(token.casefold() in n['summary'].casefold() for token in [t['number'],complement,reason,'EXECUTION',expected_due]) for n in notes),notes)
    action(manager,t,'execution',{'actionDescription':'Material inicial','evidenceUrl':'https://example.test/primeiro'})
    before=detail(t);action(c,t,'correction',{'reason':'Correção no ciclo corrente 317'})
    corrected=detail(t);first=notifications(t,'REOPENED')
    check('Correção preserva ciclo e não antecipa prazo',corrected['resolutionCycle']==before['resolutionCycle']==1 and corrected['resolutionDueAt']>=before['resolutionDueAt'],dict(before=before['resolutionDueAt'],after=corrected['resolutionDueAt'],cycle=corrected['resolutionCycle']))
    check('Cada aviso de correção distingue ciclo e motivo',bool(first) and all('Correção no ciclo corrente 317' in n['summary'] and '1' in n['summary'] and 'corre' in n['summary'].casefold() for n in first),first)
    action(manager,t,'execution',{'actionDescription':'Material corrigido','evidenceUrl':'https://example.test/corrigido'})
    action(c,t,'approve',{'reason':'Aprovação final'})
    action(c,t,'reopen',{'reason':'Reabertura depois da conclusão 968'})
    reopened=detail(t);second=[n for n in notifications(t,'REOPENED') if n['id'] not in {x['id'] for x in first}]
    check('Reabertura após conclusão inicia outro ciclo',reopened['resolutionCycle']==2 and reopened['resolutionDueAt'] is None,dict(cycle=reopened['resolutionCycle'],due=reopened['resolutionDueAt']))
    check('Cada aviso de reabertura identifica novo ciclo e motivo',bool(second) and all('Reabertura depois da conclusão 968' in n['summary'] and '2' in n['summary'] for n in second),second)

def parallel_denials():
    tp=new_type(False,False);t=create_ticket(tp);before=detail(t)['ticket']['version'];count=12
    accounts=[Account('rafael@horizonte.demo') for _ in range(count)]
    baseline=int(sql("SELECT COUNT(*) FROM security_access_events WHERE event_code='SECURITY_TICKET_ACCESS_DENIED' AND actor_id='"+other.me['id']+"' AND resource_id='"+t['id']+"'"))
    barrier=threading.Barrier(count)
    def deny(account):
        barrier.wait(timeout=15);start_time=time.monotonic()
        try:
            with account.http.open(urllib.request.Request(args.base_url+'/tickets/'+t['id']),timeout=30) as response:status=response.status;response.read()
        except urllib.error.HTTPError as error:status=error.code;error.read()
        except Exception as error:return dict(status=None,error=str(error),seconds=round(time.monotonic()-start_time,3))
        return dict(status=status,seconds=round(time.monotonic()-start_time,3))
    with concurrent.futures.ThreadPoolExecutor(max_workers=count) as executor: outcomes=list(executor.map(deny,accounts))
    after=int(sql("SELECT COUNT(*) FROM security_access_events WHERE event_code='SECURITY_TICKET_ACCESS_DENIED' AND actor_id='"+other.me['id']+"' AND resource_id='"+t['id']+"'"))
    check('Doze negativas concorrentes retornam 404',len(outcomes)==count and all(o['status']==404 for o in outcomes),outcomes)
    check('Cada negativa concorrente gerou trilha persistida',after-baseline==count,dict(before=baseline,after=after,expected=count))
    check('Ticket preserva versão após negativas concorrentes',detail(t)['ticket']['version']==before,before)

run_case('CT-07-regressoes','Alta',notification_regressions)
run_case('CT-08-concorrencia','Alta',parallel_denials)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag,hikariPoolSize='5: configuração de inicialização a confirmar no processo da API'),cases=cases,checks=results)
output=Path(args.output);output.parent.mkdir(parents=True,exist_ok=True);output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
