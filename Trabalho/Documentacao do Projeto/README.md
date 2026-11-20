# Documentação do projeto

Esta documentação registra as definições técnicas do SIGE Desk. Ela reúne especificação, estado da implementação e documentação de validação, mas não deve ser reproduzida integralmente no corpo do artigo.

| Arquivo | Fonte canônica | Uso no artigo |
| --- | --- | --- |
| [Processo/BPMN - Processo As Is.md](Processo/BPMN%20-%20Processo%20As%20Is.md) | Hipótese analítica do processo atual e limites de uso. | Sustenta a caracterização do problema; não precisa ser reproduzido como figura no artigo. |
| [Levantamento de requisitos.md](Levantamento%20de%20requisitos.md) | Atores, evidências, critérios de análise e registro de decisões de requisitos. | Sustenta a delimitação dos perfis e a derivação dos requisitos. |
| [Processo/BPMN - Processo To Be.md](Processo/BPMN%20-%20Processo%20To%20Be.md) | Processo futuro, participantes e regras de leitura. | Síntese e figura BPMN. |
| [Prototipo/Prototipo especificação.md](Prototipo/Prototipo%20especificação.md) | Estrutura das telas e critérios do protótipo. | Síntese das telas e quadros representativos. |
| [Prototipo/Revisao de cobertura e fluxos.md](Prototipo/Revisao%20de%20cobertura%20e%20fluxos.md) | Diagnóstico de coerência, decisões pendentes e roteiro de verificação antes de fechar a prototipação; não substitui as regras vigentes. | Síntese das limitações e do refinamento necessário do guia. |
| [Especificacao/Especificacao do sistema.md](Especificacao/Especificacao%20do%20sistema.md) | Requisitos, regras, dados, rastreabilidade, notificações e aceite. | Síntese de requisitos; catálogo completo mantido na documentação do projeto. |
| [BACKLOG.md](BACKLOG.md) | Marcos, tarefas, responsáveis, dependências e critérios de entrega. | Gestão interna; não integra o artigo. |

Os arquivos `.bpm` e `.png` do processo ficam em `Processo/` como artefatos de edição e figura-fonte.

## Validação e situação da entrega — 19/11/2026

- [Testes funcionais: índice das 17 fichas](Testes%20funcionais/README.md): requisitos, critérios, execução, defeitos, ciclos, retestes e evidências; 16/17 aprovados, 11/11 altas, CT-15 parcial.
- [Usabilidade: roteiro para o grupo](Usabilidade/ROTEIRO.md), [folha dos participantes](Usabilidade/PARTICIPANTE.md) e [ficha de sessão](Usabilidade/FICHA-DE-SESSAO.md): piloto interno e coleta externa planejados, sem resultados inventados.
- [Mapa de implementação e pendências](ESTADO-DA-ENTREGA.md): ponte entre requisitos, código, API e validação.
- [Histórico da revisão da problemática e justificativa](Historico/Revisao%20-%20Problematica%20e%20Justificativa.md): revisão já aplicada em setembro, preservada como registro de decisão.

Os artefatos brutos e os relatórios técnicos permanecem em [SIGE-Desk/docs/qa](../../SIGE-Desk/docs/qa/resultado-catalogo.md); as fichas acadêmicas consolidam sua leitura e não substituem os arquivos de evidência. Os registros de QA não equivalem a aceite assinado pelos integrantes nem a avaliação com participantes.
