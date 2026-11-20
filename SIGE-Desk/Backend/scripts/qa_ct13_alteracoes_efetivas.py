from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
observed={}
def execute():
 tp1=new_type(True,True);tp2=new_type(True,True)
 campaign2=staff.request('/campaigns',dict(clientId=client_id,name=tag+' campanha CT13',channel='Google Ads',objective='Fixture sintética'),'POST',201)
 t=create_ticket(tp1);start(t)
 def classify(priority,camp,tp,send):return action(staff,t,'triage',dict(priority=priority,assigneeId=manager.me['id'],campaignId=camp['id'],demandTypeId=tp['id'],effectiveResponse='Resposta CT13',sendToExecution=send))
 classify('HIGH',campaign,tp1,False);before=detail(t)
 classify('LOW',campaign2,tp2,True);after=detail(t)
 previous={x['id'] for x in before['history']}; new=[x for x in after['history'] if x['id'] not in previous]
 matrix=[]
 for act,field,old,newval in [('TRIAGE_CLASSIFIED','priority','HIGH','LOW'),('TRIAGE_CAMPAIGN','campaignId',campaign['id'],campaign2['id']),('TRIAGE_TYPE','demandTypeId',tp1['id'],tp2['id'])]:
  h=next(x for x in new if x['action']==act); actual=[h['fieldName'],json.loads(h['oldValue']),json.loads(h['newValue']),h['actorName'],h['reason']];expected=[field,old,newval,staff.me['name'],{'TRIAGE_CLASSIFIED':'Prioridade confirmada','TRIAGE_CAMPAIGN':'Campanha confirmada na triagem','TRIAGE_TYPE':'Tipo de demanda confirmado na triagem'}[act]]
  check('Mudança efetiva '+field,actual==expected and bool(h['createdAt']),dict(actual=actual,expected=expected));matrix.append(dict(event=h,expected=expected))
 check('Eventos anteriores preservados',previous.issubset({x['id'] for x in after['history']}))
 q="SELECT JSON_OBJECT('id',h.id,'actorId',h.actor_id,'actorName',u.name,'action',h.action,'fieldName',h.field_name,'oldValue',h.old_value,'newValue',h.new_value,'reason',h.reason,'createdAt',DATE_FORMAT(h.created_at,'%Y-%m-%dT%H:%i:%s.%fZ')) FROM ticket_history h LEFT JOIN users u ON u.id=h.actor_id WHERE h.ticket_id='"+t['id']+"'"
 rows=[json.loads(line) for line in sql(q).splitlines()];db={x['id']:x for x in rows};errors=[]
 for h in after['history']:
  row=db.get(h['id']);
  if not row or row['actorName']!=h['actorName'] or not row['actorId'] or row['action']!=h['action'] or row['fieldName']!=h['fieldName'] or row['oldValue']!=(json.loads(h['oldValue']) if h['oldValue'] is not None else None) or row['newValue']!=(json.loads(h['newValue']) if h['newValue'] is not None else None) or row['reason']!=h['reason'] or row['createdAt']!=h['createdAt']:errors.append(dict(api=h,db=row))
 check('Junção MySQL correlaciona ator e evento completo',not errors and len(rows)==len(after['history']),dict(count=len(rows),errors=errors))
 for h in new:
  check('Ator da reclassificação corresponde à sessão',db[h['id']]['actorId']==staff.me['id'],h['action'])
 observed.update(ticket=t['id'],before=before,after=after,matrix=matrix,dbRows=rows,actors=dict(staffId=staff.me['id'],clientId=c.me['id']))
run_case('CT-13-mudancas-efetivas','Alta',execute)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results,observed=observed)
out=Path(args.output)
if out.exists():raise SystemExit('Evidência já existe')
out.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf8')
print(json.dumps(cases,ensure_ascii=False))
