# Catálogo funcional revisado — SIGE Desk

## Escopo e fontes

Este documento revisa o desenho dos dezessete casos CT-01 a CT-17. **Não registra execução nem aprovação**. A execução deve produzir registros próprios com os resultados obtidos, evidências e situação final de cada variante. Relatórios técnicos anteriores são referência de regressão e não substituem esta rodada.

Fontes confrontadas: [especificação canônica](../../../Trabalho/Documentacao%20do%20Projeto/Especificacao/Especificacao%20do%20sistema.md), [índice de requisitos](../../../Trabalho/Documentacao%20do%20Projeto/Especificacao/Requisitos.md), [guia de telas T03/T10](../../../Trabalho/Documentacao%20do%20Projeto/Prototipo/Prototipo%20especifica%C3%A7%C3%A3o.md), [fichas acadêmicas](../../../Trabalho/Documentacao%20do%20Projeto/Testes%20funcionais/README.md), [metodologia](../../../Trabalho/Artigo/Etapa%2004%20-%20Metodologia.md) e [revisão técnica anterior](../REVISAO-ENTREGA.md). As prioridades dos casos foram preservadas do catálogo acadêmico; elas não são automaticamente a maior prioridade de qualquer requisito relacionado.

## Condição de aprovação e unidade de contagem

A unidade de contagem é o **caso CT**, não cada chamada HTTP, assertiva, teste Java ou variante. Existem 17 casos: 11 de alta prioridade, 4 de média e 2 de baixa. A meta é a conjunção:

1. 11/11 casos de alta prioridade aprovados;
2. ao menos 16/17 casos aprovados (94,12%, menor número inteiro que satisfaz 90%);
3. nenhum defeito crítico aberto.

Um CT recebe **Aprovado** somente quando todas as variantes mandatórias e resultados esperados daquele CT foram verificados. Uma variante reprovada reprova o CT. Uma variante mandatória sem execução ou evidência deixa o CT **Bloqueado** ou **Não executado/parcial**, sem contá-lo como aprovado. Não reduzir variantes, prioridades ou critérios para atingir o limiar. Justificar adiamento não equivale a aprovação.

Defeito crítico significa, nesta rodada, vazamento/alteração indevida de dados entre escopos, perda ou corrupção irrecuperável de dados, comprometimento da autenticação/autorização ou indisponibilidade total sem caminho operacional de recuperação. P1/P2/P3 devem ser classificados por impacto e urgência separadamente; a ausência de crítico não elimina a obrigação de aprovar todos os CT de alta prioridade.

## Ambiente, evidências e ciclo de trabalho

Executar primeiro em base e diretório de anexos **isolados**, com dados sintéticos identificados por execução. Não usar a publicação compartilhada como base de teste mutável sem isolamento e autorização dos efeitos. Registrar versão/revisão, host/porta, banco efetivo (sem senha), armazenamento, data/hora/fuso, perfis, comando e versões das ferramentas.

Cada registro deve conter: identificador CT e variante; requisitos; prioridade; pré-condições e dados; passos; esperado; obtido; situação final; referência de evidência; executor/revisor; identificador do defeito; correção e execução do reteste, se houver. Redigir o obtido com valores observados, não repetir o esperado. Remover senhas, cookies, CSRF, tokens e dados de pessoas dos anexos de evidência.

O ciclo é **QA reproduz → registra defeito e severidade → especialista corrige → revisão de código avalia o diff → QA reexecuta o caso e regressões relacionadas → recalcula o gate**. Preservar a primeira falha e o reteste; não sobrescrever o histórico com o último resultado. Falha de ambiente ou do próprio teste não comprova defeito de produto nem aprovação.

API com MySQL/arquivos reais pode provar regras e persistência. Testes unitários controlam relógio, calendário e limites com fidelidade para suas regras. Interface requer navegador quando o esperado envolve menus, mensagens, campos preservados, sinalização, gráficos ou download pela tela. Resultado positivo de API não comprova essas manifestações visuais. O relatório deve separar camada verificada e pendências, e não alegar usabilidade com participantes, compatibilidade de três navegadores, acessibilidade integral ou pentest a partir desta rodada funcional.

## Matriz de rastreabilidade

| Caso | Prioridade preservada | Requisitos/regras protegidos | Fluxo |
| --- | --- | --- | --- |
| CT-01 | Alta | RF-05, RF-06, RF-21; RN-01, RN-02 | Abertura e identificação da demanda |
| CT-02 | Alta | RF-07, RF-09, RF-21; RN-02, RN-03, RN-14 | Triagem, classificação e atribuição |
| CT-03 | Alta | RF-08, RF-09, RF-10; RN-04, RN-09; RNF-04 | Execução, comentário e rastreabilidade |
| CT-04 | Média | RF-11; RN-07, RN-08, RN-12 | Complemento na triagem e na execução |
| CT-05 | Alta | RF-11, RF-12; RN-04, RN-05, RN-06, RN-10, RN-11 | Entrega, aprovação, correção e reabertura |
| CT-06 | Média | RF-14, RF-16, RF-21; RN-09, RN-12 | Filtros e situação de SLA |
| CT-07 | Alta | RF-20; RN-09, RN-12, RN-13; anexo D da especificação | Notificações dos eventos do fluxo |
| CT-08 | Alta | RF-02; RN-09; RNF-02 | Isolamento entre organizações e auditoria de negação |
| CT-09 | Alta | RF-22; RN-05, RN-10; seção 8.2 | Configuração e snapshot dos tipos de demanda |
| CT-10 | Alta | RF-23; RF-21; seção 5 da especificação | Calendário, precedência e snapshot do SLA |
| CT-11 | Alta | RF-01, RF-02; RN-04, RN-09; RNF-02 | Autenticação, sessão e funções por perfil |
| CT-12 | Alta | RF-03, RF-04; RF-02; RN-02, RN-09; seção 8.2 | Cadastros e proteção de continuidade |
| CT-13 | Alta | RF-09, RF-17; RNF-04 | Histórico completo e auditável |
| CT-14 | Média | RF-13; RN-07, RN-08; RNF-05 | Cancelamento antes da execução |
| CT-15 | Média | RF-15; RN-09 | Painel e agregações autorizadas |
| CT-16 | Baixa | RF-18; seção 8.2 | Métricas de contexto |
| CT-17 | Baixa | RF-19; RF-14, RF-02; RN-09; seção 8.1 | Exportação da seleção filtrada |

## Casos e variantes mandatórias

### CT-01 — Abertura e identificação da demanda

- **Requisitos:** RF-05, RF-06, RF-21; RN-01, RN-02.
- **Prioridade:** Alta.
- **Pré-condição:** Cliente ativo da organização A; campanha ativa A e cenário de campanha ainda não cadastrada; tipo ativo com campos conhecidos; Atendimento em sessão separada.
- **Passos e variantes mandatórias:**
  1. Cliente abre demanda válida com campanha existente, tipo, canal, urgência, prazo desejado, assunto, descrição e solicitante.
  2. Consulta o ticket criado como Cliente e como Atendimento; abre outra demanda com identificação de campanha pendente.
  3. Repete abertura omitindo obrigatório, usando campanha de outra organização e cliente/campanha inativos.
  4. Atendimento registra demanda em nome de Cliente e confere autor do registro e solicitante representado.
- **Resultado esperado:** Cada envio válido persiste exatamente um ticket com identificador único, estado Aberta, urgência e prazo desejado informados; prioridade/SLA permanecem pendentes de classificação. Atendimento encontra a demanda. Campanha pendente não impede abrir, mas exige identificação. Inválidos são rejeitados sem ticket parcial; autor e solicitante permanecem distintos.
- **Evidência mínima:** IDs/números dos tickets, respostas de criação/consulta, rejeições e contagem antes/depois; tela de abertura e consulta para verificar o retorno ao usuário.
- **Camada necessária:** API com MySQL real; navegador para obrigatoriedade, preservação de campos e identificação do ticket na interface.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-02 — Triagem, classificação e atribuição

- **Requisitos:** RF-07, RF-09, RF-21; RN-02, RN-03, RN-14.
- **Prioridade:** Alta.
- **Pré-condição:** Ticket Aberta, inclusive um com campanha pendente; Atendimento/Admin ativos e Gestor ativo elegível; regra SLA padrão conhecida.
- **Passos e variantes mandatórias:**
  1. Atendimento inicia triagem e consulta o estado anterior à classificação.
  2. Define prioridade, campanha ativa compatível, responsável e regra/prazos de SLA; registra retorno efetivo ao solicitante e encaminha para execução.
  3. Tenta encaminhar sem prioridade, regra, responsável ou campanha compatível; tenta classificar como Cliente/Gestor.
  4. Compara prazos com abertura e calendário aplicado e consulta histórico.
- **Resultado esperado:** Somente Atendimento/Admin classificam e atribuem. Execução exige todos os vínculos confirmados. Prazos são calculados desde a abertura, não desde a triagem. Recebimento automático não conclui primeira resposta; retorno efetivo é identificável. Histórico contém autoria, valores alterados e datas.
- **Evidência mínima:** Snapshots antes/depois, primeira resposta, calendário e cálculo reproduzível, tentativas negativas, histórico e atribuição.
- **Camada necessária:** Integração API/banco e teste de cálculo com relógio controlado; navegador para ações por perfil e exibição de prazos.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-03 — Execução, comentário e rastreabilidade

- **Requisitos:** RF-08, RF-09, RF-10; RN-04, RN-09; RNF-04.
- **Prioridade:** Alta.
- **Pré-condição:** Ticket atribuído e Em execução; Gestor responsável, outro Gestor e Cliente em sessões distintas.
- **Passos e variantes mandatórias:**
  1. Gestor responsável consulta ticket, inclui comentário e anexo válido e executa transição permitida compatível com a regra do tipo.
  2. Consulta comentário, arquivo baixado e histórico com outro usuário autorizado.
  3. Outro Gestor e Cliente tentam a ação reservada à execução.
  4. Envia comando com versão antiga/ausente, quando exigido pelo contrato HTTP.
- **Resultado esperado:** Somente o responsável atribuído executa o trabalho. Comentário/anexo persistem e podem ser recuperados por quem tem acesso; histórico registra usuário, instante e alteração. Negativas não mudam estado. Conflitos não sobrescrevem alteração concorrente e a interface orienta recuperação.
- **Evidência mínima:** Autoria e status, bytes do arquivo, consultas após nova sessão, histórico e rejeições 403/404/409/428 conforme contrato.
- **Camada necessária:** API/banco/armazenamento; navegador para estado desatualizado e retorno visual.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-04 — Complemento na triagem e na execução

- **Requisitos:** RF-11; RN-07, RN-08, RN-12.
- **Prioridade:** Média.
- **Pré-condição:** Dois tickets: um Em triagem e outro Em execução; prazos e ciclo registrados antes do aguardo.
- **Passos e variantes mandatórias:**
  1. Solicita complemento em cada fase com informação necessária e confere origem e início de pausa.
  2. Cliente envia comentário em um ticket e anexo no outro; consulta estado antes da confirmação.
  3. Atendimento/Admin confere suficiência e retoma; tenta retomar sem complemento suficiente.
  4. Tenta cancelar aguardo originado na execução e compara prazos/pausas antes e depois.
- **Resultado esperado:** Aguardando cliente registra motivo, origem e início da pausa. Envio do Cliente sinaliza recebido, mas não retoma automaticamente. Somente Atendimento/Admin confirma suficiência e retorna à fase de origem. Aguardo originado na execução não pode ser cancelado. Pausa altera resolução conforme calendário, sem satisfazer indevidamente primeira resposta.
- **Evidência mínima:** Snapshots de ambos os percursos, intervalo de pausa, complementReceived e histórico de retomada/prazo.
- **Camada necessária:** Integração API/banco/armazenamento e cálculo determinístico; navegador para sinalização de origem e complemento recebido.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-05 — Entrega, aprovação, correção e reabertura

- **Requisitos:** RF-11, RF-12; RN-04, RN-05, RN-06, RN-10, RN-11.
- **Prioridade:** Alta.
- **Pré-condição:** Tipos com aprovação obrigatória e dispensada; variante com evidência obrigatória; Cliente ativo da organização e Gestor atribuído.
- **Passos e variantes mandatórias:**
  1. Tenta entregar sem ação ou evidência exigida; registra entrega válida em tipo que exige aprovação.
  2. Cliente solicita correção com motivo; Atendimento confirma classificação/responsável e retoma execução; compara o ciclo de resolução.
  3. Gestor entrega novamente; Cliente aprova e consulta aprovação, conclusão e histórico.
  4. Cliente, Atendimento ou Admin reabre concluída com justificativa; confirma prioridade/responsável antes da execução e compara novo ciclo e primeira resposta.
  5. Executa entrega de tipo sem aprovação e tenta dispensa manual/aprovação por Atendimento, Admin ou Gestor.
- **Resultado esperado:** Entrega incompleta é rejeitada. Tipo obrigatório entra Em validação e somente Cliente ativo da organização decide. Correção em validação preserva ciclo; reabertura após conclusão inicia novo ciclo e preserva anteriores, sem reiniciar primeira resposta cumprida. Retomada exige confirmação. Tipo sem aprovação conclui com dispensa automática registrada; não existe dispensa manual de obrigação.
- **Evidência mínima:** Percursos completos com estados, aprovação, evidência e históricos; ciclos, prazos e primeira resposta antes/depois; negativas por perfil.
- **Camada necessária:** Integração API/banco/arquivo e navegador para transições, materiais e justificativas.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-06 — Filtros e situação de SLA

- **Requisitos:** RF-14, RF-16, RF-21; RN-09, RN-12.
- **Prioridade:** Média.
- **Pré-condição:** Dados sintéticos conhecidos com pendência de classificação, pausas, primeira resposta vencida, resolução vencida, terminais e organizações distintas.
- **Passos e variantes mandatórias:**
  1. Consulta/faz filtros por cliente, campanha, tipo, prioridade, responsável, status e período de abertura, individualmente e combinados.
  2. Aplica filtro de vencidos em cada perfil; navega páginas e alterna tabela/Kanban.
  3. Compara cada resultado com fixture e regra de SLA; confere pendente, pausado e tipos de vencimento.
- **Resultado esperado:** Resultados coincidem com filtros e escopo. Pendentes não são classificados como vencidos; pausa da resolução não esconde primeira resposta vencida. Terminais preservam situação final. Tabela/Kanban/detalhe e listagem não se contradizem; paginação não altera a seleção lógica.
- **Evidência mínima:** Conjuntos esperados e retornados de IDs, datas e situação calculada; registros visuais dos estados.
- **Camada necessária:** API/banco e cálculo controlado; navegador para indicadores, filtros e alternância.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-07 — Notificações dos eventos do fluxo

- **Requisitos:** RF-20; RN-09, RN-12, RN-13; anexo D da especificação.
- **Prioridade:** Alta.
- **Pré-condição:** Sessões dos envolvidos e de usuários não envolvidos; tickets controlados para cada evento; execução do job de vencimento observável.
- **Passos e variantes mandatórias:**
  1. Dispara atribuição, comentário, aguardo, confirmação de complemento, entrada em validação, vencimento, conclusão, reabertura e cancelamento.
  2. Consulta notificações dos destinatários previstos no anexo D e de quem não pode acessar o ticket.
  3. Marca uma e depois todas como lidas; consulta filtro não lidas e paginação.
  4. Executa novamente o job para o mesmo prazo/ciclo e depois em novo ciclo.
- **Resultado esperado:** Cada evento gera aviso aos destinatários autorizados previstos, com número/resumo e próximo passo pertinentes. Não há vazamento por notificação ou acesso ao link. Leitura e filtro persistem. O mesmo vencimento não duplica aviso; novo ciclo pode gerar novo aviso. E-mail externo é opcional e não é necessário ao caso.
- **Evidência mínima:** Matriz evento × destinatário × notificação, leitura antes/depois, negações e contagem de deduplicação/ciclo.
- **Camada necessária:** API/banco/job real ou relógio controlado; navegador para consulta e leitura de avisos.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-08 — Isolamento entre organizações e auditoria de negação

- **Requisitos:** RF-02; RN-09; RNF-02.
- **Prioridade:** Alta.
- **Pré-condição:** Clientes ativos A/B e Gestores diferentes; ticket A com comentário, anexo, histórico e notificação conhecidos.
- **Passos e variantes mandatórias:**
  1. Cliente B tenta obter ticket A por ID conhecido, lista, anexo/download, histórico, comentário e notificação; repete com Gestor não atribuído.
  2. Tenta mutação direta no ticket e nos recursos vinculados sem usar a interface.
  3. Confere ausência de conteúdo/vínculos indevidos e preservação do ticket.
  4. Localiza registro da tentativa negada conforme política de auditoria e confere ator, instante, recurso e resultado.
- **Resultado esperado:** Acesso e alteração indevidos são negados sem exposição de conteúdo. A organização não é escolhida pelo cliente para ampliar escopo. A tentativa fica registrada conforme política de auditoria, sem registrar senha/cookie/token ou conteúdo protegido. Apenas receber 403/404 não comprova o requisito de registrar a tentativa.
- **Evidência mínima:** Negativas, comparação do estado persistido e registro de auditoria correlacionável. Sem acesso ao registro, a parte de auditoria fica não verificada.
- **Camada necessária:** Integração API/banco/log de auditoria; navegador demonstra restrição visual adicional, sem substituir autorização backend.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-09 — Configuração e snapshot dos tipos de demanda

- **Requisitos:** RF-22; RN-05, RN-10; seção 8.2.
- **Prioridade:** Alta.
- **Pré-condição:** Administrador; tipo novo com campos texto, número, data, seleção e anexo, incluindo condição e limites; ticket existente criado antes da edição.
- **Passos e variantes mandatórias:**
  1. Configura campos e regras de aprovação/evidência; abre ticket com cada tipo de campo válido.
  2. Omite obrigatório simples/condicional, usa número/data/opção inválidos, campo desconhecido ou nome de arquivo sem conteúdo real.
  3. Edita e inativa o tipo; compara ticket anterior e tentativa de nova abertura.
  4. Submete material de Aprovação de criativo antes da decisão do Cliente.
- **Resultado esperado:** Campos tipados, condições, opções e limites são aplicados; inválidos não persistem ticket/arquivo parcial. Ticket existente mantém snapshot de rótulos, campos e regras. Inativo não entra em novo ticket. Aprovação de criativo recebe material primeiro; decisão do Cliente ocorre somente em validação.
- **Evidência mínima:** Definições, resultados positivos/negativos por tipo, dados e arquivos antes/depois de rollback e snapshots antigo/novo.
- **Camada necessária:** API/banco/armazenamento e navegador para editor, formulário e mensagens de validação.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-10 — Calendário, precedência e snapshot do SLA

- **Requisitos:** RF-23; RF-21; seção 5 da especificação.
- **Prioridade:** Alta.
- **Pré-condição:** Administrador; regras padrão, por tipo, cliente e cliente+tipo; calendário/fuso/feriados/recessos e relógio determinístico.
- **Passos e variantes mandatórias:**
  1. Configura e classifica tickets em cada escopo de precedência: combinado, cliente, tipo e padrão.
  2. Calcula prazos em limites de expediente, fim de semana, feriado/recesso, fuso e pausa opcional em validação.
  3. Edita/inativa regra e compara ticket classificado existente com ticket/ciclo novo.
  4. Tenta inativar a última regra padrão; compara snapshot e história dos ciclos.
- **Resultado esperado:** Precedência é combinado > cliente > tipo > padrão. Somente horas úteis do calendário/fuso são contadas. Prazos da classificação derivam da abertura. Pausa em validação obedece snapshot. Alteração/inativação não reescreve tickets/ciclos antigos; novo ciclo usa regra então aplicável. Última regra padrão não pode ser desativada.
- **Evidência mínima:** Entradas/saídas e resultados de cálculo reproduzíveis, snapshots e negativas de inativação.
- **Camada necessária:** Integração API/banco e testes determinísticos de calendário; navegador para configuração e exibição de prazos.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-11 — Autenticação, sessão e funções por perfil

- **Requisitos:** RF-01, RF-02; RN-04, RN-09; RNF-02.
- **Prioridade:** Alta.
- **Pré-condição:** Contas Cliente, Atendimento, Gestor e Admin ativas, conta inativa e credencial inválida; sessões isoladas.
- **Passos e variantes mandatórias:**
  1. Autentica cada perfil e consulta identidade, menus, rotas e tickets permitidos.
  2. Tenta senha inválida, conta inativa e chamada sem sessão; testa rota/comando reservado diretamente.
  3. Efetua logout e reutiliza a sessão anterior; exercita expiração e retorno ao login.
  4. Confere proteção da senha e ausência de credenciais nas respostas/evidências; testa mutação sem proteção CSRF quando exigida pelo contrato.
- **Resultado esperado:** Somente credenciais ativas autenticam. Permissões são aplicadas no servidor além dos menus. Logout invalida sessão e acesso posterior exige autenticação. Sessão expirada tem recuperação clara. Senhas são protegidas e não retornam na API. Falha de autenticação/autorização não concede dados ou funções.
- **Evidência mínima:** Matriz perfil × ação/escopo, respostas, identidade e sessão pós-logout; evidência visual de menus/expiração e verificação técnica sem copiar segredos.
- **Camada necessária:** Integração de segurança/API/banco e navegador para sessão, menus e redirecionamento.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-12 — Cadastros e proteção de continuidade

- **Requisitos:** RF-03, RF-04; RF-02; RN-02, RN-09; seção 8.2.
- **Prioridade:** Alta.
- **Pré-condição:** Admin e Atendimento; organizações/campanhas A/B; usuário atribuído a ticket ativo e organização com único Cliente ativo e ticket aguardando resposta/validação.
- **Passos e variantes mandatórias:**
  1. Admin cria/edita/ativa/inativa cliente; Admin/Atendimento mantém campanha de cliente ativo.
  2. Tenta campanha incompatível, cliente/campanha inativos em abertura e operação por perfil sem permissão.
  3. Tenta inativar responsável com ticket ativo e o último Cliente ativo com pendência; reatribui/mantém outro Cliente e testa novamente.
  4. Consulta tickets existentes após inativação permitida.
- **Resultado esperado:** Cadastros e vínculos persistem e respeitam perfil. Inativos não entram em novos registros; tickets anteriores continuam consultáveis. Inativações que deixariam pendência sem responsável ou Cliente são bloqueadas com caminho de recuperação.
- **Evidência mínima:** Cadastros antes/depois, referências disponíveis, rejeições e comprovação de continuidade dos tickets.
- **Camada necessária:** API/banco e navegador para manutenção, seleção e orientação de bloqueio.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-13 — Histórico completo e auditável

- **Requisitos:** RF-09, RF-17; RNF-04.
- **Prioridade:** Alta.
- **Pré-condição:** Ticket com mudanças identificáveis de status, responsável, prioridade, prazo e aprovação; usuários e instantes conhecidos.
- **Passos e variantes mandatórias:**
  1. Executa as alterações permitidas com seus motivos e identifica versões antes/depois.
  2. Consulta histórico completo, incluindo recalcular prazo na retomada, decisão e reabertura/ciclos.
  3. Confere autoria, instante, campo, valor anterior/novo e motivo com o estado real e garante preservação dos eventos anteriores.
  4. Tenta consulta sem escopo e navega histórico extenso, se houver paginação.
- **Resultado esperado:** Todas as classes de alteração previstas permanecem registradas e correlacionáveis, com sequência coerente e valores completos. Reclassificação/reatribuição e aprovação não apagam história. Usuários sem acesso ao ticket não consultam seu histórico.
- **Evidência mínima:** Matriz alteração × evento esperado × evento obtido, valores reais e sequência histórica completa.
- **Camada necessária:** API/banco real; navegador para apresentação e acesso ao histórico.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-14 — Cancelamento antes da execução

- **Requisitos:** RF-13; RN-07, RN-08; RNF-05.
- **Prioridade:** Média.
- **Pré-condição:** Tickets Aberta, Em triagem, Aguardando cliente por triagem, Em execução e Aguardando cliente por execução.
- **Passos e variantes mandatórias:**
  1. Atendimento/Admin cancela os três estados anteriores à execução com motivo e confirmação.
  2. Tenta cancelar sem motivo/sem confirmação, como Cliente/Gestor e após início da execução.
  3. Consulta cancelado e tenta adicionar comentário/anexo ou retomar execução.
- **Resultado esperado:** Só cancelamentos autorizados antes da execução são persistidos, com motivo, autor e confirmação. Cancelada é terminal de consulta: sem execução, comentário/anexo ou alteração indevida. Prazos/situação final e histórico são preservados.
- **Evidência mínima:** Estados e histórico antes/depois, negativas e consulta do registro terminal.
- **Camada necessária:** API/banco/arquivo e navegador para confirmação, motivo e estado somente consulta.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-15 — Painel e agregações autorizadas

- **Requisitos:** RF-15; RN-09.
- **Prioridade:** Média.
- **Pré-condição:** Fixture com contagens conhecidas por status, prioridade, responsável e situação de prazo, incluindo escopos distintos.
- **Passos e variantes mandatórias:**
  1. Consulta painel por perfil e calcula totais diretamente dos tickets autorizados.
  2. Compara cada distribuição e grupos sem classificação/responsável com o total visível.
  3. Confere partição dos prazos dos tickets ativos e estados vazios; verifica gráficos/rótulos na interface.
- **Resultado esperado:** Totais e distribuições correspondem aos registros autorizados, sem dupla contagem ou conteúdo de outro escopo. Soma das distribuições pertinentes fecha com total; prazos dos ativos formam partição coerente. Interface apresenta os dados da API sem divergência.
- **Evidência mínima:** Dataset de referência, consulta independente, totais calculados e captura dos gráficos.
- **Camada necessária:** Integração API/banco e navegador para gráficos, rótulos e estado vazio.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-16 — Métricas de contexto

- **Requisitos:** RF-18; seção 8.2.
- **Prioridade:** Baixa.
- **Pré-condição:** Cliente solicitante; demanda aplicável com período de referência e métricas conhecidas.
- **Passos e variantes mandatórias:**
  1. Abre/consulta ticket com impressões, CTR, CPC, conversões, CPA e ROAS; inclui zeros e deixa outra métrica vazia.
  2. Envia número inválido/negativo conforme contrato e consulta valores após nova sessão.
  3. Confere período, rótulos e unidade/moeda exibidos.
- **Resultado esperado:** Métricas informadas ficam persistidas sem conversão arbitrária; zero é informado e vazio é ausente. Formatos inválidos são rejeitados sem gravação parcial. Período e unidades ficam compreensíveis na apresentação. Não se exige cálculo automático dos indicadores, fora do MVP.
- **Evidência mínima:** Payloads e valores recuperados, negativas e apresentação das unidades/período.
- **Camada necessária:** API/banco e navegador para distinção zero/vazio, rótulos e unidades.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

### CT-17 — Exportação da seleção filtrada

- **Requisitos:** RF-19; RF-14, RF-02; RN-09; seção 8.1.
- **Prioridade:** Baixa.
- **Pré-condição:** Conjunto maior que uma página, filtros conhecidos e sessões dos quatro perfis.
- **Passos e variantes mandatórias:**
  1. Aplica filtros e exporta CSV; compara cabeçalho, conteúdo, quantidade e IDs com toda a seleção autorizada, não só a página atual.
  2. Repete com resultado vazio, caracteres acentuados e dados que exigem escape CSV.
  3. Atendimento/Admin exporta pela listagem T03 e relatório T10; Cliente/Gestor tenta exportação direta e acesso ao relatório; tenta ampliar escopo por parâmetros.
- **Resultado esperado:** CSV legível e corretamente escapado contém exclusivamente a seleção inteira filtrada/autorizada. Não há vazamento entre organizações/atribuições nem truncamento pela paginação. Somente Atendimento/Admin exporta, conforme ação explícita de T03 e perfis de T10; Cliente/Gestor consulta sua lista restrita, mas a exportação direta/relatório é negada.
- **Evidência mínima:** Arquivo exportado, parse com biblioteca CSV e comparação exata de IDs, filtros e perfil.
- **Camada necessária:** Integração API/banco/formato real; navegador para acionamento e download nos perfis previstos.
- **Resultado obtido:** não executado nesta revisão de desenho; preencher no registro da rodada.
- **Situação final:** planejado, aguardando execução e evidência.

## Decisões e lacunas a resolver sem enfraquecer critérios

1. **Autor de execução:** a matriz operacional da seção 8.1 e RN-04 reservam execução/entrega ao Gestor atribuído, mas a linha “Em execução” do anexo B também lista Atendimento/Admin. Adota-se a matriz detalhada 8.1 para os CT-03/05; a harmonização documental deve ser registrada pelo responsável acadêmico. Se for decidida outra regra, alterar requisito, teste e artigo de forma explícita antes de executar novamente.
2. **Exportação por perfil:** a linha “Consultar relatório e exportar” da matriz 8.1 agrega permissões diferentes e permite leitura ambígua. O guia de telas T03 explicita “Atendimento/Admin exportar resultado filtrado”, e T10 reserva relatório a esses perfis. O CT-17 usa esse detalhamento: Cliente/Gestor consulta a lista restrita e tem a exportação/relatório negados. A harmonização textual da matriz deve explicitar essa distinção, sem ampliar o escopo do MVP.
3. **Tentativas indevidas:** CT-08 e RNF-02 exigem registro da negação conforme política de auditoria. A especificação não fixa tecnologia ou retenção. Log ou registro persistente correlacionável pode servir de evidência, mas o retorno HTTP isolado não demonstra auditoria.
4. **Feriados:** o calendário padrão inclui feriados nacionais e datas locais/recessos configurados. A política implementada BR_FIXED_V1 cobre nacionais de data fixa; feriados religiosos/locais devem ser cadastrados. Testar datas configuradas e a política registrada no snapshot, sem supor calendário externo completo.
5. **Escopo temporal:** testes de vencimento exigem relógio/fixture controlados; esperar tempo real indefinido ou editar dados do ambiente publicado não é um protocolo reproduzível. Evidenciar cálculo e execução do job, incluindo deduplicação.
6. **Catálogo × interface × usabilidade:** o gate acima avalia os CT declarados e as camadas efetivamente verificadas. Avaliação com seis participantes, UEQ-S e homologação Chrome/Edge/Firefox continuam estudos distintos, sem resultados fabricados a partir dos testes automatizados.
