# Trabalho Interdisciplinar — Sistemas Integrados de Gestão Empresarial

## Etapa 04 — Metodologia

**Tema:** Gestão de Demandas de Marketing Digital  
**Título provisório:** *Help Desk para Gestão de Demandas de Tráfego Pago*

## 1. Caracterização da pesquisa

Este trabalho será desenvolvido como uma pesquisa **aplicada**, pois busca propor e construir uma solução para um problema prático de gestão de demandas em agências de marketing digital. Quanto aos objetivos, a pesquisa terá caráter **exploratório e descritivo**: exploratório por investigar como ocorre a organização de solicitações de tráfego pago e descritivo por registrar necessidades, papéis, fluxos, requisitos e resultados de validação do protótipo.

A abordagem será predominantemente **qualitativa**, voltada a compreender o processo atual, as dificuldades dos usuários e a adequação do sistema. Dados quantitativos simples também poderão ser utilizados na validação, por exemplo quantidade de tarefas concluídas, tempo de execução de um cenário e respostas a questionário de experiência do usuário.

## 2. Procedimentos metodológicos

O desenvolvimento será organizado nas etapas a seguir.

### 2.1 Revisão bibliográfica

Será realizada revisão de trabalhos selecionados na IEEE Xplore sobre gestão de projetos em agências, automação de marketing, CRM, campanhas digitais, métricas de tráfego pago, experiência do usuário e sistemas de gestão de marketing. A revisão será complementada por fontes normativas e oficiais pertinentes ao projeto: ITIL 4 para gestão de serviços e solicitações, ISO/IEC/IEEE 29148 para requisitos, BPMN 2.0 para modelagem de processo e LGPD/ANPD para proteção de dados (PEOPLECERT, 2023; AXELOS, 2020; ISO/IEC/IEEE, 2018; OMG, 2011; BRASIL, 2018; AUTORIDADE NACIONAL DE PROTEÇÃO DE DADOS, 2024). As buscas em artigos já iniciaram com títulos relacionados a *digital marketing*, *marketing automation*, *project management*, *customer relationship management*, *campaign* e *advertising*.

Os artigos serão usados para fundamentar conceitos, identificar práticas recorrentes e apoiar escolhas de requisitos. A análise priorizará trabalhos mais aderentes ao problema, especialmente aqueles sobre gestão de projetos em agência, plataformas de marketing, gestão de campanhas, orçamento e métricas de desempenho.

### 2.2 Levantamento e validação de requisitos

Os requisitos preliminares serão derivados do problema de pesquisa, do referencial teórico e do esboço do protótipo. Em seguida, deverão ser validados com potenciais usuários: clientes de agências, profissionais de atendimento/gestão de conta e gestores de tráfego.

O instrumento de levantamento poderá conter perguntas semiestruturadas, como:

- por quais canais as demandas são recebidas atualmente;
- quais informações costumam faltar em uma solicitação;
- quais tipos de demanda são mais recorrentes;
- como são definidos prioridade, prazo e responsável;
- quais situações causam retrabalho ou atraso;
- quais informações o cliente precisa acompanhar;
- quais indicadores seriam úteis para a gestão.

As respostas serão organizadas por categorias, como entrada da demanda, priorização, execução, comunicação, aprovação, encerramento e indicadores. Cada requisito será classificado como alta, média ou baixa prioridade para a primeira versão do aplicativo. Para manter rastreabilidade, cada requisito deverá registrar origem, versão, responsável, critério de aceite e caso de teste relacionado (ISO/IEC/IEEE, 2018).

### 2.3 Modelagem do processo e do protótipo

Com base nos requisitos validados, será modelado o fluxo atual (**As Is**) e o fluxo de vida desejado (**To Be**) do ticket: abertura, triagem, atribuição, execução, espera de cliente, validação, conclusão, reabertura e cancelamento. A representação do processo futuro adotará BPMN 2.0, distinguindo Cliente e Agência como participantes e Atendimento/Gestor de conta e Gestor de tráfego como responsabilidades internas (OMG, 2011). Também serão refinados os perfis de usuário, regras de negócio, dados necessários, SLAs e telas principais.

O protótipo de interface será elaborado de forma incremental. Inicialmente, serão produzidas telas de painel, listagem de tickets, abertura de solicitação e detalhes do ticket. O protótipo deverá evidenciar os principais caminhos do usuário e permitir ajustes antes da implementação da aplicação.

### 2.4 Desenvolvimento da aplicação

A versão inicial será desenvolvida como aplicação web. A implementação seguirá ciclos curtos: selecionar requisitos priorizados, implementar, testar o cenário correspondente e registrar eventuais ajustes. O foco inicial será garantir cadastro, autenticação, tickets, comentários, histórico, status, prazos, responsáveis, notificações, filtros e controles de acesso.

Integrações automáticas com plataformas de anúncios, previsão por inteligência artificial e automação de alterações em campanhas não serão implementadas no MVP. Essa delimitação permitirá avaliar primeiro o valor do fluxo de atendimento e rastreabilidade.

### 2.5 Testes funcionais

Serão elaborados casos de teste a partir dos critérios de aceite dos requisitos. Cada caso deverá informar pré-condição, passos de execução, resultado esperado, resultado obtido e situação final.

Os cenários mínimos previstos são:

| ID | Requisito(s) relacionado(s) | Prioridade | Cenário de teste | Resultado esperado |
| --- | --- | --- | --- | --- |
| CT-01 | RF-05, RF-06 | Alta | Cliente abre ticket de alteração de campanha. | Ticket recebe identificador, status inicial, urgência informada e prazo desejado. |
| CT-02 | RF-07, RF-09, RN-03, RN-14 | Alta | Atendimento classifica e atribui o ticket. | Prioridade oficial, prazos de SLA e responsável ficam registrados; confirmação automática não conta como primeira resposta. |
| CT-03 | RF-08, RF-09, RF-10 | Alta | Gestor atualiza status e inclui comentário. | Histórico registra usuário, data/hora e alteração. |
| CT-04 | RF-11, RN-08 | Média | Ticket aguarda aprovação do cliente. | O status e a solicitação de aprovação ficam visíveis. |
| CT-05 | RF-11, RF-12, RN-05, RN-06, RN-11 | Alta | Responsável encaminha a entrega para validação e o cliente solicita correção. | O ticket fica “Em validação”, é reaberto com justificativa e preserva o histórico. |
| CT-06 | RF-14, RF-16, RF-21, RN-12 | Média | Usuário filtra demandas vencidas. | A listagem retorna apenas tickets com prazo ultrapassado. |
| CT-07 | RF-20, RN-13 | Alta | Ticket é atribuído, comentado, colocado em aguardo, vencido ou concluído. | Os envolvidos recebem a notificação configurada para o evento. |
| CT-08 | RF-02, RN-09, RNF-02 | Alta | Cliente tenta consultar ticket de outra organização. | O acesso é negado e a tentativa é registrada conforme a política de auditoria. |
| CT-09 | RNF-10, RNF-11 | Alta | Administrador executa restauração em ambiente de teste. | Dados e anexos do cenário de teste são recuperados sem violar a retenção definida. |
| CT-10 | RNF-06 | Média | Usuário abre e lista tickets em base de teste com pelo menos 100 tickets. | Pelo menos 90% das operações concluem em até 2 segundos, sem considerar limitações externas de rede. |

O critério de aprovação funcional será: 100% dos casos de teste de prioridade alta aprovados, pelo menos 90% do total de casos aprovados e nenhum defeito crítico em aberto. Casos reprovados deverão gerar registro de correção ou justificativa de adiamento.

### 2.6 Avaliação de usabilidade

Após a versão inicial, o protótipo ou aplicação será submetido a uma avaliação de usabilidade com **seis participantes**, selecionados por conveniência: dois potenciais clientes, dois profissionais de atendimento/gestão de conta e dois gestores de tráfego. Caso algum perfil não esteja disponível, a substituição e sua limitação serão registradas no relatório.

Cada participante executará cinco tarefas: (1) abrir uma demanda com os dados obrigatórios; (2) localizar e filtrar um ticket; (3) classificar, priorizar e atribuir a demanda; (4) incluir comentário e encaminhar a entrega para validação; e (5) aprovar ou reabrir a demanda com justificativa. Serão observados obstáculos, dúvidas, erros, necessidade de ajuda e tempo de realização.

O teste será considerado aprovado se pelo menos 80% das execuções de tarefas forem concluídas sem ajuda e nenhuma tarefa tiver taxa de conclusão inferior a 70%. Ao final, será aplicado o **Short User Experience Questionnaire (UEQ-S)**. Suas oito respostas, coletadas em escala de 1 a 7, serão convertidas para a escala de -3 a +3; serão calculadas as médias das dimensões pragmática e hedônica e a média geral. Como critério interno do projeto, será buscada média geral superior a 0,8 e nenhuma dimensão abaixo de 0,5. Esses valores são metas de aceitação do projeto, não substituem a interpretação comparativa do instrumento. O registro conjunto de conclusão, tempo, erros e comentários segue a abordagem de avaliação de usabilidade discutida por Radhitya et al. (2024).

Os resultados serão apresentados de forma agregada, sem identificação individual. Sugestões e dificuldades recorrentes serão transformadas em lista de melhorias para a versão final.

## 3. Participantes e dados

Os seis participantes serão convidados por conveniência entre pessoas que possuam experiência com atendimento, gestão de campanhas ou contratação de serviços de marketing digital. A participação será voluntária e ocorrerá apenas após explicação do objetivo do teste e concordância com o registro agregado dos resultados. Os dados de pesquisa serão limitados ao perfil do participante, resultados das tarefas e respostas ao questionário; não serão usados tickets reais nem dados de clientes sem autorização.

O projeto coletará somente dados necessários para a pesquisa e para o funcionamento do protótipo, como perfil de usuário, respostas ao questionário e registros fictícios de tickets. Não serão solicitadas credenciais de plataformas de anúncio, dados bancários, bases de audiência ou informações pessoais sensíveis.

## 4. Análise dos dados

Os dados qualitativos das entrevistas, observações e comentários serão analisados por categorização temática. As categorias iniciais serão: problemas do processo atual, informações necessárias no ticket, comunicação, prioridade, prazo, aprovação, evidências e indicadores.

Os dados quantitativos de teste serão analisados de forma descritiva, considerando:

- quantidade de casos de teste aprovados e reprovados;
- percentual de operações de abertura e listagem concluídas em até 2 segundos;
- quantidade de tarefas concluídas por participante;
- taxa de conclusão sem ajuda por tarefa e no conjunto do teste;
- principais erros ou dúvidas observados;
- tempo aproximado de execução das tarefas;
- médias pragmática, hedônica e geral, além dos comentários do UEQ-S.

Os resultados não terão pretensão de representar todas as agências de marketing. Eles servirão para verificar se o protótipo atende ao problema e para orientar melhorias na aplicação.

## 5. Critérios de qualidade e validade

Para reduzir ambiguidades, os requisitos serão associados a critérios de aceite e casos de teste. A aprovação funcional exigirá 100% de aprovação dos casos de alta prioridade, pelo menos 90% dos casos totais e ausência de defeito crítico. O registro de histórico do ticket permitirá verificar se as regras de negócio foram atendidas. A validação com usuários exigirá ao menos 80% de conclusão sem ajuda, sem tarefa abaixo de 70%, e adotará como meta média geral do UEQ-S superior a 0,8.

Como a avaliação utilizará amostra pequena e por conveniência, a validade externa será limitada. Para aumentar a credibilidade da análise, serão preservados os instrumentos de coleta, a lista de cenários, as decisões de requisitos e os resultados agregados de teste. Limitações e mudanças ocorridas durante o desenvolvimento deverão ser registradas na versão final.

## 6. Cronograma metodológico

| Etapa | Período previsto | Produto |
| --- | --- | --- |
| Revisão bibliográfica | Etapas 01 a 03 | Referencial / marco teórico. |
| Levantamento e priorização de requisitos | Etapa 03 | Documento de requisitos. |
| Modelagem e prototipação | Etapas 02 e 03 | Fluxos, telas e regras de negócio. |
| Desenvolvimento da versão inicial | Etapa 04 | Aplicação web inicial. |
| Testes funcionais e avaliação de usabilidade | Etapas 04 e 05 | Casos de teste, resultados e melhorias. |
| Revisão e entrega final | Etapa 05 | Versão final da aplicação e documentação. |

## 7. Situação atual

- [x] Pesquisa bibliográfica inicial realizada.
- [x] Referencial teórico inicial elaborado.
- [x] Requisitos preliminares definidos.
- [x] Esboço do protótipo produzido.
- [ ] Validação dos requisitos com potenciais usuários.
- [ ] Implementação da versão inicial do aplicativo.
- [ ] Testes funcionais executados.
- [ ] Avaliação de usabilidade executada.

> As etapas ainda não executadas serão documentadas com os resultados reais quando a aplicação estiver disponível para teste.

## Referências metodológicas

- ISO/IEC/IEEE. *ISO/IEC/IEEE 29148:2018 — Systems and software engineering: Life cycle processes: Requirements engineering*. 2018. Disponível em: [norma](https://www.iso.org/standard/72089.html).
- OBJECT MANAGEMENT GROUP. *Business Process Model and Notation (BPMN), Version 2.0*. 2011. Disponível em: [especificação BPMN](https://www.omg.org/spec/BPMN/2.0/).
- RADHITYA, Ridha et al. *Usability Analysis of Digital Signature Application Based on ISO 9241-11 Using Retrospective Think Aloud and User Experience Questionnaire*. 2024. DOI: [10.1109/ICITSI65188.2024.10929447](https://doi.org/10.1109/ICITSI65188.2024.10929447).
- PEOPLECERT. *ITIL 4 Practice Guide: Service Desk*. 2023. Disponível em: [guia de prática](https://www.peoplecert.org/browse-certifications/it-governance-and-service-management/ITIL-1/itil4-practices-service-desk-3706).
- AXELOS. *ITIL 4 Practice Guide: Service Request Management*. 2020. Disponível em: [prática ITIL](https://www.peoplecert.org/browse-certifications/it-governance-and-service-management/ITIL-1/itil4-practices-service-request-management-3690).
- BRASIL. *Lei nº 13.709, de 14 de agosto de 2018 — Lei Geral de Proteção de Dados Pessoais (LGPD).* Disponível em: [texto consolidado](https://www.gov.br/mj/pt-br/assuntos/sua-protecao/sedigi/Lei13709.pdf).
- AUTORIDADE NACIONAL DE PROTEÇÃO DE DADOS. *Guia orientativo sobre segurança da informação para agentes de tratamento de pequeno porte.* 2024. Disponível em: [guia da ANPD](https://www.gov.br/anpd/pt-br/centrais-de-conteudo/materiais-educativos-e-publicacoes/guia-orientativo-sobre-seguranca-da-informacao-para-agentes-de-tratamento-de-pequeno-porte).
