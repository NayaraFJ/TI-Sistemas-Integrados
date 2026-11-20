from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
def variant():
    t=create_ticket(new_type(False,False)); before=detail(t); version=before['ticket']['version']; path='/tickets/'+t['id']+'/comments'
    for actor in [other,other_manager]:
        query="SELECT COUNT(*) FROM security_access_events WHERE actor_id='"+actor.me['id']+"' AND resource_id='"+t['id']+"'"
        for label,body in [('invalid',{'body':'','effectiveResponse':False}),('valid',{'body':'Tentativa sintética','effectiveResponse':False})]:
            count=int(sql(query)); expected=400 if label=='invalid' else 404
            response=actor.request(path,body,'POST',expected,version=version)
            delta=int(sql(query))-count
            results.append(dict(ct=active_ct,name=actor.me['role']+' '+label,passed=True,detail=str(dict(status=expected,auditDelta=delta,response=response))))
        missing='/tickets/'+str(uuid.uuid4())+'/comments'
        response=actor.request(missing,{'body':'','effectiveResponse':False},'POST',400,version=version)
        results.append(dict(ct=active_ct,name=actor.me['role']+' inexistente inválido',passed=True,detail=str(dict(status=400,response=response))))
        check('Estado preservado '+actor.me['role'],detail(t)==before)
run_case('CT-08-corpo-invalido','Alta',variant)
cases[-1]['status']='OBSERVADO'
cases[-1]['interpretation']='Execução observacional; ausência de auditoria requer decisão sobre alcance da política. Não é aprovação integral de CT-08.'
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output)
if output.exists():raise SystemExit('evidência existente')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')

