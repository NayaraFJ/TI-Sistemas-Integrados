import assert from 'node:assert/strict';
import test from 'node:test';
import { formatHistoryValue } from '../src/shared/api/historyValue.ts';

test('apresenta valores de auditoria sem perder conteúdo', () => {
  assert.equal(formatHistoryValue(null, 'Não informado'), 'Não informado');
  assert.equal(formatHistoryValue('null', 'Não informado'), 'Não informado');
  assert.equal(formatHistoryValue('"OPEN"', 'Não informado'), 'OPEN');
  assert.equal(formatHistoryValue('"abc-123"', 'Não informado'), 'abc-123');
  assert.equal(formatHistoryValue('{"cycle":2,"priority":"HIGH"}', 'Não informado'), '{\n  "cycle": 2,\n  "priority": "HIGH"\n}');
  assert.equal(formatHistoryValue('texto livre', 'Não informado'), 'texto livre');
  assert.equal(formatHistoryValue('false', 'Não informado'), 'false');
});
