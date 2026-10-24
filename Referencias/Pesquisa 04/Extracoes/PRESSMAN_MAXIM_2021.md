**Arquivo fonte:** [engenharia-de-software-uma-abordagem-profissional-9a-edicao_2021.pdf](../engenharia-de-software-uma-abordagem-profissional-9a-edicao_2021.pdf).
**Título identificado:** Engenharia de software: uma abordagem profissional, 9ª edição.
**Páginas:** PDF com 1.348 páginas; identificação nas páginas iniciais; seção 19.5 e subseções nas pp. 799–803 do PDF. Foi consultado também o início da seção seguinte na p. 804, apenas para delimitação.
**Observação:** Extração temática parcial sobre testes. A numeração do arquivo recomposto não corresponde à paginação impressa; usa-se a posição no PDF. Há marcas e erros de codificação no texto extraído.
**DOI:** Não identificado na fonte.
**Autores:** Roger S. Pressman; Bruce R. Maxim.
**Link do artigo:** Exemplar local vinculado acima; livro, sem URL editorial verificada nesta extração.
**Ano:** 2021, tradução brasileira; o original em inglês tem copyright de 2020.
**Relevância para o tema:** 5/5. Complementa a definição de testes funcionais e a seleção de entradas para testes de caixa-preta.

## Problema de pesquisa

Não se aplica como problema empírico. O trecho aborda como identificar erros de comportamento com base nos requisitos e no domínio das informações (seção 19.5; PDF pp. 799–800).

## Objetivo

Explicar testes de caixa-preta e técnicas para derivar casos de teste (seções 19.5–19.5.3; PDF pp. 799–803).

## Hipótese / questão de pesquisa

Não se aplica: exposição didática, sem hipótese experimental nos trechos lidos.

## Trabalhos relacionados / base teórica

### Caixa-preta e requisitos

O livro usa as denominações teste de caixa-preta, comportamental e funcional para uma abordagem centrada nos requisitos funcionais e nas condições de entrada. Ela complementa a caixa-branca e pode revelar classes de erros diferentes (PDF p. 799).

São citadas funções incorretas ou ausentes, erros de interface, estruturas de dados ou acesso a dados externos, comportamento/desempenho e inicialização/término. Isso mostra que a abordagem de caixa-preta não precisa ser reduzida ao simples preenchimento de formulários (PDF pp. 799–800; última frase como interpretação).

### Particionamento de equivalência

Divide-se o domínio de entrada em classes de dados, válidas ou inválidas, das quais derivam casos de teste. Uma condição pode ser um valor, um intervalo, um conjunto ou uma condição booleana; o objetivo é selecionar casos que representem classes de comportamento e erros (seção 19.5.2; PDF pp. 801–802).

### Análise de valor limite

A técnica complementa a equivalência ao escolher valores nas bordas das classes e em sua vizinhança. Também examina o domínio de saída. Para um intervalo com limites a e b, os autores orientam testar esses limites e valores imediatamente acima e abaixo; para quantidades, explorar mínimo, máximo e vizinhos (seção 19.5.3; PDF pp. 802–803).

### Interface de componente

O teste de interface descrito na seção 19.5.1 verifica recebimento e retorno de informações em ordem, tipo e formato adequados, em contexto de integração. **Análise do leitor:** essa passagem não deve ser confundida automaticamente com avaliação visual de telas ou experiência do usuário (PDF pp. 800–801).

## Metodologia

Orientações de projeto de casos de teste a partir das condições de entrada, classes e limites. Não é relatado experimento próprio nos trechos selecionados (PDF pp. 799–803).

## Resultados

Não se aplica a achados empíricos. A obra apresenta técnicas e justificativas didáticas, sem medir sua aplicação ao projeto.

## Discussão / interpretação

**Análise do leitor:** esta fonte usa “funcional” como denominação de caixa-preta; Myers ressalva que o teste funcional normalmente utiliza essa abordagem. Para uma definição coerente, explicitar a finalidade de verificar o comportamento funcional e a técnica de selecionar casos pela especificação, sem afirmar que toda forma de caixa-preta é exclusivamente funcional.

## Limitações

**Avaliação do leitor:** uma classificação adequada de entradas depende de requisitos claros. As orientações não definem os limites de campos do SIGE Desk nem garantem que um representante cubra todos os defeitos possíveis de uma classe.

## Conclusão

**Síntese dos trechos:** equivalência e limites permitem selecionar casos de forma orientada, relacionando entradas e saídas ao comportamento esperado (PDF pp. 801–803).

## Contribuição

Complementa Myers com terminologia em português e uma exposição direta de testes baseados nos requisitos.

## Trabalhos futuros

Não se aplica como agenda empírica nos trechos selecionados.

## Validade

**Avaliação do leitor:** a fonte fundamenta as técnicas, mas não valida um conjunto de testes sem examinar sua correspondência com a especificação do produto.

## Generalização

As técnicas podem orientar diferentes sistemas; os valores concretos de teste dependem do domínio e das regras implementadas.

## Utilidade para minha pesquisa

**Aplicação proposta:** para um campo com limite definido na especificação, selecionar entradas válidas, inválidas, nas bordas e próximas delas, sempre indicando a resposta esperada. Nenhum limite numérico novo é estabelecido por esta ficha.

## Observação adicional

Referência: PRESSMAN, Roger S.; MAXIM, Bruce R. **Engenharia de software: uma abordagem profissional**. 9. ed. Porto Alegre: AMGH, 2021. O capítulo de requisitos não foi fichado nesta extração; a base principal desse tópico é Sommerville.
