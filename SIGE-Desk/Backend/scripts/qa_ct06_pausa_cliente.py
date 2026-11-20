from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
results=[];active_ct='CT-06';created=[];error=None
try:
 tp=new_type(False,False)
 for i in range(4):
  t=create_ticket(tp,subject=tag+' CT06 paginacao '+str(i));created.append(t['id']);start(t);triage(t,tp)
 t_id=created[0]; t={'id':t_id};action(manager,t,'wait',{'reason':'Fixture exclusiva CT06 aguardando complemento'})
 before=sql("SELECT JSON_OBJECT('id',id,'status',status,'responseDueAt',response_due_at,'responseCompletedAt',response_completed_at,'resolutionDueAt',resolution_due_at,'resolutionPausedAt',resolution_paused_at) FROM tickets WHERE id='"+t_id+"'")
 sql("UPDATE tickets SET response_due_at=UTC_TIMESTAMP(6)-INTERVAL 1 HOUR, response_completed_at=NULL WHERE id='"+t_id+"' AND demand_type_id='"+tp['id']+"'")
 db=json.loads(sql("SELECT JSON_OBJECT('id',id,'status',status,'responseDueAt',response_due_at,'responseCompletedAt',response_completed_at,'resolutionDueAt',resolution_due_at,'resolutionPausedAt',resolution_paused_at) FROM tickets WHERE id='"+t_id+"'"))
 check('Fixture pausada e primeira resposta vencida no MySQL',db['status']=='WAITING_FOR_CLIENT' and db['resolutionPausedAt'] is not None and db['responseCompletedAt'] is None,db)
 d=c.request('/tickets/'+t_id);check('Detalhe mostra primeira resposta vencida apesar de pausa',d['ticket']['overdue'] and d['resolutionSlaState']=='PAUSED' and d['responseSlaState']=='OVERDUE',dict(ticketId=t_id,overdue=d['ticket']['overdue'],slaState=d.get('slaState'),resolutionSlaState=d.get('resolutionSlaState'),responseSlaState=d.get('responseSlaState')))
 params={'demandTypeId':tp['id'],'size':2}; got=[];totals=[]
 for page in range(3):
  page_data=c.request('/tickets?'+urllib.parse.urlencode({**params,'page':page}));got.extend(x['id'] for x in page_data['items']);totals.append(page_data['total'])
 expected=sql("SELECT id FROM tickets WHERE demand_type_id='"+tp['id']+"' AND client_id='"+client_id+"' ORDER BY id").splitlines()
 check('Cliente percorre duas páginas sem perdas ou duplicação',len(expected)==4 and sorted(got)==sorted(expected) and len(got)==len(set(got)) and totals==[4,4,4],dict(expected=expected,obtained=got,totals=totals))
 for ac in [a,staff,c,manager,other,other_manager]:
  response=ac.request('/tickets?'+urllib.parse.urlencode({'demandTypeId':tp['id'],'overdue':'true','size':100}));ids=[x['id'] for x in response['items']]
  allowed=ac in [a,staff,c,manager]
  check('Vencido pausado no escopo '+ac.me['role']+' '+ac.me['id'],(t_id in ids)==allowed,dict(ids=ids,total=response['total'],expectedVisible=allowed))
except Exception as e:error=repr(e)
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,mysql=sql('SELECT VERSION()'),runTag=tag,layer='HTTP + MySQL',executor='qa_filtros_painel'),fixture=dict(typeId=tp['id'] if 'tp' in globals() else None,tickets=created,before=before if 'before' in globals() else None,after=db if 'db' in globals() else None),checks=results,error=error,limitations=['Fixture isolada preservada para auditoria em QA; atualização SQL restrita ao ticket e tipo criados nesta execução.','Relógio de banco usado para forçar vencimento; não testa limites do calendário.','Interface não exercitada.'])
Path(args.output).write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8');print('Checks',len(results),'falhas',sum(not x['passed'] for x in results),'erro',error);raise SystemExit(bool(error) or any(not x['passed'] for x in results))
