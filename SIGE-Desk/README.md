# SIGE Desk

Aplicação web para gestão de demandas de tráfego pago. O frontend React consome a API Spring Boot; os dados fictícios de desenvolvimento são criados exclusivamente pelo backend no perfil `demo`.

## Pré-requisitos

- Java 21 ou superior
- Node.js 22 ou superior
- Docker Desktop, para o MySQL 8.4 local

## Execução local

As instruções específicas estão no [README do frontend](Frontend/README.md) e no [README do backend](Backend/README.md), incluindo instalação, conexão MySQL e primeiro acesso administrativo.

1. Suba o banco com `docker compose up -d mysql`.
2. Defina `SIGE_DEMO_PASSWORD` no terminal para escolher a senha das contas fictícias; na ausência da variável, o padrão local é `123`.
3. Inicie a API em `Backend` com `./mvnw spring-boot:run -Dspring-boot.run.profiles=demo`.
4. Em outro terminal, execute `pnpm install` na primeira vez e `pnpm dev` em `Frontend`.

O frontend usa proxy local para `/api`, portanto não precisa conhecer a porta da API. As migrations do Flyway são executadas ao iniciar a API e o Hibernate não altera o esquema.

### MySQL já instalado

Se for usar uma instância MySQL fora do Docker, informe a conexão antes de iniciar a API. O datasource usa `createDatabaseIfNotExist=true` para criar a base indicada quando ela não existir, antes de o Flyway executar as migrations. Isso também se aplica a uma URL definida em `SIGE_DB_URL`. A conta precisa ter permissão `CREATE` para essa base, além das permissões necessárias às migrations e à aplicação. Se ela não puder criar bancos, crie a base previamente com uma conta autorizada. Não use as credenciais de outro projeto na mesma instância.

```powershell
$env:SIGE_DB_URL = 'jdbc:mysql://localhost:3306/sige_desk?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
$env:SIGE_DB_USERNAME = 'sige'
$env:SIGE_DB_PASSWORD = 'sua-senha-local'
```

O Flyway executa as migrações de `Backend/src/main/resources/db/migration` nessa base, incluindo a estrutura inicial, o registro de conclusão dos tickets e o controle de carga demo. Se a conta padrão do SIGE Desk não existir na instância, use uma conta própria ou inicie o serviço do Docker.

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

### Administrador inicial sem perfil demo

Ao iniciar a API com a tabela `users` vazia, o backend cadastra automaticamente `admin@sige.demo`, com perfil Administrador e acesso ativo, mesmo sem o perfil `demo`. A senha é armazenada com BCrypt. A prioridade da configuração é `SIGE_ADMIN_PASSWORD`, depois `SIGE_DEMO_PASSWORD` e, na ausência das duas, `123` para desenvolvimento local. Para outros ambientes, configure uma senha própria em `SIGE_ADMIN_PASSWORD` antes da primeira inicialização.

Em `Backend`, execute `./mvnw spring-boot:run` (Windows: `.\mvnw.cmd spring-boot:run`). No ambiente local sem variáveis de senha configuradas, entre com **admin@sige.demo / 123**. Essa conta pode cadastrar os demais usuários pela tela de administração. As credenciais da aplicação são independentes do usuário e senha de conexão MySQL.

O cadastro inicial ocorre somente quando não há usuários: reiniciar a API ou alterar as variáveis não redefine senhas, não reativa contas e não sobrescreve cadastros. No perfil `demo`, a carga é executada antes do administrador de fallback e funciona também com usuários existentes. Contas novas usam `SIGE_DEMO_PASSWORD` (padrão local `123`); contas existentes mantêm a senha anterior. O registro da versão em `demo_seed_runs` impede repetir a carga.

### Contas fictícias

| Perfil | E-mail |
| --- | --- |
| Cliente | `cliente@aurora.demo` |
| Cliente Horizonte | `rafael@horizonte.demo` |
| Cliente Viva | `cliente@viva.demo` |
| Atendimento | `atendimento@sige.demo` |
| Atendimento adicional | `atendimento2@sige.demo` |
| Gestor de tráfego | `gestor@sige.demo` |
| Gestor de tráfego adicional | `gestor2@sige.demo` |
| Administrador | `admin@sige.demo` |

As contas novas usam a senha definida em `SIGE_DEMO_PASSWORD`, com padrão local `123`. As contas existentes mantêm suas senhas. O [roteiro de validação](Backend/VALIDACAO-DEMO.md) descreve os 24 tickets, evidências e a comparação com as telas do protótipo.
