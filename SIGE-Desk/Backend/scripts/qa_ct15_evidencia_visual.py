"""Referência API/MySQL para confronto das capturas reais do navegador em QA local."""
from pathlib import Path
source=Path(__file__).with_name('qa_ct15_painel_pausado.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('accounts=[a,staff')],__file__,'exec'),globals())

email='ct15.vazio.20261120@sige.demo'
fixture_password='QaCt15!20261120'
if not any(u['email']==email for u in users):
    a.request('/users',dict(name='QA CT15 painel vazio',email=email,password=fixture_password,role='TRAFFIC_MANAGER',clientId=None),'POST',201)
empty=Account(email,fixture_password)
data=rows()
snapshots={}
for label,ac in [('admin',a),('atendimento',staff),('cliente',c),('cliente-externo',other),('gestor',manager),('gestor-nao-atribuido',other_manager),('vazio',empty)]:
    scoped_rows=scoped(data,ac)
    dashboard=ac.request('/dashboard')
    assert sum(i['count'] for i in dashboard['statusDistribution'])==len(scoped_rows)
    for key,outkey,inkey in [('statusDistribution','status','status'),('priorityDistribution','priority','priority'),('assigneeDistribution','id','assigneeId')]:
        expected=Counter(r[inkey] if r[inkey] is not None else ('UNCLASSIFIED' if inkey=='priority' else None) for r in scoped_rows)
        got={i[outkey]:i['count'] for i in dashboard[key] if i['count']}
        assert got==dict(expected),(label,key)
    active=[r for r in scoped_rows if r['status'] not in ('DONE','CANCELLED')]
    assert dashboard['activeCount']==len(active)
    assert sum(i['count'] for i in dashboard['deadlineDistribution'])==len(active)
    if label=='vazio':assert len(scoped_rows)==0 and dashboard['activeCount']==0
    snapshots[label]=dict(email=ac.email,actor=ac.me,datasetIds=[r['id'] for r in scoped_rows],dashboard=dashboard)
assert rows()==data,'Dados alterados durante a referência'
report=dict(createdAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads'),dataset=data,snapshots=snapshots,stable=True,fixture=dict(email=email,role='TRAFFIC_MANAGER',tickets=0,retention='Usuário sintético preservado em QA; nenhum ticket criado ou alterado.'))
Path(args.output).write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print('Referência preservada: 7 sessões,',len(data),'tickets; sem divergências nas distribuições; fixture vazia confirmada.')
