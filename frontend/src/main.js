import Vue from 'vue'
import App from './App.vue'
import router from './router'
import bootstrap from 'bootstrap-vue'
import vuelidate from 'vuelidate'
import {store} from './store'
import {funcs} from './funcs.js'
import {ax} from './axios.js'
import './axios'

import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'

Vue.config.productionTip = false

export const bus = new Vue();

Vue.use(bootstrap)
Vue.use(vuelidate)

Vue.prototype.$funcs = funcs
Vue.prototype.$ax = ax



new Vue({
  store,
  funcs,
  ax,
  router,
  render: h => h(App),
}).$mount('#app')
