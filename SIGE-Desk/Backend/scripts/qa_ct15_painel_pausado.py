from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
from collections import Counter
results=[];active_ct='CT-06'
def record(name,ok,detail):results.append(dict(ct=active_ct,name=name,passed=bool(ok),detail=detail))
def rows():
 q="SELECT JSON_OBJECT('id',id,'clientId',client_id,'campaignId',campaign_id,'demandTypeId',demand_type_id,'priority',priority,'assigneeId',assignee_id,'status',status,'createdAt',created_at,'resolutionDueAt',resolution_due_at,'responseDueAt',response_due_at,'responseCompletedAt',response_completed_at,'resolutionPausedAt',resolution_paused_at,'completedAt',completed_at) FROM tickets ORDER BY id"
 return [json.loads(x) for x in sql(q).splitlines() if x]
def scoped(data,ac):
 role=ac.me['role'];return [x for x in data if role not in ('CLIENT','TRAFFIC_MANAGER') or (x['clientId']==ac.me['clientId'] if role=='CLIENT' else x['assigneeId']==ac.me['id'])]
accounts=[a,staff,c,other,manager,other_manager];data=rows()
sample=next(x for x in reversed(data) if all(x.get(k) for k in ('clientId','campaignId','demandTypeId','priority','assigneeId','status')))
filt={k:sample[k] for k in ('clientId','campaignId','demandTypeId','priority','assigneeId','status')};filt.update(createdFrom='2020-01-01',createdTo='2099-12-31')
for ac in accounts:
 before=rows();expected=sorted(x['id'] for x in scoped(before,ac) if all(x.get(k)==v for k,v in filt.items() if k not in ('createdFrom','createdTo')) and filt['createdFrom']<=x['createdAt'][:10]<=filt['createdTo']);got=[];totals=[];page=0
 while True:
  d=ac.request('/tickets?'+urllib.parse.urlencode({**filt,'size':2,'page':page}));got.extend(x['id'] for x in d['items']);totals.append(d['total']);page+=1
  if len(got)>=d['total'] or not d['items']:break
 stable=before==rows();record('Filtros e páginas '+ac.me['role']+' '+ac.me['id'],stable and sorted(got)==expected and len(got)==len(set(got)) and all(n==len(expected) for n in totals),dict(filters=filt,expected=expected,obtained=got,totals=totals,stable=stable))
# Uma segunda combinação usa grupo existente de 3 a 30 tickets para percorrer páginas.
counts=Counter((x['clientId'],x['status']) for x in data)
choice=next((k for k,v in counts.items() if 3<=v<=30),None)
if choice:
 for ac in accounts:
  before=rows(); expected=sorted(x['id'] for x in scoped(before,ac) if (x['clientId'],x['status'])==choice);got=[];totals=[];page=0
  while True:
   d=ac.request('/tickets?'+urllib.parse.urlencode({'clientId':choice[0],'status':choice[1],'size':2,'page':page}));got.extend(x['id'] for x in d['items']);totals.append(d['total']);page+=1
   if len(got)>=d['total'] or not d['items']:break
  stable=before==rows();record('Páginas combinadas '+ac.me['role']+' '+ac.me['id'],stable and sorted(got)==expected and len(got)==len(set(got)) and all(n==len(expected) for n in totals),dict(filters={'clientId':choice[0],'status':choice[1]},expected=expected,obtained=got,totals=totals,stable=stable))
active_ct='CT-15' 
fixture_id='3b42b608-e9b0-4fbf-b634-af4549a08413'
for ac in accounts:
 before=rows(); clock_before=datetime.datetime.now(datetime.timezone.utc).replace(tzinfo=None);d=ac.request('/dashboard'); clock_after=datetime.datetime.now(datetime.timezone.utc).replace(tzinfo=None);stable=before==rows();dataset=scoped(before,ac)
 for key,outkey,inkey in [('statusDistribution','status','status'),('priorityDistribution','priority','priority'),('assigneeDistribution','id','assigneeId')]:
  exp=Counter(x[inkey] if x[inkey] is not None else ('UNCLASSIFIED' if inkey=='priority' else None) for x in dataset);got={x[outkey]:x['count'] for x in d[key]};expected={k:exp.get(k,0) for k in set(exp)|set(got)}
  record(key+' '+ac.me['role']+' '+ac.me['id'],stable and got==expected,dict(expected=expected,obtained=got,stable=stable))
 active=[x for x in dataset if x['status'] not in ('DONE','CANCELLED')];expected={'activeCount':len(active),'highPriorityCount':sum(x['priority'] in ('HIGH','URGENT') for x in active),'validationCount':sum(x['status']=='VALIDATION' for x in dataset),'waitingCount':sum(x['status']=='WAITING_FOR_CLIENT' for x in dataset),'classifiedCount':sum(x['resolutionDueAt'] is not None for x in active)};got={k:d[k] for k in expected};record('Indicadores '+ac.me['role']+' '+ac.me['id'],stable and got==expected,dict(expected=expected,obtained=got,stable=stable))
 def deadline_groups(now):
  groups=Counter()
  for x in active:
   def dt(key):return datetime.datetime.fromisoformat(x[key]) if x[key] else None
   response=dt('responseDueAt'); resolution=dt('resolutionDueAt'); completed=dt('responseCompletedAt'); paused=dt('resolutionPausedAt')
   overdue=(response is not None and (completed or now)>response) or (resolution is not None and x['completedAt'] is None and paused is None and resolution<now)
   state='OVERDUE' if overdue else 'PAUSED' if resolution and paused else 'PENDING_CLASSIFICATION' if not resolution else 'ON_TIME'
   groups[state]+=1
  return {k:groups[k] for k in ('OVERDUE','PAUSED','PENDING_CLASSIFICATION','ON_TIME')}
 expected_before=deadline_groups(clock_before);expected_after=deadline_groups(clock_after);got_deadline={x['state']:x['count'] for x in d['deadlineDistribution']};time_stable=expected_before==expected_after
 record('Prazos por estado, oráculo MySQL '+ac.me['role']+' '+ac.me['id'],stable and time_stable and got_deadline==expected_before,dict(expected=expected_before,obtained=got_deadline,stable=stable,timeStable=time_stable))
 fixture=next((x for x in before if x['id']==fixture_id),None)
 valid=bool(fixture and fixture['status']=='WAITING_FOR_CLIENT' and fixture['resolutionPausedAt'] and fixture['resolutionDueAt'] and fixture['responseCompletedAt'] and fixture['responseDueAt'] and fixture['responseCompletedAt']<=fixture['responseDueAt'])
 visible=any(x['id']==fixture_id for x in dataset)
 record('Fixture PAUSED positiva e escopo '+ac.me['role']+' '+ac.me['id'],valid and stable and time_stable and (not visible or expected_before['PAUSED']>0) and got_deadline==expected_before,dict(fixture=fixture,visible=visible,expected=expected_before,obtained=got_deadline,stable=stable,timeStable=time_stable))
 record('Partição de prazos '+ac.me['role']+' '+ac.me['id'],sum(x['count'] for x in d['deadlineDistribution'])==len(active) and d['overdueCount']==next(x['count'] for x in d['deadlineDistribution'] if x['state']=='OVERDUE'),dict(obtained=d['deadlineDistribution'],active=len(active)))
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,mysql=sql('SELECT VERSION()'),layer='HTTP + MySQL; somente leitura'),checks=results,limitations=['Fixture exclusiva CT-06 preexistente reutilizada em leitura; nenhuma fixture alterada.','Prazos calculados dos registros MySQL em duas amostras temporais; não controlam relógio nem todos os limites.','Interface não exercitada.'])
Path(args.output).write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8');print('Checks',len(results),'falhas',sum(not x['passed'] for x in results));raise SystemExit(any(not x['passed'] for x in results))
