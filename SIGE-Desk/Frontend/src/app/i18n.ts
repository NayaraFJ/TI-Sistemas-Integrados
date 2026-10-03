import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

void i18n.use(initReactI18next).init({
  resources: { 'pt-BR': { translation: {
    app: { name: 'SIGE Desk', tagline: 'Demandas claras. Operação sob controle.' },
    actions: { login: 'Entrar', logout: 'Sair', loggingOut: 'Saindo…', menu: 'Menu', create: 'Criar ticket', cancel: 'Cancelar', save: 'Salvar', back: 'Voltar', search: 'Buscar', clear: 'Limpar', all: 'Todos', yes: 'Sim', no: 'Não', open: 'Abrir', download: 'Baixar', export: 'Exportar CSV', kanban: 'Kanban', table: 'Tabela', approve: 'Aprovar entrega', correction: 'Solicitar correção', send: 'Enviar', attach: 'Adicionar anexo', startTriage: 'Iniciar triagem', triage: 'Fazer triagem', execute: 'Registrar execução', resume: 'Retomar fluxo', wait: 'Solicitar complemento', reopen: 'Reabrir ticket', markRead: 'Marcar como lida' },
    nav: { dashboard: 'Visão geral', tickets: 'Tickets', notifications: 'Notificações', reports: 'Relatórios', clients: 'Clientes', campaigns: 'Campanhas', demandTypes: 'Tipos de demanda', sla: 'Configuração de SLA', users: 'Usuários e perfis' },
    login: { title: 'Acessar o ambiente', subtitle: 'Entre com suas credenciais para acompanhar as demandas autorizadas.', email: 'E-mail', password: 'Senha', invalid: 'Não foi possível iniciar a sessão. Confira suas credenciais.' },
    dashboard: { title: 'Visão geral', active: 'Tickets ativos', priority: 'Alta prioridade', validation: 'Em validação', waiting: 'Aguardando cliente', overdue: 'SLA vencido', slaHealth: 'Saúde do SLA', classified: '{{count}} ticket(s) classificados', recent: 'Atividade recente', distribution: 'Distribuição por status' },
    tickets: { title: 'Tickets', subtitle: 'Acompanhe o fluxo da sua operação.', newTitle: 'Novo ticket', detail: 'Detalhe do ticket', number: 'Ticket', subject: 'Assunto', client: 'Cliente', campaign: 'Campanha', type: 'Tipo de demanda', channel: 'Canal', urgency: 'Urgência informada', priority: 'Prioridade oficial', status: 'Status', assignee: 'Responsável', updated: 'Atualização', createdFrom: 'Criado a partir de', createdTo: 'Criado até', description: 'Descrição', desiredDate: 'Data desejada', requester: 'Solicitante', author: 'Autor do registro', sla: 'SLA', history: 'Histórico', comments: 'Comentários', attachments: 'Anexos e evidências', additionalFields: 'Informações específicas da demanda', metrics: 'Métricas de contexto', metricsHint: 'Preencha apenas os indicadores disponíveis para esta solicitação.', noItems: 'Nenhum ticket foi encontrado.', searchPlaceholder: 'Número, assunto ou cliente', pendingCampaign: 'Campanha não cadastrada', actionDescription: 'Descrição da ação executada', evidenceUrl: 'Link da evidência', reason: 'Motivo ou observação', effectiveResponse: 'Conta como primeira resposta efetiva', waitReason: 'Informação necessária', complement: 'Complemento', page: 'Página {{current}} de {{total}}' },
    status: { OPEN: 'Aberta', TRIAGE: 'Em triagem', EXECUTION: 'Em execução', WAITING_FOR_CLIENT: 'Aguardando cliente', VALIDATION: 'Em validação', DONE: 'Concluída', REOPENED: 'Reaberta', CANCELLED: 'Cancelada' },
    priority: { URGENT: 'Urgente', HIGH: 'Alta', MEDIUM: 'Média', LOW: 'Baixa' },
    role: { CLIENT: 'Cliente', SERVICE: 'Atendimento', TRAFFIC_MANAGER: 'Gestor de tráfego', ADMIN: 'Administrador' },
    messages: { logoutError: 'Não foi possível sair. Tente novamente.', loading: 'Carregando dados…', error: 'Não foi possível carregar os dados.', validation: 'Revise os dados informados.', business: 'A solicitação não atende às regras de negócio.', conflict: 'A ação não é permitida no estado atual do ticket.', saved: 'Alteração salva.', required: 'Preencha este campo.', nonNegative: 'Informe um valor numérico maior ou igual a zero.', forbidden: 'Você não tem permissão para esta ação.' },
    metrics: { period: 'Período', impressions: 'Impressões', ctr: 'CTR (%)', cpc: 'CPC (R$)', conversions: 'Conversões', cpa: 'CPA (R$)', roas: 'ROAS' },
    notifications: { title: 'Notificações', empty: 'Você não tem notificações.' },
    tutorial: { open: 'Ver tutorial', title: 'Tutorial do SIGE Desk', progress: 'Etapa {{current}} de {{total}}', previous: 'Anterior', next: 'Próximo', finish: 'Concluir', close: 'Fechar', steps: {
      CLIENT: [
        { title: 'Acompanhe sua operação', text: 'A visão geral reúne os tickets da sua organização e destaca os itens que precisam da sua atenção.' },
        { title: 'Abra uma solicitação', text: 'Em Tickets, use Criar ticket para registrar uma demanda com campanha, tipo, descrição e anexos.' },
        { title: 'Acompanhe o fluxo', text: 'Alterne entre Kanban e tabela, aplique filtros e abra um ticket para consultar detalhes e histórico.' },
        { title: 'Responda às atualizações', text: 'As notificações indicam pedidos de complemento e entregas que aguardam sua validação.' }
      ],
      SERVICE: [
        { title: 'Leia os indicadores', text: 'A visão geral mostra o volume operacional, prioridades, itens em validação e dependências do cliente.' },
        { title: 'Organize a fila', text: 'Em Tickets, o Kanban e os filtros ajudam a encontrar a demanda que precisa de triagem.' },
        { title: 'Faça a triagem', text: 'Abra o ticket e inicie a triagem para definir campanha, tipo, prioridade, responsável e prazo de resposta.' },
        { title: 'Acompanhe resultados', text: 'Relatórios e campanhas ajudam a acompanhar o volume, a conclusão e o cumprimento do SLA.' }
      ],
      TRAFFIC_MANAGER: [
        { title: 'Veja sua fila', text: 'A visão geral considera os tickets atribuídos a você e aponta o que exige atenção.' },
        { title: 'Abra demandas atribuídas', text: 'No Kanban, abra tickets em execução para consultar contexto, prazo, comentários e anexos.' },
        { title: 'Registre a execução', text: 'Descreva o trabalho realizado e inclua a evidência antes de enviar a demanda para validação ou conclusão.' },
        { title: 'Acompanhe os retornos', text: 'Notificações informam novas atribuições, pedidos de correção e conclusões.' }
      ],
      ADMIN: [
        { title: 'Tenha a visão completa', text: 'A visão geral resume a operação e mostra os tickets que exigem atenção.' },
        { title: 'Controle o fluxo', text: 'Use Tickets para acompanhar as etapas e executar as ações permitidas em cada demanda.' },
        { title: 'Garanta a continuidade', text: 'Na triagem, classifique e atribua tickets para manter a operação em andamento.' },
        { title: 'Mantenha os cadastros', text: 'Clientes, campanhas e tipos de demanda alimentam a abertura e preservam os vínculos históricos.' },
        { title: 'Configure prazos e acessos', text: 'Em Configuração de SLA e Usuários e perfis, ajuste as regras operacionais e os acessos.' }
      ]
    } },
    reports: { active: 'Tickets ativos', completed: 'Concluídos', slaCompliant: 'SLA dentro do prazo', overdue: 'SLA vencido', averageResolution: 'Tempo médio de resolução', notAvailable: 'Sem dados', minutes: 'minutos' },
    admin: { new: 'Novo registro', edit: 'Editar registro', configuration: 'Configuração avançada', fields: { name: 'Nome', contactName: 'Contato', email: 'E-mail', phone: 'Telefone', clientId: 'Cliente', channel: 'Canal', objective: 'Objetivo', approvalRequired: 'Exige aprovação', evidenceRequired: 'Exige evidência', fieldDefinitions: 'Definição de campos', password: 'Senha', role: 'Perfil', active: 'Ativo', scope: 'Escopo', demandTypeId: 'Tipo de demanda', timezone: 'Fuso horário', businessDays: 'Dias úteis', businessStart: 'Início do expediente', businessEnd: 'Fim do expediente', holidays: 'Feriados', pauseInValidation: 'Pausar em validação', deadlines: 'Prazos por prioridade' } }
  }}},
  lng: 'pt-BR', fallbackLng: 'pt-BR', interpolation: { escapeValue: false }
});

export default i18n;
