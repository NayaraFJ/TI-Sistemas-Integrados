"""Variantes CT-01 de abertura inválida em API e MySQL isolados."""
from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
def ct01_extra():
    tp=new_type(False,False)
    camp=staff.request('/campaigns',dict(clientId=client_id,name=tag+' inativa',channel='Google Ads',objective='Teste QA'),'POST',201)
    staff.request('/campaigns/'+camp['id']+'/active?value=false',method='POST')
    foreign=next(x for x in refs['campaigns'] if x['clientId']!=client_id)
    slug=tag.lower().replace(' ','-')
    org=a.request('/clients',dict(name=tag+' inativa',contactName='QA',email=slug+'@example.test',phone=None),'POST',201)
    a.request('/clients/'+org['id']+'/active?value=false',method='POST')
    base=dict(clientId=client_id,campaignId=campaign['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag+' invalida',description='Dados sintéticos',urgency='LOW',metrics={'fields':{}})
    for label,changes,actor in [('obrigatorio',{'description':''},c),('campanha estrangeira',{'campaignId':foreign['id']},c),('campanha inativa',{'campaignId':camp['id']},c),('cliente inativo',{'clientId':org['id'],'campaignId':None,'pendingCampaign':'Pendente','requesterId':c.me['id']},staff)]:
        body={**base,**changes,'subject':tag+' '+label}; before=int(sql("SELECT COUNT(*) FROM tickets WHERE subject='"+body['subject']+"'"))
        actor.request('/tickets',body,'POST',400)
        after=int(sql("SELECT COUNT(*) FROM tickets WHERE subject='"+body['subject']+"'"))
        check('Sem ticket parcial '+label,before==after,dict(subject=body['subject'],before=before,after=after))
run_case('CT-01-negativas-adicionais','Alta',ct01_extra)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output);output.parent.mkdir(parents=True,exist_ok=True)
if output.exists():raise SystemExit('Evidência já existe; escolha outro nome.')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
