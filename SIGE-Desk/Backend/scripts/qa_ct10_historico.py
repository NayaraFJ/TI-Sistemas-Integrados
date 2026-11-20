"""Confronto somente leitura da fixture SIGE-1481 com registro anterior."""
import ast, datetime, hashlib, json, os, subprocess
from pathlib import Path

schema='sige_desk_qa_20261118'; ticket_id='327e60de-cb22-4d68-9acd-45cb00787d44'
mysql=r'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe'
def sql(q):
    return subprocess.check_output([mysql,'--user=root','--host=127.0.0.1','--batch','--raw','--skip-column-names','--default-character-set=utf8mb4',schema,'--execute='+q],env={**os.environ,'MYSQL_PWD':os.environ.get('SIGE_QA_DB_PASSWORD','123')}).decode('utf8').strip()
assert sql('SELECT DATABASE()')==schema
source=Path(__file__).with_name('review_flows.py').read_text(encoding='utf8')
exec(compile(source[:source.index('for name,fn in [')],'review_flows.py','exec'),globals())
started=datetime.datetime.now(datetime.timezone.utc).isoformat()
old_report=json.loads(Path('../docs/qa/ct10-calendario-runtime-atualizado-reteste-20261119.json').read_text(encoding='utf8'))
prior=ast.literal_eval(next(x['detail'] for x in old_report['checks'] if x['name']=='Novo ciclo usa snapshot atualizado e preserva primeira resposta'))
api=a.request('/tickets/'+ticket_id)
rows=[json.loads(x) for x in sql("SELECT JSON_OBJECT('id',id,'action',action,'field',field_name,'old',old_value,'new',new_value,'created',DATE_FORMAT(created_at,'%Y-%m-%dT%H:%i:%s.%fZ')) FROM ticket_history WHERE ticket_id='"+ticket_id+"' AND action IN ('RESOLUTION_CYCLE_CLOSED','RESOLUTION_DEADLINE_CHANGED') ORDER BY created_at,id").splitlines()]
closed=next(x for x in rows if x['action']=='RESOLUTION_CYCLE_CLOSED'); history=next(x for x in api['history'] if x['id']==closed['id'])
payload=closed['new']; snapshot=json.loads(payload['slaSnapshot']); api_payload=json.loads(history['newValue'])
prior_deadlines=[x for x in rows if x['action']=='RESOLUTION_DEADLINE_CHANGED' and x['created']<closed['created']]
latest=prior_deadlines[-1]
current=json.loads(sql("SELECT sla_rule_snapshot FROM tickets WHERE id='"+ticket_id+"'"))
check('Evento do histórico API corresponde ao MySQL',history['action']==closed['action'] and history['fieldName']==closed['field'] and api_payload==payload,dict(api=history,mysql=closed))
check('Snapshot encerrado igual ao ciclo 1 registrado anteriormente',snapshot==prior['cycle1']['snapshot'] and snapshot['version']==2,dict(closed=snapshot,prior=prior['cycle1']['snapshot']))
check('Prazo encerrado igual à última alteração anterior',payload['cycle']==1 and payload['priority']=='HIGH' and payload['resolutionDueAt']==latest['new'],dict(cycle=payload,latestDeadline=latest))
check('Ciclo atual corresponde ao snapshot e prazo do ciclo 2',api['resolutionCycle']==2 and current==prior['cycle2']['snapshot'] and current['version']==3 and api['resolutionDueAt']==prior['observed'],dict(current=current,cycle2=prior['cycle2'],resolutionDueAt=api['resolutionDueAt']))
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),schema=schema,baseUrl=args.base_url,fixture=ticket_id,source='../docs/qa/ct10-calendario-runtime-atualizado-reteste-20261119.json',checks=results)
output=Path(args.output)
if output.exists():raise SystemExit('Evidência já existe')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf8')
print('PASSOU',len(results),'verificações')
