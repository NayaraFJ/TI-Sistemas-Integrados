# Machine Learning-Based Classification of the Traffic of Digital Marketing Campaigns

- **Link do artigo:** [https://doi.org/10.1109/MetroXRAINE58569.2023.10405717](https://doi.org/10.1109/MetroXRAINE58569.2023.10405717)
- **Autores:** Sara Abbonizio; Paolo Sernani; Aldo Franco Dragoni; Paolo Rinaldesi.
- **Ano:** 2023.
- **DOI:** `10.1109/MetroXRAINE58569.2023.10405717`.
- **Relevância para o tema:** **5/5**. É diretamente útil para análise de qualidade/fraude do tráfego de campanhas de tráfego pago, embora não trate do atendimento por ticket.

## Contexto / Motivação

Conversões de campanhas podem ser artificiais, geradas por bots ou tráfego fraudulento. Sem distinguir tráfego bom de ruim, métricas e decisões de orçamento ficam comprometidas.

## Problema de pesquisa

É viável classificar automaticamente tráfego de campanhas digitais como genuíno (“good”) ou fraudulento/indevido (“bad”) com aprendizado de máquina?

## Objetivo

Comparar uma Shallow Neural Network e Random Forest para classificação de tráfego de campanhas.

## Hipótese / questão de pesquisa

Não há hipótese formal declarada; o estudo avalia a viabilidade e o desempenho das duas abordagens na classificação.

## Trabalhos relacionados / base teórica

Discute marketing digital, bots, tráfego web, geração de leads, redes neurais e Random Forest. A lacuna é automatizar detecção de qualidade do tráfego em dados reais anotados.

## Metodologia

Modelos supervisionados treinados/avaliados em dados reais de tráfego manualmente anotados. Compara Shallow Neural Network e Random Forest em **32.000 amostras** e avalia precisão e recall.

## Resultados

A Shallow Neural Network atingiu **98% de precisão e 98% de recall**, sem sinal de overfitting reportado, no conjunto testado. Isso demonstra viabilidade técnica da tarefa no conjunto utilizado.

## Discussão / interpretação

Separar qualidade de tráfego evita confundir cliques/conversões de baixa qualidade com sucesso de campanha. Porém, a anotação manual é um ponto crítico: qualidade dos rótulos limita a qualidade do modelo.

## Limitações

O resultado pode depender de uma fonte de tráfego, regras de anotação e período específicos. Mudanças de bots e plataformas causam drift; “sem overfitting” no teste não substitui validação externa/temporal.

## Conclusão

ML é viável para classificar tráfego de campanhas, e a rede neural rasa foi a melhor alternativa no experimento reportado.

## Contribuição

Entrega comparação prática com dados reais anotados e métricas elevadas para um problema importante de integridade de mídia.

## Trabalhos futuros

Validar em diferentes anunciantes/canais/períodos, automatizar ou auditar rótulos, incluir custo de erro e testar modelos contra novos padrões de fraude.

## Validade

Boa para viabilidade no dataset estudado, pois usa dados reais e métricas adequadas. É insuficiente para prometer 98% em qualquer conta ou plataforma.

## Generalização

Moderada-baixa: exige revalidação contínua por canal e contexto de campanha.

## Utilidade para minha pesquisa

É referência central para tickets de “suspeita de tráfego inválido” ou “análise de performance”: registrar fonte, período, métricas, evidências, classificação, responsável e decisão sobre pausa/ajuste. Pode apoiar uma fase futura de detecção assistida.

## Observação adicional

Na primeira versão do sistema, implemente checklist e evidências de análise antes de tentar criar modelo próprio de fraude.
