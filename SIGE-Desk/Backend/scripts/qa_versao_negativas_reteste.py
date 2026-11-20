"""Reteste independente da versão em comandos recusados, API local de QA."""
from pathlib import Path
source=Path(__file__).with_name('qa_functional.py').read_text(encoding='utf-8')
exec(compile(source[:source.index('suite=[')],str(Path(__file__).with_name('qa_functional.py')),'exec'),globals())

def versions():
    tp=new_type(False,False); t=create_ticket(tp); start(t); triage(t,tp)
    for actor in [c,staff,a]:
        before=detail(t)
        action(actor,t,'execution',{'actionDescription':'Ação recusada'},403)
        after=detail(t)
        check('Negativa preserva versão e estado '+actor.me['role'],before['ticket']['version']==after['ticket']['version'] and before['ticket']['status']==after['ticket']['status'],dict(before=before['ticket']['version'],after=after['ticket']['version']))
    before=detail(t); version=before['ticket']['version']
    manager.request('/tickets/'+t['id']+'/execution',{'actionDescription':'Versão ausente'},'POST',428,version=None)
    check('Versão ausente preserva ticket',detail(t)['ticket']['version']==version)
    manager.request('/tickets/'+t['id']+'/execution',{'actionDescription':'Versão obsoleta'},'POST',409,version=version-1)
    check('Versão obsoleta preserva ticket',detail(t)['ticket']['version']==version)
    action(manager,t,'execution',{'actionDescription':'Atualização permitida'})
    after=detail(t);check('Comando autorizado atualiza versão e estado',after['ticket']['version']>version and after['ticket']['status']=='DONE',dict(before=version,after=after['ticket']['version']))
    action(manager,t,'execution',{'actionDescription':'Estado inválido'},409)
    check('Estado inválido preserva versão',detail(t)['ticket']['version']==after['ticket']['version'])
    fresh=create_ticket(tp);v=detail(fresh)['ticket']['version']
    action(c,fresh,'cancel',{'reason':'Sem permissão'},403)
    check('Cancelamento sem permissão preserva versão',detail(fresh)['ticket']['version']==v)
    action(staff,fresh,'cancel',{'reason':''},400)
    check('Validação inválida preserva versão',detail(fresh)['ticket']['version']==v)

run_case('CT-03-versao-reteste','Alta',versions)
runtime=Path(__file__).resolve().parents[1]/'target/qa-runtime.jar'
report=dict(startedAt=started,finishedAt=datetime.datetime.now(datetime.timezone.utc).isoformat(),environment=dict(baseUrl=args.base_url,schema=SCHEMA,storage='target/qa-uploads',mysql=sql('SELECT VERSION()'),runtimeSha256=hashlib.sha256(runtime.read_bytes()).hexdigest(),runTag=tag),cases=cases,checks=results)
output=Path(args.output);output.parent.mkdir(parents=True,exist_ok=True)
if output.exists():raise SystemExit('Evidência já existe; escolha outro nome.')
output.write_text(json.dumps(report,ensure_ascii=False,indent=2),encoding='utf-8')
print(json.dumps(cases,ensure_ascii=False,indent=2));raise SystemExit(0 if all(x['status']=='APROVADO' for x in cases) else 1)
