<template>
  <div>
    <!-- TODO maybe create components -->
    <!-- Button Collapse -->
    <b-button v-b-toggle.collapse>
      Aggiungi una materia
    </b-button>
    <!-- Form Collapse -->
    <b-collapse id="collapse">
      <b-form-group label="Nome Materia:">
        <b-form-input
          id="subject-name-input"
          v-model="subjectName"
          trim
          required
        ></b-form-input>
      </b-form-group>
      <b-button @click="addSubject(subjectName)">AddSubjext</b-button>
    </b-collapse>
    <!-- /Form Collapse -->

    <!-- Subjects Table -->
    <b-container fluid class="pt-3">
      <b-table
        clickable
        small
        striped
        hover
        borderless
        responsive
        :items="subjects"
        :fields="fields"
        v-model="currentItems"
        @row-clicked="toggleDetails"
      >
        <!-- Button ToggleDetails -->
        <template v-slot:cell(actions)="{ item }">
          <b-btn @click="toggleDetails(item)">Manage</b-btn>
        </template>
        <!-- /Button ToggleDetails -->
        <template v-slot:row-details="{ item }">
          <b-button v-b-modal.modal @click="getAvailableTeachers(item)">
            Aggiungi professori
          </b-button>
          <!-- Modal table -->
          <b-modal id="modal" title="AddTeacher">
            <b-table
              selectable
              small
              striped
              hover
              borderless
              responsive
              sticky-header
              :fields="fields"
              :items="availableTeachers"
              v-model="shownItems"
              @row-selected="rowSelectedHandler"
            >
            </b-table>
            <b-button @click="addTeacher(item)">
              Aggiungi
            </b-button>
          </b-modal>
          <!-- /Modal table -->
          <!-- Delete Subject Button -->
          <b-button @click="deleteSubject(item)">
            DeleteSubject
          </b-button>
          <!-- /Delete Subject Button -->
          <!-- Subject Teachers Table -->
          <b-table
            small
            striped
            hover
            borderless
            responsive
            sticky-header
            :fields="fields"
            :items="item.teachers"
          >
            <template v-slot:cell(actions)="teacher">
              <b-button @click="deleteTeacher(teacher, item)">
                Elimina
              </b-button>
            </template>
          </b-table>
          <!-- /Subject Teachers Table -->
        </template>
      </b-table>
    </b-container>
  </div>
</template>

<script>
export default {
  layout: "logged",
  middleware: "admin",
  data() {
    return {
      currentItems: [],
      shownItems: [],
      selectedItems: [],
      availableTeachers: [],
      fields: [
        {
          key: "name",
          sortable: false
        },
        "actions"
      ]
    };
  },
  //gets subjects pre-rendering
  async asyncData({ $axios }) {
    const subjects = (await $axios.get("Subjects")).data;
    return { subjects };
  },
  methods: {
    //delete request and update data
    async deleteTeacher(teacher, subject) {
      await this.$axios.delete("Teaching", {
        params: {
          subject: subject.id,
          teacher: teacher.item.id
        }
      });

      subject.teachers.splice(teacher.index, 1);
    },
    //toggles Subjects Table Details (Subject Teachers Table + buttons)
    toggleDetails(row) {
      if (row._showDetails) {
        this.$set(row, "_showDetails", false);
      } else {
        this.teachersShown = row.teachers;
        this.currentItems.forEach(item => {
          this.$set(item, "_showDetails", false);
        });
        this.$nextTick(() => {
          this.$set(row, "_showDetails", true);
        });
      }
    },
    //add subject request and update data
    async addSubject(subject) {
      const newSubject = await this.$axios.post("Subjects", null, {
        params: {
          subject: subject
        }
      });
      this.subjects.push(newSubject.data);
    },
    //delete subject request and update data
    async deleteSubject(subject) {
      await this.$axios.delete("Subjects", {
        params: {
          id: subject.id
        }
      });
      this.subjects = this.subjects.filter(s => s.id != subject.id);
    },
    //gets teachers unassigned to this subject
    async getAvailableTeachers(subject) {
      const updatedTeachers = await this.$axios.get("Teaching", {
        params: {
          subject: subject.id
        }
      });
      this.availableTeachers = updatedTeachers.data;
    },
    // adds selected row to array selected items
    rowSelectedHandler(row) {
      this.selectedItems = row;
    },
    // add teacher request and update data
    addTeacher(item) {
      this.selectedItems.forEach(async t => {
        await this.$axios.post("Teaching", null, {
          params: {
            teacher: t.id,
            subject: item.id
          }
        });
        item.teachers.push(t);
      });
    }
  }
};
</script>

<style></style>
