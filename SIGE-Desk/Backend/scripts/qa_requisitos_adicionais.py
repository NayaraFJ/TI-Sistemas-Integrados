"""Evidências adicionais de requisitos, somente no ambiente local de QA."""
from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())

def pending_triage():
    tp=new_type(False,False)
    body=dict(clientId=client_id,demandTypeId=tp['id'],pendingCampaign='Campanha sintética pendente',channel='Google Ads',subject=tag+' pendente',description='Dados sintéticos',urgency='LOW',metrics={'fields':{}})
    t=c.request('/tickets',body,'POST',201);start(t);before=detail(t)
    payload=dict(priority='HIGH',assigneeId=manager.me['id'],campaignId=campaign['id'],demandTypeId=tp['id'],effectiveResponse='Retorno efetivo QA',sendToExecution=True)
    manager.request('/tickets/'+t['id']+'/triage',payload,'POST',404,version=before['ticket']['version'])
    action(staff,t,'triage',{**payload,'campaignId':None},400)
    action(staff,t,'triage',payload);after=detail(t)
    check('Triagem de campanha pendente e histórico',after['ticket']['status']=='EXECUTION' and before['responseCompletedAt'] is None and after['responseCompletedAt'] is not None,dict(ticket=t['id'],before=before,after=after))

def wait_paths():
    for phase in ['TRIAGE','EXECUTION']:
        tp=new_type(False,False);t=create_ticket(tp);start(t)
        if phase=='EXECUTION':triage(t,tp)
        before=detail(t); action(staff if phase=='TRIAGE' else manager,t,'wait',{'reason':'Complemento sintético'}); waiting=detail(t)
        if phase=='TRIAGE':action(c,t,'comments',{'body':'Complemento sintético','effectiveResponse':False},201)
        else:c.upload(t['id'])
        received=detail(t);action(staff,t,'resume',{'reason':'Complemento conferido'});after=detail(t)
        check('Percurso de aguardo '+phase,waiting['waitOrigin']==phase and received['complementReceived'] and received['ticket']['status']=='WAITING_FOR_CLIENT' and after['ticket']['status']==phase and before['responseCompletedAt']==after['responseCompletedAt'],dict(ticket=t['id'],before=before,waiting=waiting,received=received,after=after))

def approval_profiles():
    tp=new_type(True,True);t=create_ticket(tp);start(t);triage(t,tp)
    action(manager,t,'execution',{'actionDescription':'Entrega sintética','evidenceUrl':'https://example.test/evidencia'})
    before=detail(t)
    for actor in [staff,a,manager]:action(actor,t,'approve',{'reason':'Tentativa indevida'},403)
    after=detail(t)
    check('Decisão restrita ao cliente',after['ticket']['status']=='VALIDATION' and before['ticket']['version']==after['ticket']['version'],dict(ticket=t['id'],before=before,after=after))
    action(c,t,'correction',{'reason':'Correção sintética'});corrected=detail(t)
    check('Correção mantém ciclo e primeira resposta',corrected['resolutionCycle']==before['resolutionCycle'] and corrected['responseCompletedAt']==before['responseCompletedAt'],dict(before=before,after=corrected))

for case,fn in [('CT-02-pendente',pending_triage),('CT-04-percursos',wait_paths),('CT-05-perfis-ciclo',approval_profiles)]:run_case(case,'Média' if case.startswith('CT-04') else 'Alta',fn)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output);output.parent.mkdir(parents=True,exist_ok=True)
if output.exists():raise SystemExit('Evidência já existe; escolha outro nome.')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
