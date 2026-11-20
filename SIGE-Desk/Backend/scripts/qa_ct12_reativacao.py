import os,subprocess,json,datetime,pathlib
os.chdir(pathlib.Path(__file__).resolve().parents[1]); source=pathlib.Path('scripts/review_flows.py').read_text(encoding='utf8');exec(compile(source[:source.index('a=Account(')],'review_flows.py','exec'))
mysql=r'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe';env={**os.environ,'MYSQL_PWD':os.environ.get('SIGE_QA_DB_PASSWORD','123')}
def sql(q):return subprocess.check_output([mysql,'--user=root','--host=127.0.0.1','--batch','--skip-column-names','sige_desk_qa_20261118','--execute='+q],env=env).decode().strip()
assert sql('SELECT DATABASE()')=='sige_desk_qa_20261118'
started=datetime.datetime.now(datetime.timezone.utc).isoformat(); tag='CT12-'+uuid.uuid4().hex[:10];org=camp=None;error=None;cleanup=[]
try:
 a=Account('admin@sige.demo');staff=Account('atendimento@sige.demo');c=Account('cliente@aurora.demo');manager=Account('gestor@sige.demo')
 org=a.request('/clients',{'name':tag,'contactName':'Contato QA','email':tag.lower()+'@example.test','phone':None},'POST',201)
 body={'clientId':org['id'],'name':tag,'channel':'Google Ads','objective':'Fixture QA'};camp=a.request('/campaigns',body,'POST',201)
 for actor in [c,manager]:
  actor.request('/campaigns',body,'POST',403);actor.request('/clients/'+org['id']+'/active?value=false',method='POST',expected=403)
 for resource,obj,key in [('campaigns',camp,'campaigns'),('clients',org,'clients')]:
  a.request('/'+resource+'/'+obj['id']+'/active?value=false',method='POST');check(resource+' inativo ausente em referência',obj['id'] not in {x['id'] for x in a.request('/reference')[key]})
  a.request('/'+resource+'/'+obj['id']+'/active?value=true',method='POST');check(resource+' reativado presente em referência',obj['id'] in {x['id'] for x in a.request('/reference')[key]})
 check('Persistência MySQL',sql("SELECT CONCAT(c.active,':',p.active) FROM clients c JOIN campaigns p ON p.client_id=c.id WHERE c.id='"+org['id']+"' AND p.id='"+camp['id']+"'")=='1:1')
except Exception as e:error=repr(e)
finally:
 try:
  if camp:sql("DELETE FROM campaigns WHERE id='"+camp['id']+"' AND name='"+tag+"'");cleanup.append('campanha removida')
  if org:sql("DELETE FROM clients WHERE id='"+org['id']+"' AND name='"+tag+"'");cleanup.append('organização removida');cleanup.append('remanescente='+sql("SELECT COUNT(*) FROM clients WHERE id='"+org['id']+"'"))
 except Exception as e:cleanup.append('falha: '+repr(e))
 pathlib.Path('../docs/qa/ct12-reativacao-20261119.json').write_text(json.dumps({'inicio':started,'fim':datetime.datetime.now(datetime.timezone.utc).isoformat(),'api':'http://localhost:8081/api/v1','schema':'sige_desk_qa_20261118','tag':tag,'checks':results,'erro':error,'limpeza':cleanup},ensure_ascii=False,indent=2),encoding='utf8');print(json.dumps({'erro':error,'checks':len(results),'limpeza':cleanup},ensure_ascii=False))
