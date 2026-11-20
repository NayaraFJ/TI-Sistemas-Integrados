"""CT-08: auditoria de acessos negados em API/MySQL local."""
from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
def audit_matrix():
    t=create_ticket(new_type(False,False)); attachment=c.upload(t['id'])
    action(c,t,'comments',{'body':'Conteúdo protegido sintético CT08','effectiveResponse':False},201)
    before=detail(t); version=before['ticket']['version']
    paths=[('detalhe','/tickets/'+t['id'],'GET',None),('anexos','/tickets/'+t['id']+'/attachments','GET',None),('download','/tickets/'+t['id']+'/attachments/'+attachment['id']+'/download','GET',None),('comentário','/tickets/'+t['id']+'/comments','POST',{'body':'Tentativa sintética','effectiveResponse':False})]
    for actor in [other,other_manager]:
        for label,path,method,body in paths:
            query="SELECT JSON_OBJECT('actor',actor_id,'role',actor_role,'code',event_code,'type',resource_type,'resource',resource_id,'at',DATE_FORMAT(occurred_at,'%Y-%m-%dT%H:%i:%s.%f')) FROM security_access_events WHERE actor_id='"+actor.me['id']+"' AND resource_id='"+t['id']+"' ORDER BY occurred_at,id"
            previous=sql(query).splitlines(); actor.request(path,body,method,404,version=version); after=sql(query).splitlines()
            check('Uma trilha por negativa '+actor.me['role']+' '+label,len(after)==len(previous)+1,dict(previous=len(previous),after=len(after)))
            event=json.loads(after[-1]); check('Ator, recurso, resultado e instante '+actor.me['role']+' '+label,event['actor']==actor.me['id'] and event['role']==actor.me['role'] and event['resource']==t['id'] and event['type']=='TICKET' and event['code']=='SECURITY_TICKET_ACCESS_DENIED' and bool(event['at']),event)
    after=detail(t); check('Estado e recursos preservados',before==after,dict(ticket=t['id'],beforeVersion=version,afterVersion=after['ticket']['version']))
run_case('CT-08-auditoria-matriz','Alta',audit_matrix)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output);output.parent.mkdir(parents=True,exist_ok=True);output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
