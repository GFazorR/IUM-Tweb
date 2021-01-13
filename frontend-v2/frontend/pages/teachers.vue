<template>
  <!-- TODO put form in modal or drop down? -->
  <b-container>
    <teacherForm @clicked="addTeacher"></teacherForm>
    <!-- Table teachers -->
    <b-table striped hover borderless :fields="fields" :items="teachers">
      <template v-slot:cell(actions)="teacher">
        <b-button @click="setTeacherDeleted(teacher.item)">
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
          sortable: true
        },
        "actions"
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
      const newTeacher = await this.$axios.post("Teachers", null, {
        params: {
          name: name,
          surname: surname
        }
      });
      console.log(newTeacher);
      this.teachers.push(newTeacher.data);
    }
  }
};
</script>

<style></style>
