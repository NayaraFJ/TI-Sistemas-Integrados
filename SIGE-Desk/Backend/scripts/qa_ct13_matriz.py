from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
steps=[]
def snap(label):
 d=detail(t); steps.append(dict(label=label, ticket=d['ticket'], responseDueAt=d['responseDueAt'], resolutionDueAt=d['resolutionDueAt'], resolutionCycle=d['resolutionCycle'], history=d['history'])); return d
def execute():
 global t
 tp=new_type(True,True);t=create_ticket(tp);snap('abertura');start(t);snap('triagem');triage(t,tp);snap('classificacao')
 action(staff,t,'reassign',dict(assigneeId=other_manager.me['id'],reason='Reatribuição CT13'));snap('reatribuicao')
 action(other_manager,t,'wait',{'reason':'Aguardo CT13'});snap('aguardo')
 action(c,t,'complement',{'body':'Complemento CT13','effectiveResponse':False});snap('complemento')
 action(staff,t,'resume',{'reason':'Retomada CT13'});snap('retomada')
 action(other_manager,t,'execution',{'actionDescription':'Entrega CT13','evidenceUrl':'https://example.test/ct13'});snap('entrega')
 action(c,t,'approve',{'reason':'Aprovação CT13'});snap('aprovacao')
 action(c,t,'reopen',{'reason':'Reabertura CT13'});snap('reabertura')
 triage(t,tp);snap('reclassificacao')
 before=detail(t);other.request('/tickets/'+t['id'],expected=404)
 staff.request('/tickets/'+t['id']+'/wait',{'reason':'Versão ausente'},'POST',428,version=None)
 staff.request('/tickets/'+t['id']+'/wait',{'reason':'Versão antiga'},'POST',409,version=0)
 action(c,t,'reassign',{'assigneeId':manager.me['id'],'reason':'Negativa CT13'},403)
 after=detail(t);check('Negativas preservam ticket e histórico',before['ticket']==after['ticket'] and before['history']==after['history'])
 snap('negativas')
run_case('CT-13-matriz','Alta',execute)
try:
 tables=sql("SHOW TABLES LIKE '%histor%'")
except Exception as e: tables=str(e)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results,steps=steps,historyTables=tables)
out=Path(args.output)
if out.exists():raise SystemExit('Evidência já existe')
out.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False));print('history tables:',tables)
