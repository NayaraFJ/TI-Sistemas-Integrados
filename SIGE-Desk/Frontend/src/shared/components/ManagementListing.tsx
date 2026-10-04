import { Avatar, Box, Button, Chip, Stack, Switch, TextField, Typography } from '@mui/material';
import { useTranslation } from 'react-i18next';
import type { ReferenceData, Role } from '../api/types';
import { EmptyState, Panel } from './PageLayout';

type Item = Record<string, unknown>;
export const scopeLabels: Record<string, string> = { DEFAULT: 'Padrão', CLIENT: 'Cliente', DEMAND_TYPE: 'Tipo de demanda', CLIENT_AND_DEMAND_TYPE: 'Cliente + tipo' };
const dayLabels: Record<string, string> = { MONDAY: 'Seg', TUESDAY: 'Ter', WEDNESDAY: 'Qua', THURSDAY: 'Qui', FRIDAY: 'Sex', SATURDAY: 'Sáb', SUNDAY: 'Dom' };
const value = (item: Item, key: string) => typeof item[key] === 'string' ? item[key] as string : '';
function parse(source: unknown): unknown { try { return typeof source === 'string' ? JSON.parse(source) : source; } catch { return null; } }

export function ManagementListing({ resource, items, reference, search, onSearch, onEdit, onToggle, pending, currentUserId }: {
  resource: string; items: Item[]; reference?: ReferenceData; search: string; onSearch: (search: string) => void;
  onEdit: (item: Item) => void; onToggle: (item: Item) => void; pending: boolean; currentUserId?: string;
}) {
  const { t } = useTranslation();
  const clientName = (item: Item) => value(item, 'clientName') || reference?.clients.find(client => client.id === item.clientId)?.label || (item.clientId ? t('admin.unavailableClient') : '');
  const describe = (item: Item): string => {
    if (resource === 'clients') return [value(item, 'contactName'), value(item, 'email'), value(item, 'phone')].filter(Boolean).join(' · ');
    if (resource === 'campaigns') return [clientName(item), value(item, 'channel'), value(item, 'objective')].filter(Boolean).join(' · ');
    if (resource === 'users') return [value(item, 'email'), t(`role.${item.role as Role}`), clientName(item)].filter(Boolean).join(' · ');
    if (resource === 'demand-types') {
      const fields = parse(item.fieldDefinitions);
      return `${Array.isArray(fields) ? fields.length : 0} campo(s) · Aprovação ${item.approvalRequired ? 'obrigatória' : 'dispensada'} · Evidência ${item.evidenceRequired ? 'obrigatória' : 'opcional'}`;
    }
    const days = parse(item.businessDays);
    const schedule = Array.isArray(days) ? days.map(day => dayLabels[String(day)] ?? String(day)).join(', ') : '';
    const deadlines = parse(item.deadlines) as Record<string, { responseHours: number; resolutionHours: number }> | null;
    const urgent = deadlines?.URGENT;
    const type = reference?.demandTypes.find(type => type.id === item.demandTypeId)?.name;
    return [schedule, `${value(item, 'businessStart').slice(0, 5)}–${value(item, 'businessEnd').slice(0, 5)}`, value(item, 'timezone'), clientName(item), type, urgent ? `Urgente: ${urgent.responseHours}h/${urgent.resolutionHours}h` : ''].filter(Boolean).join(' · ');
  };
  const visible = items.filter(item => `${value(item, 'name')} ${describe(item)} ${item.active ? 'ativo' : 'inativo'} ${scopeLabels[String(item.scope)] ?? ''}`.toLocaleLowerCase('pt-BR').includes(search.toLocaleLowerCase('pt-BR')));
  return <Panel>
    <Stack direction="row" alignItems="center" gap={1.5} sx={{ px: 2.5, py: 2, borderBottom: 1, borderColor: 'divider' }}>
      <TextField fullWidth size="small" label={t('actions.search')} placeholder={t('admin.searchPlaceholder')} value={search} onChange={event => onSearch(event.target.value)} />
      <Button variant="outlined" onClick={() => onSearch('')}>{t('actions.clear')}</Button>
    </Stack>
    <Box sx={{ p: 2.5 }}>{!visible.length ? <EmptyState title={t('admin.noRecords')} description={t('admin.emptyHint')} /> : <Stack spacing={1.25}>{visible.map(item => <Stack key={String(item.id)} direction={{ xs: 'column', sm: 'row' }} alignItems={{ sm: 'center' }} justifyContent="space-between" gap={1.5} sx={{ px: 1.75, py: 1.5, minHeight: 68, border: 1, borderColor: 'divider', borderRadius: 1.5 }}>
      <Stack direction="row" alignItems="center" spacing={1.25} sx={{ minWidth: 0 }}>
        {resource === 'users' && <Avatar sx={{ width: 36, height: 36, fontSize: 13, fontWeight: 800, bgcolor: '#d9f2ef', color: '#0c6658' }}>{value(item, 'name').split(' ').filter(Boolean).map(part => part[0]).slice(0, 2).join('')}</Avatar>}
        <Box sx={{ minWidth: 0 }}><Stack direction="row" alignItems="center" gap={1} flexWrap="wrap"><Typography fontWeight={700}>{value(item, 'name')}</Typography>{resource === 'sla-rules' && <Chip size="small" label={scopeLabels[String(item.scope)] ?? item.scope} sx={{ height: 22, bgcolor: '#e7f5fb', color: '#1378a7', fontSize: 11 }} />}</Stack><Typography variant="caption" color="text.secondary" sx={{ display: 'block', mt: 0.25, overflowWrap: 'anywhere' }}>{describe(item)}</Typography></Box>
      </Stack>
      <Stack direction="row" spacing={0.75} alignItems="center" sx={{ flexShrink: 0, alignSelf: { xs: 'flex-end', sm: 'auto' } }}>
        <Typography variant="caption" color={item.active ? 'text.primary' : 'text.secondary'}>{t(item.active ? 'admin.active' : 'admin.inactive')}</Typography>
        <Switch size="small" color="secondary" checked={Boolean(item.active)} disabled={pending || (resource === 'users' && item.id === currentUserId)} onChange={() => onToggle(item)} slotProps={{ input: { 'aria-label': `${t(item.active ? 'admin.deactivate' : 'admin.activate')} ${value(item, 'name')}` } }} />
        <Button size="small" variant="outlined" onClick={() => onEdit(item)} aria-label={`${t('admin.editAction')} ${value(item, 'name')}`}>{t('admin.editAction')}</Button>
      </Stack>
    </Stack>)}</Stack>}</Box>
  </Panel>;
}
