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

## 1. Introdução

O marketing digital é uma atividade estratégica para organizações que buscam divulgar produtos e serviços, alcançar públicos específicos e acompanhar resultados com indicadores mensuráveis (Gujar, Paliwal e Panyam, 2024; Younas et al., 2025). No contexto de agências e consultorias, a gestão de tráfego pago envolve ações contínuas em plataformas como Google Ads e Meta Ads, incluindo criação e alteração de campanhas, configuração de públicos, definição de orçamento, aprovação de criativos e análise de métricas. Essas ações costumam atender simultaneamente a vários clientes e exigem coordenação entre atendimento, gestor de tráfego, designer, analista e cliente (Steponaitis e Andrijauskienė, 2023; Agus et al., 2019).

Embora os canais efetivamente utilizados por agências devam ser verificados no levantamento de requisitos, a literatura de Service Desk mostra que o encaminhamento de solicitações diretamente a diferentes pessoas pode produzir múltiplos pontos de contato. Em contraposição, o Service Desk é concebido como ponto único de contato para receber, registrar, analisar, encaminhar e acompanhar solicitações (Firmansyah e Subriadi, 2022; Fenner et al., 2015; Amanullah, Zeki e Abubakar, 2017). Quando a demanda não está centralizada, tornam-se mais difíceis a identificação do pedido original, a definição de responsável, o acompanhamento de prazo, o registro de aprovação e a recuperação das decisões tomadas. Esse cenário pode gerar retrabalho, atrasos, perda de contexto e baixa transparência no atendimento, riscos que as práticas de Service Desk e de gerenciamento de solicitações procuram mitigar por meio de registro, triagem, comunicação e acompanhamento (Firmansyah e Subriadi, 2022; PEOPLECERT, 2023a, 2023b).

Estudos sobre agências de marketing digital apontam planejamento, comunicação, monitoramento, controle, gestão de tempo e análise de dados como competências necessárias à condução de projetos (Steponaitis e Andrijauskienė, 2023). Da mesma forma, plataformas integradas de automação de marketing demonstram o valor de concentrar atividades, indicadores e responsabilidades em um único ambiente (Younas et al., 2025). Esses achados reforçam a necessidade de um sistema de informação que organize o fluxo de trabalho, e não apenas a execução técnica dos anúncios.

Assim, este trabalho propõe o desenvolvimento de uma aplicação web de Help Desk voltada à gestão de demandas de tráfego pago. A aplicação permitirá abrir, classificar, acompanhar, validar e concluir solicitações relacionadas a campanhas, mantendo histórico de mensagens, responsáveis, prazos, prioridades, aprovações e evidências. A finalidade é profissionalizar o atendimento e apoiar o controle operacional das demandas de campanha, substituindo comunicações dispersas por um processo rastreável e mensurável.

## 2. Problemática

### 2.1 Situação-problema

Uma agência de marketing digital pode receber pedidos de diferentes clientes e áreas internas ao mesmo tempo, situação que exige planejamento, comunicação, monitoramento e coordenação (Steponaitis e Andrijauskienė, 2023; Agus et al., 2019). Se as solicitações forem encaminhadas a diferentes pessoas sem um ponto de contato e um registro comum, aumentam as dificuldades para acompanhar responsável, prazo, histórico e andamento do atendimento (Firmansyah e Subriadi, 2022; Fenner et al., 2015). Além disso, alterações em orçamento, público ou criativo podem afetar diretamente o desempenho e o custo de uma campanha, o que exige registro da solicitação, da aprovação e do resultado obtido (Yahia e ElBolok, 2024). Esse cenário evidencia a necessidade de uma estrutura de atendimento que registre a solicitação, permita sua triagem e comunique seu andamento ao solicitante, práticas centrais de Service Desk e de gerenciamento de solicitações de serviço (PEOPLECERT, 2023a, 2023b).

### 2.2 Pergunta de pesquisa

**Como uma aplicação web de Help Desk pode centralizar e rastrear demandas de tráfego pago, melhorando o acompanhamento de campanhas e o atendimento a clientes de agências de marketing digital?**

### 2.3 Consequências do problema

- perda ou duplicidade de solicitações;
- dificuldade de priorizar demandas urgentes;
- falta de definição clara de responsável e prazo;
- ausência de histórico sobre alterações e aprovações;
- retrabalho entre atendimento, tráfego e criação;
- baixa visibilidade para o cliente sobre o andamento do pedido;
- dificuldade de medir volume de demandas, tempo de atendimento e cumprimento de prazos.

## 3. Justificativa

O projeto investiga como centralizar e rastrear demandas de tráfego pago quando elas são encaminhadas a múltiplos pontos de contato. Um Help Desk adaptado ao contexto de tráfego pago pode transformar solicitações em registros estruturados, permitindo acompanhar o ciclo completo de cada pedido — abertura, triagem, execução, validação e encerramento. Essa organização se aproxima das práticas de Service Desk e de gerenciamento de solicitações de serviço da ITIL 4, que enfatizam ponto de contato, registro, comunicação e acompanhamento do atendimento (Firmansyah e Subriadi, 2022; PEOPLECERT, 2023a, 2023b).

Do ponto de vista organizacional, a aplicação pode melhorar a distribuição de trabalho, reduzir retrabalho e facilitar a prestação de contas ao cliente. Para a equipe, o sistema fornecerá uma fila de tarefas, definição de responsáveis, prioridades e prazos. Para o cliente, oferecerá uma visão clara do status da solicitação e do histórico de decisões. Para a gestão, os registros poderão apoiar indicadores como quantidade de demandas por cliente, tempo médio de atendimento, demandas vencidas e tipos de alterações mais solicitadas.

Do ponto de vista acadêmico, o trabalho integra conceitos de Sistemas de Informação, Gestão de Serviços, Gestão de Projetos e Marketing Digital. A proposta permite aplicar conhecimentos de levantamento de requisitos, modelagem de processos, experiência do usuário, banco de dados, desenvolvimento web e validação de software em um problema real de gestão. A pesquisa sobre agências evidencia a importância de planejamento, comunicação e monitoramento; o sistema proposto busca operacionalizar esses elementos no fluxo diário de atendimento.

## 4. Objetivos

### 4.1 Objetivo geral

Desenvolver uma aplicação web de Help Desk para centralizar, acompanhar e registrar demandas de tráfego pago, apoiando a gestão das solicitações e o atendimento a clientes de agências de marketing digital.

### 4.2 Objetivos específicos

Pretende-se realizar uma revisão bibliográfica de artigos e normas aplicáveis para compreender o problema e fundamentar a solução; levantar, analisar e priorizar os requisitos do sistema; modelar o processo de atendimento e definir o backlog com responsabilidades, dependências e prazos de entrega; prototipar a solução; implementar o produto mínimo viável (MVP) com cadastro, autenticação, tickets, acompanhamento de prazos, histórico e comunicação entre os envolvidos; realizar testes funcionais e avaliação de usabilidade; e entregar uma versão final funcional do aplicativo, acompanhada da documentação e da demonstração previstas para o projeto.

## Referências

As referências acadêmicas foram consolidadas em [Etapa 03 - Referencial teorico.md](Etapa%2003%20-%20Referencial%20teorico.md) e em `../../Overleaf/bibliografia.bib`. Esta etapa mantém somente o texto de introdução, problema, justificativa e objetivos.
