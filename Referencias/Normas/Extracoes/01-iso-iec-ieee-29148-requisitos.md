# ISO/IEC/IEEE 29148:2018 — Requirements engineering

- **Link da norma:** [ISO/IEC/IEEE 29148:2018](https://www.iso.org/standard/72089.html)
- **Arquivo-fonte:** `29148-2018.pdf`.
- **Entidade responsável:** ISO / IEC / IEEE.
- **Ano:** 2018.
- **Relevância para o tema:** **5/5**. É a referência mais direta para transformar o problema do Help Desk em requisitos claros, rastreáveis e verificáveis.

## Contexto / Motivação

A norma orienta os processos e produtos de engenharia de requisitos ao longo do ciclo de vida de sistemas e serviços. Seu foco é transformar necessidades de partes interessadas em requisitos de sistema consistentes.

## Problema de pesquisa

Não se aplica. Trata-se de uma norma técnica, e não de estudo empírico.

## Objetivo

Definir características de bons requisitos, processos para levantar, analisar, validar e gerenciar requisitos e informações que devem compor sua especificação.

## Hipótese / questão de pesquisa

Não se aplica.

## Trabalhos relacionados / base teórica

Baseia-se em processos de ciclo de vida e de engenharia de requisitos. Distingue necessidades das partes interessadas, requisitos de sistema/software, restrições, atributos, verificação, validação e rastreabilidade.

## Metodologia

Apresenta orientação normativa e processos iterativos: identificar partes interessadas, compreender o contexto de uso, transformar necessidades em requisitos, analisar conflitos e validar a especificação com os interessados.

## Resultados

Para a redação de requisitos, a norma recomenda declarar **o que** o sistema deve fazer, e não a solução técnica. Termos vagos ou subjetivos devem ser evitados, pois dificultam a verificação. Cada requisito pode registrar atributos como identificador, versão, responsável, prioridade, risco, origem e justificativa.

A validação pode combinar revisão com partes interessadas, prototipação, modelagem e simulação. A rastreabilidade bidirecional liga o requisito às necessidades que o motivaram e aos artefatos que o implementam ou testam.

## Discussão / interpretação

O documento de requisitos já possui identificadores e prioridades, o que está alinhado à norma. Os requisitos de SLA, notificações, controle de acesso, retenção e backup devem permanecer mensuráveis, com critério de aceite e caso de teste correspondente.

## Limitações

Não define um modelo de Help Desk, telas ou prazos de SLA. A aplicação exige adaptação ao contexto da agência e validação com seus usuários.

## Conclusão

A qualidade do requisito depende de linguagem não ambígua, origem conhecida, prioridade e possibilidade de validação. Requisitos e protótipos devem ser revisados de forma iterativa com os perfis envolvidos.

## Contribuição

Oferece fundamento para a Etapa 03 e para uma matriz de rastreabilidade entre problema, objetivo, requisito, critério de aceite e teste.

## Trabalhos futuros

Após a validação com usuários, revisar os requisitos, registrar as mudanças e atualizar suas prioridades e critérios de aceite.

## Validade

É fonte normativa para a organização dos requisitos, não evidência de que uma interface específica será aceita pelos usuários.

## Generalização

Os princípios de clareza, validação e rastreabilidade se aplicam ao SIGE Desk e a outros sistemas; o conjunto concreto de requisitos depende do processo da agência.

## Utilidade para minha pesquisa

Usar uma matriz de rastreabilidade: **situação-problema → objetivo específico → necessidade do perfil → RF/RNF/regra de negócio → critério de aceite → caso de teste**. Registrar também origem, prioridade, responsável e decisão de alteração para cada requisito relevante.

## Observação adicional

Evitar requisitos como “a interface deve ser fácil de usar” ou “o sistema deve responder rapidamente” sem medida. No projeto, preferir formulações verificáveis, como taxa mínima de conclusão das tarefas, prazo de SLA e evidência de notificação registrada.

## Guia rápido — requisitos sem mistério

| Para se situar | Resumo prático |
| --- | --- |
| **Ideia central** | Um requisito é uma necessidade escrita de modo que alguém consiga construir e testar depois. |
| **O que fazer primeiro** | Ouvir Cliente, Atendimento e Gestor de tráfego; registrar o problema de cada perfil. |
| **Como escrever** | Usar “O sistema deve...” + ação observável + condição, quando necessária. |
| **Como conferir** | Perguntar: “Consigo provar que foi atendido por uma tela, registro ou teste?” |
| **Como organizar** | Dar ID, prioridade, origem, critério de aceite e caso de teste. |

**Exemplo aplicado:** em vez de “o sistema deve avisar sobre atrasos”, escrever: “**RF-20:** o sistema deve notificar o responsável e o atendimento quando o prazo de resolução do ticket vencer”. Depois, criar um teste que vence um ticket e confirma a notificação.

**Pergunta para revisar sozinha:** este requisito diz exatamente o que deve acontecer, para quem e como eu verificarei o resultado?
