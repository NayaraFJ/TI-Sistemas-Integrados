from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())

def variant():
    t=create_ticket(new_type(False,False)); before=detail(t); version=before['ticket']['version']
    path='/tickets/'+t['id']+'/comments'
    for actor in [other,other_manager]:
        query="SELECT COUNT(*) FROM security_access_events WHERE actor_id='"+actor.me['id']+"' AND resource_id='"+t['id']+"'"
        for label,body in [('invalid',{'body':'','effectiveResponse':False}),('valid',{'body':'Tentativa sintética','effectiveResponse':False})]:
            count=int(sql(query)); response=actor.request(path,body,'POST',404,version=version)
            delta=int(sql(query))-count
            check('Negativa auditada '+actor.me['role']+' '+label,delta==1,dict(auditDelta=delta,response=response))
        missing='/tickets/'+str(uuid.uuid4())+'/comments'
        response=actor.request(missing,{'body':'','effectiveResponse':False},'POST',404,version=version)
        check('Inexistente também genérico '+actor.me['role'],response['code']=='REQUEST_ERROR' and response['message']=='Ticket não encontrado',response)
        check('Estado preservado '+actor.me['role'],detail(t)==before)
    c.request(path,{'body':'','effectiveResponse':False},'POST',400,version=version)
    check('Corpo inválido do proprietário não altera ticket',detail(t)==before)

run_case('CT-08-corpo-invalido','Alta',variant)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output)
if output.exists(): raise SystemExit('evidência existente')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
raise SystemExit(0 if cases[0]['status']=='APROVADO' else 1)
