from pathlib import Path
source=Path('scripts/qa_functional.py').read_text(encoding='utf-8');exec(compile(source[:source.index('suite=[')],'scripts/qa_functional.py','exec'),globals())
ids=[]; types=[]; snapshots=[]
def run():
    tp=new_type(False,False);types.append(tp['id'])
    for phase in ['OPEN','TRIAGE','WAITING_TRIAGE','EXECUTION','WAITING_EXECUTION']:
        t=create_ticket(tp);ids.append(t['id'])
        check('Fixture API no schema '+phase,sql("SELECT COUNT(*) FROM tickets WHERE id='"+t['id']+"' AND subject='"+tag+"'")=='1')
        if phase!='OPEN':start(t)
        if phase in ['EXECUTION','WAITING_EXECUTION']:triage(t,tp)
        if phase.startswith('WAITING'):action(staff if phase=='WAITING_TRIAGE' else manager,t,'wait',{'reason':'Aguardo sintético CT14'})
        before=detail(t); absent=detail(t)
        check('Sem comando preserva dados '+phase,before==absent)
        reason='Cancelamento sintético CT14 '+phase;expected=409 if phase in ['EXECUTION','WAITING_EXECUTION'] else 200
        action(staff,t,'cancel',{'reason':reason},expected);after=detail(t);snapshots.append(dict(phase=phase,ticket=t['id'],before=before,after=after))
        keys=['responseDueAt','resolutionDueAt','responseCompletedAt','resolutionCycle']
        check('Prazos e ciclo preservados '+phase,all(before[k]==after[k] for k in keys))
        old={h['id']:h for h in before['history']};new={h['id']:h for h in after['history']}
        check('Histórico anterior preservado '+phase,all(new.get(k)==v for k,v in old.items()))
        if expected==200:
            additions=[h for h in after['history'] if h['id'] not in old]
            check('Cancelamento e versão '+phase,after['ticket']['status']=='CANCELLED' and after['ticket']['version']>before['ticket']['version'] and any(h['action']=='CANCELLED' and h['reason']==reason and h['actorName']==staff.me['name'] for h in additions),additions)
        else:check('Negativa preserva estado e versão '+phase,after['ticket']['status']==before['ticket']['status'] and after['ticket']['version']==before['ticket']['version'] and old==new)
try:run_case('CT-14-preservacao','Média',run)
finally:
    for tid in ids:
        if sql("SELECT COUNT(*) FROM tickets WHERE id='"+tid+"' AND subject='"+tag+"'")=='1':
            for table in ['ticket_deadline_notices','notifications','ticket_approvals','ticket_attachments','ticket_comments','ticket_history']:sql('DELETE FROM '+table+" WHERE ticket_id='"+tid+"'")
            sql("DELETE FROM tickets WHERE id='"+tid+"' AND subject='"+tag+"'")
    for typeid in types:sql("DELETE FROM demand_types WHERE id='"+typeid+"' AND name LIKE '"+tag+"%'")
    check('Limpeza dos tickets exclusivos',all(sql("SELECT COUNT(*) FROM tickets WHERE id='"+tid+"'")=='0' for tid in ids))
runtime=Path('target/qa-runtime.jar');report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),limitations=['ReasonRequest possui apenas reason; confirmação visual não é demonstrável pela API. Sem comando é controle de inatividade, não teste do botão.'],cases=cases,checks=results,snapshots=snapshots)
out=Path(args.output)
if out.exists():raise SystemExit('Evidência já existe')
out.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8');print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
