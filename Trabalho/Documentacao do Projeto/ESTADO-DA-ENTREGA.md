# Estado da entrega e navegação técnica

Revisão documental em 19/11/2026; não houve nova execução de testes ou implantação nesta revisão. Os requisitos normativos continuam em [Especificação](Especificacao/Especificacao%20do%20sistema.md) e [Requisitos](Especificacao/Requisitos.md). Estar implementado não significa ter avaliação de usabilidade ou aceite formal do grupo.

| Área | Situação comprovada / fonte | Continuidade |
| --- | --- | --- |
| Arquitetura e execução | [Aplicação](../../SIGE-Desk/README.md), [decisões](../../SIGE-Desk/docs/decisoes.md); backend modular Spring Boot e frontend React | Conferir versão e ambiente no ensaio da demo |
| Dados | [Migrations](../../SIGE-Desk/Backend/src/main/resources/db/migration), [dados demo](../../SIGE-Desk/Backend/VALIDACAO-DEMO.md) | Não confundir fixtures QA com dados da publicação |
| Contrato e permissões | [OpenAPI](../../SIGE-Desk/docs/openapi.yaml), [sincronização](../../SIGE-Desk/docs/openapi.md), [permissões](../../SIGE-Desk/docs/permissoes.md) | Toda mudança de API deve manter tipos e consumidores coerentes |
| Cadastros, abertura, triagem, execução, complemento e encerramento | [CT-01 a CT-14](Testes%20funcionais/README.md), com variantes e limites por ficha | Revisão documental humana e limites legados registrados |
| Filtros, métricas e CSV | CT-06, CT-16 e CT-17 aprovados no escopo das [fichas](Testes%20funcionais/README.md) | Manter oráculos e dados da seleção ao retestar |
| Painel | CT-15 aprovado; números, gráficos e estado vazio confrontados | [Capturas e fechamento](../../SIGE-Desk/docs/qa/relatorio-ct15-fechamento-20261120.md) |
| Segurança e demais RNFs | Negativas, escopo e auditoria exercitados em CT-08/11 | Sem alegação de pentest, acessibilidade integral ou três navegadores |
| Usabilidade | [Protocolo e instrumentos](Usabilidade/ROTEIRO.md) elaborados | Avaliação entre os integrantes e análise ainda pendentes |
| Operação e publicação | [Registro de publicação](../../SIGE-Desk/docs/publicacao-aws.md) | Registro documental, sem nova verificação remota nesta tarefa |

O resultado consolidado em 20/11/2026 é 17/17 (100%), com 11/11 altas e sem crítico aberto identificado nas ocorrências examinadas. Os limites incluem ausência de capturas nos casos anteriores ao complemento de CT-15, amostragem de histórico, datas controladas e recuperação não demonstrada para todos os snapshots legados. Não alterar requisitos para coincidir artificialmente com o código. Registrar divergência e reteste antes de mudar a decisão.
