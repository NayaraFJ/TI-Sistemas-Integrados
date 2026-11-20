# Limites do filtro de SLA — CT-06

## Cenários e resultado

Em 19/11/2026, o teste de integração `TicketOverdueMySqlIntegrationTest` passou em MySQL, no schema novo e exclusivo `sige_desk_qa_sla_limites_20261119`: **1 teste, zero falhas, erros ou ignorados**. O comando Maven, em `SIGE-Desk/Backend`, foi `mvn -Dtest=TicketOverdueMySqlIntegrationTest test`, com as variáveis de conexão `SIGE_TEST_DB_URL`, `SIGE_TEST_DB_USERNAME` e `SIGE_TEST_DB_PASSWORD` apontando para esse schema. O [log da execução](limites-sla-maven-20261119.log) preserva a saída; Flyway aplicou as migrações ao schema exclusivo. O teste usa transação revertida e um tipo de demanda próprio.

O instante de consulta foi fixado em `2025-01-01T12:00:00Z`. Para Cliente, Gestor atribuído, Atendimento e Admin, a consulta ao repositório retornou exatamente o conjunto esperado: `SLA-1`, `SLA-3`, `SLA-4` e `SLA-11`. Os estados calculados pelo serviço também foram confrontados com esse conjunto.

| Fixture | Condição controlada | Resultado do filtro |
| --- | --- | --- |
| SLA-8 | Primeira resposta ainda não concluída, prazo igual ao instante de consulta; resolução futura | Excluída |
| SLA-9 | Resolução ainda não concluída, prazo igual ao instante de consulta; resposta futura | Excluída |
| SLA-10 | Primeira resposta e resolução com datas de conclusão iguais aos respectivos prazos anteriores à consulta | Excluída |
| SLA-11 | Data de conclusão da resolução posterior ao prazo; primeira resposta futura | Incluída |
| SLA-3 | Primeira resposta concluída após seu prazo | Incluída |

As datas de conclusão foram definidas diretamente na fixture para isolar a consulta, sem executar as transições de encerramento; em particular, SLA-11 não demonstra o fluxo de mudança para o estado DONE. A matriz também inclui pausa, cancelamento e casos no prazo. Os horários estão separados em segundos inteiros e não demonstram todos os limites de precisão ou calendário. Não foram exercitados HTTP, interface, relógio real ou todas as combinações de SLA. CT-06 continua parcial no catálogo integral; as evidências anteriores permanecem no [relatório CT-06/CT-15](relatorio-ct06-ct15-filtros-painel-20261119.md).
