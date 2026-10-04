import { useState } from 'react';
import { keepPreviousData, useMutation, useQuery } from '@tanstack/react-query';
import { Box, Button, LinearProgress, Stack, TablePagination, ToggleButton, ToggleButtonGroup, Typography } from '@mui/material';
import Add from '@mui/icons-material/Add';
import DownloadOutlined from '@mui/icons-material/DownloadOutlined';
import ViewKanbanOutlined from '@mui/icons-material/ViewKanbanOutlined';
import TableRowsOutlined from '@mui/icons-material/TableRowsOutlined';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { downloadCsv } from '../shared/download';
import { apiClient } from '../shared/api/client';
import type { TicketFilters } from '../shared/api/types';
import { Failure, Loading } from '../shared/components/DataFeedback';
import { PageHeading, Panel } from '../shared/components/PageLayout';
import { TicketFilterPanel } from '../shared/components/TicketFilterPanel';
import { TicketKanban } from '../shared/components/TicketKanban';
import { TicketTable } from '../shared/components/TicketTable';

export function TicketsPage() {
  const { t } = useTranslation();
  const [filters, setFilters] = useState<TicketFilters>({});
  const [page, setPage] = useState(0);
  const [size, setSize] = useState(25);
  const [view, setView] = useState<'kanban' | 'table'>('kanban');
  const refs = useQuery({ queryKey: ['reference'], queryFn: apiClient.reference });
  const session = useQuery({ queryKey: ['session'], queryFn: apiClient.me });
  const query = useQuery({ queryKey: ['tickets', filters, page, size], queryFn: () => apiClient.tickets({ ...filters, page, size }), placeholderData: keepPreviousData });
  const exportCsv = useMutation({ mutationFn: () => apiClient.exportTickets(filters), onSuccess: downloadCsv });
  if (query.isPending || refs.isPending || session.isPending) return <Loading />;
  if (query.isError || refs.isError || session.isError) return <Failure error={query.error ?? refs.error ?? session.error} />;
  const canExport = ['ADMIN', 'SERVICE'].includes(session.data.role);
  return <Stack spacing={2.5}>
    <PageHeading title={t('tickets.title')} description={t('tickets.subtitle')} actions={<>
      {canExport && <Button startIcon={<DownloadOutlined />} variant="outlined" onClick={() => exportCsv.mutate()} disabled={exportCsv.isPending}>{t('actions.export')}</Button>}
      {session.data.role !== 'TRAFFIC_MANAGER' && <Button component={Link} to="/tickets/new" startIcon={<Add />} variant="contained">{t('actions.create')}</Button>}
    </>} />
    {exportCsv.isError && <Failure error={exportCsv.error} />}
    <Panel>
      <Stack direction={{ xs: 'column', sm: 'row' }} alignItems={{ sm: 'center' }} justifyContent="space-between" gap={1.5} sx={{ px: 2.5, py: 1.75, borderBottom: 1, borderColor: 'divider', bgcolor: '#fafcfd' }}>
        <Typography variant="body2"><strong>{query.data.total} ticket(s)</strong><Box component="span" sx={{ color: 'text.secondary' }}> · {t(view === 'kanban' ? 'tickets.byStage' : 'tickets.inTable')}</Box></Typography>
        <ToggleButtonGroup size="small" exclusive value={view} onChange={(_, next: 'kanban' | 'table' | null) => { if (next) setView(next); }} aria-label={t('tickets.view')}>
          <ToggleButton value="kanban"><ViewKanbanOutlined sx={{ fontSize: 17, mr: 0.75 }} />{t('actions.kanban')}</ToggleButton>
          <ToggleButton value="table"><TableRowsOutlined sx={{ fontSize: 17, mr: 0.75 }} />{t('actions.table')}</ToggleButton>
        </ToggleButtonGroup>
      </Stack>
      <Box sx={{ p: 2.5, borderBottom: 1, borderColor: 'divider' }}><TicketFilterPanel filters={filters} onChange={next => { setFilters(next); setPage(0); }} reference={refs.data} searchPlaceholder={t('tickets.searchPlaceholder')} /></Box>
      <Box aria-busy={query.isFetching} sx={{ minWidth: 0 }}>{query.isFetching && <LinearProgress />}{view === 'kanban' ? <Box sx={{ p: 2, bgcolor: '#f1f6f8' }}><TicketKanban items={query.data.items} /></Box> : <TicketTable items={query.data.items} embedded />}</Box>
      <TablePagination component="div" count={query.data.total} page={page} rowsPerPage={size} rowsPerPageOptions={[10, 25, 50]} onPageChange={(_, next) => setPage(next)} onRowsPerPageChange={event => { setSize(Number(event.target.value)); setPage(0); }} labelRowsPerPage={t('tickets.rowsPerPage')} labelDisplayedRows={({ from, to, count }) => `${from}–${to} de ${count}`} getItemAriaLabel={type => t(type === 'next' ? 'tickets.nextPage' : 'tickets.previousPage')} sx={{ borderTop: 1, borderColor: 'divider', '& .MuiTablePagination-toolbar': { flexWrap: 'wrap', justifyContent: 'flex-end' } }} />
    </Panel>
  </Stack>;
}
