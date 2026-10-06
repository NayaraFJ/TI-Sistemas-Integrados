import axios from 'axios';
import type { Dashboard, Notifications, ReferenceData, Session, TicketDetail, TicketFilters, TicketItem, TicketList, TicketReport } from './types';

// O interceptor envia o token mascarado do endpoint; o Axios não deve substituí-lo pelo cookie bruto.
const api = axios.create({ baseURL: '/api/v1', withCredentials: true, withXSRFToken: false, headers: { 'Content-Type': 'application/json' } });
let csrfToken: string | null = null;
async function csrf() { if (!csrfToken) { const response = await api.get<{token:string}>('/auth/csrf'); csrfToken=response.data.token; } return csrfToken; }
api.interceptors.request.use(async config => { if (!['get','head','options'].includes(config.method?.toLowerCase() ?? 'get') && !config.url?.endsWith('/auth/login')) config.headers.set('X-XSRF-TOKEN', await csrf()); return config; });
api.interceptors.response.use(response=>response,error=>{
  if(axios.isAxiosError(error)&&error.response?.status===401&&!error.config?.url?.startsWith('/auth/'))window.dispatchEvent(new Event('sige-session-expired'));
  return Promise.reject(error);
});
export const apiClient = {
  login: async (email:string,password:string) => { const response=await api.post<Session>('/auth/login',{email,password}); csrfToken=null; return response.data; },
  me: () => api.get<Session>('/auth/me').then(response=>response.data),
  logout: async () => { try { await api.post('/auth/logout'); } finally { csrfToken=null; } },
  dashboard: () => api.get<Dashboard>('/dashboard').then(response=>response.data),
  tickets: (filters:TicketFilters={}) => api.get<TicketList>('/tickets',{params:filters}).then(response=>response.data),
  ticket: (id:string) => api.get<TicketDetail>(`/tickets/${id}`).then(response=>response.data),
  reference: () => api.get<ReferenceData>('/reference').then(response=>response.data),
  createTicketWithFiles: (body:unknown,files:File[],fields:Record<string,File>) => {const form=new FormData();form.append('request',new Blob([JSON.stringify(body)],{type:'application/json'}));files.forEach(file=>form.append('files',file));Object.entries(fields).forEach(([name,file])=>form.append(`field.${name}`,file));return api.post<TicketItem>('/tickets',form,{headers:{'Content-Type':'multipart/form-data'}}).then(response=>response.data);},
  createTicket: (body:unknown) => api.post<TicketItem>('/tickets',body).then(response=>response.data),
  action: (id:string,path:string,body:unknown,version:number) => api.post<TicketItem>(`/tickets/${id}/${path}`,body,{headers:{'If-Match':String(version)}}).then(response=>response.data),
  comment: (id:string,body:unknown,version:number) => api.post(`/tickets/${id}/comments`,body,{headers:{'If-Match':String(version)}}).then(response=>response.data),
  notifications: (page=0,unread=false) => api.get<Notifications>('/notifications',{params:{page,size:25,unread}}).then(response=>response.data),
  readNotification: (id:string) => api.post(`/notifications/${id}/read`).then(()=>undefined),
  readAllNotifications: () => api.post('/notifications/read-all').then(()=>undefined),
  managementPage: (resource:string,search:string,page:number) => api.get<{items:Record<string,unknown>[];total:number;page:number;size:number}>(`/${resource}/page`,{params:{search,page,size:25}}).then(response=>response.data),
  managementList: (resource:string) => api.get<Record<string, unknown>[]>(`/${resource}`).then(response=>response.data),
  createResource: (resource:string,body:unknown) => api.post(`/${resource}`,body).then(response=>response.data),
  updateResource: (resource:string,id:string,body:unknown) => api.put(`/${resource}/${id}`,body).then(response=>response.data),
  setResourceActive: (resource:string,id:string,value:boolean) => api.post(`/${resource}/${id}/active`,undefined,{params:{value}}).then(response=>response.data),
  uploadAttachment: (ticketId:string,file:File) => { const body=new FormData(); body.append('file',file); return api.post(`/tickets/${ticketId}/attachments`,body,{headers:{'Content-Type':'multipart/form-data'}}).then(response=>response.data); },
  attachmentDownloadUrl: (ticketId:string,attachmentId:string) => `/api/v1/tickets/${encodeURIComponent(ticketId)}/attachments/${encodeURIComponent(attachmentId)}/download`,
  reportTickets: (filters:TicketFilters={}) => api.get<TicketReport>('/reports/tickets',{params:filters}).then(response=>response.data),
  exportTickets: (filters:TicketFilters={}) => api.get('/reports/tickets/export',{params:filters,responseType:'blob'}).then(response=>response.data as Blob),
};
