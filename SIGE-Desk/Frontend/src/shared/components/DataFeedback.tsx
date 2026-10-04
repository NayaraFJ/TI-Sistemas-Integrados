import { Alert, CircularProgress, Stack, Typography } from '@mui/material';
import { isAxiosError } from 'axios';
import { useTranslation } from 'react-i18next';

export function Loading() {
  const { t } = useTranslation();
  return <Stack alignItems="center" spacing={2} sx={{ py: 8 }}><CircularProgress /><Typography>{t('messages.loading')}</Typography></Stack>;
}
export function Failure({ error }: { error?: unknown }) {
  const { t } = useTranslation();
  const response = isAxiosError<{ code?: string; message?: string }>(error) ? error.response : undefined;
  const code = response?.data?.code;
  const key = code === 'FORBIDDEN' ? 'messages.forbidden' : code === 'VALIDATION_ERROR' ? 'messages.validation' : code === 'BUSINESS_RULE' ? 'messages.business' : code === 'INVALID_TRANSITION' || code === 'VERSION_CONFLICT' ? 'messages.conflict' : 'messages.error';
  return <Alert severity="error">{response && response.status < 500 && response.data.message ? response.data.message : t(key)}</Alert>;
}
