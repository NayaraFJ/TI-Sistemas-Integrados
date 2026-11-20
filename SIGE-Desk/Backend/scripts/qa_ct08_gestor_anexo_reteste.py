from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
def variant():
    tp=new_type(False,False)
    t=create_ticket(tp); t2=create_ticket(tp); foreign=create_ticket(tp)
    for ticket, assignee in [(t,other_manager),(t2,other_manager),(foreign,manager)]:
        start(ticket)
        action(staff,ticket,'triage',dict(priority='HIGH',assigneeId=assignee.me['id'],campaignId=campaign['id'],demandTypeId=tp['id'],effectiveResponse='Retorno sintético QA',sendToExecution=True))
    own=c.upload(t2['id']); protected=c.upload(foreign['id'])
    actor=other_manager
    actor.request('/tickets/'+t['id']); actor.request('/tickets/'+t2['id']); actor.request('/tickets/'+foreign['id'],expected=404)
    before=[detail(x) for x in [t,t2,foreign]]
    def events():
        q="SELECT JSON_OBJECT('actor',actor_id,'role',actor_role,'code',event_code,'type',resource_type,'resource',resource_id,'at',DATE_FORMAT(occurred_at,'%Y-%m-%dT%H:%i:%s.%f')) FROM security_access_events WHERE actor_id='"+actor.me['id']+"' ORDER BY occurred_at,id"
        return [json.loads(x) for x in sql(q).splitlines() if x]
    def test(attachment,should_event):
        count=len(events()); path='/tickets/'+t['id']+'/attachments/'+attachment+'/download'
        response=actor.request(path,expected=404); new=events()[count:]
        valid=len(new)==1 and new[0]['actor']==actor.me['id'] and new[0]['role']=='TRAFFIC_MANAGER' and new[0]['type']=='ATTACHMENT' and new[0]['resource']==attachment and new[0]['code']=='SECURITY_ATTACHMENT_ACCESS_DENIED' and bool(new[0]['at']) if should_event else len(new)==0
        check('Auditoria anexo '+('protegido' if should_event else 'inexistente ou visível'),valid,dict(path=path,response=response,events=new))
        return response
    response=test(protected['id'],True); absent=test(str(uuid.uuid4()),False); visible=test(own['id'],False)
    def generic(x): return {k:v for k,v in x.items() if k not in ['timestamp','path','instance']} if isinstance(x,dict) else x
    check('Corpos genéricos equivalentes',generic(response)==generic(absent)==generic(visible),dict(protected=response,missing=absent,visible=visible))
    check('Tickets e histórico preservados',[detail(x) for x in [t,t2,foreign]]==before)
    check('Bytes preservados no anexo protegido',manager.request('/tickets/'+foreign['id']+'/attachments/'+protected['id']+'/download')==b'coluna;valor\ncontrole;1\n')
    check('Bytes preservados no anexo visível',actor.request('/tickets/'+t2['id']+'/attachments/'+own['id']+'/download')==b'coluna;valor\ncontrole;1\n')
run_case('CT-08-gestor-anexo-reteste','Alta',variant)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output)
if output.exists(): raise SystemExit('evidência existente')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2)); raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
