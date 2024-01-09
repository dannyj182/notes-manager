import Vue from 'vue'
import Vuex from 'vuex'
import VuexPersistence from 'vuex-persist'
import axios from 'axios'
import VueAxios from 'vue-axios'

Vue.use(VueAxios,axios)
Vue.use(Vuex)

export default new Vuex.Store({
    state : {
        isLogin : false,
        token: '',
        username: '',
        responseLogin: '',
        url: '/',
    },
    actions : {
        desloguearse({commit}){
            commit('desloguearse')   
        },
        loguearse({commit}, res) {
            commit('loguearse', res)  
        },
    },
    mutations : {
        desloguearse(state) {
            state.isLogin = false
            state.token = ''
            state.username = ''
        },
        loguearse(state, responseLogin) {
            state.isLogin = true
            state.token = responseLogin.headers.authorization
            state.username = JSON.parse(responseLogin.config.data).username
        },
    },
    plugins:[
        new VuexPersistence({
            storage: window.sessionStorage
        }).plugin
    ]
})