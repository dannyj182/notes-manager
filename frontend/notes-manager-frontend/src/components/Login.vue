<template>

<section>
    <div class="jumbotron mb-0 p-5">
      <div v-show="!$store.state.isLogin && !showMessage" >
        <h4>Login</h4>
        <vue-form :state="formState" @submit.prevent="login()">

          <validate tag="div">
            <label class="mt-2" for="username">Username</label>
            <input type="text" id="username" autocomplete="off" v-model.trim="formData.username"
            name="username" :class="fieldClassName(formData.username, formState.username)"
            required
            :minlength="5"
            placeholder="Please enter your user name" />
            <field-messages name="username" show="$touched || $submitted">
              <div slot="required" class="text-danger pl-1">Required field</div>
            </field-messages>
          </validate>

          <validate tag="div">
            <label class="mt-2" for="password">Password</label>
            <input type="password" id="password" autocomplete="off" v-model.trim="formData.password"
            name="password" :class="fieldClassName(formData.password, formState.password)"
            required
            :minlength="8"
            placeholder="Please enter the password" />
            <field-messages name="password" show="$touched || $submitted">
              <div slot="required" class="text-danger pl-1">Required field</div>
            </field-messages>
          </validate>

          <button class="btn btn-outline-dark mt-3" :disabled="formState.$invalid">Login</button>
          <hr>
        </vue-form>
      </div>
      <div v-show="showMessage">
        <div>
          {{messageLogin}}
        </div>
        <button class="btn btn-outline-dark mt-3" @click="acceptButton()">Accept</button>
      </div>
    </div>
  </section>

</template>

<script>

  export default  {
    name: 'src-components-login',
    mounted () {
      this.goIndex()
    },
    data () {
      return {
        url: this.$store.state.url + 'auth/login',
        formState: {},
        formData: this.getInitialData(),
        messageLogin: '',
        showMessage: false
      }
    },
    methods: {
      goIndex(){
        if(this.$store.state.isLogin){
          this.$router.push('/')
        }
      },
      getInitialData() {
        return {
          username: null,
          password: null,
        }
      },
      async login(){
        const user = this.createUser({ ...this.formData})
        const res = await this.postLogin(user)
        if(res.status == 200){
          this.$store.dispatch('loguearse', res)
          this.messageLogin = 'Login successful'
        }
        else{
          this.messageLogin = 'Login failed'
        }
        this.clearForm()
        this.showMessage = true
      },
      createUser(user){
        return {
          username: user.username.toLowerCase(),
          password: user.password,
        }
      },
      async postLogin(user){
        try{
          return await this.axios.post(this.url, user, { 'content-type' : 'application/json' })
        }
        catch(err){ 
          console.error('Error in postLogin', err.message)
          return false
        }
      },
      clearForm(){
        this.formData = this.getInitialData()
        this.formState._reset()
      },
      acceptButton(){
        this.clearForm()
        if(this.$store.state.isLogin) this.$router.push('/')
        else {
          this.showMessage = false
        }
      },
      fieldClassName(formData, formState){
        if( formState && ( formState.$touched || formState.$dirty ) && ( !formData || formState.$invalid ) ) {
          return 'form-control is-invalid'
        }else if( !formData ) {
          return 'form-control'
        }else if( ( formState.$touched || formState.$dirty ) && formState.$valid ) {
          return 'form-control is-valid'
        }
      }
    }
}


</script>

<style scoped lang="css">

</style>
