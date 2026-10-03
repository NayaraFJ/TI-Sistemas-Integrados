import { Box, Button, FormControl, InputLabel, MenuItem, Select, TextField } from '@mui/material';
import { useTranslation } from 'react-i18next';
import type { Priority, ReferenceData, TicketFilters, TicketStatus } from '../api/types';

const statuses: TicketStatus[] = ['OPEN', 'TRIAGE', 'EXECUTION', 'WAITING_FOR_CLIENT', 'VALIDATION', 'DONE', 'REOPENED', 'CANCELLED'];
const priorities: Priority[] = ['URGENT', 'HIGH', 'MEDIUM', 'LOW'];

type Props = {
  filters: TicketFilters;
  onChange: (filters: TicketFilters) => void;
  reference: ReferenceData;
  searchPlaceholder?: string;
};

export function TicketFilterPanel({ filters, onChange, reference, searchPlaceholder }: Props) {
  const { t } = useTranslation();
  const update = (key: keyof TicketFilters, value: string) => onChange({ ...filters, [key]: value || undefined });

  return <>
    <Box sx={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fit,minmax(180px,1fr))', gap: 2 }}>
      <TextField label={t('actions.search')} value={filters.search ?? ''} placeholder={searchPlaceholder} onChange={event => update('search', event.target.value)} />
      <FilterSelect label={t('tickets.status')} value={filters.status ?? ''} onChange={value => update('status', value)}>
        {statuses.map(item => <MenuItem value={item} key={item}>{t(`status.${item}`)}</MenuItem>)}
      </FilterSelect>
      <FilterSelect label={t('tickets.client')} value={filters.clientId ?? ''} onChange={value => update('clientId', value)}>
        {reference.clients.map(item => <MenuItem key={item.id} value={item.id}>{item.label}</MenuItem>)}
      </FilterSelect>
      <FilterSelect label={t('tickets.campaign')} value={filters.campaignId ?? ''} onChange={value => update('campaignId', value)}>
        {reference.campaigns.map(item => <MenuItem key={item.id} value={item.id}>{item.name}</MenuItem>)}
      </FilterSelect>
      <FilterSelect label={t('tickets.type')} value={filters.demandTypeId ?? ''} onChange={value => update('demandTypeId', value)}>
        {reference.demandTypes.map(item => <MenuItem key={item.id} value={item.id}>{item.name}</MenuItem>)}
      </FilterSelect>
      <FilterSelect label={t('tickets.priority')} value={filters.priority ?? ''} onChange={value => update('priority', value)}>
        {priorities.map(item => <MenuItem key={item} value={item}>{t(`priority.${item}`)}</MenuItem>)}
      </FilterSelect>
      <FilterSelect label={t('tickets.assignee')} value={filters.assigneeId ?? ''} onChange={value => update('assigneeId', value)}>
        {reference.trafficManagers.map(item => <MenuItem key={item.id} value={item.id}>{item.name}</MenuItem>)}
      </FilterSelect>
      <TextField type="date" InputLabelProps={{ shrink: true }} label={t('tickets.createdFrom')} value={filters.createdFrom ?? ''} onChange={event => update('createdFrom', event.target.value)} />
      <TextField type="date" InputLabelProps={{ shrink: true }} label={t('tickets.createdTo')} value={filters.createdTo ?? ''} onChange={event => update('createdTo', event.target.value)} />
    </Box>
    <Button sx={{ mt: 2 }} onClick={() => onChange({})}>{t('actions.clear')}</Button>
  </>;
}

function FilterSelect({ label, value, onChange, children }: { label: string; value: string; onChange: (value: string) => void; children: React.ReactNode }) {
  const { t } = useTranslation();
  return <FormControl><InputLabel>{label}</InputLabel><Select label={label} value={value} onChange={event => onChange(event.target.value)}><MenuItem value="">{t('actions.all')}</MenuItem>{children}</Select></FormControl>;
}
