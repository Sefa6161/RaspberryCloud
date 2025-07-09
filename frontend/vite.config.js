import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  appType: 'spa',
  server: {
    proxy: {
      '/login': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        bypass: (req) => {
          if (req.method === 'GET') {
            return '/index.html';
          }
        },
      },
      '/logout': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      },
    },
  },
})
