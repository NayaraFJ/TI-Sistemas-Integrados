import assert from 'node:assert/strict';
import test from 'node:test';
import { AxiosError } from 'axios';
import { loginErrorKey } from '../src/shared/api/loginError.ts';

test('classifica somente HTTP 401 como credenciais inválidas', () => {
  const unauthorized = new AxiosError('unauthorized', undefined, undefined, {}, { status: 401 } as never);
  const forbidden = new AxiosError('forbidden', undefined, undefined, {}, { status: 403 } as never);
  const serverError = new AxiosError('server error', undefined, undefined, {}, { status: 500 } as never);
  const network = new AxiosError('network', 'ERR_NETWORK', undefined, {});
  assert.equal(loginErrorKey(unauthorized), 'login.invalid');
  assert.equal(loginErrorKey(forbidden), 'login.error');
  assert.equal(loginErrorKey(serverError), 'login.error');
  assert.equal(loginErrorKey(network), 'login.connection');
  assert.equal(loginErrorKey(new Error('unexpected')), 'login.error');
});
