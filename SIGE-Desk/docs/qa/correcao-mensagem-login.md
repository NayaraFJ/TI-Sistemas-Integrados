# Correção da mensagem de falha no login

O relato em `relatorio-interface.md` identificou que uma falha de CORS era apresentada como problema de credenciais. A interface agora reserva essa orientação à resposta HTTP 401; falhas sem resposta recebida exibem mensagem de comunicação, enquanto outras respostas e erros inesperados recebem mensagem genérica. O alerta é limpo ao editar um dos campos e continua usando o componente acessível de alerta e traduções do projeto.

No servidor, o tratamento de login converte apenas credenciais rejeitadas e restrições de conta em 401. Exceções internas de autenticação deixam de ser apresentadas como credenciais inválidas. As mensagens ao usuário não incluem detalhes internos.

Verificações: `npm run build`, `node --test tests/loginError.test.ts` e o teste Maven `LoginFailureTest` passaram. O teste de classificação cobre 401, 403, 500, falha de rede e erro inesperado; o teste do servidor cobre credenciais rejeitadas e falha interna de autenticação. A correção ainda não foi validada em navegador com o ambiente de CORS relatado.

Reteste HTTP em 19/11/2026, na API de QA em `localhost:8081`, com conta sintética de administrador: senha inválida retornou **401**, mensagem genérica de credenciais inválidas e nenhum cookie; credencial válida retornou **200**, perfil `ADMIN` e cookie de sessão. Não foram registrados valores de senha ou cookie. A falha interna foi simulada apenas no teste unitário, que verifica a propagação de `AuthenticationServiceException`; não se induziu falha no ambiente em execução, portanto não há evidência HTTP para esse cenário. A resposta a CORS e a exibição visual permanecem sem reteste no navegador.
