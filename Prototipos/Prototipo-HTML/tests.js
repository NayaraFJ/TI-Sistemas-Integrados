const assert = require('assert');
const fs = require('fs');
const vm = require('vm');
const path = require('path');
const core = require('./core');
const context = { window: {} };
vm.runInNewContext(fs.readFileSync(path.join(__dirname, 'mock-data.js'), 'utf8'), context);
const data = context.window.SIGE_SEED;
assert.equal(core.ROUTES.length, 14, 'T02–T15 devem estar mapeadas');
assert.equal(new Set(data.tickets.map(t => t.status)).size, 8, 'Todos os oito status devem ter exemplo');
assert.deepEqual(new Set(data.tickets.map(t => t.status)), new Set(core.STATUS));
for (const role of ['Cliente', 'Atendimento', 'Gestor de tráfego', 'Administrador']) assert(data.users.some(u => u.role === role && u.active));
const client = data.users.find(u => u.role === 'Cliente');
const manager = data.users.find(u => u.role === 'Gestor de tráfego');
assert(core.visibleTickets(data, client).every(t => t.clientId === client.clientId));
assert(core.visibleTickets(data, manager).every(t => t.assigneeId === manager.id));
assert.equal(core.selectSla(data, data.tickets.find(t => t.clientId === 'c-aurora')).id, 'sla-aurora');
const copy = core.clone(data.tickets.find(t => t.status === 'Aberta'));
core.transition(copy, 'Em triagem', 'Teste', 'Teste automatizado');
assert.equal(copy.status, 'Em triagem');
assert.throws(() => core.transition(copy, 'Concluída', 'Teste', ''), /Transição inválida/);
const appSource = fs.readFileSync(path.join(__dirname, 'app.js'), 'utf8');
for (const code of ['T01','T02','T03','T04','T05','T06','T07','T08','T09','T10','T11','T12','T13','T14','T15']) assert(appSource.includes(code), `${code} precisa estar implementada`);
assert(core.can(data.users.find(u => u.role === 'Cliente'), 'validate', data.tickets.find(t => t.status === 'Em validação')));
assert(!core.can(data.users.find(u => u.role === 'Administrador'), 'execute', data.tickets.find(t => t.status === 'Em execução')));
assert(appSource.includes('create_demo_ticket'), 'A jornada de criação deve estar exposta ao WebMCP');
assert(appSource.includes("ticketView: 'kanban'"), 'O Kanban deve ser a visualização principal de tickets');
assert(appSource.includes("ui.ticketView === 'kanban' ? renderKanban(items) : ticketTable(items)"), 'A tela deve alternar entre Kanban e tabela');
for (const role of Object.keys(core.ROLE_ROUTES)) {
  assert(!core.roleRoutes(role).some(route => ['new-ticket','triage','execution','validation'].includes(route)), `O menu de ${role} deve conter apenas áreas recorrentes`);
}
assert(core.canAccessRoute('Cliente', 'validation'), 'A validação contextual deve continuar acessível ao cliente');
assert(core.canAccessRoute('Atendimento', 'triage'), 'A triagem contextual deve continuar acessível ao atendimento');
assert(core.canAccessRoute('Gestor de tráfego', 'execution'), 'A execução contextual deve continuar acessível ao gestor');
assert(appSource.includes("data-action=\"open-new-ticket\""), 'A abertura deve usar um painel contextual');
assert(appSource.includes("function ticketHref(ticket)"), 'O ticket deve direcionar à etapa adequada ao perfil e status');
assert(appSource.includes("const TOUR_KEY = 'sige-desk-tours-v1'"), 'A conclusão do tutorial deve ser persistida');
assert(appSource.includes('function maybeOfferTutorial()'), 'O tutorial deve ser oferecido automaticamente no primeiro acesso');
assert(appSource.includes('function showTourStep()'), 'O tutorial deve destacar cada etapa e navegar entre telas');
for (const role of ['Cliente', 'Atendimento', 'Gestor de tráfego', 'Administrador']) {
  assert(appSource.includes(`'${role}': [`), `O perfil ${role} deve possuir roteiro próprio`);
}
for (const action of ['start-tutorial', 'tour-next', 'tour-prev', 'tour-skip']) {
  assert(appSource.includes(`data-action="${action}"`) || appSource.includes(`action==='${action}'`), `A ação ${action} deve estar implementada`);
}
assert(appSource.includes("presentation:'panel'"), 'A ferramenta de nova solicitação deve refletir o painel contextual');
console.log('OK — inventário, visibilidade, SLA e transições validados.');
