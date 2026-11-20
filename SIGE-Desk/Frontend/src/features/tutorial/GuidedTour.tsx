import { useEffect, useId, useLayoutEffect, useMemo, useRef, useState } from 'react';
import { Box, Button, IconButton, LinearProgress, Modal, Paper, Stack, Typography } from '@mui/material';
import Close from '@mui/icons-material/Close';
import ArrowBack from '@mui/icons-material/ArrowBack';
import ArrowForward from '@mui/icons-material/ArrowForward';
import { useTranslation } from 'react-i18next';
import { useLocation } from 'react-router-dom';
import type { Role } from '../../shared/api/types';
import { placeTourCard, tourSteps } from './tourModel';
import type { TourRect } from './tourModel';

const findTarget = (name: string) => document.querySelector<HTMLElement>(`[data-tour="${name}"]`);
const visible = (name: string) => {
  const element = findTarget(name);
  return Boolean(element && element.getClientRects().length && element.offsetWidth && element.offsetHeight);
};

export function GuidedTour({ role, compact, onClose }: { role: Role; compact: boolean; onClose: () => void }) {
  const { t } = useTranslation();
  const { pathname } = useLocation();
  const id = useId();
  const candidates = useMemo(() => tourSteps(pathname, role, compact), [pathname, role, compact]);
  const available = candidates.filter(item => visible(item.target));
  const steps = available.some(item => !['navigation', 'mobile-navigation'].includes(item.target))
    ? available
    : [{ target: 'page-content', message: 'fallback' }, ...available];
  const [index, setIndex] = useState(0);
  const current = Math.min(index, Math.max(steps.length - 1, 0));
  const active = steps[current];
  const card = useRef<HTMLDivElement>(null);
  const [rect, setRect] = useState<TourRect | null>(null);
  const [position, setPosition] = useState({ left: 12, top: 76 });
  const targetName = active?.target;

  useLayoutEffect(() => {
    const target = targetName ? findTarget(targetName) : null;
    target?.scrollIntoView({ block: 'center', inline: 'nearest', behavior: 'instant' });
    card.current?.focus({ preventScroll: true });
    let frame = 0;
    const measure = () => {
      const bounds = target?.getBoundingClientRect();
      const top = Math.max(8, (bounds?.top ?? 8) - 6);
      const left = Math.max(8, (bounds?.left ?? 8) - 6);
      const highlight = bounds ? {
        left, top,
        width: Math.max(0, Math.min(window.innerWidth - 8, bounds.right + 6) - left),
        height: Math.max(0, Math.min(window.innerHeight - 8, bounds.bottom + 6) - top)
      } : null;
      setRect(highlight);
      setPosition(placeTourCard(highlight, { width: window.innerWidth, height: window.innerHeight }, {
        width: card.current?.offsetWidth ?? 360, height: card.current?.offsetHeight ?? 280
      }));
    };
    const schedule = () => { cancelAnimationFrame(frame); frame = requestAnimationFrame(measure); };
    const observer = new ResizeObserver(schedule);
    if (target) observer.observe(target);
    if (card.current) observer.observe(card.current);
    window.addEventListener('resize', schedule);
    window.addEventListener('scroll', schedule, true);
    measure();
    return () => {
      cancelAnimationFrame(frame);
      observer.disconnect();
      window.removeEventListener('resize', schedule);
      window.removeEventListener('scroll', schedule, true);
    };
  }, [targetName]);

  useEffect(() => () => {
    // The mobile trigger lives in a closed drawer; return to its menu button.
    if (compact) requestAnimationFrame(() => findTarget('mobile-navigation')?.focus());
  }, [compact]);

  const next = () => current === steps.length - 1 || !steps.length ? onClose() : setIndex(current + 1);
  const previous = () => setIndex(Math.max(0, current - 1));
  return <Modal open hideBackdrop onClose={onClose} disableRestoreFocus={compact} aria-labelledby={`${id}-title`} aria-describedby={`${id}-text`}>
    <Box sx={{ position: 'fixed', inset: 0 }} onKeyDown={event => {
      if (event.key === 'ArrowRight') { event.preventDefault(); next(); }
      if (event.key === 'ArrowLeft') { event.preventDefault(); previous(); }
    }}>
      <Box component="svg" aria-hidden="true" width="100%" height="100%" sx={{ position: 'absolute', inset: 0, pointerEvents: 'none' }}>
        <defs><mask id={`${id}-mask`}><rect width="100%" height="100%" fill="white" />{rect && <rect {...{ x: rect.left, y: rect.top, width: rect.width, height: rect.height }} rx="10" fill="black" />}</mask></defs>
        <rect width="100%" height="100%" fill="rgba(10, 29, 44, 0.72)" mask={`url(#${id}-mask)`} />
        {rect && <rect x={rect.left} y={rect.top} width={rect.width} height={rect.height} rx="10" fill="none" stroke="white" strokeWidth="2" />}
      </Box>
      <Paper ref={card} role="dialog" aria-modal="true" aria-labelledby={`${id}-title`} aria-describedby={`${id}-text`} tabIndex={-1} elevation={12} sx={{ position: 'absolute', ...position, width: 'min(360px, calc(100vw - 24px))', maxHeight: 'calc(100dvh - 24px)', overflowY: 'auto', p: 2.5, borderRadius: 2, outline: 'none' }}>
        <Stack direction="row" alignItems="center" justifyContent="space-between" gap={1}>
          <Typography variant="caption" color="primary" fontWeight={800}>{t('tutorial.progress', { current: current + 1, total: Math.max(1, steps.length) })}</Typography>
          <IconButton size="small" aria-label={t('tutorial.close')} onClick={onClose}><Close fontSize="small" /></IconButton>
        </Stack>
        <LinearProgress variant="determinate" value={(current + 1) / Math.max(1, steps.length) * 100} sx={{ mt: 1, mb: 2, borderRadius: 1 }} />
        <Box aria-live="polite" aria-atomic="true">
          <Typography id={`${id}-title`} variant="h6" component="h2">{t(`tutorial.steps.${active?.message ?? 'fallback'}.title`)}</Typography>
          <Typography id={`${id}-text`} variant="body2" color="text.secondary" sx={{ mt: 1, lineHeight: 1.7 }}>{t(`tutorial.steps.${active?.message ?? 'fallback'}.text`, { role: t(`role.${role}`) })}</Typography>
        </Box>
        <Stack direction="row" justifyContent="space-between" gap={1} sx={{ mt: 2.5 }}>
          <Button startIcon={<ArrowBack />} disabled={current === 0} onClick={previous}>{t('tutorial.previous')}</Button>
          <Button variant="contained" endIcon={<ArrowForward />} onClick={next}>{t(current === steps.length - 1 || !steps.length ? 'tutorial.finish' : 'tutorial.next')}</Button>
        </Stack>
      </Paper>
    </Box>
  </Modal>;
}
