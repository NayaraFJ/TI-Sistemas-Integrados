# Trabalho Interdisciplinar — Sistemas Integrados de Gestão Empresarial

## Etapa 02 — Texto acadêmico

**Curso:** Sistemas de Informação  
**Disciplina:** Trabalho Interdisciplinar — Sistemas Integrados de Gestão Empresarial (TI SIGE)  
**Professor:** Prof. Dr. Paulo Isnard  
**Semestre:** 2/2026  
**Grupo:** 2 2026 - PBR - TI SIGE 1  

**Integrantes:** Augusto do Couto Avelar; Gabriel Fernandes de Araújo; Gabriel Rocha Lopes; Karine Kessen Gilberti Felix; Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes; Nayara Ferreira de Jesus Rodrigues; Pedro Henrique Assuncao.

## Tema / área

**Tema:** Gestão de Demandas de Marketing Digital.  
**Área de estudo:** Sistemas de Informação / Gestão de Serviços.

## Título provisório

**Help Desk para Gestão de Demandas de Tráfego Pago**

## Resumo

Este trabalho aborda a gestão de demandas de tráfego pago em agências de marketing digital e analisa como a centralização e o rastreamento de solicitações podem apoiar a definição de responsáveis e prazos, o acompanhamento do atendimento, a recuperação de decisões e o registro de aprovações e evidências. O estudo tem como objetivo desenvolver uma aplicação web de Help Desk, denominada SIGE Desk, destinada a organizar essas solicitações. A pesquisa caracteriza-se como aplicada, exploratória e descritiva, com abordagem predominantemente qualitativa, e utiliza revisão bibliográfica estruturada na IEEE Xplore e em fontes normativas e oficiais, derivação e especificação de requisitos fundamentadas no referencial, modelagem em BPMN 2.0, prototipação e planejamento de testes funcionais e de usabilidade com seis participantes. Como resultados da etapa de especificação, foram definidos os perfis de usuário, o ciclo de vida dos tickets, as regras de SLA, os requisitos funcionais e não funcionais, os mecanismos de histórico, aprovação e evidência e o plano de validação do sistema. Conclui-se, em caráter preliminar, que o SIGE Desk constitui uma proposta tecnicamente coerente para apoiar a organização e a rastreabilidade das demandas, cuja efetividade deverá ser confirmada após a implementação e a avaliação do MVP.

**Palavras-chave:** gestão de demandas; help desk; tráfego pago; marketing digital; sistemas de informação.

## Abstract

This work addresses the management of paid advertising requests in digital marketing agencies and analyzes how the centralization and tracking of requests can support the definition of responsibilities and deadlines, service monitoring, retrieval of decisions, and recording of approvals and evidence. The study aims to develop SIGE Desk, a web-based help desk application designed to organize these requests. The research is applied, exploratory, and descriptive, with a predominantly qualitative approach, and employs a structured literature review in IEEE Xplore and official and normative sources, literature-grounded requirements derivation and specification, BPMN 2.0 process modeling, prototyping, and the planning of functional and usability tests with six participants. The specification stage resulted in the definition of user roles, the ticket life cycle, SLA rules, functional and non-functional requirements, history, approval, and evidence mechanisms, and a system validation plan. It is preliminarily concluded that SIGE Desk is a technically coherent proposal to support the organization and traceability of requests, although its effectiveness must still be confirmed through MVP implementation and evaluation.

**Keywords:** demand management; help desk; paid advertising; digital marketing; information systems.

## 1. Introdução

O marketing digital é uma atividade estratégica para organizações que desejam divulgar produtos e serviços, alcançar públicos específicos e acompanhar resultados por indicadores mensuráveis (Gujar, Paliwal e Panyam, 2024; Younas et al., 2025). No âmbito da publicidade digital paga, a cadeia de serviços envolve anunciantes, agências de mídia, *publishers* digitais e plataformas de troca de anúncios, entre os quais circulam informação, recursos financeiros e serviços (Agus et al., 2019).

Em operações de marketing digital, a gestão de campanhas envolve criação, orçamento, públicos, conteúdo e análise de resultados (Younas et al., 2025; Yahia e ElBolok, 2024; Nain et al., 2025). É nesse contexto operacional que se insere a proposta do SIGE Desk, uma aplicação web de Help Desk destinada a organizar demandas de tráfego pago. No sistema, esses elementos poderão ser tratados como solicitações, tais como criação ou pausa de anúncios, ajustes de orçamento e de público, aprovação de criativos e pedido de relatórios. A literatura de Service Desk relata que o encaminhamento de solicitações diretamente a diferentes pessoas pode produzir múltiplos pontos de contato e apresenta o Service Desk como ponto único para receber, registrar, analisar, encaminhar e acompanhar solicitações (Firmansyah e Subriadi, 2022; Fenner et al., 2015; Amanullah, Zeki e Abubakar, 2017).

Os estudos revisados sobre marketing digital concentram-se na automação, no planejamento, no orçamento e no desempenho das campanhas (Gujar, Paliwal e Panyam, 2024; Younas et al., 2025; Yahia e ElBolok, 2024; Nain et al., 2025). Os estudos de Service Desk, por sua vez, tratam do recebimento e do acompanhamento de solicitações (Firmansyah e Subriadi, 2022; Fenner et al., 2015; Amanullah, Zeki e Abubakar, 2017). A proposta do SIGE Desk aproxima esses campos ao tratar uma demanda de tráfego pago como solicitação de serviço a ser registrada e acompanhada, sem executar ou automatizar a campanha.

Quando as informações de uma solicitação não são mantidas em um registro comum, tornam-se mais difíceis a identificação do pedido original, a definição de responsável, o acompanhamento de prazo, o registro de aprovação e a recuperação das decisões tomadas (Fenner et al., 2015; PEOPLECERT, 2023; AXELOS, 2020). Essa dificuldade é particularmente relevante no tráfego pago, pois alterações em orçamento, público ou criativo podem afetar o desempenho e o custo de uma campanha (Yahia e ElBolok, 2024). As práticas de Service Desk e de gerenciamento de solicitações procuram mitigar esses riscos por meio de registro, triagem, comunicação e acompanhamento até a conclusão (Firmansyah e Subriadi, 2022; PEOPLECERT, 2023; AXELOS, 2020). Assim, a questão que orienta o trabalho é: **como uma aplicação web de Help Desk pode centralizar e rastrear demandas de tráfego pago, melhorando o acompanhamento de campanhas e o atendimento a clientes de agências de marketing digital?**

O estudo justifica-se pela possibilidade de organizar as solicitações em tickets com responsáveis, prioridades, prazos, comentários, aprovações, histórico e evidências. Essa estrutura pode apoiar a distribuição do trabalho, a prestação de contas ao cliente e a consulta a indicadores de atendimento, sem substituir as plataformas de mídia nem automatizar a alteração dos anúncios. Do ponto de vista acadêmico, a proposta integra Sistemas de Informação, Gestão de Serviços, Gestão de Projetos e Marketing Digital, aplicando conhecimentos de levantamento de requisitos, modelagem de processos, experiência do usuário, desenvolvimento web e validação de software a um problema de gestão.

O objetivo geral é desenvolver uma aplicação web de Help Desk para centralizar, acompanhar e registrar demandas de tráfego pago. Para tanto, pretende-se revisar artigos e normas aplicáveis; levantar, analisar e priorizar requisitos; modelar o processo de atendimento e o backlog; prototipar e implementar um produto mínimo viável com tickets, histórico, prazos e comunicação entre os envolvidos; e realizar testes funcionais e avaliação de usabilidade.

Na versão consolidada em `../../Overleaf`, o artigo apresenta, após a introdução, o referencial teórico sobre gestão de demandas, Help Desk e tráfego pago; descreve a metodologia; expõe os resultados da especificação e da proposta de solução; e encerra com as considerações finais. As seções de problemática, justificativa e objetivos abaixo são mantidas como registro da decomposição acadêmica desta etapa.

## 2. Problemática

### 2.1 Situação-problema

No contexto delimitado para o SIGE Desk, a gestão de uma solicitação de tráfego pago requer planejamento, comunicação e acompanhamento. Como analogia de processo, a literatura de Service Desk indica que o encaminhamento de solicitações a diferentes pessoas sem ponto de contato e registro comum pode dificultar o acompanhamento de responsável, prazo, histórico e andamento do atendimento (Firmansyah e Subriadi, 2022; Fenner et al., 2015). Além disso, alterações em orçamento, público ou criativo podem afetar diretamente o desempenho e o custo de uma campanha (Yahia e ElBolok, 2024). Assim, a proposta prevê uma estrutura de atendimento que registre a solicitação, permita sua triagem e comunique seu andamento ao solicitante, práticas centrais de Service Desk e de gerenciamento de solicitações de serviço (PEOPLECERT, 2023; AXELOS, 2020).

### 2.2 Pergunta de pesquisa

**Como uma aplicação web de Help Desk pode centralizar e rastrear demandas de tráfego pago, melhorando o acompanhamento de campanhas e o atendimento a clientes de agências de marketing digital?**

### 2.3 Consequências do problema

O estudo de caso de Steponaitis e Andrijauskienė (2023), realizado em uma agência de marketing digital, identificou planejamento, comunicação, gestão de tempo, monitoramento e análise de dados entre as competências relevantes para a condução de projetos digitais. Embora não avalie a adoção de Help Desk e não permita generalizações para todas as agências, esse resultado sustenta a necessidade de organizar as informações necessárias ao acompanhamento das demandas. Sem uma gestão centralizada, podem ocorrer:

- perda ou duplicidade de solicitações;
- dificuldade de priorizar demandas urgentes;
- falta de definição clara de responsável e prazo;
- ausência de histórico sobre alterações e aprovações;
- retrabalho entre atendimento, tráfego e criação;
- baixa visibilidade para o cliente sobre o andamento do pedido;
- dificuldade de medir volume de demandas, tempo de atendimento e cumprimento de prazos.

## 3. Justificativa

O projeto investiga como centralizar e rastrear demandas de tráfego pago. Um Help Desk adaptado ao contexto de tráfego pago pode transformar solicitações em registros estruturados, permitindo acompanhar o ciclo completo de cada pedido — abertura, triagem, execução, validação e encerramento. Essa organização se aproxima das práticas de Service Desk e de gerenciamento de solicitações de serviço da ITIL 4, que enfatizam ponto de contato, registro, comunicação e acompanhamento do atendimento (Firmansyah e Subriadi, 2022; PEOPLECERT, 2023; AXELOS, 2020).

Do ponto de vista organizacional, a aplicação pode melhorar a distribuição de trabalho, reduzir retrabalho e facilitar a prestação de contas ao cliente. Para a equipe, o sistema fornecerá uma fila de tarefas, definição de responsáveis, prioridades e prazos. Para o cliente, oferecerá uma visão clara do status da solicitação e do histórico de decisões. Para a gestão, os registros poderão apoiar indicadores como quantidade de demandas por cliente, tempo médio de atendimento, demandas vencidas e tipos de alterações mais solicitadas. Essas funcionalidades respondem, no contexto do SIGE Desk, às necessidades de planejamento, comunicação, gestão de tempo e monitoramento identificadas no estudo de caso de uma agência de marketing digital (Steponaitis e Andrijauskienė, 2023).

Do ponto de vista acadêmico, o trabalho integra conceitos de Sistemas de Informação, Gestão de Serviços, Gestão de Projetos e Marketing Digital. A proposta permite aplicar conhecimentos de levantamento de requisitos, modelagem de processos, experiência do usuário, banco de dados, desenvolvimento web e validação de software em um problema real de gestão. A pesquisa sobre agências evidencia a importância de planejamento, comunicação e monitoramento; o sistema proposto busca operacionalizar esses elementos no fluxo diário de atendimento (Steponaitis e Andrijauskienė, 2023).

## 4. Objetivos

### 4.1 Objetivo geral

Desenvolver uma aplicação web de Help Desk para centralizar, acompanhar e registrar demandas de tráfego pago, apoiando a gestão das solicitações e o atendimento a clientes de agências de marketing digital.

### 4.2 Objetivos específicos

Pretende-se realizar uma revisão bibliográfica de artigos e normas aplicáveis para compreender o problema e fundamentar a solução; levantar, analisar e priorizar os requisitos do sistema; modelar o processo de atendimento e definir o backlog com responsabilidades, dependências e prazos de entrega; prototipar a solução; implementar o produto mínimo viável (MVP) com cadastro, autenticação, tickets, acompanhamento de prazos, histórico e comunicação entre os envolvidos; realizar testes funcionais e avaliação de usabilidade; e entregar uma versão final funcional do aplicativo, acompanhada da documentação e da demonstração previstas para o projeto.

## Referências

As referências acadêmicas foram consolidadas em [Etapa 03 - Referencial teorico.md](Etapa%2003%20-%20Referencial%20teorico.md), em `../../Overleaf/bibliografia.bib` e em `../../Overleaf Progressivo/bibliografia.bib`. Esta etapa mantém somente o texto de introdução, problema, justificativa e objetivos.
