from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())

def metrics_test():
    tp=new_type(False,False)
    body=dict(clientId=client_id,campaignId=campaign['id'],demandTypeId=tp['id'],channel='Google Ads',subject=tag+' métricas',description='Sintético',urgency='LOW',metrics=dict(period='novembro 2026',impressions=0,ctr=2.75,cpc=1.23,conversions=12,cpa=4.56,roas=None,fields={}))
    t=c.request('/tickets',body,'POST',201)
    saved=json.loads(Account(c.email).request('/tickets/'+t['id'])['metrics'])
    check('Métricas em nova sessão',all(saved.get(k)==v for k,v in body['metrics'].items()),dict(ticket=t['id'],sent=body['metrics'],saved=saved))
    before=int(sql('SELECT COUNT(*) FROM tickets'))
    for field,value in [('ctr',-1),('cpc','inválido'),('impressions',-1)]:
        invalid={**body,'subject':tag+' inválido '+field,'metrics':{**body['metrics'],field:value}}
        c.request('/tickets',invalid,'POST',400)
    after=int(sql('SELECT COUNT(*) FROM tickets'))
    check('Inválidos sem gravação parcial',before==after,dict(before=before,after=after))

def csv_test():
    tp=new_type(False,False)
    subjects=[tag+' ação; "cotação"\nsegunda linha']+[tag+' linha '+str(i) for i in range(100)]
    tickets=[create_ticket(tp,subject=s) for s in subjects]
    q=urllib.parse.urlencode({'search':tag,'demandTypeId':tp['id']})
    expected={t['number'] for t in tickets}
    listed=all_items(a,'/tickets?'+q)
    check('Seleção filtrada em múltiplas páginas',len(listed)==101 and {t['number'] for t in listed}==expected,dict(count=len(listed),expected=sorted(expected),obtained=sorted(t['number'] for t in listed)))
    for account in [a,staff]:
        data=account.request('/reports/tickets/export?'+q)
        rows=list(csv.reader(io.StringIO(data.decode('utf-8-sig'),newline=''),delimiter=';'))
        check('CSV seleção e estrutura '+account.email,len(rows)==102 and len({len(r) for r in rows})==1 and {r[0] for r in rows[1:]}==expected,dict(header=rows[0],count=len(rows)-1,ids=[r[0] for r in rows[1:]]))
        check('CSV escape e acentos '+account.email,any(subjects[0] in row for row in rows[1:]),dict(special=subjects[0],row=next((r for r in rows if r[0]==tickets[0]['number']),None)))
        Path('../docs/qa/ct17-selecao-'+('admin' if account==a else 'atendimento')+'-20261119.csv').write_bytes(data)
        empty=account.request('/reports/tickets/export?'+urllib.parse.urlencode({'search':tag+' inexistente'}))
        check('CSV vazio '+account.email,len(list(csv.reader(io.StringIO(empty.decode('utf-8-sig')),delimiter=';')))==1)
    for account in [c,manager]:
        for path in ['/reports/tickets','/reports/tickets/export','/reports/tickets/export?'+q+'&clientId='+client_id]:account.request(path,expected=403)

for case,fn in [('CT-16-api-complemento',metrics_test),('CT-17-api-complemento',csv_test)]:run_case(case,'Baixa',fn)
runtime=Path('target/qa-runtime.jar')
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag,layer='HTTP/MySQL/CSV; sem navegador'),cases=cases,checks=results)
output=Path(args.output)
if output.exists():raise SystemExit('Evidência já existe')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
