# Contrato OpenAPI

[openapi.yaml](openapi.yaml) é uma exportação completa do backend, serializada como JSON válido em YAML 1.2. Não é um resumo manual. O backend publica o contrato em `http://localhost:8080/v3/api-docs` e o Swagger em `http://localhost:8080/swagger-ui/index.html`.

A sessão usa `JSESSIONID`. Mutações, exceto login, exigem `X-XSRF-TOKEN`, obtido em `/api/v1/auth/csrf`. Comandos em `/tickets/{id}` exigem também `If-Match` com a versão lida em `TicketItem.version`; falta de versão retorna 428, e formulário antigo retorna 409 `VERSION_CONFLICT`. Upload é uma operação de acréscimo e não exige `If-Match`, mas altera a versão do ticket e participa da proteção transacional.

A criação aceita JSON ou multipart: parte `request` com JSON e Content-Type `application/json`, partes `files` para anexos e `field.<nome>` para campos FILE. O servidor valida campos, salva arquivos e cria o ticket atomicamente. Limites: 10 MB por arquivo, 11 MB por envio. Campos FILE não podem ser simulados informando apenas um nome no JSON.

Tickets aceitam filtro `overdue`. Cadastros usam `/{resource}/page`; notificações têm `page`, `size` e `unread`. Página começa em zero, tamanho máximo 100. Rotas antigas de cadastros permanecem para compatibilidade. Relatório e CSV incluem todos os resultados filtrados, sem restringir à página da interface.

Os erros de negócio dos controladores usam `{code,message,timestamp,fields}`: `VALIDATION_ERROR`, `BUSINESS_RULE`, `INVALID_TRANSITION`, `VERSION_CONFLICT`, `DATA_CONFLICT`, `UPLOAD_TOO_LARGE`, `FORBIDDEN` e `REQUEST_ERROR`. Respostas de filtros de segurança podem usar o corpo padrão do servidor.

## Sincronizar tipos

Em Frontend, com backend iniciado:

```powershell
pnpm api:sync http://localhost:8080/v3/api-docs
pnpm build
```

O script atualiza este contrato e `src/shared/api/generated.ts`. `types.ts` utiliza os schemas gerados; enumerações, campos e nulabilidade vêm do backend. Records de resposta serializam suas propriedades, incluindo null, e os aliases de resposta refletem essa presença. Para reproduzir os tipos sem servidor, use `pnpm api:sync` com o contrato versionado. Não edite o arquivo gerado manualmente.
