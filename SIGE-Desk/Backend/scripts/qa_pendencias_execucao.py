"""Complementos CT-03/09/14 na API e schema locais de QA."""
from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())

def execution_profiles():
    tp=new_type(False,False); t=create_ticket(tp); start(t); triage(t,tp)
    action(manager,t,'comments',{'body':'Material sintético persistente','effectiveResponse':False},201)
    upload=manager.upload(t['id']); before=detail(t)
    for actor in [c,staff,a]:
        before=detail(t)
        action(actor,t,'execution',{'actionDescription':'Tentativa não autorizada'},403)
        after=detail(t)
        keys=['comments','attachments','history']
        check('Negativa preserva status e recursos '+actor.me['role'],after['ticket']['status']==before['ticket']['status'] and all(after[k]==before[k] for k in keys),dict(beforeVersion=before['ticket']['version'],afterVersion=after['ticket']['version']))
    fresh=Account(c.email)
    resumed=fresh.request('/tickets/'+t['id'])
    check('Materiais após nova sessão',any(x['body']=='Material sintético persistente' for x in resumed['comments']) and any(x['id']==upload['id'] for x in resumed['attachments']))
    check('Download após nova sessão',fresh.request('/tickets/'+t['id']+'/attachments/'+upload['id']+'/download')==b'coluna;valor\ncontrole;1\n')
    action(manager,t,'execution',{'actionDescription':'Execução autorizada'})
    check('Gestor atribuído conclui',detail(t)['ticket']['status']=='DONE')

def field_negatives():
    fields=[dict(name='number',label='Número',type='NUMBER',required=True,validation={'min':0,'max':100}),dict(name='date',label='Data',type='DATE',required=True),dict(name='choice',label='Opção',type='SELECT',options=['Sim','Não'],required=True),dict(name='context',label='Contexto',type='TEXT',requiredWhen={'field':'choice','equals':'Sim'},validation={'minLength':3})]
    tp=a.request('/demand-types',dict(name=tag+' campos negativos',approvalRequired=True,evidenceRequired=True,fieldDefinitions=json.dumps(fields)),'POST',201)
    body=dict(clientId=client_id,campaignId=campaign['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag+' campos negativos',description='Fixture sintética',urgency='LOW',metrics={'fields':{'number':'10','date':'2026-10-04','choice':'Sim','context':'Contexto válido'}})
    variants=[('obrigatório','number',None),('número','number','abc'),('limite','number','101'),('data','date','2026-02-30'),('opção','choice','Outro'),('condicional','context',''),('desconhecido','unknown','Valor')]
    for label,key,value in variants:
        invalid=json.loads(json.dumps(body)); invalid['subject']=body['subject']+' '+label
        if value is None: del invalid['metrics']['fields'][key]
        else: invalid['metrics']['fields'][key]=value
        c.request('/tickets',invalid,'POST',400)
        check('Rejeição sem ticket parcial '+label,int(sql("SELECT COUNT(*) FROM tickets WHERE subject='"+invalid['subject']+"'"))==0)
    t=c.request('/tickets',body,'POST',201); start(t); triage(t,tp)
    action(c,t,'approve',{'reason':'Decisão prematura'},409)
    action(manager,t,'execution',{'actionDescription':'Sem material'},400)
    action(manager,t,'execution',{'actionDescription':'Material apresentado','evidenceUrl':'https://example.test/material'})
    check('Material precede decisão',detail(t)['ticket']['status']=='VALIDATION')
    action(c,t,'approve',{'reason':'Material aprovado'})
    check('Decisão posterior conclui',detail(t)['ticket']['status']=='DONE')

def cancellation_deadlines():
    t=create_ticket(new_type(False,False)); start(t); before=detail(t)
    action(staff,t,'cancel',{'reason':'Cancelamento sintético após triagem'})
    after=detail(t)
    check('Cancelamento preserva prazos e resposta',all(before[k]==after[k] for k in ['responseDueAt','resolutionDueAt','responseCompletedAt']))
    check('Cancelamento terminal e histórico',after['ticket']['status']=='CANCELLED' and any(x['action']=='CANCELLED' and x['reason']=='Cancelamento sintético após triagem' for x in after['history']))

for case,fn in [('CT-03-perfis-sessao',execution_profiles),('CT-09-negativas-material',field_negatives),('CT-13-auditoria',ct13),('CT-14-prazos',cancellation_deadlines)]: run_case(case,'Alta' if not case.startswith('CT-14') else 'Média',fn)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output); output.parent.mkdir(parents=True,exist_ok=True)
if output.exists(): raise SystemExit('Evidência já existe; escolha outro nome.')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2)); raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
