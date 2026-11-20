from pathlib import Path
source = Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')], str(Path(__file__).with_name('qa_functional.py')), 'exec'), globals())

def variant():
    tp = new_type(False, False)
    ticket = create_ticket(tp, subject=tag + ' mutação direta CT08')
    start(ticket)
    triage(ticket, tp)
    before = detail(ticket)
    db_before = sql("SELECT * FROM tickets WHERE id='" + ticket['id'] + "'")
    for actor, role, endpoint, body in [
        (other, 'CLIENT', 'comments', {'body': tag + ' comentário negado', 'effectiveResponse': False}),
        (other_manager, 'TRAFFIC_MANAGER', 'execution', {'actionDescription': tag + ' execução negada'}),
    ]:
        def events():
            query = "SELECT JSON_OBJECT('actor',actor_id,'role',actor_role,'code',event_code,'type',resource_type,'resource',resource_id,'at',DATE_FORMAT(occurred_at,'%Y-%m-%dT%H:%i:%s.%f')) FROM security_access_events WHERE actor_id='" + actor.me['id'] + "' AND resource_id='" + ticket['id'] + "' ORDER BY occurred_at,id"
            return [json.loads(row) for row in sql(query).splitlines() if row]
        old = events()
        actor.request('/tickets/' + ticket['id'] + '/' + endpoint, body, 'POST', 404, version=before['ticket']['version'])
        new = events()[len(old):]
        check(role + ' evento de negação correlacionado', len(new) == 1 and new[0]['actor'] == actor.me['id'] and new[0]['role'] == role and new[0]['code'] == 'SECURITY_TICKET_ACCESS_DENIED' and new[0]['type'] == 'TICKET' and new[0]['resource'] == ticket['id'] and bool(new[0]['at']), dict(ticketId=ticket['id'], events=new))
        check(role + ' detalhe e histórico inalterados', detail(ticket) == before)
        check(role + ' linha do ticket no MySQL inalterada', sql("SELECT * FROM tickets WHERE id='" + ticket['id'] + "'") == db_before)

run_case('CT-08-mutacao-direta', 'Alta', variant)
runtime = Path(__file__).resolve().parents[1] / 'target/qa-runtime.jar'
report = dict(startedAt=started, finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(), environment=dict(baseUrl=args.base_url, schema=SCHEMA, mysql=sql('SELECT VERSION()'), runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(), runTag=tag), cases=cases, checks=results)
output = Path(args.output)
if output.exists(): raise SystemExit('evidência existente')
output.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding='utf-8')
print(json.dumps(cases, ensure_ascii=False, indent=2))
raise SystemExit(0 if all(x['status'] == 'APROVADO' for x in cases) else 1)
