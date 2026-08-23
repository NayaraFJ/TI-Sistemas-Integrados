# Levantamento de requisitos e definição de atores

**Aplicação:** SIGE Desk — Help Desk para Gestão de Demandas de Tráfego Pago  
**Situação:** derivação e definição preliminar fundamentadas no referencial teórico, nas normas e no escopo do MVP.

## 1. Objetivo

Este documento organiza a derivação dos requisitos do SIGE Desk. Seu propósito é distinguir: (a) os atores e relações observados nas fontes; (b) os perfis de acesso definidos para o produto; e (c) as decisões de escopo adotadas para o MVP.

## 2. Evidências usadas para delimitar os atores

| Evidência | O que a fonte registra | Uso no projeto | Não permite afirmar |
| --- | --- | --- | --- |
| [Agus et al. (2019)](../../Referencias/Pesquisa%2002/Extracoes/06-cadeia-agencia-publicidade.md) | A cadeia de publicidade digital paga envolve anunciantes, agências de mídia, *publishers* digitais e plataformas de troca de anúncios, com fluxos de informação, recursos financeiros e serviços. | Delimita Cliente/anunciante e Agência como lados da relação atendida pelo sistema; *publishers* e plataformas permanecem no contexto externo. | Que *publishers* ou plataformas devem acessar o SIGE Desk. |
| [Steponaitis e Andrijauskienė (2023)](../../Referencias/Pesquisa%2001/Extracoes/05-competencias-gestao-projetos-agencia.md) | O estudo de caso investigou quatro gestores de projeto em uma agência de vinte pessoas e destacou planejamento, comunicação, monitoramento e controle. | Orienta, como decisão de produto, a inclusão de triagem, responsável, prazo, histórico e acompanhamento. | Que toda agência possua a mesma estrutura interna ou os mesmos cargos, ou que adote um Help Desk. |
| [Emon et al. (2026)](../../Referencias/Pesquisa%2001/Extracoes/01-ai-crm-bangladesh.md) | A pesquisa abrangeu gerentes de marketing, profissionais de marketing digital e especialistas em CRM envolvidos em decisões de relacionamento com o cliente. | Mostra que decisões de marketing e relacionamento podem envolver funções distintas. | Que o MVP deva implementar IA, CRM ou um perfil de sistema para cada uma dessas funções. |
| [ITIL 4 — Service Desk](../../Referencias/Normas/Extracoes/02-itil-4-service-desk.md) e [Service Request Management](../../Referencias/Normas/Extracoes/03-itil-4-service-request-management.md) | Atendimento organizado requer registro, triagem, comunicação, atribuição e acompanhamento de solicitações. | Fundamenta o ponto de contato, a triagem e a rastreabilidade do ticket. | Que os papéis internos de uma agência sejam idênticos aos de um Service Desk de TI. |

## 3. Atores e perfis iniciais do SIGE Desk

Os perfis abaixo são uma decisão de desenho do MVP. Eles convertem a relação Cliente–Agência e as responsabilidades de atendimento em permissões verificáveis; não são apresentados como organograma universal de agências de marketing.

| Tipo | Ator ou perfil | Participação no SIGE Desk | Limite de acesso inicial |
| --- | --- | --- | --- |
| Contexto externo | Anunciante / cliente | Contrata ou acompanha o serviço de publicidade e utiliza o sistema pelo perfil Cliente. | Não acessa tickets de outra organização nem configura regras globais. |
| Usuário do sistema | Cliente | Abre solicitação, complementa informações, acompanha o ticket, aprova a entrega ou solicita correção. | Consulta somente tickets vinculados à própria organização. |
| Usuário do sistema | Atendimento / gestor de conta | Registra demanda em nome do cliente quando necessário, confere informações, classifica, prioriza, atribui, comunica e acompanha prazo. | Não administra usuários, tipos de demanda ou regras globais sem perfil de Administrador. |
| Usuário do sistema | Gestor de tráfego | Executa a demanda atribuída, registra ação, comentário e evidência e encaminha para validação. | Atua somente nas demandas autorizadas ou atribuídas. |
| Usuário do sistema | Administrador | Mantém usuários, clientes, campanhas, tipos de demanda, configurações e regras de SLA. | Não substitui a aprovação do cliente nem a execução registrada pelo responsável. |
| Contexto externo | *Publisher* digital ou plataforma de troca de anúncios | Integra a cadeia de publicidade paga descrita na literatura. | Não possui conta, permissão ou integração automática no MVP. |

## 4. Critérios de análise do referencial

Os tópicos abaixo orientam a leitura das fontes e a derivação dos requisitos. Eles não constituem roteiro de entrevista nem descrevem uma prática empiricamente observada em uma agência específica.

### 4.1 Processo e entrada

1. Como uma solicitação de campanha chega à agência hoje?
2. Quais informações precisam estar disponíveis para iniciar a triagem?
3. Em que situações uma solicitação deve ser devolvida para complemento?
4. Quais tipos de demanda ocorrem com mais frequência?

### 4.2 Responsabilidades e decisão

1. Quem confirma a completude, define prioridade, prazo e responsável?
2. Quem executa cada tipo de demanda e que evidência consegue registrar?
3. Em quais tipos de demanda a validação do cliente é necessária antes da conclusão? Há exceção que exija validação antes da execução?
4. Quem pode cancelar ou reabrir uma demanda e em quais condições?

### 4.3 Acompanhamento e dados

1. Que status, prazo e informações o cliente precisa acompanhar?
2. Quais métricas, links, anexos ou evidências são relevantes para cada tipo de demanda?
3. Quais notificações são necessárias e para quem?
4. Que dados não devem ser registrados no ticket, como credenciais de plataformas ou bases de audiência?

## 5. Registro de decisões de requisitos

Cada decisão deverá registrar a fonte bibliográfica ou normativa, o trecho ou conceito utilizado, a decisão de escopo, o requisito ou regra afetada e sua limitação. Uma alteração nos atores, nas permissões ou no fluxo deve atualizar simultaneamente:

- [Especificação do sistema](Especificacao/Especificacao%20do%20sistema.md);
- [Processo To Be](Processo/BPMN%20-%20Processo%20To%20Be.md);
- [Processo As Is](Processo/BPMN%20-%20Processo%20As%20Is.md), quando a evidência tratar da prática atual;
- [Protótipo de baixa fidelidade](Prototipo/Prototipo%20de%20baixa%20fidelidade.md), se a mudança afetar uma tela ou permissão;
- rastreabilidade e critérios de aceite da especificação.

Os papéis e o fluxo são uma proposta de MVP derivada do referencial; não descrevem uma agência específica nem pretendem representar todas as suas estruturas organizacionais.
