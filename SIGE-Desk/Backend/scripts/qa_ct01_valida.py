"""Evidência CT-01: abertura válida e consulta em base isolada de QA."""
from pathlib import Path
source = Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')], str(Path(__file__).with_name('qa_functional.py')), 'exec'), globals())

def valid_opening():
    tp = new_type(False, False)
    subject = tag + ' abertura valida'
    body = dict(clientId=client_id, campaignId=campaign['id'], requesterId=c.me['id'], demandTypeId=tp['id'], channel='Google Ads', subject=subject, description='Demanda sintetica de QA', urgency='HIGH', desiredDate='2027-01-15', metrics={'fields': {}})
    query = "SELECT COUNT(*) FROM tickets WHERE subject='" + subject + "'"
    before = int(sql(query))
    created = c.request('/tickets', body, 'POST', 201)
    after = int(sql(query))
    client_view = c.request('/tickets/' + created['id'])
    staff_view = staff.request('/tickets/' + created['id'])
    check('Um ticket persistido', after == before + 1, dict(subject=subject, before=before, after=after, id=created['id'], number=created['number']))
    check('Consulta por Cliente e Atendimento', client_view['ticket']['id'] == staff_view['ticket']['id'] == created['id'], dict(client=client_view, staff=staff_view))
    check('Dados e estado preservados', client_view['desiredDate'] == body['desiredDate'] and client_view['ticket']['status'] == 'OPEN' and client_view['ticket']['urgency'] == 'HIGH' and client_view['ticket']['priority'] is None and client_view['slaState'] == 'PENDING_CLASSIFICATION', dict(created=created, desiredDate=client_view['desiredDate'], slaState=client_view['slaState']))

run_case('CT-01-abertura-valida', 'Alta', valid_opening)
runtime = Path(__file__).resolve().parents[1] / 'target/qa-runtime.jar'
report = dict(startedAt=started, finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(), environment=dict(baseUrl=args.base_url, schema=SCHEMA, storage='target/qa-uploads', mysql=sql('SELECT VERSION()'), runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(), runTag=tag), cases=cases, checks=results)
output = Path(args.output)
if output.exists(): raise SystemExit('Evidência já existe; escolha outro nome.')
output.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding='utf-8')
print(json.dumps(cases, ensure_ascii=False, indent=2))
raise SystemExit(0 if all(x['status'] == 'APROVADO' for x in cases) else 1)
