**Arquivo fonte:** [the-art-of-software-testing-3rd-edition_2012.pdf](../the-art-of-software-testing-3rd-edition_2012.pdf).
**Título identificado:** The Art of Software Testing, 3rd edition.
**Páginas:** PDF com 314 páginas; identificação nas pp. 7–8; conceitos na p. 24; técnicas nas pp. 75–77 e 83–86; testes funcionais nas pp. 163–165; usabilidade nas pp. 194–209. Todas as localizações abaixo são posições no PDF.
**Observação:** Leitura temática parcial. O arquivo recompõe o texto sem paginação impressa estável; não converter posições do PDF em páginas da edição impressa. A equação na p. 201 foi conferida visualmente; o gráfico da p. 202 não é utilizado quantitativamente nesta ficha. Traduções e paráfrases em português são do leitor.
**DOI:** Não identificado na fonte.
**Autores:** Glenford J. Myers; Corey Sandler; Tom Badgett.
**Link do artigo:** Exemplar local vinculado acima; livro, sem URL editorial verificada nesta extração.
**Ano:** 2012, copyright da edição; a ficha de catalogação também contém 2011, que não foi adotado automaticamente como ano de publicação.
**Relevância para o tema:** 5/5. Fonte central para os dois componentes do quinto tópico: testes funcionais e testes de usabilidade.

## Problema de pesquisa

Não se aplica como problema empírico. Os trechos tratam de como projetar testes para encontrar discrepâncias de comportamento e problemas da interação humana com o software (PDF pp. 24, 163–164 e 194–198).

## Objetivo

Orientar a seleção de casos de teste e a avaliação do software a partir de especificações e de tarefas realizadas por usuários (capítulos 4, 6 e 7).

## Hipótese / questão de pesquisa

Não se aplica: não há hipótese empírica própria sendo testada nos trechos selecionados.

## Trabalhos relacionados / base teórica

### Testes funcionais e caixa-preta

Na abordagem caixa-preta, os dados de teste derivam das especificações; a estrutura interna do programa não constitui a base de seleção. Procura-se identificar situações em que o comportamento observado diverge do especificado (PDF p. 24).

O teste funcional busca discrepâncias entre o programa e sua especificação externa, entendida como descrição do comportamento do ponto de vista do usuário. O livro o caracteriza como normalmente baseado em caixa-preta, ressalvando o caso de programas pequenos. O resultado esperado precisa fazer parte do caso de teste, e entradas inválidas e inesperadas merecem atenção (PDF pp. 163–164).

O propósito enfatizado é revelar erros e discrepâncias, não simplesmente reunir casos que confirmem o funcionamento esperado. **Interpretação do leitor:** a aprovação de um conjunto finito de casos não demonstra ausência de defeitos em todas as situações (PDF pp. 24 e 163–164).

### Equivalência e valores limite

O particionamento de equivalência divide o domínio em classes para as quais se espera comportamento semelhante e seleciona representantes. A identificação de classes válidas e inválidas permite reduzir a quantidade de entradas sem depender de testes aleatórios. A equivalência é uma suposição de projeto dos testes, não garantia de comportamento idêntico (PDF pp. 75–77).

A análise de valor limite enfatiza fronteiras das classes e situações imediatamente acima e abaixo delas; considera tanto entradas quanto saídas. O exemplo de uma quantidade permitida entre 1 e 255 registros usa 0, 1, 255 e 256 para explorar bordas e valores inválidos (PDF pp. 83–85). O exemplo pertence ao livro e não estabelece limites do SIGE Desk.

### Usabilidade

O capítulo 7 considera problemas de fatores humanos, incluindo adequação ao público, clareza das mensagens, consistência, retorno das ações, navegação, erros de uso, aprendizagem e satisfação. Os autores diferenciam observar a interação de apenas solicitar opiniões gerais sobre o produto (PDF pp. 194–198).

Uma interface pode gerar dificuldades mesmo quando as funções foram implementadas. **Síntese do leitor:** testes funcionais examinam conformidade de comportamento; os de usabilidade investigam também como usuários conseguem realizar tarefas e como percebem a interação (PDF pp. 163–164 e 195–199).

## Metodologia

Para testes funcionais, analisar a especificação, derivar casos e definir resultados esperados, incluindo condições inválidas e técnicas de equivalência e limites (PDF pp. 163–164).

Para usabilidade, planejar exercícios realistas e repetíveis; manter instruções equivalentes entre participantes; observar tarefas; registrar dificuldades; complementar com entrevista ou questionário. A seleção deve considerar o público e a familiaridade com o domínio (PDF pp. 198–200).

A coleta pode combinar observação, gravação, verbalização de pensamentos, tempos e respostas posteriores. A presença do observador pode alterar o comportamento; testes remotos têm outras vantagens e perdas de detalhe. Questionários devem ser planejados para obter informações analisáveis e relacionadas às tarefas (PDF pp. 204–207).

## Resultados

Não há resultado empírico próprio do SIGE Desk. O livro apresenta exemplos e discute pesquisas de outros autores. A discussão sobre número de participantes recorre a Nielsen e a questionamentos posteriores; ela não constitui resultado de um teste conduzido por este projeto (PDF pp. 201–204).

## Discussão / interpretação

**Análise do leitor:** separar, nos registros, “a função produziu a saída prevista?” de “o participante conseguiu realizar a tarefa com clareza e esforço aceitável?”. Um formulário pode rejeitar corretamente dados inválidos e ainda apresentar mensagem incompreensível. Esse exemplo é aplicação ao projeto.

## Limitações

Os autores discutem a inviabilidade de testar exaustivamente todas as entradas e a influência de contexto, complexidade, orçamento e perfis no teste de usabilidade. Não sustentam uma regra universal que torne três ou cinco participantes suficientes para qualquer sistema (PDF pp. 24, 75–76, 200–204 e 208–209).

## Conclusão

**Síntese dos trechos:** bons testes derivam de especificações e objetivos explícitos, procuram erros e incluem a experiência de usuários em tarefas reais. Os achados devem orientar correções e novas avaliações (PDF pp. 163–164, 198–199 e 208–209).

## Contribuição

Base para definir testes funcionais, caixa-preta, equivalência, limites e procedimentos de avaliação com usuários.

## Trabalhos futuros

Não se aplica como agenda de estudo empírico próprio. Repetir avaliações após ajustes aparece como orientação prática, não como resultado já realizado pelo projeto (PDF pp. 203–204 e 207–209).

## Validade

**Avaliação do leitor:** cenários padronizados e participantes adequados ajudam a interpretar os dados. As recomendações não validam automaticamente um roteiro local nem um questionário criado pela equipe. Um questionário próprio não pode ser apresentado como UEQ-S ou SUS sem corresponder a esses instrumentos.

## Generalização

Os princípios podem orientar diferentes aplicações; resultados de poucos usuários e de um conjunto de tarefas não representam automaticamente todos os usuários e contextos (PDF pp. 203–204).

## Utilidade para minha pesquisa

**Aplicação proposta:** extrair casos dos requisitos do Help Desk, registrar entrada, condição e saída esperada e separar isso das observações de execução por usuários. Em protótipo navegável, verificar apenas interações disponíveis; não declarar testados persistência, envio real de mensagens ou desempenho de backend inexistente.

## Observação adicional

Referência: MYERS, Glenford J.; SANDLER, Corey; BADGETT, Tom. **The Art of Software Testing**. 3. ed. Hoboken: John Wiley & Sons, 2012. As ideias de Nielsen discutidas aqui foram consultadas por intermédio destes autores; nenhuma leitura direta de Nielsen é presumida.
