# Execução funcional em ambiente isolado

Em 19/11, o reteste de CT-08 identificou e corrigiu uma ausência de auditoria para requisições com corpo inválido. A descrição, a interpretação da evidência anterior e os resultados posteriores estão em [correções](correcoes.md). O recorte não comprova a interface nem a totalidade das variantes mandatórias.

Após essa correção, a [rodada completa HTTP/MySQL](execucao-catalogo-visibilidade.json) registrou 17/17 casos e 11/11 de alta prioridade no recorte automatizado. O pacote Java coletou 70 testes, sem falhas ou erros e com um teste de integração MySQL ignorado nesse comando. A avaliação integral continua pendente das variantes e evidências de interface.

No runtime atualizado após a correção de precisão, a [rodada completa](execucao-catalogo-runtime-atualizado-20261119.json) voltou a registrar 17/17 casos e 11/11 altas no recorte HTTP/MySQL, inclusive repetição e novo ciclo do agendador em CT-07. A [correlação CT-10](ct10-calendario-runtime-atualizado-reteste-20261119.json) passou 42 verificações com fixture de calendário e pausa; [variantes de requisitos](variantes-requisitos-adicionais.md) preserva método, resultados, tentativas e limites de precisão. Esta rodada não comprova as telas nem encerra o gate integral. Os registros anteriores de notificação, incluindo falha e reteste de QA-DEF-02, permanecem preservados em [correções](correcoes.md) e nos arquivos abaixo.

## Complementos de notificação e auditoria de vínculos

A [execução CT-07 de perfis e reatribuição](ct07-perfis-reatribuicao-20261119.json) verificou igualdade exata dos destinatários na atribuição e no comentário posterior à reatribuição, restrição do gestor anterior, leitura individual em nova sessão e paginação. A fixture exclusiva foi removida; [variantes adicionais](variantes-notificacoes-adicionais.md) detalha os limites, inclusive leitura em lote após nova sessão, outras combinações de perfil e interface.

O [relatório CT-08 de recursos vinculados](relatorio-vinculos.md) registra achados, correção e retestes sequencial e concorrente. O reteste corrigido cobriu Cliente; Gestor não atribuído, interface e outras classes permanecem pendentes nesse recorte. Apó o ajuste, a [nova rodada integral do roteiro HTTP/MySQL](execucao-catalogo-auditoria-vinculos-20261119.json) registrou 17/17 e 11/11 altas, sem suprir essas pendências ou aprovar o gate integral. As fixtures desse roteiro foram mantidas na base isolada.

## Resultado em revisão

A rodada inicial HTTP/MySQL ampliada encontrou **16/17 casos aprovados no recorte automatizado e 10/11 de alta prioridade**, com CT-07 reprovado por conteúdo insuficiente das notificações. Após correção e ajuste do oráculo, o reteste independente [execucao-ct07-reteste-final.json](execucao-ct07-reteste-final.json) obteve **17/17 e 11/11 altas no recorte HTTP/MySQL**. Esse resultado não aprova sozinho o catálogo integral: permanecem pendentes a interface, outras variantes e a revisão de defeitos críticos. A correlação posterior de calendário com uma fixture de API consta acima.

O arquivo `execucao-final.json` registra uma rodada anterior com 17/17 no seu recorte de assertivas. Esse resultado foi revisto quando se fortaleceu o oráculo de CT-07: a consulta inicial aos tipos de evento de toda a base podia aproveitar notificações demonstrativas e não verificava seu conteúdo mínimo. Portanto, aquele 17/17 **não comprova aprovação integral do catálogo**. A rodada `execucao-ct07-antes-correcao.json` é a evidência posterior da falha real, com ticket exclusivo, nove eventos novos e conferência de destinatários.

## Ambiente e reprodução

- Execução: 18/11/2026; horários dos registros incluem fuso ou UTC explícitos.
- API: `http://localhost:8081/api/v1`; MySQL 8.0.40 local, schema exclusivo `sige_desk_qa_20261118`.
- Armazenamento: `Backend/target/qa-uploads`; dados e arquivos sintéticos preservados para auditoria.
- Runtime: cópia `Backend/target/qa-runtime.jar`; SHA-256 registrado em cada JSON. Copiar o artefato evita que um novo empacotamento Maven modifique o arquivo usado pelo processo Java.
- Perfis: Admin, Atendimento, Cliente de Aurora, Cliente de Horizonte, Gestor atribuído e outro Gestor. Não registrar cookies, senhas, tokens ou CSRF nos anexos.
- Comando a partir de `Backend`: `python scripts/qa_functional.py --output ../docs/qa/execucao-reteste.json`.
- O script reutiliza os auxiliares HTTP de `scripts/review_flows.py`, restringe o endpoint à porta local 8081 e verifica o schema MySQL antes da execução. O responsável deve iniciar a API com o mesmo schema, diretório de arquivos e perfil de demonstração isolado.

Não foram alterados banco, arquivos, aplicação ou recursos AWS publicados. O resultado se refere à implementação local, não à versão atualmente publicada. Uma falha inicial de ambiente causada pela recompilação do JAR aberto foi resolvida com cópia independente; não foi classificada como defeito funcional.

## Rastreabilidade da execução

Os requisitos, prioridades, passos e resultados esperados estão em `catalogo-funcional.md`. Cada JSON relaciona suas assertivas ao CT, com horários, obtido, situação e erro. A contagem usa dezessete casos; chamadas HTTP e assertivas não são casos adicionais.

| CT | Evidência efetivamente exercitada por API/MySQL/arquivos | Complemento necessário para aprovação integral |
|---|---|---|
| 01 | Abertura, identificador, campanha pendente, urgência/prioridade, autor distinto do solicitante, vínculos e obrigatórios | Apresentação do formulário e mensagens |
| 02 | Triagem, primeira resposta efetiva, classificação, atribuição, prazos, campos incompletos e perfil indevido | Formulário, mensagens e prazos visíveis |
| 03 | Comentário e anexo do Gestor, download exato pelo Cliente, execução autorizada e histórico | Apresentação dos materiais e transição |
| 04 | Aguardo originado em triagem/execução, complemento real, retomada prematura negada e fase restaurada | Origem, pausa e orientação exibidas |
| 05 | Aprovação, correção no mesmo ciclo, dispensa por snapshot, evidência obrigatória, reabertura em novo ciclo e reclassificação | Transições, decisão e justificativas na interface; confrontar calendário determinístico |
| 06 | Vencidos e escopo, pendência sem vencimento, período vazio; suíte ampliada também confronta filtros individuais/combinados com SQL | Tabela/Kanban e estados de prazo com relógio controlado |
| 07 | Ticket exclusivo gerou eventos do fluxo, inclusive cancelamento; destinatários, conteúdo dos eventos testados, deduplicação e novo ciclo conferidos | Consulta/leitura na interface |
| 08 | Negação entre organizações e Gestores por detalhe, anexo/download e mutação; versão preservada; registros correlacionados no MySQL | Restrição visual complementar |
| 09 | Campos NUMBER/DATE/SELECT/TEXT condicional/FILE, inválidos, multipart e rollback sem órfãos; snapshot de aprovação e rótulos/valores preservados; tipo inativo não aceita nova abertura e ticket anterior é consultável | Editor e mensagens |
| 10 | Quatro precedências em organização/tipo exclusivos, calendário/snapshot, edição/inativação sem retroação, ticket/ciclo novo com regra vigente e primeira resposta preservada; correlação de calendário e pausa em fixture controlada | Configuração e prazos visíveis; outros limites de calendário e fuso não cobertos pela fixture |
| 11 | Login de perfis, conta inativa, credencial inválida, logout, reutilização negada, mutação sem CSRF e leitura de avisos | Menus, expiração e recuperação da sessão na interface |
| 12 | CRUD, vínculos, referências ativas, negativa por perfil, proteção do último Admin/Cliente e Gestor atribuído; recuperação com Cliente substituto/reatribuição e continuidade após inativação permitida; campanha inativa não aceita nova abertura | Caminho de recuperação e mensagens na interface |
| 13 | Versões ausente/desatualizada, reatribuição, mudança de escopo, pausa/retomada e histórico com ator/data/novo valor/motivo | Confrontar todas as classes de alteração e apresentação do histórico |
| 14 | Cancelamento em aberta/triagem/aguardo pré-execução, motivo, perfil, estado terminal e comentário/anexo/retomada negados | Confirmação visual |
| 15 | Totais e partições por seis sessões; cada grupo por status confrontado com consulta independente | Gráficos/rótulos/estado vazio |
| 16 | Métricas persistidas inclusive após nova sessão, rejeição de negativo, zero distinto de vazio | Unidades, rótulos e período visíveis |
| 17 | CSV real parseado; seleção completa comparada por número, vazio, escaping e perfis Admin/Atendimento; Cliente/Gestor negados | Acionamento/download via listagem e relatório |

Essa coluna de complemento não afirma que cada item ainda esteja ausente no projeto: indica o que o executor de API não demonstrou sozinho. A consolidação deve citar a evidência produzida pelo responsável de interface e os testes determinísticos por variante; não substituir uma lacuna por inferência.

As variantes adicionais de CT-06/09/12/14/16 passaram em `variantes-adicionais-reteste.json`; os quatro níveis de precedência e ciclo novo do CT-10 passaram em `variantes-sla-reteste.json`. Foram preservados os primeiros registros de desenvolvimento desses testes: em CT-12 a fixture inicial estava Em triagem, mas o bloqueio de último Cliente exigia pendência Aguardando cliente; em CT-10 o teste inicialmente supôs campo `scope` no snapshot, que contém o identificador da regra. As correções foram no oráculo/pré-condição, com reteste positivo; não são defeitos atribuídos ao produto.

## Ciclo de defeito e reteste

Após ajuste adicional no tratamento de falha de auditoria, o novo runtime passou novamente o teste de 12 negativas concorrentes, com 12 respostas 404 e 12 registros persistidos, preservando a versão do ticket; CT-07 também passou nesse recorte ([regressoes-auditoria-runtime-novo.json](regressoes-auditoria-runtime-novo.json)). A nova rodada completa [execucao-auditoria-runtime-novo.json](execucao-auditoria-runtime-novo.json) registrou 17/17 e 11/11 altas no recorte HTTP/MySQL. O gate integral continua dependente das variantes e interface indicadas em [resultado-catalogo.md](resultado-catalogo.md).

Em [falha-gravacao-auditoria.json](falha-gravacao-auditoria.json), um trigger isolado forçou falha na gravação: a resposta 404 não expôs o assunto, mas incluía o caminho solicitado, e nenhum evento foi persistido. Após o ajuste, [falha-gravacao-auditoria-reteste.json](falha-gravacao-auditoria-reteste.json) registrou 404 equivalente ao de ticket inexistente, exceto pelo horário, sem assunto exposto e com versão preservada. A falha produziu log correlacionável `SECURITY_TICKET_AUDIT_FAILED`; não houve registro na tabela bloqueada. O trigger foi removido em ambas as execuções. Esse resultado demonstra o comportamento da fixture, não persistência no banco durante indisponibilidade da gravação.

**Regressões de notificações e auditoria em 19/11:** `regressoes-notificacoes-auditoria-reteste.json` registra aprovação de três verificações adicionais no runtime atualizado: retomada com informação recebida distinta do motivo de confirmação, fase e data do prazo em cada aviso; correção em validação no ciclo 1 e reabertura após conclusão no ciclo 2; e 12 acessos concorrentes negados com 12 respostas 404, 12 eventos persistidos e versão do ticket preservada. A API foi iniciada com pool Hikari configurado em cinco conexões. A comparação do prazo da notificação verifica a data, não o instante completo.

A primeira execução, `regressoes-notificacoes-auditoria.json`, passou a concorrência e parou no teste de correção por exigir prazo idêntico. A regra aplicada pausava o SLA em validação, permitindo deslocamento do prazo; o oráculo foi ajustado para exigir mesmo ciclo e prazo não antecipado, preservando a evidência original.

A rodada completa posterior `execucao-apos-regressoes.json` aprovou 16/17 casos e 11/11 altas. CT-17 falhou porque o oráculo comparava as referências com somente os primeiros 100 tickets do Gestor. Após corrigir a paginação, o reteste focado `ct17-paginacao-reteste.json` passou. Essa aprovação separada não altera a contagem registrada na rodada completa.

Após a correção do resumo de complemento no histórico, `comentario-longo-multibyte.json` aprovou comentário sintético longo com caracteres multibyte: API e detalhe preservaram o corpo integral; comprimento em caracteres e bytes e SHA-256 conferiram no MySQL; histórico e avisos de retomada preservaram resumo identificável e confirmação. Em seguida, `execucao-runtime-final.json` aprovou 17/17 e 11/11 altas no recorte HTTP/MySQL, incluindo CT-17 com paginação corrigida. Interface e revisão de defeitos críticos continuam sendo avaliadas separadamente.

**QA-DEF-01 / CT-08:** versão anterior retornou 404 sem trilha persistente (`ct08-antes-correcao.json`). O especialista implementou migration V7 e auditoria em transação independente. O reteste HTTP/MySQL comprovou múltiplos eventos para ator/alvo após 404, preservando o ticket (`execucao-rodada1.json`, CT-08, e rodadas posteriores). A correção está aprovada nessa camada. Uma base vazia adicional recebeu V1–V7 e passou o teste de integração (`migration-v7.json`).

**CT-07 / conteúdo de notificações:** `execucao-ct07-antes-correcao.json` observou em SIGE-1079 cinco notificações de atribuição com o mesmo resumo “SIGE-1079 foi atribuído a você.”. Os destinatários estavam no escopo correto, mas faltavam assunto, prioridade e prazo requeridos pelo Anexo D. Após a correção, `execucao-ct07-apos-correcao.json` registrou uma falha do oráculo: exigia o texto e autor do Gestor também nas notificações de comentário do Cliente. O oráculo foi corrigido para conferir cada par de autor e texto; `execucao-ct07-reteste-final.json` passou com eventos e destinatários da fixture, conteúdo de cada aviso testado, deduplicação e novo ciclo. Uma nova fixture em `execucao-ct07-complemento.json` verificou em cada aviso de complemento o ticket, assunto, informação recebida, fase de origem, conferência para retomada e data do prazo aplicável; os cinco avisos passaram. A consulta e leitura pela interface permanecem pendentes.

Em 19/11, o pacote Java final executou 57 testes, sem falhas, com um teste de integração MySQL ignorado nesse comando; a migration foi exercitada separadamente em base vazia conforme registro anterior. `SlaServiceTest` passou 14 testes sem falhas ou ignorados, incluindo recesso configurado e fuso UTC, além do feriado nacional já existente. A consolidação deve comparar as variantes com a API.

O relatório local é `Backend/target/surefire-reports/br.pucminas.sige.sla.application.SlaServiceTest.txt`. No teste de recesso, 24 e 28/12 foram configurados como dias excluídos; em conjunto com o feriado nacional de 25/12 e o fim de semana, a resposta vence em 29/12/2026 às 12h UTC. No teste de fuso UTC, a resolução vence em 29/09/2026 às 13h UTC e o snapshot registra o fuso escolhido.

## Limites e decisão do gate

O limiar inteiro é 11/11 altas e pelo menos 16/17 no total, com zero crítico aberto. Justificativa de adiamento não aprova um CT. `numericGate` nos JSON avalia apenas o recorte ali executado; `criticalOpen` solicita revisão independente em vez de declarar ausência de defeitos por ausência de falhas no script.

Esta rodada não executou avaliação com participantes, UEQ-S, estudo de usabilidade, homologação de três navegadores ou teste de invasão. Nenhuma dessas conclusões pode ser derivada de resultados HTTP ou da existência de testes Java.

## Complemento CT-08 — matriz de auditoria por operação

Em 19/11/2026, `scripts/qa_ct08_matriz_auditoria.py` executou contra a API local `localhost:8081` e MySQL `sige_desk_qa_20261118`: **38 verificações aprovadas** no recorte HTTP/banco. Uma fixture sintética com comentário e anexo foi consultada por Cliente de outra organização e Gestor não atribuído. Para cada perfil, detalhe, listagem de anexos, download e tentativa de criar comentário retornaram 404; cada requisição acrescentou um registro persistente com ator, papel, tipo e ID do ticket, código de negação e instante. O detalhe completo do ticket permaneceu igual antes e depois. Os registros e identificação do runtime estão em [ct08-matriz-auditoria.json](ct08-matriz-auditoria.json). As fixtures foram preservadas na base isolada para reprodução. O resultado não comprova as demais operações vinculadas nem a restrição visual, e não altera a situação integral parcial do caso.

## Complemento CT-03, CT-09, CT-13 e CT-14

O roteiro `scripts/qa_pendencias_execucao.py` produziu [evidência complementar](pendencias-api-20261119-complemento.json) com 155 verificações bem-sucedidas no recorte HTTP/MySQL. CT-03 verificou negativas de execução por Cliente, Atendimento e Admin, recuperação de comentário e anexo por nova sessão e execução pelo Gestor atribuído; CT-09 confrontou sete negativas de campos com ausência de ticket parcial e verificou material antes de aprovação; CT-13 confrontou eventos de classificação, responsável, prazos, decisão e ciclo; CT-14 verificou prazos e histórico no cancelamento em triagem. Essas verificações não cobrem todas as variantes dos casos nem a interface.

**Defeito: negativa de execução incrementa a versão.** Em duas fixtures independentes, um Cliente com acesso ao ticket tentou `POST /tickets/{id}/execution` com versão atual. A API respondeu 403, mas a versão recuperada passou de 6 para 7; esperava-se preservar a versão após a operação recusada. A mudança pode produzir conflito de concorrência em um formulário legítimo. Evidências: [primeira rodada](pendencias-api-20261119.json) e [reteste](pendencias-api-20261119-reteste.json). O recorte complementar verificou status, comentários, anexos e histórico preservados, registrando também as versões antes e depois; não deve ser interpretado como aprovação da invariância de versão. O código sugere que `verifyVersion` registra atividade antes da verificação do papel, hipótese a confirmar na correção. As fixtures e arquivos de evidência permanecem disponíveis no ambiente isolado.

**Reteste após correção:** [versao-negativas-reteste-20261119.json](versao-negativas-reteste-20261119.json) registra 52 verificações bem-sucedidas no runtime identificado pelo hash do arquivo. Cliente, Atendimento e Admin receberam 403 na execução sem alterar versão ou estado; ausência e obsolescência de `If-Match`, estado inválido, cancelamento não autorizado e entrada inválida também preservaram versão. O Gestor atribuído executou com versão válida, alterando estado e versão. A cobertura é limitada às operações verificadas.

**Catálogo após correções:** `scripts/qa_functional.py --output ../docs/qa/execucao-catalogo-versao-corrigida-20261119.json` executou os 17 casos na API local e schema isolado. A [evidência](execucao-catalogo-versao-corrigida-20261119.json) registra 17/17 casos e 11/11 de prioridade alta aprovados no recorte HTTP/MySQL, incluindo o job de notificações com repetição e novo ciclo. Não equivale à aprovação integral do catálogo, que também exige evidências visuais e variantes indicadas em [resultado-catalogo.md](resultado-catalogo.md).

**Runtime 6:** o [reteste de versão](versao-negativas-runtime6-20261119.json) passou 52 verificações, e a [execução integral do roteiro HTTP/MySQL](execucao-catalogo-runtime6-20261119.json) passou 17/17 casos, 11/11 de alta prioridade, inclusive o agendador com repetição e novo ciclo. Os arquivos registram identificação do runtime e fixtures sintéticas com tags distintas das outras rodadas. Mantêm-se os limites da aprovação integral descritos no catálogo.

**Rodada após correção do filtro SLA:** em 19/11/2026, das 14h43 às 14h46 BRT, `qa_functional.py` executou novamente CT-01 a CT-17 no endereço local 8081, schema `sige_desk_qa_20261118`, MySQL 8.0.40 e armazenamento `target/qa-uploads`. O [registro preservado](execucao-catalogo-sla-corrigido-20261119.json) identifica a execução `Review a0f70be8` e o runtime SHA-256 `8e38886b7a655b48fd514648113897ef1ff6d0dd32dd869eec1df16871d261ea`. Os 17 casos do roteiro passaram, inclusive os 11 de alta prioridade; CT-07 observou repetição do job e novo ciclo. Não houve falha no recorte executado. O resultado numérico do roteiro não comprova ausência de defeito crítico nem aprovação integral: permanecem necessárias revisão independente e as variantes, camadas visuais e limites indicados em [resultado-catalogo.md](resultado-catalogo.md).
