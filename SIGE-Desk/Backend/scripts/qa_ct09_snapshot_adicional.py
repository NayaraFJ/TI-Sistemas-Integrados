from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
def run():
    fields=[dict(name='budget',label='Valor original',type='NUMBER',required=True,validation={'min':0,'max':100}),dict(name='date',label='Data',type='DATE',required=True),dict(name='choice',label='Escolha',type='SELECT',options=['Sim','Nao'],required=True),dict(name='context',label='Contexto',type='TEXT',requiredWhen={'field':'choice','equals':'Sim'},validation={'minLength':3}),dict(name='brief',label='Arquivo',type='FILE',required=True)]
    tp=a.request('/demand-types',dict(name=tag+' CT09',approvalRequired=True,evidenceRequired=True,fieldDefinitions=json.dumps(fields)),'POST',201)
    body=dict(clientId=client_id,campaignId=campaign['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag+' CT09 antigo',description='Fixture sintética',urgency='LOW',metrics={'fields':{'budget':'0','date':'2026-10-04','choice':'Sim','context':'abc','brief':'brief.csv'}})
    def multipart(payload,expected):
        b='ct09'+uuid.uuid4().hex; content=b'campo;valor\ncontrole;1\n'
        raw=('--'+b+'\r\nContent-Disposition: form-data; name="request"\r\nContent-Type: application/json\r\n\r\n').encode()+json.dumps(payload).encode()+('\r\n--'+b+'\r\nContent-Disposition: form-data; name="field.brief"; filename="brief.csv"\r\nContent-Type: text/csv\r\n\r\n').encode()+content+('\r\n--'+b+'--\r\n').encode()
        return c.request('/tickets',method='POST',expected=expected,raw=raw,content_type='multipart/form-data; boundary='+b)
    c.request('/tickets',body,'POST',400);check('Nome sem arquivo não cria ticket',int(sql("SELECT COUNT(*) FROM tickets WHERE subject='"+body['subject']+"'"))==0)
    old=multipart(body,201);d=detail(old);check('Valores persistidos',json.loads(d['metrics'])['fields']==body['metrics']['fields'],d['fieldLabels']);check('Arquivo recuperado',len(d['attachments'])==1 and c.request('/tickets/'+old['id']+'/attachments/'+d['attachments'][0]['id']+'/download')==b'campo;valor\ncontrole;1\n')
    revised=json.loads(json.dumps(fields));revised[0]['label']='Valor revisado';revised[0]['validation']['max']=200
    a.request('/demand-types/'+tp['id'],dict(name=tp['name']+' revisado',approvalRequired=False,evidenceRequired=False,fieldDefinitions=json.dumps(revised)),'PUT')
    fresh=json.loads(json.dumps(body));fresh['subject']=tag+' CT09 novo';fresh['metrics']['fields'].update(budget='200',choice='Nao',context='')
    new=multipart(fresh,201);nd=detail(new);check('Rótulos e valores antigos preservados',detail(old)['fieldLabels']==d['fieldLabels'] and json.loads(detail(old)['metrics'])==json.loads(d['metrics']))
    check('Novo ticket aplica rótulo e limite revisados',nd['fieldLabels']['budget']=='Valor revisado' and json.loads(nd['metrics'])['fields']['budget']=='200',nd['fieldLabels'])
    check('Condição falsa dispensa campo',json.loads(nd['metrics'])['fields']['choice']=='Nao')
    start(old);triage(old,tp);action(manager,old,'execution',{'actionDescription':'Sem evidência'},400);action(manager,old,'execution',{'actionDescription':'Material antigo','evidenceUrl':'https://example.test/material'});check('Regras antigas aplicadas',detail(old)['ticket']['status']=='VALIDATION')
    start(new);triage(new,tp);action(manager,new,'execution',{'actionDescription':'Conclusão revisada'});check('Regras novas aplicadas',detail(new)['ticket']['status']=='DONE')
    a.request('/demand-types/'+tp['id']+'/active?value=false',method='POST');invalid=json.loads(json.dumps(fresh));invalid['subject']=tag+' CT09 inativo';multipart(invalid,400)
    check('Inativo não cria ticket e anterior é consultável',int(sql("SELECT COUNT(*) FROM tickets WHERE subject='"+invalid['subject']+"'"))==0 and detail(old)['fieldLabels']==d['fieldLabels'])
run_case('CT-09-complemento-snapshot','Alta',run)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
out=Path(args.output)
if out.exists(): raise SystemExit('Evidência já existe')
out.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8');print(json.dumps(cases,ensure_ascii=False,indent=2))
