"""Variantes negativas CT-02 na API e schema locais de QA."""
from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())

created=[]
def run():
    type_id=sql("SELECT id FROM demand_types WHERE active=1 AND JSON_LENGTH(field_definitions)=0 LIMIT 1")
    tp=next(x for x in refs['demandTypes'] if x['id']==type_id)
    ticket=create_ticket(tp);created.append(ticket['id']);start(ticket)
    payload=dict(priority='HIGH',assigneeId=manager.me['id'],campaignId=campaign['id'],demandTypeId=tp['id'],effectiveResponse='Retorno QA CT02',sendToExecution=True)
    foreign=next(x for x in refs['campaigns'] if x['clientId']!=client_id)
    before=detail(ticket)
    action(staff,ticket,'triage',{**payload,'campaignId':foreign['id']},400)
    after=detail(ticket)
    check('Campanha de outro cliente rejeitada sem alteração',before==after,dict(ticket=ticket['id'],foreignCampaign=foreign['id'],before=before,after=after))
    triage(ticket,tp)
    before=detail(ticket)
    action(manager,ticket,'triage',payload,403)
    after=detail(ticket)
    check('Gestor atribuído não classifica nem altera o ticket',before==after,dict(ticket=ticket['id'],before=before,after=after))
    check('Persistência confirma estado e vínculos',sql("SELECT CONCAT(status,'|',priority,'|',assignee_id,'|',campaign_id) FROM tickets WHERE id='"+ticket['id']+"'")==f"EXECUTION|HIGH|{manager.me['id']}|{campaign['id']}")

try:
    run_case('CT-02-variantes','Alta',run)
finally:
    for ticket_id in created:
        for table in ['ticket_deadline_notices','notifications','ticket_history','ticket_approvals','ticket_attachments','ticket_comments']:
            sql("DELETE FROM "+table+" WHERE ticket_id='"+ticket_id+"'")
        sql("DELETE FROM tickets WHERE id='"+ticket_id+"'")
    cleanup={ticket_id:sql("SELECT COUNT(*) FROM tickets WHERE id='"+ticket_id+"'") for ticket_id in created}
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256((Path(__file__).resolve().parents[1]/'target/qa-runtime.jar').read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results,cleanup=cleanup)
output=Path(args.output)
if output.exists():raise SystemExit('Evidência já existe; escolha outro nome.')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2))
raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) and all(x=='0' for x in cleanup.values()) else 1)
