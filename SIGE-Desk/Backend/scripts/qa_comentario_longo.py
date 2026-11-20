"""Validação de comentário multibyte e resumo em schema QA isolado."""
from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
def long_comment():
    tp=new_type(False,False);t=create_ticket(tp);start(t);triage(t,tp);action(manager,t,'wait',{'reason':'Aguardar detalhes'})
    body='Início único: '+'ação técnica çã 漢字 '*1200+' FIM-EXCLUSIVO';expected_hash=hashlib.sha256(body.encode('utf-8')).hexdigest()
    saved=action(c,t,'comments',{'body':body,'effectiveResponse':False},201)
    check('API retorna comentário integral',saved['body']==body,dict(chars=len(body),bytes=len(body.encode('utf-8'))))
    detail_result=detail(t);check('Detalhe contém comentário integral',any(x['body']==body for x in detail_result['comments']))
    row=sql("SELECT CONCAT(CHAR_LENGTH(body),'|',OCTET_LENGTH(body),'|',SHA2(body,256)) FROM ticket_comments WHERE id='"+saved['id']+"'")
    check('MySQL preserva bytes integrais',row==str(len(body))+'|'+str(len(body.encode('utf-8')))+'|'+expected_hash,row)
    history=[x for x in detail_result['history'] if x['action']=='COMPLEMENT_RECEIVED']
    check('Histórico registra resumo limitado e identificável',bool(history) and any('Início único' in (x['reason'] or '') and len(x['reason'])<=120 for x in history),history)
    action(staff,t,'resume',{'reason':'Conferência realizada'})
    raw=sql("SELECT JSON_OBJECT('summary',summary) FROM notifications WHERE ticket_id='"+t['id']+"' AND event_type='TICKET_RESUMED'")
    notes=[json.loads(x)['summary'] for x in raw.splitlines() if x]
    check('Notificações de retomada incluem resumo e confirmação',bool(notes) and all('Início único' in x and 'Conferência realizada' in x for x in notes),notes)
run_case('CT-07-comentario-longo','Alta',long_comment)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output);output.parent.mkdir(parents=True,exist_ok=True);output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
raise SystemExit(0 if cases[0]['status']=='APROVADO' else 1)
