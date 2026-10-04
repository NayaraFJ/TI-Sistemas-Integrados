import { Box, LinearProgress, Stack, Typography } from '@mui/material';
import { PieChart } from '@mui/x-charts/PieChart';
import { useTranslation } from 'react-i18next';
import type { TicketStatus } from '../api/types';
import { statusAppearance, ticketStatuses } from './PageLayout';

export function StatusDistribution({ distribution, bars = false }: { distribution: { status: TicketStatus; count: number }[]; bars?: boolean }) {
  const { t } = useTranslation();
  const total = distribution.reduce((sum, item) => sum + item.count, 0);
  if (bars) return <Stack spacing={2}>{ticketStatuses.map(status => {
    const count = distribution.find(item => item.status === status)?.count ?? 0;
    return <Box key={status}><Stack direction="row" justifyContent="space-between" sx={{ mb: 0.75 }}><Typography variant="body2">{t(`status.${status}`)}</Typography><Typography variant="body2" fontWeight={700}>{count}</Typography></Stack><LinearProgress variant="determinate" value={total ? count / total * 100 : 0} sx={{ height: 4, borderRadius: 2, bgcolor: '#e7f5fb', '& .MuiLinearProgress-bar': { bgcolor: statusAppearance[status].color } }} /></Box>;
  })}</Stack>;
  const slices = distribution.filter(item => item.count > 0).map(item => ({ id: item.status, value: item.count, label: t(`status.${item.status}`), color: statusAppearance[item.status].color }));
  return <Box>
    <Box sx={{ position: 'relative' }}><PieChart skipAnimation height={200} series={[{ data: slices, innerRadius: 56, outerRadius: 82, paddingAngle: 1, cornerRadius: 2 }]} hideLegend />
      <Stack alignItems="center" justifyContent="center" sx={{ position: 'absolute', inset: 0, pointerEvents: 'none' }}><Typography fontWeight={800}>{total}</Typography><Typography variant="caption" color="text.secondary">tickets</Typography></Stack>
    </Box>
    <Stack spacing={0.75}>{slices.map(item => <Stack key={item.id} direction="row" alignItems="center" spacing={1}><Box aria-hidden sx={{ width: 7, height: 7, borderRadius: 0.25, bgcolor: item.color }} /><Typography variant="caption" color="text.secondary" sx={{ flexGrow: 1 }}>{item.label}</Typography><Typography variant="caption" fontWeight={700}>{item.value}</Typography></Stack>)}</Stack>
  </Box>;
}
