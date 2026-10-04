import { useQuery } from '@tanstack/react-query';
import { Box, Button, Link, Stack, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import Add from '@mui/icons-material/Add';
import { Link as RouterLink } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { apiClient } from '../shared/api/client';
import { Failure, Loading } from '../shared/components/DataFeedback';
import { EmptyState, Metric, PageHeading, Panel, SectionHeading, StatusBadge } from '../shared/components/PageLayout';
import { StatusDistribution } from '../shared/components/StatusDistribution';

export function DashboardPage() {
  const { t } = useTranslation();
  const dashboard = useQuery({ queryKey: ['dashboard'], queryFn: apiClient.dashboard });
  const session = useQuery({ queryKey: ['session'], queryFn: apiClient.me });
  if (dashboard.isPending || session.isPending) return <Loading />;
  if (dashboard.isError || session.isError) return <Failure error={dashboard.error ?? session.error} />;
  const data = dashboard.data;
  const total = data.statusDistribution.reduce((sum, item) => sum + item.count, 0);
  const metrics = [
    { key: 'active', value: data.activeCount, hint: t('dashboard.activeHint') },
    { key: 'priority', value: data.highPriorityCount, hint: t('dashboard.priorityHint') },
    { key: 'validation', value: data.validationCount, hint: t('dashboard.validationHint') },
    { key: 'waiting', value: data.waitingCount, hint: t('dashboard.waitingHint') }
  ];
  return <Stack spacing={2.5}>
    <PageHeading title={t('dashboard.title')} description={t('dashboard.subtitle', { role: t(`role.${session.data.role}`) })} actions={session.data.role !== 'TRAFFIC_MANAGER' && <Button component={RouterLink} to="/tickets/new" startIcon={<Add />} variant="contained">{t('actions.create')}</Button>} />
    <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr 1fr', lg: 'repeat(4,1fr)' }, gap: 1.75 }}>{metrics.map(metric => <Metric key={metric.key} label={t(`dashboard.${metric.key}`)} value={metric.value} hint={metric.hint} progress={total ? metric.value / total * 100 : 0} />)}</Box>
    <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', lg: 'minmax(0,1.8fr) minmax(270px,0.8fr)' }, gap: 2.25, alignItems: 'start' }}>
      <Panel><SectionHeading title={t('dashboard.recent')} actions={<Button component={RouterLink} to="/tickets">{t('dashboard.viewAll')}</Button>} />
        {!data.recent.length ? <EmptyState title={t('tickets.noItems')} /> : <TableContainer><Table size="small" aria-label={t('dashboard.recent')} sx={{ minWidth: 500 }}><TableHead><TableRow><TableCell>{t('tickets.number')}</TableCell><TableCell>{t('tickets.client')}</TableCell><TableCell>{t('tickets.status')}</TableCell></TableRow></TableHead><TableBody>{data.recent.map(item => <TableRow key={item.id} hover><TableCell><Link component={RouterLink} to={`/tickets/${item.id}`} fontWeight={700} color="text.primary" underline="hover" sx={{display:'block'}}>{item.number} · {item.subject}</Link><Typography variant="caption" color="text.secondary" display="block" sx={{ mt: 0.5 }}>{new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' }).format(new Date(item.updatedAt))}</Typography></TableCell><TableCell>{item.clientName}</TableCell><TableCell><StatusBadge status={item.status} /></TableCell></TableRow>)}</TableBody></Table></TableContainer>}
      </Panel>
      <Panel><SectionHeading title={t('dashboard.distribution')} /><Box sx={{ p: 2.5 }}><StatusDistribution distribution={data.statusDistribution} /><Box sx={{ mt: 3, pt: 2.5, borderTop: 1, borderColor: 'divider' }}><Typography variant="h6">{t('dashboard.slaHealth')}</Typography><Typography variant="caption" color="text.secondary">{t('dashboard.classified', { count: data.classifiedCount })}</Typography><Typography sx={{ mt: 1, fontSize: 28, fontWeight: 800, color: data.overdueCount ? 'error.main' : 'secondary.main' }}>{data.overdueCount}</Typography><Typography variant="body2" color="text.secondary">{t('dashboard.overdue')}</Typography></Box></Box></Panel>
    </Box>
  </Stack>;
}
