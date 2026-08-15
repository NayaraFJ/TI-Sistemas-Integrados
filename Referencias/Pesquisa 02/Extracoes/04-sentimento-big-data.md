# AI-Powered Sentiment Analysis for Targeted Digital Marketing Campaigns with Big Data

- **Link do artigo:** [https://doi.org/10.1109/ICET66147.2025.11321368](https://doi.org/10.1109/ICET66147.2025.11321368)
- **Autores:** Fiza Shahzad; Kainat Rizwan; Rana Hassan Ajmal; Mir Wajid Ali; Shua Noreen.
- **Ano:** 2025.
- **DOI:** `10.1109/ICET66147.2025.11321368`.
- **Relevância para o tema:** **3/5**. Tem utilidade para pedidos de análise de sentimento e conteúdo, mas é periférico ao núcleo de gestão de demandas.

## Contexto / Motivação

Marketing orientado por dados depende de compreender sentimento do consumidor em tempo real. Ferramentas convencionais têm dificuldade com sarcasmo, gírias e contexto cultural.

## Problema de pesquisa

Como classificar sentimento de forma mais robusta para apoiar campanhas direcionadas em grande volume de dados?

## Objetivo

Propor um modelo híbrido CNN + BiLSTM + BERT ajustado e avaliar seu uso para análise de sentimento e campanhas orientadas por sentimento.

## Hipótese / questão de pesquisa

Não há hipótese formal; a proposição é que a fusão de extração local, dependência sequencial e embeddings contextuais supere modelos isolados.

## Trabalhos relacionados / base teórica

Baseia-se em CNN, BiLSTM, BERT, análise de sentimentos, big data e marketing personalizado. A lacuna é unir esses componentes para lidar melhor com linguagem contextual.

## Metodologia

Modelo híbrido treinado/avaliado em dados de avaliações de produtos da Amazon. Combina CNN, BiLSTM e BERT fine-tuned; também simula ecossistemas de marketing guiados por sentimento. Declara requisitos éticos: anonimização, GDPR/CCPA, LIME e SHAP.

## Resultados

O modelo alcançou **95,1% de acurácia** nos dados Amazon, superando CNN, BiLSTM e BERT isolados. Em ecossistemas simulados, campanhas guiadas por sentimento tiveram aproximadamente **30%** mais conversão.

## Discussão / interpretação

A arquitetura pode captar diferentes aspectos do texto, mas o resultado comercial vem de simulação, não de experimento de campo. Além disso, desempenho em reviews Amazon pode não transferir para comentários de redes sociais em português.

## Limitações

Falta detalhamento no resumo sobre tamanho/divisão dos dados, métricas por classe e cenário de simulação. Não há validação em campanha real; há riscos de privacidade, viés linguístico e mudança de domínio.

## Conclusão

O híbrido é apresentado como solução escalável e precisa para sentimento em marketing, com potencial de melhorar segmentação.

## Contribuição

Integra três famílias de modelos e coloca explicabilidade/privacidade como componentes do uso de IA de marketing.

## Trabalhos futuros

Validar em dados e campanhas reais, outros idiomas/domínios, medir vieses e comparar impacto com grupos de controle.

## Validade

Boa para desempenho técnico na base descrita; fraca para sustentar aumento de conversão real, pois essa parte decorre de simulação.

## Generalização

Limitada por domínio, idioma e canal. Requer revalidação antes de uso com clientes brasileiros.

## Utilidade para minha pesquisa

Pode fundamentar um tipo de solicitação “análise de sentimento/feedback” e requisitos de privacidade, explicabilidade e consentimento. Não é requisito essencial para a primeira versão do Help Desk.

## Observação adicional

Não use avaliações de modelo como substituto de evidência de retorno financeiro em campanha.
