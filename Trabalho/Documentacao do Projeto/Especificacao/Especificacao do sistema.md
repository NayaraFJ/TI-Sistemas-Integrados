# Help Desk para Gestão de Demandas de Tráfego Pago

## Documentação do projeto — Especificação preliminar do sistema

> Este documento reúne requisitos iniciais derivados do problema, objetivos e referencial teórico. Eles devem ser validados com usuários e podem ser refinados antes da implementação.

> A estrutura de requisitos e rastreabilidade adota como referência a engenharia de requisitos da ISO/IEC/IEEE 29148; o fluxo de solicitações considera a prática ITIL 4 de gerenciamento de solicitações; e os controles de acesso e minimização de dados consideram a Lei Geral de Proteção de Dados Pessoais (LGPD) e as orientações da Autoridade Nacional de Proteção de Dados (ANPD). A retenção operacional é tema para eventual adoção em produção, não para este protótipo acadêmico. A representação do processo correspondente está documentada em Business Process Model and Notation (BPMN) 2.0 no arquivo [BPMN - Processo To Be.md](../Processo/BPMN%20-%20Processo%20To%20Be.md) (ISO/IEC/IEEE, 2018; PEOPLECERT, 2023; BRASIL, 2018; AUTORIDADE NACIONAL DE PROTEÇÃO DE DADOS, 2021; OMG, 2011).

## 1. Visão do produto

O SIGE Desk será uma aplicação web para organizar demandas de tráfego pago entre clientes e equipe de uma agência de marketing digital. O sistema centralizará solicitações, responsáveis, prazos, status, comentários, aprovações e evidências de execução. A delimitação dos atores, as evidências bibliográficas e as decisões de escopo estão registradas em [Levantamento de requisitos.md](../Levantamento%20de%20requisitos.md).

## 2. Perfis de usuário

Os perfis são uma decisão de desenho do MVP e não a afirmação de que toda agência utiliza os mesmos cargos. Eles transformam as responsabilidades identificadas no referencial em permissões do sistema e poderão ser refinados somente se o escopo do projeto for alterado.

| Perfil | Permissões principais |
| --- | --- |
| Cliente | Abrir e consultar as próprias demandas; enviar comentários/anexos; aprovar ou solicitar complementação. |
| Atendimento / gestor de conta | Registrar demandas para clientes; fazer triagem; definir prioridade/prazo; acompanhar e comunicar-se. |
| Gestor de tráfego | Consultar demandas atribuídas; atualizar status; comentar; registrar execução e evidências. |
| Administrador | Gerenciar usuários, clientes, campanhas, tipos de demanda, prioridades e relatórios. |

## 3. Requisitos funcionais

As fontes da última coluna orientaram o levantamento e a priorização dos requisitos; elas não constituem prescrição literal de cada funcionalidade.

| ID | Requisito | Prioridade | Fonte(s) de embasamento |
| --- | --- | --- | --- |
| RF-01 | O sistema deve permitir autenticação de usuários. | Alta | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md); [LGPD](../../../Referencias/Normas/Extracoes/05-lgpd-lei-13709.md) |
| RF-02 | O sistema deve controlar permissões conforme o perfil de usuário. | Alta | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md); [LGPD](../../../Referencias/Normas/Extracoes/05-lgpd-lei-13709.md) |
| RF-03 | O administrador deve poder cadastrar, editar, ativar e inativar clientes; clientes inativos não devem ficar disponíveis para novas solicitações. | Alta | [CRM e intenção de recompra](../../../Referencias/Pesquisa%2001/Extracoes/03-crm-intencao-recompra.md); [gestão de projetos em agência](../../../Referencias/Pesquisa%2001/Extracoes/05-competencias-gestao-projetos-agencia.md) |
| RF-04 | O administrador ou atendimento deve poder cadastrar campanhas vinculadas a um cliente. | Alta | [DIGI-AI](../../../Referencias/Pesquisa%2001/Extracoes/02-digi-ai-plataforma.md); [cadeia de publicidade digital](../../../Referencias/Pesquisa%2002/Extracoes/06-cadeia-agencia-publicidade.md) |
| RF-05 | O sistema deve permitir abrir ticket com cliente, campanha, canal, tipo, urgência informada, prazo desejado, assunto e descrição. A prioridade oficial e os prazos de acordo de nível de serviço (SLA) serão definidos na triagem. | Alta | [ITIL — Service Request Management](../../../Referencias/Normas/Extracoes/03-itil-4-service-request-management.md); [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md) |
| RF-06 | O sistema deve gerar identificador único para cada ticket. | Alta | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md); [ISO/IEC/IEEE 29148](../../../Referencias/Normas/Extracoes/01-iso-iec-ieee-29148-requisitos.md) |
| RF-07 | O atendimento deve poder classificar e atribuir um ticket a um responsável. | Alta | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md); [gestão de projetos em agência](../../../Referencias/Pesquisa%2001/Extracoes/05-competencias-gestao-projetos-agencia.md) |
| RF-08 | O responsável deve poder atualizar o status do ticket. | Alta | [ITIL — Service Request Management](../../../Referencias/Normas/Extracoes/03-itil-4-service-request-management.md) |
| RF-09 | O sistema deve registrar data, hora, usuário, motivo, campo alterado, valor anterior e novo valor em cada alteração de status, responsável, prioridade, prazo ou aprovação. | Alta | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md); [LGPD](../../../Referencias/Normas/Extracoes/05-lgpd-lei-13709.md) |
| RF-10 | Usuários autorizados devem poder incluir comentários e anexos no ticket. | Alta | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md) |
| RF-11 | O sistema deve permitir registrar aprovação, reprovação ou solicitação de complemento pelo cliente. | Média | [ITIL — Service Request Management](../../../Referencias/Normas/Extracoes/03-itil-4-service-request-management.md); [gestão de projetos em agência](../../../Referencias/Pesquisa%2001/Extracoes/05-competencias-gestao-projetos-agencia.md) |
| RF-12 | O responsável deve poder registrar a ação executada e evidências no encerramento. | Alta | [ITIL — Service Request Management](../../../Referencias/Normas/Extracoes/03-itil-4-service-request-management.md) |
| RF-13 | O sistema deve permitir cancelar ticket com motivo obrigatório. | Média | [ITIL — Service Request Management](../../../Referencias/Normas/Extracoes/03-itil-4-service-request-management.md) |
| RF-14 | O usuário deve poder filtrar tickets por cliente, campanha, tipo, prioridade, responsável, status e período. | Alta | [classificação de tráfego](../../../Referencias/Pesquisa%2002/Extracoes/13-classificacao-trafego.md); [DIGI-AI](../../../Referencias/Pesquisa%2001/Extracoes/02-digi-ai-plataforma.md) |
| RF-15 | O painel deve apresentar total de tickets por status, prioridade, responsável e prazo. | Média | [classificação de tráfego](../../../Referencias/Pesquisa%2002/Extracoes/13-classificacao-trafego.md); [gestão de projetos em agência](../../../Referencias/Pesquisa%2001/Extracoes/05-competencias-gestao-projetos-agencia.md) |
| RF-16 | O sistema deve identificar tickets vencidos e tickets aguardando resposta do cliente. | Média | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md); [ITIL — Service Request Management](../../../Referencias/Normas/Extracoes/03-itil-4-service-request-management.md) |
| RF-17 | O sistema deve permitir consultar histórico completo de cada ticket. | Alta | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md); [LGPD](../../../Referencias/Normas/Extracoes/05-lgpd-lei-13709.md) |
| RF-18 | O sistema deve permitir registrar métricas de contexto, como impressões, taxa de cliques (CTR), custo por clique (CPC), conversão, custo por aquisição (CPA) e retorno sobre o investimento em anúncios (ROAS), quando aplicável. | Baixa | [otimização de orçamento](../../../Referencias/Pesquisa%2002/Extracoes/02-otimizacao-orcamento.md); [busca por voz e anúncios pagos](../../../Referencias/Pesquisa%2002/Extracoes/09-voz-anuncios-pagos.md) |
| RF-19 | O sistema deve permitir exportar uma lista de tickets filtrada para apoio a relatórios. | Baixa | [classificação de tráfego](../../../Referencias/Pesquisa%2002/Extracoes/13-classificacao-trafego.md); [gestão de projetos em agência](../../../Referencias/Pesquisa%2001/Extracoes/05-competencias-gestao-projetos-agencia.md) |
| RF-20 | O sistema deve enviar notificação no sistema e, quando configurado, por e-mail aos envolvidos autorizados conforme o evento — solicitante, responsável, Atendimento/gestor de conta e Administrador — quando o ticket for atribuído, comentado, estiver aguardando cliente, entrar em validação, vencer ou for concluído/reaberto. | Alta | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md); [ITIL — Service Request Management](../../../Referencias/Normas/Extracoes/03-itil-4-service-request-management.md) |
| RF-21 | O sistema deve exibir os prazos de primeira resposta e resolução do SLA, incluindo situação de vencimento ou pausa. | Alta | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md) |
| RF-22 | O administrador deve poder cadastrar, editar, ativar e inativar tipos de demanda, definindo campos obrigatórios, necessidade de aprovação do cliente e necessidade de evidência para cada tipo. | Alta | [ITIL — Service Request Management](../../../Referencias/Normas/Extracoes/03-itil-4-service-request-management.md); [ISO/IEC/IEEE 29148](../../../Referencias/Normas/Extracoes/01-iso-iec-ieee-29148-requisitos.md) |
| RF-23 | O administrador deve poder configurar o calendário de SLA, incluindo horário de atendimento, fuso horário, feriados e recessos, e definir prazos de primeira resposta e resolução por prioridade, com regra opcional por cliente ou tipo de demanda. | Alta | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md) |

## 4. Requisitos não funcionais

| ID     | Requisito       | Critério inicial | Fonte(s) de embasamento |
| ------ | --------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- | --- |
| RNF-02 | Segurança       | Senhas devem ser armazenadas de forma protegida; autenticação, autorização por perfil e auditoria devem impedir e registrar acessos indevidos. | [LGPD](../../../Referencias/Normas/Extracoes/05-lgpd-lei-13709.md); [ISO/IEC/IEEE 29148](../../../Referencias/Normas/Extracoes/01-iso-iec-ieee-29148-requisitos.md) |
| RNF-03 | Privacidade     | O sistema deve coletar somente dados necessários para a gestão da demanda e não deve armazenar bases de audiência ou dados sensíveis. | [LGPD](../../../Referencias/Normas/Extracoes/05-lgpd-lei-13709.md); [privacidade e publicidade direcionada](../../../Referencias/Pesquisa%2002/Extracoes/08-privacidade-publicidade-direcionada.md) |
| RNF-04 | Rastreabilidade | Alterações de status, responsável, prazo e aprovação devem permanecer no histórico. | [ITIL — Service Desk](../../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md); [LGPD](../../../Referencias/Normas/Extracoes/05-lgpd-lei-13709.md) |
| RNF-05 | Integridade     | Um ticket concluído ou cancelado não poderá ser apagado sem manter registro administrativo. | [ISO/IEC/IEEE 29148](../../../Referencias/Normas/Extracoes/01-iso-iec-ieee-29148-requisitos.md); [LGPD](../../../Referencias/Normas/Extracoes/05-lgpd-lei-13709.md) |
| RNF-07 | Compatibilidade | A interface deve funcionar em navegadores modernos. | [ISO/IEC/IEEE 29148](../../../Referencias/Normas/Extracoes/01-iso-iec-ieee-29148-requisitos.md) |
| RNF-09 | Acessibilidade  | Campos devem possuir rótulos claros, contraste adequado e navegação possível por teclado. | [usabilidade e ISO 9241-11](../../../Referencias/Normas/Extracoes/06-usabilidade-assinatura-digital-iso-9241-11.md) |

> **Conformidade fora do escopo do protótipo acadêmico:** como o projeto utiliza dados fictícios ou autorizados apenas para demonstração e não será disponibilizado em produção, não são requisitos da entrega acadêmica a política operacional de retenção, o backup, a restauração e a disponibilidade contínua. Em uma eventual adoção real, a agência deverá definir esses controles e a base legal aplicável, em conformidade com a LGPD.

## 5. Acordos de nível de serviço (SLA) preliminares

Os SLAs serão configuráveis pela agência e representam uma proposta inicial para o produto mínimo viável (MVP). Na ausência de configuração específica de cliente ou tipo de demanda, o calendário padrão será de segunda a sexta-feira, das 08h às 18h, no fuso horário `America/Sao_Paulo`, excluídos os feriados nacionais e os feriados ou recessos cadastrados pela agência. Somente os intervalos inseridos nesse calendário serão contabilizados como horas úteis.

O prazo de primeira resposta vai da abertura ao primeiro retorno efetivo registrado ao solicitante; o prazo de resolução vai da abertura à conclusão. A confirmação automática de recebimento não encerra a primeira resposta. Ao entrar em **Aguardando cliente**, o relógio de resolução deve ser pausado, preservando-se o tempo útil restante, e deve voltar a contar ao sair desse status. O status **Em validação** não pausa o SLA, salvo regra específica configurada pela agência. Quando um ticket concluído for reaberto, o sistema deve preservar os prazos e resultados do ciclo anterior, iniciar um novo ciclo de resolução a partir da reabertura conforme a prioridade vigente e não reiniciar o prazo de primeira resposta já cumprido.

| Prioridade | Primeira resposta | Resolução prevista |
| --- | ---: | ---: |
| Urgente | 2 horas úteis | 8 horas úteis |
| Alta | 4 horas úteis | 16 horas úteis |
| Média | 8 horas úteis | 24 horas úteis |
| Baixa | 16 horas úteis | 40 horas úteis |

## 6. Regras de negócio

| ID    | Regra                                                                                                                                                                                                                                                |
| ----- | ---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| RN-01 | Na abertura, todo ticket deve possuir cliente, tipo de demanda, urgência informada, assunto, descrição e solicitante. A prioridade oficial torna-se obrigatória após a triagem.                                                                      |
| RN-02 | Quando a demanda estiver vinculada a campanha existente, cliente e campanha devem ser compatíveis.                                                                                                                                                   |
| RN-03 | Somente atendimento ou administrador pode definir prioridade, prazo e responsável na triagem.                                                                                                                                                        |
| RN-04 | Somente responsável, atendimento ou administrador pode alterar o status de um ticket, exceto o Cliente, que pode aprovar ou solicitar correção de ticket em “Em validação”, resultando exclusivamente nas transições para “Concluída” ou “Reaberta”. |
| RN-05 | Após a execução, o ticket deve seguir para “Em validação”, com descrição da ação executada e evidência quando configurada para o tipo de demanda.                                                                                                    |
| RN-06 | O status “Concluída” exige validação/aprovação necessária ou registro de que o tipo de demanda não requer validação do cliente.                                                                                                                      |
| RN-07 | O status “Cancelada” exige motivo de cancelamento.                                                                                                                                                                                                   |
| RN-08 | O status “Aguardando cliente” deve registrar qual informação complementar é necessária.                                                                                                                                                               |
| RN-09 | O cliente deve visualizar somente tickets vinculados à sua organização.                                                                                                                                                                              |
| RN-10 | Tipos de demanda configurados como sensíveis podem exigir validação do cliente antes da conclusão, conforme sua regra de aprovação.                                                                                                                |
| RN-11 | Cliente, atendimento ou administrador pode reabrir um ticket concluído mediante justificativa; a reabertura preserva todo o histórico anterior.                                                                                                      |
| RN-12 | Ao vencer o SLA, o sistema deve sinalizar o ticket e notificar o responsável e o atendimento.                                                                                                                                                        |
| RN-13 | Atribuição, comentário, mudança para “Aguardando cliente”, “Em validação”, “Concluída” e “Reaberta” devem gerar notificações aos envolvidos.                                                                                                         |
| RN-14 | A confirmação automática de recebimento não conta como primeira resposta do SLA. A primeira resposta exige retorno efetivo de Atendimento, Administrador ou responsável ao solicitante, registrado no ticket.                                        |

## 7. Governança de dados, acesso e continuidade

- O acesso será baseado no princípio do menor privilégio: cada perfil verá e alterará apenas as informações necessárias às suas responsabilidades.
- Toda alteração relevante será auditável por usuário, data/hora, valor anterior e novo valor.
- Anexos deverão ser vinculados ao ticket e acessíveis somente a usuários autorizados.
- O MVP utilizará dados fictícios ou autorizados para teste; credenciais de plataformas de anúncios e bases de audiência não serão armazenadas.
- A finalidade declarada para os dados do MVP é registrar, comunicar, executar, validar e auditar demandas de tráfego pago. Antes da produção, a agência deverá identificar controlador, operadores e base legal aplicável, além de publicar aviso de privacidade compatível.
- O sistema deverá manter canal de contato para solicitações de titulares relacionadas a acesso, correção ou eliminação de dados, encaminhando-as ao responsável definido pela agência.
- A política de retenção, backup, restauração e resposta a incidentes será definida somente em caso de adoção real, pois não integra o protótipo acadêmico com dados fictícios ou autorizados.

## 8. Dados principais

| Entidade | Dados iniciais |
| --- | --- |
| Usuário | Identificador, nome, e-mail, perfil, status e cliente vinculado quando o perfil for Cliente. |
| Cliente | Identificador, nome, contato e status. |
| Campanha | Identificador, cliente, nome, canal, objetivo e status. |
| Tipo de demanda | Identificador, nome, status, campos obrigatórios, necessidade de aprovação do cliente e necessidade de evidência. |
| Configuração de SLA | Calendário, horário de atendimento, fuso horário, feriados e recessos, prioridade, prazos de primeira resposta e resolução e escopo opcional por cliente ou tipo de demanda. |
| Ticket | Número, cliente, campanha, tipo, canal, urgência informada, prazo desejado, prioridade oficial, prazos de SLA, assunto, descrição, métricas de contexto quando aplicável, solicitante, responsável e status. |
| Comentário | Ticket, autor, data/hora, texto e referência a anexos vinculados. |
| Histórico | Ticket, campo alterado, valor anterior, novo valor, usuário e data/hora. |
| Aprovação | Ticket, decisão, usuário, data/hora e observação. |
| Evidência | Ticket, descrição, link ou arquivo, autor e data/hora. |
| Anexo | Identificador, ticket, nome original, tipo, tamanho, referência de armazenamento, autor e data/hora. |
| Notificação | Ticket, evento gerador, destinatário, canal, resumo, data/hora de geração, situação de envio e data de leitura, quando aplicável. |

## 9. Critérios de aceite iniciais

| Cenário | Resultado esperado | Requisito(s) relacionado(s) | Caso(s) de teste |
| --- | --- | --- | --- |
| Cliente abre uma demanda | O ticket recebe número, status “Aberta”, urgência informada e prazo desejado; fica visível para atendimento. | RF-05, RF-06 | CT-01 |
| Usuário acessa o sistema | Usuário autenticado acessa apenas as funções e os tickets permitidos ao seu perfil. | RF-01, RF-02 | CT-11 |
| Administrador mantém clientes e campanhas | Cliente pode ser ativado/inativado e campanha cadastrada fica vinculada ao cliente selecionado. | RF-03, RF-04 | CT-12 |
| Atendimento faz a triagem | Prioridade oficial, prazos de SLA e responsável ficam registrados no histórico. | RF-07, RF-09, RN-03, RN-14 | CT-02 |
| Gestor de tráfego executa uma alteração | O ticket recebe comentário/evidência e pode ser encaminhado para validação. | RF-08, RF-10, RF-12, RN-05 | CT-03, CT-05 |
| Informação adicional é necessária | O ticket fica “Aguardando cliente” até o complemento ser registrado. | RF-11, RN-08 | CT-04 |
| Entrega precisa ser confirmada | O ticket segue para “Em validação”; após aprovação, torna-se “Concluída”. | RF-11, RN-05, RN-06 | CT-05 |
| Cliente solicita correção | O ticket é reaberto com justificativa e retorna à fila de execução. | RF-11, RN-11 | CT-05 |
| Usuário filtra demandas vencidas | A listagem retorna os tickets vencidos de acordo com os filtros e as permissões do perfil. | RF-14, RF-16, RF-21, RN-12 | CT-06 |
| Atendimento cancela ticket antes da execução | O motivo do cancelamento é registrado e o ticket não segue para execução. | RF-13, RN-07 | CT-14 |
| Usuário consulta o painel de demandas | O painel apresenta totais por status, prioridade, responsável e prazo, conforme o perfil autorizado. | RF-15 | CT-15 |
| Solicitante informa métricas de contexto | Quando aplicável, métricas como impressões, CTR, CPC, conversão, CPA ou ROAS ficam registradas no ticket. | RF-18 | CT-16 |
| Usuário exporta uma listagem filtrada | A exportação contém apenas os tickets retornados pelos filtros e acessíveis ao perfil do usuário. | RF-19 | CT-17 |
| Ticket é atribuído, comentado, entra em aguardo ou validação, vence, é concluído ou reaberto | Os envolvidos recebem notificação conforme seu perfil e configuração. | RF-20, RN-13 | CT-07 |
| Administrador configura um tipo de demanda | O tipo registra campos obrigatórios e regras de aprovação e evidência aplicáveis aos tickets desse tipo. | RF-22, RN-05, RN-10 | CT-09 |
| Administrador configura o SLA | Calendário, prazos e regra por prioridade, cliente ou tipo de demanda são aplicados ao cálculo do ticket correspondente. | RF-23 | CT-10 |
| Usuário consulta histórico completo | A consulta exibe todas as alterações auditáveis do ticket, com autor, data/hora, motivo, campo, valor anterior e novo valor. | RF-09, RF-17, RNF-04 | CT-13 |
| Cliente tenta acessar ticket de outra organização | O acesso é negado e a tentativa fica registrada conforme a política de auditoria. | RF-02, RN-09, RNF-02 | CT-08 |

## 10. Fora do escopo do MVP

- integração automática com APIs de Google Ads e Meta Ads;
- criação automática de anúncios e criativos;
- cálculo automatizado de ROAS, CPA ou previsão de desempenho;
- gestão financeira/faturamento;
- chat em tempo real externo ao histórico do ticket.

## Referências de governança e processo

- BRASIL. *Lei nº 13.709, de 14 de agosto de 2018 — Lei Geral de Proteção de Dados Pessoais (LGPD).* Brasília, DF: Presidência da República, 2018. Disponível em: [texto consolidado](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709compilado.htm). Acesso em: 16 ago. 2026.
- AUTORIDADE NACIONAL DE PROTEÇÃO DE DADOS. *Guia orientativo sobre segurança da informação para agentes de tratamento de pequeno porte.* Brasília, DF, 2021. Disponível em: [guia da ANPD](https://www.gov.br/anpd/pt-br/documentos-e-publicacoes/guia-vf.pdf). Acesso em: 16 ago. 2026.
- ISO/IEC/IEEE. *ISO/IEC/IEEE 29148:2018 — Systems and software engineering: Life cycle processes: Requirements engineering*. 2. ed. Geneva, 2018. Disponível em: [norma](https://www.iso.org/standard/72089.html). Acesso em: 16 ago. 2026.
- PEOPLECERT. *ITIL 4 Practice Guide: Service Request Management*. London, 2023. Disponível em: [prática ITIL](https://www.peoplecert.org/browse-certifications/it-governance-and-service-management/ITIL-1/itil4-practices-service-request-management-3690). Acesso em: 16 ago. 2026.
- OBJECT MANAGEMENT GROUP. *Business Process Model and Notation (BPMN), Version 2.0*. Needham, 2011. Disponível em: [especificação BPMN](https://www.omg.org/spec/BPMN/2.0/). Acesso em: 16 ago. 2026.

## Anexo A — Catálogo inicial de tipos de demanda

| Tipo | Dados obrigatórios adicionais | Aprovação | Evidência esperada | SLA sugerido |
| --- | --- | --- | --- | --- |
| Ajuste de orçamento | Campanha, orçamento atual/proposto, justificativa e data desejada. | Cliente, quando o tipo for configurado como sensível. | Comentário com valor aplicado e captura/link quando disponível. | Alta ou Urgente, conforme impacto. |
| Criação, alteração ou pausa de anúncio | Campanha, canal, anúncio/ativo afetado quando existente, ação solicitada e motivo. | Cliente, quando alterar conteúdo ou gerar impacto relevante. | Comentário, captura ou link da criação, alteração ou pausa. | Alta ou Média. |
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
| RT-01 | Pedidos dispersos e perda de contexto | RF-05, RF-06, RF-17 | Ticket identificado e histórico consultável — CT-01 e CT-13. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-02 | Falta de triagem, prazo e responsável | RF-07, RF-09, RF-21, RF-23, RN-14 | Triagem registra responsável, prioridade, prazo e SLA; confirmação automática não encerra primeira resposta — CT-02 e CT-10. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-03 | Retrabalho e ausência de aprovação | RF-11, RF-12, RF-22, RN-05, RN-06, RN-11 | Validação, evidência, configuração do tipo e reabertura com justificativa — CT-05 e CT-09. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-04 | Atrasos e comunicação insuficiente | RF-16, RF-20, RN-12, RN-13 | Vencimento e eventos geram indicação e notificação — CT-06 e CT-07. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-05 | Proteção de dados | RF-02, RNF-02 | Acesso por organização — CT-08. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-06 | Interface compreensível para diferentes perfis | RNF-09 | Campos claros, contraste e navegação por teclado; avaliação de usabilidade conforme [Etapa 04 - Metodologia.md](../../Artigo/Etapa%2004%20-%20Metodologia.md). | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-07 | Separação entre atores externos da cadeia de publicidade e perfis de acesso do MVP, conforme [levantamento de requisitos](../Levantamento%20de%20requisitos.md) | RF-01, RF-02, RF-07, RF-08, RN-03, RN-04, RN-09 | Perfis autorizados executam somente as ações previstas; cliente não acessa tickets de outra organização — CT-02, CT-03, CT-08 e CT-11. | Alta | Grupo TI SIGE 1 | 1.0 |
| RT-08 | Acompanhamento do ciclo de vida e apoio à consulta gerencial, derivados do referencial e do escopo do MVP | RF-13, RF-14, RF-15, RF-18, RF-19 | Cancelamento justificado, filtro de vencidos, painel, métricas de contexto e exportação — CT-06 e CT-14 a CT-17. | Média | Grupo TI SIGE 1 | 1.0 |

## Anexo D — Matriz de notificações

| Evento | Destinatários | Canal | Conteúdo mínimo |
| --- | --- | --- | --- |
| Ticket atribuído | Responsável e Atendimento/gestor de conta | No sistema; e-mail se configurado | Número, assunto, prioridade oficial e prazo de resolução. |
| Comentário incluído | Solicitante, responsável e usuários mencionados com permissão | No sistema; e-mail se configurado | Número, autor e resumo do comentário. |
| Aguardando cliente | Solicitante e Atendimento/gestor de conta | No sistema; e-mail se configurado | Informação complementar necessária e prazo aplicável. |
| Em validação | Solicitante e Atendimento/gestor de conta | No sistema; e-mail se configurado | Ação executada, evidência e ação esperada do cliente. |
| SLA vencido | Responsável, Atendimento/gestor de conta e Administrador | No sistema; e-mail se configurado | Número, prioridade, prazo vencido e tempo em atraso. |
| Concluída ou Reaberta | Solicitante, responsável e Atendimento/gestor de conta | No sistema; e-mail se configurado | Novo status, justificativa e próximo passo, quando houver. |

> Notificações não devem revelar conteúdo ou anexos a usuários sem permissão para consultar o ticket.
