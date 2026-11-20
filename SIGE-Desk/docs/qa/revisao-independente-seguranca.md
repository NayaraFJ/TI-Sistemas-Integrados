# Revisão independente de segurança e classificação — 19/11/2026

## Alcance e decisão

Revisão documental e estática dos registros do catálogo, correções, relatórios de execução/interface e pontos de código citados. Não foram feitas novas chamadas HTTP, mutações de dados ou testes de invasão. A revisão não encontrou evidência de defeito crítico aberto nas ocorrências registradas; isso não demonstra ausência de falhas críticas em cenários não exercitados. O gate integral permanece não satisfeito pelas pendências de `resultado-catalogo.md`.

## Ocorrências avaliadas

| Ocorrência | Evidência e classificação | Situação |
| --- | --- | --- |
| QA-DEF-01 — trilha de negativa | A negativa era 404 sem trilha; não há demonstração de vazamento ou alteração indevida na fixture. Severidade relevante e prioridade P2, conforme `correcoes.md`; não se enquadra por si só na definição de crítico do catálogo. | Corrigido e retestado no recorte descrito; matriz de CT-08 continua incompleta. Falha induzida da gravação gera log, mas não evento em tabela: não tratar esse cenário como persistência garantida. |
| QA-DEF-02 — conteúdo de avisos | Atribuição omitia dados exigidos, sem evidência registrada de exposição a destinatário indevido ou perda de dados. Proposta de classificação: severidade moderada e prioridade P2, pela deficiência em informação necessária ao fluxo; não crítico pela definição do catálogo. | Corrigido e retestado em HTTP/MySQL nas fixtures registradas; leitura visual permanece pendente. |
| Orientação de login | `relatorio-interface.md` registra mensagem pedindo conferência de credenciais quando o proxy retornou 403 CORS; a implementação anterior usava a mesma mensagem para qualquer erro da mutation. A causa da rejeição observada foi configuração da origem, mas a orientação exibida induz diagnóstico incorreto. Proposta: severidade baixa, prioridade P3; não é comprometimento de autenticação comprovado. | Corrigida e retestada conforme atualização abaixo; validação visual permanece pendente. |

## Consistência e testes ainda necessários

- A seção “Revisão adicional” no final de `correcoes.md` diz que os retestes de concorrência e falha induzida estão pendentes, enquanto a seção anterior registra sua execução em `regressoes-auditoria-runtime-novo.json` e `falha-gravacao-auditoria-reteste.json`. Trata-se de texto histórico desatualizado, não de prova de que os retestes faltam; preservar a cronologia e sinalizar que foram posteriormente executados.
- Para CT-08, executar por perfil e escopo as consultas e mutações em detalhe, histórico, comentários e anexos, incluindo IDs de recursos vinculados divergentes; conferir resposta, ausência de mudança e, quando aplicável, ator, alvo, instante e evento. Usar base isolada e comparar antes/depois. A execução prévia de 12 negativas concorrentes não comprova toda essa matriz.
- Para login, retestar no navegador indisponibilidade de rede/CORS e tentativa vazia após erro, distinguindo mensagem e recuperação; confirmar que respostas de autenticação não revelem existência de usuário. A sessão visual anterior não comprova esses casos completos.
- Para falha de auditoria, a injeção já registrada comprova resposta genérica e log na fixture, não entrega persistente durante indisponibilidade. Validar operacionalmente detecção e tratamento do log; não afirmar trilha em banco quando a gravação falha.
- Para CT-07, comparar o instante completo de prazo, fuso e precisão de serialização no aviso com a fixture determinística; a evidência atual confere apenas a data.

Fontes: [catálogo](catalogo-funcional.md), [consolidação](resultado-catalogo.md), [correções](correcoes.md), [relatório de interface](relatorio-interface.md) e [relatório de execução](relatorio-execucao.md).

## Atualização das evidências e revisão da correção

A classificação de crítico segue o [catálogo](catalogo-funcional.md): vazamento ou alteração indevida entre escopos, perda ou corrupção irrecuperável, comprometimento de autenticação/autorização ou indisponibilidade total sem recuperação operacional. Prioridade e severidade são avaliadas separadamente.

| Ocorrência e histórico | Classificação e situação atual |
| --- | --- |
| Login: a interface atribuiu a credenciais uma falha de CORS. | Baixa/P3; sem comprometimento de autenticação demonstrado. A [correção](correcao-mensagem-login.md) passou testes de classificação, servidor e build. Reteste HTTP com conta sintética obteve 401 sem cookie para senha inválida e 200 com sessão para credencial válida. Falha interna foi apenas simulada em teste unitário; CORS, apresentação visual e tentativa vazia após erro ainda precisam de reteste em navegador. |
| Uma negativa de execução retornou 403 e incrementou a versão de 6 para 7 em [duas fixtures](pendencias-api-20261119-reteste.json). | Moderada/P2: podia causar conflito em formulário legítimo; não foi demonstrada alteração indevida de conteúdo ou defeito crítico. Após correção, [52 verificações](versao-negativas-visibilidade-reteste.json) passaram nas negativas e execução autorizada exercitadas. Outras mutações não estão abrangidas. |
| Na [primeira corrida de comentários](concorrencia-if-match-independente.json), duas chamadas simultâneas retornaram 201/500, com um comentário e um evento persistidos. | Moderada/P2 pela resposta de conflito; não foi demonstrada perda ou corrupção irrecuperável. [Três corridas posteriores](revisao-concorrencia-independente.md) retornaram 201/409 com uma alteração por fixture; a [disputa no runtime corrigido](concorrencia-visibilidade-reteste.json) também passou. Outras ações concorrentes e interface não foram verificadas. |
| CT-08: [comentário com corpo inválido](ct08-corpo-invalido-20261119.json) para ticket fora do escopo retornou 400 sem evento. O rótulo de aprovação indica conclusão do roteiro observacional, não atendimento do requisito de auditoria. | Relevante/P2; não houve vazamento ou alteração indevida demonstrados. Corrigido e [retestado](ct08-corpo-invalido-reteste.json): Cliente externo e Gestor não atribuído receberam 404 e geraram um evento por tentativa válida ou inválida; inexistente recebeu 404 e proprietário com corpo inválido recebeu 400 sem mudança. Não esgota CT-08. |

A inspeção estática da alteração em `TicketController.checkTicketVisibility` e `TicketService.verifyVisibility` mostra verificação de leitura pela mesma política antes do corpo, com nova verificação na operação; versão e ação permanecem na transação do controlador. Não foi identificada regressão acionável no recorte inspecionado. A [matriz retestada](ct08-matriz-visibilidade-reteste.json) cobre detalhe, lista de anexos, download e comentário negados para Cliente externo e Gestor não atribuído, com trilha. Ainda faltam as demais variantes por perfil e recursos vinculados, inclusive IDs divergentes, e restrição visual. Falha induzida de persistência permanece limitada ao log, como registrado acima.

A [rodada HTTP/MySQL mais recente](execucao-catalogo-visibilidade.json) registrou 17/17 casos e 11/11 altas no recorte automatizado. O [resultado consolidado](resultado-catalogo.md) mantém 17 casos parciais por variantes e evidências faltantes; o gate integral não está satisfeito. Esta revisão não executou HTTP, testes de invasão ou verificação visual novos, e não demonstra ausência de defeitos fora do alcance considerado.
