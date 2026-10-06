import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({mode})=>{
  const apiTarget=loadEnv(mode,'.','SIGE_').SIGE_API_TARGET??'http://localhost:8080';
  return {
  plugins: [react()],
  server: { port: 5173, proxy: { '/api': apiTarget, '/actuator': apiTarget } },
  build: {
    rollupOptions: {
      output: {
        manualChunks: {
          charts: ['@mui/x-charts'],
          kanban: ['@caldwell619/react-kanban', '@hello-pangea/dnd'],
          react: ['react', 'react-dom', 'react-router-dom'],
          mui: ['@mui/material', '@mui/icons-material', '@emotion/react', '@emotion/styled'],
          query: ['@tanstack/react-query', 'axios', 'i18next', 'react-i18next'],
        },
      },
    },
  },
};
});
