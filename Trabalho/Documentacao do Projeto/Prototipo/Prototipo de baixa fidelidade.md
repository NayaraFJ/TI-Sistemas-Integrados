# Trabalho Interdisciplinar — Sistemas Integrados de Gestão Empresarial

## Documentação do projeto — Esboço do protótipo de baixa fidelidade

**Aplicação:** Help Desk para Gestão de Demandas de Tráfego Pago  
**Objetivo do protótipo:** demonstrar como clientes e equipe da agência poderão registrar, acompanhar e concluir demandas de campanhas.

## 1. Escopo e regras relacionados

O escopo funcional, os perfis, os estados, as regras de negócio e os acordos de nível de serviço são mantidos uma única vez em [Especificação do sistema.md](../Especificacao/Especificacao%20do%20sistema.md). O fluxo BPMN correspondente está em [BPMN - Processo To Be.md](../Processo/BPMN%20-%20Processo%20To%20Be.md). Este arquivo documenta somente as telas de baixa fidelidade e os critérios específicos do protótipo.

## 2. Esboço do protótipo

### 2.1 Telas representativas

As telas abaixo materializam os caminhos definidos na especificação, sem repetir as regras que governam esses caminhos.

### 2.1.1 Tela inicial — painel de demandas

```text
+--------------------------------------------------------------------------------+
| SIGE Desk                         [Nova solicitação]       [Usuário / Perfil] |
+--------------------------------------------------------------------------------+
| Indicadores: [12 abertas] [4 em execução] [2 em validação] [2 vencidas]      |
+------------------------------+-------------------------------------------------+
| Filtros                      | Minhas demandas                                |
| - Cliente                    | #104 Ajustar orçamento - Cliente Alfa          |
| - Campanha                   | Prioridade: Alta | Em execução | vence 22/09   |
| - Tipo de demanda            |                                                 |
| - Prioridade                 | #105 Aprovar criativo - Cliente Beta           |
| - Status                     | Prioridade: Média | Aguardando cliente         |
+------------------------------+-------------------------------------------------+
| Menu: Dashboard | Demandas | Clientes | Campanhas | Relatórios | Administração |
+--------------------------------------------------------------------------------+
```

### 2.1.2 Tela de abertura de solicitação

```text
Nova solicitação

Cliente:       [selecionar cliente                     ]
Campanha:      [selecionar campanha ou informar nova   ]
Tipo:          [Alteração de campanha                  ]
Canal:         [Google Ads / Meta Ads / outro          ]
Urgência:      [Baixa | Média | Alta | Urgente          ]
Prazo desejado:[dd/mm/aaaa                              ]
Assunto:       [_______________________________________]
Descrição:     [_______________________________________]
               [_______________________________________]
Anexos:        [Adicionar arquivo]

[Cancelar]                                              [Enviar solicitação]
```

## 3. Critérios iniciais de sucesso

O protótipo será considerado adequado à proposta se permitir:

- registrar uma solicitação sem recorrer a mensagens externas;
- visualizar o status, o responsável e o prazo de cada demanda;
- manter histórico das interações e alterações de status;
- filtrar demandas por cliente, campanha, prioridade e status;
- identificar demandas em atraso ou aguardando resposta do cliente;
- notificar os envolvidos em atribuições, comentários, mudanças de status e vencimentos;
- permitir validar a entrega ou reabrir o ticket com justificativa;
- concluir uma demanda registrando a ação executada.
