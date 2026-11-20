from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8');exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
results=[];active_ct='CT-06';fixture={};error=None
try:
 tp=new_type(False,False)
 for name in ['response_late_completed','resolution_late','paused_on_time','done_late','cancelled']:
  t=create_ticket(tp,subject=tag+' matriz '+name);fixture[name]=t['id']
  if name=='cancelled':action(staff,t,'cancel',{'reason':'Fixture exclusiva cancelada'})
  else:
   start(t);triage(t,tp)
   if name=='paused_on_time':action(manager,t,'wait',{'reason':'Fixture exclusiva pausada'})
   if name=='done_late':action(manager,t,'execution',{'actionDescription':'Fixture concluída'})
 def update(name,fields):
  sql("UPDATE tickets SET "+fields+" WHERE id='"+fixture[name]+"' AND demand_type_id='"+tp['id']+"'")
 update('response_late_completed',"response_due_at=UTC_TIMESTAMP(6)-INTERVAL 2 HOUR,response_completed_at=UTC_TIMESTAMP(6)-INTERVAL 1 HOUR,resolution_due_at=UTC_TIMESTAMP(6)+INTERVAL 1 DAY")
 update('resolution_late',"response_due_at=UTC_TIMESTAMP(6)+INTERVAL 1 DAY,response_completed_at=UTC_TIMESTAMP(6),resolution_due_at=UTC_TIMESTAMP(6)-INTERVAL 1 HOUR")
 update('paused_on_time',"response_due_at=UTC_TIMESTAMP(6)+INTERVAL 1 DAY,response_completed_at=UTC_TIMESTAMP(6),resolution_due_at=UTC_TIMESTAMP(6)-INTERVAL 1 HOUR")
 update('done_late',"response_due_at=UTC_TIMESTAMP(6)+INTERVAL 1 DAY,response_completed_at=UTC_TIMESTAMP(6),resolution_due_at=UTC_TIMESTAMP(6)-INTERVAL 2 HOUR,completed_at=UTC_TIMESTAMP(6)-INTERVAL 1 HOUR")
 update('cancelled',"response_due_at=UTC_TIMESTAMP(6)-INTERVAL 2 HOUR,response_completed_at=NULL")
 dataset=[json.loads(x) for x in sql("SELECT JSON_OBJECT('id',id,'status',status,'responseDueAt',response_due_at,'responseCompletedAt',response_completed_at,'resolutionDueAt',resolution_due_at,'resolutionPausedAt',resolution_paused_at,'completedAt',completed_at) FROM tickets WHERE demand_type_id='"+tp['id']+"' ORDER BY id").splitlines()]
 expected={fixture[k] for k in ['response_late_completed','resolution_late','done_late']}
 for ac in [a,staff,c,manager,other,other_manager]:
  resp=ac.request('/tickets?'+urllib.parse.urlencode({'demandTypeId':tp['id'],'overdue':'true','size':100}));obtained={x['id'] for x in resp['items']};want=expected if ac in [a,staff,c,manager] else set()
  check('Filtro matriz '+ac.me['role']+' '+ac.me['id'],obtained==want and resp['total']==len(want),dict(expected=sorted(want),obtained=sorted(obtained),total=resp['total']))
 for name,tid in fixture.items():
  d=c.request('/tickets/'+tid);want=tid in expected
  check('Detalhe '+name,d['ticket']['overdue']==want,dict(expected=want,obtained=d['ticket']['overdue'],response=d['responseSlaState'],resolution=d['resolutionSlaState'],status=d['ticket']['status']))
except Exception as e:error=repr(e)
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),schema=SCHEMA,baseUrl=args.base_url,tag=tag,typeId=tp['id'] if 'tp' in globals() else None,fixture=fixture,dataset=dataset if 'dataset' in globals() else None,checks=results,error=error,limitations=['Fixtures exclusivas preservadas em QA. Datas forçadas no banco apenas por ID e tipo próprios.','Sem relógio controlado de aplicação, interface ou demonstração de todos limites.'])
Path(args.output).write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8');print('Checks',len(results),'falhas',sum(not x['passed'] for x in results),'erro',error);raise SystemExit(bool(error) or any(not x['passed'] for x in results))
