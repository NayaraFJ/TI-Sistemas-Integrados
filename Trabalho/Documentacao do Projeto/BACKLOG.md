# Backlog do SIGE Desk

Este é o backlog operacional do grupo. Ele converte o [cronograma oficial](../../README.md#cronograma) em marcos de entrega e usa como fontes técnicas o [levantamento de requisitos](Levantamento%20de%20requisitos.md), o [processo](Processo/BPMN%20-%20Processo%20To%20Be.md), o [protótipo](Prototipo/Prototipo%20de%20baixa%20fidelidade.md) e a [especificação](Especificacao/Especificacao%20do%20sistema.md).

As atribuições iniciais representam afinidade e coordenação, não exclusividade: Pedro Henrique Assuncao atua no eixo de back-end; Lorrainy Pâmela Siqueira Ferreira, no de front-end; e Nayara Ferreira de Jesus Rodrigues, na documentação, no papel de Product Owner (PO) e na interface com a orientação. Cada entrega deve ter revisão de pelo menos outro integrante.

## Integrantes e atribuição inicial

| Integrante                         | Atribuição inicial                                                  |
| ---------------------------------- | ------------------------------------------------------------------- |
| Augusto do Couto Avelar            |                                                                     |
| Gabriel Fernandes de Araújo        |                                                                     |
| Gabriel Rocha Lopes                |                                                                     |
| Karine Kessen Gilberti Felix       |                                                                     |
| Lorrainy Pâmela Siqueira Ferreira  | Front-end                                                           |
| Maria Heloisa Aguiar Mendes        |                                                                     |
| Nayara Ferreira de Jesus Rodrigues | Documentação; PO (priorização e aceite); interface com a orientação |
| Pedro Henrique Assuncao            | Back-end                                                            |

## Frentes que ainda precisam de titular

As cinco frentes abaixo existem para distribuir trabalho entre os oito integrantes. Os campos permanecem em branco até o grupo decidir quem assume cada uma.

| Frente | Escopo principal | Titular |
| --- | --- | --- |
| Processo e requisitos | Levantamento As Is, validação do To Be, requisitos e critérios de aceite. | |
| Revisão acadêmica do artigo | Revisão cruzada de coerência, estrutura, citações, referências, linguagem e padrão PUC Minas antes de cada etapa. | |
| Dados e modelo relacional | Entidades, relacionamentos, restrições, migrações e dados de demonstração. | |
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

| ID | Item | Entrega verificável | Dependência | Conclusão prevista | Concluído em | Responsável | Situação |
| --- | --- | --- | --- | --- | --- | --- | --- |
| BL-01 | Confirmar tema, área e título com o orientador. | Registro da validação e título vigente no README. | — | 28/08/2026 | | Nayara Ferreira de Jesus Rodrigues | Em andamento |
| BL-02 | Revisar a introdução conforme retorno do orientador. | Texto revisado em `Trabalho/Artigo` e no Overleaf, com revisão cruzada registrada. | BL-01 | 28/08/2026 | | Nayara Ferreira de Jesus Rodrigues; revisor do artigo a definir | Em revisão |
| BL-03 | Preparar o envio do link do repositório. | Link acessível e arquivos organizados. | BL-01 | 28/08/2026 | | Nayara Ferreira de Jesus Rodrigues | A fazer |

### 2ª Etapa — até 25/09

| ID    | Item                                                            | Entrega verificável                                          | Dependência  | Conclusão prevista | Concluído em | Responsável                        | Situação   |
| ----- | --------------------------------------------------------------- | ------------------------------------------------------------ | ------------ | ------------------ | ------------ | ---------------------------------- | ---------- |
| BL-04 | Consolidar introdução, problemática, justificativa e objetivos. | Seções coerentes no artigo.                                  | BL-02        | 25/09/2026         |              | Nayara Ferreira de Jesus Rodrigues | Em revisão |
| BL-05 | Validar o processo atual (As Is) e os atores do MVP com potenciais usuários. | Fluxo As Is enxuto, papéis e hipóteses validados ou corrigidos e registro das respostas no levantamento de requisitos. | BL-04        | 25/09/2026         |              |                                    | Em andamento |
| BL-06 | Refinar o esboço de baixa fidelidade.                           | Painel, abertura de ticket e detalhe do ticket documentados. | BL-04        | 25/09/2026         |              | Lorrainy Pâmela Siqueira Ferreira  | Em revisão |
| BL-07 | Revisar o fluxo To Be com base no problema e no esboço.         | BPMN atualizado e legível.                                   | BL-05, BL-06 | 25/09/2026         |              |                                    | Em revisão |

### 3ª Etapa — até 30/10

| ID | Item | Entrega verificável | Dependência | Conclusão prevista | Concluído em | Responsável | Situação |
| --- | --- | --- | --- | --- | --- | --- | --- |
| BL-08 | Concluir o referencial / marco teórico. | Fontes selecionadas, citações e referências consolidadas. | BL-04 | 30/10/2026 | | Nayara Ferreira de Jesus Rodrigues | Em revisão |
| BL-09 | Validar e priorizar requisitos do MVP. | Requisitos, regras, critérios de aceite e rastreabilidade revisados. | BL-05, BL-08 | 30/10/2026 | | Nayara Ferreira de Jesus Rodrigues | Em revisão |
| BL-10 | Refinar o BPMN To Be e alinhá-lo aos requisitos. | Fluxo, papéis, estados e regras consistentes. | BL-07, BL-09 | 30/10/2026 | | | Em revisão |
| BL-11 | Revisar protótipo contra requisitos prioritários. | Cada tela cobre os caminhos prioritários. | BL-06, BL-09 | 30/10/2026 | | Lorrainy Pâmela Siqueira Ferreira | Em revisão |
| BL-12 | Definir titulares para as cinco frentes ainda abertas. | Matriz de responsabilidades preenchida e aceita pelo grupo. | — | 30/10/2026 | | Nayara Ferreira de Jesus Rodrigues | A fazer |
| BL-13 | Preparar a entrega da 3ª Etapa. | Repositório atualizado e link enviado. | BL-08 a BL-12 | 30/10/2026 | | | A fazer |

### 4ª Etapa — até 27/11

| ID | Item | Entrega verificável | Dependência | Conclusão prevista | Concluído em | Responsável | Situação |
| --- | --- | --- | --- | --- | --- | --- | --- |
| BL-14 | Consolidar metodologia e plano de validação. | Metodologia coerente com o estado real do projeto. | BL-09 | 27/11/2026 | | Nayara Ferreira de Jesus Rodrigues | Em revisão |
| BL-15 | Definir arquitetura, tecnologias, ambiente e convenções do APP. | Documento de decisões técnicas e projeto inicial executável. | BL-09 | 27/11/2026 | | Pedro Henrique Assuncao | A fazer |
| BL-16 | Criar o modelo relacional e as migrações iniciais. | Diagrama/modelo com entidades, chaves, relacionamentos, restrições e script de criação. | BL-09, BL-15 | 27/11/2026 | | Pedro Henrique Assuncao | A fazer |
| BL-17 | Criar e versionar o contrato entre front-end e back-end. | Especificação de API com autenticação, rotas, parâmetros, corpos, respostas, erros e exemplos. | BL-09, BL-15 | 27/11/2026 | | Pedro Henrique Assuncao e Lorrainy Pâmela Siqueira Ferreira | A fazer |
| BL-18 | Criar mocks e cenários de integração do contrato de API. | Dados de exemplo e casos de sucesso/erro consumíveis pelo front-end antes da API final. | BL-17 | 27/11/2026 | | Pedro Henrique Assuncao e Lorrainy Pâmela Siqueira Ferreira | A fazer |
| BL-19 | Implementar autenticação, autorização por perfil e auditoria básica. | Usuários autenticados acessam somente funções e tickets autorizados. | BL-15, BL-16, BL-17 | 27/11/2026 | | Pedro Henrique Assuncao | A fazer |
| BL-20 | Implementar cadastros e o fluxo principal de tickets no back-end. | Clientes, campanhas, tipos de demanda, abertura, triagem, atribuição, estados e histórico persistidos. | BL-16, BL-17, BL-19 | 27/11/2026 | | Pedro Henrique Assuncao | A fazer |
| BL-21 | Implementar estrutura visual, navegação e telas principais no front-end. | Interface responsiva para autenticação, painel, lista, abertura e detalhe. | BL-11, BL-15, BL-18 | 27/11/2026 | | Lorrainy Pâmela Siqueira Ferreira | A fazer |
| BL-22 | Integrar front-end, API e persistência no caminho principal. | Demonstração de abertura até conclusão com dados de teste. | BL-20, BL-21 | 27/11/2026 | | | A fazer |
| BL-23 | Preparar dados fictícios e roteiro de demonstração da versão inicial. | Base reproduzível, sem dados pessoais reais. | BL-16, BL-22 | 27/11/2026 | | | A fazer |
| BL-24 | Preparar versão inicial e entrega da 4ª Etapa. | Repositório atualizado, instruções de execução e demonstração funcional. | BL-14 a BL-23 | 27/11/2026 | | Nayara Ferreira de Jesus Rodrigues | A fazer |

### 5ª Etapa e apresentação final — até 11/12

| ID | Item | Entrega verificável | Dependência | Conclusão prevista | Concluído em | Responsável | Situação |
| --- | --- | --- | --- | --- | --- | --- | --- |
| BL-25 | Implementar aprovação, reabertura, evidências e notificações prioritárias. | Fluxos previstos demonstráveis e registrados. | BL-20, BL-22 | 11/12/2026 | | | A fazer |
| BL-26 | Implementar filtros, painel e indicação de vencimento de prazo. | Consulta de tickets por filtros e visão de acompanhamento. | BL-20, BL-22 | 11/12/2026 | | Lorrainy Pâmela Siqueira Ferreira | A fazer |
| BL-27 | Implementar segurança e controles de qualidade definidos. | Evidências de acesso por perfil e requisitos não funcionais prioritários aplicáveis ao protótipo. | BL-19, BL-20 | 11/12/2026 | | | A fazer |
| BL-28 | Executar casos de teste, registrar defeitos e realizar retestes. | Evidências dos casos prioritários, lista de defeitos e situação final. | BL-22, BL-25 a BL-27 | 11/12/2026 | | | A fazer |
| BL-29 | Realizar avaliação de usabilidade e registrar melhorias. | Resultado agregado das tarefas e UEQ-S; melhorias priorizadas pelo PO. | BL-22 | 11/12/2026 | | | A fazer |
| BL-30 | Preparar release, instruções de execução e contingência da demonstração. | Versão identificada, passo a passo de execução e plano alternativo para a demo. | BL-23, BL-28 | 11/12/2026 | | | A fazer |
| BL-31 | Consolidar artigo, documentação de apoio e referências no padrão PUC Minas. | Versão revisada pelo orientador e pronta para submissão. | BL-28, BL-29 | 11/12/2026 | | Nayara Ferreira de Jesus Rodrigues | A fazer |
| BL-32 | Preparar apresentação, roteiro e divisão de fala da demo. | Apresentação ensaiada, dados de demonstração e responsáveis por cada parte. | BL-30, BL-31 | 11/12/2026 | | | A fazer |
| BL-33 | Participar da Mostra Tecnológica Interdisciplinar e da apresentação final. | APP final, apresentação e demo executada. | BL-32 | 11/12/2026 | | | A fazer |
| BL-34 | Realizar a revisão cruzada final do artigo. | Checklist de coerência entre etapas, Overleaf, citações, referências, documentação de apoio, resumo, abstract e padrão PUC Minas; pendências corrigidas ou registradas. | BL-31 | 11/12/2026 | | Revisor do artigo a definir | A fazer |

## Contrato de integração front-end e back-end

BL-17 e BL-18 são obrigatórios antes de integrar as telas à API. O contrato deve ficar versionado na documentação do projeto e conter, no mínimo:

1. autenticação e perfis autorizados por rota;
2. recursos de usuário, cliente, campanha, tipo de demanda, ticket, comentário, anexo, evidência, aprovação e histórico;
3. formato dos dados, campos obrigatórios, paginação, filtros e ordenação;
4. códigos de resposta e mensagens de erro;
5. exemplos de requisição e resposta, incluindo erros de permissão;
6. regra de versionamento: qualquer alteração incompatível exige atualização simultânea de contrato, mock, back-end e front-end.

## Itens fora do escopo do MVP

Estes itens permanecem fora do backlog de entrega, salvo decisão explícita do grupo: integração automática com Google Ads e Meta Ads, execução automática de mudanças de campanha, recomendações por inteligência artificial, cálculo automático de métricas, faturamento e chat externo ao histórico do ticket.
