import { quasar, transformAssetUrls } from '@quasar/vite-plugin'
import vue from '@vitejs/plugin-vue'
import { URL, fileURLToPath } from 'node:url'
import { defineConfig } from 'vite'
import compress from 'vite-plugin-compression'

export default defineConfig({
  plugins: [
    vue({
      template: {
        transformAssetUrls,
      },
    }),
    quasar({
      sassVariables: 'src/quasar-variables.sass',
    }),
    compress({ threshold: 0, algorithm: 'gzip' }),
    compress({ threshold: 0, algorithm: 'brotliCompress' }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  server: {
    // https: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
        configure: (proxy) => {
          proxy.on('proxyReq', (proxyReq, req, res) => {
            res.on('close', () => {
              if (!res.writableEnded) proxyReq.destroy()
            })
          })
        },
      },
    },
  },
})
