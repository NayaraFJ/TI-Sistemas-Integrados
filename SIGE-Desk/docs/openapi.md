# Contrato OpenAPI

O arquivo [openapi.yaml](openapi.yaml) registra os recursos e convenções públicas da versão `1.0.0`. O contrato detalhado é gerado pela própria aplicação para acompanhar os DTOs e controladores Spring.

Com a API iniciada, consulte:

- JSON OpenAPI: `http://localhost:8080/v3/api-docs`
- Swagger UI: `http://localhost:8080/swagger-ui/index.html`

O contrato descreve a versão `/api/v1`. A sessão é identificada pelo cookie `JSESSIONID`; as mutações, exceto o login, exigem o token CSRF obtido em `GET /api/v1/auth/csrf` e enviado no cabeçalho `X-XSRF-TOKEN`.

Os erros seguem `{ code, message, timestamp, fields }`. O frontend mantém seus rótulos e mensagens de interface no catálogo i18n e pode usar o `code` para apresentar uma mensagem contextual. Os códigos utilizados atualmente são `VALIDATION_ERROR`, `BUSINESS_RULE`, `INVALID_TRANSITION`, `VERSION_CONFLICT`, `FORBIDDEN` e `REQUEST_ERROR`.
