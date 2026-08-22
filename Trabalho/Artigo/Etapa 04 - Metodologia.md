# Trabalho Interdisciplinar — Sistemas Integrados de Gestão Empresarial

## Etapa 04 — Metodologia

**Tema:** Gestão de Demandas de Marketing Digital  
**Título provisório:** *Help Desk para Gestão de Demandas de Tráfego Pago*

## Resumo

Este trabalho aborda a gestão de demandas de tráfego pago em agências de marketing digital e investiga como solicitações encaminhadas a múltiplos pontos de contato podem dificultar a definição de responsáveis e prazos, o acompanhamento do atendimento, a recuperação de decisões e o registro de aprovações e evidências. Diante disso, o estudo tem como objetivo desenvolver o SIGE Desk, uma aplicação web de Help Desk destinada a centralizar e rastrear essas solicitações. A pesquisa caracteriza-se como aplicada, exploratória e descritiva, com abordagem predominantemente qualitativa, e utiliza revisão bibliográfica estruturada na IEEE Xplore e em fontes normativas e oficiais, derivação e especificação de requisitos fundamentadas no referencial, modelagem do processo em Business Process Model and Notation (BPMN) 2.0.2, prototipação e planejamento de testes funcionais e de usabilidade com seis participantes. Como resultados da etapa de especificação, foram definidos os perfis de usuário, o ciclo de vida dos tickets, as regras de acordo de nível de serviço (SLA), os requisitos funcionais e não funcionais, os mecanismos de histórico, aprovação e evidência e o plano de validação do sistema. Esses resultados indicam que a organização das demandas em tickets pode explicitar responsabilidades, prazos e decisões, reduzindo a perda de contexto e ampliando a rastreabilidade e a transparência do atendimento. Conclui-se, em caráter preliminar, que o SIGE Desk constitui uma proposta tecnicamente coerente para o problema identificado, cuja efetividade deverá ser confirmada após a implementação e a avaliação do produto mínimo viável (MVP).

**Palavras-chave:** gestão de demandas; help desk; tráfego pago; marketing digital; sistemas de informação.

## Abstract

This work addresses the management of paid advertising requests in digital marketing agencies and investigates how requests directed to multiple points of contact can make it difficult to assign responsibilities and deadlines, monitor service progress, retrieve decisions, and record approvals and evidence. Therefore, the study aims to develop SIGE Desk, a web-based help desk application designed to centralize and track these requests. The research is applied, exploratory, and descriptive, with a predominantly qualitative approach, and employs a structured literature review in IEEE Xplore and official and normative sources, literature-grounded requirements derivation and specification, Business Process Model and Notation (BPMN) 2.0.2 process modeling, prototyping, and the planning of functional and usability tests with six participants. The specification stage resulted in the definition of user roles, the ticket life cycle, service level agreement (SLA) rules, functional and non-functional requirements, history, approval, and evidence mechanisms, and a system validation plan. These results indicate that organizing requests as tickets can clarify responsibilities, deadlines, and decisions while reducing context loss and increasing service traceability and transparency. It is preliminarily concluded that SIGE Desk is a technically coherent proposal for the identified problem, although its effectiveness must still be confirmed through minimum viable product (MVP) implementation and evaluation.

**Keywords:** demand management; help desk; paid advertising; digital marketing; information systems.

## 1. Caracterização da pesquisa

Este trabalho será desenvolvido como uma pesquisa **aplicada**, pois busca propor e construir uma solução para um problema prático de gestão de demandas em agências de marketing digital. Quanto aos objetivos, a pesquisa terá caráter **exploratório e descritivo**: exploratório por investigar como ocorre a organização de solicitações de tráfego pago e descritivo por registrar necessidades, papéis, fluxos, requisitos e resultados de validação do protótipo.

A abordagem será predominantemente **qualitativa**, voltada a compreender o processo atual, as dificuldades dos usuários e a adequação do sistema. Dados quantitativos simples também poderão ser utilizados na validação, por exemplo quantidade de tarefas concluídas, tempo de execução de um cenário e respostas a questionário de experiência do usuário.

## 2. Procedimentos metodológicos

O desenvolvimento será organizado nas etapas a seguir.

### 2.1 Revisão bibliográfica

A revisão bibliográfica foi realizada de forma estruturada na base **IEEE Xplore**. As Pesquisas 01 e 02 foram executadas em **15 de agosto de 2026**; a Pesquisa 03, complementar e voltada a Help Desk e comunicação, foi executada em **21 de agosto de 2026**. À revisão foram somadas fontes normativas e oficiais pertinentes ao projeto e, exclusivamente para fundamentar o instrumento de usabilidade UEQ-S, um artigo externo localizado em `Referencias/Outras Fontes`; esse artigo não integra os totais do corpus da IEEE Xplore.

Nas Pesquisas 01 e 02, o recorte de publicação compreende **2019 a 2026**, de forma inclusiva, e documentos em **inglês**; as buscas foram executadas no campo *Document Title*. A Pesquisa 03 foi executada no campo *All*, sem recorte temporal e limitada, na exportação, a **artigos de periódicos e conferências**. As expressões booleanas foram:

```text
Pesquisa 01
("Document Title":"digital marketing")
AND
("Document Title":"marketing automation"
 OR "Document Title":"project management"
 OR "Document Title":"customer relationship management"
 OR "Document Title":"marketing agency"
 OR "Document Title":"management system")

Pesquisa 02
("Document Title":"digital marketing")
AND
("Document Title":campaign
 OR "Document Title":advertising
 OR "Document Title":"paid advertising"
 OR "Document Title":"media buying")

Pesquisa 03
("service desk") AND communication
```

Foram incluídos artigos recuperados pelas expressões, com resumo ou texto disponível para leitura e que contribuíssem diretamente para pelo menos um dos seguintes aspectos: gestão de demandas ou projetos em agências; centralização de atividades de marketing; operação de campanhas pagas e suas métricas; relacionamento e comunicação com clientes; privacidade aplicável ao contexto de campanhas; ou ponto único de contato, registro, rastreamento e processamento de solicitações. Nas Pesquisas 01 e 02, aplicaram-se adicionalmente o período e o idioma definidos; na Pesquisa 03, o filtro de tipo documental foi periódico ou conferência. Foram excluídos estudos cujo foco principal fosse: (a) automação, predição ou personalização por IA fora do escopo do MVP; (b) IoT, sinalização digital ou outro domínio tecnológico sem relação com a gestão de tickets; (c) comportamento de consumo ou nicho de mercado sem contribuição direta para o fluxo de atendimento; (d) tema genérico já coberto por fonte mais aderente; ou (e) domínio de telecomunicações, infraestrutura, saúde ou segurança sem transferência direta para o fluxo de demandas proposto.

As três buscas recuperaram **44 artigos sem duplicidade**: 10 na Pesquisa 01, 15 na Pesquisa 02 e 19 na Pesquisa 03. As duplicidades foram verificadas pela comparação dos DOIs. A triagem consistiu na leitura de títulos e resumos, na verificação da aderência aos critérios de inclusão e exclusão e na extração de conteúdo dos trabalhos avaliados. Ao final, **13 artigos** foram utilizados no referencial teórico e **31 artigos** foram descartados conforme esses critérios. A relação dos descartes e suas justificativas está documentada em [Tabela - artigos descartados.md](../../Referencias/Tabela%20-%20artigos%20descartados.md).

Os artigos selecionados foram usados para fundamentar conceitos, identificar práticas recorrentes e apoiar escolhas de requisitos. A análise priorizou trabalhos sobre gestão de projetos em agência, plataformas de marketing, gestão de campanhas, orçamento, métricas de desempenho, privacidade, rastreabilidade, ponto único de contato e processamento de solicitações.

### 2.2 Derivação e especificação de requisitos

Os requisitos preliminares serão derivados do problema de pesquisa, dos objetivos, do referencial teórico, das normas aplicáveis e do esboço do protótipo. Portanto, o levantamento de requisitos desta etapa é bibliográfico e documental; não envolve entrevista ou outra coleta de dados com clientes, profissionais de atendimento/gestão de conta ou gestores de tráfego.

A análise das fontes orientará as categorias entrada da demanda, priorização, execução, comunicação, aprovação, encerramento e indicadores. Cada requisito será classificado como alta, média ou baixa prioridade para a primeira versão do aplicativo. Para manter rastreabilidade, cada requisito deverá registrar origem, versão, responsável, critério de aceite e caso de teste relacionado (ISO/IEC/IEEE, 2018).

### 2.3 Modelagem do processo e do protótipo

O fluxo de vida desejado (**To Be**) e o esboço do protótipo de baixa fidelidade já foram elaborados a partir dos requisitos preliminares. O fluxo atual (**As Is**) é uma hipótese analítica derivada da problemática e do referencial, usada para contrastar o problema com a solução proposta; não descreve empiricamente a rotina de uma agência específica. A representação do processo futuro adota Business Process Model and Notation (BPMN) 2.0.2, distinguindo Cliente e Agência como participantes e Atendimento/Gestor de conta e Gestor de tráfego como responsabilidades internas (OMG, 2014). Também serão refinados os perfis de usuário, regras de negócio, dados necessários, acordos de nível de serviço (SLAs) e telas principais.

O protótipo de baixa fidelidade apresenta painel, listagem de tickets, abertura de solicitação e detalhes do ticket. Ele será refinado conforme a revisão dos requisitos derivados e deverá evidenciar os principais caminhos do usuário antes da implementação da aplicação.

### 2.4 Desenvolvimento da aplicação

A versão inicial será desenvolvida como aplicação web. A implementação seguirá ciclos curtos: selecionar requisitos priorizados, implementar, testar o cenário correspondente e registrar eventuais ajustes. O foco inicial será garantir cadastro, autenticação, tickets, comentários, histórico, status, prazos, responsáveis, notificações, filtros e controles de acesso.

Integrações automáticas com plataformas de anúncios, previsão por inteligência artificial e automação de alterações em campanhas não serão implementadas no MVP. Essa delimitação permitirá avaliar primeiro o valor do fluxo de atendimento e rastreabilidade.

### 2.5 Testes funcionais

Os casos de teste serão derivados dos critérios de aceite e deverão registrar pré-condição, passos, resultado esperado, resultado obtido e situação final. O catálogo canônico dos dezessete cenários, seus requisitos associados e seus resultados esperados está em [Especificação do sistema.md](../Documentacao%20do%20Projeto/Especificacao/Especificacao%20do%20sistema.md#9-critérios-de-aceite-iniciais). No artigo, eles são descritos de forma sintética, pois o catálogo completo permanece na documentação do projeto.

O critério de aprovação funcional será 100% dos casos de alta prioridade aprovados, pelo menos 90% do total de casos aprovados e nenhum defeito crítico em aberto. Casos reprovados deverão gerar registro de correção ou justificativa de adiamento.

### 2.6 Avaliação de usabilidade

Após a versão inicial, o protótipo ou aplicação será submetido a uma avaliação de usabilidade com **seis participantes**, selecionados por conveniência: dois potenciais clientes, dois profissionais de atendimento/gestão de conta e dois gestores de tráfego. Caso algum perfil não esteja disponível, a substituição e sua limitação serão registradas no relatório.

Cada participante executará cinco tarefas: (1) abrir uma demanda com os dados obrigatórios; (2) localizar e filtrar um ticket; (3) classificar, priorizar e atribuir a demanda; (4) incluir comentário e encaminhar a entrega para validação; e (5) aprovar ou reabrir a demanda com justificativa. Serão observados obstáculos, dúvidas, erros, necessidade de ajuda e tempo de realização.

Ao final, será aplicado o **Short User Experience Questionnaire (UEQ-S)**. A versão curta possui oito itens, distribuídos entre as dimensões pragmática e hedônica; suas respostas, coletadas em escala de 1 a 7, serão convertidas para a escala de -3 a +3, conforme a proposta e a validação do instrumento (SCHREPP; HINDERKS; THOMASCHEWSKI, 2017). Serão calculadas as médias das dimensões e a média geral. O UEQ-S será empregado para reduzir o esforço de resposta após a execução das tarefas, reconhecendo-se que ele fornece uma avaliação resumida, e não o detalhamento das seis dimensões do UEQ completo. Os resultados, juntamente com conclusão das tarefas, tempo, erros, dúvidas e comentários, serão analisados de forma descritiva para identificar melhorias na interface, sem estabelecer meta numérica de aprovação para o protótipo acadêmico. O registro conjunto desses dados segue a abordagem de avaliação de usabilidade discutida por Radhitya et al. (2024).

Os resultados serão apresentados de forma agregada, sem identificação individual. Sugestões e dificuldades recorrentes serão transformadas em lista de melhorias para a versão final.

## 3. Participantes e dados

Os seis participantes serão convidados por conveniência entre pessoas que possuam experiência com atendimento, gestão de campanhas ou contratação de serviços de marketing digital. A participação será voluntária e ocorrerá apenas após explicação do objetivo do teste e concordância com o registro agregado dos resultados. Os dados de pesquisa serão limitados ao perfil do participante, resultados das tarefas e respostas ao questionário; não serão usados tickets reais nem dados de clientes sem autorização.

O projeto coletará somente dados necessários para a pesquisa e para o funcionamento do protótipo, como perfil de usuário, respostas ao questionário e registros fictícios de tickets. Não serão solicitadas credenciais de plataformas de anúncio, dados bancários, bases de audiência ou informações pessoais sensíveis.

## 4. Análise dos dados

Os dados qualitativos das entrevistas, observações e comentários serão analisados por categorização temática. As categorias iniciais serão: problemas do processo atual, informações necessárias no ticket, comunicação, prioridade, prazo, aprovação, evidências e indicadores.

Os dados quantitativos de teste serão analisados de forma descritiva, considerando:

- quantidade de casos de teste aprovados e reprovados;
- quantidade de tarefas concluídas por participante;
- principais erros ou dúvidas observados;
- tempo aproximado de execução das tarefas;
- médias pragmática, hedônica e geral, além dos comentários do UEQ-S.

Os resultados não terão pretensão de representar todas as agências de marketing. Eles servirão para verificar se o protótipo atende ao problema e para orientar melhorias na aplicação.

## 5. Critérios de qualidade e validade

Para reduzir ambiguidades, os requisitos serão associados a critérios de aceite e casos de teste. A aprovação funcional exigirá 100% de aprovação dos casos de alta prioridade, pelo menos 90% dos casos totais e ausência de defeito crítico. O registro de histórico do ticket permitirá verificar se as regras de negócio foram atendidas. A avaliação com usuários será interpretada de forma descritiva, a partir da realização das tarefas, dos erros, das dúvidas, do tempo e das respostas ao UEQ-S, sem meta numérica rígida para o protótipo acadêmico.

Como a avaliação utilizará amostra pequena e por conveniência, a validade externa será limitada. A revisão bibliográfica também se limita à IEEE Xplore, ao campo de título e a publicações em inglês; portanto, não pretende esgotar a literatura. Para aumentar a credibilidade da análise, serão preservados os instrumentos de coleta, a lista de cenários, as decisões de requisitos e os resultados agregados de teste. Limitações e mudanças ocorridas durante o desenvolvimento deverão ser registradas na versão final.

## 6. Cronograma de atividades do artigo e do projeto

| Tarefa | 07/08<br>Definição de grupos | 28/08<br>1ª Etapa | 25/09<br>2ª Etapa | 30/10<br>3ª Etapa | 27/11<br>4ª Etapa | 11/12<br>5ª Etapa e apresentação final |
| --- | :---: | :---: | :---: | :---: | :---: | :---: |
| Auto registro no CANVAS, debate de possibilidades, definição de grupo, tema / área e título provisório | X |  |  |  |  |  |
| Validação do tema e do título; revisão da introdução pelo orientador |  | X |  |  |  |  |
| Introdução, problemática, justificativa e objetivos |  |  | X |  |  |  |
| Esboço do protótipo do APP |  |  | X | X |  |  |
| Referencial / marco teórico |  |  |  | X |  |  |
| Requisitos do APP |  |  |  | X |  |  |
| Metodologia |  |  |  |  | X |  |
| Versão inicial do APP |  |  |  |  | X |  |
| Versão final do APP |  |  |  |  |  | X |
| Mostra Tecnológica Interdisciplinar |  |  |  |  |  | X |
| Artigo completo revisado pelo orientador, no padrão PUC Minas |  |  |  |  |  | X |
| Apresentação final e demo do APP |  |  |  |  |  | X |

> Em cada etapa, será realizado o envio do link do repositório GitHub correspondente à entrega prevista.

## 7. Resultados da especificação

A etapa de especificação identificou 23 requisitos funcionais, seis requisitos não funcionais e 14 regras de negócio. Eles foram organizados em cinco eixos: abertura e acompanhamento de solicitações; triagem, atribuição e prazos; comunicação, aprovação e evidências; gestão de cadastros e configurações; e rastreabilidade, segurança, privacidade, compatibilidade e acessibilidade. A organização demonstra que a proposta não se limita ao registro de chamados, pois articula controle operacional, transparência para o cliente e apoio à gestão.

O catálogo detalhado, os critérios de aceite e a rastreabilidade estão mantidos somente em [Especificação do sistema.md](../Documentacao%20do%20Projeto/Especificacao/Especificacao%20do%20sistema.md). No artigo, esse resultado é apresentado de forma sintética.

## 8. Situação atual

- [x] Pesquisa bibliográfica inicial realizada.
- [x] Referencial teórico inicial elaborado.
- [x] Requisitos preliminares definidos.
- [x] Esboço do protótipo produzido.
- [x] Síntese dos resultados de especificação elaborada para o artigo; o catálogo completo permanece na documentação do projeto.
- [ ] Implementação da versão inicial do aplicativo.
- [ ] Testes funcionais executados.
- [ ] Avaliação de usabilidade executada.

> As etapas ainda não executadas serão documentadas com os resultados reais quando a aplicação estiver disponível para teste.

## Referências metodológicas

- ISO/IEC/IEEE. *ISO/IEC/IEEE 29148:2018 — Systems and software engineering: Life cycle processes: Requirements engineering*. 2. ed. Geneva, 2018. Disponível em: [norma](https://www.iso.org/standard/72089.html). Acesso em: 16 ago. 2026.
- OBJECT MANAGEMENT GROUP. *Business Process Model and Notation (BPMN), Version 2.0.2*. Needham, 2014. Disponível em: [especificação BPMN](https://www.omg.org/spec/BPMN/2.0.2/). Acesso em: 16 ago. 2026.
- RADHITYA, Ridha et al. *Usability Analysis of Digital Signature Application Based on ISO 9241-11 Using Retrospective Think Aloud and User Experience Questionnaire*. 2024. DOI: [10.1109/ICITSI65188.2024.10929447](https://doi.org/10.1109/ICITSI65188.2024.10929447). Acesso em: 15 ago. 2026.
- SCHREPP, Martin; HINDERKS, Andreas; THOMASCHEWSKI, Jörg. *Design and Evaluation of a Short Version of the User Experience Questionnaire (UEQ-S).* *International Journal of Interactive Multimedia and Artificial Intelligence*, v. 4, n. 6, p. 103-108, 2017. DOI: [10.9781/ijimai.2017.09.001](https://doi.org/10.9781/ijimai.2017.09.001). Acesso em: 15 ago. 2026.
- PEOPLECERT. *ITIL 4 Practice Guide: Service Desk*. London, 2023a. Disponível em: [guia de prática](https://www.peoplecert.org/browse-certifications/it-governance-and-service-management/ITIL-1/itil4-practices-service-desk-3706). Acesso em: 16 ago. 2026.
- PEOPLECERT. *ITIL 4 Practice Guide: Service Request Management*. London, 2023b. Disponível em: [prática ITIL](https://www.peoplecert.org/browse-certifications/it-governance-and-service-management/ITIL-1/itil4-practices-service-request-management-3690). Acesso em: 16 ago. 2026.
- BRASIL. *Lei nº 13.709, de 14 de agosto de 2018 — Lei Geral de Proteção de Dados Pessoais (LGPD).* Brasília, DF: Presidência da República, 2018. Disponível em: [texto consolidado](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709compilado.htm). Acesso em: 16 ago. 2026.
- AUTORIDADE NACIONAL DE PROTEÇÃO DE DADOS. *Guia orientativo sobre segurança da informação para agentes de tratamento de pequeno porte.* Brasília, DF, 2021. Disponível em: [guia da ANPD](https://www.gov.br/anpd/pt-br/documentos-e-publicacoes/guia-vf.pdf). Acesso em: 16 ago. 2026.
