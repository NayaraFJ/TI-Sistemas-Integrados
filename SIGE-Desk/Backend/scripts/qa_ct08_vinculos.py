"""CT-08: IDs divergentes, anexos e notificações em QA local."""
from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())

def variant():
    tp=new_type(False,False); t=create_ticket(tp); other_ticket=create_ticket(tp)
    first=c.upload(t['id'],b'conteudo primeiro sintetico\n'); second=c.upload(other_ticket['id'],b'conteudo segundo sintetico\n')
    action(c,t,'comments',{'body':'Historico protegido sintetico','effectiveResponse':False},201)
    start(t); triage(t,tp)
    before=detail(t); before2=detail(other_ticket)
    check('Fixture contém histórico, comentário e anexo',bool(before['history']) and bool(before['comments']) and len(before['attachments'])==1)
    def events(actor):
        q="SELECT JSON_OBJECT('actor',actor_id,'role',actor_role,'code',event_code,'type',resource_type,'resource',resource_id,'at',DATE_FORMAT(occurred_at,'%Y-%m-%dT%H:%i:%s.%f')) FROM security_access_events WHERE actor_id='"+actor.me['id']+"' ORDER BY occurred_at,id"
        return [json.loads(x) for x in sql(q).splitlines() if x]
    for actor in [c,manager,other,other_manager]:
        visible=actor in [c,manager]; count=len(events(actor))
        actor.request('/tickets/'+t['id']+'/attachments/'+second['id']+'/download',expected=404)
        new=events(actor)[count:]
        check('ID divergente negado '+actor.me['role'],True,dict(ticket=t['id'],attachment=second['id'],audit=new))
        if not visible:
            check('Auditoria do ticket invisível '+actor.me['role'],len(new)==1 and new[0]['actor']==actor.me['id'] and new[0]['role']==actor.me['role'] and new[0]['resource']==t['id'] and new[0]['type']=='TICKET' and new[0]['code']=='SECURITY_TICKET_ACCESS_DENIED' and bool(new[0]['at']),new)
    for actor in [other,other_manager]:
        count=len(events(actor)); boundary='qa'+uuid.uuid4().hex
        raw=('--'+boundary+'\r\nContent-Disposition: form-data; name="file"; filename="negado.csv"\r\nContent-Type: text/csv\r\n\r\nnegado\r\n--'+boundary+'--\r\n').encode()
        actor.request('/tickets/'+t['id']+'/attachments',method='POST',expected=404,raw=raw,content_type='multipart/form-data; boundary='+boundary)
        new=events(actor)[count:]
        check('Auditoria upload negado '+actor.me['role'],len(new)==1 and new[0]['resource']==t['id'] and new[0]['actor']==actor.me['id'] and new[0]['role']==actor.me['role'] and bool(new[0]['at']) and new[0]['code']=='SECURITY_TICKET_ACCESS_DENIED',new)
    check('Ticket e histórico preservados',detail(t)==before,dict(ticket=t['id']))
    check('Ticket e anexo divergente preservados',detail(other_ticket)==before2 and c.request('/tickets/'+other_ticket['id']+'/attachments/'+second['id']+'/download')==b'conteudo segundo sintetico\n',dict(ticket=other_ticket['id']))
    check('Anexo original preservado',c.request('/tickets/'+t['id']+'/attachments/'+first['id']+'/download')==b'conteudo primeiro sintetico\n')
run_case('CT-08-vinculos','Alta',variant)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output)
if output.exists():raise SystemExit('evidência existente')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
