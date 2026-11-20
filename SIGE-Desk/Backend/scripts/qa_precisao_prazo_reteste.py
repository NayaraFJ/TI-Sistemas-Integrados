import json, datetime
from pathlib import Path
p=Path(__file__).with_name('qa_functional.py'); s=p.read_text(encoding='utf8'); exec(compile(s[:s.index('suite=[')],str(p),'exec'),globals())
def test():
 tp=new_type(True,True); t=create_ticket(tp); start(t); evidence={'ticketId':t['id'],'number':t['number'],'stages':{}}
 def capture(label,r,event=None,reason=None):
  d=detail(t); due=d['resolutionDueAt']; h=[x for x in d['history'] if x['action']=='RESOLUTION_DEADLINE_CHANGED' and x['reason']==reason]; notes=[]
  if event:
   raw=sql("SELECT summary FROM notifications WHERE ticket_id='"+t['id']+"' AND event_type='"+event+"'"); notes=raw.splitlines()
  obj=dict(responseVersion=r['version'],detailVersion=d['ticket']['version'],due=due,history=[x['newValue'] for x in h],summaries=notes,status=d['ticket']['status']); evidence['stages'][label]=obj
  check(label+' versão',r['version']==d['ticket']['version'],obj)
  if reason: check(label+' prazo do histórico',bool(h) and json.loads(h[0]['newValue'])==due,obj)
  if event: check(label+' prazo do aviso',bool(notes) and all(due in n for n in notes),obj)
 r=triage(t,tp);capture('classificacao',r,'ASSIGNED','Prazo de resolução aplicado')
 r=action(manager,t,'wait',{'reason':'QA precisão aguardo'});capture('aguardo',r)
 action(c,t,'comments',{'body':'QA precisão complemento','effectiveResponse':False},201)
 r=action(staff,t,'resume',{'reason':'QA precisão retomada'});capture('retomada',r,'TICKET_RESUMED','Prazo recalculado após a pausa')
 r=action(manager,t,'execution',{'actionDescription':'QA material','evidenceUrl':'https://example.test/qa'});capture('validacao',r)
 r=action(c,t,'correction',{'reason':'QA correção'});capture('correcao',r,reason='Prazo recalculado após a pausa')
 r=action(manager,t,'execution',{'actionDescription':'QA material corrigido','evidenceUrl':'https://example.test/qa2'});capture('nova_validacao',r)
 r=action(c,t,'approve',{'reason':'QA aprovação'});capture('aprovacao',r,reason='Prazo recalculado após validação')
 def snap():
  d=detail(t); counts=sql("SELECT CONCAT((SELECT COUNT(*) FROM ticket_history WHERE ticket_id='"+t['id']+"'),':',(SELECT COUNT(*) FROM notifications WHERE ticket_id='"+t['id']+"'))");return dict(version=d['ticket']['version'],due=d['resolutionDueAt'],status=d['ticket']['status'],counts=counts)
 before=snap();action(c,t,'approve',{'reason':'QA inválida após conclusão'},409);after=snap();evidence['rollback']={'before':before,'after':after};check('rollback após flush',before==after,evidence['rollback'])
 Path('../docs/qa/precisao-prazo-runtime-reteste-dados.json').write_text(json.dumps(evidence,ensure_ascii=False,indent=2),encoding='utf8')
run_case('CT-07-prazo-runtime','Alta',test)
Path('../docs/qa/precisao-prazo-runtime-reteste.json').write_text(json.dumps(dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),schema=SCHEMA,baseUrl=args.base_url,cases=cases,checks=results),ensure_ascii=False,indent=2),encoding='utf8')
print(json.dumps(cases,ensure_ascii=False,indent=2))
