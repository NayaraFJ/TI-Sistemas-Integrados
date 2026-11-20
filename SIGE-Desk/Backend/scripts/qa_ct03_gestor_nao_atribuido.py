"""CT-03: execução por Gestor não atribuído, somente API e MySQL locais de QA."""
from pathlib import Path
source = Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')], str(Path(__file__).with_name('qa_functional.py')), 'exec'), globals())

output = Path(args.output)
if output.exists():
    raise SystemExit('Evidência já existe; escolha outro nome.')

def scenario():
    tp = new_type(False, False)
    t = create_ticket(tp)
    start(t)
    triage(t, tp)
    before = detail(t)
    check('Fixture em execução atribuída ao Gestor', before['ticket']['status'] == 'EXECUTION' and before['ticket']['assigneeName'] == manager.me['name'], dict(ticket=t['id'], number=t['number'], assignee=before['ticket']['assigneeName']))
    version = before['ticket']['version']
    query = "SELECT JSON_OBJECT('status',status,'version',version_number,'assignee',assignee_id) FROM tickets WHERE id='"+t['id']+"'"
    history_query = "SELECT JSON_OBJECT('id',id,'actor',actor_id,'action',action,'field',field_name,'old',old_value,'new',new_value,'reason',reason,'created',DATE_FORMAT(created_at,'%Y-%m-%dT%H:%i:%s.%f')) FROM ticket_history WHERE ticket_id='"+t['id']+"' ORDER BY created_at,id"
    db_before = sql(query)
    history_before = sql(history_query)
    check('Fixture confirmada no MySQL', bool(db_before) and json.loads(db_before)['assignee'] == manager.me['id'] and json.loads(db_before)['version'] == version, db_before)
    # Versão fornecida explicitamente: o Gestor não atribuído não pode consultar o ticket.
    other_manager.request('/tickets/'+t['id']+'/execution', {'actionDescription':'Tentativa sintética não autorizada'}, 'POST', 404, version=version)
    after = detail(t)
    check('Negativa preserva estado, versão e histórico', before['ticket']['status'] == after['ticket']['status'] and version == after['ticket']['version'] and before['history'] == after['history'], dict(ticket=t['id'], beforeVersion=version, afterVersion=after['ticket']['version'], beforeStatus=before['ticket']['status'], afterStatus=after['ticket']['status'], beforeHistory=before['history'], afterHistory=after['history']))
    db_after = sql(query)
    history_after = sql(history_query)
    check('MySQL preserva estado, versão, responsável e histórico', db_before == db_after and history_before == history_after, dict(before=db_before, after=db_after, historyBefore=history_before, historyAfter=history_after))
    assigned = manager.request('/tickets/'+t['id'])
    check('Gestor atribuído acessa a fixture', assigned['ticket']['version'] == version and assigned['ticket']['status'] == 'EXECUTION')
    action(manager, t, 'execution', {'actionDescription':'Execução sintética autorizada'})
    final = detail(t)
    check('Gestor atribuído executa após negativa', final['ticket']['status'] == 'DONE' and final['ticket']['version'] > version and any(h['action'] == 'EXECUTION_RECORDED' for h in final['history']), dict(status=final['ticket']['status'], version=final['ticket']['version']))

run_case('CT-03-gestor-nao-atribuido', 'Alta', scenario)
runtime = Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report = dict(startedAt=started, finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(), environment=dict(baseUrl=args.base_url, schema=SCHEMA, storage='target/qa-uploads', mysql=sql('SELECT VERSION()'), runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(), runTag=tag), scope='Uma fixture sintética; resposta HTTP e comparação de estado, versão e histórico por API e MySQL. Hash identifica o arquivo, não necessariamente o processo. Não demonstra interface ou outras operações.', cases=cases, checks=results)
output.parent.mkdir(parents=True, exist_ok=True)
output.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding='utf-8')
print(json.dumps(cases, ensure_ascii=False, indent=2))
raise SystemExit(0 if all(x['status'] == 'APROVADO' for x in cases) else 1)
