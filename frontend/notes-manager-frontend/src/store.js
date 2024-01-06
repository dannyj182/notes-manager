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
        url: '',
    },
    actions : {
        
    },
    mutations : {
        
    },
    plugins:[
        new VuexPersistence({
            storage: window.sessionStorage
        }).plugin
    ]
})