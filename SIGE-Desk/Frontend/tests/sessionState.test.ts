import assert from 'node:assert/strict';
import test from 'node:test';
import { clearPreviousSession, hasPreviousSession, isExpiredSessionResponse, markSessionEstablished } from '../src/shared/api/sessionState.ts';

test('identifica 401 de sessão anterior, inclusive na consulta de sessão', () => {
  assert.equal(isExpiredSessionResponse(401, '/auth/me', true), true);
  assert.equal(isExpiredSessionResponse(401, '/tickets', true), true);
  assert.equal(isExpiredSessionResponse(401, '/auth/csrf', true), true);
  assert.equal(isExpiredSessionResponse(401, '/auth/me', false), false);
  assert.equal(isExpiredSessionResponse(401, '/auth/login', true), false);
  assert.equal(isExpiredSessionResponse(401, '/auth/logout', true), false);
  assert.equal(isExpiredSessionResponse(403, '/tickets', true), false);
  assert.equal(isExpiredSessionResponse(500, '/tickets', true), false);
});

test('mantém sinalização em memória quando armazenamento está indisponível', () => {
  Object.defineProperty(globalThis, 'sessionStorage', { configurable: true, get() { throw new DOMException('blocked', 'SecurityError'); } });
  try {
    assert.equal(hasPreviousSession(), false);
    markSessionEstablished();
    assert.equal(hasPreviousSession(), true);
    clearPreviousSession();
    assert.equal(hasPreviousSession(), false);
  } finally { delete (globalThis as { sessionStorage?: Storage }).sessionStorage; }
});
