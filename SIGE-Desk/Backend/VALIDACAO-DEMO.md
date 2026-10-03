# Validação do protótipo com dados demonstrativos

A carga do perfil `demo` persiste dados fictícios no MySQL para testar a aplicação real. A referência das telas T01 a T15 é [Prototipo especificação.md](../../Trabalho/Documentacao%20do%20Projeto/Prototipo/Prototipo%20especificação.md).

## Carga e preservação dos dados

Na pasta `SIGE-Desk/Backend`:

```powershell
$env:SIGE_DEMO_PASSWORD = '123'
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=demo"
```

A carga funciona mesmo se o administrador já existir. O registro `prototype-validation-v1` em `demo_seed_runs` impede a duplicação; os cadastros compatíveis são reutilizados e suas senhas são preservadas. Dados próprios e a sequência dos tickets não são apagados nem reiniciados. Tickets da carga anterior, se existirem, também são mantidos. Ao executar novamente, estados, comentários e alterações feitos durante a validação permanecem como foram salvos. Não é necessário manter o perfil `demo` ativo para consultar os dados depois da carga.

São criados 24 tickets, três por estado, com descrição iniciada por `[DEMO]`, além de:

- Três organizações e seis campanhas ativas; uma organização e uma campanha inativas.
- Seis tipos de demanda e quatro regras de SLA: padrão, por cliente, por tipo e por cliente/tipo.
- Contas dos quatro perfis, com dois responsáveis de tráfego e dois usuários de Atendimento; uma conta inativa.
- Comentários, histórico, notificações lidas e não lidas e métricas de contexto, incluindo zero informado.
- 24 arquivos de contexto e nove evidências CSV, gravados sob `uploads/demo/` (ou `SIGE_STORAGE_PATH`) e disponíveis para download.

## Contas de acesso

Contas novas usam a senha escolhida em `SIGE_DEMO_PASSWORD`, neste exemplo **123**. Contas que já existiam mantêm sua senha anterior.

| Perfil | Login | Escopo esperado |
| --- | --- | --- |
| Administrador | `admin@sige.demo` | Todos os tickets e cadastros administrativos |
| Atendimento | `atendimento@sige.demo` | Todos os tickets, triagem e relatórios |
| Atendimento adicional | `atendimento2@sige.demo` | Mesmo perfil, notificações próprias |
| Gestor de tráfego | `gestor@sige.demo` | Somente demandas atribuídas a Lucas |
| Gestor adicional | `gestor2@sige.demo` | Somente demandas atribuídas a Ana |
| Cliente Aurora | `cliente@aurora.demo` | Somente demandas da Aurora Educação |
| Cliente Horizonte | `rafael@horizonte.demo` | Somente demandas da Horizonte Solar |
| Cliente Viva | `cliente@viva.demo` | Somente demandas da Viva Saúde |
| Conta inativa | `inativo@sige.demo` | Login deve ser recusado |

Saia da sessão antes de trocar de perfil. A aprovação em validação pertence ao Cliente da organização do ticket.

## Roteiro por tela

Os números `SIGE-*` dependem da sequência existente; localize os exemplos pelo assunto na busca.

| Tela | O que conferir | Dados/cenário |
| --- | --- | --- |
| T01 — Acesso | Login válido, inválido e conta inativa; menus por perfil | Contas acima |
| T02 — Painel | Totais, distribuição e recentes respeitando o perfil | Oito estados; prazos recentes e históricos |
| T03 — Lista/Kanban | Oito colunas; busca, filtros de cliente, campanha, prioridade, status, responsável e data; paginação e tabela | 24 tickets; aberturas hoje, há dois e há doze dias |
| T04 — Nova solicitação | Cliente bloqueado para perfil Cliente; cadastro usando tipos, campos adicionais e campanhas disponíveis | Três organizações, seis campanhas e seis tipos; cenário com campanha pendente |
| T05 — Detalhe | Solicitante/autor, contexto, métricas, histórico, comentários e downloads | Todos os tickets; evidências em validação/conclusão/reabertura |
| T06 — Triagem | Iniciar triagem, classificar, atribuir, informar primeiro retorno e confirmar campanha | “Conferir relatório semanal de leads”; “Cadastrar campanha de Black Friday” |
| T07 — Execução | Ações apenas ao gestor atribuído; registrar ação/material e encaminhar conforme o tipo | “Pausar anúncio com oferta encerrada”; “Corrigir campanha com prazo vencido” |
| T08 — Validação | Aprovar ou pedir correção como Cliente; reabrir concluída; distinguir ciclo | “Validar criativo da pós-graduação”; “Corrigir segmentação por região” (ciclo 1); “Reabrir revisão de público após conclusão” (ciclo 2) |
| Complemento | Origem triagem/execução, envio pelo Cliente e conferência antes da retomada | “Informar orçamento autorizado”; “Confirmar URL da página de destino”; “Conferir complemento recebido do cliente” |
| Cancelamento | Motivo no histórico, consulta e ausência de ação de execução | “Cancelar campanha de evento adiado”; “Cancelar alteração duplicada” |
| T09 — Notificações | Listas próprias, lidas/não lidas, marcar como lida e abrir ticket autorizado | Clientes e gestores têm notificações lidas e não lidas; administrador tem pendências |
| T10 — Relatórios | Resumo, filtros, prazos vencidos e exportação do escopo autorizado | Atendimento/Administrador; concluídos com evidência |
| T11 — Clientes | Organizações ativas/inativas e vínculos preservados | Aurora, Horizonte, Viva e Arquivo Studio |
| T12 — Campanhas | Vínculo ao cliente, canal, campanhas ativas/inativas | Seis campanhas ativas e uma arquivada |
| T13 — Tipos | Exigência de aprovação/evidência e campos adicionais | Orçamento, relatório, anúncio, criativo, segmentação e análise |
| T14 — SLA | Escopos, calendário, prazos e snapshots nos tickets | Quatro regras; prazos pendentes, em dia, pausados e vencidos |
| T15 — Usuários | Perfis, organizações dos Clientes, responsáveis e conta inativa | Contas acima |

Registre diferenças entre comportamento/interface e o protótipo durante a inspeção. Ter dados para cada cenário permite testar a cobertura, mas não significa que todas as telas e regras já foram aprovadas. As evidências e métricas são fictícias; a carga não altera campanhas em plataformas externas.
