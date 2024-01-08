<template>

  <section class="sticky-top">
    <nav class="navbar navbar-expand navbar-dark bg-dark">
      <router-link to="/index">
        <a class="navbar-brand" href="#">Home</a>
      </router-link>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarNavAltMarkup" aria-controls="navbarNavAltMarkup" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarNavAltMarkup">
        <ul class="navbar-nav text-center " v-if="!$store.state.isLogin">
          <li class="nav-item ">
            <router-link to="/login">
              <a class="nav-link text-light" href="#">Login</a>
            </router-link>
          </li>
        </ul>
        <ul class="navbar-nav" v-else>
          <li class="nav-item">
            <router-link to="/notes">
              <a class="nav-link" href="#">Notes</a>
            </router-link>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" 
            href="#" 
            id="navbarDropdownMenuLink" 
            data-toggle="dropdown" 
            aria-haspopup="true" 
            aria-expanded="false">
            {{ this.$store.state.username }}
          </a>
          <div class="dropdown-menu dropdown-menu-right" aria-labelledby="navbarDropdownMenuLink">
              <a class="dropdown-item" href="#" @click=desloguearse()>Cerrar Sesión</a>
            </div>
          </li>
        </ul>
      </div>
    </nav>
  </section>
  
</template>

<script>

  export default  {
    name: 'src-components-navbar',
    methods: {
      desloguearse(){
        if(this.$router.currentRoute.path !== '/index') this.$router.push('/')
        this.$store.dispatch('desloguearse')
      },
    }
}


</script>

<style scoped lang="scss">

</style>
