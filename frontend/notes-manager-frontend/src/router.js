import Vue from "vue";
import VueRouter from "vue-router"

import Index from './components/Index.vue'
import Login from './components/Login.vue'
import Notes from './components/Notes.vue'
import NotFound from './components/NotFound.vue'

Vue.use(VueRouter)

export const router = new VueRouter({
    mode: 'history',
    routes: [
        {path: '/', redirect: '/index'},
        {path: '/index' , component: Index },
        {path: '/login' , component: Login },
        {path: '/notes' , component: Notes },
        {path: '/:pathMatch(.*)*', name: 'NotFound', component: NotFound },
    ]
})