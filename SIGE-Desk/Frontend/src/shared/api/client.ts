import axios from 'axios';
import type { Dashboard, NotificationItem, ReferenceData, Session, TicketDetail, TicketFilters, TicketItem, TicketList, TicketReport } from './types';

// O interceptor envia o token mascarado do endpoint; o Axios não deve substituí-lo pelo cookie bruto.
const api = axios.create({ baseURL: '/api/v1', withCredentials: true, withXSRFToken: false, headers: { 'Content-Type': 'application/json' } });
let csrfToken: string | null = null;
async function csrf() { if (!csrfToken) { const response = await api.get<{token:string}>('/auth/csrf'); csrfToken=response.data.token; } return csrfToken; }
api.interceptors.request.use(async config => { if (!['get','head','options'].includes(config.method?.toLowerCase() ?? 'get') && !config.url?.endsWith('/auth/login')) config.headers.set('X-XSRF-TOKEN', await csrf()); return config; });
export const apiClient = {
  login: async (email:string,password:string) => { const response=await api.post<Session>('/auth/login',{email,password}); csrfToken=null; return response.data; },
  me: () => api.get<Session>('/auth/me').then(response=>response.data),
  logout: async () => { try { await api.post('/auth/logout'); } finally { csrfToken=null; } },
  dashboard: () => api.get<Dashboard>('/dashboard').then(response=>response.data),
  tickets: (filters:TicketFilters={}) => api.get<TicketList>('/tickets',{params:filters}).then(response=>response.data),
  ticket: (id:string) => api.get<TicketDetail>(`/tickets/${id}`).then(response=>response.data),
  reference: () => api.get<ReferenceData>('/reference').then(response=>response.data),
  createTicket: (body:unknown) => api.post<TicketItem>('/tickets',body).then(response=>response.data),
  action: (id:string,path:string,body:unknown) => api.post<TicketItem>(`/tickets/${id}/${path}`,body).then(response=>response.data),
  comment: (id:string,body:unknown) => api.post(`/tickets/${id}/comments`,body).then(response=>response.data),
  notifications: () => api.get<{items:NotificationItem[];unreadCount:number}>('/notifications').then(response=>response.data),
  readNotification: (id:string) => api.post(`/notifications/${id}/read`).then(()=>undefined),
  readAllNotifications: () => api.post('/notifications/read-all').then(()=>undefined),
  managementList: (resource:string) => api.get<Record<string, unknown>[]>(`/${resource}`).then(response=>response.data),
  createResource: (resource:string,body:unknown) => api.post(`/${resource}`,body).then(response=>response.data),
  updateResource: (resource:string,id:string,body:unknown) => api.put(`/${resource}/${id}`,body).then(response=>response.data),
  setResourceActive: (resource:string,id:string,value:boolean) => api.post(`/${resource}/${id}/active`,undefined,{params:{value}}).then(response=>response.data),
  uploadAttachment: (ticketId:string,file:File) => { const body=new FormData(); body.append('file',file); return api.post(`/tickets/${ticketId}/attachments`,body,{headers:{'Content-Type':'multipart/form-data'}}).then(response=>response.data); },
  attachmentDownloadUrl: (ticketId:string,attachmentId:string) => `/api/v1/tickets/${encodeURIComponent(ticketId)}/attachments/${encodeURIComponent(attachmentId)}/download`,
  reportTickets: (filters:TicketFilters={}) => api.get<TicketReport>('/reports/tickets',{params:filters}).then(response=>response.data),
  exportTickets: (filters:TicketFilters={}) => api.get('/reports/tickets/export',{params:filters,responseType:'blob'}).then(response=>response.data as Blob),
};
