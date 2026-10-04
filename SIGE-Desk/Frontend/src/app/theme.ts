import { createTheme } from '@mui/material';

export const theme = createTheme({
  palette: {
    primary: { main: '#1378a7', dark: '#10293f' },
    secondary: { main: '#0b826d' },
    background: { default: '#f3f7f9', paper: '#ffffff' },
    text: { primary: '#10293f', secondary: '#667986' },
    divider: '#d9e3e8'
  },
  shape: { borderRadius: 7 },
  typography: {
    fontFamily: 'Inter, ui-sans-serif, system-ui, -apple-system, "Segoe UI", sans-serif',
    fontSize: 14,
    h4: { fontSize: 26, fontWeight: 800, lineHeight: 1.2, letterSpacing: '-0.03em' },
    h6: { fontSize: 15, fontWeight: 800 },
    body1: { fontSize: 14 }, body2: { fontSize: 13 }, caption: { fontSize: 12 }
  },
  components: {
    MuiCard: { defaultProps: { variant: 'outlined' }, styleOverrides: { root: { boxShadow: '0 1px 2px rgba(16,41,63,0.03)', borderRadius: 14 } } },
    MuiPaper: { styleOverrides: { outlined: { boxShadow: '0 1px 2px rgba(16,41,63,0.03)' } } },
    MuiButton: { defaultProps: { disableElevation: true }, styleOverrides: { root: { textTransform: 'none', fontWeight: 700, fontSize: 13, borderRadius: 9, whiteSpace: 'nowrap' }, contained: { backgroundColor: '#10293f', '&:hover': { backgroundColor: '#1a4968' } }, outlined: { borderColor: '#d9e3e8', color: '#10293f', backgroundColor: 'white', '&:hover': { borderColor: '#9db2be', backgroundColor: '#f8fbfc' } } } },
    MuiTextField: { defaultProps: { size: 'small' } },
    MuiFormControl: { defaultProps: { size: 'small' } },
    MuiOutlinedInput: { styleOverrides: { root: { borderRadius: 9, backgroundColor: 'white' } } },
    MuiTableCell: { styleOverrides: { head: { backgroundColor: '#f7fafb', color: '#5c6f7a', fontSize: 11, textTransform: 'uppercase', letterSpacing: '0.04em', fontWeight: 700, padding: '11px 15px' }, body: { fontSize: 13, padding: '13px 15px', borderColor: '#e7eef1' } } },
    MuiTableRow: { styleOverrides: { root: { '&:last-child td': { borderBottom: 0 } } } },
    MuiToggleButton: { styleOverrides: { root: { textTransform: 'none', fontSize: 12, fontWeight: 700, borderColor: '#d9e3e8', '&.Mui-selected': { bgcolor: '#e7f5fb', color: '#10293f' } } } },
    MuiTab: { styleOverrides: { root: { textTransform: 'none', fontSize: 13, fontWeight: 700, minWidth: 80 } } },
    MuiSwitch: { styleOverrides: {
      root: { width: 38, height: 22, padding: 0 },
      sizeSmall: { width: 38, height: 22, padding: 0 },
      switchBase: { padding: 3, color: 'white', '&.Mui-checked': { transform: 'translateX(16px)', color: 'white', '& + .MuiSwitch-track': { opacity: 1, backgroundColor: '#0b826d' } }, '&.Mui-disabled + .MuiSwitch-track': { opacity: 0.4 } },
      thumb: { width: 16, height: 16, boxShadow: '0 1px 2px rgba(0,0,0,0.12)' },
      track: { borderRadius: 11, opacity: 1, backgroundColor: '#bbc7cd' }
    } }
  }
});
