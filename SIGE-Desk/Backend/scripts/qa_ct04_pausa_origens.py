from pathlib import Path
from zoneinfo import ZoneInfo
source=Path('scripts/qa_functional.py').read_text(encoding='utf-8'); exec(compile(source[:source.index('suite=[')],'scripts/qa_functional.py','exec'),globals())
source=Path('scripts/qa_ct10_calendario_ciclos.py').read_text(encoding='utf-8'); exec(compile(source[source.index('def parse('):source.index('def correlation(')],'oracle','exec'),globals())

def run():
    # First ticket proves the API's database before any SQL update.
    tp=new_type(False,False); probe=create_ticket(tp)
    check('Ticket API presente no schema QA',sql("SELECT COUNT(*) FROM tickets WHERE id='"+probe['id']+"' AND subject='"+tag+"'")=='1',probe['id'])
    default=next(x for x in a.request('/sla-rules') if x['scope']=='DEFAULT' and x['active'])
    deadlines={p:{'responseHours':4,'resolutionHours':12} for p in ['LOW','MEDIUM','HIGH','URGENT']}
    rule=a.request('/sla-rules',{**default,'name':tag+' CT04 calendario','scope':'CLIENT_AND_DEMAND_TYPE','clientId':client_id,'demandTypeId':tp['id'],'timezone':'America/Sao_Paulo','businessDays':'["MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY"]','businessStart':'08:00:00','businessEnd':'18:00:00','holidays':'["2026-11-17","2026-11-23"]','pauseInValidation':True,'deadlines':json.dumps(deadlines)},'POST',201)
    for phase in ['TRIAGE','EXECUTION']:
        t=probe if phase=='TRIAGE' else create_ticket(tp)
        check('Ticket pertence ao schema '+phase,sql("SELECT COUNT(*) FROM tickets WHERE id='"+t['id']+"'")=='1')
        sql("UPDATE tickets SET created_at='2026-11-16 20:00:00' WHERE id='"+t['id']+"'")
        start(t)
        if phase=='EXECUTION':triage(t,tp)
        before=detail(t);action(staff if phase=='TRIAGE' else manager,t,'wait',{'reason':'Contexto CT04 sintético'});waiting=detail(t)
        sql("UPDATE tickets SET resolution_paused_at='2026-11-18 20:00:00' WHERE id='"+t['id']+"'")
        action(c,t,'comments',{'body':'Complemento sintético CT04','effectiveResponse':False},201);received=detail(t);action(staff,t,'resume',{'reason':'Conferido CT04'});resumed=detail(t)
        if phase=='TRIAGE':triage(t,tp)
        final=detail(t);data=row(t);interval=data['pause'][0];seconds=business_seconds(parse(interval['start']),parse(interval['end']));expected_response=add_work(parse(data['created']),4*3600);base_resolution=add_work(parse(data['created']),12*3600);expected_resolution=add_work(base_resolution,seconds)
        check('Pausa e prazos '+phase,waiting['waitOrigin']==phase and received['complementReceived'] and received['ticket']['status']=='WAITING_FOR_CLIENT' and resumed['ticket']['status']==phase and seconds>0 and data['snapshot']['id']==rule['id'] and parse(final['responseDueAt'])==expected_response and parse(final['resolutionDueAt'])==expected_resolution and before['responseCompletedAt']==resumed['responseCompletedAt'],dict(ticket=t['id'],before=before,waiting=waiting,received=received,resumed=resumed,final=final,db=data,businessSeconds=seconds,expectedResponse=iso(expected_response),expectedResolution=iso(expected_resolution)))
run_case('CT-04-pausa-origens','Média',run)
runtime=Path('target/qa-runtime.jar');report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
out=Path(args.output)
if out.exists():raise SystemExit('Evidência já existe')
out.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8');print(json.dumps(cases,ensure_ascii=False));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)

