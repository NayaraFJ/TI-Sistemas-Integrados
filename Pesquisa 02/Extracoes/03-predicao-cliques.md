# AI-Powered Advertising Strategy Optimization and Click Prediction for Improved Digital Marketing

- **Link do artigo:** [https://doi.org/10.1109/SMARTGENCON60755.2023.10442343](https://doi.org/10.1109/SMARTGENCON60755.2023.10442343)
- **Autores:** Ashok Kumar.
- **Ano:** 2023.
- **DOI:** `10.1109/SMARTGENCON60755.2023.10442343`.
- **Relevância para o tema:** **4/5**. É útil para métricas, segmentação e decisões de tráfego pago, mas não para o fluxo de atendimento em si.

## Contexto / Motivação

Anúncios direcionados ao público inadequado desperdiçam recursos. Há necessidade de prever probabilidade de clique usando histórico de interações e características de anúncio/usuário.

## Problema de pesquisa

Qual abordagem de aprendizado de máquina prevê melhor cliques em anúncios e quais fatores ajudam a melhorar segmentação e posicionamento?

## Objetivo

Desenvolver e comparar modelos de predição/classificação de clique com dados históricos de interação, atributos demográficos, características do anúncio e contexto.

## Hipótese / questão de pesquisa

Não há hipótese formal. A expectativa é que engenharia de atributos e modelos supervisionados identifiquem padrões de clique e apoiem melhor direcionamento.

## Trabalhos relacionados / base teórica

Parte de CTR, publicidade on-line, segmentação e ML. A lacuna é usar correlação de Pearson e engenharia de atributos para destacar fatores relevantes à predição de clique.

## Metodologia

Dados históricos de interação de anúncio, demografia, atributos do anúncio e contexto. O estudo aplica engenharia de atributos, correlação de Pearson e compara algoritmos de ML, incluindo regressão logística, SVM e redes neurais.

## Resultados

A regressão logística alcançou a melhor acurácia de **97,5%** e **AUC 0,996** no conjunto de teste. Os autores defendem uso do resultado para público-alvo e posicionamento de anúncios.

## Discussão / interpretação

Desempenho alto pode indicar forte sinal nos atributos, mas também exige verificação de separação treino/teste, balanceamento, vazamento de dados e representatividade. Acurácia isolada pode enganar em classes desbalanceadas; AUC ajuda, mas não substitui validação externa.

## Limitações

O resumo não explicita tamanho/origem do conjunto, proporção das classes, protocolo de validação, custos de erro ou teste fora da amostra. Não demonstra que a adoção do modelo aumentou receita em campanhas reais.

## Conclusão

O estudo conclui que predição de clique por ML pode apoiar segmentação e eficiência de anúncio, com melhor resultado para regressão logística na base usada.

## Contribuição

Compara modelos e reporta métricas fortes para o problema de clique, destacando engenharia de atributos/correlação.

## Trabalhos futuros

Validar com bases externas e temporais, reportar precisão/recall/calibração, incluir conversão/valor e testar efeito em campanhas ao vivo.

## Validade

Promissora para classificação na base de teste, mas incompleta para afirmar generalização ou ganho comercial sem detalhes de dados e validação.

## Generalização

Baixa sem validação por cliente, plataforma, período e público. Modelos de clique sofrem drift de campanha e comportamento.

## Utilidade para minha pesquisa

Ajuda a definir campos e métricas para pedidos de otimização: CTR, público, criativo, posicionamento, hipótese e resultado. Uma futura funcionalidade de recomendação deve ser opcional, explicável e auditável.

## Observação adicional

Para Help Desk, o resultado relevante é o processo de solicitar, aprovar, executar e medir uma otimização — não necessariamente implementar ML desde o início.
