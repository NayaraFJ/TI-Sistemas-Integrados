import { useState } from 'react';
import { Box, ButtonBase, Stack, Typography, useMediaQuery } from '@mui/material';
import { BarChart } from '@mui/x-charts/BarChart';
import { PieChart } from '@mui/x-charts/PieChart';
import { useTranslation } from 'react-i18next';
import type { TicketStatus } from '../api/types';
import { statusAppearance, ticketStatuses } from './PageLayout';

export function StatusDistribution({ distribution, bars = false }: { distribution: { status: TicketStatus; count: number }[]; bars?: boolean }) {
  const { t } = useTranslation();
  const reducedMotion = useMediaQuery('(prefers-reduced-motion: reduce)');
  const [selected, setSelected] = useState<TicketStatus | null>(null);
  const data = ticketStatuses.map(status => ({
    id: status, value: distribution.find(item => item.status === status)?.count ?? 0,
    label: t(`status.${status}`), color: statusAppearance[status].color
  }));
  const total = data.reduce((sum, item) => sum + item.value, 0);
  if (total === 0) return <Box sx={{ minHeight: 240, display: 'flex', alignItems: 'center', justifyContent: 'center', textAlign: 'center' }}><Typography color="text.secondary">{t('tickets.noItems')}</Typography></Box>;
  const summary = data.map(item => `${item.label}: ${item.value}`).join('; ');
  if (bars) return <Box role="img" aria-label={`${t('reports.distribution')}. ${summary}`}>
    <BarChart height={380} layout="horizontal" borderRadius={5} skipAnimation={reducedMotion} hideLegend
      margin={{ top: 8, bottom: 20, left: 0, right: 28 }}
      yAxis={[{ scaleType: 'band', data: data.map(item => item.label), width: 136, categoryGapRatio: 0.45, disableLine: true, disableTicks: true, colorMap: { type: 'ordinal', colors: data.map(item => item.color) }, tickLabelStyle: { fontSize: 12 } }]}
      xAxis={[{ min: 0, max: Math.max(...data.map(item => item.value)) * 1.2, tickMinStep: 1, disableLine: true, disableTicks: true }]}
      series={[{ id: 'status', data: data.map(item => item.value), label: t('tickets.title'), barLabel: 'value', barLabelPlacement: 'outside' }]}
      grid={{ vertical: true }} axisHighlight={{ y: 'band' }}
      sx={{ '& .MuiChartsGrid-line': { stroke: '#e7eef1', strokeDasharray: '3 4' }, '& .MuiBarLabel-root': { fontSize: 12, fontWeight: 500, fill: 'text.primary' } }}
    />
  </Box>;
  const slices = data.filter(item => item.value > 0);
  const selectedIndex = slices.findIndex(item => item.id === selected);
  const active = slices[selectedIndex];
  const percentage = (count: number) => new Intl.NumberFormat('pt-BR', { style: 'percent', maximumFractionDigits: 1 }).format(count / total);
  return <Box>
    <Box sx={{ position: 'relative' }}><PieChart skipAnimation={reducedMotion} height={240} margin={0}
      highlightedItem={active ? { seriesId: 'status', dataIndex: selectedIndex } : null}
      onHighlightChange={item => setSelected(item?.dataIndex === undefined ? null : slices[item.dataIndex]?.id ?? null)}
      series={[{ id: 'status', data: slices, innerRadius: 68, outerRadius: 94, paddingAngle: 3, cornerRadius: 5,
        highlightScope: { fade: 'global', highlight: 'item' }, highlighted: { additionalRadius: 5 }, faded: { additionalRadius: -2 },
        valueFormatter: item => `${item.value} · ${percentage(item.value)}`
      }]} hideLegend aria-label={`${t('dashboard.distribution')}. ${summary}`} />
      <Stack alignItems="center" justifyContent="center" sx={{ position: 'absolute', inset: 0, pointerEvents: 'none', width: 128, mx: 'auto' }}>
        <Typography sx={{ fontSize: 28, lineHeight: 1.2, fontWeight: 700 }}>{active?.value ?? total}</Typography>
        <Typography variant="caption" color="text.secondary" textAlign="center">{active?.label ?? t('charts.total')}</Typography>
        {active && <Typography variant="caption" color="text.secondary">{percentage(active.value)}</Typography>}
      </Stack>
    </Box>
    <Stack spacing={0.5}>{slices.map(item => <ButtonBase key={item.id} onMouseEnter={() => setSelected(item.id)} onMouseLeave={() => setSelected(null)} onFocus={() => setSelected(item.id)} onBlur={() => setSelected(null)} onClick={() => setSelected(item.id)} aria-label={t('charts.highlight', { status: item.label, count: item.value, percentage: percentage(item.value) })} sx={{ display: 'flex', gap: 1, justifyContent: 'flex-start', textAlign: 'left', px: 1, py: 0.75, borderRadius: 1, bgcolor: selected === item.id ? 'action.hover' : 'transparent', '&.Mui-focusVisible': { outline: '2px solid', outlineColor: 'primary.main' } }}>
      <Box aria-hidden sx={{ width: 8, height: 8, flexShrink: 0, borderRadius: '2px', bgcolor: item.color }} />
      <Typography variant="body2" sx={{ flexGrow: 1 }}>{item.label}</Typography>
      <Typography variant="caption" color="text.secondary" sx={{ whiteSpace: 'nowrap', fontVariantNumeric: 'tabular-nums' }}>{item.value} · {percentage(item.value)}</Typography>
    </ButtonBase>)}</Stack>
  </Box>;
}
