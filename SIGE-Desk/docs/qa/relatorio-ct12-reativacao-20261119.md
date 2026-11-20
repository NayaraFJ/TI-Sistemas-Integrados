# CT-12 — complemento de reativação e permissões

Em 19/11/2026, foram executadas 26 verificações HTTP e MySQL, todas aprovadas, em `http://localhost:8081/api/v1`, schema `sige_desk_qa_20261118`, com organização e campanha sintéticas exclusivas. A confirmação do vínculo da API com esse schema e armazenamento `target/qa-uploads` foi fornecida pelo responsável pela inicialização; o schema foi confirmado no MySQL antes da mutação. A evidência detalhada e os horários UTC estão em [ct12-reativacao-20261119.json](ct12-reativacao-20261119.json); roteiro: `Backend/scripts/qa_ct12_reativacao.py`.

Admin criou organização e campanha. Cliente e Gestor receberam 403 nas tentativas de criar campanha e inativar organização. Para cada cadastro, a inativação retirou o item das referências e a reativação o devolveu; MySQL confirmou ambos ativos. Os registros exclusivos foram removidos ao final e a organização teve contagem remanescente zero. Não houve defeito observado neste recorte.

Os bloqueios de último Admin/Cliente e responsável, substituição, reatribuição e consulta de ticket anterior já constam em [variantes-adicionais-reteste.json](variantes-adicionais-reteste.json); a reativação acima não os repete. O bloqueio anterior de último Admin alterou seu papel e recebeu 409, sem demonstrar isoladamente uma tentativa de inativação. O resultado não comprova os caminhos visuais nem mensagens na interface. Executor: qa_cadastro_ct12; revisor pendente.

## Inativação do último Admin em unidade

O teste `Backend/src/test/java/br/pucminas/sige/users/api/LastAdminDeactivationTest.java` exercitou `UserController.update` diretamente com repositórios e usuário atual simulados e objetos sintéticos em memória. Sem outro Admin ativo, a tentativa de inativação lançou `IllegalStateException` com a mensagem de proteção e manteve ativo o usuário; com outro Admin ativo, a inativação ocorreu e o substituto permaneceu ativo. Resultado: **2 testes, 0 falhas, 0 erros, 0 ignorados**, em [ct12-ultimo-admin-unidade-20261119.txt](ct12-ultimo-admin-unidade-20261119.txt). Nenhum dado da API ou banco compartilhados foi alterado por esses testes.

O comando Maven offline utilizou `-Dtest=LastAdminDeactivationTest` e o repositório local `C:\Users\nayar\.m2\repository`. A primeira tentativa falhou na compilação ao acessar dependência local; a segunda passou. A camada de unidade não comprova o código HTTP nem a apresentação da mensagem. A resposta HTTP 409 para perda de papel do último Admin continua documentada na rodada anterior.

## Inativação do último Admin por HTTP

Em 19/11/2026, uma instância demo adicional foi iniciada em `localhost:8082`, com schema novo `sige_desk_qa_admin_20261119` e armazenamento `target/qa-admin-uploads`, separados da instância 8081. A [evidência HTTP/MySQL](ct12-ultimo-admin-http-20261119.json) registra 11 verificações aprovadas: havia exatamente um Admin ativo; o PUT para inativá-lo retornou **409**, código `INVALID_TRANSITION` e mensagem de proteção; o MySQL manteve `1:ADMIN`, a sessão continuou funcional e permaneceu exatamente um Admin ativo. O roteiro é `Backend/scripts/qa_ct12_ultimo_admin_http.py`. O servidor adicional foi encerrado após a rodada; schema e arquivos sintéticos foram preservados para auditoria. A interface segue fora deste recorte.
