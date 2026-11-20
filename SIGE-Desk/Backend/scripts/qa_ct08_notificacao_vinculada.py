"""CT-08: tentativa de marcar notificação alheia, com auditoria observada."""
from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
def variant():
    t=create_ticket(new_type(False,False)); action(c,t,'comments',{'body':'Notificação sintética protegida','effectiveResponse':False},201)
    rows=sql("SELECT id FROM notifications WHERE ticket_id='"+t['id']+"' AND user_id='"+a.me['id']+"' ORDER BY created_at LIMIT 1").splitlines()
    check('Notificação alheia disponível',len(rows)==1); note=rows[0]
    def state():return sql("SELECT CONCAT(id,'|',user_id,'|',ticket_id,'|',COALESCE(DATE_FORMAT(read_at,'%Y-%m-%dT%H:%i:%s.%f'),'NULL')) FROM notifications WHERE id='"+note+"'")
    before=state(); ticket_before=detail(t)
    for actor in [other,other_manager]:
        q="SELECT COUNT(*) FROM security_access_events WHERE actor_id='"+actor.me['id']+"'"
        count=int(sql(q)); actor.request('/notifications/'+note+'/read',method='POST',expected=404)
        delta=int(sql(q))-count
        check('Notificação alheia não alterada '+actor.me['role'],state()==before,dict(notificationId=note,ticketId=t['id'],actorId=actor.me['id'],auditDelta=delta))
        check('Notificação ausente da lista '+actor.me['role'],note not in {n['id'] for n in all_items(actor,'/notifications')})
    check('Ticket e histórico preservados',detail(t)==ticket_before)
run_case('CT-08-notificacao-vinculada','Alta',variant)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output)
if output.exists():raise SystemExit('evidência existente')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
