"""Execução HTTP do catálogo CT-01..17, exclusivamente em base local de QA.

Executar em Backend: python scripts/qa_functional.py --output ../docs/qa/execucao.json
A base efetiva é conferida no MySQL; nunca aponta para produção/AWS.
"""
import datetime, hashlib, os, subprocess, time
from pathlib import Path

SCHEMA='sige_desk_qa_20261118'
MYSQL=r'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe'
def sql(query):
    env={**os.environ,'MYSQL_PWD':os.environ.get('SIGE_QA_DB_PASSWORD','123')}
    return subprocess.check_output([MYSQL,'--user=root','--host=127.0.0.1','--batch','--skip-column-names','--default-character-set=utf8mb4',SCHEMA,'--execute='+query],env=env).decode('utf-8').strip()

assert sql('SELECT DATABASE()')==SCHEMA
started=datetime.datetime.now(datetime.timezone.utc).isoformat()
source=Path(__file__).with_name('review_flows.py').read_text(encoding='utf-8')
source=source[:source.index('for name,fn in [')].replace("'target/fixes-uploads'","'target/qa-uploads'")
exec(compile(source,str(Path(__file__).with_name('review_flows.py')),'exec'),globals())
setup=list(results);results=[];cases=[];active_ct=None
def check(name,condition,detail=''):
    results.append(dict(ct=active_ct,name=name,passed=bool(condition),detail=str(detail)))
    if not condition:raise AssertionError(name+': '+str(detail))
def run_case(ct,priority,fn):
    global active_ct,c
    active_ct=ct;begin=len(results);begin_time=datetime.datetime.now(datetime.timezone.utc).isoformat()
    try:
        c=Account('cliente@aurora.demo')
        fn();status='APROVADO';error=None
    except Exception as exc:
        status='REPROVADO';error=str(exc)
    cases.append(dict(id=ct,priority=priority,status=status,startedAt=begin_time,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),assertions=len(results)-begin,error=error))
    print(ct,status,error or '',flush=True)

def all_items(account,path):
    rows=[];page=0
    while True:
        data=account.request(path+('&' if '?' in path else '?')+'size=100&page='+str(page));rows.extend(data['items'])
        if len(rows)>=data['total']:return rows
        page+=1

def ct01():
    tp=new_type(False,False);t=create_ticket(tp);d=detail(t)
    check('Identificador, status, urgência e SLA pendente',t['number'].startswith('SIGE-') and t['status']=='OPEN' and t['urgency']=='HIGH' and t['priority'] is None and d['slaState']=='PENDING_CLASSIFICATION')
    payload=dict(clientId=client_id,demandTypeId=tp['id'],pendingCampaign='Campanha QA pendente',channel='Google Ads',subject=tag+' pendente',description='Dados sintéticos',urgency='LOW',desiredDate='2027-01-15',metrics={'fields':{}})
    pending=c.request('/tickets',payload,'POST',201);check('Campanha pendente e prazo preservados',detail(pending)['pendingCampaign']==payload['pendingCampaign'] and detail(pending)['desiredDate']==payload['desiredDate'])
    payload['subject']='';c.request('/tickets',payload,'POST',400)
    represented=staff.request('/tickets',dict(clientId=client_id,campaignId=campaign['id'],requesterId=c.me['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag+' representado',description='Registro por Atendimento',urgency='LOW',metrics={'fields':{}}),'POST',201)
    represented_detail=detail(represented);check('Autor e solicitante distintos',represented_detail['authorName']!=represented_detail['requesterName'])
    foreign_campaign=next(x for x in refs['campaigns'] if x['clientId']!=client_id)
    invalid=dict(clientId=client_id,campaignId=foreign_campaign['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag+' inválido',description='Vínculo inválido',urgency='LOW',metrics={'fields':{}})
    c.request('/tickets',invalid,'POST',400)

def ct02():
    tp=new_type();t=create_ticket(tp);start(t)
    check('Início da triagem não encerra primeira resposta',detail(t)['responseCompletedAt'] is None)
    triage(t,tp);d=detail(t)
    check('Classificação, responsável, prazos e retorno',d['ticket']['status']=='EXECUTION' and d['ticket']['priority']=='HIGH' and d['ticket']['assigneeName'] and d['responseDueAt'] and d['resolutionDueAt'] and d['responseCompletedAt'])
    check('Retorno efetivo persistido',any(x['effectiveResponse'] for x in d['comments']))
    unclassified=create_ticket(tp);start(unclassified)
    payload=dict(priority='HIGH',assigneeId=manager.me['id'],campaignId=campaign['id'],demandTypeId=tp['id'],effectiveResponse='Retorno QA',sendToExecution=True)
    for missing in ['priority','assigneeId','campaignId']:
        invalid={**payload,missing:None};action(staff,unclassified,'triage',invalid,400)
    action(c,unclassified,'triage',payload,403)

def ct03():
    tp=new_type(False,False);t=create_ticket(tp);start(t);triage(t,tp)
    action(manager,t,'comments',{'body':'Comentário QA','effectiveResponse':False},201)
    uploaded=manager.upload(t['id']);check('Anexo do Gestor recuperável pelo Cliente',c.request('/tickets/'+t['id']+'/attachments/'+uploaded['id']+'/download')==b'coluna;valor\ncontrole;1\n')
    action(c,t,'execution',{'actionDescription':'Tentativa indevida'},403)
    action(manager,t,'execution',{'actionDescription':'Ação QA executada'})
    check('Gestor atualiza fluxo e histórico',detail(t)['ticket']['status']=='DONE' and any(h['action']=='EXECUTION_RECORDED' for h in detail(t)['history']))

def ct04():
    for phase in ['TRIAGE','EXECUTION']:
        tp=new_type(False,False);t=create_ticket(tp);start(t)
        actor=staff
        if phase=='EXECUTION':triage(t,tp);actor=manager
        action(actor,t,'wait',{'reason':'Falta dado obrigatório QA'});d=detail(t)
        check('Origem e motivo do aguardo '+phase,d['waitOrigin']==phase and d['waitReason']=='Falta dado obrigatório QA')
        action(staff,t,'resume',{'reason':'Sem informação suficiente'},409)
        c.upload(t['id']);check('Anexo recebido mantém aguardo',detail(t)['complementReceived'] and detail(t)['ticket']['status']=='WAITING_FOR_CLIENT')
        if phase=='EXECUTION':action(staff,t,'cancel',{'reason':'Indevido após executar'},409)
        action(staff,t,'resume',{'reason':'Complemento QA conferido'})
        check('Retoma fase original '+phase,detail(t)['ticket']['status']==phase)

def ct06():
    expired=a.request('/tickets?overdue=true&size=100');check('Existe fixture vencida',len(expired['items'])>0)
    check('Filtro somente vencidos',all(t['overdue'] for t in expired['items']))
    pending=create_ticket(new_type(False,False));check('Pendente de classificação não vencido',not pending['overdue'])
    for account in [c,other,manager]:
        rows=account.request('/tickets?overdue=true&size=100')['items'];check('Vencidos respeitam escopo '+account.email,all(t['overdue'] for t in rows))
    check('Período sem resultados',a.request('/tickets?createdFrom=2099-01-01&createdTo=2099-01-02')['total']==0)
    tp=new_type(False,False);selected=create_ticket(tp);start(selected);triage(selected,tp)
    filters={'clientId':client_id,'campaignId':campaign['id'],'demandTypeId':tp['id'],'priority':'HIGH','assigneeId':manager.me['id'],'status':'EXECUTION'}
    sql_columns={'clientId':'client_id','campaignId':'campaign_id','demandTypeId':'demand_type_id','priority':'priority','assigneeId':'assignee_id','status':'status'}
    for combination in [[(key,value)] for key,value in filters.items()]+[list(filters.items())]:
        query=urllib.parse.urlencode(combination)
        where=' AND '.join(sql_columns[key]+"='"+value+"'" for key,value in combination)
        expected=set(sql('SELECT id FROM tickets WHERE '+where).splitlines())
        obtained={row['id'] for row in all_items(a,'/tickets?'+query)}
        check('Filtro confrontado com MySQL '+query,obtained==expected,dict(expected=sorted(expected),obtained=sorted(obtained)))

def ct07():
    # Fixture exclusiva: registros anteriores e dados demonstrativos não são oráculo.
    tp=new_type(True,True);t=create_ticket(tp);start(t);triage(t,tp);assigned_due=detail(t)['resolutionDueAt']
    action(manager,t,'comments',{'body':'Comentário CT07 exclusivo','effectiveResponse':False},201)
    action(manager,t,'wait',{'reason':'Informação CT07 exclusiva'})
    action(c,t,'comments',{'body':'Complemento CT07 exclusivo','effectiveResponse':False},201)
    action(staff,t,'resume',{'reason':'Complemento conferido CT07'})
    # Vencimento controlado somente na base e ticket sintéticos desta execução.
    sql("UPDATE tickets SET resolution_due_at=UTC_TIMESTAMP(6)-INTERVAL 5 MINUTE WHERE id='"+t['id']+"'")
    def rows(ticket=t):
        raw=sql("SELECT JSON_OBJECT('id',n.id,'userId',n.user_id,'event',n.event_type,'summary',n.summary) FROM notifications n JOIN tickets t ON t.id=n.ticket_id WHERE t.id='"+ticket['id']+"'")
        return [json.loads(x) for x in raw.splitlines() if x]
    deadline=time.monotonic()+70
    while not any(x['event']=='SLA_OVERDUE' for x in rows()) and time.monotonic()<deadline:time.sleep(1)
    action(manager,t,'execution',{'actionDescription':'Material CT07 executado','evidenceUrl':'https://example.test/ct07-material'})
    action(c,t,'approve',{'reason':'Aprovação CT07 exclusiva'})
    action(c,t,'reopen',{'reason':'Novo ciclo CT07 exclusivo'})
    cancelled=create_ticket(new_type(False,False));start(cancelled);action(staff,cancelled,'cancel',{'reason':'Cancelamento CT07 exclusivo'})
    observed=rows()+rows(cancelled);required={'ASSIGNED','COMMENT_ADDED','WAITING_FOR_CLIENT','COMPLEMENT_RECEIVED','TICKET_RESUMED','VALIDATION_REQUESTED','SLA_OVERDUE','DONE','REOPENED','CANCELLED'}
    check('Eventos novos da fixture CT07',required.issubset({x['event'] for x in observed}),observed)
    expected_users={a.me['id'],staff.me['id'],manager.me['id'],c.me['id']}
    allowed={u['id'] for u in users if u['active'] and (u['role'] in ['ADMIN','SERVICE'] or u['id'] in [manager.me['id'],c.me['id']])}
    for event in required:
        recipients={x['userId'] for x in observed if x['event']==event}
        mandatory=expected_users-{manager.me['id']} if event=='CANCELLED' else expected_users
        check('Destinatários obrigatórios/escopo '+event,mandatory.issubset(recipients) and recipients.issubset(allowed),dict(ticketId=t['id'],recipients=sorted(recipients)))
    first_overdue=len([x for x in observed if x['event']=='SLA_OVERDUE'])
    # A próxima execução real do scheduler não deve repetir o prazo/ciclo já avisado.
    for _ in range(62):time.sleep(1)
    check('Job repetido não duplica aviso no mesmo prazo/ciclo',len([x for x in rows() if x['event']=='SLA_OVERDUE'])==first_overdue)
    triage(t,tp)
    sql("UPDATE tickets SET resolution_due_at=UTC_TIMESTAMP(6)-INTERVAL 5 MINUTE WHERE id='"+t['id']+"'")
    deadline=time.monotonic()+70
    while len([x for x in rows() if x['event']=='SLA_OVERDUE'])==first_overdue and time.monotonic()<deadline:time.sleep(1)
    cycles=sql("SELECT cycle FROM ticket_deadline_notices WHERE ticket_id='"+t['id']+"' AND kind='RESOLUTION' ORDER BY cycle")
    check('Novo ciclo gera novo aviso de vencimento',cycles.splitlines()==['1','2'] and len([x for x in rows() if x['event']=='SLA_OVERDUE'])==first_overdue*2,cycles)
    # Conteúdo mínimo do Anexo D: preservar o obtido mesmo em reprovação.
    due_date=assigned_due[:10];due_br='/'.join(reversed(due_date.split('-')))
    expected_tokens={'ASSIGNED':[['HIGH','Alta'],[tag],[due_date,due_br]], 'WAITING_FOR_CLIENT':[['Informação CT07 exclusiva'],['EXECUTION','execução'],['paus','suspens']], 'TICKET_RESUMED':[['Complemento conferido CT07'],['EXECUTION','execução'],['prazo']], 'VALIDATION_REQUESTED':[['Material CT07 executado'],['https://example.test/ct07-material'],['aprov','corrig']], 'SLA_OVERDUE':[['HIGH','Alta'],['resolução'],['min','hora','segundo']], 'DONE':[['Aprovação CT07 exclusiva'],['1'],['conclu','DONE']], 'REOPENED':[['Novo ciclo CT07 exclusivo'],['2']], 'CANCELLED':[['Cancelamento CT07 exclusivo'],[staff.me['name']],['confirm']]}
    for event,token_groups in expected_tokens.items():
        summaries=[x['summary'] for x in observed if x['event']==event]
        check('Conteúdo mínimo de cada notificação '+event,bool(summaries) and all(all(any(token.casefold() in summary.casefold() for token in group) for group in token_groups) for summary in summaries),summaries)
    complements=[x for x in observed if x['event']=='COMPLEMENT_RECEIVED']
    check('Complemento: cada aviso identifica ticket, informação, fase e prazo aplicável',bool(complements) and all(all(token.casefold() in x['summary'].casefold() for token in [t['number'],tag,'Complemento CT07 exclusivo','EXECUTION',assigned_due[:10]]) and 'confer' in x['summary'].casefold() and 'prazo' in x['summary'].casefold() for x in complements),complements)
    comment_summaries=[x['summary'] for x in observed if x['event']=='COMMENT_ADDED']
    check('Conteúdo de cada comentário corresponde ao seu autor',bool(comment_summaries) and all((manager.me['name'] in s and 'Comentário CT07 exclusivo' in s) or (c.me['name'] in s and 'Complemento CT07 exclusivo' in s) for s in comment_summaries),comment_summaries)
    check('Comentário do Cliente também tem autor e resumo',any(x['event']=='COMMENT_ADDED' and c.me['name'] in x['summary'] and 'Complemento CT07 exclusivo' in x['summary'] for x in observed),[x for x in observed if x['event']=='COMMENT_ADDED'])
    for account in [c,other,manager]:
        visible={t['id'] for t in all_items(account,'/tickets')}
        notes=all_items(account,'/notifications')
        check('Notificações só apontam tickets autorizados '+account.email,all(n['ticketId'] in visible for n in notes))

def ct08():
    t=create_ticket(new_type(False,False));att=c.upload(t['id']);other.request('/tickets/'+t['id'],expected=404);other.request('/tickets/'+t['id']+'/attachments',expected=404);other.request('/tickets/'+t['id']+'/attachments/'+att['id']+'/download',expected=404)
    tables=set(sql('SHOW TABLES').splitlines())
    check('Tentativa negada tem mecanismo persistente de auditoria','security_access_events' in tables)
    count=int(sql("SELECT COUNT(*) FROM security_access_events WHERE event_code='SECURITY_TICKET_ACCESS_DENIED' AND actor_id='"+other.me['id']+"' AND resource_id='"+t['id']+"'"));check('Negação foi registrada para o ticket',count>=3,count)
    before=detail(t)['ticket']['version']
    other.request('/tickets/'+t['id']+'/comments',{'body':'Mutação indevida','effectiveResponse':False},'POST',404,version=before)
    other_manager.request('/tickets/'+t['id'],expected=404)
    other_manager.request('/tickets/'+t['id']+'/attachments/'+att['id']+'/download',expected=404)
    other_manager.request('/tickets/'+t['id']+'/comments',{'body':'Mutação Gestor indevida','effectiveResponse':False},'POST',404,version=before)
    for account in [other,other_manager]:
        check('Busca por número não amplia escopo '+account.email,account.request('/tickets?search='+t['number'])['total']==0)
        check('Filtro clientId não amplia escopo '+account.email,all(row['id']!=t['id'] for row in all_items(account,'/tickets?clientId='+client_id)))
    forbidden=dict(clientId=other.me['clientId'],pendingCampaign='Ampliação indevida',demandTypeId=t['demandTypeId'],channel='Google Ads',subject=tag+' outra organização',description='Tentativa indevida',urgency='LOW',metrics={'fields':{}})
    c.request('/tickets',forbidden,'POST',403)
    check('Mutação indevida preserva versão',detail(t)['ticket']['version']==before)

def ct09():
    typed_fields_and_atomic_upload()
    fields=[dict(name='context',label='Rótulo original QA',type='TEXT',required=True,validation={'minLength':3})]
    tp=a.request('/demand-types',dict(name=tag+' snapshot rótulos',approvalRequired=True,evidenceRequired=True,fieldDefinitions=json.dumps(fields)),'POST',201)
    body=dict(clientId=client_id,campaignId=campaign['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag+' snapshot',description='Snapshot sintético',urgency='LOW',metrics={'fields':{'context':'Texto original'}})
    t=c.request('/tickets',body,'POST',201);before=detail(t)
    fields[0]['label']='Rótulo revisado QA'
    a.request('/demand-types/'+tp['id'],dict(name=tp['name'],approvalRequired=False,evidenceRequired=False,fieldDefinitions=json.dumps(fields)),'PUT')
    after=detail(t);check('Rótulos e valores anteriores preservados',before['fieldLabels']==after['fieldLabels'] and after['fieldLabels']['context']=='Rótulo original QA' and json.loads(after['metrics'])['fields']['context']=='Texto original')
    invalid=json.loads(json.dumps(body));invalid['metrics']['fields']['unknown']='Valor indevido';c.request('/tickets',invalid,'POST',400)
    invalid=json.loads(json.dumps(body));invalid['metrics']['fields']={};c.request('/tickets',invalid,'POST',400)
    a.request('/demand-types/'+tp['id']+'/active?value=false',method='POST');c.request('/tickets',body,'POST',400)
    check('Ticket anterior consultável após inativar tipo',detail(t)['fieldLabels']==before['fieldLabels'])

def ct10():
    tp=new_type(False,False);default=next(x for x in a.request('/sla-rules') if x['scope']=='DEFAULT' and x['active'])
    def rule(scope,client=None,demand=None):
        body={**default,'name':tag+' SLA '+scope,'scope':scope,'clientId':client,'demandTypeId':demand,'holidays':'["2026-12-24"]','pauseInValidation':True}
        return a.request('/sla-rules',body,'POST',201)
    rule('DEMAND_TYPE',demand=tp['id']);rule('CLIENT',client=client_id);specific=rule('CLIENT_AND_DEMAND_TYPE',client_id,tp['id'])
    t=create_ticket(tp);start(t);triage(t,tp)
    snap=json.loads(sql("SELECT sla_rule_snapshot FROM tickets WHERE id='"+t['id']+"'"))
    check('Precedência cliente e tipo',snap['id']==specific['id'])
    check('Calendário completo congelado',snap['timezone']==specific['timezone'] and '2026-12-24' in snap['holidays'] and snap['pauseInValidation'] and snap['nationalHolidayPolicy']=='BR_FIXED_V1')
    a.request('/sla-rules/'+specific['id'],{**specific,'holidays':'["2026-12-31"]','pauseInValidation':False},'PUT')
    old=json.loads(sql("SELECT sla_rule_snapshot FROM tickets WHERE id='"+t['id']+"'"));check('Ticket existente preserva snapshot',old==snap)
    fresh=create_ticket(tp);start(fresh);triage(fresh,tp);new=json.loads(sql("SELECT sla_rule_snapshot FROM tickets WHERE id='"+fresh['id']+"'"));check('Novo ticket aplica revisão',new['version']>snap['version'] and new['holidays']==['2026-12-31'])
    # Organização/tipo exclusivos para não depender de regras de rodadas anteriores.
    slug=tag.lower().replace(' ','-')
    org=a.request('/clients',dict(name=tag+' precedência',contactName='Contato QA',email=slug+'-sla@example.test',phone=None),'POST',201)
    requester=a.request('/users',dict(name=tag+' solicitante SLA',email=slug+'-sla-user@example.test',password='ReviewPass123!',role='CLIENT',clientId=org['id']),'POST',201)
    camp=staff.request('/campaigns',dict(clientId=org['id'],name=tag+' campanha SLA',channel='Google Ads',objective='Precedência QA'),'POST',201)
    isolated_type=new_type(False,False)
    def classify():
        ticket=staff.request('/tickets',dict(clientId=org['id'],campaignId=camp['id'],requesterId=requester['id'],demandTypeId=isolated_type['id'],channel='Google Ads',subject=tag+' precedência',description='SLA QA',urgency='LOW',metrics={'fields':{}}),'POST',201)
        start(ticket);action(staff,ticket,'triage',dict(priority='HIGH',assigneeId=manager.me['id'],campaignId=camp['id'],demandTypeId=isolated_type['id'],effectiveResponse='Classificação SLA QA',sendToExecution=True))
        snapshot=json.loads(sql("SELECT sla_rule_snapshot FROM tickets WHERE id='"+ticket['id']+"'"));return ticket,snapshot
    _,fallback=classify();defaults={r['id'] for r in a.request('/sla-rules') if r['scope']=='DEFAULT' and r['active']};check('Fallback padrão quando sem regra específica',fallback['id'] in defaults)
    type_rule=rule('DEMAND_TYPE',demand=isolated_type['id']);_,typed=classify();check('Regra tipo prevalece sobre padrão',typed['id']==type_rule['id'])
    client_rule=rule('CLIENT',client=org['id']);_,client_snapshot=classify();check('Regra cliente prevalece sobre tipo',client_snapshot['id']==client_rule['id'])
    combined=rule('CLIENT_AND_DEMAND_TYPE',org['id'],isolated_type['id']);old_ticket,combined_snapshot=classify();check('Regra combinada prevalece sobre cliente',combined_snapshot['id']==combined['id'])
    a.request('/sla-rules/'+combined['id'],{**combined,'active':False},'PUT');check('Inativar regra preserva snapshot anterior',json.loads(sql("SELECT sla_rule_snapshot FROM tickets WHERE id='"+old_ticket['id']+"'"))==combined_snapshot)
    action(manager,old_ticket,'execution',{'actionDescription':'Encerrar ciclo SLA QA'});before=a.request('/tickets/'+old_ticket['id'])
    action(a,old_ticket,'reopen',{'reason':'Novo ciclo com regra vigente'});action(staff,old_ticket,'triage',dict(priority='HIGH',assigneeId=manager.me['id'],campaignId=camp['id'],demandTypeId=isolated_type['id'],effectiveResponse='Nova classificação SLA QA',sendToExecution=True))
    after=a.request('/tickets/'+old_ticket['id']);cycle_snapshot=json.loads(sql("SELECT sla_rule_snapshot FROM tickets WHERE id='"+old_ticket['id']+"'"));check('Novo ciclo aplica regra ativa e preserva primeira resposta',after['resolutionCycle']==2 and cycle_snapshot['id']==client_rule['id'] and after['responseDueAt']==before['responseDueAt'])

def ct11():notifications_and_auth()
def ct12():
    catalog_guards();catalog_roundtrip()
    slug=tag.lower().replace(' ','-')
    org=a.request('/clients',dict(name=tag+' continuidade',contactName='Contato QA',email=slug+'-org@example.test',phone=None),'POST',201)
    camp=staff.request('/campaigns',dict(clientId=org['id'],name=tag+' campanha continuidade',channel='Google Ads',objective='Continuidade QA'),'POST',201)
    def user(suffix,role,org_id=None):
        return a.request('/users',dict(name=tag+' '+suffix,email=slug+'-'+suffix+'@example.test',password='ReviewPass123!',role=role,clientId=org_id),'POST',201)
    first=user('primeiro','CLIENT',org['id']);client_account=Account(first['email'],'ReviewPass123!');tp=new_type(False,False)
    body=dict(clientId=org['id'],campaignId=camp['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag+' continuidade',description='Continuidade QA',urgency='LOW',metrics={'fields':{}})
    t=client_account.request('/tickets',body,'POST',201);start(t);action(staff,t,'wait',{'reason':'Cliente precisa complementar para continuar'})
    a.request('/users/'+first['id'],{**first,'active':False},'PUT',409)
    backup=user('substituto','CLIENT',org['id']);backup_account=Account(backup['email'],'ReviewPass123!')
    a.request('/users/'+first['id'],{**first,'active':False},'PUT');check('Cliente substituto mantém acesso após inativação permitida',backup_account.request('/tickets/'+t['id'])['ticket']['id']==t['id'])
    action(backup_account,t,'comments',{'body':'Complemento pelo substituto','effectiveResponse':False},201);action(staff,t,'resume',{'reason':'Substituto respondeu'})
    assigned=user('gestor-temporario','TRAFFIC_MANAGER')
    payload=dict(priority='HIGH',assigneeId=assigned['id'],campaignId=camp['id'],demandTypeId=tp['id'],effectiveResponse='Triagem continuidade',sendToExecution=True)
    action(staff,t,'triage',payload);a.request('/users/'+assigned['id'],{**assigned,'active':False},'PUT',409)
    action(staff,t,'reassign',dict(assigneeId=manager.me['id'],reason='Liberar responsável temporário'))
    a.request('/users/'+assigned['id'],{**assigned,'active':False},'PUT');check('Reatribuição permite inativação com continuidade',manager.request('/tickets/'+t['id'])['ticket']['assigneeName']==manager.me['name'])
    staff.request('/campaigns/'+camp['id']+'/active?value=false',method='POST');client_account=backup_account;client_account.request('/tickets',body,'POST',400)
    check('Ticket anterior consultável com campanha inativa',backup_account.request('/tickets/'+t['id'])['ticket']['campaignId']==camp['id'])
    a.request('/clients/'+org['id']+'/active?value=false',method='POST',expected=409)
def ct13():
    concurrency_and_reassignment()
    row=a.request('/tickets?search='+urllib.parse.quote(tag)+'&size=100')['items'][0];history=a.request('/tickets/'+row['id'])['history']
    check('Autor e data presentes em todos eventos',all(x['actorName'] and x['createdAt'] for x in history))
    changed=[x for x in history if x['fieldName']];check('Campos alterados conservam novo valor e motivo',all(x['newValue'] is not None and x['reason'] for x in changed),changed)
    tp=new_type(True,True);t=create_ticket(tp);start(t);triage(t,tp);classified=detail(t)
    def event(action_name):return next(x for x in detail(t)['history'] if x['action']==action_name)
    def decoded(value):return json.loads(value) if value is not None else None
    for action_name,old_value,new_value in [('TRIAGE_CLASSIFIED',None,'HIGH'),('ASSIGNEE_CHANGED',None,manager.me['id']),('SENT_TO_EXECUTION','TRIAGE','EXECUTION'),('RESPONSE_DEADLINE_CHANGED',None,classified['responseDueAt']),('RESOLUTION_DEADLINE_CHANGED',None,classified['resolutionDueAt'])]:
        h=event(action_name);check('Auditoria confere estado real '+action_name,decoded(h['oldValue'])==old_value and decoded(h['newValue'])==new_value and h['actorName']==staff.me['name'],h)
    prior={h['id'] for h in classified['history']}
    action(staff,t,'reassign',dict(assigneeId=other_manager.me['id'],reason='Reatribuição auditável QA'))
    h=event('ASSIGNEE_CHANGED');check('Auditoria confere responsável anterior/novo',decoded(h['oldValue'])==manager.me['id'] and decoded(h['newValue'])==other_manager.me['id'] and h['reason']=='Reatribuição auditável QA',h)
    action(other_manager,t,'execution',{'actionDescription':'Execução auditável QA','evidenceUrl':'https://example.test/audit'})
    action(c,t,'approve',{'reason':'Decisão auditável QA'});h=event('APPROVED');check('Decisão auditada com ator e motivo',decoded(h['oldValue'])=='VALIDATION' and decoded(h['newValue'])=='DONE' and h['actorName']==c.me['name'] and h['reason']=='Decisão auditável QA',h)
    action(c,t,'reopen',{'reason':'Reabertura auditável QA'});h=event('RESOLUTION_CYCLE_CLOSED');closed=decoded(h['newValue']);check('Ciclo anterior congelado na auditoria',closed['cycle']==1 and closed['resolutionDueAt'] and closed['slaSnapshot'],closed)
    current=detail(t)['history'];check('Eventos anteriores preservados após decisões e reabertura',prior.issubset({h['id'] for h in current}) and all(h['createdAt'] and h['actorName'] for h in current))
def ct14():
    files_and_cancel()
    for state in ['OPEN','TRIAGE']:
        t=create_ticket(new_type(False,False))
        if state=='TRIAGE':start(t)
        action(staff,t,'cancel',{'reason':''},400);action(c,t,'cancel',{'reason':'Sem perfil'},403)
        action(staff,t,'cancel',{'reason':'Cancelamento QA '+state})
        check('Estado terminal de cancelamento '+state,detail(t)['ticket']['status']=='CANCELLED')
        action(staff,t,'resume',{'reason':'Retomar indevidamente'},409)
        boundary='qa'+uuid.uuid4().hex
        body=('--'+boundary+'\r\nContent-Disposition: form-data; name="file"; filename="cancelado.csv"\r\nContent-Type: text/csv\r\n\r\nvalor\n1\r\n--'+boundary+'--\r\n').encode()
        c.request('/tickets/'+t['id']+'/attachments',method='POST',expected=409,raw=body,content_type='multipart/form-data; boundary='+boundary)
def ct15():
    pagination_and_aggregates()
    for account in [a,c,other,staff,manager,other_manager]:
        dashboard=account.request('/dashboard')
        for bucket in dashboard['statusDistribution']:
            count=account.request('/tickets?size=1&status='+bucket['status'])['total']
            check('Cada grupo por status confere '+account.email+' '+bucket['status'],bucket['count']==count,dict(expected=count,obtained=bucket['count']))
def ct16():
    tp=new_type(False,False);metrics=dict(period='QA novembro',impressions=1000,ctr=2.5,cpc=1.2,conversions=10,cpa=12,roas=3.5,fields={})
    body=dict(clientId=client_id,campaignId=campaign['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag+' métricas',description='Métricas sintéticas',urgency='LOW',metrics=metrics)
    t=c.request('/tickets',body,'POST',201);saved=json.loads(detail(t)['metrics']);check('Todas métricas preservadas',all(saved[k]==v for k,v in metrics.items()))
    fresh_session=Account(c.email);check('Métricas preservadas em nova sessão',json.loads(fresh_session.request('/tickets/'+t['id'])['metrics'])==saved)
    body['metrics']['ctr']=-1;c.request('/tickets',body,'POST',400)
    body['metrics']={'period':'Zero e vazio','impressions':0,'ctr':None,'fields':{}};zero=c.request('/tickets',body,'POST',201);saved=json.loads(detail(zero)['metrics']);check('Zero difere de métrica ausente',saved['impressions']==0 and saved.get('ctr') is None)
def ct17():
    reference_and_report()
    selection='/tickets?search='+urllib.parse.quote(tag)
    expected={t['number'] for t in all_items(a,selection)}
    for account in [a,staff]:
        exported=account.request('/reports/tickets/export?search='+urllib.parse.quote(tag));rows=list(csv.reader(io.StringIO(exported.decode('utf-8-sig')),delimiter=';'))
        check('CSV cobre exatamente seleção inteira '+account.email,{r[0] for r in rows[1:]}==expected)
        empty=account.request('/reports/tickets/export?search=QA-SEM-RESULTADO-EXCLUSIVO');check('CSV vazio preserva cabeçalho',len(list(csv.reader(io.StringIO(empty.decode('utf-8-sig')),delimiter=';')))==1)
    manager.request('/reports/tickets/export',expected=403)

suite=[('CT-01','Alta',ct01),('CT-02','Alta',ct02),('CT-03','Alta',ct03),('CT-04','Média',ct04),('CT-05','Alta',lambda:(main_flow(),no_approval())),('CT-06','Média',ct06),('CT-07','Alta',ct07),('CT-08','Alta',ct08),('CT-09','Alta',ct09),('CT-10','Alta',ct10),('CT-11','Alta',ct11),('CT-12','Alta',ct12),('CT-13','Alta',ct13),('CT-14','Média',ct14),('CT-15','Média',ct15),('CT-16','Baixa',ct16),('CT-17','Baixa',ct17)]
# Cancelamento precisa produzir seu evento antes de CT07.
for ct,priority,fn in suite:
    if ct=='CT-07':
        c=Account('cliente@aurora.demo');t=create_ticket(new_type(False,False));action(staff,t,'cancel',{'reason':'Fixture notificacao cancelada'})
    run_case(ct,priority,fn)
passed=sum(x['status']=='APROVADO' for x in cases);high=[x for x in cases if x['priority']=='Alta'];high_passed=sum(x['status']=='APROVADO' for x in high)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag,executor='qa_execucao',retention='Base e uploads sintéticos preservados exclusivamente para auditoria desta execução.',layer='HTTP + MySQL; interface e variantes determinísticas avaliadas separadamente'),cases=cases,checks=results,setupChecks=setup,summary=dict(total=len(cases),passed=passed,failed=len(cases)-passed,percent=passed/len(cases)*100,highTotal=len(high),highPassed=high_passed,criticalOpen='requires independent defect review',numericGate=high_passed==len(high) and passed/len(cases)>=0.9))
output=Path(args.output);output.parent.mkdir(parents=True,exist_ok=True);output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(report['summary'],ensure_ascii=False));raise SystemExit(0 if report['summary']['numericGate'] else 1)
