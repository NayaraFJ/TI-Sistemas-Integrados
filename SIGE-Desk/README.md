# SIGE Desk

Aplicação web para gestão de demandas de tráfego pago. O frontend React consome a API Spring Boot; os dados fictícios de desenvolvimento são criados exclusivamente pelo backend no perfil `demo`.

## Pré-requisitos

- Java 21 ou superior
- Node.js 22 ou superior
- Docker Desktop, para o MySQL 8.4 local

## Execução local

1. Suba o banco com `docker compose up -d mysql`.
2. Defina `SIGE_DEMO_PASSWORD` no terminal. Este valor protege as contas fictícias e não é salvo no repositório.
3. Inicie a API em `Backend` com `./mvnw spring-boot:run -Dspring-boot.run.profiles=demo`.
4. Em outro terminal, execute `npm install` e `npm run dev` em `Frontend`.

O frontend usa proxy local para `/api`, portanto não precisa conhecer a porta da API. As migrations do Flyway são executadas ao iniciar a API e o Hibernate não altera o esquema.

### MySQL já instalado

Se for usar uma instância MySQL fora do Docker, crie uma base vazia e uma conta com permissão nela, depois informe a conexão antes de iniciar a API. Não use as credenciais de outro projeto na mesma instância.

```powershell
$env:SIGE_DB_URL = 'jdbc:mysql://localhost:3306/sige_desk?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
$env:SIGE_DB_USERNAME = 'sige'
$env:SIGE_DB_PASSWORD = 'sua-senha-local'
```

O Flyway executa `V1__initial_schema.sql` e `V2__add_ticket_completion_timestamp.sql` nessa base. Se a conta padrão do SIGE Desk não existir na instância, use uma conta própria ou inicie o serviço do Docker.

### Validação de migration MySQL

O teste `MySqlMigrationIntegrationTest` é ativado somente quando estas variáveis apontam para uma base MySQL **vazia e descartável**:

```powershell
$env:SIGE_TEST_DB_URL = 'jdbc:mysql://localhost:3306/sige_desk_test?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
$env:SIGE_TEST_DB_USERNAME = 'sige'
$env:SIGE_TEST_DB_PASSWORD = 'sua-senha-local'
```

Execute `./mvnw test` em `Backend`. O teste aplica as migrations e verifica as tabelas essenciais; ele não limpa nem recria a base indicada.

## Contrato da API

Com a API em execução, o contrato OpenAPI vivo está em `http://localhost:8080/v3/api-docs` e a interface Swagger em `http://localhost:8080/swagger-ui/index.html`. Ambos refletem os endpoints da versão `/api/v1`.

## Contas do perfil demo

| Perfil | E-mail |
| --- | --- |
| Cliente | `cliente@aurora.demo` |
| Atendimento | `atendimento@sige.demo` |
| Gestor de tráfego | `gestor@sige.demo` |
| Administrador | `admin@sige.demo` |

Todas usam a senha definida em `SIGE_DEMO_PASSWORD`.
