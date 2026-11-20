import json, datetime, hashlib
from pathlib import Path
src=Path('scripts/qa_functional.py').read_text(encoding='utf-8')
exec(compile(src[:src.index('suite=[')],'scripts/qa_functional.py','exec'),globals())
u=t=None; evidence={}; error=None
try:
 email='ct07-batch-'+uuid.uuid4().hex+'@example.test'
 u=a.request('/users',dict(name='QA CT07 batch',email=email,password='ReviewPass123!',role='CLIENT',clientId=client_id),'POST',201)
 account=Account(email,'ReviewPass123!')
 check('Usuário exclusivo sem avisos',len(all_items(account,'/notifications'))==0 and sql("SELECT COUNT(*) FROM notifications WHERE user_id='"+u['id']+"'")=='0')
 tp=next(x for x in a.request('/demand-types') if x.get('active',True) and not json.loads(x.get('fieldDefinitions') or '[]'))
 t=create_ticket(tp,actor=account);start(t);triage(t,tp)
 action(manager,t,'comments',{'body':'CT07 lote fixture exclusiva','effectiveResponse':False},201)
 before=all_items(account,'/notifications'); ids={x['id'] for x in before}
 db_before=json.loads(sql("SELECT JSON_OBJECT('total',COUNT(*),'unread',SUM(read_at IS NULL)) FROM notifications WHERE user_id='"+u['id']+"'"))
 check('Múltiplos avisos exclusivos não lidos',len(ids)>=2 and all(x['ticketId']==t['id'] and x['readAt'] is None for x in before) and db_before['total']==db_before['unread']==len(ids),db_before)
 account.request('/notifications/read-all',method='POST',expected=204)
 immediate=all_items(account,'/notifications');check('Lote marca todos', {x['id'] for x in immediate}==ids and all(x['readAt'] is not None for x in immediate))
 account.request('/auth/logout',method='POST',expected=204)
 fresh=Account(email,'ReviewPass123!');after=all_items(fresh,'/notifications');unread=all_items(fresh,'/notifications?unread=true')
 db=json.loads(sql("SELECT JSON_OBJECT('total',COUNT(*),'read',SUM(read_at IS NOT NULL)) FROM notifications WHERE user_id='"+u['id']+"'"))
 check('Nova sessão mantém leitura', {x['id'] for x in after}==ids and all(x['readAt'] is not None for x in after),{'count':len(after),'read':sum(x['readAt'] is not None for x in after)})
 check('Filtro não lidas vazio',len(unread)==0,len(unread));check('MySQL confirma leitura',db['total']==db['read']==len(ids),db)
 evidence=dict(userId=u['id'],ticketId=t['id'],ticketNumber=t['number'],notificationIds=sorted(ids),beforeDb=db_before,afterDb=db,unreadAfter=len(unread),readAtAfter={x['id']:x['readAt'] for x in after})
 fresh.request('/auth/logout',method='POST',expected=204)
except Exception as exc:error=repr(exc)
finally:
 try:
  if t:
   tid=t['id']
   for table,col in [('notifications','ticket_id'),('outbox_events','aggregate_id'),('ticket_history','ticket_id'),('ticket_comments','ticket_id'),('security_access_events','resource_id'),('tickets','id')]:sql("DELETE FROM "+table+" WHERE "+col+"='"+tid+"'")
  if u:sql("DELETE FROM users WHERE id='"+u['id']+"'")
  check('Fixture removida', (not t or sql("SELECT COUNT(*) FROM tickets WHERE id='"+t['id']+"'")=='0') and (not u or sql("SELECT COUNT(*) FROM users WHERE id='"+u['id']+"'")=='0'))
 except Exception as exc:error=(error or '')+' cleanup: '+repr(exc)
 report=dict(at=datetime.datetime.now(datetime.timezone.utc).isoformat(),baseUrl=args.base_url,schema=SCHEMA,mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(Path('target/qa-runtime.jar').read_bytes()).hexdigest(),evidence=evidence,checks=results,error=error)
 Path('../docs/qa/ct07-leitura-lote-nova-sessao-20261119.json').write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
 print(json.dumps(report,ensure_ascii=False,indent=2))
 if error:raise SystemExit(1)
