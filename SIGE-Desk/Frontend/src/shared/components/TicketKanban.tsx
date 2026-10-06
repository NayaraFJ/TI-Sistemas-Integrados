import { ControlledBoard, type KanbanBoard } from '@caldwell619/react-kanban';
import '@caldwell619/react-kanban/dist/styles.css';
import { Alert, Box, Card, CardContent, Chip, IconButton, Stack, Tooltip, Typography } from '@mui/material';
import ChevronLeft from '@mui/icons-material/ChevronLeft';
import ChevronRight from '@mui/icons-material/ChevronRight';
import { useRef } from 'react';
import { Link } from 'react-router-dom';
import { useTranslation } from 'react-i18next';
import { PriorityText } from './PageLayout';
import type { TicketItem, TicketStatus } from '../api/types';

const columns: { status: TicketStatus; color: string }[] = [
  { status: 'OPEN', color: '#0b5d84' },
  { status: 'TRIAGE', color: '#7661a6' },
  { status: 'EXECUTION', color: '#147fa3' },
  { status: 'WAITING_FOR_CLIENT', color: '#b97713' },
  { status: 'VALIDATION', color: '#0b826d' },
  { status: 'DONE', color: '#39834c' },
  { status: 'REOPENED', color: '#b05d28' },
  { status: 'CANCELLED', color: '#6b7280' }
];
type TicketCard = { id: string; title: string; ticket: TicketItem };

export function TicketKanban({ items }: { items: TicketItem[] }) {
  const { t } = useTranslation();
  const container = useRef<HTMLDivElement>(null);
  const scrollColumns = (direction: number) => container.current?.querySelector('.react-kanban-board')?.scrollBy({ left: direction * 308, behavior: 'smooth' });
  if (!items.length) return <Alert severity="info">{t('tickets.noItems')}</Alert>;
  const board: KanbanBoard<TicketCard> = {
    columns: columns.map(({ status }) => ({
      id: status,
      title: t(`status.${status}`),
      cards: items.filter(item => item.status === status).map(ticket => ({
        id: ticket.id, title: ticket.subject, ticket
      }))
    }))
  };

  return <Box ref={container} role="region" aria-label={t('actions.kanban')} tabIndex={0} sx={{
    minWidth: 0, width: '100%', borderRadius: 2,
    '&:focus-visible': { outline: '2px solid', outlineColor: 'primary.main', outlineOffset: 3 },
    '& .react-kanban-board': { p: 0, pb: 2, overflowX: 'auto', scrollbarWidth: 'thin' },
    '& .react-kanban-column': {
      width: 270, minWidth: 270, minHeight: 360, boxSizing: 'border-box',
      m: 0, mr: 2, p: 1.5, borderRadius: 2, bgcolor: '#eaf0f4',
      border: '1px solid #dce5eb'
    },
    '& .react-kanban-card-skeleton': { minWidth: 244, maxWidth: 244 }
  }}>
    <Stack direction="row" alignItems="center" justifyContent="space-between" sx={{ mb: 1.5 }}>
      <Typography variant="body2" color="text.secondary">{t('tickets.kanbanCount', { count: items.length })}</Typography>
      <Stack direction="row" spacing={0.5}>
        <Tooltip title={t('tickets.previousColumn')}><IconButton aria-label={t('tickets.previousColumn')} onClick={() => scrollColumns(-1)} size="small"><ChevronLeft /></IconButton></Tooltip>
        <Tooltip title={t('tickets.nextColumn')}><IconButton aria-label={t('tickets.nextColumn')} onClick={() => scrollColumns(1)} size="small"><ChevronRight /></IconButton></Tooltip>
      </Stack>
    </Stack>
    <ControlledBoard<TicketCard>
      disableCardDrag disableColumnDrag
      allowAddCard={false} allowRemoveCard={false} allowAddColumn={false}
      allowRemoveColumn={false} allowRenameColumn={false}
      renderColumnHeader={column => <Stack direction="row" alignItems="center" spacing={1} sx={{ mb: 2, minHeight: 32 }}>
        <Box aria-hidden sx={{ width: 8, height: 8, borderRadius: '50%', bgcolor: columns.find(item => item.status === column.id)?.color, flexShrink: 0 }} />
        <Typography variant="body2" fontWeight={700} sx={{ flexGrow: 1 }}>{column.title}</Typography>
        <Chip size="small" label={column.cards.length} sx={{ bgcolor: 'white', fontWeight: 700 }} />
      </Stack>}
      renderCard={({ ticket }) => <Card component={Link} to={`/tickets/${ticket.id}`} variant="outlined" sx={{
        display: 'block', width: 244, mb: 1.5, textDecoration: 'none', color: 'inherit',
        borderColor: '#dce5eb', borderRadius: 1.5, boxShadow: '0 2px 4px rgba(6,59,90,0.04)',
        transition: 'border-color 150ms, box-shadow 150ms',
        '&:hover': { borderColor: 'primary.main', boxShadow: '0 4px 12px rgba(6,59,90,0.12)' },
        '&:focus-visible': { outline: '2px solid', outlineColor: 'primary.main' }
      }}>
        <CardContent sx={{ p: 2, '&:last-child': { pb: 2 } }}>
          <Stack direction="row" justifyContent="space-between" alignItems="center" gap={1}><Typography variant="caption" color="primary" fontWeight={800}>{ticket.number}</Typography><PriorityText priority={ticket.priority}/></Stack>{ticket.overdue&&<Chip size="small" color="error" variant="outlined" label={t('dashboard.overdue')} sx={{mt:1}}/>}
          <Typography variant="body2" fontWeight={600} sx={{ mt: 0.5, minHeight: 42, overflowWrap: 'anywhere' }}>{ticket.subject}</Typography>
          <Typography variant="caption" display="block" color="text.secondary" sx={{ mt: 1 }}>{ticket.clientName}</Typography>
          <Typography variant="caption" display="block" color="text.secondary">{ticket.typeName}</Typography><Typography variant="caption" display="block" color="text.secondary" sx={{ mt: 1, pt: 1, borderTop: 1, borderColor: 'divider' }}>{ticket.campaignName ?? 'Campanha pendente'}</Typography>
          <Stack direction="row" alignItems="center" justifyContent="space-between" gap={1} sx={{ mt: 1.5 }}>
            <Typography variant="caption" color="text.secondary">{new Intl.DateTimeFormat('pt-BR', {dateStyle: 'short'}).format(new Date(ticket.updatedAt))}</Typography>
            <Typography variant="caption" color="text.secondary" sx={{ textAlign: 'right', overflowWrap: 'anywhere' }}>{ticket.assigneeName ?? t('tickets.unassigned')}</Typography>
          </Stack>
        </CardContent>
      </Card>}
    >{board}</ControlledBoard>
  </Box>;
}
