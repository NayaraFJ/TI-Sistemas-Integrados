import type { Role } from '../../shared/api/types';

export type TourStep = { target: string; message: string };
const step = (target: string, message = target): TourStep => ({ target, message });

export function tourSteps(path: string, role: Role, compact: boolean): TourStep[] {
  let steps: TourStep[];
  if (path === '/') {
    steps = [step('dashboard-metrics'), step('dashboard-recent'), step('dashboard-status'), step('dashboard-deadlines')];
    if (role !== 'TRAFFIC_MANAGER') steps.push(step('page-actions', 'create'));
  } else if (path === '/tickets') {
    steps = [step('ticket-filters'), step('ticket-view'), step('ticket-results')];
    if (role !== 'TRAFFIC_MANAGER') steps.push(step('page-actions', role === 'CLIENT' ? 'create' : 'ticket-tools'));
  } else if (path === '/tickets/new') {
    steps = [step('request-form')];
  } else if (path.startsWith('/tickets/')) {
    steps = [step('ticket-context'), step('ticket-actions'), step('ticket-sla'), step('ticket-history')];
  } else if (path === '/notifications') {
    steps = [step('notification-filters'), step('notification-list'), step('page-actions', 'read-notifications')];
  } else if (path === '/reports') {
    steps = [step('report-filters'), step('report-summary'), step('report-charts'), step('page-actions', 'export')];
  } else {
    steps = [step('management-list'), step('page-actions', 'add-record')];
  }
  steps.push(step(compact ? 'mobile-navigation' : 'navigation'));
  return steps;
}

export type TourRect = { left: number; top: number; width: number; height: number };

// Keep the card in the viewport, choosing a free side of the highlighted area.
export function placeTourCard(target: TourRect | null, viewport: { width: number; height: number }, card: { width: number; height: number }) {
  const margin = 12;
  const gap = 16;
  const maxLeft = Math.max(margin, viewport.width - card.width - margin);
  const maxTop = Math.max(margin, viewport.height - card.height - margin);
  const clamp = (value: number, max: number) => Math.max(margin, Math.min(value, max));
  if (!target) return { left: clamp((viewport.width - card.width) / 2, maxLeft), top: maxTop };
  const below = target.top + target.height + gap;
  const above = target.top - card.height - gap;
  const right = target.left + target.width + gap;
  const left = target.left - card.width - gap;
  if (below <= maxTop) return { left: clamp(target.left, maxLeft), top: clamp(below, maxTop) };
  if (above >= margin) return { left: clamp(target.left, maxLeft), top: above };
  if (right <= maxLeft) return { left: right, top: clamp(target.top, maxTop) };
  if (left >= margin) return { left, top: clamp(target.top, maxTop) };
  return { left: maxLeft, top: maxTop };
}
