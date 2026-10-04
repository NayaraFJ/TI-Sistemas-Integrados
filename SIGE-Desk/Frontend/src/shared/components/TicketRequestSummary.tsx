import { Alert, Box, Card, CardContent, Stack, Typography } from '@mui/material';
import { useTranslation } from 'react-i18next';
import type { TicketDetail } from '../api/types';
import { PriorityText } from './PageLayout';

export function TicketRequestSummary({ data }: { data: TicketDetail }) {
  const { t } = useTranslation();
  const ticket = data.ticket;
  const details = [
    { label: t('tickets.type'), value: ticket.typeName },
    { label: t('tickets.campaign'), value: ticket.campaignName ?? data.pendingCampaign ?? '—' },
    { label: t('tickets.priority'), value: <PriorityText priority={ticket.priority} /> },
    { label: t('tickets.urgency'), value: t(`priority.${ticket.urgency}`) },
    { label: t('tickets.assignee'), value: ticket.assigneeName ?? t('tickets.unassigned') },
    { label: t('tickets.desiredDate'), value: data.desiredDate ? new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeZone: 'UTC' }).format(new Date(`${data.desiredDate}T12:00:00Z`)) : '—' },
    { label: t('tickets.requester'), value: data.requesterName },
    { label: t('tickets.author'), value: data.authorName }
  ];
  return <Stack spacing={2}>
    {data.waitReason && <Alert severity="warning">{t('tickets.waitReason')}: {data.waitReason}{data.complementReceived && ` · ${t('tickets.complementReceived')}`}</Alert>}
    <Card><CardContent><Typography variant="h6">{t('tickets.requestData')}</Typography><Box component="dl" sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: '1fr 1fr' }, gap: 2, m: 0, mt: 2 }}>{details.map(detail => <Box key={detail.label}><Typography component="dt" variant="caption" color="text.secondary">{detail.label}</Typography><Typography component="dd" variant="body2" sx={{ m: 0, mt: 0.5 }}>{detail.value}</Typography></Box>)}</Box><Typography variant="caption" color="text.secondary" display="block" sx={{ mt: 2.5 }}>{t('tickets.description')}</Typography><Typography variant="body2" sx={{ mt: 0.5, whiteSpace: 'pre-wrap' }}>{data.description}</Typography></CardContent></Card>
  </Stack>;
}
