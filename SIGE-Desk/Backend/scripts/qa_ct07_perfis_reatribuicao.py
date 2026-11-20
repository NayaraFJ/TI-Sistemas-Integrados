import json, datetime
from pathlib import Path
src=Path('scripts/qa_functional.py').read_text(encoding='utf-8')
exec(compile(src[:src.index('suite=[')],'scripts/qa_functional.py','exec'),globals())
t=None
try:
 tp=next(x for x in a.request('/demand-types') if x.get('active',True))
 t=create_ticket(tp); start(t); triage(t,tp)
 def rows():
  s=sql("SELECT JSON_OBJECT('id',id,'userId',user_id,'event',event_type,'readAt',read_at) FROM notifications WHERE ticket_id='"+t['id']+"'")
  return [json.loads(x) for x in s.splitlines() if x]
 before=rows(); assigned=[x for x in before if x['event']=='ASSIGNED']
 expected={u['id'] for u in users if u['active'] and u['role'] in ['ADMIN','SERVICE']}|{c.me['id'],manager.me['id']}
 check('Atribuição: destinatários exatos', {x['userId'] for x in assigned}==expected,dict(expected=sorted(expected),actual=sorted(x['userId'] for x in assigned)))
 old=next(x for x in assigned if x['userId']==manager.me['id'])
 action(staff,t,'reassign',dict(assigneeId=other_manager.me['id'],reason='Reatribuição isolada CT07'))
 action(other_manager,t,'comments',{'body':'Comentário após reatribuição CT07','effectiveResponse':False},201)
 later=[x for x in rows() if x['event']=='COMMENT_ADDED']
 expected2=(expected-{manager.me['id']})|{other_manager.me['id']}
 check('Comentário após reatribuição: destinatários exatos',{x['userId'] for x in later}==expected2,dict(expected=sorted(expected2),actual=sorted(x['userId'] for x in later)))
 check('Gestor anterior não vê notificação antiga', all(x['ticketId']!=t['id'] for x in all_items(manager,'/notifications')))
 manager.request('/notifications/'+old['id']+'/read',method='POST',expected=404)
 manager.request('/tickets/'+t['id'],expected=404)
 check('Novo gestor vê aviso', any(x['ticketId']==t['id'] and x['eventType']=='COMMENT_ADDED' for x in all_items(other_manager,'/notifications')))
 note=next(x for x in later if x['userId']==other_manager.me['id'])
 other_manager.request('/notifications/'+note['id']+'/read',method='POST',expected=204)
 fresh=Account('gestor2@sige.demo')
 after=all_items(fresh,'/notifications');check('Leitura individual persiste em nova sessão',any(x['id']==note['id'] and x['readAt'] is not None for x in after))
 unread=all_items(fresh,'/notifications?unread=true');check('Filtro exclui aviso lido',all(x['id']!=note['id'] and x['readAt'] is None for x in unread))
 pages=[];page=0
 while True:
  p=fresh.request('/notifications?size=7&page='+str(page));pages+=p['items'];page+=1
  if len(pages)>=p['total']:break
 check('Paginação retorna conjunto completo sem duplicatas',len(pages)==len(after)==len({x['id'] for x in pages}) and {x['id'] for x in pages}=={x['id'] for x in after},dict(total=len(after),pages=page))
finally:
 if t:
  tid=t['id']
  for q in ["DELETE FROM notifications WHERE ticket_id='"+tid+"'", "DELETE FROM outbox_events WHERE aggregate_id='"+tid+"'", "DELETE FROM ticket_history WHERE ticket_id='"+tid+"'", "DELETE FROM ticket_comments WHERE ticket_id='"+tid+"'", "DELETE FROM security_access_events WHERE resource_id='"+tid+"'", "DELETE FROM tickets WHERE id='"+tid+"'"]:sql(q)
  check('Fixture removida',sql("SELECT COUNT(*) FROM tickets WHERE id='"+tid+"'")=='0',tid)
 Path('../docs/qa/ct07-perfis-reatribuicao-20261119.json').write_text(json.dumps(dict(at=datetime.datetime.now(datetime.timezone.utc).isoformat(),baseUrl=args.base_url,schema=SCHEMA,ticket=t['id'] if t else None,checks=results),ensure_ascii=False,indent=2),encoding='utf-8')
 print(json.dumps(results,ensure_ascii=False,indent=2))
