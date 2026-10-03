# SIGE Desk — roteiro de implementação

**Data:** 27/09/2026  
**Objetivo:** transformar o protótipo aprovado em uma aplicação React + TypeScript consumindo uma API Java/Spring Boot, com dados fictícios mantidos exclusivamente no backend e banco preparado por migrations.

## 1. Base de trabalho e decisões iniciais

- **Destino:** `SIGE-Desk/Frontend` e `SIGE-Desk/Backend`, hoje sem implementação.
- **Referência visual e de interação:** [protótipo navegável](../Prototipo-SIGE-Desk/README.md), especialmente `index.html`, `styles.css` e `app.js`. Reproduzir composição, navegação, estados, responsividade e linguagem visual aprovados. A migração é uma reimplementação em componentes React, não a inclusão do HTML ou do JavaScript antigo na aplicação nova.
- **Inventário funcional e perfis:** [mapa de telas](../Prototipos/Mapa-de-telas-SIGE-Desk.md). Cobrir as 15 telas e os quatro fluxos por perfil.
- **Regras de negócio detalhadas:** [especificação do sistema](../Trabalho/Documentacao%20do%20Projeto/Especificacao/Especificacao%20do%20sistema.md) e [orientação das telas](../Trabalho/Documentacao%20do%20Projeto/Prototipo/Prototipo%20especificação.md), sobretudo permissões, transições, SLA e exceções. O mapa e o protótipo guiam a experiência; divergências de regra devem ser resolvidas no contrato da API antes de implementá-las.

Remover **todos os identificadores T01, T02 etc. da interface**, inclusive títulos, menu, breadcrumb, tutorial, textos alternativos e mensagens. Manter os códigos apenas como referência de rastreabilidade nos documentos e testes. Remover cartões de **acesso rápido demo**, `Restaurar base`, textos de ambiente de demonstração e ferramentas específicas da demonstração. O login fica com e-mail e senha. Preservar o tutorial por perfil como ajuda de uso, se couber na experiência aprovada, sem ações que alterem tickets; sua preferência pode permanecer no navegador porque é estado de interface, não dado de negócio.

**Nenhum mock no frontend:** sem `mock-data.js`, `SIGE_SEED`, `localStorage` para tickets/usuários, respostas estáticas, interceptadores de API simulados, valores fixos em gráficos ou CSV montado de uma coleção local. Toda lista, indicador, permissão contextual, notificação, anexo e relatório deve vir da API. O frontend pode manter apenas estado efêmero de interface, como filtros, aba, ordenação e estado de formulário. Os dados fictícios para desenvolvimento são criados no backend sob perfil `demo`.

## 2. Arquitetura proposta

| Camada | Escolha | Responsabilidade |
| --- | --- | --- |
| Frontend | React, TypeScript estrito, Vite | Telas, interação, acessibilidade e composição visual. |
| Componentes | Material UI com tema próprio e tokens derivados de `styles.css` | Componentes consistentes sem perder o visual aprovado; criar componentes próprios apenas quando a biblioteca não cobrir o desenho, como o Kanban. |
| Navegação e dados | React Router, TanStack Query | Rotas protegidas, carregamento, cache e invalidação após mutações. |
| Formulários e textos | React Hook Form + Zod; i18next + react-i18next | Validação de interface alinhada ao contrato, mensagens em catálogos `pt-BR`, preparo para outro idioma. |
| Backend | Java, Spring Boot com Web, Security, Validation, Data JPA e Actuator | API, autorização, regras, persistência, observabilidade. Selecionar versões compatíveis e fixá-las no início da implementação. |
| Banco e evolução | MySQL + Flyway | Esquema versionado desde a primeira execução; mesma família de banco em desenvolvimento e na etapa seguinte. |
| Contrato | OpenAPI versionado | Definição de requisições, respostas e erros; gerar tipos ou cliente TypeScript para evitar divergência. |

**Organização:** monorepositório com `Frontend/`, `Backend/`, `docs/` para contrato e decisões, `compose.yaml` para MySQL local e README na raiz. No backend, começar com **monólito modular por domínio** (`auth`, `users`, `clients`, `campaigns`, `demandtypes`, `tickets`, `sla`, `notifications`, `reports`, `files`). Em cada módulo, separar API/DTO, aplicação, domínio e infraestrutura. Serviços de aplicação coordenam casos de uso; a regra de transição, acesso, cálculo de SLA e emissão de eventos fica centralizada no domínio. Controladores não acessam repositórios diretamente. Evitar uma classe genérica que concentre todos os cadastros.

**Autenticação e segurança:** Spring Security, senhas com hash forte, sessão por cookie `HttpOnly`, `Secure` em HTTPS e proteção CSRF para mutações. Usar mesmo domínio para frontend e API em deploy, com proxy local no desenvolvimento. A API identifica usuário e perfil pela sessão, nunca por ID de usuário enviado para autorizar uma ação. Aplicar autorização por papel **e por vínculo** em consulta, mutação, download e exportação. A interface oculta ações indisponíveis, mas a API sempre valida novamente. Respostas de credencial inválida ou conta inativa não devem revelar qual dos dois ocorreu.

## 3. Modelo de dados e banco

Criar `V1__initial_schema.sql` com chaves primárias, estrangeiras, índices e restrições para: usuário/perfil/vínculo com cliente, cliente, campanha, tipo de demanda e definição de campos, regra/calendário de SLA, ticket, ciclo de resolução, comentário, anexo/evidência, aprovação, histórico/auditoria, notificação e registro de eventos emitidos. Datas devem ser persistidas em UTC e formatadas na interface segundo o fuso configurado; valores monetários e métricas com tipos numéricos apropriados, nunca `float` para dinheiro.

O ticket deve preservar **autor do registro** e **solicitante**, urgência informada e prioridade oficial separadas, campanha pendente ou vinculada, configuração do tipo aplicada, regra/calendário de SLA aplicados e fase de origem de `Aguardando cliente`. Usar snapshots ou versão da configuração para que alterações futuras em tipos e SLA não reescrevam o histórico de tickets existentes. Histórico de mudanças relevantes é append-only, com ator, instante, campo/ação, valor anterior, novo valor e motivo. Não excluir tickets concluídos/cancelados nem vínculos históricos ao inativar cadastros.

Flyway é responsável pelo esquema; não usar `ddl-auto=update`. O perfil `demo` executa um **semeador idempotente no backend**, separado das migrations de produção, com pelo menos um usuário ativo por perfil e dados fictícios coerentes para os oito estados do ticket. A senha inicial vem de variável de ambiente, é armazenada apenas como hash e não aparece em código, no bundle do frontend ou em migration. Em outros perfis, o semeador fica desligado. Documentar as contas de desenvolvimento por e-mail e o modo de definir a senha local, sem botões de acesso rápido na tela.

Anexos devem ter metadados no banco e conteúdo em armazenamento privado configurável, inicialmente um diretório de desenvolvimento no backend. Validar tamanho, tipo e nome; fazer download por endpoint autenticado que verifica o acesso ao ticket. Links de evidência devem ser URLs válidas, sem `mock://`.

## 4. Contrato mínimo da API

Adotar `/api/v1`, paginação e filtros definidos no servidor, erros padronizados com código, mensagem traduzível e detalhes de validação. Não enviar entidades JPA diretamente. O frontend exibe mensagens do catálogo por código de erro e recebe campos dinâmicos/valores de referência da API.

| Área | Operações iniciais |
| --- | --- |
| Sessão | `POST /auth/login`, `POST /auth/logout`, `GET /auth/me`, consulta de token CSRF se necessária à configuração. |
| Painel | `GET /dashboard` com indicadores, distribuição por status, saúde do SLA e atividade recente **calculados para o perfil autenticado**. |
| Tickets | `GET /tickets` com paginação, busca, cliente, campanha, tipo, prioridade, responsável, status e período de abertura; `POST /tickets`; `GET /tickets/{id}`; `GET /tickets/{id}/actions` ou ações permitidas no detalhe. |
| Ciclo do ticket | Endpoints de comando para iniciar triagem, classificar/atribuir, solicitar/completar/conferir complemento, retomar, registrar execução, enviar à validação, aprovar, solicitar correção, concluir sem aprovação quando o tipo permitir, cancelar e reabrir. Cada comando recebe apenas os dados necessários e valida estado, ator e vínculo. |
| Comunicação e arquivos | Comentários, upload, listagem e download de anexos/evidências; histórico somente leitura. |
| Notificações | Listar, filtrar e marcar como lidas; gerar eventos no backend junto com a transação da mudança. |
| Relatórios | Indicadores e `GET /reports/tickets/export` com os **mesmos filtros e escopo** da listagem, para CSV gerado no servidor. |
| Cadastros | CRUD lógico/ativação de clientes, campanhas, tipos de demanda, regras/calendários de SLA e usuários, com validações de vínculo e bloqueios de inativação. |

Incluir no contrato exemplos de sucesso, `400` validação, `401` sessão ausente/expirada, `403` ação proibida, `404` recurso fora do escopo ou inexistente e `409` transição/conflito de versão. Para edição concorrente de ticket, usar coluna de versão e resposta `409`, com recarga e explicação ao usuário.

## 5. Regras que precisam de uma única implementação no backend

1. **Visibilidade:** Cliente vê tickets da sua organização; Gestor de tráfego, apenas os atribuídos; Atendimento e Administrador, o escopo da agência. Aplicar a mesma política em listas, detalhe, comentários, notificações, relatórios e anexos.
2. **Estados:** `Aberta`, `Em triagem`, `Em execução`, `Aguardando cliente`, `Em validação`, `Concluída`, `Reaberta`, `Cancelada`. Comandos verificam transição e pré-condições. Abrir a tela de triagem não muda o status; a ação de iniciar triagem muda.
3. **Execução e aprovação:** somente o Gestor atribuído registra execução; Cliente ativo da organização decide em validação. Tipo sem aprovação pode concluir diretamente após execução e registra a dispensa derivada da configuração. Evidência é obrigatória quando o tipo exigir.
4. **Complemento:** registrar motivo e origem (triagem/execução); envio do Cliente marca recebido, mas mantém a pausa. Atendimento/Admin conferem e retomam a fase correta. Cancelar só antes do início da execução.
5. **Reabertura:** correção em validação preserva o ciclo de resolução; reabertura de concluída exige motivo e inicia novo ciclo. Atendimento/Admin reconfirmam prioridade e responsável antes de devolver à execução.
6. **SLA:** prioridade oficial é definida na triagem, sem inferência da urgência. Calcular primeira resposta e resolução em horas úteis a partir da abertura, com calendário e precedência cliente+tipo > cliente > tipo > padrão. Só resposta efetiva encerra o primeiro prazo. Pausar resolução em aguardo e, se a regra prever, em validação. Preservar o cálculo aplicado e identificar qual prazo venceu.
7. **Notificações e auditoria:** publicar os eventos previstos na especificação dentro da transação do caso de uso, com destinatários filtrados por autorização. Registrar toda mudança de estado, responsável, prioridade, prazo, aprovação e inativação relevante.

## 6. Ordem de implementação e entregáveis

| Fase | Trabalho | Entregável verificável |
| --- | --- | --- |
| 0. Contrato e decisões | Comparar protótipo, mapa e especificação; fechar matriz papel × ação × estado × vínculo; mapear telas para rotas sem códigos T; definir DTOs, erros e estados vazios. | OpenAPI inicial, matriz de autorização e lista de decisões registradas. |
| 1. Fundação | Criar projetos, qualidade de código, configuração por ambiente, MySQL local, Flyway V1, healthcheck, autenticação, semeador `demo`, shell React, tema e i18n. | Login real, sessão, quatro contas de desenvolvimento e esquema criado do zero. |
| 2. Cadastros e referências | Clientes, campanhas, tipos, SLA e usuários; listas, formulários, ativação e inativação; endpoints de opções para formulários. | Cadastros persistidos na API, sem valores de negócio embutidos no frontend. |
| 3. Jornada principal | Abrir ticket, listar em Kanban/tabela, filtrar, consultar detalhe, triagem, atribuição, execução, evidência, validação e conclusão. | Fluxo completo entre contas distintas, com permissões no servidor. |
| 4. Exceções e colaboração | Complemento, pausa/retomada, correção, reabertura, cancelamento, comentários, anexos, histórico e notificações. | Oito estados e caminhos alternativos cobertos com dados de backend. |
| 5. Gestão e acabamento | Dashboard real, relatórios/CSV, tutorial por perfil, responsividade, acessibilidade, erros/estados vazios e revisão visual tela a tela. | Todas as telas equivalentes ao protótipo, sem marcas de demonstração. |
| 6. Validação e entrega | Testes de domínio e autorização, integração com MySQL migrado, testes dos fluxos críticos, build, análise de bundle, documentação de execução e configuração. | Aplicação reproduzível localmente e pronta para a próxima etapa de implantação/banco definitivo. |

Para cada tela, implementar nesta sequência curta: contrato e dados → serviço/regra → componente React → estados de carregamento/erro/vazio → conferência visual com o protótipo → teste do fluxo e permissão. Isso evita criar uma tela bonita que ainda dependa de valores locais.

## 7. Estrutura esperada

```text
SIGE-Desk/
  README.md
  PLANO-DE-IMPLEMENTACAO.md
  compose.yaml
  docs/
    openapi.yaml
    permissoes.md
    decisoes.md
  Frontend/
    src/
      app/                 # router, providers, tema, i18n
      shared/              # componentes, cliente HTTP, tipos gerados
      features/            # auth, tickets, dashboard, notificacoes, relatorios, cadastros
      locales/pt-BR/       # textos e mensagens
  Backend/
    src/main/java/.../     # módulos de domínio
    src/main/resources/
      db/migration/        # Flyway
    src/test/              # domínio, API, integração
```

## 8. Critérios de aceite

- As 15 telas e os fluxos Cliente, Atendimento, Gestor de tráfego e Administrador funcionam com sessão e dados retornados pela API; nenhum usuário precisa trocar de perfil dentro de uma mesma sessão para completar o fluxo.
- Não há códigos `T01`–`T15`, acesso rápido demo, restauração da base nem números/linhas de exemplo visíveis no frontend. Busca no código confirma que não há seed, senha ou regras de negócio duplicadas no bundle.
- Cada perfil tem ao menos uma conta fictícia ativa criada pelo backend em `demo`; credenciais inválidas/contas inativas são recusadas; usuário não autorizado não vê nem altera dados de outro cliente/ticket, inclusive via URL direta e download.
- Lista, Kanban, dashboard, notificações, SLA e relatórios refletem o mesmo estado persistido. CSV respeita filtros e abrangência da consulta.
- Abrir, triar, executar, validar, complementar, corrigir, reabrir e cancelar seguem as transições e pré-condições documentadas, com histórico íntegro. Confirmar especialmente aprovação obrigatória, conclusão sem aprovação e os dois tipos de reabertura.
- Migrations criam o banco vazio sem intervenção manual; semeador `demo` pode rodar novamente sem duplicar registros e nunca roda fora do perfil apropriado.
- Formulários têm validação de interface e de servidor; mensagens e rótulos vêm do i18n; teclado, foco, contraste e telas pequenas são conferidos com o protótipo aprovado.
- Testes de integração exercitam autorização por vínculo, migração MySQL, transições, cálculo de SLA e exportação filtrada. Os quatro percursos por perfil são conferidos de ponta a ponta antes da entrega.

## 9. Decisões a fechar antes de codificar as permissões

1. **Manutenção de clientes:** o [mapa de telas](../Prototipos/Mapa-de-telas-SIGE-Desk.md) e o protótipo permitem `Clientes` a Atendimento e Administrador; o RF-03 e o [guia detalhado](../Trabalho/Documentacao%20do%20Projeto/Prototipo/Prototipo%20especificação.md) reservam cadastro/edição ao Administrador. **Regra provisória para o contrato:** somente Administrador modifica clientes; Atendimento pode consultar os dados necessários à operação. Registrar a decisão final na matriz antes de liberar os endpoints e os controles da tela.
2. **Execução por Atendimento/Admin:** a matriz de transições da especificação os menciona em `Em execução`, enquanto os perfis, o guia da tela e o protótipo atribuem o registro ao Gestor responsável. **Regra provisória:** somente Gestor atribuído executa; Atendimento/Admin podem triar, reassumir o fluxo e reatribuir, sem registrar execução em nome dele.
3. **Nome e escopo da entrega:** o pedido diz “subir pra valer”, mas as fontes acadêmicas delimitam um MVP demonstrável com dados fictícios. Este roteiro entrega a aplicação funcional e persistida, preparada para implantação; endereço, infraestrutura e operação com dados reais devem ser definidos na etapa de publicação.

## 10. Estado da implementação

| Fase | Estado | Evidência |
| --- | --- | --- |
| 0. Contrato e decisões | Implementada | Matriz em `docs/permissoes.md`, decisões em `docs/decisoes.md`, contrato versionado em `docs/openapi.yaml` e contrato vivo em `/v3/api-docs`. Conflitos de versão retornam `409 VERSION_CONFLICT` e são traduzidos no frontend. |
| 1. Fundação | Implementada, com validação de banco pendente | React/Vite/TypeScript, i18n, Spring Security, sessão/CSRF, MySQL/Flyway, `compose.yaml` e semeador exclusivo do perfil `demo`. A migration contra uma instância MySQL ainda precisa ser executada no ambiente local. |
| 2. Cadastros e referências | Implementada | Endpoints e telas com busca, formulários e ativação para clientes, campanhas, tipos, regras de SLA e usuários; opções de formulário vêm da API. |
| 3. Jornada principal | Implementada | Criação com métricas e anexos, filtros e paginação no servidor, tabela, Kanban, detalhe, triagem com campanha/tipo, atribuição, execução e validação são persistidos e autorizados no backend. |
| 4. Exceções e colaboração | Implementada | Complemento, pausa/retomada, correção, reabertura, cancelamento, comentários, anexos, evidências, auditoria e notificações. A publicação persiste notificações e um evento outbox por transição, com teste automatizado. |
| 5. Gestão e acabamento | Implementada, com conferência visual pendente | Dashboard com saúde do SLA, relatórios/CSV com totais e tempo médio, navegação responsiva, carregamento, erro e vazio. O tutorial por perfil está disponível na barra lateral e usa o catálogo de tradução. A comparação visual precisa ser feita com a aplicação em execução. |
| 6. Validação e entrega | Em andamento | Build de produção do frontend e testes unitários de transição, autorização, paginação, painel, relatório e cálculo de SLA passaram. Há um teste de integração de migration preparado para uma base MySQL descartável por variáveis de ambiente. A busca estática confirmou que o frontend não contém seed, mock, `localStorage` de negócio, acesso rápido de demonstração nem códigos T01–T15. As migrations `V1` e `V2` aguardam execução em MySQL; também faltam os fluxos autenticados completos em ambiente executável. |

## Fontes técnicas para as escolhas

- [React com TypeScript](https://react.dev/learn/typescript), [Material UI](https://mui.com/material-ui/getting-started/) e [react-i18next](https://react.i18next.com/guides/quick-start).
- [React Router](https://reactrouter.com/start/declarative/routing) e [TanStack Query](https://tanstack.com/query/latest/docs/framework/react).
- [Spring Security — autorização de requisições](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html) e [Spring Boot — inicialização do banco com Flyway](https://docs.spring.io/spring-boot/how-to/data-initialization.html).
