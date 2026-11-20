# Roteiro de avaliação de usabilidade

Versão 3 — detalhamento do UEQ-S em 20/11/2026. **Situação: planejado, sem sessões realizadas neste registro.** A avaliação será realizada somente pelos integrantes do grupo. A familiaridade com a aplicação e a participação em seu desenvolvimento serão registradas como limitações dos resultados.

## Objetivo e materiais

Observar se a pessoa compreende o fluxo, encontra informações e realiza tarefas do seu perfil. Relacionar dificuldades a requisitos e melhorias. A meta funcional de 90% dos CTs não é uma nota de usabilidade.

Preparar ambiente de demonstração isolado, contas fictícias separadas de Cliente, Atendimento e Gestor, organização e campanha ativas e tickets no estado inicial de cada tarefa. Usar a mesma versão da aplicação durante cada rodada e registrar versão, navegador, resolução e ambiente na [ficha de sessão](FICHA-DE-SESSAO.md). Não usar dados ou senhas reais de clientes. O [guia demo](../../../SIGE-Desk/Backend/VALIDACAO-DEMO.md) é referência para as fixtures; confirmar os estados antes da sessão, sem pressupor que os IDs locais de QA existam em outro ambiente.

Registrar se o participante já viu o tutorial e se o utilizou durante alguma tarefa. O recurso nativo pode ser usado sem ajuda do moderador; nesse caso, registrar o uso separadamente, sem classificar automaticamente como ajuda humana. Manter a mesma orientação para todos.

Entregar somente a [folha do participante](PARTICIPANTE.md), o cenário do papel e as credenciais fictícias por canal reservado. O moderador conserva critérios de observação e transições entre papéis. As ações fora do perfil são feitas pelo moderador em outra sessão para preparar a tarefa seguinte; não entram no tempo do participante.

## Organização entre os oito integrantes

Distribuição inicial de organização, sem alegação de participação já ocorrida:

| Função na avaliação | Integrantes |
| --- | --- |
| Moderação e preparação das sessões | Nayara Ferreira de Jesus Rodrigues |
| Observação e registro | Augusto do Couto Avelar |
| Simulação de Cliente | Gabriel Fernandes de Araújo; Gabriel Rocha Lopes |
| Simulação de Atendimento | Karine Kessen Gilberti Felix; Pedro Henrique Assuncao |
| Simulação de Gestor de tráfego | Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes |

Os papéis simulados não comprovam experiência profissional. Registrar familiaridade com o sistema e o que cada integrante implementou. Para tarefas de moderação/observação também serem experimentadas, pode haver rodízio em sessões adicionais; identificar os rodízios e evitar que alguém avalie a própria sessão. Usar código único por integrante e informar quantas pessoas distintas executaram tarefas. Todos podem relatar dificuldades; distinguir problemas observados de sugestões feitas na condição de desenvolvedor.

Antes da aplicação, verificar se as instruções são compreensíveis, se os estados iniciais permitem as tarefas e se a ficha captura os dados necessários. Corrigir defeitos impeditivos e registrar versão, motivo e data de alterações do roteiro. Não juntar tempos de versões diferentes sem explicitar a diferença. Os papéis simulados não equivalem a uma amostra de clientes ou profissionais externos; a avaliação oferece indícios internos de dificuldades de uso.

## Condução de uma sessão

1. Explicar propósito, voluntariedade e uso agregado dos registros; obter concordância. Permitir desistência sem justificativa. Não gravar tela/voz por padrão; gravação exige concordância específica e registro de guarda e acesso.
2. Registrar rodada, código anônimo do integrante, perfil real e simulado, familiaridade, versão e dados de ambiente. Não mostrar soluções antes da tentativa.
3. Entregar uma tarefa por vez. Iniciar o cronômetro quando a pessoa terminar de ler e confirmar compreensão; encerrar na conclusão observável, desistência ou interrupção. Pausar e registrar espera de ambiente ou preparação entre papéis.
4. Observar em silêncio, sem indicar botão ou rota. Se houver dúvida sobre o enunciado, esclarecer sem dar a solução. Se solicitada ajuda de navegação, registrar o instante e o conteúdo; a tarefa passa a “com ajuda”.
5. Como limite operacional inicial, após cinco minutos sem avanço perguntar se deseja continuar ou encerrar. Esse limite pode ser ajustado na conferência do roteiro; não é parâmetro da literatura. Falha de ambiente gera “interrompida”, não erro de usabilidade atribuído ao participante.
6. Depois da tentativa, perguntar onde houve dificuldade e o que a pessoa esperava. Perguntas retrospectivas evitam obrigar verbalização contínua durante a medição de tempo. Se houver fala espontânea, registrar seu contexto.
7. Após o conjunto completo de tarefas do papel e U-T1/U-T2, aplicar o [questionário UEQ-S](UEQ-S-PARTICIPANTE.md) uma única vez, individualmente, antes da discussão final. Usar o [guia detalhado](GUIA-UEQ-S.md) para instrução, transcrição e cálculo. Depois colher os comentários finais e revisar os registros com o observador.

## Tarefas e critérios de observação do moderador

As instruções ao participante descrevem objetivos, sem antecipar os passos da interface. Preparar tickets diferentes para aprovação e correção; não exigir decisões incompatíveis no mesmo estado.

| ID / perfil | Estado inicial / preparação | Objetivo observável e dados do cenário | Requisitos |
| --- | --- | --- | --- |
| U-C1 Cliente | Organização e campanha ativas; tipo definido | Abrir pedido de alteração de anúncio com assunto, descrição, canal e urgência; identificar número e estado pendente de triagem | RF-05, RF-06, RF-21 |
| U-C2 Cliente | Ticket aguardando Cliente, com motivo | Encontrar pedido de informação, responder com comentário ou anexo fictício e perceber que a retomada depende de conferência | RF-10, RF-11; RN-08 |
| U-C3 Cliente | Entrega em validação com material | Consultar evidência e aprovar; reconhecer conclusão | RF-11, RF-12; RN-05, RN-06 |
| U-C4 Cliente | Outro ticket em validação | Solicitar correção com justificativa; distinguir correção de reabertura | RF-11; RN-11 |
| U-C5 Cliente | Ticket concluído | Reabrir com justificativa e identificar novo ciclo e próximo passo | RF-11; RN-11 |
| U-A1 Atendimento | Demanda aberta com campanha pendente | Identificar contexto, classificar, associar campanha, atribuir Gestor e registrar primeira resposta; localizar prioridade e prazos | RF-07, RF-09, RF-21 |
| U-A2 Atendimento | Complemento recebido no aguardo | Conferir informação, confirmar complemento e reconhecer retorno à fase de origem | RF-10, RF-11; RN-08 |
| U-G1 Gestor | Ticket em execução atribuído ao participante | Registrar ação e evidência fictícia; reconhecer validação ou conclusão conforme tipo | RF-08, RF-10, RF-12; RN-04, RN-05 |
| U-G2 Gestor | Ticket atribuído em execução | Pedir informação ao Cliente e reconhecer aguardo e pausa | RF-11; RN-08 |
| U-T1 Todos | Tickets acessíveis e fixture conhecida | Encontrar ticket pelo identificador ou assunto, aplicar filtro pertinente e interpretar responsável, status e prazo | RF-14, RF-16, RF-17, RF-21 |
| U-T2 Todos | Aviso não lido de ticket acessível | Encontrar notificação, consultar contexto e marcar leitura | RF-20 |

U-T1 e U-T2 são aplicadas aos três papéis. O participante usa somente seu papel durante o bloco medido; as passagens entre Cliente, Atendimento e Gestor são preparadas em sessões fictícias independentes. Admin será usado somente na preparação das fixtures, sem constituir um quarto papel de execução das tarefas.

## Registros, UEQ-S e análise

Usar uma cópia de [FICHA-DE-SESSAO.md](FICHA-DE-SESSAO.md) por sessão. Classificar cada tarefa como concluída sem ajuda, concluída com ajuda, não concluída ou interrompida por ambiente; não transformar tarefa não aplicável em sucesso. Anotar tempo em segundos, erros observados, dúvidas, ajuda, evidência e versão. Não preencher células vazias como zero.

Aplicar o [questionário preparado para o participante](UEQ-S-PARTICIPANTE.md), com os oito pares da versão portuguesa oficial conferidos em 20/11/2026. Preservar termos, ordem e polos; a folha acrescenta identificação e numeração das sete posições, sem criar outra tradução. O [guia do UEQ-S](GUIA-UEQ-S.md) especifica fala do moderador, momento de aplicação, exemplos e regras de cálculo. Na organização inicial, seis executores podem produzir até seis questionários, um após cada bloco completo; moderação e observação não geram respostas de uso sem executarem tarefas.

Manter as respostas originais de 1 a 7 e converter cada valor válido por resposta menos quatro. Por decisão interna deste protocolo, calcular pragmática somente com Q1–Q4 completos, hedônica com Q5–Q8 completos e geral com os oito completos; informar quantidade válida em cada dimensão. Não imputar ausências. A média geral será complementar. O manual oficial recomenda o UEQ completo em avaliações usuais após tarefas; a opção já prevista pelo UEQ-S será mantida como medida resumida complementar, registrando a perda de detalhamento como limitação. Não serão apresentadas as seis escalas do UEQ completo a partir destes oito itens.

Apresentar os resultados por papel simulado e versão do roteiro, informando quantas pessoas distintas participaram, quantas executaram tarefas e quais realizaram rodízio. Para tarefas, informar conclusão com/sem ajuda, falhas, interrupções e tempo descritivo das tarefas efetivamente tentadas, identificando situações excluídas. Para comentários, agrupar temas de fluxo, informações, comunicação, prioridade, prazo, aprovação, evidências e indicadores; relacionar a tarefa, requisito e sugestão. Registrar a familiaridade com a aplicação e o envolvimento no desenvolvimento como possíveis influências sobre os tempos, a necessidade de ajuda e as respostas. Os achados não constituem validação independente nem representam usuários externos. Sem inferência estatística, benchmark improvisado ou nota mínima arbitrária do UEQ-S.

Transformar achados em lista de melhorias com impacto, tarefa/requisito, responsável, decisão, correção e reteste. A evidência funcional existente não antecipa satisfação ou eficiência dos participantes. Guardar respostas e fichas em acesso restrito ao grupo durante a elaboração e revisão do trabalho; antes da coleta, definir na ficha a data de descarte e eventuais exigências da disciplina. Publicar somente resultados agregados e exemplos anonimizados.

## Histórico do instrumento

Versão 3: detalha aplicação única ao final do bloco, entrega folha de UEQ-S com pares oficiais, define completude por dimensão, registra uso do tutorial e acrescenta a limitação da versão curta. As tarefas e a distribuição inicial dos integrantes foram preservadas; não houve coleta nesta revisão.
