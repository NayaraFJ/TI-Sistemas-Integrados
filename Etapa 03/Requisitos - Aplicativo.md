# Help Desk para Gestão de Demandas de Tráfego Pago

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
| RF-20 | O sistema deve enviar notificação no sistema e, quando configurado, por e-mail ao responsável ou solicitante quando o ticket for atribuído, comentado, estiver aguardando cliente, vencer ou for concluído/reaberto. | Alta |
| RF-21 | O sistema deve exibir os prazos de primeira resposta e resolução do SLA, incluindo situação de vencimento ou pausa. | Alta |

## 4. Requisitos não funcionais

| ID | Requisito | Critério inicial |
| --- | --- | --- |
| RNF-01 | Usabilidade | O fluxo de abertura de ticket deve ser compreensível por cliente e equipe sem treinamento técnico avançado. |
| RNF-02 | Segurança | Senhas devem ser armazenadas de forma protegida; autenticação, autorização por perfil e auditoria devem impedir e registrar acessos indevidos. |
| RNF-03 | Privacidade | O sistema deve coletar somente dados necessários para a gestão da demanda e não deve armazenar bases de audiência ou dados sensíveis. |
| RNF-04 | Rastreabilidade | Alterações de status, responsável, prazo e aprovação devem permanecer no histórico. |
| RNF-05 | Integridade | Um ticket concluído ou cancelado não poderá ser apagado sem manter registro administrativo. |
| RNF-06 | Desempenho | Listagem e abertura de ticket devem ter resposta percebida como imediata em uso normal; o tempo-alvo será definido nos testes. |
| RNF-07 | Compatibilidade | A interface deve funcionar em navegadores modernos de computador e celular. |
| RNF-08 | Disponibilidade | O sistema deve informar indisponibilidade e evitar perda de dados ao salvar uma solicitação. |
| RNF-09 | Acessibilidade | Campos devem possuir rótulos claros, contraste adequado e navegação possível por teclado. |
| RNF-10 | Backup | O sistema deve manter cópia diária dos dados e anexos, com retenção mínima de 30 dias e teste periódico de restauração. |
| RNF-11 | Retenção | Tickets, histórico e anexos devem ser mantidos por 24 meses após conclusão ou cancelamento; ao fim do período, devem ser anonimizados ou eliminados, salvo obrigação legal ou contratual. |

## 5. Acordos de nível de serviço (SLA) preliminares

Os SLAs serão configuráveis pela agência e representam uma proposta inicial para o MVP. Os prazos são contados em horas úteis: a primeira resposta vai da abertura ao primeiro retorno registrado ao solicitante; a resolução vai da abertura à conclusão. O relógio de resolução fica pausado quando o ticket estiver em **Aguardando cliente**.

| Prioridade | Primeira resposta | Resolução prevista |
| --- | ---: | ---: |
| Urgente | 2 horas úteis | 8 horas úteis |
| Alta | 4 horas úteis | 16 horas úteis |
| Média | 8 horas úteis | 24 horas úteis |
| Baixa | 16 horas úteis | 40 horas úteis |

## 6. Regras de negócio

| ID | Regra |
| --- | --- |
| RN-01 | Todo ticket deve possuir cliente, tipo de demanda, prioridade, assunto, descrição e solicitante. |
| RN-02 | Quando a demanda estiver vinculada a campanha existente, cliente e campanha devem ser compatíveis. |
| RN-03 | Somente atendimento ou administrador pode definir prioridade, prazo e responsável na triagem. |
| RN-04 | Somente responsável, atendimento ou administrador pode alterar o status de um ticket. |
| RN-05 | Após a execução, o ticket deve seguir para “Em validação”, com descrição da ação executada e evidência quando configurada para o tipo de demanda. |
| RN-06 | O status “Concluída” exige validação/aprovação necessária ou registro de que o tipo de demanda não requer validação do cliente. |
| RN-07 | O status “Cancelada” exige motivo de cancelamento. |
| RN-08 | O status “Aguardando cliente” deve registrar qual informação ou aprovação é necessária. |
| RN-09 | O cliente deve visualizar somente tickets vinculados à sua organização. |
| RN-10 | Alterações de orçamento, público ou criativo podem exigir aprovação do cliente antes da execução, conforme configuração do tipo de demanda. |
| RN-11 | Cliente, atendimento ou administrador pode reabrir um ticket concluído mediante justificativa; a reabertura preserva todo o histórico anterior. |
| RN-12 | Ao vencer o SLA, o sistema deve sinalizar o ticket e notificar o responsável e o atendimento. |
| RN-13 | Atribuição, comentário, mudança para “Aguardando cliente”, “Em validação”, “Concluída” e “Reaberta” devem gerar notificações aos envolvidos. |

## 7. Governança de dados, acesso e continuidade

- O acesso será baseado no princípio do menor privilégio: cada perfil verá e alterará apenas as informações necessárias às suas responsabilidades.
- Toda alteração relevante será auditável por usuário, data/hora, valor anterior e novo valor.
- Anexos deverão ser vinculados a um ticket e seguir a mesma política de retenção do respectivo histórico.
- O MVP utilizará dados fictícios ou autorizados para teste; credenciais de plataformas de anúncios e bases de audiência não serão armazenadas.
- Backups diários deverão ser protegidos e a restauração deverá ser testada periodicamente antes de uso em produção.
- A política de retenção de 24 meses é uma proposta inicial e deve ser revisada conforme contratos, finalidade do tratamento e orientação jurídica.
- A finalidade declarada para os dados do MVP é registrar, comunicar, executar, validar e auditar demandas de tráfego pago. Antes da produção, a agência deverá identificar controlador, operadores e base legal aplicável, além de publicar aviso de privacidade compatível.
- O sistema deverá manter canal de contato para solicitações de titulares relacionadas a acesso, correção ou eliminação de dados, encaminhando-as ao responsável definido pela agência.
- O procedimento de incidente deverá prever registro, contenção, avaliação do impacto, decisão de comunicação e ações de correção, observadas as obrigações legais aplicáveis.

## 8. Dados principais

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

## 9. Critérios de aceite iniciais

| Cenário | Resultado esperado | Requisito(s) relacionado(s) | Caso(s) de teste |
| --- | --- | --- | --- |
| Cliente abre uma demanda | O ticket recebe número, status “Aberta” e fica visível para atendimento. | RF-05, RF-06 | CT-01 |
| Atendimento faz a triagem | Prioridade, prazo e responsável ficam registrados no histórico. | RF-07, RF-09, RN-03 | CT-02 |
| Gestor de tráfego executa uma alteração | O ticket recebe comentário/evidência e pode ser encaminhado para validação. | RF-08, RF-10, RF-12, RN-05 | CT-03, CT-05 |
| Cliente precisa aprovar uma mudança | O ticket fica “Aguardando cliente” até decisão registrada. | RF-11, RN-08, RN-10 | CT-04 |
| Entrega precisa ser confirmada | O ticket segue para “Em validação”; após aprovação, torna-se “Concluída”. | RF-11, RN-05, RN-06 | CT-05 |
| Cliente solicita correção | O ticket é reaberto com justificativa e retorna à fila de execução. | RF-11, RN-11 | CT-05 |
| Prazo é ultrapassado | O ticket é identificado como vencido no painel ou listagem. | RF-16, RF-21, RN-12 | CT-06 |
| Ticket é atribuído, comentado ou vencido | Os envolvidos recebem notificação conforme seu perfil e configuração. | RF-20, RN-13 | CT-07 |
| Cliente tenta acessar ticket de outra organização | O acesso é negado e a tentativa fica registrada conforme a política de auditoria. | RF-02, RN-09, RNF-02 | CT-08 |
| Backup é restaurado em cenário de teste | Dados e anexos do cenário são recuperados sem violar a retenção definida. | RNF-10, RNF-11 | CT-09 |

## 10. Fora do escopo do MVP

- integração automática com APIs de Google Ads e Meta Ads;
- criação automática de anúncios e criativos;
- cálculo automatizado de ROAS, CPA ou previsão de desempenho;
- gestão financeira/faturamento;
- chat em tempo real externo ao histórico do ticket.

## Referências de governança e processo

- BRASIL. *Lei nº 13.709, de 14 de agosto de 2018 — Lei Geral de Proteção de Dados Pessoais (LGPD).* Disponível em: [texto consolidado](https://www.gov.br/mj/pt-br/assuntos/sua-protecao/sedigi/Lei13709.pdf).
- AUTORIDADE NACIONAL DE PROTEÇÃO DE DADOS. *Guia orientativo sobre segurança da informação para agentes de tratamento de pequeno porte.* Disponível em: [guia da ANPD](https://www.gov.br/anpd/pt-br/centrais-de-conteudo/materiais-educativos-e-publicacoes/guia-orientativo-sobre-seguranca-da-informacao-para-agentes-de-tratamento-de-pequeno-porte).
- ISO/IEC/IEEE. *ISO/IEC/IEEE 29148:2018 — Systems and software engineering: Life cycle processes: Requirements engineering*. 2018. Disponível em: [norma](https://www.iso.org/standard/72089.html).
- AXELOS. *ITIL 4 Practice Guide: Service Request Management*. 2020. Disponível em: [prática ITIL](https://www.peoplecert.org/browse-certifications/it-governance-and-service-management/ITIL-1/itil4-practices-service-request-management-3690).
- OBJECT MANAGEMENT GROUP. *Business Process Model and Notation (BPMN), Version 2.0*. 2011. Disponível em: [especificação BPMN](https://www.omg.org/spec/BPMN/2.0/).

## Anexo A — Catálogo inicial de tipos de demanda

| Tipo | Dados obrigatórios adicionais | Aprovação | Evidência esperada | SLA sugerido |
| --- | --- | --- | --- | --- |
| Ajuste de orçamento | Campanha, orçamento atual/proposto, justificativa e data desejada. | Cliente, quando o tipo for configurado como sensível. | Comentário com valor aplicado e captura/link quando disponível. | Alta ou Urgente, conforme impacto. |
| Alteração ou pausa de anúncio | Campanha, canal, anúncio/ativo afetado, ação solicitada e motivo. | Cliente, quando alterar conteúdo ou gerar impacto relevante. | Comentário, captura ou link da alteração. | Alta ou Média. |
| Aprovação de criativo | Campanha, canal, criativo/anexo e data limite. | Cliente obrigatória. | Decisão registrada no ticket. | Média. |
| Relatório de desempenho | Campanha, período, métricas desejadas e formato. | Não obrigatória, salvo solicitação específica. | Arquivo ou link do relatório entregue. | Média. |
| Análise de métricas | Campanha, período, pergunta de negócio e indicadores disponíveis. | Não obrigatória. | Comentário analítico e evidência/relatório de apoio. | Baixa ou Média. |

## Anexo B — Matriz de transições de status

| Status atual | Próximo status permitido | Quem pode alterar | Registro obrigatório | Efeito no SLA |
| --- | --- | --- | --- | --- |
| Aberta | Em triagem, Cancelada | Atendimento, Administrador | Motivo do cancelamento, quando aplicável. | Inicia primeira resposta e resolução. |
| Em triagem | Em execução, Aguardando cliente, Cancelada | Atendimento, Administrador | Prioridade, prazo, responsável e motivo do aguardo/cancelamento. | Continua a contagem. |
| Em execução | Aguardando cliente, Em validação | Responsável, Atendimento, Administrador | Comentário e evidência para validação. | Pausa somente em Aguardando cliente. |
| Aguardando cliente | Em triagem, Em execução, Cancelada | Atendimento, Administrador | Informação recebida ou motivo de cancelamento. | Retoma resolução ao sair do aguardo. |
| Em validação | Concluída, Reaberta | Cliente, Atendimento, Administrador | Aprovação ou justificativa da correção. | Continua até conclusão; reabertura retorna à execução. |
| Concluída | Reaberta | Cliente, Atendimento, Administrador | Justificativa de reabertura. | Novo ciclo de resolução deve ser registrado. |
| Reaberta | Em execução | Atendimento, Administrador | Responsável e prioridade confirmados. | Continua conforme regra configurada. |

## Anexo C — Matriz de rastreabilidade preliminar

| ID | Origem da necessidade | Requisito(s) | Critério de aceite / teste | Prioridade | Responsável | Versão |
| --- | --- | --- | --- | --- | --- |
| RT-01 | Pedidos dispersos e perda de contexto | RF-05, RF-06, RF-17 | Ticket identificado e histórico consultável — CT-01. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-02 | Falta de triagem, prazo e responsável | RF-07, RF-09, RF-21 | Triagem registra responsável, prioridade, prazo e SLA — CT-02. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-03 | Retrabalho e ausência de aprovação | RF-11, RF-12, RN-05, RN-06, RN-11 | Validação, evidência e reabertura com justificativa — CT-05. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-04 | Atrasos e comunicação insuficiente | RF-16, RF-20, RN-12, RN-13 | Vencimento e eventos geram indicação e notificação — CT-06 e CT-07. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-05 | Proteção de dados e continuidade | RF-02, RNF-02, RNF-10, RNF-11 | Acesso por organização e restauração de backup — CT-08 e CT-09. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-06 | Interface compreensível para diferentes perfis | RNF-01, RNF-09 | Teste de tarefas e UEQ-S conforme Etapa 04. | Alta | Grupo TI SIGE 1 | 1.0 |
