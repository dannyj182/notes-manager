<template>

  <section>
    <div>
      <div class="jumbotron mb-0">
        <div v-if="!editMode">
          <button type="button" class="btn btn-outline-primary" @click="addNote()">Add</button>
          <div v-if="notes.length">
            <div class="card-group">
              <div v-for="(note, index) in notes" :key="index">
                <div class="card bg-light mt-1 mr-1" style="max-width: 18rem;">
                  <div class="card-header">
                    <div class="row justify-content-between">
                      <div class="ml-1" @click="editNote(note)">{{ note.title }}</div>
                      <button class="btn btn-outline-secondary mr-1 p-0" @click="deleteNote(index)">Delete</button>
                    </div>
                  </div>
                  <div class="card-body" @click="editNote(note)">
                    <p class="card-text">{{ note.description }}</p>
                  </div>
                  <div class="card-footer" @click="editNote(note)">
                    <small class="text-muted">Modified: {{ note.modified }}</small>
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
          <button type="button" class="btn btn-outline-primary" @click="back()">Back</button>
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
                      <button class="btn btn-outline-secondary mr-1 p-0" :disabled="!formState.$dirty">Save</button>
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

    },
    data () {
      return {
        editMode: false,
        newNote: {
          title: '',
          description: '',
          status: '',
          modified: ''
        },
        notes: [
          {
            title: 'Note 1',
            description: 'Description 1',
            status: 'active',
            modified: '2020-01-01',
          },
          {
            title: 'Note 2',
            description: 'Description 2',
            status: 'active',
            modified: '2020-01-01',
          },
          {
            title: 'Note 3',
            description: 'Description 3',
            status: 'active',
            modified: '2020-01-01',
          },
          {
            title: 'Note 4',
            description: 'Description 4',
            status: 'active',
            modified: '2020-01-01',
          },
          {
            title: 'Note 5',
            description: 'Description 5',
            status: 'active',
            modified: '2020-01-01',
          },
          {
            title: 'Note 6',
            description: 'Description 6',
            status: 'active',
            modified: '2020-01-01',
          },
          {
            title: 'Note 7',
            description: 'Description 7',
            status: 'active',
            modified: '2020-01-01',
          },
          {
            title: 'Note 8',
            description: 'Description 8',
            status: 'active',
            modified: '2020-01-01',
          },
          {
            title: 'Note 9',
            description: 'Description 9',
            status: 'active',
            modified: '2020-01-01',
          },
          {
            title: 'Note 10',
            description: 'Description 10',
            status: 'active',
            modified: '2020-01-01',
          },
        ],
        formData: this.getInitialData(),
        formState: {},
      }
    },
    methods: {
      addNote(){
        this.notes.unshift(this.newNote);
      },
      editNote(note){
        this.editMode = true;
        this.formData = {
          title: note.title,
          description: note.description,
        }
      },
      saveNote(){
        this.editMode = false;
      },
      back(){
        this.editMode = false;
      },
      deleteNote(index){
        this.notes.splice(index, 1);
      },
      getInitialData() {
        return {
          title: '',
          description: '',
        }
      },
    },
    computed: {

    }
}


</script>

<style scoped lang="css">

</style>
