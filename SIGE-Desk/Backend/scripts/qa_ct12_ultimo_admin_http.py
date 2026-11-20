import os,subprocess,json,datetime,pathlib,hashlib
os.chdir(pathlib.Path(__file__).resolve().parents[1]);src=pathlib.Path('scripts/review_flows.py').read_text(encoding='utf8');src=src[:src.index('a=Account(')].replace('8081','8082');exec(compile(src,'review_flows.py','exec'))
mysql=r'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe';env={**os.environ,'MYSQL_PWD':os.environ.get('SIGE_QA_DB_PASSWORD','123')}
def sql(q):return subprocess.check_output([mysql,'--user=root','--host=127.0.0.1','--batch','--skip-column-names','sige_desk_qa_admin_20261119','--execute='+q],env=env).decode().strip()
started=datetime.datetime.now(datetime.timezone.utc).isoformat();error=None;before=after=response=None
try:
 check('Schema isolado confirmado',sql('SELECT DATABASE()')=='sige_desk_qa_admin_20261119')
 a=Account('admin@sige.demo'); users=a.request('/users');admins=[x for x in users if x['role']=='ADMIN' and x['active']];check('Único Admin ativo identificado',len(admins)==1,{'count':len(admins)})
 admin=admins[0];uid=admin['id'];before=sql("SELECT CONCAT(active,':',role) FROM users WHERE id='"+uid+"'");check('Estado inicial persistido ativo/Admin',before=='1:ADMIN',before)
 response=a.request('/users/'+uid,{**admin,'active':False},'PUT',409)
 after=sql("SELECT CONCAT(active,':',role) FROM users WHERE id='"+uid+"'");check('Estado persistido preservado',before==after,{'before':before,'after':after})
 check('Sessão ainda acessa usuário ativo',a.request('/auth/me')['id']==uid)
 check('Um único Admin ativo permanece',sql("SELECT COUNT(*) FROM users WHERE role='ADMIN' AND active=1")=='1')
except Exception as e:error=repr(e)
out={'startedAt':started,'finishedAt':datetime.datetime.now(datetime.timezone.utc).isoformat(),'baseUrl':args.base_url,'schema':'sige_desk_qa_admin_20261119','storage':'target/qa-admin-uploads','runtimeSha256':hashlib.sha256(pathlib.Path('target/qa-runtime.jar').read_bytes()).hexdigest(),'mysql':sql('SELECT VERSION()'),'before':before,'after':after,'response':response,'checks':results,'error':error,'retention':'Schema isolado e uploads preservados para auditoria'}
pathlib.Path('../docs/qa/ct12-ultimo-admin-http-20261119.json').write_text(json.dumps(out,ensure_ascii=False,indent=2),encoding='utf8');print(json.dumps({'error':error,'checks':len(results),'before':before,'after':after,'response':response},ensure_ascii=False))
