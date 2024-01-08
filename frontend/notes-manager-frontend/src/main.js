import Vue from 'vue'
import App from './App.vue'

Vue.config.productionTip = false

import "./form"
import "./bootstrap"
import {router} from "./router"
import store from './store'
import './globalFilters'
import './axios'

new Vue({
  store,
  router,
  render: h => h(App),
}).$mount('#app')