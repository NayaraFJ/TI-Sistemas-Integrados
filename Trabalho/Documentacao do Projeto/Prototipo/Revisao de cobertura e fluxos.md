# SIGE Desk — Revisão de cobertura e fluxos para prototipação

**Data:** 06/09/2026.  
**Natureza:** revisão documental e registro de consolidação; não é teste de um protótipo implementado.
**Conclusão original:** o inventário de telas era suficiente para começar os esboços, mas os fluxos de tratamento precisavam de consolidação.
**Situação após a correção:** os achados A01–A18 foram incorporados nas fontes canônicas de especificação, processo e orientação de telas em 06/09/2026. Este arquivo preserva as evidências da revisão; em caso de divergência, prevalecem a [Especificação do sistema](../Especificacao/Especificacao%20do%20sistema.md), o [Processo To Be](../Processo/BPMN%20-%20Processo%20To%20Be.md) e o [guia do protótipo](Prototipo%20especificação.md).

## 1. Escopo e fontes

O objeto da revisão é o [guia do protótipo em Markdown](Prototipo%20especificação.md), cruzado com a [especificação do sistema](../Especificacao/Especificacao%20do%20sistema.md), o [levantamento de requisitos](../Levantamento%20de%20requisitos.md), o [processo To Be](../Processo/BPMN%20-%20Processo%20To%20Be.md), o [backlog](../BACKLOG.md) e a [metodologia](../../Artigo/Etapa%2004%20-%20Metodologia.md), também em Markdown. O Overleaf foi consultado para a sincronização exigida pelo projeto. Os PDFs são exportações dos Markdown e não constituem fonte independente nem objeto dos achados deste relatório. Não se trata de auditoria visual ou validação do arquivo nativo BPMN.

As referências abaixo usam códigos e seções para continuarem localizáveis após a edição dos documentos. As propostas foram consolidadas primeiro na especificação canônica e refletidas no processo, guia de telas, critérios de aceite, plano de validação, metodologia e síntese acadêmica. Este relatório não substitui essas fontes.

## 2. Cobertura encontrada

| Dimensão | Evidência encontrada | Interpretação |
| --- | --- | --- |
| Telas | 15 identificadores, de T01 a T15, sem lacunas ou duplicações. | O inventário está organizado; não foi identificada necessidade de criar uma 16ª tela para resolver os problemas desta revisão. |
| Requisitos funcionais | RF-01 a RF-23 aparecem na matriz da seção 7 do guia. | Cobertura nominal de 23/23. Citar a tela não comprova o fluxo completo, a permissão ou o comportamento de exceção. |
| Regras de negócio | A matriz cita 12 dos 14 IDs. RN-08 e RN-09 aparecem no conteúdo das telas, mas não na matriz final. | Lacuna de rastreabilidade, e não ausência total dessas regras no guia. |
| Requisitos não funcionais | Os seis IDs vigentes aparecem: RNF-02, 03, 04, 05, 07 e 09. | As lacunas na numeração não indicam requisitos ausentes. Segurança de armazenamento, autorização real, integridade e compatibilidade não são comprovadas apenas por telas. |
| Estados | Há variantes descritas para os oito estados. | A variante visual existe no planejamento, mas faltam ações de entrada/saída e condições em algumas transições. |
| Jornadas | Principal, complemento, cancelamento e administração estão descritos. | Faltam a separação por sessão/perfil e o detalhamento de caminhos alternativos. |
| Validação | CT-01 a CT-17 estão previstos na metodologia e no arquivo de validação do Overleaf. | São cenários planejados; não há resultado de execução apresentado nesta revisão. |

Pontos a preservar: separação entre urgência informada e prioridade oficial; centralização do ticket em T05; histórico de alterações; distinção entre primeira resposta e resolução; cadastros sem apagar vínculos históricos; uso de dados fictícios; limite explícito contra integração ou execução automática em plataformas de anúncios.

## 3. Pendências prioritárias antes de fechar os fluxos

**P1** indica conflito ou indefinição que pode produzir caminhos incompatíveis, ações indevidas ou dados insuficientes. **P2** indica detalhamento necessário para entregar e testar o protótipo de modo consistente. Essas prioridades são da revisão e não substituem as prioridades dos RF nem o aceite do backlog.

### A01 — P1 — Navegação mistura transferência de trabalho com mudança de tela

**Evidência:** guia, diagrama da seção 5 e passos 3–5 da seção 5.1: T06 encaminha diretamente a T07; T07 a T08; T08 retorna a T07. Entretanto, T06 é de Atendimento/Administrador, T07 é do executor autorizado e a aprovação em T08 é do Cliente.

**Problema:** uma seta pode ser interpretada como redirecionamento da mesma sessão para uma tela ou ação de outro perfil. O diagrama também omite ligações descritas no texto, como T02 → T05 e T05 → T07/T08. T07 permite aguardar cliente, mas essa saída não aparece no desenho.

**Melhoria proposta:** separar o mapa de navegação do ciclo de estados. Após encaminhar, aprovar ou pedir correção, retornar a T05 na sessão atual. A tarefa seguinte deve ser acessada pelo outro perfil por T02/T03/T09 → T05 → ação permitida. Acrescentar voltar/cancelar e preservar os filtros ao retornar à listagem. A troca de usuário da demonstração deve ser externa ao fluxo operacional ou passar por sair/entrar.

**Aceite:** completar o ciclo com contas distintas sem dar ao executor o botão de aprovação do Cliente nem ao Cliente acesso a T07.

### A02 — P1 — Conclusão sem aprovação não existe na matriz de transições

**Evidência:** T07 permite concluir quando o tipo dispensa aprovação; RN-06 também admite dispensa. Já RN-05 exige encaminhar para validação, o Anexo B só permite `Em execução → Aguardando cliente/Em validação` e o To Be sempre passa pela validação do Cliente.

**Problema:** um relatório ou análise de métricas sem aprovação obrigatória tem caminhos contraditórios conforme o documento consultado. A evidência também é incondicional na linha de execução do Anexo B, enquanto T07 a condiciona ao tipo.

**Melhoria proposta:** tornar RN-05 condicional à regra do tipo e incluir `Em execução → Concluída` com ação executada, evidência quando exigida e registro de dispensa derivado da configuração. A dispensa não deve ser uma autorização manual para ignorar aprovação obrigatória. Representar no To Be a decisão “Exige aprovação?”.

**Aceite:** demonstrar um tipo com aprovação e outro sem; impedir conclusão direta quando a aprovação for exigida.

### A03 — P1 — Reabertura conflita com a regra de permissão e omite a retomada

**Evidência:** RN-11 e Anexo B permitem ao Cliente reabrir `Concluída`; RN-04 limita sua exceção a aprovar/corrigir em `Em validação`. T08 lista Cliente somente em `Em validação`, apesar de oferecer reabertura de concluído. O Anexo B exige que Atendimento/Administrador confirme responsável e prioridade em `Reaberta → Em execução`, mas o guia manda diretamente de T08 a T07.

**Melhoria proposta:** explicitar a reabertura em RN-04 e nos perfis/estados de T08. Após a justificativa, mostrar T05 em `Reaberta`; Atendimento/Administrador confirma responsável e prioridade, em uma variante de T06, e só então libera a execução. Diferenciar correção da entrega ainda em validação de reabertura após conclusão. O novo ciclo de SLA está expressamente definido na especificação apenas para esta última; explicitar o efeito da correção para evitar reinício indevido.

**Aceite:** Cliente reabre um concluído e permanece em T05; o executor só retoma após a confirmação prevista. A correção não apaga o trabalho nem a decisão anterior.

### A04 — P1 — Retorno de complemento e cancelamento dependem de uma origem não registrada

**Evidência:** T06 e T07 podem colocar o ticket em `Aguardando cliente`; a seção 5.2 diz que Atendimento retoma em “T06/T05”, sem nome da ação. O Anexo B permite sair de aguardo para triagem, execução ou cancelamento sem distinguir a origem. O To Be e T06 limitam cancelamento a antes da execução.

**Problema:** um ticket pausado durante a execução pode acabar cancelado indevidamente. Um complemento pode retornar à fase errada ou ficar sem uma ação de retomada. Na linha de triagem do Anexo B, prioridade, prazo e responsável parecem exigidos para qualquer saída, inclusive aguardo por informações incompletas.

**Melhoria proposta:** registrar a fase anterior ao aguardo e se a execução já começou. O comentário/anexo do Cliente deve sinalizar “complemento recebido”; Atendimento/Administrador confirma a suficiência e usa “Retomar triagem” ou “Retomar execução”. Se insuficiente, permanece em aguardo com nova solicitação explícita. Cancelar somente se a execução nunca começou. Separar os campos obrigatórios por transição, sem exigir triagem completa para pedir informação.

**Aceite:** testar aguardo originado na triagem e na execução, complemento insuficiente e cancelamento bloqueado após início da execução. A pausa só termina na retomada efetiva, conforme a decisão consolidada.

### A05 — P1 — Autoridade para aprovar e escopo dos perfis estão abertos

**Evidência:** levantamento, seção 3, afirma que Administrador não substitui a aprovação do Cliente nem a execução registrada pelo responsável. T08 e Anexo B incluem Atendimento/Administrador entre os atores de validação. T07 admite esses perfis “quando autorizados”, sem critério. T10 usa “demais perfis que receberem a permissão”. T12 permite Atendimento, mas o texto da seção 5 restringe opções administrativas ao Administrador.

**Problema:** “conforme regra” não permite decidir quais botões desenhar. Também não está definido qual Cliente pode aprovar: qualquer usuário da organização ou somente o solicitante. RF-04 permite cadastro de campanha pelo Atendimento, mas o caminho pode ficar escondido no menu.

**Melhoria proposta:** consolidar uma matriz de perfil × ação × estado × abrangência. Preservar a aprovação do Cliente; se houver registro por Atendimento em nome dele, decidir explicitamente representação, evidência e autoria, sem confundir com aprovação própria. Definir acesso de Atendimento por carteira ou agência e de Gestor por atribuição. Alinhar menu de campanhas ao RF-04 e decidir exportação/relatórios por perfil.

**Aceite:** nenhum botão depende apenas da expressão “quando autorizado”; cada ação tem ator e condição verificáveis, inclusive na abertura direta da tela.

### A06 — P1 — SLA começa antes de existir a prioridade que determina seus prazos

**Evidência:** especificação, seção 5, conta primeira resposta e resolução desde a abertura. RN-01 e T04 deixam prioridade oficial para a triagem. T06 exige primeira resposta efetiva antes de encaminhar, mas não define como esse retorno é registrado. T14 admite regras por cliente/tipo e menciona pausa em validação, sem campo explícito para essa opção.

**Decisões necessárias:** qual regra vale antes da triagem; precedência entre cliente e tipo; efeito de alteração da prioridade, prazo ou calendário sobre tickets existentes; resultado do SLA em cancelamento; distinção entre atraso da primeira resposta e da resolução. Também é preciso esclarecer o “Inicia” da linha `Aberta` do Anexo B para não sugerir que o relógio só começa ao entrar em triagem.

**Melhoria proposta:** escolher uma política inicial explícita, sem equiparar urgência a prioridade automaticamente. Se os prazos só forem calculados na triagem, usar a abertura como origem e indicar antes disso “prazo pendente de classificação”; definir como detectar demora na própria triagem. Se houver regra provisória, documentar sua origem e substituição. Mostrar em T06 a regra aplicada, os prazos calculados e o retorno ao solicitante que efetivamente encerra a primeira resposta. Em T14, definir precedência, vigência e o campo da pausa em validação, caso essa configuração permaneça.

**Aceite:** exemplos com resultado esperado para abertura antes da triagem, fora do expediente, feriado, pausa/retomada, correção, reabertura e alteração de regra. Os exemplos não podem usar valores arbitrários que contradigam a política escolhida.

### A07 — P1 — Campanha não cadastrada tem entrada, mas não tem resolução

**Evidência:** T04 admite “campanha existente ou indicação de não cadastrada”; o Anexo A exige campanha em todos os cinco tipos iniciais. T12 cadastra campanhas; T06 não descreve vincular uma campanha criada depois. RN-02 apenas verifica compatibilidade quando há vínculo.

**Problema:** o Cliente pode encontrar um bloqueio em todos os tipos ou enviar uma demanda sem caminho para completar o vínculo. Não há campo definido para identificar a campanha ainda não cadastrada nem ação posterior de associação.

**Melhoria proposta:** decidir se o vínculo é obrigatório na abertura ou apenas antes da execução. Se aceitar campanha não cadastrada, pedir uma identificação mínima, encaminhar pendência à triagem e permitir a Atendimento/Administrador cadastrar em T12 e vincular em T06, preservando o solicitante e os dados enviados. Se não aceitar, retirar a opção e mostrar orientação quando não houver campanha elegível. Não transformar cadastro de campanha em funcionalidade do perfil Cliente sem revisar as permissões.

**Aceite:** demonstrar cliente sem campanhas, campanha incompatível e campanha cadastrada durante a triagem, caso essa alternativa seja adotada.

### A08 — P1 — Aprovação de criativo pode exigir como evidência a própria decisão futura

**Evidência:** o Anexo A define “Aprovação de criativo” com aprovação obrigatória e evidência esperada “Decisão registrada no ticket”. T07 exige evidência antes de enviar para validação quando o tipo a exigir. O levantamento, seção 4.2, pergunta sobre aprovação anterior à execução, mas não registra uma decisão final para essa exceção.

**Problema:** se a decisão for configurada como evidência obrigatória de execução, a entrega não poderá chegar ao Cliente para ser aprovada. Além disso, “executar” pode ser interpretado como aplicar uma mudança sensível antes da autorização.

**Melhoria proposta:** distinguir material submetido à aprovação (criativo, arquivo ou link e descrição) da decisão produzida em T08. Para esse tipo, definir “execução” como preparação/submissão do material, se essa for a intenção. Documentar se autorização prévia à mudança em plataforma é uma pré-condição externa ou uma etapa do MVP. A regra existente fala em aprovação antes da conclusão; não comprova aprovação antes da execução.

**Aceite:** submeter criativo sem decisão antecipada, obter decisão do Cliente e preservá-la ligada ao material avaliado. Nenhuma ação do protótipo deve afirmar publicação automática na plataforma.

## 4. Lacunas de detalhamento e de entrega

| ID / prioridade | Evidência e efeito | Ajuste recomendado e critério verificável |
| --- | --- | --- |
| A09 / P2 — Início da triagem e edição operacional | Anexo B prevê `Aberta → Em triagem`, mas T05/T06 não nomeiam “Iniciar triagem”. Histórico prevê mudanças de responsável, prioridade e prazo, sem fluxo de edição fora da triagem. | Definir o gatilho explícito de início, salvar/abandonar e a ação de reatribuir/replanejar em cada estado permitido. Abrir uma tela para consulta não deve mudar o status por implicação. Exibir histórico antes/depois e motivo onde exigido. |
| A10 / P2 — Contrato dos formulários | T11 não separa campos obrigatórios; T13 oferece “campos adicionais” sem tipo, opções ou validação; T04 não define solicitante quando Atendimento registra em nome do cliente. | Criar dicionário por campo: nome, tipo, obrigatório/condicional, valores e mensagem. Diferenciar usuário que registrou de solicitante representado. Definir unidade, moeda/período das métricas e diferença entre zero e não informado; formatos/limites de anexos e erro de upload. Não inventar números-limite sem decisão registrada. |
| A11 / P2 — Configuração e inativação com tickets em andamento | T13 diz que a configuração afeta novos tickets, mas não explica preservar a regra antiga em T05/T07. T15 permite inativar responsáveis; T14 permite desativar regra. | Mostrar regra aplicável ao ticket, inclusive após editar o tipo. Definir efeito de inativar cliente/solicitante com validação pendente, responsável com trabalho atribuído e última regra padrão de SLA. Escolher bloqueio com orientação ou substituição/revisão explícita; preservar histórico não resolve sozinho a continuidade. |
| A12 / P2 — Acesso e recuperação | T01 permite e-mail ou usuário, mas T15 só cadastra e-mail e não define primeiro acesso. Recuperação fica a critério da interface. Não há saída de sessão descrita. | Usar e-mail como identificador ou especificar o campo usuário. Definir criação/ativação de acesso, conta inativa, sair e sessão expirada. Se recuperação não entrar no protótipo, retirar o link da demonstração e registrar a delimitação; se entrar, usar variantes com envio/retorno definidos. Não exigir serviço real de e-mail para o esboço. |
| A13 / P2 — Notificações parcialmente especificadas | Anexo D menciona usuários mencionados e e-mail configurável; T05 não prevê menções nem existe local declarado de configuração de e-mail. Cancelamento notifica no To Be, mas não figura no Anexo D/T09. | Definir evento, destinatário, canal e ação. Decidir se menções/e-mail serão representados ou apenas configuração de demonstração. Acrescentar cancelamento à matriz ou alinhar o processo. Demonstrar ticket aberto sem responsável, retorno de complemento e notificação cuja permissão foi retirada. |
| A14 / P2 — Consultas e relatórios | T03 e T10 repetem filtros e exportação. “Período”, “prazo” e “vencido” não especificam a data/relógio usados. T02 exige atalho de nova solicitação para todos, embora T04 tenha só dois perfis. | Fixar o significado dos filtros e contadores, distinguir primeira resposta/resolução e mostrar filtros ativos. Manter T03 para operação e T10 como consulta gerencial com resumo, sem criar novos cálculos. Definir colunas/formato e escopo completo da exportação. Ocultar nova solicitação para perfis sem essa ação. |
| A15 / P2 — Matriz e roteiro de avaliação | RN-08 e RN-09 faltam na matriz do guia. CT-04 não explicita retomada; CT-05 detalha correção, mas não toda aprovação/reabertura após conclusão. Metodologia exige as mesmas cinco tarefas de todos, incluindo ações de perfis distintos. | Acrescentar RN-08 em T05/T06/T07 e RN-09 em T02/T03/T05/T09/T10/T15. Desdobrar cenários sem apagar CT existentes. Distribuir tarefas por perfil ou declarar sessões simuladas separadas; incluir Administração na inspeção funcional. Separar o que o esboço demonstra do que depende de implementação, inclusive teclado, compatibilidade, persistência e segurança. |
| A16 / P2 — Interações após encerramento | T05 permite comentários/anexos sem delimitar estado; a variante Cancelada exige “bloqueio de continuidade”, mas não distingue consulta, nova interação e alteração do trabalho já registrado. T08 permite reabrir Concluída. | Definir se comentários/anexos continuam disponíveis em Concluída e Cancelada e se a retomada do trabalho exige reabertura. Preservar a entrega e a decisão do ciclo encerrado; uma nova interação não deve mudar o status implicitamente. Mostrar as ações restantes em cada variante terminal. |
| A17 / P2 — To Be representa apenas parte das alternativas | O Mermaid do Markdown não mostra conclusão sem aprovação, reabertura de concluído e complemento durante execução. O cancelamento no desenho vem depois da decisão de completude; o guia o permite antes da execução. | Atualizar o fluxo principal e suas alternativas no Markdown. Se o diagrama for deliberadamente resumido, declarar isso e apontar para a matriz completa. Manter os artefatos derivados semanticamente alinhados depois da consolidação das regras. |
| A18 / P2 — Links de entrada no guia | BACKLOG e Levantamento apontavam para o guia fora da subpasta `Prototipo`; o índice também usava um rótulo diferente do nome real do arquivo. | Corrigido nesta revisão: os dois links chegam ao Markdown correto e o índice usa o nome correspondente. Manter uma referência canônica para evitar que a equipe trabalhe com caminhos divergentes. |

## 5. Fluxos propostos para consolidar na especificação

Esta tabela é uma proposta de fechamento, não uma nova matriz vigente. A01–A08 identificam as decisões e conflitos que precisam ser resolvidos para sua adoção. Cada evento deve produzir histórico e as notificações previstas, respeitando a permissão de consulta.

| Origem | Ação e ator propostos | Destino / efeito | Tela e continuidade |
| --- | --- | --- | --- |
| Sem ticket | Cliente ou Atendimento envia solicitação válida. | `Aberta`; número gerado; solicitante e autor identificados. | T04 → T05 da mesma sessão. |
| Aberta | Atendimento/Administrador inicia triagem. | `Em triagem`; início registrado. | T05 → T06. |
| Em triagem | Atendimento/Administrador encaminha com prioridade, SLA e responsável definidos. | `Em execução`. | T06 → T05; responsável abre T07 por T05 em sua sessão. |
| Em triagem | Atendimento/Administrador solicita informação faltante. | `Aguardando cliente`, origem triagem e pausa registradas. | T06 → T05; Cliente recebe a pendência. |
| Em execução | Executor autorizado solicita informação faltante. | `Aguardando cliente`, origem execução e pausa registradas. | T07 → T05. |
| Aguardando cliente | Cliente envia complemento. | Mantém aguardo até conferência, conforme proposta A04. | T05 com confirmação de envio e pendência de conferência. |
| Aguardando cliente | Atendimento/Administrador confere informação suficiente e retoma. | Volta à fase registrada e retoma resolução. | T05/T06; executor acessa T07 quando liberado. |
| Aberta, Em triagem ou aguardo anterior à execução | Atendimento/Administrador cancela com justificativa. | `Cancelada`; sem transição operacional posterior. | T06 → T05; não exigir resposta do Cliente para cancelar. |
| Em execução, tipo com aprovação | Executor registra ação e material/evidência exigidos e envia para validação. | `Em validação`. | T07 → T05; Cliente abre T08 por T05. |
| Em execução, tipo sem aprovação | Executor autorizado conclui com registros obrigatórios e dispensa do tipo. | `Concluída`. | T07 → T05; depende de corrigir RN-05/Anexo B. |
| Em validação | Cliente autorizado aprova a entrega. | `Concluída`; decisão vinculada à entrega. | T08 → T05 do Cliente. |
| Em validação | Cliente autorizado solicita correção justificada. | `Reaberta`; preservar ciclo em curso, conforme política a explicitar em A03. | T08 → T05; Atendimento recebe a pendência. |
| Concluída | Cliente, Atendimento ou Administrador reabre com justificativa. | `Reaberta`; novo ciclo de resolução, sem reiniciar primeira resposta cumprida. | T08 → T05. |
| Reaberta | Atendimento/Administrador confirma responsável e prioridade. | `Em execução`. | Variante T06 → T05; responsável retoma T07. |
| Cancelada | Usuário autorizado consulta. | Mantém estado; sem reabertura/exclusão operacional prevista. | T05 de consulta. |

Na validação, “solicitar complemento” de RF-11 deve ter significado consolidado: decidir se é o mesmo que pedir correção (`Reaberta`) ou um comentário sem mudança de estado. Não criar silenciosamente uma terceira transição para o Cliente.

### 5.1 Jornada de cada perfil

| Perfil | Caminho mínimo demonstrável | Restrição que precisa ficar evidente |
| --- | --- | --- |
| Cliente | T01 → T02 → T04 → T05; T09 → T05 para responder complemento; T05 → T08 para aprovar/corrigir/reabrir no estado permitido. | Própria organização; sem triagem, execução ou mudança livre de prioridade/status. |
| Atendimento | T01 → T02/T03 → T05 → T06; conferir complemento; confirmar retomada de reaberta; cadastrar campanha em T12 conforme RF-04. | Aprovação do Cliente não é substituída por uma aprovação própria. |
| Gestor de tráfego | T01 → T02/T03/T09 → T05 → T07; registrar entrega, pedir informação e acompanhar decisão. | Demandas atribuídas/autorizadas; não entrar automaticamente na sessão do Cliente. |
| Administrador | T01 → T02 → T11–T15; cadastrar base e configurar regras; executar apenas as ações operacionais formalmente autorizadas. | Alterar configuração não pode invalidar silenciosamente o histórico ou substituir a autoria da entrega/decisão. |

## 6. Ajustes esperados por tela

| Tela | Complemento documental necessário |
| --- | --- |
| T01 | Identificador de acesso, conta inativa, primeiro acesso/recuperação delimitados, sair e sessão expirada. |
| T02 | Atalhos por perfil, ligação direta ao detalhe e significado dos totais/prazos. |
| T03 | Data do filtro de período, filtros de SLA, retorno preservado e exportação autorizada. |
| T04 | Solicitante versus autor, campanha pendente, condições de campos por tipo, erros de anexo e destino ao cancelar. |
| T05 | Ações por estado/perfil; iniciar triagem, conferir complemento, retomar, editar atribuição/prazo e consultar decisão; separar decisão do material entregue. |
| T06 | Obrigatórios por ação; salvar/cancelar; primeira resposta efetiva; vincular campanha; retomar aguardo e confirmar reaberta. |
| T07 | Salvar registro sem encerrar, submeter versus concluir, material de criativo, aguardo e retorno a T05. |
| T08 | Estados em que cada ação aparece, autoridade de aprovação, justificativas, confirmação e retorno a T05. |
| T09 | Destinatários/eventos completos; cancelamento; retorno de complemento; leitura e acesso a ticket que deixou de ser permitido. |
| T10 | Permissões, finalidade do resumo, significado de filtros e equivalência da exportação com a consulta. |
| T11 | Campos obrigatórios e efeito da inativação sobre usuários e tickets pendentes. |
| T12 | Entrada pelo Atendimento, vínculo com T06, campanha sem cliente ativo e efeitos de edição/inativação. |
| T13 | Dicionário de campos configuráveis, regra de evidência/aprovação por tipo e preservação da regra de tickets existentes. |
| T14 | Política antes da triagem, precedência, regra padrão, pausa em validação e efeito de editar/desativar. |
| T15 | Primeiro acesso, e-mail duplicado, vínculo de Cliente, alcance dos perfis e inativação com pendências. |

Os casos adicionais podem ser variantes das 15 telas. A prioridade é explicitar comportamento; não é necessário antecipar identidade visual, tecnologia, contrato de API completo ou layout final para resolver esta revisão.

## 7. Roteiro mínimo para conferir o protótipo

Os itens RV abaixo são propostas de verificação documental e de navegação. Complementam os CT existentes; não são testes executados nem novos requisitos automaticamente aprovados.

| ID | Cenário | Resultado a demonstrar | Relação com plano atual |
| --- | --- | --- | --- |
| RV-01 | Abertura válida e abertura com campo obrigatório faltante. | Número/status só após envio válido; dados preservados na correção; autor e solicitante claros. | CT-01, CT-11. |
| RV-02 | Atendimento encaminha e Gestor abre a demanda. | Mudança de ator explícita, responsável registrado e ações corretas em cada conta. | CT-02, CT-03. |
| RV-03 | Complemento pedido na triagem, primeiro insuficiente e depois suficiente. | Motivo visível, aguardo mantido quando insuficiente e retomada à triagem quando conferido. | Amplia CT-04. |
| RV-04 | Complemento pedido durante execução. | Retorno à execução e cancelamento bloqueado porque o trabalho já começou. | Amplia CT-04 e CT-14. |
| RV-05 | Entrega com aprovação obrigatória. | Evidência/material quando exigido; Cliente aprova; executor não aprova no lugar dele. | Amplia CT-05. |
| RV-06 | Entrega sem aprovação obrigatória. | Conclusão direta com ação e dispensa registrada; sem botão para dispensar tipo obrigatório. | CT-05 e CT-09, alternativa ausente. |
| RV-07 | Correção em validação e reabertura de concluído. | Dois cenários separados; justificativa, confirmação de retomada e tratamento correto dos ciclos de SLA. | Desdobra CT-05. |
| RV-08 | Cancelamento de aberta, triagem e aguardo anterior à execução. | Motivo obrigatório; terminal de consulta; não depende de completar a demanda primeiro. | Amplia CT-14. |
| RV-09 | Ticket sem prioridade, fora de expediente, pausado, vencido e reaberto. | Datas e relógios coerentes com a política consolidada; primeira resposta automática não conta. | CT-02, CT-06, CT-10. |
| RV-10 | Campanha inexistente e criativo para aprovação. | Não há bloqueio sem saída nem exigência de uma decisão ainda não produzida. | CT-01, CT-05, CT-09, CT-12. |
| RV-11 | Alterar tipo/SLA e inativar cadastro com ticket em andamento. | Regra anterior e histórico preservados; caminho definido para pendências. | Amplia CT-09, CT-10 e CT-12. |
| RV-12 | Filtros, painel, exportação e tentativa de consultar outra organização. | Resultados coerentes, sem exposição de conteúdo por lista, detalhe, anexo ou notificação. | CT-06, CT-08, CT-13, CT-15, CT-17. |
| RV-13 | Todos os eventos de notificação definidos. | Destinatário, mensagem e destino corretos; notificações sem acesso não revelam conteúdo. | Amplia CT-07. |
| RV-14 | Erro de envio/upload, lista vazia, conta inativa e sessão expirada. | Retorno claro, caminho de recuperação e ausência de mudança de estado fictícia após erro. | CT-03 e CT-11, variantes. |

Preparar dados fictícios com duas organizações, ao menos uma conta por perfil, campanhas ativas/inativas, um tipo com aprovação e outro sem, um criativo, tickets nos oito estados e exemplos separados de aguardo por origem. Registrar a sessão usada em cada passo e um resultado esperado antes de montar as conexões.

## 8. Ordem recomendada e critério de prontidão

1. **Consolidar regras:** resolver A02–A08 na especificação, com decisão, motivo, IDs afetados e responsável pelo aceite a definir pelo grupo. Não delegar essas decisões implicitamente à equipe de interface.
2. **Fechar transições e acesso:** produzir matriz de estados com uma linha por transição e matriz de permissões; resolver A01/A09. Declarar também ações que mantêm o estado, como comentar, salvar evidência e consultar histórico.
3. **Completar o guia:** incorporar os ajustes de T01–T15, RN-08/RN-09 e cenários RV; definir campos e comportamentos de erro/inativação.
4. **Sincronizar a documentação:** atualizar o To Be em Markdown e a síntese acadêmica para refletirem as mesmas decisões do guia e da especificação.
5. **Percorrer as jornadas:** verificar os caminhos com contas/dados fictícios, registrar resultados e então considerar fechado o roteiro da prototipação.

Essas atividades se relacionam a BL-06 (esboço), BL-09 (requisitos), BL-10 (To Be), BL-11 (navegação) e BL-14 (validação), sem alterar automaticamente responsáveis, datas ou situação de aceite.

### Checklist de prontidão

- [ ] Não há divergência entre RN, matriz de transições, To Be e guia para conclusão, correção, reabertura e cancelamento.
- [ ] Cada ação tem perfil, estado de origem, pré-condição, campos obrigatórios, resultado, destino de navegação e retorno em caso de erro.
- [ ] Complemento guarda a origem, define conferência/retomada e não libera cancelamento após execução.
- [ ] Aprovação obrigatória não pode ser dispensada ou realizada por perfil não autorizado.
- [ ] Campanha não cadastrada e aprovação de criativo têm caminhos completos.
- [ ] SLA antes da triagem, precedência de regras e ciclos têm exemplos verificáveis.
- [ ] As 15 telas e variantes permitem percorrer as jornadas com troca explícita de usuário.
- [ ] RF-01–RF-23, RN-01–RN-14 e os seis RNF vigentes têm evidência e limite de verificação identificados.
- [ ] O roteiro de usabilidade respeita os perfis e diferencia avaliação visual de comprovação funcional.
- [ ] Os Markdown de processo, especificação e protótipo descrevem as mesmas decisões, com links válidos entre as fontes.

**Situação após a correção:** diagnóstico concluído, com oito achados P1 e dez P2 tratados. As decisões foram incorporadas sem criar novas telas: a matriz de permissões, o contrato mínimo de formulário, as transições por origem do aguardo, os ciclos de SLA, as variantes e os critérios de aceite agora registram o comportamento antes da prototipação. Os dois links de entrada do guia e o rótulo do índice também permanecem corrigidos. A comprovação do comportamento continuará dependente do protótipo navegável e dos testes previstos.
