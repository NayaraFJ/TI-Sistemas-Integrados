(function (root, factory) {
  const api = factory();
  if (typeof module === 'object' && module.exports) module.exports = api;
  else root.SIGE_CORE = api;
})(typeof globalThis !== 'undefined' ? globalThis : this, function () {
  const STATUS = ['Aberta', 'Em triagem', 'Em execução', 'Aguardando cliente', 'Em validação', 'Concluída', 'Reaberta', 'Cancelada'];
  const ROUTES = [
    ['dashboard', 'T02', 'Visão geral'], ['tickets', 'T03', 'Tickets'], ['new-ticket', 'T04', 'Novo ticket'],
    ['ticket', 'T05', 'Detalhe do ticket'], ['triage', 'T06', 'Triagem'], ['execution', 'T07', 'Execução'],
    ['validation', 'T08', 'Validação'], ['notifications', 'T09', 'Notificações'], ['reports', 'T10', 'Relatórios'],
    ['clients', 'T11', 'Clientes'], ['campaigns', 'T12', 'Campanhas'], ['types', 'T13', 'Tipos de demanda'],
    ['sla', 'T14', 'Configuração de SLA'], ['users', 'T15', 'Usuários e perfis']
  ];
  const ROLE_ROUTES = {
    'Cliente': ['dashboard', 'tickets', 'notifications'],
    'Atendimento': ['dashboard', 'tickets', 'notifications', 'reports', 'clients', 'campaigns'],
    'Gestor de tráfego': ['dashboard', 'tickets', 'notifications'],
    'Administrador': ['dashboard', 'tickets', 'notifications', 'reports', 'clients', 'campaigns', 'types', 'sla', 'users']
  };
  const TRANSITIONS = {
    'Aberta': ['Em triagem', 'Aguardando cliente', 'Cancelada'], 'Em triagem': ['Em execução', 'Aguardando cliente', 'Cancelada'],
    'Em execução': ['Aguardando cliente', 'Em validação', 'Concluída'], 'Aguardando cliente': ['Em triagem', 'Em execução'],
    'Em validação': ['Concluída', 'Em execução'], 'Concluída': ['Reaberta'], 'Reaberta': ['Em triagem', 'Em execução'], 'Cancelada': []
  };
  const clone = value => JSON.parse(JSON.stringify(value));
  const byId = (items, id) => items.find(item => item.id === id);
  const formatDate = value => {
    if (!value) return '—';
    const date = new Date(value);
    return Number.isNaN(date.getTime()) ? value : new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: value.includes('T') ? 'short' : undefined }).format(date);
  };
  const roleRoutes = role => ROLE_ROUTES[role] || [];
  const canAccessRoute = (role, route) => {
    if (['login', 'ticket'].includes(route)) return true;
    if (route === 'new-ticket') return ['Cliente', 'Atendimento', 'Administrador'].includes(role);
    if (route === 'triage') return ['Atendimento', 'Administrador'].includes(role);
    if (route === 'execution') return role === 'Gestor de tráfego';
    if (route === 'validation') return role === 'Cliente';
    return roleRoutes(role).includes(route);
  };
  function visibleTickets(data, user) {
    if (!user) return [];
    if (user.role === 'Cliente') return data.tickets.filter(t => t.clientId === user.clientId);
    if (user.role === 'Gestor de tráfego') return data.tickets.filter(t => t.assigneeId === user.id);
    return data.tickets.slice();
  }
  function can(user, action, ticket) {
    if (!user) return false;
    const ownClient = user.role === 'Cliente' && ticket && ticket.clientId === user.clientId;
    const assigned = user.role === 'Gestor de tráfego' && ticket && ticket.assigneeId === user.id;
    const staff = ['Atendimento', 'Administrador'].includes(user.role);
    const rules = {
      create: ['Cliente', 'Atendimento', 'Administrador'].includes(user.role), comment: ownClient || assigned || staff,
      triage: staff && ticket && ['Aberta', 'Em triagem', 'Reaberta'].includes(ticket.status),
      execute: assigned && ticket && ['Em execução', 'Reaberta'].includes(ticket.status),
      validate: ownClient && ticket && ticket.status === 'Em validação', reopen: ownClient && ticket && ticket.status === 'Concluída',
      complement: ownClient && ticket && ticket.status === 'Aguardando cliente',
      resume: staff && ticket && ticket.status === 'Aguardando cliente' && ticket.complementReceived,
      cancel: staff && ticket && !ticket.executionStarted && ['Aberta', 'Em triagem'].includes(ticket.status)
    };
    return Boolean(rules[action]);
  }
  function selectSla(data, ticket) {
    const active = data.slaRules.filter(r => r.active);
    return active.find(r => r.scope === 'Cliente + tipo' && r.scopeId === `${ticket.clientId}:${ticket.typeId}`)
      || active.find(r => r.scope === 'Cliente' && r.scopeId === ticket.clientId)
      || active.find(r => r.scope === 'Tipo' && r.scopeId === ticket.typeId)
      || active.find(r => r.scope === 'Padrão') || null;
  }
  function transition(ticket, to, actor, reason) {
    if (!(TRANSITIONS[ticket.status] || []).includes(to)) throw new Error(`Transição inválida: ${ticket.status} → ${to}`);
    const from = ticket.status; ticket.status = to; ticket.updatedAt = new Date().toISOString();
    ticket.executionStarted = ticket.executionStarted || to === 'Em execução';
    ticket.history.unshift({ at: ticket.updatedAt, user: actor, action: `Status alterado para ${to}`, from, to, reason: reason || 'Atualização registrada' });
    return ticket;
  }
  return { STATUS, ROUTES, ROLE_ROUTES, TRANSITIONS, clone, byId, formatDate, roleRoutes, canAccessRoute, visibleTickets, can, selectSla, transition };
});
