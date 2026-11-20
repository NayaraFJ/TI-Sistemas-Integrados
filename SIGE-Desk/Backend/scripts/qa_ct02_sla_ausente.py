"""CT-02: banco e servidor exclusivos, nunca executar contra ambiente compartilhado."""
import datetime, hashlib, os, subprocess
from pathlib import Path

schema='sige_qa_ct02_isolado_20261119'
mysql=r'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe'
def sql(q):
    return subprocess.check_output([mysql,'--user=root','--host=127.0.0.1','--batch','--skip-column-names','--default-character-set=utf8mb4',schema,'--execute='+q],env={**os.environ,'MYSQL_PWD':os.environ.get('SIGE_QA_DB_PASSWORD','123')}).decode('utf8').strip()
assert sql('SELECT DATABASE()')==schema
source=Path(__file__).with_name('review_flows.py').read_text(encoding='utf8')
source=source[:source.index('for name,fn in [')].replace('http://localhost:8081/api/v1','http://localhost:8082/api/v1')
exec(compile(source,'review_flows.py','exec'),globals())
started=datetime.datetime.now(datetime.timezone.utc).isoformat()
rules=a.request('/sla-rules'); defaults=[r for r in rules if r['scope']=='DEFAULT' and r['active']]
check('Uma regra padrão ativa',len(defaults)==1)
rule=defaults[0]
blocked=a.request('/sla-rules/'+rule['id'],{**rule,'active':False},'PUT',409)
check('Última regra permanece ativa',sql("SELECT COUNT(*) FROM sla_rules WHERE scope='DEFAULT' AND active=1")=='1',blocked)
tp=new_type(False,False);t=create_ticket(tp);start(t);before=detail(t)
sql('UPDATE sla_rules SET active=0')
check('Fixture isolada sem regra ativa',sql('SELECT COUNT(*) FROM sla_rules WHERE active=1')=='0')
payload=dict(priority='HIGH',assigneeId=manager.me['id'],campaignId=campaign['id'],demandTypeId=tp['id'],effectiveResponse='Retorno QA',sendToExecution=True)
response=staff.request('/tickets/'+t['id']+'/triage',payload,'POST',409,version=before['ticket']['version'])
read_response=staff.request('/tickets/'+t['id'],expected=409)
check('Triagem e leitura sem SLA retornam erro explícito',response['message']=='Regra padrão de SLA não encontrada' and read_response['message']==response['message'],dict(ticket=t['id'],triage=response,read=read_response,before=before))
check('Banco mantém ausência de classificação',sql("SELECT CONCAT(status,'|',COALESCE(priority,'NULL'),'|',COALESCE(assignee_id,'NULL'),'|',IF(sla_rule_snapshot IS NULL,'NULL','SET')) FROM tickets WHERE id='"+t['id']+"'")=='TRIAGE|NULL|NULL|NULL')
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),schema=schema,baseUrl=args.base_url,runtimeSha256=hashlib.sha256(Path('target/qa-runtime.jar').read_bytes()).hexdigest(),checks=results)
output=Path(args.output)
if output.exists():raise SystemExit('Evidência já existe')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf8')
print('PASSOU',len(results),'verificações')
