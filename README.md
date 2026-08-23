# TI SIGE — organização do repositório

## Fonte acadêmica e documentação

`Trabalho/` está organizado em duas áreas com finalidades distintas:

| Área                                                                               | Finalidade                                                                           | Relação com o Overleaf                                      |
| ---------------------------------------------------------------------------------- | ------------------------------------------------------------------------------------ | ----------------------------------------------------------- |
| [Trabalho/Artigo](Trabalho/Artigo/README.md)                                       | Fonte acadêmica: resumo, introdução, problema, objetivos, referencial e metodologia. | Compõe o texto contínuo de `Overleaf`.                      |
| [Trabalho/Documentacao do Projeto](Trabalho/Documentacao%20do%20Projeto/README.md) | Processo BPMN, protótipo de baixa fidelidade e especificação completa.               | Fornece sínteses para o corpo e detalhes para os apêndices. |
| [Referências](Referencias/README.md)                                               | Referências bibliográficas                                                           |                                                             |

Os arquivos de artigo são complementares, não capítulos para cópia literal. A documentação técnica não deve ser transferida integralmente para o corpo do artigo: requisitos, regras, matrizes e casos de teste ficam na documentação e nos apêndices; o artigo os interpreta e sintetiza.

### Regra de sincronização

Toda alteração acadêmica em `Trabalho/Artigo` deve ser refletida semanticamente em `Overleaf` na mesma tarefa. Toda mudança de processo, protótipo ou especificação em `Trabalho/Documentacao do Projeto` deve atualizar a síntese ou o apêndice correspondente em `Overleaf`, quando afetar o artigo. Quando uma informação já possuir arquivo canônico, use um link para ele em vez de manter cópias concorrentes.

O planejamento interno do desenvolvimento está em [Trabalho/Documentacao do Projeto/BACKLOG.md](Trabalho/Documentacao%20do%20Projeto/BACKLOG.md); ele não integra o artigo.

O [índice de referências e extrações](Referencias/README.md) organiza artigos usados, artigos descartados, normas, legislação e outras fontes.

## Contexto do projeto

### Foco na Gestão de Agências e Consultorias
* *Tema:* Gestão de Demandas de Marketing Digital.
* *Área de Estudo:* Sistemas de Informação / Gestão de Serviços.
* *Título:* Help Desk para Gestão de Demandas de Tráfego Pago.
* *Resumo:* Direciona a aplicação para o gerenciamento de solicitações de clientes de marketing digital. O sistema de Help Desk centralizaria pedidos de criação de anúncios, ajustes de campanhas e análises de métricas em um fluxo registrado, rastreável e profissional.


Pesquisa 01
`(`
  `"Document Title":"digital marketing"`
`)`
`AND`
`(`
  `"Document Title":"marketing automation"`
  `OR "Document Title":"project management"`
  `OR "Document Title":"customer relationship management"`
  `OR "Document Title":"marketing agency"`
  `OR "Document Title":"management system"`
`)`

Pesquisa 02
`(`
  `"Document Title":"digital marketing"`
`)`
`AND`
`(`
  `"Document Title":campaign`
  `OR "Document Title":advertising`
  `OR "Document Title":"paid advertising"`
  `OR "Document Title":"media buying"`
`)`

| Pesquisa | Prioridade | Trabalho | Uso no projeto |
| -------- | ---------- | -------- | -------------- |
| 01 | Alta | _Improving Digital Project Management Competencies: Case Study of a Digital Marketing Agency_ | Gestão de demandas e projetos em agência. |
| 01 | Alta | _Revolutionizing In-House Digital Marketing with End-to-End Marketing Automation..._ | Fluxos, automação e eficiência operacional. |
| 01 | Alta | _DIGI-AI: an AI-Powered Web Platform for Intelligent Digital Marketing Automation_ | Exemplo de plataforma web para campanhas e análises. |
| 01 | Média | _Digital Marketing, Brand Awareness and Customer Relationship Management on Repurchase Intention in E-Commerce Marketplace_ | Relacionamento, transparência e atendimento ao cliente. |
| 02 | Complementar | _A Proposed Nonlinear Programming Optimization Model for Optimal Budget Mix of Digital Marketing Campaigns_ | Orçamento de campanhas. |
| 02 | Complementar | _Impact of Voice-Based Search Queries on Paid Ad Rankings..._ | Anúncios pagos, CPC, CTR e conversão. |
| 02 | Complementar | _Digital Publisher, Advertising Media Agency and Mobile Exchange..._ | Contexto de agência e cadeia de publicidade digital. |
| 02 | Complementar | _Machine Learning-Based Classification of the Traffic of Digital Marketing Campaigns_ | Análise de tráfego, inclusive fraude. |
| 02 | Complementar | _Examining the Ethical Implications of Data Privacy and Targeted Advertising in Digital Marketing_ | Privacidade e uso responsável de dados de campanhas. |


## Cronograma

Fonte: **Trabalho Interdisciplinar — Sistemas Integrados de Gestão Empresarial (TI SIGE), 2/2026**, Prof. Dr. Paulo Isnard. O cumprimento do cronograma integra a avaliação final.

* ***Grupo:**   2 2026 - PBR - TI SIGE 1
* ***Contato:** 
	- Augusto do Couto Avelar - 770279@pucminas.edu.br
	- Gabriel Fernandes de Araújo - 1445492@pucminas.edu.br
	- Gabriel Rocha Lopes - 1460309@pucminas.edu.br
	- Karine Kessen Gilberti Felix - 1448907@pucminas.edu.br
	- Lorrainy Pâmela Siqueira Ferreira - 1452297@pucminas.edu.br
	- Maria Heloisa Aguiar Mendes - 1406560@pucminas.edu.br
	- Nayara Ferreira de Jesus Rodrigues - 720023@pucminas.edu.br
	- Pedro Henrique Assuncao -1478372@pucminas.edu.br
- **Tema / área:** Gestão de Demandas de Marketing Digital — Sistemas de Informação / Gestão de Serviços.
- **Título provisório:** *Help Desk para Gestão de Demandas de Tráfego Pago.*

| Data | Etapa / entrega | Atividades e entregas | Pontos |
| --- | --- | --- | ---: |
| 07/08 | Definição de Grupos | Auto registro no CANVAS.<br>Debate de possibilidades.<br>Tema / Área e Título Provisório. | — |
| 28/08 | 1ª Etapa | Postagem Repositório GitHub (envio link).<br>Tema e Título validado e **INTRODUÇÃO** revisada pelo orientador. | 05 |
| 25/09 | 2ª Etapa | Postagem Repositório GitHub (envio link).<br>**INTRODUÇÃO + PROBLEMÁTICA + JUSTIFICATIVA + OBJETIVOS**.<br>Esboço do protótipo do APP. | 20 |
| 30/10 | 3ª Etapa | Postagem Repositório GitHub (envio link).<br>(anterior +) **REFERENCIAL / MARCO TEÓRICO**.<br>Protótipo do APP + Requisitos. | 20 |
| 27/11 | 4ª Etapa | Postagem Repositório GitHub (envio link).<br>(anterior +) **METODOLOGIA**.<br>Versão inicial do APP. | 20 |
| 11/12 | 5ª Etapa | Postagem Repositório GitHub (envio link).<br>Versão Final do APP.<br>Mostra Tecnológica Interdisciplinar. | 10 |
| 11/12 | APRESENTAÇÃO FINAL — DEMO APP | Versão Final completa revisada pelo orientador.<br>**PADRÃO PUC MINAS**. | 20 (+ 5 pontos) |
