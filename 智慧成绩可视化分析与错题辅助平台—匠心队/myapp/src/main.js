// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import FastClick from 'fastclick'
import VueRouter from 'vue-router'
import App from './App'
import Home from './components/tabbar.vue'
import router from './router/index.js'
import store from './store'
import axios from 'axios';
import {
    LoadingPlugin
} from 'vux'
import BootstrapVue from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import {
    ToastPlugin
} from 'vux'
import VCharts from 'v-charts'
Vue.use(VCharts)
Vue.use(ToastPlugin)
Vue.use(VueRouter)
Vue.use(LoadingPlugin)
Vue.use(BootstrapVue)
Vue.prototype.$axios = axios.create({
    baseURL: 'http://125.124.143.31:8888'
})
// const routes = [{
//     path: '/',
//     component: Home
// }]

// const router = new VueRouter({
//     routes
// })

FastClick.attach(document.body)

Vue.config.productionTip = false

/* eslint-disable no-new */
new Vue({
    router,
    store,
    render: h => h(App)
}).$mount('#app-box')
