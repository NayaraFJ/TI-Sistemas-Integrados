import { Box, Button, Checkbox, FormControlLabel, IconButton, MenuItem, Stack, TextField, Typography } from '@mui/material';
import Add from '@mui/icons-material/Add';
import DeleteOutline from '@mui/icons-material/DeleteOutline';
import type { Priority } from '../api/types';
import { useTranslation } from 'react-i18next';

type Definition = { name: string; label?: string; required: boolean; [key: string]: unknown };
function array<T>(source: unknown): T[] { try { const parsed = typeof source === 'string' ? JSON.parse(source) : source; return Array.isArray(parsed) ? parsed : []; } catch { return []; } }
export function DynamicFieldsEditor({ value, onChange }: { value: unknown; onChange: (value: string) => void }) {
  const fields = array<Definition>(value);
  const save = (next: Definition[]) => onChange(JSON.stringify(next));
  return <Stack spacing={1.5}>
    <Typography variant="h6">Campos adicionais da solicitação</Typography>
    {fields.map((field, index) => <Stack key={index} spacing={1} sx={{ p: 1.5, border: 1, borderColor: 'divider', borderRadius: 1.5 }}>
      <Stack direction="row" spacing={1}><TextField required fullWidth label="Nome do campo" value={field.name} onChange={event => save(fields.map((current, at) => at === index ? { ...current, name: event.target.value } : current))} /><IconButton aria-label={`Remover campo ${field.name || index + 1}`} onClick={() => save(fields.filter((_, at) => at !== index))}><DeleteOutline /></IconButton></Stack>
      <TextField fullWidth label="Rótulo exibido (opcional)" value={field.label ?? ''} onChange={event => save(fields.map((current, at) => at === index ? { ...current, label: event.target.value } : current))} />
      <FormControlLabel control={<Checkbox size="small" checked={field.required} onChange={event => save(fields.map((current, at) => at === index ? { ...current, required: event.target.checked } : current))} />} label="Preenchimento obrigatório" />
    </Stack>)}
    <Button variant="outlined" startIcon={<Add />} onClick={() => save([...fields, { name: '', required: false }])}>Adicionar campo</Button>
  </Stack>;
}

export function SlaCalendarEditor({ values, onChange }: { values: Record<string, unknown>; onChange: (key: string, value: unknown) => void }) {
  const days = array<string>(values.businessDays);
  const holidays = array<string>(values.holidays);
  return <Stack spacing={2}>
    <Typography variant="h6">Calendário de atendimento</Typography>
    <TextField select label="Dias de atendimento" value={days} SelectProps={{ multiple: true }} onChange={event => onChange('businessDays', JSON.stringify(typeof event.target.value === 'string' ? event.target.value.split(',') : event.target.value))} required>
      {Object.entries({ MONDAY: 'Segunda', TUESDAY: 'Terça', WEDNESDAY: 'Quarta', THURSDAY: 'Quinta', FRIDAY: 'Sexta', SATURDAY: 'Sábado', SUNDAY: 'Domingo' }).map(([key, label]) => <MenuItem key={key} value={key}>{label}</MenuItem>)}
    </TextField>
    <Typography variant="body2" color="text.secondary">Feriados sem expediente</Typography>
    {holidays.map((holiday, index) => <Stack key={index} direction="row" spacing={1}><TextField fullWidth required type="date" label={`Feriado ${index + 1}`} slotProps={{ inputLabel: { shrink: true } }} value={holiday} onChange={event => onChange('holidays', JSON.stringify(holidays.map((current, at) => at === index ? event.target.value : current)))} /><IconButton aria-label={`Remover feriado ${index + 1}`} onClick={() => onChange('holidays', JSON.stringify(holidays.filter((_, at) => at !== index)))}><DeleteOutline /></IconButton></Stack>)}
    <Button variant="outlined" startIcon={<Add />} onClick={() => onChange('holidays', JSON.stringify([...holidays, '']))}>Adicionar feriado</Button>
  </Stack>;
}

export function SlaDeadlinesEditor({ value, onChange }: { value: unknown; onChange: (value: string) => void }) {
  const { t } = useTranslation();
  let deadlines: Record<string, { responseHours?: number; resolutionHours?: number }> = {};
  try { deadlines = typeof value === 'string' ? JSON.parse(value) : value as typeof deadlines; } catch { /* Os valores serão informados no formulário. */ }
  const change = (priority: Priority, key: 'responseHours' | 'resolutionHours', hours: string) => onChange(JSON.stringify({ ...deadlines, [priority]: { ...deadlines[priority], [key]: hours ? Number(hours) : null } }));
  return <Stack spacing={1.5}><Typography variant="h6">Prazos em horas úteis</Typography>{(['URGENT', 'HIGH', 'MEDIUM', 'LOW'] as Priority[]).map(priority => <Box key={priority} sx={{ display: 'grid', gridTemplateColumns: { xs: '1fr', sm: '90px 1fr 1fr' }, gap: 1.5, alignItems: 'center' }}>
    <Typography variant="body2" fontWeight={700}>{t(`priority.${priority}`)}</Typography>
    <TextField required type="number" label="Primeira resposta (h)" value={deadlines[priority]?.responseHours ?? ''} slotProps={{ htmlInput: { min: 1, step: 1 } }} onChange={event => change(priority, 'responseHours', event.target.value)} />
    <TextField required type="number" label="Resolução (h)" value={deadlines[priority]?.resolutionHours ?? ''} slotProps={{ htmlInput: { min: 1, step: 1 } }} onChange={event => change(priority, 'resolutionHours', event.target.value)} />
  </Box>)}</Stack>;
}
