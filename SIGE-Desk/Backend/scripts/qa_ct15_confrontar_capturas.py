"""Confronta texto acessível capturado do navegador com referência API/MySQL preservada."""
import datetime,hashlib,json,re
from pathlib import Path
base=Path(__file__).resolve().parents[2]/'docs/qa'
reference=json.loads((base/'ct15-referencia-visual-20261120.json').read_text(encoding='utf-8'))
folder=base/'capturas-ct15-20261120'
checks=[];files=[]
def check(profile,label,expected,obtained):
    checks.append(dict(profile=profile,label=label,expected=expected,obtained=obtained,passed=obtained==expected))
status=dict(OPEN='Aberta',TRIAGE='Em triagem',EXECUTION='Em execução',WAITING_FOR_CLIENT='Aguardando cliente',VALIDATION='Em validação',DONE='Concluída',REOPENED='Reaberta',CANCELLED='Cancelada')
priority=dict(URGENT='Urgente',HIGH='Alta',MEDIUM='Média',LOW='Baixa',UNCLASSIFIED='Sem classificação')
deadline=dict(OVERDUE='Vencido',PAUSED='Pausado',PENDING_CLASSIFICATION='Pendente de classificação',ON_TIME='No prazo')
for profile,item in reference['snapshots'].items():
    ax=(folder/(profile+'-ax.txt')).read_text(encoding='utf-8'); d=item['dashboard']
    def value(label):
        found=re.search(re.escape(label)+r' (\d+)\b',ax)
        return int(found[1]) if found else None
    for key,label in [('activeCount','TICKETS ATIVOS'),('highPriorityCount','ALTA PRIORIDADE'),('validationCount','EM VALIDAÇÃO'),('waitingCount','AGUARDANDO CLIENTE')]:check(profile,label,d[key],value(label))
    for key,mapping,attr in [('statusDistribution',status,'status'),('priorityDistribution',priority,'priority'),('deadlineDistribution',deadline,'state')]:
        for row in d[key]:
            # O gráfico de status omite categorias de contagem zero; as outras tabelas as exibem.
            if key=='statusDistribution' and row['count']==0:continue
            check(profile,key+':'+row[attr],row['count'],value(mapping[row[attr]]))
    for row in d['assigneeDistribution']:check(profile,'assignee:'+str(row['id']),row['count'],value(row['name']))
    match=re.search(r'(\d+) ticket\(s\) classificados',ax)
    check(profile,'classifiedCount',d['classifiedCount'],int(match[1]) if match else None)
    total=len(item['datasetIds'])
    if total:
        match=re.search(r'\b(\d+) tickets\b',ax)
        check(profile,'total visível no gráfico',total,int(match[1]) if match else None)
    else:check(profile,'estado vazio',True,ax.count('Nenhum ticket foi encontrado.')>=2)
    for ext in ['.jpg','-ax.txt']:
        file=folder/(profile+ext);files.append(dict(path=file.relative_to(base).as_posix(),bytes=file.stat().st_size,sha256=hashlib.sha256(file.read_bytes()).hexdigest()))
report=dict(createdAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),checks=checks,captures=files,passed=all(c['passed'] for c in checks),scope='Texto da interface real versus API previamente confrontada com dataset MySQL; inspeção das imagens complementa o confronto numérico.')
(base/'ct15-confronto-capturas-20261120.json').write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(len(checks),'confrontos; falhas:',sum(not c['passed'] for c in checks))
for c in checks:
    if not c['passed']:print(c)
raise SystemExit(not report['passed'])
