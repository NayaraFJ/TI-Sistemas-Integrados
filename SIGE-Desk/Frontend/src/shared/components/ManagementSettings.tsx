import { Box, Button, Checkbox, FormControlLabel, IconButton, MenuItem, Stack, TextField, Typography } from '@mui/material';
import Add from '@mui/icons-material/Add';
import DeleteOutline from '@mui/icons-material/DeleteOutline';
import type { Priority } from '../api/types';
import { useTranslation } from 'react-i18next';

type Definition = { name:string; label?:string; required:boolean; type?:'TEXT'|'NUMBER'|'DATE'|'SELECT'|'FILE'; options?:string[]; requiredWhen?:{field:string;equals:string}; validation?:Record<string,number|string>; };
function array<T>(source: unknown): T[] { try { const parsed = typeof source === 'string' ? JSON.parse(source) : source; return Array.isArray(parsed) ? parsed : []; } catch { return []; } }
export function DynamicFieldsEditor({value,onChange}:{value:unknown;onChange:(value:string)=>void}){
  const fields=array<Definition>(value);
  const save=(next:Definition[])=>onChange(JSON.stringify(next));
  const update=(index:number,change:Partial<Definition>)=>save(fields.map((field,at)=>at===index?{...field,...change}:field));
  const limit=(index:number,key:string,input:string)=>{const validation={...fields[index].validation};if(input==='')delete validation[key];else validation[key]=key.endsWith('Date')?input:Number(input);update(index,{validation});};
  const types={TEXT:'Texto',NUMBER:'Número',DATE:'Data',SELECT:'Seleção',FILE:'Anexo'};
  return <Stack spacing={2}><Typography variant="h6">Campos adicionais da solicitação</Typography>{fields.map((field,index)=><Stack key={index} spacing={1.5} sx={{p:2,border:1,borderColor:'divider',borderRadius:1.5}}>
    <Stack direction="row" spacing={1}><TextField fullWidth required label="Nome do campo" value={field.name} onChange={event=>update(index,{name:event.target.value})}/><IconButton aria-label={`Remover campo ${field.name||index+1}`} onClick={()=>save(fields.filter((_,at)=>at!==index))}><DeleteOutline/></IconButton></Stack>
    <TextField label="Rótulo exibido" value={field.label??''} onChange={event=>update(index,{label:event.target.value})}/>
    <TextField select label="Tipo do campo" value={field.type??'TEXT'} onChange={event=>update(index,{type:event.target.value as Definition['type'],options:undefined,validation:undefined})}>{Object.entries(types).map(([key,label])=><MenuItem key={key} value={key}>{label}</MenuItem>)}</TextField>
    {field.type==='SELECT'&&<TextField multiline minRows={3} required label="Opções (uma por linha)" value={(field.options??[]).join('\n')} onChange={event=>update(index,{options:event.target.value.split('\n')})}/>}
    <FormControlLabel control={<Checkbox checked={field.required} onChange={event=>update(index,{required:event.target.checked})}/>} label="Sempre obrigatório"/>
    <TextField select label="Obrigatório quando o campo" value={field.requiredWhen?.field??''} onChange={event=>update(index,{requiredWhen:event.target.value?{field:event.target.value,equals:field.requiredWhen?.equals??''}:undefined})}><MenuItem value="">Sem condição</MenuItem>{fields.filter(other=>other.name&&other.name!==field.name).map(other=><MenuItem key={other.name} value={other.name}>{other.label||other.name}</MenuItem>)}</TextField>
    {field.requiredWhen&&<TextField label="Tiver o valor" value={field.requiredWhen.equals} onChange={event=>update(index,{requiredWhen:{...field.requiredWhen!,equals:event.target.value}})}/>}
    {(field.type??'TEXT')==='TEXT'&&<Stack direction="row" spacing={1}><TextField type="number" label="Comprimento mínimo" value={field.validation?.minLength??''} onChange={event=>limit(index,'minLength',event.target.value)}/><TextField type="number" label="Comprimento máximo" value={field.validation?.maxLength??''} onChange={event=>limit(index,'maxLength',event.target.value)}/></Stack>}
    {field.type==='NUMBER'&&<Stack direction="row" spacing={1}><TextField type="number" label="Valor mínimo" value={field.validation?.min??''} slotProps={{htmlInput:{step:'any'}}} onChange={event=>limit(index,'min',event.target.value)}/><TextField type="number" label="Valor máximo" value={field.validation?.max??''} slotProps={{htmlInput:{step:'any'}}} onChange={event=>limit(index,'max',event.target.value)}/></Stack>}
    {field.type==='DATE'&&<Stack direction="row" spacing={1}><TextField type="date" label="Data mínima" slotProps={{inputLabel:{shrink:true}}} value={field.validation?.minDate??''} onChange={event=>limit(index,'minDate',event.target.value)}/><TextField type="date" label="Data máxima" slotProps={{inputLabel:{shrink:true}}} value={field.validation?.maxDate??''} onChange={event=>limit(index,'maxDate',event.target.value)}/></Stack>}
  </Stack>)}<Button variant="outlined" startIcon={<Add/>} onClick={()=>save([...fields,{name:'',required:false,type:'TEXT'}])}>Adicionar campo</Button></Stack>;
}

export function SlaCalendarEditor({ values, onChange }: { values: Record<string, unknown>; onChange: (key: string, value: unknown) => void }) {
  const days = array<string>(values.businessDays);
  const holidays = array<string>(values.holidays);
  return <Stack spacing={2}>
    <Typography variant="h6">Calendário de atendimento</Typography>
    <TextField select label="Dias de atendimento" value={days} SelectProps={{ multiple: true }} onChange={event => onChange('businessDays', JSON.stringify(typeof event.target.value === 'string' ? event.target.value.split(',') : event.target.value))} required>
      {Object.entries({ MONDAY: 'Segunda', TUESDAY: 'Terça', WEDNESDAY: 'Quarta', THURSDAY: 'Quinta', FRIDAY: 'Sexta', SATURDAY: 'Sábado', SUNDAY: 'Domingo' }).map(([key, label]) => <MenuItem key={key} value={key}>{label}</MenuItem>)}
    </TextField>
    <Typography variant="body2" color="text.secondary">Feriados nacionais fixos são excluídos automaticamente. Cadastre abaixo feriados locais, religiosos e recessos da agência.</Typography>
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
