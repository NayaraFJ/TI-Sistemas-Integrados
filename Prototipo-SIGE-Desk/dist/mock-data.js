(function () {
  const seed = {
    meta: { version: 1, nextTicket: 1049 },
    session: null,
    users: [
      { id: 'u-cli-1', name: 'Marina Costa', email: 'cliente@aurora.demo', password: 'demo123', role: 'Cliente', clientId: 'c-aurora', active: true },
      { id: 'u-cli-2', name: 'Rafael Lima', email: 'rafael@horizonte.demo', password: 'demo123', role: 'Cliente', clientId: 'c-horizonte', active: true },
      { id: 'u-atd-1', name: 'Beatriz Souza', email: 'atendimento@sige.demo', password: 'demo123', role: 'Atendimento', clientId: null, active: true },
      { id: 'u-ges-1', name: 'Lucas Martins', email: 'gestor@sige.demo', password: 'demo123', role: 'Gestor de tráfego', clientId: null, active: true },
      { id: 'u-ges-2', name: 'Carla Mendes', email: 'carla@sige.demo', password: 'demo123', role: 'Gestor de tráfego', clientId: null, active: true },
      { id: 'u-adm-1', name: 'Nayara Rodrigues', email: 'admin@sige.demo', password: 'demo123', role: 'Administrador', clientId: null, active: true },
      { id: 'u-inativo', name: 'Conta Inativa', email: 'inativo@sige.demo', password: 'demo123', role: 'Cliente', clientId: 'c-aurora', active: false }
    ],
    clients: [
      { id: 'c-aurora', name: 'Aurora Educação', contact: 'Marina Costa', email: 'marketing@aurora.demo', phone: '(31) 98888-1020', active: true },
      { id: 'c-horizonte', name: 'Horizonte Solar', contact: 'Rafael Lima', email: 'midia@horizonte.demo', phone: '(31) 97777-2080', active: true },
      { id: 'c-verde', name: 'Verde Vivo Cosméticos', contact: 'Aline Braga', email: 'aline@verdevivo.demo', phone: '(31) 96666-3010', active: false }
    ],
    campaigns: [
      { id: 'cp-aurora-vest', clientId: 'c-aurora', name: 'Vestibular 2027', channel: 'Google Ads', objective: 'Geração de leads', active: true },
      { id: 'cp-aurora-pos', clientId: 'c-aurora', name: 'Pós-graduação Setembro', channel: 'Meta Ads', objective: 'Conversão', active: true },
      { id: 'cp-horizonte-b2b', clientId: 'c-horizonte', name: 'Solar para Empresas', channel: 'LinkedIn Ads', objective: 'Leads B2B', active: true },
      { id: 'cp-horizonte-casa', clientId: 'c-horizonte', name: 'Energia em Casa', channel: 'Meta Ads', objective: 'Reconhecimento', active: false }
    ],
    demandTypes: [
      { id: 'tp-budget', name: 'Ajuste de orçamento', active: true, approval: true, evidence: true, fields: ['Orçamento atual', 'Orçamento proposto', 'Justificativa', 'Data desejada'] },
      { id: 'tp-ad', name: 'Criação, alteração ou pausa de anúncio', active: true, approval: true, evidence: true, fields: ['Ativo afetado', 'Ação solicitada', 'Motivo'] },
      { id: 'tp-creative', name: 'Aprovação de criativo', active: true, approval: true, evidence: true, fields: ['Criativo/anexo', 'Data limite'] },
      { id: 'tp-report', name: 'Relatório de desempenho', active: true, approval: false, evidence: true, fields: ['Período', 'Métricas desejadas', 'Formato'] },
      { id: 'tp-analysis', name: 'Análise de métricas', active: true, approval: false, evidence: false, fields: ['Período', 'Pergunta de negócio', 'Indicadores disponíveis'] }
    ],
    slaRules: [
      { id: 'sla-default', name: 'Regra padrão', scope: 'Padrão', scopeId: null, timezone: 'America/Sao_Paulo', schedule: 'Seg–Sex, 08h–18h', holidays: 'Nacionais e recessos cadastrados', pauseValidation: false, active: true, deadlines: { Urgente: [2, 8], Alta: [4, 16], Média: [8, 24], Baixa: [16, 40] } },
      { id: 'sla-aurora', name: 'Aurora prioritária', scope: 'Cliente', scopeId: 'c-aurora', timezone: 'America/Sao_Paulo', schedule: 'Seg–Sex, 08h–18h', holidays: 'Nacionais', pauseValidation: false, active: true, deadlines: { Urgente: [1, 6], Alta: [3, 12], Média: [6, 20], Baixa: [12, 32] } }
    ],
    tickets: [
      {
        id: 't-1048', number: 'SIGE-1048', subject: 'Aumentar orçamento no fim de semana', clientId: 'c-aurora', campaignId: 'cp-aurora-vest', pendingCampaign: '', typeId: 'tp-budget', channel: 'Google Ads', requester: 'Marina Costa', author: 'Marina Costa', urgency: 'Alta', desiredDate: '2026-09-09', priority: null, assigneeId: null, status: 'Aberta', createdAt: '2026-09-06T09:10:00', updatedAt: '2026-09-06T09:10:00', responseDue: null, resolutionDue: null, slaState: 'Pendente de classificação', waitOrigin: null, executionStarted: false, cycle: 1, approvalRequired: true, evidenceRequired: true,
        description: 'A campanha do vestibular ganhou tração e precisamos avaliar aumento de R$ 2.000 para o fim de semana.', metrics: { impressions: '48210', ctr: '3.8', cpc: '2.14', conversions: '91', cpa: '38.50', roas: '4.2', period: '01/09 a 05/09/2026', currency: 'BRL' },
        comments: [], attachments: [], evidence: [], approval: null,
        history: [{ at: '2026-09-06T09:10:00', user: 'Marina Costa', action: 'Ticket criado', from: '—', to: 'Aberta', reason: 'Solicitação enviada pelo portal' }]
      },
      {
        id: 't-1047', number: 'SIGE-1047', subject: 'Relatório semanal de captação', clientId: 'c-horizonte', campaignId: 'cp-horizonte-b2b', pendingCampaign: '', typeId: 'tp-report', channel: 'LinkedIn Ads', requester: 'Rafael Lima', author: 'Beatriz Souza', urgency: 'Média', desiredDate: '2026-09-08', priority: 'Média', assigneeId: 'u-ges-2', status: 'Em triagem', createdAt: '2026-09-05T14:20:00', updatedAt: '2026-09-06T08:45:00', responseDue: '2026-09-07T12:20:00', resolutionDue: '2026-09-09T18:00:00', slaState: 'No prazo', waitOrigin: null, executionStarted: false, cycle: 1, approvalRequired: false, evidenceRequired: true,
        description: 'Consolidar leads, CPL e taxa de conversão da última semana para reunião comercial.', metrics: {}, comments: [{ id: 'cm-1', author: 'Beatriz Souza', at: '2026-09-06T08:45:00', text: 'Recebemos a demanda e estamos organizando o relatório.', effectiveResponse: true }], attachments: [], evidence: [], approval: null,
        history: [{ at: '2026-09-05T14:20:00', user: 'Beatriz Souza', action: 'Ticket criado em nome do cliente', from: '—', to: 'Aberta', reason: 'Solicitação recebida em reunião' }, { at: '2026-09-06T08:40:00', user: 'Beatriz Souza', action: 'Triagem iniciada', from: 'Aberta', to: 'Em triagem', reason: 'Análise da solicitação' }]
      },
      {
        id: 't-1046', number: 'SIGE-1046', subject: 'Pausar anúncio com oferta encerrada', clientId: 'c-aurora', campaignId: 'cp-aurora-pos', pendingCampaign: '', typeId: 'tp-ad', channel: 'Meta Ads', requester: 'Marina Costa', author: 'Marina Costa', urgency: 'Urgente', desiredDate: '2026-09-06', priority: 'Urgente', assigneeId: 'u-ges-1', status: 'Em execução', createdAt: '2026-09-05T16:00:00', updatedAt: '2026-09-06T08:20:00', responseDue: '2026-09-05T17:00:00', resolutionDue: '2026-09-06T15:00:00', slaState: 'No prazo', waitOrigin: null, executionStarted: true, cycle: 1, approvalRequired: true, evidenceRequired: true,
        description: 'Pausar o conjunto da oferta de matrícula antecipada. O período promocional terminou ontem.', metrics: {}, comments: [{ id: 'cm-2', author: 'Beatriz Souza', at: '2026-09-05T16:25:00', text: 'Solicitação priorizada e encaminhada ao Lucas.', effectiveResponse: true }], attachments: [], evidence: [], approval: null,
        history: [{ at: '2026-09-05T16:00:00', user: 'Marina Costa', action: 'Ticket criado', from: '—', to: 'Aberta', reason: 'Solicitação enviada' }, { at: '2026-09-05T16:20:00', user: 'Beatriz Souza', action: 'Triagem concluída', from: 'Aberta', to: 'Em execução', reason: 'Responsável e SLA definidos' }]
      },
      {
        id: 't-1045', number: 'SIGE-1045', subject: 'Confirmar URL da página de destino', clientId: 'c-horizonte', campaignId: 'cp-horizonte-b2b', pendingCampaign: '', typeId: 'tp-ad', channel: 'LinkedIn Ads', requester: 'Rafael Lima', author: 'Rafael Lima', urgency: 'Média', desiredDate: '2026-09-10', priority: 'Média', assigneeId: 'u-ges-2', status: 'Aguardando cliente', createdAt: '2026-09-04T10:00:00', updatedAt: '2026-09-05T17:10:00', responseDue: '2026-09-04T18:00:00', resolutionDue: '2026-09-09T10:00:00', slaState: 'Pausado', waitOrigin: 'execução', executionStarted: true, cycle: 1, approvalRequired: true, evidenceRequired: true,
        waitReason: 'Enviar a URL final e confirmar se os parâmetros UTM já foram revisados.', complementReceived: false,
        description: 'Atualizar o anúncio B2B com a nova página de captação.', metrics: {}, comments: [{ id: 'cm-3', author: 'Carla Mendes', at: '2026-09-05T17:10:00', text: 'Precisamos da URL final antes de continuar.', effectiveResponse: false }], attachments: [], evidence: [], approval: null,
        history: [{ at: '2026-09-05T17:10:00', user: 'Carla Mendes', action: 'Complemento solicitado', from: 'Em execução', to: 'Aguardando cliente', reason: 'URL final ausente' }]
      },
      {
        id: 't-1044', number: 'SIGE-1044', subject: 'Aprovar criativo da campanha de pós', clientId: 'c-aurora', campaignId: 'cp-aurora-pos', pendingCampaign: '', typeId: 'tp-creative', channel: 'Meta Ads', requester: 'Marina Costa', author: 'Beatriz Souza', urgency: 'Alta', desiredDate: '2026-09-07', priority: 'Alta', assigneeId: 'u-ges-1', status: 'Em validação', createdAt: '2026-09-03T09:30:00', updatedAt: '2026-09-05T15:40:00', responseDue: '2026-09-03T12:30:00', resolutionDue: '2026-09-08T12:30:00', slaState: 'No prazo', waitOrigin: null, executionStarted: true, cycle: 1, approvalRequired: true, evidenceRequired: true,
        description: 'Validar a versão final do criativo para o curso de Gestão Estratégica.', metrics: {}, comments: [], attachments: [{ id: 'at-1', name: 'criativo-pos-v3.png', size: '1,8 MB', type: 'image/png', author: 'Lucas Martins' }], evidence: [{ id: 'ev-1', description: 'Criativo v3 submetido à validação', link: 'mock://criativo-pos-v3', author: 'Lucas Martins', at: '2026-09-05T15:40:00', nature: 'Material submetido' }], approval: null,
        history: [{ at: '2026-09-05T15:40:00', user: 'Lucas Martins', action: 'Entrega enviada para validação', from: 'Em execução', to: 'Em validação', reason: 'Criativo v3 finalizado' }]
      },
      {
        id: 't-1043', number: 'SIGE-1043', subject: 'Análise de queda no CTR', clientId: 'c-horizonte', campaignId: 'cp-horizonte-b2b', pendingCampaign: '', typeId: 'tp-analysis', channel: 'LinkedIn Ads', requester: 'Rafael Lima', author: 'Rafael Lima', urgency: 'Baixa', desiredDate: '2026-09-05', priority: 'Baixa', assigneeId: 'u-ges-2', status: 'Concluída', createdAt: '2026-09-01T11:00:00', updatedAt: '2026-09-04T16:00:00', responseDue: '2026-09-02T17:00:00', resolutionDue: '2026-09-07T17:00:00', slaState: 'Cumprido', waitOrigin: null, executionStarted: true, cycle: 1, approvalRequired: false, evidenceRequired: false,
        description: 'Analisar a queda de CTR observada na campanha B2B.', metrics: { impressions: '82410', ctr: '1.7', cpc: '8.20', conversions: '27', cpa: '124.00', roas: '2.1', period: '24/08 a 31/08/2026', currency: 'BRL' }, comments: [{ id: 'cm-4', author: 'Carla Mendes', at: '2026-09-04T15:55:00', text: 'A queda está concentrada em dois segmentos com frequência elevada.', effectiveResponse: false }], attachments: [], evidence: [], approval: { decision: 'Dispensa automática', user: 'Configuração do tipo', at: '2026-09-04T16:00:00', note: 'Tipo sem aprovação obrigatória' },
        history: [{ at: '2026-09-04T16:00:00', user: 'Carla Mendes', action: 'Ticket concluído', from: 'Em execução', to: 'Concluída', reason: 'Dispensa automática conforme tipo Análise de métricas' }]
      },
      {
        id: 't-1042', number: 'SIGE-1042', subject: 'Revisar segmentação da campanha', clientId: 'c-aurora', campaignId: 'cp-aurora-vest', pendingCampaign: '', typeId: 'tp-ad', channel: 'Google Ads', requester: 'Marina Costa', author: 'Marina Costa', urgency: 'Alta', desiredDate: '2026-09-05', priority: 'Alta', assigneeId: 'u-ges-1', status: 'Reaberta', createdAt: '2026-08-29T08:30:00', updatedAt: '2026-09-06T08:00:00', responseDue: '2026-08-29T11:30:00', resolutionDue: '2026-09-08T11:00:00', slaState: 'Novo ciclo', waitOrigin: null, executionStarted: true, cycle: 2, reopenOrigin: 'conclusão', approvalRequired: true, evidenceRequired: true,
        description: 'Revisar os termos de busca e excluir consultas informativas.', metrics: {}, comments: [], attachments: [], evidence: [{ id: 'ev-2', description: 'Lista inicial de negativas aplicada', link: 'mock://negativas-v1', author: 'Lucas Martins', at: '2026-09-03T11:00:00', nature: 'Ação executada' }], approval: { decision: 'Aprovado', user: 'Marina Costa', at: '2026-09-03T14:00:00', note: 'Primeira entrega aprovada' },
        history: [{ at: '2026-09-03T14:00:00', user: 'Marina Costa', action: 'Entrega aprovada', from: 'Em validação', to: 'Concluída', reason: 'Entrega validada' }, { at: '2026-09-06T08:00:00', user: 'Marina Costa', action: 'Ticket reaberto', from: 'Concluída', to: 'Reaberta', reason: 'Novos termos indesejados apareceram' }]
      },
      {
        id: 't-1041', number: 'SIGE-1041', subject: 'Campanha de evento cancelada', clientId: 'c-horizonte', campaignId: null, pendingCampaign: 'Feira Solar Minas', typeId: 'tp-ad', channel: 'Meta Ads', requester: 'Rafael Lima', author: 'Rafael Lima', urgency: 'Média', desiredDate: '2026-09-04', priority: null, assigneeId: null, status: 'Cancelada', createdAt: '2026-09-02T10:15:00', updatedAt: '2026-09-02T11:10:00', responseDue: null, resolutionDue: null, slaState: 'Encerrado por cancelamento', waitOrigin: null, executionStarted: false, cycle: 1, approvalRequired: true, evidenceRequired: true,
        description: 'Criar campanha para divulgação da Feira Solar Minas.', metrics: {}, comments: [], attachments: [], evidence: [], approval: null, cancelReason: 'Evento cancelado pelo organizador.',
        history: [{ at: '2026-09-02T11:10:00', user: 'Beatriz Souza', action: 'Ticket cancelado', from: 'Aberta', to: 'Cancelada', reason: 'Evento cancelado pelo organizador' }]
      }
    ],
    notifications: [
      { id: 'n-1', userId: 'u-cli-1', ticketId: 't-1044', event: 'Em validação', summary: 'O criativo v3 está pronto para sua decisão.', at: '2026-09-05T15:40:00', read: false },
      { id: 'n-2', userId: 'u-cli-2', ticketId: 't-1045', event: 'Aguardando cliente', summary: 'Envie a URL final e confirme os parâmetros UTM.', at: '2026-09-05T17:10:00', read: false },
      { id: 'n-3', userId: 'u-ges-1', ticketId: 't-1046', event: 'Ticket atribuído', summary: 'Demanda urgente atribuída a você.', at: '2026-09-05T16:20:00', read: false },
      { id: 'n-4', userId: 'u-atd-1', ticketId: 't-1042', event: 'Reaberta', summary: 'Ticket reaberto pelo cliente e aguardando confirmação.', at: '2026-09-06T08:00:00', read: false },
      { id: 'n-5', userId: 'u-adm-1', ticketId: 't-1041', event: 'Cancelada', summary: 'Ticket cancelado antes da execução.', at: '2026-09-02T11:10:00', read: true },
      { id: 'n-6', userId: 'u-ges-2', ticketId: 't-1043', event: 'Concluída', summary: 'Análise concluída com dispensa automática de aprovação.', at: '2026-09-04T16:00:00', read: true }
    ]
  };

  window.SIGE_SEED = seed;
})();
