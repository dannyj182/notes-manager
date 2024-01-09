<template>

  <section>
    <div>
      <div class="jumbotron mb-0 p-5">
        <div v-if="!editMode">
          <h4>{{ this.lastStatus | title }}</h4>
          <div class="d-flex justify-content-start">
            <div class="d-flex justify-content-between">
              <button type="button" class="btn btn-secondary mr-1 p-1" @click="addNote()">Add</button>
              <button type="button" class="btn btn-primary mr-1 p-1" @click="setLastStatus('active')">Home</button>
              <button type="button" class="btn btn-success mr-1 p-1" @click="setLastStatus('archived')">Archive</button>
              <button type="button" class="btn btn-warning mr-1 p-1" @click="setLastStatus('deleted')">Trash</button>
            </div>
          </div>
          <div class="d-flex justify-content-start">
            <button v-for="(tag, index) in tags" :key="index" type="button" class="btn btn-info mr-1 my-1 p-1" @click="filterByTagName(tag.name)">{{ tag.name }}</button>
            </div>
          <div v-if="filteredNotes.length">
            <div class="card-group">
              <div v-for="(note, index) in filteredNotes" :key="index">
                <div class="card bg-light mt-1 mr-1" style="max-width: 18rem;">
                  <div class="card-header">
                    <div class="row justify-content-center">
                      <button v-show="lastStatus != 'active'" class="btn btn-primary mr-1 p-1" @click="changeStatus(note, 'active')">Activate</button>
                      <button v-show="lastStatus != 'archived'" class="btn btn-success mr-1 p-1" @click="changeStatus(note, 'archived')">Archive</button>
                      <button v-show="lastStatus != 'deleted'" class="btn btn-warning mr-1 p-1" @click="changeStatus(note, 'deleted')">Delete</button>
                      <button v-show="lastStatus == 'deleted'" class="btn btn-danger mr-1 p-1" @click="deleteNote(note, 'deleted')">Delete</button>
                    </div>
                  </div>
                  <div class="card-header" @click="editNote(note)">
                    <div class="row justify-content-start">
                      <div class="ml-1">{{ note.title }}</div>
                    </div>
                  </div>
                  <div class="card-body" @click="editNote(note)">
                    <p class="card-text">{{ note.description }}</p>
                  </div>
                  <div class="card-body">
                    <div v-for="(tag, index) in note.tags" :key="index">
                      <div class="d-flex justify-content-between border border-dark">
                        <div class="mr-1">{{ tag.name }}</div>
                        <button class="btn btn-danger m-0 p-1" @click="deleteTag(note, tag)">X</button>
                      </div>
                    </div>
                  </div>
                  <div class="card-footer" @click="editNote(note)">
                    <small class="text-muted">Modified: {{ note.modificationDate | dateformat }}</small>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div v-else>
            <div class="alert alert-dark text-center mt-2 mb-0 d-inline-flex">The notes you add will appear here</div>
          </div>
        </div>
        <div v-else>
          <button type="button" class="btn btn-info mr-1 p-1" @click="back()">Back</button>
          <vue-form :state="formState" @submit.prevent="saveNote()">
            <div class="card-group">
              <div class="card bg-light mt-1 mr-1" style="max-width: 18rem;">
                <div class="card-header">
                    <div class="row justify-content-between">
                      <validate tag="div">
                        <input type="text" id="title" autocomplete="off" v-model.trim="formData.title"
                        name="title"
                        required
                        />
                      </validate>
                      <button class="btn btn-success mr-1 p-1" :disabled="!formState.$dirty">Save</button>
                    </div>
                </div>
                <div class="card-body">
                  <validate tag="p">
                    <input type="text" id="description" autocomplete="off" v-model.trim="formData.description"
                    name="description"
                    size="30"
                    required
                    />
                  </validate>
                </div>
                <div class="card-body">
                  <validate tag="div">
                    <label for="tag">Tag</label>
                    <select type="text" id="tag" class="form-control" v-model="formData.tag"
                    name="tag"
                    required
                    >
                      <option disabled>Select a tag</option>
                      <option v-for="(tag, index) in tags" :key="index" :value="tag">{{ tag.name }}</option>
                    </select>
                  </validate>
                </div>
              </div>
            </div>
          </vue-form>
        </div>
      </div>
    </div>
  </section>

</template>

<script lang="js">

  export default  {
    name: 'src-components-notes',
    props: [],
    mounted () {
      this.goIndex()
      this.getNotes()
      this.getTags()
    },
    data () {
      return {
        url: this.$store.state.url + 'notes/',
        urlTag: this.$store.state.url + 'tags/',
        editMode: false,
        newNote: {
          title: '',
          description: '',
          status: 'active',
          modificationDate: new Date().toISOString(),
        },
        notes: [],
        filteredNotes: [],
        formData: this.getInitialData(),
        formState: {},
        editedId: '',
        lastStatus: 'active',
        tags: [],
        noteEdited: {},
      }
    },
    methods: {
      goIndex(){
        if(!this.$store.state.isLogin) this.$router.push('/')
      },
      async getNotes(){
        if(this.$store.state.isLogin){
          try{
            const { data : notes } = await this.axios(this.url, {
              headers: {
                  'Authorization': `Bearer ${this.$store.state.token}`
              }
          })
            this.notes = notes;
            this.updateList(this.lastStatus);
          } catch (e) {
            console.error('Error in getNotes', e.message);
          }
        }
      },
      async addNote(){
        try{
          const { data : note } = await this.axios.post(this.url, this.newNote, {
            headers: {
                'Authorization': `Bearer ${this.$store.state.token}`,
                'content-type' : 'application/json'
            }
          })
          this.notes.unshift(note);
          this.updateList(this.lastStatus);
        }
        catch(e){ 
          console.error('Error in addNote', e.message)
        }
      },
      editNote(note){
        this.editMode = true;
        this.editedId = note.noteId;
        this.noteEdited = note;
        this.formData = {
          title: note.title,
          description: note.description,
        }
      },
      async saveNote(){
        this.editMode = false;
        const noteDTO = { ...this.formData }
        this.noteEdited.title = noteDTO.title;
        this.noteEdited.description = noteDTO.description;
        this.noteEdited.tags.push(noteDTO.tag);
        try{
          const { data : note } = await this.axios.put(this.url + this.editedId, this.noteEdited, {
            headers: {
                'Authorization': `Bearer ${this.$store.state.token}`,
                'content-type' : 'application/json'
            }
          })
          this.filterList(note.noteId);
          this.notes.unshift(note);
          this.updateList(this.lastStatus);
          this.editedId = '';
        }
        catch(e){ 
          console.error('Error in saveNote', e.message)
        }
      },
      back(){
        this.editMode = false;
      },
      async changeStatus(note, status){
        this.filterList(note.noteId);
        note.status = status;
        try{
          const { data : noteEdited } = await this.axios.put(this.url + note.noteId, note, {
            headers: {
              'Authorization': `Bearer ${this.$store.state.token}`,
              'content-type' : 'application/json'
            }
          })
          this.notes.unshift(noteEdited);
          this.updateList(this.lastStatus);
        }
        catch(e){ 
          console.error('Error in changeStatus', e.message)
        }
      },
      async deleteNote(note){
        this.filterList(note.noteId);
        try{
          await this.axios.delete(this.url + note.noteId, {
            headers: {
              'Authorization': `Bearer ${this.$store.state.token}`,
              'content-type' : 'application/json'
            }
          })
        }
        catch(e){ 
          console.error('Error in deleteNote', e.message)
        }
        this.updateList(this.lastStatus);
      },
      getInitialData() {
        return {
          title: '',
          description: '',
        }
      },
      updateList(status){
        this.filteredNotes = this.notes.filter(note => note.status == status);
      },
      setLastStatus(status){
        this.lastStatus = status;
        this.updateList(status);
      },
      filterList(noteId){
        this.filteredNotes = this.notes.filter(note => note.noteId != noteId);
        this.notes = this.notes.filter(note => note.noteId != noteId);
      },
      async getTags(){
        if(this.$store.state.isLogin){
          try{
            const { data : tags } = await this.axios(this.urlTag, {
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
      async deleteTag(note, tag){
        note.tags.splice(note.tags.indexOf(tag), 1)
        try{
          const { data : noteEdited } = await this.axios.put(this.url + note.noteId, note, {
            headers: {
                'Authorization': `Bearer ${this.$store.state.token}`,
                'content-type' : 'application/json'
            }
          })
          this.filterList(noteEdited.noteId);
          this.filteredNotes.unshift(noteEdited);
        }
        catch(e){ 
          console.error('Error in saveNote', e.message)
        }
      },
      filterByTagName(name){
        this.filteredNotes = this.notes.filter(note => note.tags.some(tag => tag.name == name));
      },
    },
}


</script>

<style scoped lang="css">

</style>
