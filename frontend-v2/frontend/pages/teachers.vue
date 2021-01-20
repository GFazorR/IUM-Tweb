<template>
  <!-- TODO put form in modal or drop down? -->
  <b-container>
    <b-button class="mb-4" variant="success" v-b-modal.modal>
      Aggiungi professore
    </b-button>
    <b-modal id="modal" title="Aggiungi un professore" hide-footer centered>
      <teacherForm @clicked="addTeacher"></teacherForm>
    </b-modal>
    <!-- Table teachers -->
    <b-table hover :fields="fields" :items="teachers">
      <template v-slot:cell(actions)="teacher">
        <b-button variant="danger" @click="setTeacherDeleted(teacher.item)">
          Elimina
        </b-button>
      </template>
    </b-table>
  </b-container>
</template>

<script>
import { mapGetters } from "vuex";
import teacherForm from "../components/teacher-form.vue";

export default {
  components: { teacherForm },
  layout: "logged",
  middleware: "admin",
  data() {
    return {
      fields: [
        {
          key: "name",
          label: "Professore",
          sortable: false
        },
        {
          key: "actions",
          sortable: false,
          class: "text-center",
          style: "width=200px"
        }
      ]
    };
  },
  computed: {
    ...mapGetters(["isAdmin, user"])
  },
  //gets teacher pre-rendering
  async asyncData({ $axios }) {
    const teachers = (await $axios.get("Teachers")).data;
    return { teachers };
  },
  methods: {
    //delete teacher request and update data
    async setTeacherDeleted(t) {
      const id = t.id;
      const { data } = await this.$axios.delete("Teachers", {
        params: {
          id: id
        }
      });
      this.teachers = this.teachers.filter(t => t.id != id);
    },
    //add Teacher request and update data
    async addTeacher(name, surname) {
      if (name != "" && surname != "") {
        const newTeacher = await this.$axios.post("Teachers", null, {
          params: {
            name: name,
            surname: surname
          }
        });
        this.teachers.push(newTeacher.data);
        name = "";
        surname = "";
        this.$bvModal.hide("modal");
      }
    }
  }
};
</script>

<style></style>
