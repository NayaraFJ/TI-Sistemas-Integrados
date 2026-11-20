"""CT-10: relógio de fixture no MySQL, API real e oráculo externo de calendário."""
from pathlib import Path
from zoneinfo import ZoneInfo
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())

def parse(s):
    # MySQL DATETIME(6) arredonda o instante em nanos ao microssegundo mais próximo.
    import re
    match=re.fullmatch(r'(.*?)(?:\.(\d+))?Z',s)
    if not match:return datetime.datetime.fromisoformat(s.replace('Z','+00:00'))
    digits=(match.group(2) or '').ljust(9,'0')
    nanos=int(digits[:9]); micros=(nanos+500)//1000
    return datetime.datetime.fromisoformat(match.group(1)+'+00:00')+datetime.timedelta(microseconds=micros)
def iso(d): return d.astimezone(datetime.timezone.utc).isoformat().replace('+00:00','Z')
def add_work(start, seconds):
    zone=ZoneInfo('America/Sao_Paulo'); p=start.astimezone(zone); left=datetime.timedelta(seconds=seconds)
    holidays={'2026-11-17','2026-11-20','2026-11-23'}
    while left>datetime.timedelta(0):
        day=p.date(); start_day=datetime.datetime.combine(day,datetime.time(8),zone); end_day=datetime.datetime.combine(day,datetime.time(18),zone)
        if day.weekday()>=5 or day.isoformat() in holidays or p>=end_day:
            p=datetime.datetime.combine(day+datetime.timedelta(days=1),datetime.time(0),zone);continue
        p=max(p,start_day); used=min(left,end_day-p);p+=used;left-=used
    return p.astimezone(datetime.timezone.utc)
def business_seconds(start,end):
    zone=ZoneInfo('America/Sao_Paulo'); day=start.astimezone(zone).date(); stop=end.astimezone(zone).date(); total=0
    holidays={'2026-11-17','2026-11-20','2026-11-23'}
    while day<=stop:
        if day.weekday()<5 and day.isoformat() not in holidays:
            a=datetime.datetime.combine(day,datetime.time(8),zone).astimezone(datetime.timezone.utc);b=datetime.datetime.combine(day,datetime.time(18),zone).astimezone(datetime.timezone.utc)
            total+=max(0,(min(end,b)-max(start,a)).total_seconds())
        day+=datetime.timedelta(days=1)
    return total
def row(t):
    return json.loads(sql("SELECT JSON_OBJECT('created', DATE_FORMAT(created_at,'%Y-%m-%dT%H:%i:%s.%fZ'),'cycleStart',DATE_FORMAT(resolution_cycle_started_at,'%Y-%m-%dT%H:%i:%s.%fZ'),'pause',resolution_pause_intervals,'snapshot',sla_rule_snapshot) FROM tickets WHERE id='"+t['id']+"'"))

def correlation():
    tp=new_type(True,False);default=next(x for x in a.request('/sla-rules') if x['scope']=='DEFAULT' and x['active'])
    deadlines={p:{'responseHours':4,'resolutionHours':12} for p in ['LOW','MEDIUM','HIGH','URGENT']}
    rule=a.request('/sla-rules',{**default,'name':tag+' calendario','scope':'CLIENT_AND_DEMAND_TYPE','clientId':client_id,'demandTypeId':tp['id'],'timezone':'America/Sao_Paulo','businessDays':'["MONDAY","TUESDAY","WEDNESDAY","THURSDAY","FRIDAY"]','businessStart':'08:00:00','businessEnd':'18:00:00','holidays':'["2026-11-17","2026-11-23"]','pauseInValidation':True,'deadlines':json.dumps(deadlines)},'POST',201)
    t=create_ticket(tp);sql("UPDATE tickets SET created_at='2026-11-16 20:00:00' WHERE id='"+t['id']+"'");start(t);triage(t,tp);initial=detail(t);r1=row(t)
    response=add_work(parse(r1['created']),4*3600);resolution=add_work(parse(r1['created']),12*3600)
    check('Ciclo 1 calcula a partir da abertura no calendário do snapshot',parse(initial['responseDueAt'])==response and parse(initial['resolutionDueAt'])==resolution and r1['snapshot']['id']==rule['id'],dict(ticket=t['id'],fixture=r1,responseExpected=iso(response),resolutionExpected=iso(resolution),responseObserved=initial['responseDueAt'],resolutionObserved=initial['resolutionDueAt']))
    action(manager,t,'execution',{'actionDescription':'Entrega sintética CT10'});validation=detail(t)
    check('Snapshot habilita pausa em validação',validation['slaState']=='PAUSED' and r1['snapshot']['pauseInValidation'],dict(ticket=t['id'],status=validation['ticket']['status'],slaState=validation['slaState']))
    sql("UPDATE tickets SET resolution_paused_at='2026-11-18 20:00:00' WHERE id='"+t['id']+"'")
    action(c,t,'correction',{'reason':'Correção sintética CT10'});corrected=detail(t);rc=row(t);interval=rc['pause'][0];start_pause=parse(interval['start']);end_pause=parse(interval['end']);pause_seconds=business_seconds(start_pause,end_pause);expected=add_work(resolution,pause_seconds)
    check('Pausa de validação usa somente tempo útil e preserva ciclo',parse(corrected['resolutionDueAt'])==expected and corrected['resolutionCycle']==1 and corrected['responseDueAt']==initial['responseDueAt'],dict(interval=interval,businessSeconds=pause_seconds,expected=iso(expected),observed=corrected['resolutionDueAt'],snapshot=rc['snapshot']))
    triage(t,tp);action(manager,t,'execution',{'actionDescription':'Entrega corrigida CT10'});action(c,t,'approve',{'reason':'Aprovação sintética CT10'});done=detail(t)
    edited=a.request('/sla-rules/'+rule['id'],{**rule,'pauseInValidation':False},'PUT');action(c,t,'reopen',{'reason':'Ciclo novo CT10'})
    sql("UPDATE tickets SET resolution_cycle_started_at='2026-11-19 20:00:00' WHERE id='"+t['id']+"'")
    triage(t,tp);cycle2=detail(t);r2=row(t);expected2=add_work(parse(r2['cycleStart']),12*3600)
    check('Novo ciclo usa snapshot atualizado e preserva primeira resposta',cycle2['resolutionCycle']==2 and r2['snapshot']['version']>r1['snapshot']['version'] and not r2['snapshot']['pauseInValidation'] and parse(cycle2['resolutionDueAt'])==expected2 and cycle2['responseDueAt']==done['responseDueAt'] and cycle2['responseCompletedAt']==done['responseCompletedAt'],dict(ticket=t['id'],cycle1=r1,cycle2=r2,expected=iso(expected2),observed=cycle2['resolutionDueAt']))
    action(manager,t,'execution',{'actionDescription':'Nova entrega CT10'});validation2=detail(t)
    check('Novo snapshot desativa pausa em validação',validation2['ticket']['status']=='VALIDATION' and validation2['slaState']!='PAUSED',dict(status=validation2['ticket']['status'],slaState=validation2['slaState']))

run_case('CT-10-calendario-pausa-ciclos','Alta',correlation)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output)
if output.exists():raise SystemExit('Evidência já existe.')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
