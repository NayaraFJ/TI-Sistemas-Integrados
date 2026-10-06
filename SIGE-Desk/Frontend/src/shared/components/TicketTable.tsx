import { Link as RouterLink } from 'react-router-dom';
import { Box, Chip, Link, Table, TableBody, TableCell, TableContainer, TableHead, TableRow, Typography } from '@mui/material';
import { useTranslation } from 'react-i18next';
import type { TicketItem } from '../api/types';
import { EmptyState, Panel, PriorityText, StatusBadge } from './PageLayout';

export function TicketTable({ items, embedded = false }: { items: TicketItem[]; embedded?: boolean }) {
  const { t } = useTranslation();
  const content = !items.length ? <EmptyState title={t('tickets.noItems')} /> : <TableContainer>
    <Table size="small" aria-label={t('tickets.title')} sx={{ minWidth: 740 }}>
      <TableHead><TableRow>{['number', 'client', 'status', 'priority', 'assignee', 'updated'].map(key => <TableCell key={key}>{t(`tickets.${key}`)}</TableCell>)}</TableRow></TableHead>
      <TableBody>{items.map(item => <TableRow key={item.id} hover>
        <TableCell sx={{ minWidth: 230, maxWidth: 360 }}><Link component={RouterLink} to={`/tickets/${item.id}`} underline="hover" color="text.primary" fontWeight={700} sx={{ display: 'block' }}>{item.number} · {item.subject}</Link><Typography variant="caption" display="block" color="text.secondary" sx={{ mt: 0.5 }}>{item.typeName}{item.campaignName ? ` · ${item.campaignName}` : ''}</Typography></TableCell>
        <TableCell>{item.clientName}</TableCell>
        <TableCell><StatusBadge status={item.status} />{item.overdue&&<Chip size="small" color="error" variant="outlined" label={t('dashboard.overdue')} sx={{mt:0.5}}/>}</TableCell>
        <TableCell><PriorityText priority={item.priority} /></TableCell>
        <TableCell>{item.assigneeName ?? t('tickets.unassigned')}</TableCell>
        <TableCell sx={{ whiteSpace: 'nowrap' }}>{new Intl.DateTimeFormat('pt-BR', { dateStyle: 'short', timeStyle: 'short' }).format(new Date(item.updatedAt))}</TableCell>
      </TableRow>)}</TableBody>
    </Table>
  </TableContainer>;
  return embedded ? <Box>{content}</Box> : <Panel>{content}</Panel>;
}
