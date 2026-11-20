# UEQ-S — guia para o moderador e a análise

Versão 1 — 20/11/2026. **Procedimento planejado, sem respostas coletadas neste arquivo.** Entregar aos executores o [questionário](UEQ-S-PARTICIPANTE.md); este guia é para quem conduz e consolida a avaliação.

## O que será medido

O UEQ-S registra a percepção da experiência de uso. Seus oito pares de palavras oferecem duas medidas resumidas: qualidade pragmática (Q1–Q4), relacionada ao apoio para realizar as atividades, e qualidade hedônica (Q5–Q8), relacionada ao interesse e à novidade percebidos. Isso complementa os registros de tarefas; uma resposta favorável não comprova ausência de erro funcional ou execução sem ajuda.

A média dos oito itens pode ser apresentada como síntese complementar, mas não substitui as duas dimensões nem constitui, sozinha, uma nota de aprovação. O [manual oficial, versão 11, páginas 12–13](https://www.ueq-online.org/Material/Handbook.pdf#page=12) recomenda o UEQ completo em testes usuais de usabilidade, pois a versão curta perde detalhe. O projeto mantém o UEQ-S já previsto no protocolo, como medida breve complementar às tarefas e aos comentários; essa escolha limita a análise às dimensões amplas, sem afirmar a mensuração das seis escalas do UEQ completo.

## Quando aplicar

Aplicar **uma vez por executor e sessão**, imediatamente depois de encerrar todo o bloco do papel **e** U-T1/U-T2. Não aplicar oito questionários, um por item, nem um questionário por tarefa. Exemplos de sequência:

- Cliente: cinco tarefas do papel → busca/filtros → notificações → UEQ-S → conversa final.
- Atendimento: duas tarefas do papel → busca/filtros → notificações → UEQ-S → conversa final.
- Gestor: duas tarefas do papel → busca/filtros → notificações → UEQ-S → conversa final.

Não é necessário concluir as tarefas com sucesso para responder. Registrar as tentativas, ajudas e interrupções e encerrar o bloco de forma explícita. Se o ambiente impedir qualquer experiência de uso suficiente, registrar o questionário como não aplicado e justificar; não inventar respostas. Pessoas que apenas moderaram ou observaram não respondem como se tivessem executado as tarefas.

Na rodada inicial, seis executores podem gerar até seis questionários. Em um rodízio adicional, guardar outro registro de sessão com o mesmo código de pessoa. Não tratar respostas repetidas da mesma pessoa como participantes independentes, nem juntar versões diferentes da aplicação sem identificar a mudança.

## Aplicação passo a passo

1. Conferir se a folha corresponde aos oito pares da versão portuguesa oficial. A numeração Q1–Q8 serve somente à identificação; as sete posições são lidas da esquerda para a direita. Não embaralhar itens, inverter lados ou substituir palavras por sinônimos.
2. Encerrar o cronômetro da última tarefa. Registrar o papel, a versão, o uso do tutorial e qualquer ajuda. O tempo do questionário não entra no tempo da tarefa.
3. Entregar o formulário em branco, com código e sessão. Reservar um momento sem consulta às respostas dos outros integrantes.
4. Ler a fala abaixo, sem explicar o que seria uma resposta desejável. A pessoa marca a posição de sua própria impressão em cada linha.
5. Ao receber, conferir somente legibilidade, valores de 1 a 7 e presença de uma única marca por item. Se houver duas marcas, pedir à pessoa que esclareça a escolha, sem sugerir qual. Se não quiser esclarecer ou responder, manter ausência e registrar o motivo conhecido.
6. Preservar o formulário original. Transcrever os valores para a ficha, conferir a transcrição e somente depois calcular as médias.
7. Colher comentários finais após o preenchimento. Não discutir o desempenho coletivo nem mostrar avaliações dos colegas antes de cada resposta individual.

**Fala sugerida do moderador — instrução local do projeto:**

> Agora queremos registrar sua impressão da experiência que acabou de ter com o SIGE Desk. Cada linha contém duas palavras opostas. Marque uma das sete posições, aproximando-se do lado que melhor representa sua experiência nesta sessão. Não há resposta certa ou errada. Responda individualmente, sem consultar os colegas. Você pode deixar de responder se não quiser.

Se surgir dúvida sobre como marcar, explicar apenas o funcionamento da escala. Se houver dúvida sobre um termo, registrar a dúvida e evitar reformular o par com uma interpretação sugerida pelo moderador. A compreensão da versão usada também é uma limitação a registrar.

## Transcrição e conversão

Guardar dois conjuntos separados: respostas originais e valores convertidos. As posições da versão portuguesa conferida têm os polos favoráveis à direita; portanto, cada valor convertido é `resposta original − 4`.

| Posição original | Valor convertido |
| --- | --- |
| 1 | −3 |
| 2 | −2 |
| 3 | −1 |
| 4 | 0 |
| 5 | +1 |
| 6 | +2 |
| 7 | +3 |

Ausência não é posição 4 nem valor 0. Valores fora de 1–7, marcações ambíguas não esclarecidas e respostas ausentes não entram como dados válidos. Manter o original e o motivo de qualquer exclusão, sem corrigir a opinião da pessoa.

## Cálculos e ausências

Este protocolo adota completude por dimensão, como regra interna de análise definida antes da coleta:

- **Pragmática:** média dos quatro valores convertidos Q1–Q4, somente se os quatro forem válidos.
- **Hedônica:** média de Q5–Q8, somente se os quatro forem válidos.
- **Geral:** média dos oito valores, somente se os oito forem válidos; usar como síntese complementar.

Se Q8 estiver ausente, por exemplo, ainda é possível calcular a pragmática completa; hedônica e geral ficam sem cálculo naquela sessão. Não preencher a ausência com média ou neutralidade. Informar `n válido` separadamente para cada estatística. Essa regra de completude é uma decisão do projeto, não uma obrigação atribuída ao instrumento.

**Exemplo fictício para aprender o cálculo — não copiar para os resultados:**

| Item | Q1 | Q2 | Q3 | Q4 | Q5 | Q6 | Q7 | Q8 |
| --- | --- | --- | --- | --- | --- | --- | --- | --- |
| Resposta inventada | 5 | 6 | 6 | 5 | 4 | 5 | 4 | 6 |
| Convertida | 1 | 2 | 2 | 1 | 0 | 1 | 0 | 2 |

Pragmática = `(1 + 2 + 2 + 1) / 4 = 1,50`; hedônica = `(0 + 1 + 0 + 2) / 4 = 0,75`; geral = `9 / 8 = 1,125`, apresentada como `1,13` se forem usadas duas casas. Os cálculos devem manter a precisão original; arredondar somente a apresentação.

Na consolidação, calcular as médias das sessões válidas por papel e rodada. Informar quantos integrantes distintos executaram, quantos questionários foram recebidos, quantos são completos e quantas ausências/exclusões existem. Com dois executores por papel, a comparação é descritiva e limitada. Não chamar a média de percentual de satisfação, não aplicar o limite funcional de 90% ao questionário e não classificar o produto como aprovado apenas por essa média. Não descartar respostas baixas por divergirem das demais.

## O que entregar após a coleta

O observador entrega as fichas preenchidas e os formulários originais em área restrita. A síntese para o artigo contém: número de pessoas e papéis, versão avaliada, tarefas e tempos, médias pragmática e hedônica com quantidades válidas, síntese geral se pertinente, dificuldades recorrentes, melhorias e limitações. Não publicar respostas associadas aos nomes dos colegas. Não executar macros ou confundir a planilha do UEQ de 26 itens com a ferramenta específica de UEQ-S.

Fontes: [pares oficiais em português, página 2](https://www.ueq-online.org/Material/UEQS_Items.pdf#page=2); [Schrepp, Hinderks e Thomaschewski (2017)](https://doi.org/10.9781/ijimai.2017.09.001); [Schrepp, manual oficial, versão 11, 12/09/2023](https://www.ueq-online.org/Material/Handbook.pdf). Consulta aos materiais oficiais: 20/11/2026.
