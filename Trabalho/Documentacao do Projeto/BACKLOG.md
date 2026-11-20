# Backlog do SIGE Desk

Este é o backlog operacional do grupo. Ele converte o [cronograma oficial](../../README.md#cronograma) em marcos de entrega e usa como fontes técnicas o [levantamento de requisitos](Levantamento%20de%20requisitos.md), o [processo](Processo/BPMN%20-%20Processo%20To%20Be.md), o [protótipo](Prototipo/Prototipo%20especificação.md) e a [especificação](Especificacao/Especificacao%20do%20sistema.md).

As atribuições iniciais representam afinidade e coordenação, não exclusividade: Pedro Henrique Assuncao atua no eixo de back-end; Karine Kessen Gilberti Felix, no de banco de dados; Lorrainy Pâmela Siqueira Ferreira e Maria Heloisa Aguiar Mendes, no de front-end; e Nayara Ferreira de Jesus Rodrigues, na documentação, no papel de Product Owner (PO) e na interface com a orientação. Cada entrega deve ter revisão de pelo menos outro integrante.

**Revisão de situação: 20/11/2026.** A implementação e os ensaios já existem. “Implementado” ou “Verificado” descrevem evidência técnica, sem pressupor aceite do grupo ou postagem no ambiente acadêmico. Não foram inferidas datas retroativas de conclusão. As [17 fichas funcionais](Testes%20funcionais/README.md) consolidam 17 aprovações, incluindo 11/11 altas; CT-15 foi concluído com capturas e confronto visual em 20/11/2026. A usabilidade tem [roteiro de aplicação](Usabilidade/ROTEIRO.md), ainda sem coleta realizada.

## Integrantes e atribuição inicial

| Integrante                         | Atribuição inicial                                                   |
| ---------------------------------- | -------------------------------------------------------------------- |
| Augusto do Couto Avelar            | Revisor                                                              |
| Gabriel Fernandes de Araújo        |                                                                      |
| Gabriel Rocha Lopes                |                                                                      |
| Karine Kessen Gilberti Felix       | Banco de dados; modelo relacional, migrações e dados de demonstração |
| Lorrainy Pâmela Siqueira Ferreira  | Front-end                                                            |
| Maria Heloisa Aguiar Mendes        | Front-end                                                            |
| Nayara Ferreira de Jesus Rodrigues | Documentação; PO (priorização e aceite); interface com a orientação  |
| Pedro Henrique Assuncao            | Back-end                                                             |

## Frentes e responsáveis

As frentes abaixo organizam a distribuição de trabalho entre os oito integrantes. Os campos em branco permanecem abertos até decisão do grupo.

| Frente | Escopo principal | Titular |
| --- | --- | --- |
| Processo e requisitos | Derivação do As Is e do To Be a partir do referencial, requisitos e critérios de aceite. | |
| Revisão acadêmica do artigo | Revisão cruzada de coerência, estrutura, citações, referências, linguagem e padrão PUC Minas antes de cada etapa. | |
| Dados e modelo relacional | Entidades, relacionamentos, restrições, migrações e dados de demonstração. | Karine Kessen Gilberti Felix |
| Interface e front-end | Estrutura visual, navegação, componentes, responsividade e telas. | Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes |
| Contratos e integração | Contrato de API, mocks e compatibilidade entre front-end e back-end. | |
| Qualidade e testes | Casos de teste, defeitos, retestes, segurança e avaliação de usabilidade. | |
| Entrega e demonstração | Release, instruções de execução, apresentação, Mostra e plano de contingência. | |

## Como usar

- `A fazer`: ainda não iniciado; `Em andamento`: possui responsável e trabalho iniciado; `Em revisão`: artefato existente, mas ainda pendente de validação do grupo ou do orientador; `Concluído`: entrega aceita pelo grupo.
- Uma tarefa só recebe responsável quando a pessoa concordar com a atribuição.
- Uma tarefa concluída deve conter link, captura, registro de teste ou outro artefato verificável.
- `Conclusão prevista` deriva do marco oficial. `Concluído em` só deve ser preenchido quando houver aceite; não invente datas retroativas para artefatos já existentes.
- Se uma tarefa comprometer um marco, registre o impedimento e redefina sua prioridade na reunião do grupo.
- O revisor do artigo não substitui a autoria: quem redige revisa a versão seguinte de outra pessoa. A revisão acadêmica deve ocorrer antes de cada postagem e antes do envio ao orientador.

## Marcos oficiais

| Marco | Data | Critério de saída |
| --- | --- | --- |
| Definição de Grupos | 07/08/2026 | Grupo, tema / área e título provisório definidos. |
| 1ª Etapa | 28/08/2026 | Repositório enviado; tema e título validados; introdução revisada pelo orientador. |
| 2ª Etapa | 25/09/2026 | Introdução, problemática, justificativa, objetivos e esboço do protótipo entregues. |
| 3ª Etapa | 30/10/2026 | Referencial / marco teórico, protótipo e requisitos entregues. |
| 4ª Etapa | 27/11/2026 | Metodologia e versão inicial do APP entregues. |
| 5ª Etapa | 11/12/2026 | Versão final do APP e participação na Mostra Tecnológica Interdisciplinar. |
| Apresentação final — demo APP | 11/12/2026 | Demo pronta; versão final revisada pelo orientador no padrão PUC Minas. |

## Backlog por marco

### Base e 1ª Etapa — até 28/08

| ID    | Item                                                 | Entrega verificável                                                                | Dependência | Conclusão prevista | Concluído em | Responsável                                                           | Situação     |
| ----- | ---------------------------------------------------- | ---------------------------------------------------------------------------------- | ----------- | ------------------ | ------------ | --------------------------------------------------------------------- | ------------ |
| BL-01 | Confirmar tema, área e título com o orientador. | Registro da validação e título vigente no README. | — | 28/08/2026 |  | Nayara Ferreira de Jesus Rodrigues | Em andamento |
| BL-02 | Revisar a introdução conforme retorno do orientador. | Texto revisado em `Trabalho/Artigo` e no Overleaf, com revisão cruzada registrada. | BL-01 | 28/08/2026 |  | Nayara Ferreira de Jesus Rodrigues; Augusto do Couto Avelar (revisor) | Em revisão |
| BL-03 | Preparar o envio do link do repositório. | Link acessível e arquivos organizados. | BL-01 | 28/08/2026 |  | Nayara Ferreira de Jesus Rodrigues | Em revisão — envio não comprovado |

### 2ª Etapa — até 25/09

| ID    | Item                                                                                | Entrega verificável                                                                     | Dependência  | Conclusão prevista | Concluído em | Responsável                                                           | Situação     |
| ----- | ----------------------------------------------------------------------------------- | --------------------------------------------------------------------------------------- | ------------ | ------------------ | ------------ | --------------------------------------------------------------------- | ------------ |
| BL-04 | Consolidar introdução, problemática, justificativa e objetivos. | Seções coerentes no artigo. | BL-02 | 25/09/2026 |  | Nayara Ferreira de Jesus Rodrigues; Augusto do Couto Avelar (revisor) | Em revisão |
| BL-05 | Consolidar o processo analítico (As Is) e os atores do MVP a partir do referencial. | Fluxo As Is, papéis, evidências e limitações registrados no levantamento de requisitos. | BL-04 | 25/09/2026 |  |  | Documentado |
| BL-06 | Refinar o esboço de baixa fidelidade. | Painel, abertura de ticket e detalhe do ticket documentados. | BL-04 | 25/09/2026 |  | Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes | Em revisão |
| BL-07 | Revisar o fluxo To Be com base no problema e no esboço. | BPMN atualizado e legível. | BL-05, BL-06 | 25/09/2026 |  |  | Documentado |

### 3ª Etapa — até 30/10

| ID    | Item                                                   | Entrega verificável                                                  | Dependência   | Conclusão prevista | Concluído em | Responsável                                                           | Situação   |
| ----- | ------------------------------------------------------ | -------------------------------------------------------------------- | ------------- | ------------------ | ------------ | --------------------------------------------------------------------- | ---------- |
| BL-08 | Concluir o referencial / marco teórico. | Fontes selecionadas, citações e referências consolidadas. | BL-04 | 30/10/2026 |  | Nayara Ferreira de Jesus Rodrigues; Augusto do Couto Avelar (revisor) | Em revisão |
| BL-09 | Derivar e priorizar requisitos do MVP. | Requisitos, regras, critérios de aceite e rastreabilidade revisados. | BL-05, BL-08 | 30/10/2026 |  | Nayara Ferreira de Jesus Rodrigues | Documentado |
| BL-10 | Refinar o BPMN To Be e alinhá-lo aos requisitos. | Fluxo, papéis, estados e regras consistentes. | BL-07, BL-09 | 30/10/2026 |  |  | Documentado |
| BL-11 | Revisar protótipo contra requisitos prioritários. | Cada tela cobre os caminhos prioritários. | BL-06, BL-09 | 30/10/2026 |  | Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes | Documentado |
| BL-12 | Definir titulares para as cinco frentes ainda abertas. | Matriz de responsabilidades preenchida e aceita pelo grupo. | — | 30/10/2026 |  | Nayara Ferreira de Jesus Rodrigues | A fazer |
| BL-13 | Preparar a entrega da 3ª Etapa. | Repositório atualizado e link enviado. | BL-08 a BL-12 | 30/10/2026 |  | Nayara Ferreira de Jesus Rodrigues | A fazer |

### 4ª Etapa — até 27/11

| ID    | Item                                                                     | Entrega verificável                                                                                    | Dependência         | Conclusão prevista | Concluído em | Responsável                                                                                                           | Situação   |
| ----- | ------------------------------------------------------------------------ | ------------------------------------------------------------------------------------------------------ | ------------------- | ------------------ | ------------ | --------------------------------------------------------------------------------------------------------------------- | ---------- |
| BL-14 | Consolidar metodologia e plano de validação. | Metodologia coerente com o estado real do projeto. | BL-09 | 27/11/2026 |  | Nayara Ferreira de Jesus Rodrigues; Augusto do Couto Avelar (revisor) | Em revisão |
| BL-15 | Definir arquitetura, tecnologias, ambiente e convenções do APP. | [Decisões técnicas](../../SIGE-Desk/docs/decisoes.md) e [aplicação executável](../../SIGE-Desk/README.md). | BL-09 | 27/11/2026 |  | Pedro Henrique Assuncao | Implementado |
| BL-16 | Criar o modelo relacional e as migrações iniciais. | [Migrations e código](../../SIGE-Desk/Backend/src/main/resources/db/migration); conferir apresentação do diagrama com o grupo. | BL-09, BL-15 | 27/11/2026 |  | Karine Kessen Gilberti Felix | Implementado |
| BL-17 | Criar e versionar o contrato entre front-end e back-end. | [Contrato OpenAPI versionado](../../SIGE-Desk/docs/openapi.yaml), [guia](../../SIGE-Desk/docs/openapi.md) e tipos do frontend. | BL-09, BL-15 | 27/11/2026 |  | Pedro Henrique Assuncao; Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes | Implementado |
| BL-18 | Criar mocks e cenários de integração do contrato de API. | [Contrato e tipos gerados](../../SIGE-Desk/docs/openapi.md); integração por API real e fixtures QA; mocks independentes não comprovados. | BL-17 | 27/11/2026 |  | Pedro Henrique Assuncao; Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes | Em revisão — API real integrada; mocks não comprovados |
| BL-19 | Implementar autenticação, autorização por perfil e auditoria básica. | [CT-08](Testes%20funcionais/CT-08.md) e [CT-11](Testes%20funcionais/CT-11.md). | BL-15, BL-16, BL-17 | 27/11/2026 |  | Pedro Henrique Assuncao; Karine Kessen Gilberti Felix | Verificado no escopo QA |
| BL-20 | Implementar cadastros e o fluxo principal de tickets no back-end. | [CT-01 a CT-14](Testes%20funcionais/README.md): cadastros e fluxo exercitados. | BL-16, BL-17, BL-19 | 27/11/2026 |  | Pedro Henrique Assuncao; Karine Kessen Gilberti Felix | Verificado no escopo QA |
| BL-21 | Implementar estrutura visual, navegação e telas principais no front-end. | [Frontend](../../SIGE-Desk/Frontend/README.md) e [observações de interface](../../SIGE-Desk/docs/qa/relatorio-interface.md). | BL-11, BL-15, BL-18 | 27/11/2026 |  | Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes | Implementado |
| BL-22 | Integrar front-end, API e persistência no caminho principal. | [Fichas de execução](Testes%20funcionais/README.md) e [guia demo](../../SIGE-Desk/Backend/VALIDACAO-DEMO.md). | BL-20, BL-21 | 27/11/2026 |  | Pedro Henrique Assuncao; Karine Kessen Gilberti Felix; Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes | Verificado no escopo QA |
| BL-23 | Preparar dados fictícios e roteiro de demonstração da versão inicial. | [Dados e roteiro demo](../../SIGE-Desk/Backend/VALIDACAO-DEMO.md); ensaio do grupo pendente. | BL-16, BL-22 | 27/11/2026 |  |  | Implementado — ensaio da demo pendente |
| BL-24 | Preparar versão inicial e entrega da 4ª Etapa. | Repositório atualizado, instruções de execução e demonstração funcional. | BL-14 a BL-23 | 27/11/2026 |  | Nayara Ferreira de Jesus Rodrigues | Em revisão — entrega não comprovada |

### 5ª Etapa e apresentação final — até 11/12

| ID    | Item                                                                        | Entrega verificável                                                                                                                                                     | Dependência          | Conclusão prevista | Concluído em | Responsável                                                    | Situação |
| ----- | --------------------------------------------------------------------------- | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------- | -------------------- | ------------------ | ------------ | -------------------------------------------------------------- | -------- |
| BL-25 | Implementar aprovação, reabertura, evidências e notificações prioritárias. | [CT-03](Testes%20funcionais/CT-03.md), [CT-05](Testes%20funcionais/CT-05.md), [CT-07](Testes%20funcionais/CT-07.md). | BL-20, BL-22 | 11/12/2026 |  |  | Verificado no escopo QA |
| BL-26 | Implementar filtros, painel e indicação de vencimento de prazo. | [CT-06 aprovado](Testes%20funcionais/CT-06.md) e [CT-15 aprovado](Testes%20funcionais/CT-15.md). | BL-20, BL-22 | 11/12/2026 |  | Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes | Verificado no escopo QA |
| BL-27 | Implementar segurança e controles de qualidade definidos. | [CT-08/11](Testes%20funcionais/README.md) e [limites da revisão](../../SIGE-Desk/docs/qa/decisao-defeitos-atual.md); não equivale a pentest, acessibilidade ou compatibilidade completas. | BL-19, BL-20 | 11/12/2026 |  |  | Verificado parcialmente — cobertura RNF limitada |
| BL-28 | Executar casos de teste, registrar defeitos e realizar retestes. | [17 fichas com ciclos e evidências](Testes%20funcionais/README.md); 17/17 aprovados e 11/11 altas; CT-15 concluído em 20/11/2026. | BL-22, BL-25 a BL-27 | 11/12/2026 |  |  | Critério interno atendido — 17/17 |
| BL-29 | Realizar avaliação de usabilidade e registrar melhorias. | [Roteiro](Usabilidade/ROTEIRO.md), [instruções aos participantes](Usabilidade/PARTICIPANTE.md) e [ficha de coleta](Usabilidade/FICHA-DE-SESSAO.md); aplicação pendente. | BL-22 | 11/12/2026 |  |  | Planejado — avaliação interna pendente |
| BL-30 | Preparar release, instruções de execução e contingência da demonstração. | [Execução local](../../SIGE-Desk/README.md) e [registro de publicação](../../SIGE-Desk/docs/publicacao-aws.md); confirmar versão da demo e contingência antes da apresentação. | BL-23, BL-28 | 11/12/2026 |  |  | Em revisão — contingência e release pendentes |
| BL-31 | Consolidar artigo, documentação de apoio e referências no padrão PUC Minas. | Versão revisada pelo orientador e pronta para submissão. | BL-28, BL-29 | 11/12/2026 |  | Nayara Ferreira de Jesus Rodrigues | Em revisão |
| BL-32 | Preparar apresentação, roteiro e divisão de fala da demo. | Apresentação ensaiada, dados de demonstração e responsáveis por cada parte. | BL-30, BL-31 | 11/12/2026 |  |  | A fazer |
| BL-33 | Participar da Mostra Tecnológica Interdisciplinar e da apresentação final. | APP final, apresentação e demo executada. | BL-32 | 11/12/2026 |  |  | A fazer |
| BL-34 | Realizar a revisão cruzada final do artigo. | Checklist de coerência entre etapas, Overleaf, citações, referências, documentação de apoio, resumo, abstract e padrão PUC Minas; pendências corrigidas ou registradas. | BL-31 | 11/12/2026 |  | Augusto do Couto Avelar (revisor) | Pendente de revisão humana |

## Contrato de integração front-end e back-end

O contrato já está versionado em [openapi.yaml](../../SIGE-Desk/docs/openapi.yaml), com [procedimento de sincronização](../../SIGE-Desk/docs/openapi.md). A integração real substituiu a dependência operacional de mocks para os fluxos já verificados; a existência de uma suíte independente de mocks de BL-18 não foi comprovada. A revisão do contrato deve conferir:

1. autenticação e perfis autorizados por rota;
2. recursos de usuário, cliente, campanha, tipo de demanda, ticket, comentário, anexo, evidência, aprovação e histórico;
3. formato dos dados, campos obrigatórios, paginação, filtros e ordenação;
4. códigos de resposta e mensagens de erro;
5. exemplos de requisição e resposta, incluindo erros de permissão;
6. regra de versionamento: qualquer alteração incompatível exige atualização simultânea de contrato, mock, back-end e front-end.

## Itens fora do escopo do MVP

Estes itens permanecem fora do backlog de entrega, salvo decisão explícita do grupo: integração automática com Google Ads e Meta Ads, execução automática de mudanças de campanha, recomendações por inteligência artificial, cálculo automático de métricas, faturamento e chat externo ao histórico do ticket.

## Próximas ações de fechamento

1. Revisar o fechamento de CT-15, suas capturas e confronto do dataset na ficha [CT-15](Testes%20funcionais/CT-15.md); execução concluída em 20/11/2026.
2. Revisar as fichas sorteadas e registrar aceite humano, sem substituir a autoria real dos ensaios.
3. Conferir o roteiro e realizar a avaliação de usabilidade entre os integrantes do grupo, registrando papéis e familiaridade.
4. Analisar os dados da avaliação interna por papel simulado, explicitar limitações e registrar melhorias e retestes.
5. Ensaiar demo, conferir release, contingência, artigo e envio acadêmico.

O roteiro não autoriza afirmar resultados de usabilidade antes da coleta. A manutenção em dados legados com snapshots sobrescritos depende de fonte histórica e não está encerrada pela aprovação prospectiva de CT-09.
