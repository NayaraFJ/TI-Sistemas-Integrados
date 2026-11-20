# Correções submetidas ao reteste funcional

## CT-08 — Negativa com corpo inválido sem auditoria

- **Ocorrência:** uma chamada de comentário a ticket de outra organização, quando o corpo era inválido, retornava 400 antes da verificação de escopo e não gerava trilha. Na [evidência anterior](ct08-corpo-invalido-20261119.json), as verificações registram 400 e `auditDelta: 0`; o rótulo `APROVADO` desse arquivo apenas indica que o roteiro observacional terminou, não aprovação do requisito de auditoria.
- **Correção:** a visibilidade do ticket é verificada em leitura antes da validação do corpo. A verificação de versão e a ação continuam na mesma transação, evitando o incremento indevido já corrigido. O acesso é novamente verificado na operação, preservando sua proteção própria.
- **Reteste:** em [ct08-corpo-invalido-reteste.json](ct08-corpo-invalido-reteste.json), Cliente de outra organização e Gestor não atribuído receberam 404 genérico e geraram um evento por tentativa, com corpo válido ou inválido. Ticket inexistente também retornou 404; para o proprietário, corpo inválido retornou 400 sem alterar o ticket. A [matriz de 38 verificações](ct08-matriz-visibilidade-reteste.json) e a [disputa de versões](concorrencia-visibilidade-reteste.json) também passaram no runtime corrigido. Esses testes não substituem as variantes visuais ou toda a matriz de segurança.
- **Regressão de versão:** as [52 verificações de negativas e versão](versao-negativas-visibilidade-reteste.json) também passaram no runtime corrigido.

## QA-DEF-01 — Tentativa de acesso entre organizações sem trilha persistida

- **Caso relacionado:** CT-08; RF-02, RN-09 e RNF-02.
- **Prioridade/severidade:** P2/relevante. O acesso era negado, mas faltava a evidência de segurança exigida no aceite.
- **Condição de reprodução:** autenticar um Cliente e solicitar detalhe, histórico ou anexo de um ticket existente pertencente a outra organização.
- **Esperado:** negar o acesso sem revelar o recurso e registrar ator, evento, alvo e data/hora da tentativa.
- **Observado antes da correção:** `TicketAccessPolicy.requireVisible` respondia 404 sem gravar evento de segurança. Os testes da policy cobriam apenas a decisão booleana de visibilidade, não o registro da tentativa.
- **Correção:** migration V7 cria `security_access_events`; um bean separado `SecurityAccessAudit` persiste `SECURITY_TICKET_ACCESS_DENIED` em transação `REQUIRES_NEW`. A policy lança uma exceção interna com os dados da tentativa; após o encerramento da transação original, o tratamento HTTP grava a trilha em transação independente e retorna 404 genérico. Isso evita reter uma conexão da operação negada enquanto outra é adquirida para a auditoria.
- **Dados da trilha:** identificador do evento, ator, perfil, código, tipo do recurso, ID do recurso e data/hora. Não registra conteúdo do ticket, senha, token ou dados do navegador. A tabela não é exposta pela API ao Cliente.
- **Regressão automatizada:** `TicketAccessPolicyTest` exige a exceção interna e 404 genérico; `ApiExceptionHandlerTest` verifica que a auditoria é chamada antes da resposta. O teste de migration passou a exigir a tabela nova quando executado com MySQL.
- **Resultado técnico em 18/11/2026:** teste da policy com 5 aprovações; suíte Java com 49 testes coletados, zero falhas/erros e um teste MySQL ignorado por ausência de configuração de base descartável nesse comando. Os arquivos de log permanecem em `Backend/target/qa-policy-test.log` e `Backend/target/qa-verify.log`.
- **Migration real:** `MySqlMigrationIntegrationTest` foi executado separadamente na base vazia `sige_desk_qa_migration_20261118`: sete migrations V1–V7 aplicadas, uma aprovação, nenhum erro/falha/ignorado. O log está em `Backend/target/qa-migration-test.log`. A V5 emitiu o aviso transitório de índice redundante já conhecido; a V6 removeu a redundância.
- **Situação funcional:** **corrigido e aprovado no reteste independente HTTP + MySQL**. A [reprodução anterior](ct08-antes-correcao.json) registrou 404 sem mecanismo persistente na versão V6. A [execução após a correção](execucao-rodada1.json) comprovou três acessos negados com 404 e três eventos persistidos para o ator e alvo esperados, mesmo após o rollback das chamadas. Isso fecha QA-DEF-01 nessa camada; a avaliação de interface e de usabilidade permanece distinta.

## Revisão da permissão de entrega

A revisão também confrontou a divergência entre o Anexo B e a matriz operacional 8.1/RN-04. O código `TicketService.requireExecutionActor` já exige Gestor de tráfego atribuído ao ticket para registrar a entrega. Nenhuma ampliação de permissão foi implementada. A harmonização do texto acadêmico pertence à fonte canônica e será conduzida na consolidação documental.

## QA-DEF-02 — Conteúdo insuficiente nas notificações

- **Caso relacionado:** CT-07; RF-20 e Anexo D.
- **Observado antes da correção:** a atribuição persistia apenas uma mensagem genérica, sem assunto, prioridade e prazo; a evidência está em [execucao-ct07-antes-correcao.json](execucao-ct07-antes-correcao.json).
- **Correção submetida:** a composição centralizada inclui número e assunto e acrescenta, conforme o evento, prioridade, prazo, autor e resumo, motivo, origem e situação da pausa, contexto da retomada, ação e evidência de validação, justificativa, ciclo e próximo passo. O aviso de SLA informa tipo, prazo e atraso em segundos. O conteúdo é limitado ao tamanho persistido, com indicação de abreviação.
- **Regressão automatizada:** `NotificationSummaryTest` cobre atribuição, espera e retomada, comentário, validação, eventos finais, vencimento e limite de texto Unicode; os testes de serviço exercitam publicação e avisos de prazo. O teste funcional reforçado verifica eventos da fixture, destinatários, conteúdo e ausência de duplicação do aviso.
- **Reteste:** [execucao-ct07-apos-correcao.json](execucao-ct07-apos-correcao.json) registrou uma falha do próprio oráculo, que exigia o comentário do Gestor nas notificações do comentário do Cliente. Após corrigir a associação entre autor e texto e exigir o conteúdo em cada aviso aplicável, [execucao-ct07-reteste-final.json](execucao-ct07-reteste-final.json) aprovou CT-07 no recorte HTTP/MySQL, incluindo destinatários, agendador, deduplicação e novo ciclo. O resultado anterior permanece preservado.
- **Complemento recebido:** [execucao-ct07-complemento.json](execucao-ct07-complemento.json) registrou uma fixture adicional; os cinco avisos passaram na conferência de ticket, assunto, informação, fase, orientação de retomada e data do prazo aplicável.
- **Situação:** corrigido e aprovado no recorte automatizado; consulta e leitura na interface ainda precisam de evidência. O pacote final executou 57 testes Java sem falhas, com um teste MySQL ignorado no comando; a migration foi executada separadamente em base vazia. Não se conclui o gate integral apenas por essa rodada.

## Retestes adicionais de 19/11

Após ajuste adicional para falha de auditoria, [regressoes-auditoria-runtime-novo.json](regressoes-auditoria-runtime-novo.json) aprovou 12 negativas concorrentes, 12 eventos persistidos e preservação da versão. [execucao-auditoria-runtime-novo.json](execucao-auditoria-runtime-novo.json) aprovou 17/17 no recorte HTTP/MySQL. A injeção com trigger restrito está preservada [antes](falha-gravacao-auditoria.json) e [depois](falha-gravacao-auditoria-reteste.json): no reteste, a resposta 404 foi equivalente à de recurso inexistente, exceto horário, e houve log correlacionável da falha; não houve persistência na tabela deliberadamente bloqueada. O trigger foi removido. Esses resultados não comprovam sozinhos o gate integral.

O reteste [regressoes-notificacoes-auditoria-reteste.json](regressoes-notificacoes-auditoria-reteste.json) aprovou a distinção entre complemento e motivo na retomada, a distinção entre correção no mesmo ciclo e reabertura em novo ciclo e a auditoria de 12 negativas simultâneas com pool de cinco conexões. A primeira execução [regressoes-notificacoes-auditoria.json](regressoes-notificacoes-auditoria.json) revelou erro de oráculo: o prazo podia ser estendido pela pausa em validação. O reteste passou após corrigir a expectativa, mantendo a primeira evidência.

A rodada completa [execucao-apos-regressoes.json](execucao-apos-regressoes.json) registrou 16/17 e 11/11 altas. O CT-17 falhou por comparação com apenas uma página de tickets; a paginação foi corrigida no roteiro e [ct17-paginacao-reteste.json](ct17-paginacao-reteste.json) passou isoladamente.

Após a correção do resumo no histórico, [comentario-longo-multibyte.json](comentario-longo-multibyte.json) comprovou o corpo integral por API e hash no MySQL, além de resumo identificável no histórico e nos avisos. A rodada [execucao-runtime-final.json](execucao-runtime-final.json) passou 17/17 e 11/11 altas no recorte HTTP/MySQL. A avaliação da interface e a revisão de defeitos críticos continuam separadas.

### Revisão adicional em 19/11/2026 — registro anterior aos retestes

A confirmação de complemento agora identifica a informação recebida no aguardo atual (comentário ou nome do anexo), além da justificativa de confirmação, fase retomada e prazo após recálculo. A orientação da reabertura distingue correção solicitada durante validação, que pode seguir para execução, de um novo ciclo após conclusão, que requer confirmação da triagem. Para registros anteriores sem identificação específica do complemento, a mensagem orienta consultar o histórico. A auditoria de acesso negado foi deslocada para depois do encerramento da transação de negócio para evitar esgotamento do pool por transações aninhadas. Na ocasião deste registro, essas alterações aguardavam retestes funcionais e de concorrência independentes. Os retestes posteriores estão registrados na seção “Retestes adicionais de 19/11” acima.

Em falha de persistência da auditoria, o tratamento retorna a mesma estrutura e o mesmo status 404 usados para ticket inexistente, sem detalhes da falha na resposta. Um erro `SECURITY_TICKET_AUDIT_FAILED` registra ator, alvo e classe da falha no log; nesse caso, o evento não é gravado na tabela e a ocorrência requer investigação operacional. Os testes de handler e policy passaram (8 testes); o reteste HTTP com falha induzida na base QA ainda estava pendente nesse momento. O reteste posterior está registrado em [falha-gravacao-auditoria-reteste.json](falha-gravacao-auditoria-reteste.json) e na seção “Retestes adicionais de 19/11” acima.

## Conflito simultâneo de versão em comentários — reteste independente

A [primeira execução](concorrencia-if-match-independente.json) retornou 201 e 500 para dois comentários simultâneos com o mesmo `If-Match`, embora apenas um comentário e um evento tenham persistido. Após atualização do runtime, três corridas independentes retornaram 201 e 409, com versão 0→1 e exatamente um comentário e um evento em cada fixture: [1](concorrencia-if-match-reteste-1.json), [2](concorrencia-if-match-reteste-2.json), [3](concorrencia-if-match-reteste-3.json). Runtime SHA-256: `10f163768eb01b93dc32891fea4ed71be259bdeabce9c443cfd732d5ca754f94`. Correção aprovada nesse recorte HTTP/MySQL; não substitui a verificação das demais ações e da interface. Detalhes em [revisão independente](revisao-concorrencia-independente.md).

## Período inválido na listagem — CT-06

A interface ocultava os filtros e a ação Limpar quando o período era inválido. Após a correção, o [reteste visual](relatorio-interface.md) manteve os controles visíveis: corrigir as datas ou usar Limpar recuperou a listagem sem recarregar. O build do frontend passou; não foi preservada captura. O confronto de [período individual por API e MySQL](ct06-periodo-individual-20261119.json) complementa o teste dos filtros.
