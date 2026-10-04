import { useState } from 'react';
import { useMutation, useQuery, useQueryClient } from '@tanstack/react-query';
import { Box, Button, Chip, Link, Stack, Tab, Tabs, Typography } from '@mui/material';
import DoneAll from '@mui/icons-material/DoneAll';
import { Link as RouterLink, useNavigate } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { apiClient } from '../shared/api/client';
import { Failure, Loading } from '../shared/components/DataFeedback';
import { EmptyState, PageHeading, Panel } from '../shared/components/PageLayout';
import type { NotificationItem } from '../shared/api/types';

export function NotificationsPage() {
  const { t } = useTranslation();
  const cache = useQueryClient();
  const navigate = useNavigate();
  const [tab, setTab] = useState<'all' | 'unread'>('all');
  const query = useQuery({ queryKey: ['notifications'], queryFn: apiClient.notifications });
  const refresh = () => cache.invalidateQueries({ queryKey: ['notifications'] });
  const read = useMutation({ mutationFn: apiClient.readNotification, onSuccess: refresh });
  const readAll = useMutation({ mutationFn: apiClient.readAllNotifications, onSuccess: refresh });
  const open = useMutation({ mutationFn: async (item: NotificationItem) => { if (!item.readAt) await apiClient.readNotification(item.id); return item; }, onSuccess: item => { void refresh(); navigate(`/tickets/${item.ticketId}`); } });
  if (query.isPending) return <Loading />;
  if (query.isError) return <Failure error={query.error} />;
  const items = query.data.items.filter(item => tab === 'all' || !item.readAt);
  const pending = read.isPending || readAll.isPending || open.isPending;
  return <Stack spacing={2.5}>
    <PageHeading title={t('notifications.title')} description={t('notifications.subtitle')} actions={<Button startIcon={<DoneAll />} variant="outlined" disabled={pending || !query.data.unreadCount} onClick={() => readAll.mutate()}>{t('notifications.markAll')}</Button>} />
    {(read.isError || readAll.isError || open.isError) && <Failure error={read.error ?? readAll.error ?? open.error} />}
    <Panel>
      <Tabs value={tab} onChange={(_, value: 'all' | 'unread') => setTab(value)} sx={{ px: 1.5, borderBottom: 1, borderColor: 'divider' }} aria-label={t('notifications.filters')}><Tab value="all" label={t('notifications.all')} /><Tab value="unread" label={`${t('notifications.unread')} (${query.data.unreadCount})`} /></Tabs>
      {!items.length ? <EmptyState title={t('notifications.upToDate')} description={t('notifications.empty')} /> : <Stack spacing={1.25} sx={{ p: 2.5 }}>{items.map(item => <Stack key={item.id} direction={{ xs: 'column', sm: 'row' }} alignItems={{ sm: 'center' }} justifyContent="space-between" gap={1.5} sx={{ p: 1.75, border: 1, borderColor: 'divider', borderRadius: 1.5, bgcolor: item.readAt ? 'white' : '#f2fafc' }}>
        <Box sx={{ minWidth: 0 }}><Stack direction="row" gap={1} alignItems="center"><Link component={RouterLink} to={`/tickets/${item.ticketId}`} onClick={event => { if (!event.ctrlKey && !event.metaKey && !event.shiftKey && event.button === 0) { event.preventDefault(); open.mutate(item); } }} underline="hover" color="text.primary" fontWeight={700}>{item.ticketNumber}</Link>{!item.readAt && <Chip label={t('notifications.new')} size="small" sx={{ height: 20, fontSize: 10, color: 'primary.main', bgcolor: '#e7f5fb' }} />}</Stack><Typography variant="body2" color="text.secondary" sx={{ mt: 0.5, overflowWrap: 'anywhere' }}>{item.summary}</Typography><Typography variant="caption" color="text.secondary">{new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' }).format(new Date(item.createdAt))}</Typography></Box>
        <Stack direction="row" spacing={1} sx={{ flexShrink: 0 }}>{!item.readAt && <Button size="small" onClick={() => read.mutate(item.id)} disabled={pending}>{t('actions.markRead')}</Button>}<Button size="small" variant="outlined" onClick={() => open.mutate(item)} disabled={pending}>{t('actions.open')}</Button></Stack>
      </Stack>)}</Stack>}
    </Panel>
  </Stack>;
}
