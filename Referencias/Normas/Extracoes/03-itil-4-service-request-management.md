# ITIL 4 Practice Guide — Service Request Management

- **Arquivo-fonte:** `852634540-Practice-Service-request-management.pdf`.
- **Entidade responsável:** AXELOS / ITIL.
- **Ano:** 2020.
- **Relevância para o tema:** **5/5**. É a referência mais próxima do ciclo de vida das solicitações de campanha tratadas pelo SIGE Desk.

## Contexto / Motivação

O guia trata solicitações iniciadas por usuários para obter, alterar ou acessar algo previamente definido como serviço. Destaca que a solicitação deve ser classificada e atendida por um modelo de requisição apropriado.

## Problema de pesquisa

Não se aplica. É guia de prática de gestão de serviços.

## Objetivo

Orientar o tratamento de solicitações de serviço de forma eficiente, conveniente e alinhada às expectativas dos usuários e aos níveis de serviço acordados.

## Hipótese / questão de pesquisa

Não se aplica.

## Trabalhos relacionados / base teórica

Relaciona catálogo de serviços, modelos de solicitação, automação de fluxo, gestão de nível de serviço, aprovações, colaboração e métricas.

## Metodologia

Descreve processos de classificação, seleção/aplicação do modelo de solicitação, autorização quando necessária, cumprimento, comunicação ao solicitante, revisão e melhoria do modelo.

## Resultados

Solicitações recorrentes podem ter modelos predefinidos com dados obrigatórios, responsáveis, aprovações e prazos. O catálogo deve refletir os SLAs aplicáveis. O guia recomenda manter o solicitante informado sobre o andamento e usar a análise de solicitações para melhorar modelos e automações.

## Discussão / interpretação

Os tipos de demanda do SIGE Desk funcionam como um catálogo inicial: alteração/pausa de anúncio, ajuste de orçamento, alteração de público, aprovação de criativo, relatório e análise de métricas. Cada tipo pode definir formulário, prioridade padrão, necessidade de aprovação, evidência esperada e SLA configurável.

## Limitações

O modelo ITIL pressupõe serviços relativamente padronizados. Algumas demandas de marketing serão excepcionais e precisarão de triagem manual, sem automatizar decisões de campanha.

## Conclusão

Padronizar a entrada reduz informação faltante, melhora a previsibilidade de prazo e facilita medir o atendimento. A padronização deve coexistir com possibilidade de exceção justificada.

## Contribuição

Sustenta o catálogo de tipos de ticket, os campos obrigatórios, as regras de aprovação, os SLAs por prioridade e as notificações de andamento.

## Trabalhos futuros

Após uso piloto, identificar os tipos mais frequentes, revisar formulários e criar modelos apenas para demandas realmente repetitivas.

## Validade

Adequada para desenhar o processo de solicitação; não comprova que os prazos propostos são adequados para a capacidade real da agência.

## Generalização

Os conceitos de catálogo, modelo de solicitação e comunicação se aplicam a diferentes serviços. Os tipos, prazos e aprovações precisam ser configurados para cada agência ou cliente.

## Utilidade para minha pesquisa

Criar, para cada tipo de demanda, uma ficha contendo: dados obrigatórios, campos condicionais, perfil responsável, necessidade de aprovação, SLA, evidência de execução e transições de status permitidas.

## Observação adicional

O status **Aguardando cliente** deve informar o que falta e pausar somente o prazo de resolução; o prazo de primeira resposta não deve ser apagado nem reiniciado.

## Guia rápido — transformar pedidos em tipos de demanda

| Para cada tipo de ticket | O que você deve definir |
| --- | --- |
| Nome | Ex.: “Ajuste de orçamento”. |
| Dados obrigatórios | Campanha, valor atual/proposto, motivo, data desejada. |
| Responsável | Quem normalmente executa ou faz a triagem. |
| Aprovação | Se exige aprovação do cliente antes ou depois da execução. |
| SLA | Prazo de primeira resposta e de resolução. |
| Evidência | O que comprova a entrega: comentário, link, captura ou anexo. |

```mermaid
flowchart LR
    A[Tipo de demanda] --> B[Formulário com dados corretos]
    B --> C[Triagem e SLA]
    C --> D[Execução ou aguardo de aprovação]
    D --> E[Validação, conclusão ou reabertura]
```

**Exemplo aplicado:** “Aprovação de criativo” pode exigir o anexo do criativo, o canal, a campanha e a decisão do cliente; não deve usar o mesmo formulário de “Relatório mensal”.

**Pergunta para revisar sozinha:** que informação costuma faltar nesse tipo de pedido e depois vira retrabalho? Transforme essa informação em campo obrigatório.
