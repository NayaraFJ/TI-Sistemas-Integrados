"""Reteste local: API dedicada e schema novo, sem usar a instância compartilhada."""
import hashlib, json, os, subprocess, sys, time, urllib.request
from pathlib import Path

schema='sige_desk_qa_ct09_rollback_20261119'
port=18091
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
env.update(SIGE_DB_URL='jdbc:mysql://localhost:3306/'+schema+'?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC',SIGE_DB_PASSWORD=env['MYSQL_PWD'],SIGE_STORAGE_PATH='target/qa-ct09-rollback-uploads')
log=Path('target/qa-ct09-rollback-runtime.log').open('w',encoding='utf-8')
proc=subprocess.Popen([r'C:\Program Files\Java\jdk-25\bin\java.exe','-jar',str(jar),'--spring.profiles.active=demo','--server.port='+str(port)],env=env,stdout=log,stderr=subprocess.STDOUT)
try:
    for _ in range(90):
        if proc.poll() is not None: raise RuntimeError('Instância encerrou durante inicialização; consultar log.')
        try:
            with urllib.request.urlopen('http://127.0.0.1:'+str(port)+'/actuator/health',timeout=1) as response:
                if response.status==200 and 'Carga demo aplicada' in Path('target/qa-ct09-rollback-runtime.log').read_text(encoding='utf-8'): break
        except Exception: time.sleep(1)
    else: raise RuntimeError('Instância não ficou pronta.')
    source=Path('scripts/review_flows.py').read_text(encoding='utf-8')
    source=source[:source.index('def main_flow():')]
    source=source.replace("default='http://localhost:8081/api/v1'", "default='http://127.0.0.1:18091/api/v1'")
    source=source.replace("if args.base_url!='http://localhost:8081/api/v1'", "if args.base_url!='http://127.0.0.1:18091/api/v1'")
    sys.argv=[sys.argv[0]]
    exec(compile(source,'review_flows.py','exec'),globals())
    import datetime
    root=Path('target/qa-ct09-rollback-uploads'); root.mkdir(parents=True,exist_ok=True)
    fields=[dict(name='brief',label='Briefing',type='FILE',required=True)]
    tp=a.request('/demand-types',dict(name=tag+' rollback',approvalRequired=False,evidenceRequired=False,fieldDefinitions=json.dumps(fields)),'POST',201)
    subject=tag+' rollback segundo arquivo'
    payload=dict(clientId=client_id,campaignId=campaign['id'],demandTypeId=tp['id'],channel='Google Ads',subject=subject,description='Fixture sintética',urgency='LOW',metrics={'fields':{'brief':'brief.csv'}})
    def inventory():
        return {str(x.relative_to(root)):hashlib.sha256(x.read_bytes()).hexdigest() for x in sorted(root.rglob('*')) if x.is_file()}
    def multipart(files,expected):
        b='qa'+uuid.uuid4().hex
        chunks=[('--'+b+'\r\nContent-Disposition: form-data; name="request"\r\nContent-Type: application/json\r\n\r\n').encode()+json.dumps(payload).encode()]
        for name,filename,mime,content in files:
            chunks.append(('--'+b+'\r\nContent-Disposition: form-data; name="'+name+'"; filename="'+filename+'"\r\nContent-Type: '+mime+'\r\n\r\n').encode()+content)
        return c.request('/tickets',method='POST',expected=expected,raw=b'\r\n'.join(chunks)+('\r\n--'+b+'--\r\n').encode(),content_type='multipart/form-data; boundary='+b)
    valid=('field.brief','brief.csv','text/csv',b'campo;valor\ncontrole;1\n')
    good=multipart([valid],201)
    check('Controle positivo possui anexo',len(detail(good)['attachments'])==1)
    payload['subject']=subject+' negativa'
    before=inventory()
    count_before=int(sql("SELECT COUNT(*) FROM "+schema+".tickets WHERE subject='"+payload['subject']+"'"))
    error=multipart([valid,('files','invalid.exe','application/octet-stream',b'invalid')],400)
    after=inventory()
    count_after=int(sql("SELECT COUNT(*) FROM "+schema+".tickets WHERE subject='"+payload['subject']+"'"))
    check('Inventário de caminhos e hashes preservado',before==after,dict(before=before,after=after))
    check('Ticket ausente antes e depois',count_before==0 and count_after==0,dict(before=count_before,after=count_after))
    output=dict(timestamp=datetime.datetime.now(datetime.timezone.utc).isoformat(),schema=schema,port=port,storage=str(root),jarSha256=hashlib.sha256(jar.read_bytes()).hexdigest(),subject=payload['subject'],positiveTicket=good['id'],response=error,inventoryBefore=before,inventoryAfter=after,checks=results)
    Path('../docs/qa/ct09-rollback-inventario-20261119.json').write_text(json.dumps(output,ensure_ascii=False,indent=2),encoding='utf-8')
    print('PASSOU',len(results),'verificações')
finally:
    proc.terminate()
    try: proc.wait(timeout=15)
    except subprocess.TimeoutExpired: proc.kill();proc.wait()
    log.close()
