import type { ApiSchemas } from './generated';

type Response<T> = T extends readonly (infer U)[] ? Response<U>[] : T extends object ? { [K in keyof T]-?: Response<T[K]> } : T;
export type Session = Response<ApiSchemas['AuthSessionResponse']>;
export type TicketItem = Response<ApiSchemas['TicketItem']>;
export type TicketList = Response<ApiSchemas['TicketList']>;
export type TicketHistory = Response<ApiSchemas['TicketHistoryItem']>;
export type TicketComment = Response<ApiSchemas['TicketCommentItem']>;
export type TicketAttachment = Response<ApiSchemas['AttachmentItem']>;
export type TicketDetail = Response<ApiSchemas['TicketDetail']>;
export type ReferenceData = Response<ApiSchemas['ReferenceResponse']>;
export type Dashboard = Response<ApiSchemas['DashboardResponse']>;
export type TicketReport = Response<ApiSchemas['TicketReport']>;
export type NotificationItem = Response<ApiSchemas['NotificationItem']>;
export type Notifications = Response<ApiSchemas['NotificationResponse']>;
export type Role = Session['role'];
export type TicketStatus = TicketItem['status'];
export type Priority = TicketItem['urgency'];
export interface TicketFilters { search?:string; status?:TicketStatus; clientId?:string; campaignId?:string; demandTypeId?:string; priority?:Priority; assigneeId?:string; createdFrom?:string; createdTo?:string; overdue?:boolean; page?:number; size?:number; }
