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

Cliente:       [sua organização / selecionar se Atendimento]
Campanha:      [selecionar campanha ou informar não cadastrada]
Tipo:          [Criação, alteração ou pausa de anúncio ]
Canal:         [plataforma de mídia / outro            ]
Urgência:      [Baixa | Média | Alta | Urgente          ]
Prazo desejado:[dd/mm/aaaa                              ]
Assunto:       [_______________________________________]
Descrição:     [_______________________________________]
               [_______________________________________]
Métricas de contexto (opcional):
               [impressões, CTR, CPC, conversão, CPA ou ROAS]
Anexos:        [Adicionar arquivo]

[Cancelar]                                              [Enviar solicitação]
```

### 2.1.3 Tela de detalhe do ticket

```text
#104 — Ajustar orçamento da campanha X                         [Em validação]
Cliente: Cliente Alfa | Campanha: Campanha X | Responsável: Gestor de tráfego
Prioridade: Alta | Prazo de resolução: 22/09, 16h | Tipo: Ajuste de orçamento

Descrição
Solicitação, contexto, anexos e métricas informadas quando aplicável.

Histórico
22/09 10h12 Atendimento classificou, definiu prioridade e atribuiu responsável.
22/09 14h30 Gestor de tráfego registrou ação executada e evidência.

Comentários e evidências
[Adicionar comentário ou anexo]

Ações conforme o perfil
Cliente: [Enviar complemento] [Aprovar entrega] [Solicitar correção]
Atendimento/Administrador: [Triar] [Solicitar complemento] [Cancelar com motivo]
Gestor de tráfego: [Atualizar status] [Registrar ação e evidência] [Encaminhar para validação]
```

As ações apresentadas ao usuário devem respeitar os estados e as permissões definidos na especificação. Por exemplo, aprovar ou solicitar correção só é permitido ao Cliente quando o ticket estiver em **Em validação**; cancelar exige motivo e só é permitido antes da execução.

## 3. Critérios iniciais de sucesso

O protótipo será considerado adequado à proposta se permitir:

- registrar uma solicitação com o SIGE Desk como registro principal;
- visualizar o status, o responsável e o prazo de cada demanda;
- manter histórico das interações e alterações de status;
- filtrar demandas por cliente, campanha, prioridade e status;
- identificar demandas em atraso ou aguardando resposta do cliente;
- notificar os envolvidos em atribuições, comentários, mudanças de status e vencimentos;
- permitir validar a entrega ou reabrir o ticket com justificativa;
- concluir uma demanda registrando a ação executada.
- exportar uma listagem filtrada para apoio a relatórios.
