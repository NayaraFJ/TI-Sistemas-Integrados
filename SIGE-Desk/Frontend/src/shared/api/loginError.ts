import axios from 'axios';

export function loginErrorKey(error: unknown): 'login.invalid' | 'login.connection' | 'login.error' {
  if (axios.isAxiosError(error)) {
    if (error.response?.status === 401) return 'login.invalid';
    if (!error.response && error.request) return 'login.connection';
  }
  return 'login.error';
}
