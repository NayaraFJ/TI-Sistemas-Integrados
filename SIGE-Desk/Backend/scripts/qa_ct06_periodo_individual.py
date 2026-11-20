from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())
from collections import Counter
start=datetime.datetime.now(datetime.timezone.utc).isoformat()
rows=lambda: [json.loads(x) for x in sql("SELECT JSON_OBJECT('id',id,'createdAt',created_at) FROM tickets ORDER BY id").splitlines() if x]
before=rows(); counts=Counter(x['createdAt'][:10] for x in before)
day=next((day for day,n in sorted(counts.items()) if 0<n<len(before)),None)
if day is None: raise SystemExit('Sem datas distintas para inclusão/exclusão')
expected=sorted(sql("SELECT id FROM tickets WHERE created_at >= '"+day+" 00:00:00' AND created_at < DATE_ADD('"+day+"', INTERVAL 1 DAY) ORDER BY id").splitlines())
excluded=sorted(x['id'] for x in before if x['createdAt'][:10]!=day)
got=[]; totals=[]; page=0
while True:
 d=a.request('/tickets?'+urllib.parse.urlencode({'createdFrom':day,'createdTo':day,'size':100,'page':page})); got += [x['id'] for x in d['items']]; totals.append(d['total']); page+=1
 if len(got)>=d['total'] or not d['items']:break
after=rows(); checks={'positive':bool(expected),'excludedExists':bool(excluded),'idsMatch':sorted(got)==expected,'noDuplicates':len(got)==len(set(got)),'totalsMatch':all(t==len(expected) for t in totals),'stable':before==after,'excludesOutside':not bool(set(got)&set(excluded))}
report={'startedAt':start,'finishedAt':datetime.datetime.now(datetime.timezone.utc).isoformat(),'baseUrl':args.base_url,'schema':SCHEMA,'method':'HTTP admin e SQL direto; somente leitura de tickets','filter':{'createdFrom':day,'createdTo':day},'expectedIds':expected,'obtainedIds':sorted(got),'excludedIds':excluded,'totals':totals,'checks':checks}
Path(args.output).write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8');print('checks',checks,'incluídos',len(expected),'excluídos',len(excluded));raise SystemExit(not all(checks.values()))
