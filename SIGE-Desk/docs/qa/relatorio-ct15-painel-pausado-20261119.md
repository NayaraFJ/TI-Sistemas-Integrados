# CT-15 — grupo positivo de SLA pausado

Em 19/11/2026, 20:55:11–20:55:13 UTC, executou-se `python scripts/qa_ct15_painel_pausado.py --output ../docs/qa/ct15-painel-pausado-20261119.json` no diretório `Backend`, usando o Python do runtime Codex. API `http://localhost:8081/api/v1`, MySQL local `sige_desk_qa_20261118`. Foram 75 verificações HTTP e de confronto, sem falhas. [Roteiro](../../Backend/scripts/qa_ct15_painel_pausado.py) e [evidência](ct15-painel-pausado-20261119.json) preservam as consultas, conjuntos, resultados e horários.

A fixture exclusiva da matriz CT-06 preexistente, ticket `3b42b608-e9b0-4fbf-b634-af4549a08413`, foi reutilizada somente em leitura. O banco confirmou estado `WAITING_FOR_CLIENT`, resolução com prazo e pausa registrados e primeira resposta concluída dentro do prazo. O oráculo independente calculou todos os grupos dos tickets ativos por escopo, a partir dos campos do banco, antes e depois de cada chamada ao painel; os dados permaneceram estáveis e nenhum limite temporal foi cruzado durante cada comparação.

| Sessão | Fixture no escopo | PAUSED esperado no banco | PAUSED no painel |
|---|---:|---:|---:|
| Admin | Sim | 2 | 2 |
| Atendimento | Sim | 2 | 2 |
| Cliente da organização | Sim | 2 | 2 |
| Cliente externo | Não | 0 | 0 |
| Gestor atribuído | Sim | 2 | 2 |
| Gestor não atribuído | Não | 0 | 0 |

Os demais grupos de prazo, a partição de ativos, indicadores e distribuições de status, prioridade e responsável também coincidiram para as seis sessões. Como o painel retorna agregados, a evidência demonstra a contagem total em conjunto com o registro no escopo, sem atribuição individual pelo painel. Nenhum defeito foi observado neste recorte. A interface, gráficos, rótulos, estado vazio e limites temporais continuam fora da comprovação; CT-15 permanece parcial. O processo HTTP não teve sua conexão efetiva ao schema identificada independentemente: o confronto bem-sucedido com o banco indicado fornece evidência indireta, não confirmação da configuração do processo.
