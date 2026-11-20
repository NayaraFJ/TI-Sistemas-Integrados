const sessionKey = 'sige-session-established';
let establishedInMemory = false;

export function hasPreviousSession(): boolean {
  try { return establishedInMemory || sessionStorage.getItem(sessionKey) === '1'; } catch { return establishedInMemory; }
}
export function markSessionEstablished(): void {
  establishedInMemory = true;
  try { sessionStorage.setItem(sessionKey, '1'); } catch { /* A sessão continua utilizável sem armazenamento. */ }
}
export function clearPreviousSession(): void {
  establishedInMemory = false;
  try { sessionStorage.removeItem(sessionKey); } catch { /* Nenhum dado de autenticação é armazenado aqui. */ }
}

export function isExpiredSessionResponse(status: number | undefined, url: string | undefined, previousSession: boolean): boolean {
  return previousSession && status === 401 && !url?.startsWith('/auth/login') && !url?.startsWith('/auth/logout');
}
