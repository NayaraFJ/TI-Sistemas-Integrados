# Trabalho Interdisciplinar — Sistemas Integrados de Gestão Empresarial

## Etapa 04 — Metodologia

**Tema:** Gestão de Demandas de Marketing Digital  
**Título provisório:** *Help Desk para Gestão de Tráfego Pago: uma Aplicação para Controle de Campanhas e Atendimento a Clientes*

## 1. Caracterização da pesquisa

Este trabalho será desenvolvido como uma pesquisa **aplicada**, pois busca propor e construir uma solução para um problema prático de gestão de demandas em agências de marketing digital. Quanto aos objetivos, a pesquisa terá caráter **exploratório e descritivo**: exploratório por investigar como ocorre a organização de solicitações de tráfego pago e descritivo por registrar necessidades, papéis, fluxos, requisitos e resultados de validação do protótipo.

A abordagem será predominantemente **qualitativa**, voltada a compreender o processo atual, as dificuldades dos usuários e a adequação do sistema. Dados quantitativos simples também poderão ser utilizados na validação, por exemplo quantidade de tarefas concluídas, tempo de execução de um cenário e respostas a questionário de experiência do usuário.

## 2. Procedimentos metodológicos

O desenvolvimento será organizado nas etapas a seguir.

### 2.1 Revisão bibliográfica

Será realizada revisão de trabalhos selecionados na IEEE Xplore sobre gestão de projetos em agências, automação de marketing, CRM, campanhas digitais, métricas de tráfego pago, experiência do usuário e sistemas de gestão de marketing. A revisão já iniciou com buscas por títulos relacionados a *digital marketing*, *marketing automation*, *project management*, *customer relationship management*, *campaign* e *advertising*.

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

As respostas serão organizadas por categorias, como entrada da demanda, priorização, execução, comunicação, aprovação, encerramento e indicadores. Cada requisito será classificado como alta, média ou baixa prioridade para a primeira versão do aplicativo.

### 2.3 Modelagem do processo e do protótipo

Com base nos requisitos validados, será modelado o fluxo de vida do ticket: abertura, triagem, atribuição, execução, espera de cliente, conclusão e cancelamento. Também serão refinados os perfis de usuário, regras de negócio, dados necessários e telas principais.

O protótipo de interface será elaborado de forma incremental. Inicialmente, serão produzidas telas de painel, listagem de tickets, abertura de solicitação e detalhes do ticket. O protótipo deverá evidenciar os principais caminhos do usuário e permitir ajustes antes da implementação da aplicação.

### 2.4 Desenvolvimento da aplicação

A versão inicial será desenvolvida como aplicação web. A implementação seguirá ciclos curtos: selecionar requisitos priorizados, implementar, testar o cenário correspondente e registrar eventuais ajustes. O foco inicial será garantir cadastro, autenticação, tickets, comentários, histórico, status, prazos, responsáveis e filtros.

Integrações automáticas com plataformas de anúncios, previsão por inteligência artificial e automação de alterações em campanhas não serão implementadas no MVP. Essa delimitação permitirá avaliar primeiro o valor do fluxo de atendimento e rastreabilidade.

### 2.5 Testes funcionais

Serão elaborados casos de teste a partir dos critérios de aceite dos requisitos. Cada caso deverá informar pré-condição, passos de execução, resultado esperado, resultado obtido e situação final.

Os cenários mínimos previstos são:

| ID | Cenário de teste | Resultado esperado |
| --- | --- | --- |
| CT-01 | Cliente abre ticket de alteração de campanha. | Ticket recebe identificador e status inicial. |
| CT-02 | Atendimento classifica e atribui o ticket. | Prioridade, prazo e responsável ficam registrados. |
| CT-03 | Gestor atualiza status e inclui comentário. | Histórico registra usuário, data/hora e alteração. |
| CT-04 | Ticket aguarda aprovação do cliente. | O status e a solicitação de aprovação ficam visíveis. |
| CT-05 | Responsável conclui o ticket com evidência. | Ação executada e evidência ficam disponíveis no histórico. |
| CT-06 | Usuário filtra demandas vencidas. | A listagem retorna apenas tickets com prazo ultrapassado. |

### 2.6 Avaliação de usabilidade

Após a versão inicial, o protótipo ou aplicação será submetido a uma avaliação de usabilidade com um pequeno grupo de potenciais usuários. A amostra será por conveniência e deverá incluir, sempre que possível, representantes dos perfis de cliente, atendimento e gestor de tráfego.

Os participantes executarão tarefas como abrir uma demanda, localizar um ticket, atualizar o status, inserir um comentário e concluir uma solicitação. Serão observados obstáculos, dúvidas, erros e tempo de realização. Ao final, será aplicado questionário breve, preferencialmente o **Short User Experience Questionnaire (UEQ-S)**, que avalia atratividade, qualidade pragmática e qualidade hedônica.

Os resultados serão apresentados de forma agregada, sem identificação individual. Sugestões e dificuldades recorrentes serão transformadas em lista de melhorias para a versão final.

## 3. Participantes e dados

Os participantes serão convidados por conveniência entre pessoas que possuam experiência com atendimento, gestão de campanhas ou contratação de serviços de marketing digital. A participação será voluntária e ocorrerá apenas após explicação do objetivo do teste.

O projeto coletará somente dados necessários para a pesquisa e para o funcionamento do protótipo, como perfil de usuário, respostas ao questionário e registros fictícios de tickets. Não serão solicitadas credenciais de plataformas de anúncio, dados bancários, bases de audiência ou informações pessoais sensíveis.

## 4. Análise dos dados

Os dados qualitativos das entrevistas, observações e comentários serão analisados por categorização temática. As categorias iniciais serão: problemas do processo atual, informações necessárias no ticket, comunicação, prioridade, prazo, aprovação, evidências e indicadores.

Os dados quantitativos de teste serão analisados de forma descritiva, considerando:

- quantidade de casos de teste aprovados e reprovados;
- quantidade de tarefas concluídas por participante;
- principais erros ou dúvidas observados;
- tempo aproximado de execução das tarefas;
- escores e comentários do UEQ-S, quando aplicado.

Os resultados não terão pretensão de representar todas as agências de marketing. Eles servirão para verificar se o protótipo atende ao problema e para orientar melhorias na aplicação.

## 5. Critérios de qualidade e validade

Para reduzir ambiguidades, os requisitos serão associados a critérios de aceite e casos de teste. O registro de histórico do ticket permitirá verificar se as regras de negócio foram atendidas. A validação com usuários complementará os testes técnicos, pois uma função pode estar correta do ponto de vista de software, mas ser difícil de usar.

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
