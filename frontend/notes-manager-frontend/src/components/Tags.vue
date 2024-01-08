<template>

  <section>
    <div class="jumbotron mb-0 p-5">
      <h4>Tags</h4>

      <vue-form :state="formState" @submit.prevent="saveTag()">
        <div class="card-group">
          <div class="card bg-light mt-1 mr-1" style="max-width: 18rem;">
            <div class="card-header">
                <div class="row justify-content-between">
                  <validate tag="div">
                    <input type="text" id="name" autocomplete="off" v-model.trim="formData.name"
                    name="name"
                    required
                    />
                  </validate>
                  <button class="btn btn-outline-secondary mr-1 p-0" :disabled="!formState.$dirty">Save</button>
                </div>
            </div>
          </div>
        </div>
      </vue-form>

      <div v-if="tags.length">
        <div class="card-group">
          <div v-for="(tag, index) in tags" :key="index">
            <div class="card bg-light mt-1 mr-1" style="max-width: 18rem;">
              <div class="card-header">
                {{ tag.name }}
              </div>
              <div class="card-header">
                <button class="btn btn-outline-secondary mr-1 p-0" @click="deleteTag(tag)">Delete</button>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

</template>

<script>

  export default  {
    name: 'src-components-tags',
    props: [],
    mounted () {
      this.goIndex()
      this.getTags()
    },
    data () {
      return {
        url: this.$store.state.url + 'tags/',
        tags: [],
        tagName: '',
        formData: this.getInitialData(),
        formState: {},
      }
    },
    methods: {
      goIndex(){
        if(!this.$store.state.isLogin) this.$router.push('/')
      },
      async getTags(){
        if(this.$store.state.isLogin){
          try{
            const { data : tags } = await this.axios(this.url, {
              headers: {
                  'Authorization': `Bearer ${this.$store.state.token}`
              }
          })
            this.tags = tags;
          } catch (e) {
            console.error('Error in getTags', e.message);
          }
        }
      },
      async deleteTag(tag){
        try{
          await this.axios.delete(this.url + tag.name, {
            headers: {
              'Authorization': `Bearer ${this.$store.state.token}`,
              'content-type' : 'application/json'
            }
          })
        }
        catch(e){ 
          console.error('Error in deleteTag', e.message)
        }
        this.tags.splice(this.tags.indexOf(tag), 1)
      },async saveTag(){
        const newTag = { ...this.formData }
        this.formData = this.getInitialData();
        if(this.validateInput(newTag)) return
        try{
          const { data : tag } = await this.axios.post(this.url, newTag, {
            headers: {
                'Authorization': `Bearer ${this.$store.state.token}`,
                'content-type' : 'application/json'
            }
          })
          this.tags.unshift(tag);
        }
        catch(e){ 
          console.error('Error in saveNote', e.message)
        }
      },
      getInitialData() {
        return {
          name: '',
        }
      },
      validateInput(newTag) {
        const validation = this.tags.find(tag => tag.name === newTag.name)
        if(validation) return true
        else return false
      },
    },
    computed: {

    }
}


</script>

<style scoped lang="css">

</style>
