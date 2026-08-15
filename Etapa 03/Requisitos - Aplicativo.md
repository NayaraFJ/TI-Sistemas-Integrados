# Help Desk para Gestão de Tráfego Pago

## Etapa 03 — Requisitos preliminares do aplicativo

> Este documento reúne requisitos iniciais derivados do problema, objetivos e referencial teórico. Eles devem ser validados com usuários e podem ser refinados antes da implementação.

## 1. Visão do produto

O SIGE Desk será uma aplicação web para organizar demandas de tráfego pago entre clientes e equipe de uma agência de marketing digital. O sistema centralizará solicitações, responsáveis, prazos, status, comentários, aprovações e evidências de execução.

## 2. Perfis de usuário

| Perfil | Permissões principais |
| --- | --- |
| Cliente | Abrir e consultar as próprias demandas; enviar comentários/anexos; aprovar ou solicitar complementação. |
| Atendimento / gestor de conta | Registrar demandas para clientes; fazer triagem; definir prioridade/prazo; acompanhar e comunicar-se. |
| Gestor de tráfego | Consultar demandas atribuídas; atualizar status; comentar; registrar execução e evidências. |
| Administrador | Gerenciar usuários, clientes, campanhas, tipos de demanda, prioridades e relatórios. |

## 3. Requisitos funcionais

| ID | Requisito | Prioridade |
| --- | --- | --- |
| RF-01 | O sistema deve permitir autenticação de usuários. | Alta |
| RF-02 | O sistema deve controlar permissões conforme o perfil de usuário. | Alta |
| RF-03 | O administrador deve poder cadastrar, editar, ativar e inativar clientes. | Alta |
| RF-04 | O administrador ou atendimento deve poder cadastrar campanhas vinculadas a um cliente. | Alta |
| RF-05 | O sistema deve permitir abrir ticket com cliente, campanha, canal, tipo, prioridade, prazo, assunto e descrição. | Alta |
| RF-06 | O sistema deve gerar identificador único para cada ticket. | Alta |
| RF-07 | O atendimento deve poder classificar e atribuir um ticket a um responsável. | Alta |
| RF-08 | O responsável deve poder atualizar o status do ticket. | Alta |
| RF-09 | O sistema deve registrar data, hora, usuário e motivo em cada mudança de status ou responsável. | Alta |
| RF-10 | Usuários autorizados devem poder incluir comentários e anexos no ticket. | Alta |
| RF-11 | O sistema deve permitir registrar aprovação, reprovação ou solicitação de complemento pelo cliente. | Média |
| RF-12 | O responsável deve poder registrar a ação executada e evidências no encerramento. | Alta |
| RF-13 | O sistema deve permitir cancelar ticket com motivo obrigatório. | Média |
| RF-14 | O usuário deve poder filtrar tickets por cliente, campanha, tipo, prioridade, responsável, status e período. | Alta |
| RF-15 | O painel deve apresentar total de tickets por status, prioridade, responsável e prazo. | Média |
| RF-16 | O sistema deve identificar tickets vencidos e tickets aguardando resposta do cliente. | Média |
| RF-17 | O sistema deve permitir consultar histórico completo de cada ticket. | Alta |
| RF-18 | O sistema deve permitir registrar métricas de contexto, como CTR, CPC, conversão, CPA e ROAS, quando aplicável. | Baixa |
| RF-19 | O sistema deve permitir exportar uma lista de tickets filtrada para apoio a relatórios. | Baixa |

## 4. Requisitos não funcionais

| ID | Requisito | Critério inicial |
| --- | --- | --- |
| RNF-01 | Usabilidade | O fluxo de abertura de ticket deve ser compreensível por cliente e equipe sem treinamento técnico avançado. |
| RNF-02 | Segurança | Senhas devem ser armazenadas de forma protegida e o acesso deve respeitar o perfil do usuário. |
| RNF-03 | Privacidade | O sistema deve coletar somente dados necessários para a gestão da demanda e não deve armazenar bases de audiência ou dados sensíveis. |
| RNF-04 | Rastreabilidade | Alterações de status, responsável, prazo e aprovação devem permanecer no histórico. |
| RNF-05 | Integridade | Um ticket concluído ou cancelado não poderá ser apagado sem manter registro administrativo. |
| RNF-06 | Desempenho | Listagem e abertura de ticket devem ter resposta percebida como imediata em uso normal; o tempo-alvo será definido nos testes. |
| RNF-07 | Compatibilidade | A interface deve funcionar em navegadores modernos de computador e celular. |
| RNF-08 | Disponibilidade | O sistema deve informar indisponibilidade e evitar perda de dados ao salvar uma solicitação. |
| RNF-09 | Acessibilidade | Campos devem possuir rótulos claros, contraste adequado e navegação possível por teclado. |

## 5. Regras de negócio

| ID | Regra |
| --- | --- |
| RN-01 | Todo ticket deve possuir cliente, tipo de demanda, prioridade, assunto, descrição e solicitante. |
| RN-02 | Quando a demanda estiver vinculada a campanha existente, cliente e campanha devem ser compatíveis. |
| RN-03 | Somente atendimento ou administrador pode definir prioridade, prazo e responsável na triagem. |
| RN-04 | Somente responsável, atendimento ou administrador pode alterar o status de um ticket. |
| RN-05 | O status “Concluída” exige descrição da ação executada; evidência será obrigatória apenas para tipos configurados. |
| RN-06 | O status “Cancelada” exige motivo de cancelamento. |
| RN-07 | O status “Aguardando cliente” deve registrar qual informação ou aprovação é necessária. |
| RN-08 | O cliente deve visualizar somente tickets vinculados à sua organização. |
| RN-09 | Alterações de orçamento, público ou criativo podem exigir aprovação do cliente antes da execução, conforme configuração do tipo de demanda. |

## 6. Dados principais

| Entidade | Dados iniciais |
| --- | --- |
| Usuário | Identificador, nome, e-mail, perfil, status e cliente vinculado quando o perfil for Cliente. |
| Cliente | Identificador, nome, contato e status. |
| Campanha | Identificador, cliente, nome, canal, objetivo e status. |
| Ticket | Número, cliente, campanha, tipo, canal, prioridade, assunto, descrição, solicitante, responsável, prazo e status. |
| Comentário | Ticket, autor, data/hora, texto e anexo opcional. |
| Histórico | Ticket, campo alterado, valor anterior, novo valor, usuário e data/hora. |
| Aprovação | Ticket, decisão, usuário, data/hora e observação. |
| Evidência | Ticket, descrição, link ou arquivo, autor e data/hora. |

## 7. Critérios de aceite iniciais

| Cenário | Resultado esperado |
| --- | --- |
| Cliente abre uma demanda | O ticket recebe número, status “Aberta” e fica visível para atendimento. |
| Atendimento faz a triagem | Prioridade, prazo e responsável ficam registrados no histórico. |
| Gestor de tráfego executa uma alteração | O ticket recebe comentário/evidência e pode ser encaminhado para conclusão. |
| Cliente precisa aprovar uma mudança | O ticket fica “Aguardando cliente” até decisão registrada. |
| Prazo é ultrapassado | O ticket é identificado como vencido no painel ou listagem. |
| Ticket é concluído | A ação executada fica registrada e o histórico permanece disponível. |

## 8. Fora do escopo do MVP

- integração automática com APIs de Google Ads e Meta Ads;
- criação automática de anúncios e criativos;
- cálculo automatizado de ROAS, CPA ou previsão de desempenho;
- gestão financeira/faturamento;
- chat em tempo real externo ao histórico do ticket.
