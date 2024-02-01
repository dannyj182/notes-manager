<template>

  <section>
    <div>
      <div class="jumbotron mb-0 p-5">
        <div v-if="!editMode">
          <h4>{{ this.lastStatus | title }}</h4>
          <div class="d-flex justify-content-start mb-1">
            <div class="d-flex justify-content-between">
              <button type="button" class="btn btn-secondary mr-1 p-1" @click="addNote()">Add</button>
              <button type="button" class="btn btn-primary mr-1 p-1" @click="setLastStatus('active')">Home</button>
              <button type="button" class="btn btn-success mr-1 p-1" @click="setLastStatus('archived')">Archive</button>
              <button type="button" class="btn btn-warning mr-1 p-1" @click="setLastStatus('deleted')">Trash</button>
            </div>
          </div>
          <div>
            <h4>Tags</h4>
            <div class="d-flex justify-content-start">
              <button v-for="(tag, index) in tags" :key="index" type="button" class="btn btn-info mr-1 my-1 p-1" @click="filterByTagName(tag.name)">{{ tag.name }}</button>
            </div>
          </div>
          <div v-if="filteredNotes.length">
            <div class="card-group">
              <div v-for="(note, index) in filteredNotes" :key="index">
                <div class="card bg-light mt-1 mr-1" style="max-width: 18rem;">
                  <div class="card-header">
                    <div class="row justify-content-center">
                      <button v-show="lastStatus != 'active'" class="btn btn-primary mr-1 p-1" @click="updateStatus(note, 'active')">Activate</button>
                      <button v-show="lastStatus != 'archived'" class="btn btn-success mr-1 p-1" @click="updateStatus(note, 'archived')">Archive</button>
                      <button v-show="lastStatus != 'deleted'" class="btn btn-warning mr-1 p-1" @click="updateStatus(note, 'deleted')">Delete</button>
                      <button v-show="lastStatus == 'deleted'" class="btn btn-danger mr-1 p-1" @click="deleteNote(note)">Delete</button>
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
                        <button class="btn btn-danger btn-small m-0 p-1" @click="deleteTag(note, tag)">X</button>
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
          tags: [],
        },
        notes: [],
        filteredNotes: [],
        formData: this.getInitialData(),
        formState: {},
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
      async addNote(){
        try{
          const { data : note } = await this.axios.post(this.url, this.newNote, {
            headers: {
                'Authorization': `Bearer ${this.$store.state.token}`,
                'content-type' : 'application/json'
            }
          })
          this.notes.unshift(note);
          this.setLastStatus('active');
        }
        catch(e){ 
          console.error('Error in addNote', e.message)
        }
      },
      editNote(note){
        this.editMode = true;
        this.noteEdited = note;
        this.formData = {
          title: note.title,
          description: note.description,
        }
      },
      async saveNote(){
        this.editMode = false;
        this.updateNote();
        try{
          const { data : updatedNote } = await this.axios.put(this.url + this.noteEdited.noteId, this.noteEdited, {
            headers: {
                'Authorization': `Bearer ${this.$store.state.token}`,
                'content-type' : 'application/json'
            }
          })
          this.updateLists(this.noteEdited, updatedNote);
        }
        catch(e){ 
          console.error('Error in saveNote', e.message)
        }
      },
      updateNote(){
        let noteDTO = { ...this.formData }
        this.noteEdited.title = noteDTO.title;
        this.noteEdited.description = noteDTO.description;
        if(noteDTO.tag != null) this.noteEdited.tags.push(noteDTO.tag);
      },
      async updateStatus(note, status){
        this.filteredNotes.splice(this.filteredNotes.indexOf(note), 1)
        this.notes.splice(this.notes.indexOf(note), 1)
        note.status = status;
        try{
          const { data : updatedNote } = await this.axios.put(this.url + note.noteId, note, {
            headers: {
              'Authorization': `Bearer ${this.$store.state.token}`,
              'content-type' : 'application/json'
            }
          })
          this.notes.unshift(updatedNote);
        }
        catch(e){ 
          console.error('Error in updateStatus', e.message)
        }
      },
      async deleteNote(note){
        this.filteredNotes.splice(this.filteredNotes.indexOf(note), 1)
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
      },
      async deleteTag(note, tag){
        note.tags.splice(note.tags.indexOf(tag), 1)
        try{
          const { data : updatedNote } = await this.axios.put(this.url + note.noteId, note, {
            headers: {
                'Authorization': `Bearer ${this.$store.state.token}`,
                'content-type' : 'application/json'
            }
          })
          this.updateLists(note, updatedNote);
        }
        catch(e){ 
          console.error('Error in deleteTag', e.message)
        }
      },
      updateLists(note, updatedNote){
        this.filteredNotes.splice(this.filteredNotes.indexOf(note), 1);
        this.filteredNotes.unshift(updatedNote);
        this.notes.splice(this.notes.indexOf(note), 1);
        this.notes.unshift(updatedNote);
      },
      back(){
        this.editMode = false;
      },
      getInitialData() {
        return {
          title: '',
          description: '',
        }
      },
      setLastStatus(status){
        this.lastStatus = status;
        this.updateList(this.lastStatus);
      },
      updateList(status){
        this.filteredNotes = this.notes.filter(note => note.status == status);
      },
      filterByTagName(name){
        this.filteredNotes = this.notes.filter(note => note.tags.some(tag => tag.name == name));
      },
    },
}


</script>

<style scoped lang="css">
.btn-small{
  font-size: xx-small;
}
</style>
