from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
observed={}
def rows_for(ticket):
 q="SELECT JSON_OBJECT('id',h.id,'actorId',h.actor_id,'actorName',u.name,'action',h.action,'fieldName',h.field_name,'oldValue',h.old_value,'newValue',h.new_value,'reason',h.reason,'createdAt',DATE_FORMAT(h.created_at,'%Y-%m-%dT%H:%i:%s.%fZ')) FROM ticket_history h LEFT JOIN users u ON u.id=h.actor_id WHERE h.ticket_id='"+ticket+"'"
 raw=subprocess.check_output([MYSQL,'--user=root','--host=127.0.0.1','--batch','--raw','--skip-column-names','--default-character-set=utf8mb4',SCHEMA,'--execute='+q],env={**os.environ,'MYSQL_PWD':os.environ.get('SIGE_QA_DB_PASSWORD','123')}).decode('utf8'); return [json.loads(line) for line in raw.splitlines()]
def match(hist,rows):
 db={x['id']:x for x in rows}; errors=[]
 for h in hist:
  row=db.get(h['id']);
  if not row or any([row['actorName']!=h['actorName'],row['action']!=h['action'],row['fieldName']!=h['fieldName'],row['oldValue']!=(json.loads(h['oldValue']) if h['oldValue'] is not None else None),row['newValue']!=(json.loads(h['newValue']) if h['newValue'] is not None else None),row['reason']!=h['reason'],row['createdAt']!=h['createdAt']]): errors.append(h['id'])
 return errors

def execute():
 prior=json.loads(Path('SIGE-Desk/docs/qa/ct13-matriz-20261119.json').read_text(encoding='utf8')); priorhistory=prior['steps'][-1]['history'];priorrows=rows_for(prior['steps'][0]['ticket']['id']);byid={r['id']:r for r in priorrows}
 errors=match(priorhistory,priorrows);check('Primeira fixture confere eventos e nomes pela junção',not errors and len(priorrows)==len(priorhistory),errors)
 expected={'TICKET_CREATED':c.me['id'],'TRIAGE_STARTED':staff.me['id'],'TRIAGE_CLASSIFIED':staff.me['id'],'COMPLEMENT_REQUESTED':other_manager.me['id'],'COMPLEMENT_RECEIVED':c.me['id'],'TICKET_RESUMED':staff.me['id'],'EXECUTION_RECORDED':other_manager.me['id'],'APPROVED':c.me['id'],'REOPENED':c.me['id'],'RESOLUTION_CYCLE_CLOSED':c.me['id']}
 mismatches=[dict(action=h['action'],actual=byid[h['id']]['actorId'],expected=expected[h['action']]) for h in priorhistory if h['action'] in expected and byid[h['id']]['actorId']!=expected[h['action']]]
 check('Atores da primeira fixture correspondem às sessões',not mismatches,mismatches)
 tp=new_type(False,False); t=create_ticket(tp);start(t);triage(t,tp);initial=detail(t);actors=[c,staff,manager]
 for i in range(120): action(actors[i%3],t,'comments',{'body':'Histórico extenso CT13 %03d'%i,'effectiveResponse':False},201)
 data=detail(t);history=data['history'];rows=rows_for(t['id']);errors=match(history,rows)
 check('Histórico extenso retorna todos eventos MySQL',not errors and len(history)==len(rows) and len(history)>=120,dict(api=len(history),mysql=len(rows),errors=errors))
 check('Histórico não duplica eventos',len({h['id'] for h in history})==len(history))
 check('Histórico preserva eventos anteriores',{h['id'] for h in initial['history']}.issubset({h['id'] for h in history}))
 check('Ordenação temporal decrescente',all(x['createdAt']>=y['createdAt'] for x,y in zip(history,history[1:])))
 db={r['id']:r for r in rows};counts={actor.me['id']:0 for actor in actors}
 for h in history:
  if h['action']=='COMMENT_ADDED':counts[db[h['id']]['actorId']]+=1
 check('120 comentários auditam as três sessões',all(v==40 for v in counts.values()),counts)
 other.request('/tickets/'+t['id'],expected=404)
 observed.update(ticket=t['id'],history=history,dbRows=rows,priorActorRows=priorrows,commentActors=counts,contract='Detalhe retorna lista de histórico, sem endpoint paginado no contrato implementado.')
run_case('CT-13-historico-extenso-atores','Alta',execute)
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,mysql=sql('SELECT VERSION()'),runTag=tag),cases=cases,checks=results,observed=observed)
out=Path(args.output)
if out.exists():raise SystemExit('Evidência já existe')
out.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf8');print(json.dumps(cases,ensure_ascii=False))

