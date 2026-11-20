# Roteiro de avaliação de usabilidade

Versão 1 — preparado em 19/11/2026. **Situação: planejado, sem sessões realizadas neste registro.** Aplicar primeiro um piloto entre os integrantes e, após ajustes, o protocolo com seis participantes externos por conveniência. As duas fases têm resultados separados; integrantes que desenvolveram o produto não substituem automaticamente usuários externos.

## Objetivo e materiais

Observar se a pessoa compreende o fluxo, encontra informações e realiza tarefas do seu perfil. Relacionar dificuldades a requisitos e melhorias. A meta funcional de 90% dos CTs não é uma nota de usabilidade.

Preparar ambiente de demonstração isolado, contas fictícias separadas de Cliente, Atendimento e Gestor, organização e campanha ativas e tickets no estado inicial de cada tarefa. Usar a mesma versão da aplicação durante cada rodada e registrar versão, navegador, resolução e ambiente na [ficha de sessão](FICHA-DE-SESSAO.md). Não usar dados ou senhas reais de clientes. O [guia demo](../../../SIGE-Desk/Backend/VALIDACAO-DEMO.md) é referência para as fixtures; confirmar os estados antes da sessão, sem pressupor que os IDs locais de QA existam em outro ambiente.

Entregar somente a [folha do participante](PARTICIPANTE.md), o cenário do papel e as credenciais fictícias por canal reservado. O moderador conserva critérios de observação e transições entre papéis. As ações fora do perfil são feitas pelo moderador em outra sessão para preparar a tarefa seguinte; não entram no tempo do participante.

## Piloto com os oito integrantes

Distribuição inicial de organização, sem alegação de participação já ocorrida:

| Função no piloto | Integrantes |
| --- | --- |
| Moderação e preparação das sessões | Nayara Ferreira de Jesus Rodrigues |
| Observação e registro | Augusto do Couto Avelar |
| Simulação de Cliente | Gabriel Fernandes de Araújo; Gabriel Rocha Lopes |
| Simulação de Atendimento | Karine Kessen Gilberti Felix; Pedro Henrique Assuncao |
| Simulação de Gestor de tráfego | Lorrainy Pâmela Siqueira Ferreira; Maria Heloisa Aguiar Mendes |

Os papéis simulados não comprovam experiência profissional. Registrar familiaridade com o sistema e o que cada integrante implementou. Para tarefas de moderação/observação também serem experimentadas, pode haver rodízio em sessões adicionais; identificar essas sessões como piloto e evitar que alguém avalie a própria sessão. Todos podem relatar dificuldades; não misturar opiniões de desenvolvimento com dados externos.

Executar piloto, verificar se instruções são compreensíveis, se os estados iniciais permitem as tarefas e se a ficha captura os dados necessários. Corrigir defeitos impeditivos, revisar roteiro e registrar versão, motivo e data da alteração. Congelar uma versão antes da fase externa. Não juntar tempos de versões diferentes sem explicitar a diferença.

## Coleta externa planejada

Convidar seis pessoas: duas com experiência de contratação/solicitação de serviços de marketing (Cliente), duas de atendimento/gestão de conta e duas de gestão de tráfego. Registrar experiência por categoria, sem nome na planilha analítica. A amostra é exploratória, por conveniência; não representa todas as agências. Se o perfil for substituído, registrar experiência real e papel simulado, apresentar a limitação e não relatar a pessoa como profissional daquele papel.

## Condução de uma sessão

1. Explicar propósito, voluntariedade e uso agregado dos registros; obter concordância. Permitir desistência sem justificativa. Não gravar tela/voz por padrão; gravação exige concordância específica e registro de guarda e acesso.
2. Registrar fase (piloto/externa), código anônimo, perfil real e simulado, familiaridade, versão e dados de ambiente. Não mostrar soluções antes da tentativa.
3. Entregar uma tarefa por vez. Iniciar o cronômetro quando a pessoa terminar de ler e confirmar compreensão; encerrar na conclusão observável, desistência ou interrupção. Pausar e registrar espera de ambiente ou preparação entre papéis.
4. Observar em silêncio, sem indicar botão ou rota. Se houver dúvida sobre o enunciado, esclarecer sem dar a solução. Se solicitada ajuda de navegação, registrar o instante e o conteúdo; a tarefa passa a “com ajuda”.
5. Como limite operacional inicial, após cinco minutos sem avanço perguntar se deseja continuar ou encerrar. Esse limite pode ser ajustado no piloto; não é parâmetro da literatura. Falha de ambiente gera “interrompida”, não erro de usabilidade atribuído ao participante.
6. Depois da tentativa, perguntar onde houve dificuldade e o que a pessoa esperava. Perguntas retrospectivas evitam obrigar verbalização contínua durante a medição de tempo. Se houver fala espontânea, registrar seu contexto.
7. Após o conjunto de tarefas do papel, aplicar UEQ-S individualmente, sem orientar respostas; em seguida colher comentários finais. Encerrar e revisar os registros com o observador.

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

U-T1 e U-T2 são aplicadas aos três papéis. O participante usa somente seu papel durante o bloco medido; as passagens entre Cliente, Atendimento e Gestor são preparadas em sessões fictícias independentes. Admin não é contado como quarto perfil da amostra externa planejada.

## Registros, UEQ-S e análise

Usar uma cópia de [FICHA-DE-SESSAO.md](FICHA-DE-SESSAO.md) por sessão. Classificar cada tarefa como concluída sem ajuda, concluída com ajuda, não concluída ou interrompida por ambiente; não transformar tarefa não aplicável em sucesso. Anotar tempo em segundos, erros observados, dúvidas, ajuda, evidência e versão. Não preencher células vazias como zero.

Aplicar os oito itens do UEQ-S a partir do [material oficial](https://www.ueq-online.org/), preservando termos, ordem, escala e orientação dos polos. O portal oferece o questionário curto e a ferramenta de análise. Arquivar a versão efetivamente aplicada com os dados da rodada; não improvisar tradução nem selecionar oito itens do UEQ completo. A ficha possui campos para transcrição, mas não substitui o formulário oficial. Fonte metodológica já adotada no projeto: Schrepp, Hinderks e Thomaschewski (2017), DOI 10.9781/ijimai.2017.09.001.

Na disposição padronizada do UEQ-S, converter 1–7 em -3–+3 por resposta menos quatro, conferindo os polos da versão aplicada. Calcular média pragmática dos quatro primeiros itens e hedônica dos quatro últimos; registrar média geral dos oito como síntese. Manter respostas originais. Se houver resposta ausente, registrar ausência e não imputar neutralidade; informar o número válido por estatística, evitando comparar médias construídas com conjuntos diferentes sem explicação.

Apresentar piloto e fase externa separadamente, com quantidades válidas e perfil. Para tarefas, informar conclusão com/sem ajuda, falhas, interrupções e tempo descritivo das tarefas efetivamente tentadas, identificando situações excluídas. Para comentários, agrupar temas de fluxo, informações, comunicação, prioridade, prazo, aprovação, evidências e indicadores; relacionar a tarefa, requisito e sugestão. No piloto, priorizar refinamento do instrumento; não usá-lo como validação independente do produto. Sem inferência estatística, benchmark improvisado ou nota mínima arbitrária do UEQ-S.

Transformar achados em lista de melhorias com impacto, tarefa/requisito, responsável, decisão, correção e reteste. A evidência funcional existente não antecipa satisfação ou eficiência dos participantes. Guardar respostas e fichas em acesso restrito ao grupo durante a elaboração e revisão do trabalho; antes da coleta, definir na ficha a data de descarte e eventuais exigências da disciplina. Publicar somente resultados agregados e exemplos anonimizados.
