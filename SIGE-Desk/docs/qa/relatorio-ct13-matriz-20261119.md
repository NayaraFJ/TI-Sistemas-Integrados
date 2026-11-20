# CT-13 — confronto adicional de histórico

Execução em 19/11/2026, 17:27 UTC, API `http://localhost:8081/api/v1`, MySQL `sige_desk_qa_20261118`. Fixture sintética identificada no [registro HTTP](ct13-matriz-20261119.json); dados mantidos no schema isolado para auditoria. Os roteiros são `Backend/scripts/qa_ct13_matriz.py` e `Backend/scripts/qa_ct13_confronto.py`. O hash do JAR no registro identifica o arquivo, não prova o binário carregado pelo processo.

A sequência abriu, iniciou triagem, classificou, reatribuiu, aguardou complemento, retomou, entregou, aprovou, reabriu e reclassificou o ticket. O primeiro roteiro registrou 48 verificações HTTP; o segundo registrou 20 confrontos sem falha em [JSON](ct13-confronto-20261119.json). A comparação consultou `ticket_history` diretamente, inclusive IDs de ator, instante, campos, valores e motivo. Todos os eventos da API coincidiram com os registros persistidos; os eventos anteriores permaneceram presentes a cada etapa. Os testes negativos consultaram histórico sem escopo (404) e enviaram versão ausente (428), versão obsoleta (409) e reatribuição por Cliente (403); a versão, o ticket e a lista de eventos permaneceram iguais.

| Alteração | Confronto observado |
| --- | --- |
| Estado e classificação | Abertura, triagem, execução, aguardo, retomada, entrega, aprovação e reabertura; prioridade inicial null → HIGH e responsáveis null → Gestor. |
| Reatribuição | IDs anterior e novo e motivo explícito coincidiram; reclassificação voltou a atribuir ao Gestor inicial. |
| Prazos | Prazos iniciais conferidos com o detalhe; resolução antes/depois da pausa e da validação conferida com os snapshots; novo ciclo recebeu prazo próprio. |
| Aprovação e ciclo | VALIDATION → DONE, motivo e autoria; fechamento preservou prazo e snapshot do primeiro ciclo. |
| Negativas | Consultas e mutações negadas sem mudança no estado e no histórico do ticket. |

**Limites:** a comparação de identidade usou os nomes retornados pela API e verificou a existência de `actor_id` no banco, sem junção independente para comprovar que cada ID corresponde ao nome. A segunda classificação manteve HIGH; não comprova mudança efetiva HIGH → outra prioridade. Campanha e tipo foram confirmados sem mudança real, e histórico extenso/paginação não foi exercitado. Naquela rodada, apresentação visual e recuperação de conflito estavam pendentes; os complementos posteriores constam abaixo e no relatório de interface. A rodada não identificou defeito no recorte verificado.

## Complemento: alterações efetivas e ator

Uma segunda fixture em 19/11/2026 às 17:29 UTC classificou inicialmente o ticket SIGE-1559 como HIGH, com campanha e tipo iniciais, mantendo-o em triagem. A classificação seguinte alterou HIGH → LOW, campanha e tipo para novos IDs e encaminhou o ticket à execução. Os valores anteriores e novos nos três eventos coincidiram com os snapshots do detalhe, com motivo e instante; os eventos anteriores foram preservados. A [evidência própria](ct13-alteracoes-efetivas-20261119.json), produzida por `Backend/scripts/qa_ct13_alteracoes_efetivas.py`, registra 28 verificações aprovadas na API/MySQL.

A consulta independente uniu `ticket_history.actor_id` a `users.id` e comparou nome, campos, valores, motivo e instante dos eventos com a API. Nos eventos novos da reclassificação, o ID também coincidiu com o usuário autenticado de Atendimento. Isso cobre a identidade da segunda fixture; a primeira não foi novamente conferida pela mesma junção. Histórico extenso e apresentação visual permanecem pendentes, e o CT integral continua parcial. Nenhum defeito foi identificado neste complemento.

## Histórico extenso e atores da primeira fixture

O detalhe retorna `history` como lista integral ordenada por data decrescente; o contrato implementado não oferece paginação própria de histórico. Uma fixture adicionou 120 comentários alternando Cliente, Atendimento e Gestor atribuído. A resposta trouxe 129 eventos, com IDs únicos, ordem temporal decrescente e preservação dos eventos anteriores; o conteúdo foi confrontado por ID com MySQL. A junção com `users` conferiu os nomes, e os 120 eventos de comentário foram distribuídos por ID de ator exatamente 40 por sessão. Cliente de outra organização recebeu 404 no detalhe. A [evidência de reteste](ct13-historico-extenso-reteste-20261119.json) registra 262 verificações sem falha. O volume é uma amostra funcional, sem avaliação de desempenho ou limite máximo.

A mesma execução confrontou por junção os eventos da primeira fixture e os IDs esperados das sessões para criação, classificação, aguardo, complemento, retomada, entrega, aprovação e reabertura. A [primeira tentativa](ct13-historico-extenso-20261119.json) falhou antes de criar a fixture extensa: a saída padrão do cliente MySQL escapou JSON aninhado e o leitor do roteiro não conseguiu decodificá-lo. O roteiro passou a solicitar saída bruta; trata-se de falha de instrumento, não de produto. Naquele ponto, apresentação visual e recuperação de conflito ainda estavam pendentes; foram verificadas posteriormente.


## Decisão após a interface

O [reteste visual](relatorio-interface.md) verificou a apresentação de campo, valores, autor, horário e motivo após correção, além da recuperação de conflito em duas abas. Com os confrontos HTTP/MySQL e as negativas acima, a auditoria independente considerou as variantes mandatórias demonstradas e CT-13 aprovado no escopo executado. A observação visual não tem captura preservada; os 129 eventos são amostra, sem teste de limite máximo.
