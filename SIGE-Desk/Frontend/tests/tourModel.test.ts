import assert from 'node:assert/strict';
import test from 'node:test';
import { placeTourCard, tourSteps } from '../src/features/tutorial/tourModel.ts';

test('gestor recebe guia da fila sem orientações de criação ou exportação', () => {
  for (const path of ['/', '/tickets']) {
    const steps = tourSteps(path, 'TRAFFIC_MANAGER', false);
    assert.equal(steps.some(item => ['create', 'ticket-tools', 'export'].includes(item.message)), false);
    assert.equal(steps.at(-1)?.target, 'navigation');
  }
});

test('guia é contextual e usa o menu compacto em telas pequenas', () => {
  assert.equal(tourSteps('/tickets', 'CLIENT', true).find(item => item.target === 'page-actions')?.message, 'create');
  assert.equal(tourSteps('/tickets', 'ADMIN', true).find(item => item.target === 'page-actions')?.message, 'ticket-tools');
  assert.equal(tourSteps('/tickets/new', 'CLIENT', true)[0].target, 'request-form');
  assert.equal(tourSteps('/tickets/123', 'CLIENT', true)[0].target, 'ticket-context');
  assert.equal(tourSteps('/reports', 'ADMIN', true)[0].target, 'report-filters');
  assert.equal(tourSteps('/users', 'ADMIN', true)[0].target, 'management-list');
  assert.equal(tourSteps('/notifications', 'CLIENT', true).at(-1)?.target, 'mobile-navigation');
});

test('cartão escolhe uma área livre abaixo, acima ou ao lado do destaque', () => {
  const viewport = { width: 1280, height: 720 };
  const card = { width: 360, height: 250 };
  assert.deepEqual(placeTourCard({ left: 280, top: 100, width: 900, height: 150 }, viewport, card), { left: 280, top: 266 });
  assert.deepEqual(placeTourCard({ left: 280, top: 500, width: 900, height: 100 }, viewport, card), { left: 280, top: 234 });
  assert.deepEqual(placeTourCard({ left: 10, top: 80, width: 240, height: 600 }, viewport, card), { left: 266, top: 80 });
});

test('cartão permanece no viewport móvel mesmo com alvo maior que a tela', () => {
  const position = placeTourCard({ left: 16, top: -100, width: 358, height: 1200 }, { width: 390, height: 700 }, { width: 360, height: 320 });
  assert.ok(position.left >= 12 && position.left + 360 <= 378);
  assert.ok(position.top >= 12 && position.top + 320 <= 688);
});

test('alvo ausente mantém o cartão acessível e dentro da tela', () => {
  assert.deepEqual(placeTourCard(null, { width: 390, height: 700 }, { width: 360, height: 320 }), { left: 15, top: 368 });
});
