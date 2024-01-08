import Vue from 'vue'

Vue.filter('dateformat', function(value){
    const dateCurrent = new Date();
    const dateCompare = new Date(value);
    const validate = (
        dateCurrent.getFullYear() === dateCompare.getFullYear() &&
        dateCurrent.getMonth() === dateCompare.getMonth() &&
        dateCurrent.getDate() === dateCompare.getDate()
      );
    if(validate) {
        let hora = dateCompare.getHours()
        let minutes = dateCompare.getMinutes()
        if(hora < 10) hora = `0${hora}`
        if(minutes < 10) minutes = `0${minutes}`
        return `${hora}:${minutes}`
    }
    else {
        let mes = dateCompare.getMonth()+1
        let dia = dateCompare.getDate()
        if(mes < 10) mes = `0${mes}`
        if(dia < 10) dia = `0${dia}`
        return `${dia}-${mes}-${dateCompare.getFullYear()}`
    }
})
Vue.filter('title', function(value){
    if(value == 'active') return 'Home'
    else if(value == 'archived') return 'Archived'
    else if(value == 'deleted') return 'Trash'
    else return 'Notes'
})