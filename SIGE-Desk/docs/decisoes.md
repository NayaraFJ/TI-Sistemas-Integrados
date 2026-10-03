# Decisões de arquitetura

1. O banco da aplicação é **MySQL 8.4**, com Flyway e `ddl-auto=validate`. O esquema não é criado pelo Hibernate.
2. O sistema é um monólito modular Spring Boot. As regras de estado, visibilidade e SLA ficam no backend.
3. A autenticação usa sessão protegida por cookie `HttpOnly`; mutações usam proteção CSRF. O cliente não transmite identificador de usuário para se autorizar.
4. O perfil `demo` semeia apenas dados fictícios no backend e exige `SIGE_DEMO_PASSWORD`. O frontend não contém seed nem interceptador de API.
5. Arquivos são privados, metadados ficam no MySQL e conteúdo é gravado no diretório configurado por `SIGE_STORAGE_PATH`. Downloads sempre verificam a visibilidade do ticket.
6. Eventos de ticket criam notificações e um registro de outbox na mesma transação, preparando a integração futura de e-mail sem acoplar a regra de negócio ao canal externo.
