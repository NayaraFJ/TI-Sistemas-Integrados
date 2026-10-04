# SIGE Desk — Backend

API Spring Boot do SIGE Desk, com MySQL, migrações Flyway e autenticação por sessão.

## Pré-requisitos

- Java 21 ou superior, disponível no terminal (`java -version`).
- Uma instância MySQL em execução e acessível; o projeto disponibiliza MySQL 8.4 em Docker como alternativa.
- Acesso à internet na primeira execução para baixar as dependências do Maven. O Maven Wrapper está incluído no projeto.

## Configuração do banco

A conexão é definida em [src/main/resources/application.yml](src/main/resources/application.yml), no bloco `spring.datasource`.

| Configuração | Onde configurar | Padrão local atual |
| --- | --- | --- |
| Servidor e porta MySQL | `spring.datasource.url` ou variável `SIGE_DB_URL` | `localhost:3306` |
| Nome do banco | Após a porta, na URL: `/sige_desk` | `sige_desk` |
| Usuário do banco | `spring.datasource.username` ou variável `SIGE_DB_USERNAME` | `root` |
| Senha do banco | `spring.datasource.password` ou variável `SIGE_DB_PASSWORD` | `123` |

As variáveis de ambiente têm prioridade sobre os valores padrão do arquivo. No PowerShell, configure-as no mesmo terminal em que iniciará o backend:

```powershell
$env:SIGE_DB_URL = 'jdbc:mysql://localhost:3306/sige_desk?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC'
$env:SIGE_DB_USERNAME = 'root'
$env:SIGE_DB_PASSWORD = 'sua-senha-do-mysql'
```

Para mudar o nome do banco, substitua apenas `sige_desk` na URL pelo nome desejado. Ajuste também servidor e porta se sua instância usar outro endereço. Informe a senha real dessa instância MySQL; as instalações MySQL80 e XAMPP têm usuários, senhas e bancos independentes.

### Tabelas e migrações

Não é necessário configurar o nome de uma tabela para iniciar o sistema. O Flyway cria e atualiza as tabelas automaticamente a partir dos arquivos em [src/main/resources/db/migration](src/main/resources/db/migration):

- `V1__initial_schema.sql`: estrutura inicial, incluindo a tabela `users` e os demais cadastros.
- `V2__add_ticket_completion_timestamp.sql`: atualização do registro de conclusão dos tickets.
- `V3__demo_seed_tracking.sql`: controle da carga demo para impedir duplicação nas reinicializações.
- `V4__resolution_cycles_and_pause_calendar.sql`: marco do ciclo de resolução, intervalos de pausa e calendário no snapshot de SLA.

O banco também é criado automaticamente quando não existe, pois `spring.datasource.hikari.data-source-properties.createDatabaseIfNotExist` está habilitado. A conta MySQL precisa de permissão para criar o banco e aplicar as migrações, além de ler e gravar os dados. Se não tiver permissão de criação, o banco deve ser criado previamente por uma conta autorizada.

Os nomes das tabelas fazem parte das migrações e dos mapeamentos das entidades Java. Não renomeie tabelas nem altere migrações já aplicadas para configurar a conexão: mudanças de estrutura devem ser feitas por novas migrações e pelos ajustes correspondentes no código.

### Alternativa com Docker

Se optar pelo MySQL do projeto, execute na pasta `SIGE-Desk`:

```powershell
docker compose up -d mysql
```

Depois, no terminal do backend, configure `SIGE_DB_USERNAME` como `sige` e `SIGE_DB_PASSWORD` como `sige_local`, conforme [compose.yaml](../compose.yaml). O serviço usa a porta `3306`; essa porta precisa estar livre.

## Instalação e execução

Abra o PowerShell na pasta `SIGE-Desk/Backend`, configure a conexão e execute:

```powershell
.\mvnw.cmd spring-boot:run
```

Na primeira execução, o Maven Wrapper baixa o Maven e as dependências. As migrações são aplicadas durante a inicialização; a API fica disponível em [http://localhost:8080](http://localhost:8080).

Inicie o frontend em outro terminal seguindo seu [README](../Frontend/README.md).

## Login do administrador

Com a tabela `users` vazia, o backend cadastra automaticamente uma conta ativa com perfil Administrador, mesmo sem habilitar o perfil `demo`:

| Campo | Padrão local |
| --- | --- |
| Login / e-mail | `admin@sige.demo` |
| Senha | `123` |

Essas são as credenciais de acesso à aplicação, independentes das credenciais MySQL configuradas em `spring.datasource`. O administrador permite cadastrar os demais usuários pela interface.

Para escolher outra senha **antes da primeira criação da conta**, configure:

```powershell
$env:SIGE_ADMIN_PASSWORD = 'sua-senha-de-acesso'
.\mvnw.cmd spring-boot:run
```

A prioridade da senha inicial é `SIGE_ADMIN_PASSWORD`, depois `SIGE_DEMO_PASSWORD` e, na ausência das duas, `123`. O valor é armazenado com BCrypt. Use uma senha própria fora do desenvolvimento local.

Reiniciar o backend ou mudar essas variáveis não sobrescreve usuários, não redefine senhas e não reativa contas existentes. O cadastro automático só ocorre quando não há usuários.

## Dados de demonstração opcionais

Para adicionar os dados de validação do protótipo, inicie com o perfil `demo`. A carga funciona também em bancos que já têm o administrador ou outros registros:

```powershell
$env:SIGE_DEMO_PASSWORD = 'sua-senha-demo'
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
```

Nesse perfil, são acrescentados 24 tickets nos oito estados, organizações, campanhas, tipos, regras de SLA, comentários, histórico, notificações e arquivos CSV para download. Contas e cadastros compatíveis são reutilizados; suas senhas e alterações são preservadas. A carga é registrada em `demo_seed_runs` e só é aplicada uma vez por versão. A presença de usuários não bloqueia mais a carga; contas incompatíveis com os perfis/vínculos demo interrompem a inicialização com uma mensagem explícita.

Contas novas usam `SIGE_DEMO_PASSWORD` (padrão local `123`). Um administrador já existente mantém sua senha anterior. Consulte o [roteiro de validação demo](VALIDACAO-DEMO.md) para contas, cenários e telas a verificar. Os arquivos ficam em `uploads/demo/`, ou sob o caminho configurado em `SIGE_STORAGE_PATH`; preserve essa pasta para manter os downloads após reiniciar.

## Verificação e testes

- Saúde da API: [http://localhost:8080/actuator/health](http://localhost:8080/actuator/health).
- Swagger: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).

Para executar os testes do backend:

```powershell
.\mvnw.cmd test
```

O teste de migração MySQL depende de uma base vazia e descartável configurada separadamente; consulte o [README geral](../README.md#validação-de-migration-mysql).


A revisão funcional, as pendências por requisito e o roteiro HTTP em banco isolado estão em [docs/REVISAO-ENTREGA.md](../docs/REVISAO-ENTREGA.md).
