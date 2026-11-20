from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
def variant():
    marker='QA_PROTEGIDO_'+uuid.uuid4().hex
    comment_marker='QA_COMENTARIO_'+uuid.uuid4().hex
    body_marker='QA_NEGADO_'+uuid.uuid4().hex
    password='QaPass!'+uuid.uuid4().hex
    email='qa-audit-'+uuid.uuid4().hex+'@example.test'
    u=a.request('/users',dict(name='Conta sintética QA',email=email,password=password,role='CLIENT',clientId=other.me['clientId']),'POST',201)
    actor=Account(email,password)
    t=create_ticket(new_type(False,False),subject=marker)
    action(c,t,'comments',{'body':comment_marker,'effectiveResponse':False},201)
    before=detail(t)
    actor.token=actor.request('/auth/csrf')['token']
    cookie_values=[cookie.value for cookie in actor.jar if cookie.value]
    check('Credenciais sintéticas e sessão disponíveis',bool(password) and bool(actor.token) and bool(cookie_values))
    count=int(sql("SELECT COUNT(*) FROM security_access_events WHERE actor_id='"+actor.me['id']+"' AND resource_id='"+t['id']+"'"))
    actor.request('/tickets/'+t['id']+'/comments',{'body':body_marker,'effectiveResponse':False},'POST',404,version=before['ticket']['version'])
    rows=sql("SELECT JSON_OBJECT('id',id,'actor_id',actor_id,'actor_role',actor_role,'event_code',event_code,'resource_type',resource_type,'resource_id',resource_id,'occurred_at',DATE_FORMAT(occurred_at,'%Y-%m-%dT%H:%i:%s.%f')) FROM security_access_events WHERE actor_id='"+actor.me['id']+"' AND resource_id='"+t['id']+"' ORDER BY occurred_at,id").splitlines()
    check('Evento único correlacionado',len(rows)==count+1,dict(actorId=actor.me['id'],ticketId=t['id'],delta=len(rows)-count))
    columns=sql("SELECT COLUMN_NAME FROM information_schema.columns WHERE table_schema=DATABASE() AND table_name='security_access_events' ORDER BY ordinal_position").splitlines()
    expected=['id','actor_id','actor_role','event_code','resource_type','resource_id','occurred_at']
    check('Todas as colunas da tabela incluídas',columns==expected,columns)
    event=json.loads(rows[-1]); check('Evento completo correlacionado',event['actor_id']==actor.me['id'] and event['actor_role']=='CLIENT' and event['event_code']=='SECURITY_TICKET_ACCESS_DENIED' and event['resource_type']=='TICKET' and event['resource_id']==t['id'] and bool(event['occurred_at']))
    sensitive={'senha':password,'token_csrf':actor.token,'assunto':marker,'comentario_protegido':comment_marker,'corpo_negado':body_marker}
    sensitive.update({'cookie_'+str(i):value for i,value in enumerate(cookie_values)})
    record=rows[-1]
    check('Registro integral sem marcadores sensíveis',all(value not in record for value in sensitive.values()),{key:value not in record for key,value in sensitive.items()})
    logs=[]
    for path in Path('target').glob('qa-runtime*-out.log'):
        for line in path.read_text(encoding='utf-8',errors='replace').splitlines():
            if actor.me['id'] in line and t['id'] in line and 'SECURITY_TICKET_ACCESS_DENIED' in line:logs.append((path.name,line))
    check('Log correlacionado encontrado',len(logs)>=1,dict(files=sorted(set(name for name,line in logs)),count=len(logs)))
    check('Log correlacionado sem marcadores sensíveis',all(value not in line for name,line in logs for value in sensitive.values()),{key:all(value not in line for name,line in logs) for key,value in sensitive.items()})
    check('Ticket e recursos preservados',detail(t)==before)
run_case('CT-08-auditoria-dados-sensiveis','Alta',variant)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output)
if output.exists():raise SystemExit('evidência existente')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
