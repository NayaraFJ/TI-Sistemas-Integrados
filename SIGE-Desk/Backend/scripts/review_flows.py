"""Teste HTTP dos fluxos em instância demo isolada (nunca apontar para a base principal)."""
import argparse, csv, io, json, uuid, urllib.request, urllib.error, urllib.parse, http.cookiejar
from pathlib import Path

parser=argparse.ArgumentParser()
parser.add_argument('--base-url',default='http://localhost:8081/api/v1')
parser.add_argument('--output',default='target/review-api-results.json')
args=parser.parse_args()
if args.base_url!='http://localhost:8081/api/v1': raise SystemExit('Este roteiro aceita somente a instância isolada de revisão na porta 8081.')
results=[]
def check(name,condition,detail=''):
    results.append(dict(name=name,passed=bool(condition),detail=str(detail)))
    if not condition: raise AssertionError(name+': '+str(detail))
class Account:
    def __init__(self,email,password="123"):
        self.email=email;self.jar=http.cookiejar.CookieJar();self.http=urllib.request.build_opener(urllib.request.HTTPCookieProcessor(self.jar));self.token=None
        self.me=self.request('/auth/login',dict(email=email,password=password),'POST')
    def request(self,path,body=None,method='GET',expected=200,raw=None,content_type=None,csrf=True,version="auto"):
        headers={}
        if method!='GET' and path!='/auth/login' and csrf:
            if self.token is None:self.token=self.request('/auth/csrf')['token']
            headers['X-XSRF-TOKEN']=self.token
        if raw is not None: data=raw;headers['Content-Type']=content_type
        elif body is not None:data=json.dumps(body).encode();headers['Content-Type']='application/json'
        else:data=None
        parts=path.split('/')
        if method=='POST' and len(parts)>=4 and parts[1]=='tickets' and parts[3]!='attachments' and csrf:
            if version=='auto':version=self.request('/tickets/'+parts[2])['ticket']['version']
            if version is not None:headers['If-Match']=str(version)
        req=urllib.request.Request(args.base_url+path,data=data,headers=headers,method=method)
        try:
            with self.http.open(req,timeout=10) as response:status=response.status;data=response.read();headers=dict(response.headers)
        except urllib.error.HTTPError as error:status=error.code;data=error.read();headers=dict(error.headers)
        check(self.email+' '+method+' '+path+' HTTP '+str(expected),status==expected,data[:200] if status!=expected else '')
        if not data:return None
        if 'json' in headers.get('Content-Type',''):return json.loads(data)
        return data
    def upload(self,ticket_id,content=b'coluna;valor\ncontrole;1\n'):
        boundary='review'+uuid.uuid4().hex
        body=('--'+boundary+'\r\nContent-Disposition: form-data; name="file"; filename="contexto-review.csv"\r\nContent-Type: text/csv\r\n\r\n').encode()+content+('\r\n--'+boundary+'--\r\n').encode()
        return self.request('/tickets/'+ticket_id+'/attachments',method='POST',expected=201,raw=body,content_type='multipart/form-data; boundary='+boundary)

a=Account('admin@sige.demo');c=Account('cliente@aurora.demo');other=Account('rafael@horizonte.demo');staff=Account('atendimento@sige.demo');manager=Account('gestor@sige.demo');other_manager=Account('gestor2@sige.demo')
refs=a.request('/reference');users=a.request('/users');client_id=c.me['clientId'];campaign=next(item for item in refs['campaigns'] if item['clientId']==client_id);tag='Review '+uuid.uuid4().hex[:8]
def new_type(approval=True,evidence=True):return a.request('/demand-types',dict(name=tag+uuid.uuid4().hex[:5],approvalRequired=approval,evidenceRequired=evidence,fieldDefinitions='[]'),'POST',201)
def create_ticket(type_item,actor=c,subject=None):
    return actor.request('/tickets',dict(clientId=client_id,campaignId=campaign['id'],demandTypeId=type_item['id'],channel='Google Ads',subject=subject or tag,description='Teste fictício da revisão',urgency='HIGH',metrics={'fields':{}}),'POST',201)
def start(ticket):return staff.request('/tickets/'+ticket['id']+'/triage/start',{'reason':'Conferência de teste'},'POST')
def triage(ticket,type_item):return staff.request('/tickets/'+ticket['id']+'/triage',dict(priority='HIGH',assigneeId=manager.me['id'],campaignId=campaign['id'],demandTypeId=type_item['id'],effectiveResponse='Retorno efetivo da revisão',sendToExecution=True),'POST')
def detail(ticket):return c.request('/tickets/'+ticket['id'])
def action(actor,ticket,path,body,expected=200):return actor.request('/tickets/'+ticket['id']+'/'+path,body,'POST',expected)
def scenario(name,fn):
    try:fn();check(name,True)
    except Exception as error:results.append(dict(name=name,passed=False,detail=str(error)))

def main_flow():
    tp=new_type();ticket=create_ticket(tp);check('Abertura sem prioridade oficial',ticket['priority'] is None and ticket['status']=='OPEN')
    other.request('/tickets/'+ticket['id'],expected=404);manager.request('/tickets/'+ticket['id'],expected=404);action(c,ticket,'triage/start',{'reason':'Inválido'},403)
    start(ticket);action(staff,ticket,'wait',{'reason':'Falta contexto'});action(staff,ticket,'resume',{'reason':'Ainda sem complemento'},409)
    action(c,ticket,'comments',{'body':'Contexto recebido por comentário','effectiveResponse':False},201)
    check('Comentário confirma complemento sem retomar',detail(ticket)['complementReceived'] and detail(ticket)['ticket']['status']=='WAITING_FOR_CLIENT')
    action(staff,ticket,'resume',{'reason':'Complemento suficiente'})
    a.request('/demand-types/'+tp['id'],dict(name=tp['name'],approvalRequired=False,evidenceRequired=False,fieldDefinitions='[]'),'PUT')
    triage(ticket,tp);action(a,ticket,'execution',{'actionDescription':'Tentativa indevida'},403);other_manager.request('/tickets/'+ticket['id'],expected=404)
    action(manager,ticket,'execution',{'actionDescription':'Sem evidência'},400)
    action(manager,ticket,'execution',{'actionDescription':'URL inválida','evidenceUrl':'ftp://example.test/arquivo'},400)
    result=action(manager,ticket,'execution',{'actionDescription':'Material submetido','evidenceUrl':'https://example.test/material'})
    check('Snapshot mantém aprovação após editar tipo',result['status']=='VALIDATION')
    action(a,ticket,'approve',{'reason':'Tentativa indevida'},403)
    action(c,ticket,'correction',{'reason':'Ajustar versão'});check('Correção mantém ciclo',detail(ticket)['resolutionCycle']==1)
    action(manager,ticket,'execution',{'actionDescription':'Versão corrigida','evidenceUrl':'https://example.test/material-v2'})
    action(c,ticket,'approve',{'reason':'Versão aprovada'});action(c,ticket,'comments',{'body':'Registro posterior','effectiveResponse':False},201)
    check('Comentário após conclusão mantém status',detail(ticket)['ticket']['status']=='DONE')
    action(c,ticket,'reopen',{'reason':'Novo trabalho identificado'});data=detail(ticket);check('Reabertura inicia ciclo pendente',data['resolutionCycle']==2 and data['resolutionDueAt'] is None and data['responseCompletedAt'] is not None)
    action(manager,ticket,'execution',{'actionDescription':'Sem reconfirmar','evidenceUrl':'https://example.test/pre-triagem'},409);triage(ticket,tp)
    data=detail(ticket);check('Histórico audita responsável, prazo e ciclo',{'ASSIGNEE_CHANGED','RESOLUTION_DEADLINE_CHANGED','RESOLUTION_CYCLE_CLOSED'}.issubset({item['action'] for item in data['history']}))
    action(manager,ticket,'execution',{'actionDescription':'Segundo ciclo','evidenceUrl':'https://example.test/material-v3'});action(c,ticket,'approve',{'reason':'Segundo ciclo aprovado'})

def no_approval():
    tp=new_type(False,False);ticket=create_ticket(tp);start(ticket);triage(ticket,tp);action(manager,ticket,'execution',{'actionDescription':'Análise concluída'})
    data=detail(ticket);check('Conclusão sem aprovação registra dispensa',data['ticket']['status']=='DONE' and any(item['action']=='APPROVAL_WAIVED' for item in data['history']))

def files_and_cancel():
    tp=new_type(False,False);ticket=create_ticket(tp);start(ticket);action(staff,ticket,'wait',{'reason':'Enviar contexto'})
    content=b'coluna;valor\ncontrole;1\n';attachment=c.upload(ticket['id'],content);check('Anexo confirma complemento',detail(ticket)['complementReceived'])
    check('Download mantém o conteúdo',c.request('/tickets/'+ticket['id']+'/attachments/'+attachment['id']+'/download')==content)
    other.request('/tickets/'+ticket['id']+'/attachments/'+attachment['id']+'/download',expected=404)
    action(staff,ticket,'cancel',{'reason':'Cancelamento de teste antes da execução'})
    action(c,ticket,'comments',{'body':'Inválido','effectiveResponse':False},409)
    tp=new_type(False,False);large=create_ticket(tp);c.upload(large['id'],b'valor\n'+b'x'*(2*1024*1024));check('Upload de 2 MB respeita limite de 10 MB',len(detail(large)['attachments'])==1)

def catalog_guards():
    admin_user=next(user for user in users if user['id']==a.me['id']);marina=next(user for user in users if user['id']==c.me['id']);gestor=next(user for user in users if user['id']==manager.me['id'])
    a.request('/users/'+admin_user['id'],{**admin_user,'role':'SERVICE'},'PUT',409)
    a.request('/users/'+marina['id'],{**marina,'active':False},'PUT',409)
    a.request('/users/'+gestor['id'],{**gestor,'role':'SERVICE'},'PUT',409)
    default=next(rule for rule in a.request('/sla-rules') if rule['scope']=='DEFAULT' and rule['active'])
    a.request('/sla-rules/'+default['id'],{**default,'scope':'CLIENT','clientId':client_id},'PUT',409)
    a.request('/sla-rules',{**default,'name':tag,'businessDays':'[]'},'POST',400)
    a.request('/sla-rules',{**default,'name':tag,'businessStart':'18:00:00','businessEnd':'08:00:00'},'POST',400)
    organization=a.request('/clients',dict(name=tag,contactName='Contato de teste',email=tag.lower().replace(' ','-')+'@example.test',phone=None),'POST',201)
    a.request('/clients/'+organization['id'],dict(name=tag+' editado',contactName='Contato de teste',email=tag.lower().replace(' ','-')+'@example.test',phone=None),'PUT')
    a.request('/clients/'+organization['id']+'/active?value=false',method='POST')
    a.request('/campaigns',dict(clientId=organization['id'],name=tag,channel='Google Ads',objective='Teste'),'POST',400)
    c.request('/clients',expected=403);staff.request('/users',expected=403);manager.request('/reports/tickets',expected=403)

def catalog_roundtrip():
    email=tag.lower().replace(' ','-')+'-user@example.test'
    user=a.request('/users',dict(name=tag+' Usuário',email=email,password='ReviewPass123!',role='CLIENT',clientId=client_id),'POST',201)
    account=Account(email,'ReviewPass123!');account.request('/auth/me')
    user=a.request('/users/'+user['id'],dict(name=tag+' Editado',email=email,role='CLIENT',clientId=client_id,active=False),'PUT')
    account.request('/auth/me',expected=403)
    anonymous=Account.__new__(Account);anonymous.email='Conta inativa';anonymous.http=urllib.request.build_opener();anonymous.token=None
    anonymous.request('/auth/login',dict(email=email,password='ReviewPass123!'),'POST',401)
    body=dict(clientId=client_id,name=tag+' Campanha',channel='Google Ads',objective='Teste fictício')
    created=staff.request('/campaigns',body,'POST',201);body['name']+=' editada'
    staff.request('/campaigns/'+created['id'],body,'PUT');staff.request('/campaigns/'+created['id']+'/active?value=false',method='POST')
    check('Campanha inativa sai das referências',created['id'] not in {item['id'] for item in a.request('/reference')['campaigns']})
    tp=new_type(False,False);a.request('/demand-types/'+tp['id']+'/active?value=false',method='POST')
    check('Tipo inativo sai das referências',tp['id'] not in {item['id'] for item in a.request('/reference')['demandTypes']})
    rule=next(item for item in a.request('/sla-rules') if item['scope']=='DEFAULT');body={**rule,'name':tag+' SLA','scope':'CLIENT','clientId':client_id,'active':False}
    created=a.request('/sla-rules',body,'POST',201);body['name']+=' editada';a.request('/sla-rules/'+created['id'],body,'PUT')

def reference_and_report():
    for account in [a,c,other,staff,manager,other_manager]:account.request('/reference');account.request('/dashboard');account.request('/notifications')
    scope=manager.request('/tickets?size=100');options=manager.request('/reference')
    check('Gestor recebe apenas referências do escopo',set(item['id'] for item in options['clients']).issubset({item['clientId'] for item in scope['items']}) and options['clientUsers']==[])
    check('Cliente não recebe outros solicitantes',len(c.request('/reference')['clientUsers'])==1)
    tickets=a.request('/tickets?search='+urllib.parse.quote(tag)+'&size=100');report=a.request('/reports/tickets?search='+urllib.parse.quote(tag))
    exported=a.request('/reports/tickets/export?search='+urllib.parse.quote(tag));rows=list(csv.reader(io.StringIO(exported.decode('utf-8-sig')),delimiter=';'))
    check('CSV, relatório e lista coincidem',len(rows)-1==report['total']==tickets['total'])
    c.request('/reports/tickets/export',expected=403)
    fresh=create_ticket(new_type(False,False),subject='=1+1')
    exported=a.request('/reports/tickets/export?search='+urllib.parse.quote(fresh['number']));rows=list(csv.reader(io.StringIO(exported.decode('utf-8-sig')),delimiter=';'));check('CSV neutraliza fórmula em assunto',rows[1][1].startswith("'="))
    page=a.request('/tickets?page=1&size=10');check('Paginação devolve segunda página',page['page']==1 and len(page['items'])==10)
    a.request('/tickets?page=-1',expected=400)

def concurrency_and_reassignment():
    tp=new_type(False,False);ticket=create_ticket(tp);old=detail(ticket)['ticket']['version'];start(ticket)
    stale=staff.request('/tickets/'+ticket['id']+'/triage/start',{'reason':'Formulário antigo'},'POST',409,version=old)
    check('Formulário antigo retorna conflito explícito',stale['code']=='VERSION_CONFLICT')
    staff.request('/tickets/'+ticket['id']+'/wait',{'reason':'Sem versão'},'POST',428,version=None)
    triage(ticket,tp);before=detail(ticket)
    action(staff,ticket,'reassign',dict(assigneeId=other_manager.me['id'],reason='Continuidade do atendimento'))
    manager.request('/tickets/'+ticket['id'],expected=404);other_manager.request('/tickets/'+ticket['id'])
    after=detail(ticket);check('Reatribuição preserva prazo e ciclo',before['resolutionDueAt']==after['resolutionDueAt'] and before['resolutionCycle']==after['resolutionCycle'])
    action(c,ticket,'reassign',dict(assigneeId=manager.me['id'],reason='Indevido'),403)
    action(other_manager,ticket,'wait',{'reason':'Preciso de contexto'})
    action(a,ticket,'reassign',dict(assigneeId=manager.me['id'],reason='Redistribuição durante espera'))
    other_manager.request('/tickets/'+ticket['id'],expected=404)
    action(c,ticket,'complement',{'body':'Dados enviados','effectiveResponse':False});action(staff,ticket,'resume',{'reason':'Contexto suficiente'})
    check('Retomada audita novo prazo',any(h['action']=='RESOLUTION_DEADLINE_CHANGED' for h in detail(ticket)['history']))

def typed_fields_and_atomic_upload():
    fields=[dict(name='budget',label='Orçamento',type='NUMBER',required=True,validation={'min':0,'max':100}),dict(name='date',label='Data',type='DATE',required=True),dict(name='channel',label='Canal',type='SELECT',options=['Google','Meta'],required=True),dict(name='context',label='Contexto',type='TEXT',requiredWhen={'field':'channel','equals':'Meta'},validation={'minLength':3}),dict(name='brief',label='Briefing',type='FILE',required=True)]
    tp=a.request('/demand-types',dict(name=tag+' campos tipados',approvalRequired=False,evidenceRequired=False,fieldDefinitions=json.dumps(fields)),'POST',201)
    body=dict(clientId=client_id,campaignId=campaign['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag+' tipado',description='Teste de tipos e atomicidade',urgency='HIGH',metrics={'fields':{'budget':'10','date':'2026-10-04','channel':'Meta','context':'Detalhes','brief':'brief.csv'}})
    c.request('/tickets',body,'POST',400)
    def multipart(payload,files,expected):
        boundary='review'+uuid.uuid4().hex
        chunks=[('--'+boundary+'\r\nContent-Disposition: form-data; name="request"\r\nContent-Type: application/json\r\n\r\n').encode()+json.dumps(payload).encode()]
        for name,filename,mime,content in files:chunks.append(('--'+boundary+'\r\nContent-Disposition: form-data; name="'+name+'"; filename="'+filename+'"\r\nContent-Type: '+mime+'\r\n\r\n').encode()+content)
        return c.request('/tickets',method='POST',expected=expected,raw=b'\r\n'.join(chunks)+('\r\n--'+boundary+'--\r\n').encode(),content_type='multipart/form-data; boundary='+boundary)
    uploaded=[('field.brief','brief.csv','text/csv',b'coluna;valor\ncontrole;1\n')]
    for key,value in [('budget','abc'),('budget','101'),('date','2026-02-30'),('channel','Outro'),('context','')]:
        invalid=json.loads(json.dumps(body));invalid['metrics']['fields'][key]=value;multipart(invalid,uploaded,400)
    created=detail(multipart(body,uploaded,201));check('Arquivo tipado persistido junto ao ticket',len(created['attachments'])==1 and json.loads(created['metrics'])['fields']['brief']=='brief.csv')
    body['subject']=tag+' rollback'
    storage=Path(__file__).resolve().parents[1]/'target/fixes-uploads';before={p for p in storage.rglob('*') if p.is_file()}
    multipart(body,uploaded+[('files','invalid.exe','application/octet-stream',b'invalid')],400)
    check('Falha no segundo arquivo não deixa ticket',a.request('/tickets?search='+urllib.parse.quote(body['subject']))['total']==0)
    check('Rollback remove arquivos já escritos',before=={p for p in storage.rglob('*') if p.is_file()})
    duplicate=next(x for x in a.request('/clients') if x['email'])
    a.request('/clients',dict(name=tag,contactName='Teste',email=duplicate['email'],phone=None),'POST',400)

def pagination_and_aggregates():
    for account in [a,c,other,staff,manager,other_manager]:
        dashboard=account.request('/dashboard');scope=account.request('/tickets?size=1');check('Distribuições por prioridade e responsável fecham o escopo '+account.email,sum(x['count'] for x in dashboard['priorityDistribution'])==scope['total']==sum(x['count'] for x in dashboard['assigneeDistribution']))
        check('Distribuição de prazos fecha ativos '+account.email,sum(x['count'] for x in dashboard['deadlineDistribution'])==dashboard['activeCount'])
        expired=account.request('/tickets?overdue=true&size=100');check('Filtro só retorna vencidos '+account.email,all(x['overdue'] for x in expired['items']))
        notes=account.request('/notifications?size=1&unread=true');check('Notificações paginadas e não lidas '+account.email,len(notes['items'])<=1 and all(x['readAt'] is None for x in notes['items']) and notes['total']==notes['unreadCount'])
    page=a.request('/clients/page?size=1&search='+urllib.parse.quote(tag));check('Cadastro usa paginação no servidor',page['size']==1 and len(page['items'])<=1 and page['total']>=1)
    c.request('/clients/page',expected=403);a.request('/clients/page?page=-1',expected=400)
    result=a.request('/reports/tickets?overdue=true');listing=a.request('/tickets?overdue=true&size=1');check('Relatório e lista concordam sobre vencimento',result['total']==listing['total'])
    expired=a.request('/tickets?overdue=true&size=100')
    check('Job publica vencimento automaticamente',any(any(h['action']=='SLA_OVERDUE' for h in a.request('/tickets/'+t['id'])['history']) for t in expired['items'] if t['status'] not in ['DONE','CANCELLED']))

def notifications_and_auth():
    notes=c.request('/notifications');foreign=other.request('/notifications')['items'][0]
    c.request('/notifications/'+foreign['id']+'/read',method='POST',expected=404)
    c.request('/notifications/read-all',method='POST',expected=204);check('Leitura em lote persistiu',c.request('/notifications')['unreadCount']==0)
    staff.request('/tickets/00000000-0000-0000-0000-000000000000/cancel',{'reason':'Sem CSRF'},'POST',403,csrf=False)
    c.request('/auth/logout',method='POST',expected=204);c.request('/auth/me',expected=401)
    invalid=Account.__new__(Account);invalid.email='login inválido';invalid.http=urllib.request.build_opener();invalid.token=None
    invalid.request('/auth/login',dict(email='admin@sige.demo',password='senha-incorreta'),'POST',401)

for name,fn in [('Jornada principal e dois ciclos',main_flow),('Conclusão com dispensa',no_approval),('Anexos e cancelamento',files_and_cancel),('Proteções de cadastros',catalog_guards),('Criação e edição de cadastros',catalog_roundtrip),('Escopo, relatórios e CSV',reference_and_report),('Concorrência e reatribuição',concurrency_and_reassignment),('Campos tipados e upload atômico',typed_fields_and_atomic_upload),('Paginação, painel e vencimentos',pagination_and_aggregates),('Notificações, CSRF e logout',notifications_and_auth)]:scenario(name,fn)
output=Path(args.output);output.parent.mkdir(parents=True,exist_ok=True);output.write_text(json.dumps(results,ensure_ascii=False,indent=2),encoding='utf-8')
failed=[result for result in results if not result['passed']]
print(json.dumps(dict(checks=len(results),passed=len(results)-len(failed),failed=failed),ensure_ascii=False,indent=2))
raise SystemExit(1 if failed else 0)
