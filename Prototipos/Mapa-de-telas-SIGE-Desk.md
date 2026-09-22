# SIGE Desk — Protótipo e fluxos


## Perfis de acesso

| Perfil            | Papel principal                                                                |
| ----------------- | ------------------------------------------------------------------------------ |
| Cliente           | Abre solicitações, acompanha tickets da própria organização e valida entregas. |
| Atendimento       | Recebe, organiza, classifica e acompanha as demandas.                          |
| Gestor de tráfego | Executa tickets atribuídos e registra evidências.                              |
| Administrador     | Supervisiona a operação e mantém cadastros, SLA e usuários.                    |















## Telas

### T01 — Acessar o ambiente

![T01 — Acessar o ambiente](sige-desk-telas/T01-login.png)

**O que faz:** autentica o usuário por credenciais ou pelos quatro acessos rápidos de demonstração.

**Para que serve:** iniciar uma sessão e adaptar os menus, dados visíveis e ações ao perfil autenticado.

**Acessos:** público; atende Cliente, Atendimento, Gestor de tráfego e Administrador.

### T02 — Visão geral

![T02 — Visão geral](sige-desk-telas/T02-visao-geral.png)

**O que faz:** apresenta indicadores de tickets, atividade recente, distribuição por status e saúde do SLA.

**Para que serve:** dar uma leitura rápida da operação antes de abrir, tratar ou analisar demandas.

**Acessos:** Cliente, Atendimento, Gestor de tráfego e Administrador. Os números respeitam a visão de cada perfil.

### T03 — Tickets

![T03 — Tickets](sige-desk-telas/T03-tickets.png)

**O que faz:** lista tickets em Kanban ou tabela, com filtros, busca e atalhos para o detalhe ou ação compatível com o status.

**Para que serve:** localizar e acompanhar a fila de trabalho ponta a ponta.

**Acessos:** Cliente, Atendimento, Gestor de tráfego e Administrador; cada perfil visualiza somente os tickets permitidos.

### T04 — Novo ticket

![T04 — Novo ticket](sige-desk-telas/T04-novo-ticket.png)

**O que faz:** coleta identificação, assunto, canal, urgência, descrição, métricas e anexos de uma solicitação.

**Para que serve:** registrar uma demanda com informação suficiente para a triagem.

**Acessos:** Cliente, Atendimento e Administrador.

### T05 — Detalhe do ticket

![T05 — Detalhe do ticket](sige-desk-telas/T05-detalhe-ticket.png)

**O que faz:** reúne status, SLA, comentários, anexos, evidências, contexto e histórico auditável de uma demanda.

**Para que serve:** consultar o estado atual e executar as ações liberadas no ciclo do ticket.

**Acessos:** Cliente, Atendimento, Gestor de tráfego e Administrador; a visibilidade do ticket é restringida por perfil e vínculo.

### T06 — Triagem

![T06 — Triagem](sige-desk-telas/T06-triagem.png)

**O que faz:** define prioridade, responsável, tipo, campanha e regra de SLA; também permite solicitar complemento ou cancelar.

**Para que serve:** transformar uma solicitação aberta em trabalho organizado e encaminhado para execução.

**Acessos:** Atendimento e Administrador.

### T07 — Execução

![T07 — Execução](sige-desk-telas/T07-execucao.png)

**O que faz:** registra a entrega realizada, link ou anexo de evidência e permite pausar a demanda por dependência externa.

**Para que serve:** documentar o trabalho operacional antes da validação ou conclusão.

**Acessos:** Gestor de tráfego, apenas para tickets atribuídos ao usuário.

### T08 — Validação

![T08 — Validação](sige-desk-telas/T08-validacao.png)

**O que faz:** mostra a entrega submetida e permite aprová-la ou solicitar correção com observação.

**Para que serve:** formalizar a decisão do cliente e fechar o ticket ou devolvê-lo à execução.

**Acessos:** Cliente, para tickets de sua própria organização que estejam em validação.

### T09 — Notificações

![T09 — Notificações](sige-desk-telas/T09-notificacoes.png)

**O que faz:** organiza eventos relevantes, como nova atribuição, pedido de complemento, entrega para validação e alteração de status.

**Para que serve:** evitar que uma mudança importante no fluxo passe despercebida.

**Acessos:** Cliente, Atendimento, Gestor de tráfego e Administrador.

### T10 — Relatórios

![T10 — Relatórios](sige-desk-telas/T10-relatorios.png)

**O que faz:** consolida volume, conclusão, cumprimento de SLA e tempo médio, com filtros e exportação de dados.

**Para que serve:** acompanhar desempenho operacional e apoiar decisões de gestão.

**Acessos:** Atendimento e Administrador.

### T11 — Clientes

![T11 — Clientes](sige-desk-telas/T11-clientes.png)

**O que faz:** cadastra, edita, busca e ativa/inativa organizações clientes.

**Para que serve:** manter a base que alimenta abertura de tickets, vínculos e regras operacionais.

**Acessos:** Atendimento e Administrador.

### T12 — Campanhas

![T12 — Campanhas](sige-desk-telas/T12-campanhas.png)

**O que faz:** mantém campanhas por cliente, canal e objetivo, com busca, edição e ativação.

**Para que serve:** permitir contextualização consistente de demandas na abertura e na triagem.

**Acessos:** Atendimento e Administrador.

### T13 — Tipos de demanda

![T13 — Tipos de demanda](sige-desk-telas/T13-tipos-demanda.png)

**O que faz:** configura tipos de solicitação, campos, exigência de evidência e necessidade de aprovação.

**Para que serve:** padronizar o tratamento de cada categoria de demanda.

**Acessos:** Administrador.

### T14 — Configuração de SLA

![T14 — Configuração de SLA](sige-desk-telas/T14-sla.png)

**O que faz:** mantém calendários, prazos e regras de precedência por cliente, tipo ou padrão.

**Para que serve:** calcular e acompanhar os compromissos de primeira resposta e resolução.

**Acessos:** Administrador.

### T15 — Usuários e perfis

![T15 — Usuários e perfis](sige-desk-telas/T15-usuarios-perfis.png)

**O que faz:** administra usuários, e-mails, papéis, vínculo com clientes e situação de acesso.

**Para que serve:** manter a separação de responsabilidades e o controle de acesso do SIGE Desk.

**Acessos:** Administrador.

## Fluxos por perfil

Os diagramas abaixo mostram as telas que cada perfil pode alcançar. Os rótulos nas setas indicam a função que conduz à próxima tela. As imagens não cortam no visualizador e também estão disponíveis em SVG, caso seja necessário ampliar ou editar.

### Cliente

![Fluxo de telas — Cliente](sige-desk-fluxos/fluxo-cliente.png)

[Abrir versão SVG](sige-desk-fluxos/fluxo-cliente.svg)

### Atendimento

![Fluxo de telas — Atendimento](sige-desk-fluxos/fluxo-atendimento.png)

[Abrir versão SVG](sige-desk-fluxos/fluxo-atendimento.svg)

### Gestor de tráfego

![Fluxo de telas — Gestor de tráfego](sige-desk-fluxos/fluxo-gestor-trafego.png)

[Abrir versão SVG](sige-desk-fluxos/fluxo-gestor-trafego-final.svg)

### Administrador

![Fluxo de telas — Administrador](sige-desk-fluxos/fluxo-administrador.png)

[Abrir versão SVG](sige-desk-fluxos/fluxo-administrador.svg)
