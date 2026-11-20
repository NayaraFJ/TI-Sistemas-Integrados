from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8');exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
first=json.loads(Path('../docs/qa/ct06-pausa-cliente-reteste-20261119.json').read_text(encoding='utf-8')); tp=first['fixture']['typeId'];tid=first['fixture']['tickets'][0];out=[]
for ac in [a,staff,c,manager,other,other_manager]:
 d=ac.request('/tickets?'+urllib.parse.urlencode({'demandTypeId':tp,'overdue':'true','size':100}));out.append(dict(role=ac.me['role'],id=ac.me['id'],expectedVisible=ac in [a,staff,c,manager],obtained=[x['id'] for x in d['items']],total=d['total']))
detail=c.request('/tickets/'+tid); query=sql("SELECT JSON_OBJECT('responseDueAt',response_due_at,'responseCompletedAt',response_completed_at,'resolutionDueAt',resolution_due_at,'resolutionPausedAt',resolution_paused_at,'status',status) FROM tickets WHERE id='"+tid+"'")
report=dict(finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),schema=SCHEMA,ticketId=tid,typeId=tp,database=json.loads(query),detail={k:detail.get(k) for k in ['slaState','responseSlaState','resolutionSlaState']},item={k:detail['ticket'].get(k) for k in ['id','status','slaState','overdue']},profiles=out)
Path(args.output).write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8');print(json.dumps(report,ensure_ascii=False))
