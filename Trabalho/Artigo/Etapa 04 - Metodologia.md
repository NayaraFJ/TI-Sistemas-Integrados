# Trabalho Interdisciplinar — Sistemas Integrados de Gestão Empresarial

## Etapa 04 — Metodologia

**Tema:** Gestão de Demandas de Marketing Digital  
**Título:** *Help Desk para Gestão de Demandas de Tráfego Pago*

## Resumo

Este trabalho aborda a gestão de demandas de tráfego pago em agências de marketing digital e investiga como solicitações encaminhadas a múltiplos pontos de contato podem dificultar a definição de responsáveis e prazos, o acompanhamento do atendimento, a recuperação de decisões e o registro de aprovações e evidências. Diante disso, o estudo tem como objetivo desenvolver o SIGE Desk, uma aplicação web de Help Desk destinada a centralizar e rastrear essas solicitações. A pesquisa caracteriza-se como aplicada, exploratória e descritiva, com abordagem predominantemente qualitativa, e utiliza revisão bibliográfica estruturada na IEEE Xplore, complementada por livros e fontes normativas e oficiais, derivação e especificação de requisitos fundamentadas no referencial, modelagem do processo em Business Process Model and Notation (BPMN) 2.0, prototipação, desenvolvimento da aplicação e definição de protocolos de testes funcionais e de avaliação de usabilidade com seis participantes. Como resultados da etapa de especificação, foram definidos os perfis de usuário, o ciclo de vida dos tickets, as regras de acordo de nível de serviço (SLA), os requisitos funcionais e não funcionais, os mecanismos de histórico, aprovação e evidência e o plano de validação do sistema. Esses resultados indicam que a organização das demandas em tickets pode explicitar responsabilidades, prazos e decisões, reduzindo a perda de contexto e ampliando a rastreabilidade e a transparência do atendimento. Conclui-se, em caráter preliminar, que o SIGE Desk constitui uma proposta tecnicamente coerente para o problema identificado, cuja efetividade permaneceu dependente da avaliação do produto mínimo viável (MVP) com usuários.

**Palavras-chave:** gestão de demandas; help desk; tráfego pago; marketing digital; sistemas de informação.

## Abstract

This work addresses the management of paid advertising requests in digital marketing agencies and investigates how requests directed to multiple points of contact can make it difficult to assign responsibilities and deadlines, monitor service progress, retrieve decisions, and record approvals and evidence. Therefore, the study aims to develop SIGE Desk, a web-based help desk application designed to centralize and track these requests. The research is applied, exploratory, and descriptive, with a predominantly qualitative approach, and employs a structured literature review in IEEE Xplore, complemented by books and official and normative sources, literature-grounded requirements derivation and specification, Business Process Model and Notation (BPMN) 2.0 process modeling, prototyping, application development, and the design of functional test and six-participant usability evaluation protocols. The specification stage resulted in the definition of user roles, the ticket life cycle, service level agreement (SLA) rules, functional and non-functional requirements, history, approval, and evidence mechanisms, and a system validation plan. These results indicate that organizing requests as tickets can clarify responsibilities, deadlines, and decisions while reducing context loss and increasing service traceability and transparency. It is preliminarily concluded that SIGE Desk is a technically coherent proposal for the identified problem, although its effectiveness remained dependent on minimum viable product (MVP) evaluation with users.

**Keywords:** demand management; help desk; paid advertising; digital marketing; information systems.

## 1. Caracterização da pesquisa

A pesquisa foi caracterizada como **aplicada**, pois teve como objetivo propor e construir uma solução para um problema prático de gestão de demandas em agências de marketing digital. Quanto aos objetivos, assumiu caráter **exploratório e descritivo**: exploratório pela investigação de possibilidades de organização de solicitações de tráfego pago a partir da literatura e da proposta de sistema; descritivo pelo registro de necessidades, papéis, fluxos e requisitos.

A abordagem adotada foi predominantemente **qualitativa**, com análise bibliográfica e documental do problema e da adequação da solução proposta. O protocolo de avaliação também incorporou medidas quantitativas descritivas, como conclusão de tarefas, tempo de execução e respostas ao questionário de experiência do usuário. A revisão, a especificação, a modelagem, a prototipação e a implementação foram documentadas no projeto. As verificações técnicas da aplicação foram registradas separadamente; a avaliação com participantes foi descrita pelo protocolo elaborado, sem apresentação de resultados de sua aplicação nesta entrega.

## 2. Procedimentos metodológicos

Os procedimentos foram organizados em revisão bibliográfica, derivação de requisitos, modelagem, prototipação, desenvolvimento e definição dos protocolos de validação.

### 2.1 Revisão bibliográfica

A busca de artigos para o corpus temático foi realizada de forma estruturada na base **IEEE Xplore**. As Pesquisas 01 e 02 foram executadas em **15 de agosto de 2026**; a Pesquisa 03, complementar e voltada a Help Desk e comunicação, foi executada em **21 de agosto de 2026**. À revisão foram somadas fontes normativas e oficiais pertinentes ao projeto e, exclusivamente para fundamentar o instrumento de usabilidade UEQ-S, um artigo externo localizado em `Referencias/Outras Fontes`; esse artigo não integra os totais do corpus da IEEE Xplore.

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

As três buscas recuperaram **44 artigos sem duplicidade**: 10 na Pesquisa 01, 15 na Pesquisa 02 e 19 na Pesquisa 03. As duplicidades foram verificadas pela comparação dos DOIs. A triagem consistiu na leitura de títulos e resumos, na verificação da aderência aos critérios de inclusão e exclusão e na extração de conteúdo dos trabalhos avaliados. Ao final, **13 artigos** foram selecionados para fundamentar o projeto e **31 artigos** foram descartados conforme esses critérios. A seleção e as contribuições dos trabalhos foram registradas na [Tabela - artigos utilizados.md](../../Referencias/Tabela%20-%20artigos%20utilizados.md); os descartes e suas justificativas, na [Tabela - artigos descartados.md](../../Referencias/Tabela%20-%20artigos%20descartados.md).

Os artigos selecionados foram usados para fundamentar conceitos, identificar práticas recorrentes e apoiar escolhas de requisitos. A análise priorizou trabalhos sobre gestão de projetos em agência, plataformas de marketing, gestão de campanhas, orçamento, métricas de desempenho, privacidade, rastreabilidade, ponto único de contato e processamento de solicitações.

A fundamentação conceitual foi complementada pela **Pesquisa 04**, com leitura temática de **oito livros e do Guia do Scrum de 2020**, abrangendo pesquisa bibliográfica, requisitos, backlog, MVP e testes funcionais e de usabilidade. Os trechos foram registrados em [nove fichas de extração](../../Referencias/Pesquisa%2004/Extracoes/README.md), com identificação das obras, páginas e limites de leitura. Essa seleção não integra os totais de artigos das Pesquisas 01 a 03 e não foi tratada como revisão exaustiva das obras ou da literatura. Sua fundamentação e as referências completas estão consolidadas nas cinco primeiras seções de [Etapa 03 - Referencial teorico.md](Etapa%2003%20-%20Referencial%20teorico.md).

### 2.2 Derivação e especificação de requisitos

Os requisitos preliminares foram derivados do problema de pesquisa, dos objetivos, do referencial teórico, das normas aplicáveis e do esboço do protótipo. O levantamento foi bibliográfico e documental e não incluiu entrevistas ou outra coleta de dados com clientes, profissionais de atendimento/gestão de conta ou gestores de tráfego.

A análise das fontes orientou as categorias entrada da demanda, priorização, execução, comunicação, aprovação, encerramento e indicadores. Cada requisito foi classificado como alta, média ou baixa prioridade. Para manter rastreabilidade, o registro associou os requisitos a origem, versão, responsável, critério de aceite e caso de teste relacionado (ISO/IEC/IEEE, 2018).

### 2.3 Modelagem do processo e do protótipo

O fluxo desejado (**To Be**) e o esboço do protótipo de baixa fidelidade foram elaborados a partir dos requisitos preliminares. O fluxo atual (**As Is**) foi construído como hipótese analítica derivada da problemática e do referencial, sem representar um diagnóstico empírico de uma agência específica. A modelagem adotou Business Process Model and Notation (BPMN) 2.0 e distinguiu Cliente e Agência como participantes e Atendimento/Gestor de conta e Gestor de tráfego como responsabilidades internas (OMG, 2011).

O refinamento do fluxo separou a transferência de trabalho entre perfis da navegação da sessão, distinguiu correção em validação de reabertura após conclusão e permitiu conclusão direta apenas para tipos sem aprovação obrigatória. Também registrou a fase de origem do aguardo para orientar a retomada e definiu a classificação do SLA após a abertura, sem converter urgência informada em prioridade automática.

A prototipação organizou painel, listagem de tickets, abertura de solicitação, detalhes do ticket e os demais fluxos dos perfis. O [protótipo navegável](../../Prototipo-SIGE-Desk/README.md) representou as quinze telas definidas na documentação, com dados fictícios, e serviu de referência para a implementação.

### 2.4 Desenvolvimento da aplicação

O SIGE Desk foi desenvolvido como aplicação web, com frontend, API e persistência em banco de dados. A priorização dos requisitos delimitou o escopo da implementação, que incluiu cadastro, autenticação, tickets, comentários, histórico, estados, prazos, responsáveis, notificações, filtros e controles de acesso. Os registros de desenvolvimento e de verificação técnica foram mantidos na [documentação da aplicação](../../SIGE-Desk/README.md) e no [relatório de revisão técnica](../../SIGE-Desk/docs/REVISAO-ENTREGA.md).

O recorte do MVP excluiu integrações automáticas com plataformas de anúncios, previsão por inteligência artificial e automação de alterações em campanhas. Essa delimitação concentrou a proposta no fluxo de atendimento e na rastreabilidade das solicitações.

### 2.5 Testes funcionais

Os casos de teste foram derivados dos critérios de aceite. O catálogo contemplou dezessete cenários e definiu o registro de identificador, requisitos relacionados, prioridade, pré-condição, passos, resultado esperado, resultado obtido e situação final. Os cenários, os requisitos associados e os resultados esperados foram consolidados na [Especificação do sistema.md](../Documentacao%20do%20Projeto/Especificacao/Especificacao%20do%20sistema.md#9-critérios-de-aceite-iniciais), com síntese no artigo.

O critério interno de aprovação funcional foi definido como 100% dos casos de alta prioridade aprovados, pelo menos 90% do total de casos aprovados e ausência de defeito crítico em aberto. O protocolo estabeleceu o registro de correção ou justificativa de adiamento para cada reprovação. Esses limiares expressaram critérios de aceite do projeto, sem constituir resultados obtidos ou parâmetros universais da literatura.

A execução funcional em ambiente local isolado empregou requisições HTTP à API, MySQL e arquivos sintéticos, com evidências no [catálogo funcional](../../SIGE-Desk/docs/qa/catalogo-funcional.md), no [relatório de execução](../../SIGE-Desk/docs/qa/relatorio-execucao.md) e no [registro da execução mais recente](../../SIGE-Desk/docs/qa/execucao-catalogo-sla-corrigido-20261119.json). Em uma rodada anterior, o recorte automatizado registrou 16 dos 17 casos aprovados e 10 dos 11 de alta prioridade; o CT-07 foi reprovado porque notificações de atribuição não continham assunto, prioridade e prazo exigidos. Após as correções, a execução de 19 de novembro de 2026 registrou aprovação dos 17 casos, incluindo os 11 de alta prioridade, nas assertivas exercitadas por HTTP e MySQL. O pacote de testes Java coletou 75 testes, sem falhas ou erros, dos quais dois foram ignorados. Um deles, o teste de integração TicketOverdueMySqlIntegrationTest, foi executado separadamente em um esquema MySQL exclusivo de QA e aprovado, sem testes ignorados nessa execução.

A consolidação das evidências de interface, API e banco aprovou, até esta revisão, dezesseis dos dezessete casos (94,1%), incluindo os onze de alta prioridade. CT-15 permaneceu parcial pela ausência de captura preservada dos gráficos. O limiar numérico de 90% foi atingido e, considerando a revisão de defeitos conhecidos, o critério interno foi atendido no escopo examinado. As observações visuais não tiveram capturas preservadas, e o histórico extenso foi avaliado por amostragem. Em CT-09, a correção preserva prospectivamente os snapshots, mas não recupera aqueles substituídos anteriormente. Em CT-07, avisos adicionais foram enviados a usuários autorizados; a matriz de notificações da especificação não declara exclusividade dos destinatários listados. A revisão dos defeitos conhecidos não identificou crítico aberto no escopo examinado, sem descartar falhas em cenários não verificados. Os resultados referem-se à implementação local e não constituem avaliação de usabilidade com participantes.

### 2.6 Avaliação de usabilidade

Foi elaborado um protocolo de avaliação de usabilidade com amostra planejada de **seis participantes**: dois potenciais clientes, dois profissionais de atendimento/gestão de conta e dois gestores de tráfego. A seleção por conveniência foi definida para contemplar os três perfis envolvidos no atendimento e identificar dificuldades nas tarefas de cada papel. O dimensionamento teve finalidade exploratória e não decorreu de cálculo amostral. O protocolo incluiu o registro de eventuais substituições de perfil e de suas limitações.

O roteiro distribuiu as tarefas conforme as permissões de cada perfil. Para clientes, contemplou abertura, complemento, aprovação, solicitação de correção e reabertura; para Atendimento, classificação, atribuição e retomada; para gestores de tráfego, registro da execução. Também previu busca e filtros e o uso de sessões fictícias separadas para demonstrar as transferências entre papéis. A ficha de observação incluiu conclusão das tarefas, obstáculos, dúvidas, erros, necessidade de ajuda e tempo de realização.

O **Short User Experience Questionnaire (UEQ-S)** foi selecionado para aplicação após as tarefas. O instrumento possui oito itens distribuídos entre as dimensões pragmática e hedônica. Para o tratamento das respostas, foi definida a conversão da escala de 1 a 7 para a escala de -3 a +3 e o cálculo das médias das dimensões e da média geral (SCHREPP; HINDERKS; THOMASCHEWSKI, 2017). A escolha da versão curta considerou a redução do esforço de resposta e seu caráter resumido, sem o detalhamento das seis dimensões do UEQ completo.

O plano de análise combinou as medidas de execução das tarefas, as respostas ao UEQ-S e os comentários dos participantes, com interpretação descritiva e sem meta numérica de aprovação para o protótipo acadêmico. A combinação dessas medidas teve apoio na abordagem discutida por Radhitya et al. (2024). A documentação desta entrega registrou a definição do protocolo, sem apresentar resultados de aplicação com os seis participantes.

## 3. Participantes e dados

O protocolo definiu o convite por conveniência entre pessoas com experiência em atendimento, gestão de campanhas ou contratação de serviços de marketing digital. A participação voluntária, a explicação do objetivo do teste e a concordância com o registro agregado dos resultados foram estabelecidas como condições da coleta. O conjunto de dados foi delimitado ao perfil do participante, ao desempenho nas tarefas, às observações e às respostas ao questionário.

Foram previstos tickets fictícios e dados estritamente necessários à avaliação. O protocolo excluiu a solicitação de credenciais de plataformas de anúncios, dados bancários, bases de audiência e informações pessoais sensíveis, bem como o uso de dados reais de clientes sem autorização.

## 4. Análise dos dados

Foi definida a categorização temática das observações das tarefas e dos comentários dos participantes, com as categorias iniciais adequação do fluxo proposto, informações necessárias no ticket, comunicação, prioridade, prazo, aprovação, evidências e indicadores. O procedimento associou as dificuldades recorrentes às tarefas e aos requisitos correspondentes como base para a identificação de melhorias na interface e no fluxo de atendimento.

Para os dados quantitativos, o plano de análise adotou estatística descritiva e contemplou:

- quantidade de casos de teste aprovados e reprovados;
- quantidade de tarefas concluídas com e sem ajuda;
- principais erros ou dúvidas observados;
- tempo de execução das tarefas;
- médias pragmática, hedônica e geral do UEQ-S.

A apresentação agregada, sem identificação individual, e a conversão das dificuldades em uma lista de melhorias foram definidas como formas de registro. O plano não atribuiu aos resultados representatividade de todas as agências de marketing nem antecipou conclusões sobre a efetividade da aplicação.

## 5. Critérios de qualidade e validade

A associação entre requisitos, critérios de aceite e casos de teste foi adotada para reduzir ambiguidades e apoiar a verificação das regras de negócio. O histórico do ticket foi incluído como fonte de conferência das alterações do fluxo. A avaliação de usabilidade foi estruturada para considerar realização das tarefas, erros, dúvidas, tempo e respostas ao UEQ-S, sem meta numérica rígida.

A revisão do corpus temático ficou restrita à IEEE Xplore, com os campos, idiomas e recortes descritos para cada busca. Os livros e o Guia do Scrum da Pesquisa 04 complementaram a fundamentação conceitual, e o artigo externo sobre o UEQ-S fundamentou o instrumento. Essas seleções não foram tratadas como exaustivas. O tamanho reduzido e a seleção por conveniência foram reconhecidos como limites à generalização da avaliação proposta.

O protocolo incluiu a preservação dos instrumentos, cenários, decisões de requisitos e registros agregados, bem como a documentação de substituições de participantes e alterações de procedimento. A ausência de resultados da aplicação com participantes nesta entrega delimitou o alcance das conclusões sobre usabilidade.

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

> O cronograma estabeleceu o envio do link do repositório GitHub em cada entrega.

## 7. Resultados da especificação

A etapa de especificação identificou 23 requisitos funcionais, seis requisitos não funcionais e 14 regras de negócio. Eles foram organizados em cinco eixos: abertura e acompanhamento de solicitações; triagem, atribuição e prazos; comunicação, aprovação e evidências; gestão de cadastros e configurações; e rastreabilidade, segurança, privacidade, compatibilidade e acessibilidade. A organização demonstra que a proposta não se limita ao registro de chamados, pois articula controle operacional, transparência para o cliente e apoio à gestão.

O catálogo detalhado, os critérios de aceite e a rastreabilidade estão mantidos somente em [Especificação do sistema.md](../Documentacao%20do%20Projeto/Especificacao/Especificacao%20do%20sistema.md). No artigo, esse resultado é apresentado de forma sintética.

## 8. Situação atual

- [x] Pesquisa bibliográfica inicial realizada.
- [x] Referencial teórico inicial elaborado.
- [x] Requisitos preliminares definidos.
- [x] Esboço do protótipo produzido.
- [x] Síntese dos resultados de especificação elaborada para o artigo; o catálogo completo permanece na documentação do projeto.
- [x] Implementação da versão inicial do aplicativo, documentada em `SIGE-Desk`.
- [x] Verificações técnicas da implementação registradas no relatório de revisão.
- [ ] Aprovação integral do catálogo acadêmico de dezessete casos documentada.
- [ ] Avaliação de usabilidade executada.

> Os registros disponíveis comprovaram a implementação e verificações técnicas. A documentação desta entrega não incluiu a aplicação do protocolo de usabilidade com participantes nem comprovou a aprovação integral do catálogo acadêmico.

## Referências metodológicas

- ISO/IEC/IEEE. *ISO/IEC/IEEE 29148:2018 — Systems and software engineering: Life cycle processes: Requirements engineering*. 2. ed. Geneva, 2018. Disponível em: [norma](https://www.iso.org/standard/72089.html). Acesso em: 16 ago. 2026.
- OBJECT MANAGEMENT GROUP. *Business Process Model and Notation (BPMN), Version 2.0*. Needham, 2011. Disponível em: [especificação BPMN](https://www.omg.org/spec/BPMN/2.0/). Acesso em: 16 ago. 2026.
- RADHITYA, Ridha et al. *Usability Analysis of Digital Signature Application Based on ISO 9241-11 Using Retrospective Think Aloud and User Experience Questionnaire*. 2024. DOI: [10.1109/ICITSI65188.2024.10929447](https://doi.org/10.1109/ICITSI65188.2024.10929447). Acesso em: 15 ago. 2026.
- SCHREPP, Martin; HINDERKS, Andreas; THOMASCHEWSKI, Jörg. *Design and Evaluation of a Short Version of the User Experience Questionnaire (UEQ-S).* *International Journal of Interactive Multimedia and Artificial Intelligence*, v. 4, n. 6, p. 103-108, 2017. DOI: [10.9781/ijimai.2017.09.001](https://doi.org/10.9781/ijimai.2017.09.001). Acesso em: 15 ago. 2026.
- PEOPLECERT. *ITIL 4 Practice Guide: Service Desk*. London, 2023. Disponível em: [guia de prática](https://www.peoplecert.org/browse-certifications/it-governance-and-service-management/ITIL-1/itil4-practices-service-desk-3706). Acesso em: 16 ago. 2026.
- AXELOS. *ITIL 4 Practice Guide: Service Request Management*. London, 2020. Disponível em: [site da ITIL](https://www.axelos.com/best-practice-solutions/itil). Acesso em: 16 ago. 2026.
- BRASIL. *Lei nº 13.709, de 14 de agosto de 2018 — Lei Geral de Proteção de Dados Pessoais (LGPD).* Brasília, DF: Presidência da República, 2018. Disponível em: [texto consolidado](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709compilado.htm). Acesso em: 16 ago. 2026.
- AUTORIDADE NACIONAL DE PROTEÇÃO DE DADOS. *Guia orientativo sobre segurança da informação para agentes de tratamento de pequeno porte.* Brasília, DF, 2021. Disponível em: [guia da ANPD](https://www.gov.br/anpd/pt-br/documentos-e-publicacoes/guia-vf.pdf). Acesso em: 16 ago. 2026.
