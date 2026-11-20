"""Verifica rejeição de abertura sem campanha nem identificação pendente."""
from pathlib import Path
source = Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')], str(Path(__file__).with_name('qa_functional.py')), 'exec'), globals())

def missing_campaign():
    tp = new_type(False, False)
    subject = tag + ' campanha sem identificacao'
    body = dict(clientId=client_id, campaignId=None, pendingCampaign='', requesterId=c.me['id'], demandTypeId=tp['id'], channel='Google Ads', subject=subject, description='Demanda sintetica de QA', urgency='HIGH', desiredDate='2027-01-15', metrics={'fields': {}})
    query = "SELECT COUNT(*) FROM tickets WHERE subject='" + subject + "'"
    before = int(sql(query))
    try:
        c.request('/tickets', body, 'POST', 400)
    finally:
        after = int(sql(query))
        check('Sem ticket parcial', before == after, dict(subject=subject, before=before, after=after))

run_case('CT-01-campanha-sem-identificacao', 'Alta', missing_campaign)
runtime = Path(__file__).resolve().parents[1] / 'target/qa-runtime.jar'
report = dict(startedAt=started, finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(), environment=dict(baseUrl=args.base_url, schema=SCHEMA, storage='target/qa-uploads', mysql=sql('SELECT VERSION()'), runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(), runTag=tag), cases=cases, checks=results)
output = Path(args.output)
if output.exists(): raise SystemExit('Evidência já existe; escolha outro nome.')
output.write_text(json.dumps(report, ensure_ascii=False, indent=2), encoding='utf-8')
print(json.dumps(cases, ensure_ascii=False, indent=2))
raise SystemExit(0 if all(x['status'] == 'APROVADO' for x in cases) else 1)
