import json,subprocess,os,datetime
from pathlib import Path
p=Path('SIGE-Desk/docs/qa/ct13-matriz-20261119.json'); d=json.loads(p.read_text(encoding='utf8')); steps={s['label']:s for s in d['steps']}; history=steps['negativas']['history']; ticket=steps['abertura']['ticket']['id']
env={**os.environ,'MYSQL_PWD':os.environ.get('SIGE_QA_DB_PASSWORD','123')}; mysql=r'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe'
q="SELECT JSON_OBJECT('id',id,'action',action,'fieldName',field_name,'oldValue',old_value,'newValue',new_value,'reason',reason,'createdAt',DATE_FORMAT(created_at,'%Y-%m-%dT%H:%i:%s.%fZ'),'actorId',actor_id) FROM ticket_history WHERE ticket_id='"+ticket+"'"
raw=subprocess.check_output([mysql,'-u','root','-h','127.0.0.1','--default-character-set=utf8mb4','--batch','--raw','--skip-column-names','sige_desk_qa_20261118','--execute='+q],env=env).decode('utf8'); db=[json.loads(x) for x in raw.splitlines()]; checks=[]
def record(name,ok,detail): checks.append(dict(name=name,passed=bool(ok),detail=detail))
def val(x): return json.loads(x) if x is not None else None
def event(label,action):
 prev=list(steps).index(label); old={h['id'] for s in list(steps.values())[:prev] for h in s['history']}; return next(h for h in steps[label]['history'] if h['action']==action and h['id'] not in old)
def verify(label,act,field,old,new,actor,reason):
 h=event(label,act); actual=[h['fieldName'],val(h['oldValue']),val(h['newValue']),h['actorName'],h['reason']]; expected=[field,old,new,actor,reason]; record(label+' '+act,actual==expected and bool(h['createdAt']),dict(expected=expected,actual=actual,createdAt=h['createdAt'],id=h['id']))
client=event('triagem','TRIAGE_STARTED')['actorName']; creator=event('abertura','TICKET_CREATED')['actorName']; mgr=event('aguardo','COMPLEMENT_REQUESTED')['actorName']; managerid=val(event('classificacao','ASSIGNEE_CHANGED')['newValue']); otherid=val(event('reatribuicao','ASSIGNEE_CHANGED')['newValue'])
verify('triagem','TRIAGE_STARTED','status','OPEN','TRIAGE',client,'Conferência de teste')
verify('classificacao','TRIAGE_CLASSIFIED','priority',None,'HIGH',client,'Prioridade confirmada')
verify('classificacao','ASSIGNEE_CHANGED','assigneeId',None,managerid,client,'Responsável confirmado')
verify('classificacao','SENT_TO_EXECUTION','status','TRIAGE','EXECUTION',client,'Triagem concluída')
for act,key,reason in [('RESPONSE_DEADLINE_CHANGED','responseDueAt','Prazo de primeira resposta aplicado'),('RESOLUTION_DEADLINE_CHANGED','resolutionDueAt','Prazo de resolução aplicado')]: verify('classificacao',act,key,None,steps['classificacao'][key],client,reason)
verify('reatribuicao','ASSIGNEE_CHANGED','assigneeId',managerid,otherid,client,'Reatribuição CT13')
verify('aguardo','COMPLEMENT_REQUESTED','status','EXECUTION','WAITING_FOR_CLIENT',mgr,'Aguardo CT13')
verify('retomada','TICKET_RESUMED','status','WAITING_FOR_CLIENT','EXECUTION',client,'Retomada CT13')
verify('retomada','RESOLUTION_DEADLINE_CHANGED','resolutionDueAt',steps['aguardo']['resolutionDueAt'],steps['retomada']['resolutionDueAt'],client,'Prazo recalculado após a pausa')
verify('entrega','EXECUTION_RECORDED','status','EXECUTION','VALIDATION',mgr,'Entrega CT13')
verify('aprovacao','APPROVED','status','VALIDATION','DONE',creator,'Aprovação CT13')
verify('aprovacao','RESOLUTION_DEADLINE_CHANGED','resolutionDueAt',steps['entrega']['resolutionDueAt'],steps['aprovacao']['resolutionDueAt'],creator,'Prazo recalculado após validação')
verify('reabertura','REOPENED','status','DONE','REOPENED',creator,'Reabertura CT13')
closed=event('reabertura','RESOLUTION_CYCLE_CLOSED'); cv=val(closed['newValue']);record('Ciclo encerrado',closed['actorName']==creator and closed['reason']=='Reabertura CT13' and cv['cycle']==1 and cv['resolutionDueAt']==steps['aprovacao']['resolutionDueAt'] and bool(cv['slaSnapshot']),cv)
verify('reclassificacao','ASSIGNEE_CHANGED','assigneeId',otherid,managerid,client,'Responsável confirmado')
verify('reclassificacao','RESOLUTION_DEADLINE_CHANGED','resolutionDueAt',None,steps['reclassificacao']['resolutionDueAt'],client,'Prazo de resolução aplicado')
record('Histórico preservado por todas as etapas',all({h['id'] for h in x['history']} <= {h['id'] for h in y['history']} for x,y in zip(steps.values(),list(steps.values())[1:])),len(history))
byid={x['id']:x for x in db};dif=[]
for h in history:
 b=byid.get(h['id']);
 if not b or any([b['action']!=h['action'],b['fieldName']!=h['fieldName'],b['oldValue']!=val(h['oldValue']),b['newValue']!=val(h['newValue']),b['reason']!=h['reason'],not b['actorId'],b['createdAt']!=h['createdAt']]):dif.append(dict(api=h,db=b))
record('API coincide com registros MySQL',len(db)==len(history) and not dif,dict(apiCount=len(history),dbCount=len(db),differences=dif))
record('Negativas preservam histórico e versão',steps['negativas']['history']==steps['reclassificacao']['history'] and steps['negativas']['ticket']==steps['reclassificacao']['ticket'],dict(version=steps['negativas']['ticket']['version']))
out=dict(ticket=ticket,checks=checks,dbRows=db,failed=sum(not x['passed'] for x in checks));Path('SIGE-Desk/docs/qa/ct13-confronto-20261119.json').write_text(json.dumps(out,ensure_ascii=False,indent=2),encoding='utf8');print(json.dumps(dict(total=len(checks),failed=out['failed'],failures=[x for x in checks if not x['passed']]),ensure_ascii=False))
