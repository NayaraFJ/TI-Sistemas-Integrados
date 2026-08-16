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

O marketing digital é uma atividade estratégica para organizações que buscam divulgar produtos e serviços, alcançar públicos específicos e acompanhar resultados com indicadores mensuráveis. No contexto de agências e consultorias, a gestão de tráfego pago envolve ações contínuas em plataformas como Google Ads e Meta Ads, incluindo criação e alteração de campanhas, configuração de públicos, definição de orçamento, aprovação de criativos e análise de métricas. Essas ações costumam atender simultaneamente a vários clientes e exigem coordenação entre atendimento, gestor de tráfego, designer, analista e cliente.

Apesar da importância desse processo, é comum que as demandas sejam registradas por meios informais, como WhatsApp, e-mail e mensagens diretas. Essa prática torna difícil identificar o pedido original, definir o responsável, acompanhar o prazo, registrar aprovações e recuperar o histórico de mudanças feitas em uma campanha. Como consequência, aumentam os riscos de retrabalho, atrasos, perda de informações, execução de alterações sem aprovação e falta de transparência no atendimento ao cliente.

Estudos sobre agências de marketing digital apontam planejamento, comunicação, monitoramento, controle, gestão de tempo e análise de dados como competências necessárias à condução de projetos (Steponaitis e Andrijauskienė, 2023). Da mesma forma, plataformas integradas de automação de marketing demonstram o valor de concentrar atividades, indicadores e responsabilidades em um único ambiente (Younas et al., 2025). Esses achados reforçam a necessidade de um sistema de informação que organize o fluxo de trabalho, e não apenas a execução técnica dos anúncios.

Assim, este trabalho propõe o desenvolvimento de uma aplicação web de Help Desk voltada à gestão de demandas de tráfego pago. A aplicação permitirá abrir, classificar, acompanhar, validar e concluir solicitações relacionadas a campanhas, mantendo histórico de mensagens, responsáveis, prazos, prioridades, aprovações e evidências. A finalidade é profissionalizar o atendimento e apoiar o controle operacional das demandas de campanha, substituindo comunicações dispersas por um processo rastreável e mensurável.

## 2. Problemática

### 2.1 Situação-problema

Uma agência de marketing digital pode receber pedidos de diferentes clientes e áreas internas ao mesmo tempo. Sem um fluxo centralizado, a solicitação pode ser perdida em conversas, repassada sem contexto ou executada sem que o cliente consiga acompanhar sua situação. Além disso, alterações em orçamento, público ou criativo podem afetar diretamente o desempenho e o custo de uma campanha, o que exige registro da solicitação, da aprovação e do resultado obtido. Esse cenário evidencia a necessidade de uma estrutura de atendimento que registre a solicitação, permita sua triagem e comunique seu andamento ao solicitante, práticas centrais de Service Desk e de gerenciamento de solicitações de serviço (PEOPLECERT, 2023a, 2023b).

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

O projeto é relevante porque trata de um problema frequente em serviços de marketing digital: a gestão de demandas que chega por canais informais. Um Help Desk adaptado ao contexto de tráfego pago pode transformar mensagens avulsas em registros estruturados, permitindo acompanhar o ciclo completo de cada pedido — abertura, triagem, execução, validação e encerramento. Essa organização se aproxima das práticas de Service Desk e de gerenciamento de solicitações de serviço da ITIL 4, que enfatizam ponto de contato, registro, comunicação e acompanhamento do atendimento (PEOPLECERT, 2023a, 2023b).

Do ponto de vista organizacional, a aplicação pode melhorar a distribuição de trabalho, reduzir retrabalho e facilitar a prestação de contas ao cliente. Para a equipe, o sistema fornecerá uma fila de tarefas, definição de responsáveis, prioridades e prazos. Para o cliente, oferecerá uma visão clara do status da solicitação e do histórico de decisões. Para a gestão, os registros poderão apoiar indicadores como quantidade de demandas por cliente, tempo médio de atendimento, demandas vencidas e tipos de alterações mais solicitadas.

Do ponto de vista acadêmico, o trabalho integra conceitos de Sistemas de Informação, Gestão de Serviços, Gestão de Projetos e Marketing Digital. A proposta permite aplicar conhecimentos de levantamento de requisitos, modelagem de processos, experiência do usuário, banco de dados, desenvolvimento web e validação de software em um problema real de gestão. A pesquisa sobre agências evidencia a importância de planejamento, comunicação e monitoramento; o sistema proposto busca operacionalizar esses elementos no fluxo diário de atendimento.

## 4. Objetivos

### 4.1 Objetivo geral

Desenvolver uma aplicação web de Help Desk para centralizar, acompanhar e registrar demandas de tráfego pago, apoiando a gestão das solicitações e o atendimento a clientes de agências de marketing digital.

### 4.2 Objetivos específicos

Pretende-se realizar uma revisão bibliográfica de artigos e normas aplicáveis para compreender o problema e fundamentar a solução; levantar, analisar e priorizar os requisitos do sistema; modelar o processo de atendimento e definir o backlog com responsabilidades, dependências e prazos de entrega; prototipar a solução; implementar o produto mínimo viável (MVP) com cadastro, autenticação, tickets, acompanhamento de prazos, histórico e comunicação entre os envolvidos; realizar testes funcionais e avaliação de usabilidade; e entregar uma versão final funcional do aplicativo, acompanhada da documentação e da demonstração previstas para o projeto.

## Referências

As referências acadêmicas foram consolidadas em [Etapa 03 - Referencial teorico.md](Etapa%2003%20-%20Referencial%20teorico.md) e em `../../Overleaf/bibliografia.bib`. Esta etapa mantém somente o texto de introdução, problema, justificativa e objetivos.
