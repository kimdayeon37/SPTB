import { createApp } from 'vue'
import { Quasar, Notify, Dialog } from 'quasar'
import { createPinia } from 'pinia'
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

import '@quasar/extras/roboto-font/roboto-font.css'
import '@quasar/extras/material-icons/material-icons.css'

// Import Quasar css
import 'quasar/src/css/index.sass'

// Assumes your root component is App.vue
// and placed in same folder as main.js
import App from './App.vue'
import router from './router'

const myApp = createApp(App)

myApp.use(Quasar, {
  plugins: { Notify, Dialog }, // import Quasar plugins and add here
})
const pinia = createPinia()
myApp.use(pinia)
pinia.use(piniaPluginPersistedstate)

myApp.use(router)
// Assumes you have a <div id="app"></div> in your index.html
myApp.mount('#app')
