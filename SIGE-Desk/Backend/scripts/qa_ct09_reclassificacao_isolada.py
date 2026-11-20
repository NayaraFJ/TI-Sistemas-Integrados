"""Reteste local: API dedicada e schema novo, sem usar a instância compartilhada."""
import hashlib, json, os, subprocess, sys, time, urllib.request
from pathlib import Path

schema='sige_desk_qa_labels_reteste2_20261119'
port=18089
jar=Path('target/sige-desk-api-0.1.0-SNAPSHOT.jar').resolve()
mysql=r'C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe'
env={**os.environ,'MYSQL_PWD':os.environ.get('SIGE_QA_DB_PASSWORD','123')}
def sql(query):
    return subprocess.check_output([mysql,'--user=root','--host=127.0.0.1','--batch','--skip-column-names','--default-character-set=utf8mb4','--execute='+query],env=env).decode('utf-8').strip()
if sql("SELECT COUNT(*) FROM information_schema.schemata WHERE schema_name='"+schema+"'")!='0':
    raise SystemExit('Schema de teste já existe; interrompido sem alterações.')
try:
    urllib.request.urlopen('http://127.0.0.1:'+str(port)+'/actuator/health',timeout=1)
except Exception: pass
else: raise SystemExit('Porta ocupada; interrompido.')
env.update(SIGE_DB_URL='jdbc:mysql://localhost:3306/'+schema+'?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC',SIGE_DB_PASSWORD=env['MYSQL_PWD'],SIGE_STORAGE_PATH='target/qa-labels-uploads')
log=Path('target/qa-labels-runtime.log').open('w',encoding='utf-8')
proc=subprocess.Popen([r'C:\Program Files\Java\jdk-25\bin\java.exe','-jar',str(jar),'--spring.profiles.active=demo','--server.port='+str(port)],env=env,stdout=log,stderr=subprocess.STDOUT)
try:
    for _ in range(90):
        if proc.poll() is not None: raise RuntimeError('Instância encerrou durante inicialização; consultar log.')
        try:
            with urllib.request.urlopen('http://127.0.0.1:'+str(port)+'/actuator/health',timeout=1) as response:
                if response.status==200 and 'Carga demo aplicada' in Path('target/qa-labels-runtime.log').read_text(encoding='utf-8'): break
        except Exception: time.sleep(1)
    else: raise RuntimeError('Instância não ficou pronta.')
    source=Path('scripts/review_flows.py').read_text(encoding='utf-8')
    source=source[:source.index('def main_flow():')]
    source=source.replace("default='http://localhost:8081/api/v1'", "default='http://127.0.0.1:18089/api/v1'")
    source=source.replace("if args.base_url!='http://localhost:8081/api/v1'", "if args.base_url!='http://127.0.0.1:18089/api/v1'")
    sys.argv=[sys.argv[0]]
    exec(compile(source,'review_flows.py','exec'),globals())
    original=a.request('/demand-types',dict(name=tag+' original',approvalRequired=False,evidenceRequired=False,fieldDefinitions='[{"name":"brief","label":"Arquivo original","type":"TEXT","required":false}]'),'POST',201)
    new=a.request('/demand-types',dict(name=tag+' novo',approvalRequired=True,evidenceRequired=True,fieldDefinitions='[{"name":"brief","label":"Arquivo novo","type":"TEXT","required":false}]'),'POST',201)
    ticket=c.request('/tickets',dict(clientId=client_id,campaignId=campaign['id'],demandTypeId=original['id'],channel='Google Ads',subject=tag,description='Fixture sintética',urgency='HIGH',metrics={'fields':{'brief':'valor-sintetico'}}),'POST',201)
    check('Rótulo original na abertura',detail(ticket)['fieldLabels']['brief']=='Arquivo original')
    start(ticket);triage(ticket,new)
    fresh=Account('cliente@aurora.demo'); observed=fresh.request('/tickets/'+ticket['id'])
    check('Nova sessão mantém rótulo original',observed['fieldLabels']['brief']=='Arquivo original')
    check('Nova sessão mantém valor original',json.loads(observed['metrics'])['fields']['brief']=='valor-sintetico')
    row=json.loads(sql("SELECT type_snapshot FROM "+schema+".tickets WHERE id='"+ticket['id']+"'"))
    check('Snapshot no MySQL tem regras do novo tipo',row['id']==new['id'] and row['approvalRequired'] and row['evidenceRequired'] and row['fieldDefinitions'][0]['label']=='Arquivo novo')
    check('Snapshot no MySQL preserva definição original',row['originalFieldDefinitions'][0]['label']=='Arquivo original')
    action(manager,ticket,'execution',{'actionDescription':'Sem evidência'},400)
    action(manager,ticket,'execution',{'actionDescription':'Com evidência','evidenceUrl':'https://example.test/evidencia'})
    check('Nova regra exige aprovação',fresh.request('/tickets/'+ticket['id'])['ticket']['status']=='VALIDATION')
    output={'schema':schema,'port':port,'jarSha256':hashlib.sha256(jar.read_bytes()).hexdigest(),'ticket':ticket['number'],'checks':results}
    Path('target/qa-ct09-reclassificacao-isolada.json').write_text(json.dumps(output,ensure_ascii=False,indent=2),encoding='utf-8')
    print('PASSOU',len(results),'verificações; evidência em target/qa-ct09-reclassificacao-isolada.json')
finally:
    proc.terminate()
    try: proc.wait(timeout=15)
    except subprocess.TimeoutExpired: proc.kill();proc.wait()
    log.close()
