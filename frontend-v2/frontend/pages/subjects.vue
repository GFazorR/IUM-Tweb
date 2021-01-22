<template>
  <div>
    <!-- TODO maybe create components -->
    <!-- Button Collapse -->
    <b-button variant="success" v-b-modal.add-subject-modal>
      Aggiungi una materia
    </b-button>
    <!-- Form Collapse -->
    <b-modal
      id="add-subject-modal"
      title="Aggiungi una Materia"
      centered
      hide-footer
    >
      <b-form-group label="Nome Materia:">
        <b-form-input
          id="subject-name-input"
          v-model="subjectName"
          :state="subjectState"
          aria-describedby="input-live-feedback"
          trim
          required
        ></b-form-input>
        <b-form-invalid-feedback id="input-live-feedback">
          Il campo non può essere vuoto
        </b-form-invalid-feedback>
      </b-form-group>
      <b-button variant="success" @click="addSubject(subjectName)"
        >Aggiungi</b-button
      >
    </b-modal>
    <!-- /Form Collapse -->

    <!-- Subjects Table -->
    <b-container fluid class="pt-3">
      <b-table hover :items="subjects" :fields="fields" v-model="currentItems">
        <!-- Button ToggleDetails -->
        <template v-slot:cell(name)="{ item }">
          <div class="ml-1">
            <b> {{ item.name }} </b>
          </div>
        </template>
        <template v-slot:cell(actions)="{ item }">
          <div>
            <b-button size="md" variant="primary" @click="toggleDetails(item)">
              <b-icon icon="gear-fill" aria-hidden="true"></b-icon>
            </b-button>
            <b-button size="md" variant="danger" @click="deleteSubject(item)">
              <b-icon icon="trash-fill" aria-hidden="true"></b-icon>
            </b-button>
          </div>
        </template>
        <!-- /Button ToggleDetails -->
        <template v-slot:row-details="{ item }">
          <b-container fluid>
            <b-button
              variant="success"
              v-b-modal.modal
              @click="getAvailableTeachers(item)"
              class="mt-2 mb-2 ml-2"
            >
              Aggiungi professori
            </b-button>
            <!-- Modal table -->
            <b-modal
              centered
              hide-footer
              id="modal"
              title="Aggiungi Professori"
            >
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
              <b-button variant="success" @click="addTeacher(item)">
                Aggiungi
              </b-button>
            </b-modal>
            <!-- /Modal table -->
            <!-- Delete Subject Button -->

            <!-- /Delete Subject Button -->
            <!-- Subject Teachers Table -->
            <b-container v-if="item.teachers.length == 0">
              <h4 class="text-center">
                Non ci sono professori che insegnano questa materia
              </h4>
            </b-container>
            <b-container v-else fluid>
              <b-table
                fixed
                small
                hover
                striped
                sticky-header
                :fields="innerFields"
                :items="item.teachers"
              >
                <template v-slot:cell(actions)="teacher">
                  <b-button
                    variant="danger"
                    @click="deleteTeacher(teacher, item)"
                  >
                    <b-icon icon="trash-fill" aria-hidden="true"></b-icon>
                  </b-button>
                </template>
              </b-table>
            </b-container>
          </b-container>
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
      subjectName: "",
      currentItems: [],
      shownItems: [],
      selectedItems: [],
      availableTeachers: [],
      fields: [
        {
          key: "name",
          label: "Materia",
          sortable: false,
          class: "align-middle"
        },
        {
          key: "actions",
          label: "",
          sortable: false,
          class: "text-center align-middle",
          style: "width=200px"
        }
      ],
      innerFields: [
        {
          key: "name",
          label: "Professore",
          sortable: false,
          class: "align-middle"
        },
        {
          key: "actions",
          label: "",
          sortable: false,
          class: "text-center align-middle",
          style: "width=200px"
        }
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
      if (subject != "") {
        const newSubject = await this.$axios.post("Subjects", null, {
          params: {
            subject: subject
          }
        });
        this.subjects.push(newSubject.data);
        this.subjectName = "";
        this.$bvModal.hide("add-subject-modal");
      }
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
    async addTeacher(item) {
      if (this.selectedItems.length > 0) {
        this.selectedItems.forEach(async t => {
          await this.$axios.post("Teaching", null, {
            params: {
              teacher: t.id,
              subject: item.id
            }
          });
          item.teachers.push(t);
          this.$bvModal.hide("modal");
          this.selectedItems = [];
        });
      } else {
        this.$toast.error("No Teachers selected!");
      }
    }
  },
  computed: {
    subjectState() {
      return this.subjectName.length > 0 ? true : false;
    }
  }
};
</script>

<style></style>
