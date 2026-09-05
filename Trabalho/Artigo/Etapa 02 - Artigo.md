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

### 1.1 Problemática

No estudo de caso realizado em uma agência de marketing digital, Steponaitis e Andrijauskienė (2023) identificaram planejamento, comunicação, gestão de tempo e monitoramento entre as competências relevantes para a condução de projetos digitais. Esses achados situam a organização do trabalho e a troca de informações como aspectos pertinentes à investigação das demandas de tráfego pago, embora não constituam um diagnóstico de todas as agências nem uma avaliação do uso de Help Desk.

Na gestão de serviços de TI, Firmansyah e Subriadi (2022) relatam que o encaminhamento de incidentes e solicitações diretamente a diferentes pessoas, no contexto governamental discutido pelos autores, dá origem a múltiplos pontos de contato. O guia de Service Desk da ITIL 4 também destaca que canais de comunicação sem integração podem introduzir confusão no fluxo de informações e recomenda o registro das interações e a comunicação do andamento do atendimento (PEOPLECERT, 2023). Esses fundamentos oferecem uma referência para investigar a centralização das solicitações no contexto deste trabalho.

A problemática consiste, portanto, em como adaptar essas práticas ao registro e ao acompanhamento de demandas de tráfego pago, articulando o pedido, as responsabilidades, os prazos e as decisões de atendimento. No SIGE Desk, essa articulação será investigada por meio de solicitações como ajustes de orçamento, aprovação de criativos e pedidos de relatórios, conforme o escopo apresentado na introdução. Trata-se de um problema delimitado a partir da literatura e da proposta de sistema, cuja adequação deverá ser examinada no desenvolvimento e na avaliação da aplicação.

À vista disso, coloca-se a seguinte pergunta de pesquisa: **como uma aplicação web de Help Desk pode centralizar e rastrear demandas de tráfego pago, melhorando o acompanhamento de campanhas e o atendimento a clientes de agências de marketing digital?**

### 1.2 Objetivos

#### 1.2.1 Objetivo geral

Desenvolver uma aplicação web de Help Desk para centralizar, acompanhar e registrar demandas de tráfego pago, apoiando a gestão das solicitações e o atendimento a clientes de agências de marketing digital.

#### 1.2.2 Objetivos específicos

- Realizar uma revisão bibliográfica de artigos e normas aplicáveis para compreender o problema e fundamentar a solução.
- Levantar, analisar e priorizar os requisitos do sistema.
- Modelar o processo de atendimento e definir o *backlog* com responsabilidades, dependências e prazos de entrega.
- Prototipar a solução.
- Implementar o produto mínimo viável (MVP) com cadastro, autenticação, tickets, acompanhamento de prazos, histórico e comunicação entre os envolvidos.
- Realizar testes funcionais e avaliação de usabilidade.
- Entregar uma versão final funcional do aplicativo, acompanhada da documentação e da demonstração previstas para o projeto.

### 1.3 Justificativa

A relevância deste trabalho está na investigação de uma forma de organizar e acompanhar solicitações de tráfego pago, aproximando a gestão de serviços do atendimento prestado por agências de marketing digital. A motivação decorre da relação entre as competências de planejamento, comunicação e monitoramento identificadas por Steponaitis e Andrijauskienė (2023) e as práticas de registro e acompanhamento discutidas na literatura de Service Desk (Firmansyah e Subriadi, 2022). A partir dessa relação, o SIGE Desk propõe tratar cada demanda como uma solicitação de serviço cujo andamento possa ser consultado pelos envolvidos. A pesquisa busca examinar como essa organização pode apoiar o trabalho da equipe e a participação do cliente ao longo do atendimento.

A atualidade do tema encontra respaldo nos estudos recentes que integram tecnologias digitais à gestão das atividades de marketing. Gujar, Paliwal e Panyam (2024) discutem a automação de marketing apoiada por inteligência artificial e software como serviço, enquanto Younas et al. (2025) apresentam uma plataforma web que reúne recursos de campanhas, conteúdo e análise de dados. Esses trabalhos situam a integração de informações e atividades entre os assuntos investigados no campo. Nesse contexto, o presente estudo concentra-se na organização das solicitações relacionadas às campanhas, contribuindo para discutir o atendimento que acompanha essas atividades e a forma de registrar as interações entre agência e cliente.

Na dimensão organizacional e econômica, a contribuição esperada está no apoio ao planejamento do atendimento e ao uso do tempo de trabalho. Fenner et al. (2015) discutem a capacidade de atendimento de um Service Desk e consideram aspectos como registro de chamados, tempo de atendimento, espera e acompanhamento de níveis de serviço. No SIGE Desk, a definição de responsáveis e prazos e a manutenção de um histórico são propostas como meios de tornar consultáveis a distribuição e o andamento das demandas. Os registros poderão subsidiar a identificação de solicitações pendentes e a análise dos tempos de atendimento, oferecendo informações para decisões sobre prioridades e capacidade da equipe. A utilidade desses recursos dependerá de sua adequação ao processo e da atualização dos registros pelos usuários.

A dimensão econômica também se relaciona às decisões sobre os recursos destinados à publicidade. Yahia e ElBolok (2024) apresentam um modelo de alocação de orçamento entre plataformas, considerando limitações de verba e preferências do anunciante. Esse estudo evidencia a presença de critérios e restrições nas decisões de investimento em campanhas. No projeto proposto, o registro de um pedido de ajuste de orçamento, de sua justificativa e da decisão correspondente busca oferecer condições para consultar o que foi solicitado e autorizado. A contribuição pretendida é informacional: apoiar o acompanhamento de decisões que envolvem recursos de clientes e agências. Eventuais economias de tempo, redução de custos ou ganhos de retorno financeiro dependerão de avaliação posterior em condições de uso.

Na dimensão social, o trabalho é relevante pela atenção à comunicação e à participação das pessoas envolvidas no atendimento. O guia de Service Desk da ITIL 4 orienta a adoção de canais convenientes para os usuários e a comunicação de informações sobre suas solicitações (PEOPLECERT, 2023). No SIGE Desk, esse princípio orienta a possibilidade de o cliente consultar o andamento de um pedido, esclarecer informações e registrar sua aprovação ou a necessidade de ajustes. A contribuição social esperada está em oferecer condições para uma relação de serviço mais compreensível e participativa, na qual o solicitante disponha de informações para acompanhar o atendimento e manifestar suas necessidades durante o processo.

Para os profissionais da agência, a proposta busca disponibilizar um contexto compartilhado sobre o pedido e as decisões registradas, de modo a apoiar a comunicação durante sua execução. A relevância desse aspecto se aproxima dos achados de Steponaitis e Andrijauskienė (2023), que incluem comunicação e empatia entre as competências necessárias à gestão de projetos digitais. A aplicação será concebida como apoio à interação entre pessoas, preservando oportunidades de esclarecimento e retorno sobre a entrega. Nessa perspectiva, a avaliação de usabilidade prevista poderá identificar dificuldades para consultar, comunicar e validar uma demanda. Seus resultados oferecerão indícios sobre a adequação da interface a essas atividades, enquanto efeitos mais amplos sobre relações de trabalho e qualidade do atendimento exigirão acompanhamento posterior.

Na dimensão acadêmica, a pesquisa articula Sistemas de Informação, Gestão de Serviços, Gestão de Projetos e Marketing Digital em torno de um problema delimitado. Firmansyah e Subriadi (2022) destacam que a adoção de modelos de Service Desk exige ajustes às características das organizações. A proposta do SIGE Desk permite examinar essa necessidade de adaptação ao traduzir práticas de registro, comunicação e acompanhamento para solicitações de tráfego pago. A contribuição reside na explicitação dos fundamentos e das escolhas que orientam essa tradução, permitindo discutir a adequação das práticas ao domínio escolhido e os limites de sua aplicação.

O desenvolvimento também produzirá requisitos, modelos de processo, uma aplicação e registros de avaliação que poderão servir de base para análises e aperfeiçoamentos posteriores. A relação entre fundamentação, decisões de projeto e verificação do sistema permite examinar se a solução implementada corresponde ao que foi proposto. Dessa forma, o valor acadêmico do trabalho abrange tanto a aplicação integrada dos conhecimentos do curso quanto a produção de um artefato documentado e avaliável. A relevância da pesquisa está, assim, na investigação de uma alternativa de atendimento com contribuições organizacionais, econômicas e sociais esperadas, cuja adequação inicial será examinada por testes funcionais e avaliação de usabilidade.

## Referências

As referências acadêmicas foram consolidadas em [Etapa 03 - Referencial teorico.md](Etapa%2003%20-%20Referencial%20teorico.md), em `../../Overleaf/bibliografia.bib` e em `../../Overleaf Progressivo/bibliografia.bib`. Esta etapa mantém somente o texto de introdução, problema, justificativa e objetivos.
