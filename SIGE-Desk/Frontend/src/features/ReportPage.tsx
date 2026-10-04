import { useState } from 'react';
import { keepPreviousData, useMutation, useQuery } from '@tanstack/react-query';
import { Box, Button, Stack, Typography } from '@mui/material';
import DownloadOutlined from '@mui/icons-material/DownloadOutlined';
import { useTranslation } from 'react-i18next';
import { downloadCsv } from '../shared/download';
import { apiClient } from '../shared/api/client';
import type { TicketFilters, TicketStatus } from '../shared/api/types';
import { Failure, Loading } from '../shared/components/DataFeedback';
import { Metric, PageHeading, Panel, SectionHeading } from '../shared/components/PageLayout';
import { StatusDistribution } from '../shared/components/StatusDistribution';
import { TicketFilterPanel } from '../shared/components/TicketFilterPanel';
import { TicketTable } from '../shared/components/TicketTable';

export function ReportPage() {
  const { t } = useTranslation();
  const [filters, setFilters] = useState<TicketFilters>({});
  const refs = useQuery({ queryKey: ['reference'], queryFn: apiClient.reference });
  const query = useQuery({ queryKey: ['report', filters], queryFn: () => apiClient.reportTickets(filters), placeholderData: keepPreviousData });
  const exportCsv = useMutation({ mutationFn: () => apiClient.exportTickets(filters), onSuccess: downloadCsv });
  if (query.isPending || refs.isPending) return <Loading />;
  if (query.isError || refs.isError) return <Failure error={query.error ?? refs.error} />;
  const summary = query.data.summary;
  const compliant = summary.classifiedCount ? Math.round(summary.slaCompliantCount / summary.classifiedCount * 100) : null;
  const average = summary.averageResolutionMinutes === null ? t('reports.notAvailable') : summary.averageResolutionMinutes >= 60 ? `${Math.round(summary.averageResolutionMinutes / 60 * 10) / 10} h` : `${summary.averageResolutionMinutes} min`;
  const counts = new Map<TicketStatus, number>();
  query.data.items.forEach(item => counts.set(item.status, (counts.get(item.status) ?? 0) + 1));
  const distribution = Array.from(counts, ([status, count]) => ({ status, count }));
  return <Stack spacing={2.5}>
    <PageHeading title={t('nav.reports')} description={t('reports.subtitle')} actions={<Button startIcon={<DownloadOutlined />} variant="outlined" onClick={() => exportCsv.mutate()} disabled={exportCsv.isPending}>{t('actions.export')}</Button>} />
    {exportCsv.isError && <Failure error={exportCsv.error} />}
    <Panel><Box sx={{ p: 2.5 }}><TicketFilterPanel filters={filters} onChange={setFilters} reference={refs.data} /></Box></Panel>
    <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr 1fr', lg: 'repeat(4,1fr)' }, gap: 1.75 }}>
      <Metric label={t('reports.total')} value={query.data.total} hint={t('reports.totalHint')} />
      <Metric label={t('reports.completed')} value={summary.completedCount} hint={t('reports.completedHint')} />
      <Metric label={t('reports.slaCompliant')} value={compliant === null ? '—' : `${compliant}%`} hint={`${summary.slaCompliantCount}/${summary.classifiedCount} ${t('reports.classified')}`} />
      <Metric label={t('reports.averageResolution')} value={average} hint={t('reports.averageHint')} />
    </Box>
    <Box sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', lg: 'minmax(0,1.8fr) minmax(270px,0.8fr)' }, gap: 2.25, alignItems: 'start' }}>
      <Panel><SectionHeading title={t('reports.distribution')} /><Box sx={{ p: 2.5 }}><StatusDistribution distribution={distribution} bars /></Box></Panel>
      <Panel><SectionHeading title={t('reports.quickRead')} /><Box sx={{ p: 2.5 }}><StatusDistribution distribution={distribution} /><Typography variant="body2" color="text.secondary" sx={{ mt: 2 }}>{t('reports.active')}: <strong>{summary.activeCount}</strong> · {t('reports.overdue')}: <strong>{summary.overdueCount}</strong></Typography></Box></Panel>
    </Box>
    <Panel><SectionHeading title={t('reports.tickets')} /><TicketTable items={query.data.items} embedded /></Panel>
  </Stack>;
}
