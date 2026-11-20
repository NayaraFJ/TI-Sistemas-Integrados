"""Reteste independente da auditoria de recursos vinculados em QA local."""
from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
def variant():
    tp=new_type(False,False); t=create_ticket(tp); t2=create_ticket(tp); own=c.upload(t2['id'])
    foreign_campaign=next(x for x in refs['campaigns'] if x['clientId']==other.me['clientId'])
    foreign=other.request('/tickets',dict(clientId=other.me['clientId'],campaignId=foreign_campaign['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag,description='Fixture sintética',urgency='LOW',metrics={'fields':{}}),'POST',201)
    attachment=other.upload(foreign['id']); action(other,foreign,'comments',{'body':'Conteúdo sintético','effectiveResponse':False},201)
    note=sql("SELECT id FROM notifications WHERE ticket_id='"+foreign['id']+"' AND user_id='"+a.me['id']+"' ORDER BY created_at LIMIT 1").splitlines()[0]
    before=detail(t); before2=detail(t2); foreign_before=other.request('/tickets/'+foreign['id'])
    def note_state():return sql("SELECT CONCAT(id,'|',user_id,'|',ticket_id,'|',COALESCE(DATE_FORMAT(read_at,'%Y-%m-%dT%H:%i:%s.%f'),'NULL')) FROM notifications WHERE id='"+note+"'")
    note_before=note_state()
    def events():
        q="SELECT JSON_OBJECT('actor',actor_id,'role',actor_role,'code',event_code,'type',resource_type,'resource',resource_id,'at',DATE_FORMAT(occurred_at,'%Y-%m-%dT%H:%i:%s.%f')) FROM security_access_events WHERE actor_id='"+c.me['id']+"' ORDER BY occurred_at,id"
        return [json.loads(x) for x in sql(q).splitlines() if x]
    def test(path,method,kind,resource,expected_event):
        count=len(events()); response=c.request(path,method=method,expected=404); new=events()[count:]
        good=(len(new)==1 and new[0]['actor']==c.me['id'] and new[0]['role']=='CLIENT' and new[0]['type']==kind and new[0]['resource']==resource and new[0]['code']=='SECURITY_'+kind+'_ACCESS_DENIED' and bool(new[0]['at'])) if expected_event else len(new)==0
        check('Auditoria '+kind+' '+('existente protegido' if expected_event else 'ausente ou visível'),good,dict(path=path,response=response,events=new)); return response
    response=test('/tickets/'+t['id']+'/attachments/'+attachment['id']+'/download','GET','ATTACHMENT',attachment['id'],True)
    absent=test('/tickets/'+t['id']+'/attachments/'+str(uuid.uuid4())+'/download','GET','ATTACHMENT','',False)
    visible=test('/tickets/'+t['id']+'/attachments/'+own['id']+'/download','GET','ATTACHMENT','',False)
    def generic(x):return {k:v for k,v in x.items() if k not in ['timestamp','path','instance']} if isinstance(x,dict) else x
    check('Resposta genérica anexo',generic(response)==generic(absent)==generic(visible),dict(protected=response,missing=absent,visible=visible))
    response=test('/notifications/'+note+'/read','POST','NOTIFICATION',note,True)
    absent=test('/notifications/'+str(uuid.uuid4())+'/read','POST','NOTIFICATION','',False)
    check('Resposta genérica notificação',generic(response)==generic(absent),dict(protected=response,missing=absent))
    check('Tickets e notificação preservados',detail(t)==before and detail(t2)==before2 and other.request('/tickets/'+foreign['id'])==foreign_before and note_state()==note_before)
    check('Conteúdo do anexo protegido preservado',other.request('/tickets/'+foreign['id']+'/attachments/'+attachment['id']+'/download')==b'coluna;valor\ncontrole;1\n')
    from concurrent.futures import ThreadPoolExecutor
    sessions=[Account('cliente@aurora.demo') for _ in range(8)]
    for session in sessions:session.request('/auth/csrf');session.token=session.request('/auth/csrf')['token']
    count=len(events())
    def concurrent_attempt(pair):
        i,session=pair
        if i%2:session.request('/notifications/'+note+'/read',method='POST',expected=404)
        else:session.request('/tickets/'+t['id']+'/attachments/'+attachment['id']+'/download',expected=404)
    with ThreadPoolExecutor(max_workers=8) as executor:list(executor.map(concurrent_attempt,enumerate(sessions)))
    new=events()[count:]
    check('Oito negativas simultâneas registradas',len(new)==8 and sum(e['type']=='ATTACHMENT' and e['resource']==attachment['id'] for e in new)==4 and sum(e['type']=='NOTIFICATION' and e['resource']==note for e in new)==4 and all(e['actor']==c.me['id'] and e['role']=='CLIENT' and bool(e['at']) for e in new),new)
    check('Estado preservado após concorrência',detail(t)==before and detail(t2)==before2 and other.request('/tickets/'+foreign['id'])==foreign_before and note_state()==note_before)
run_case('CT-08-vinculos-reteste','Alta',variant)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output)
if output.exists():raise SystemExit('evidência existente')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
