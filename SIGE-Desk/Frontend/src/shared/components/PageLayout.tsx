import type { ReactNode } from 'react';
import { Box, Chip, LinearProgress, Paper, Stack, Typography } from '@mui/material';
import { useTranslation } from 'react-i18next';
import type { Priority, TicketStatus } from '../api/types';

export const statusAppearance: Record<TicketStatus, { color: string; background: string }> = {
  OPEN: { color: '#176a94', background: '#eaf4fb' },
  TRIAGE: { color: '#9c5b0c', background: '#fff4df' },
  EXECUTION: { color: '#285ab5', background: '#eaf3ff' },
  WAITING_FOR_CLIENT: { color: '#a44b1a', background: '#fff0e8' },
  VALIDATION: { color: '#6644ad', background: '#f1eefe' },
  DONE: { color: '#20704f', background: '#e7f7ef' },
  REOPENED: { color: '#ad3943', background: '#fdeef0' },
  CANCELLED: { color: '#5d6970', background: '#edf0f2' }
};
export const ticketStatuses = Object.keys(statusAppearance) as TicketStatus[];

export function PageHeading({ title, description, actions }: { title: string; description?: string; actions?: ReactNode }) {
  return <Stack direction={{ xs: 'column', sm: 'row' }} justifyContent="space-between" alignItems={{ sm: 'flex-start' }} gap={2}>
    <Box><Typography variant="h4" component="h1">{title}</Typography>{description && <Typography color="text.secondary" sx={{ mt: 0.75 }}>{description}</Typography>}</Box>
    {actions && <Stack direction="row" gap={1} flexWrap="wrap" sx={{ flexShrink: 0 }}>{actions}</Stack>}
  </Stack>;
}

export function Panel({ children }: { children: ReactNode }) {
  return <Paper variant="outlined" sx={{ borderRadius: 2, overflow: 'hidden', minWidth: 0 }}>{children}</Paper>;
}

export function SectionHeading({ title, actions }: { title: string; actions?: ReactNode }) {
  return <Stack direction="row" alignItems="center" justifyContent="space-between" gap={2} sx={{ px: 2.5, py: 2, borderBottom: 1, borderColor: 'divider' }}>
    <Typography variant="h6" component="h2">{title}</Typography>{actions}
  </Stack>;
}

export function EmptyState({ title, description }: { title: string; description?: string }) {
  return <Box sx={{ px: 3, py: 6, textAlign: 'center' }}><Typography fontWeight={700}>{title}</Typography>{description && <Typography color="text.secondary" sx={{ mt: 1 }}>{description}</Typography>}</Box>;
}

export function Metric({ label, value, hint, progress }: { label: string; value: ReactNode; hint?: string; progress?: number }) {
  return <Paper variant="outlined" sx={{ p: 2.25, borderRadius: 2, minWidth: 0 }}>
    <Typography color="text.secondary" sx={{ fontSize: 11, textTransform: 'uppercase', letterSpacing: '0.04em', fontWeight: 700 }}>{label}</Typography>
    <Typography sx={{ fontSize: 29, lineHeight: 1.3, fontWeight: 800, my: 0.5 }}>{value}</Typography>
    {hint && <Typography variant="caption" color="text.secondary">{hint}</Typography>}
    {progress !== undefined && <LinearProgress variant="determinate" value={Math.max(0, Math.min(100, progress))} sx={{ mt: 1.5, height: 4, borderRadius: 2, bgcolor: '#e7f5fb' }} />}
  </Paper>;
}

export function StatusBadge({ status }: { status: TicketStatus }) {
  const { t } = useTranslation();
  const style = statusAppearance[status];
  return <Chip size="small" label={<Stack direction="row" alignItems="center" spacing={0.75}><Box aria-hidden sx={{ width: 6, height: 6, bgcolor: 'currentColor', borderRadius: '50%' }} /><span>{t(`status.${status}`)}</span></Stack>} sx={{ height: 25, fontSize: 11, fontWeight: 800, color: style.color, bgcolor: style.background }} />;
}

export function PriorityText({ priority }: { priority: Priority | null }) {
  const { t } = useTranslation();
  const colors: Record<Priority, string> = { URGENT: '#bd3f43', HIGH: '#b56216', MEDIUM: '#1378a7', LOW: '#667986' };
  return <Typography component="span" variant="body2" fontWeight={700} sx={{ color: priority ? colors[priority] : 'text.secondary', whiteSpace: 'nowrap' }}>{priority ? t(`priority.${priority}`) : t('tickets.pendingTriage')}</Typography>;
}
