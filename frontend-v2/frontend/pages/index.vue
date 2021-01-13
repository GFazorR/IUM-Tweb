<template>
  <!-- TODO Fix Layout  -->
  <!-- TODO maybe create componets -->
  <div class="container">
    <!-- Tabella Subjects -->
    <b-row>
      <b-col cols="2">
        <subjectsComponent :subjects="subjects"> </subjectsComponent>
      </b-col>
      <b-col cols="10">
        <!-- Calendar  -->
        <b-container v-if="selectedSubject != null">
          <b-card class="calendar">
            <h1 class="px-2 pb-1 mb-2 border-bottom border-dark text-center">
              {{ selectedSubject.name }}
            </h1>
            <b-row>
              <b-container>
                <b-card
                  v-for="d of this.slots"
                  :key="d.date"
                  class=" d-inline-block"
                >
                  <p
                    class="px-2 pb-1 mb-2 border-bottom border-dark text-center"
                  >
                    {{ $moment(d.date).format("ddd DD/MM/YY") }}
                  </p>
                  <b-button-group vertical class="mx-auto">
                    <b-button
                      v-for="s of d.daySlots"
                      :key="s.date"
                      :disabled="!s.isAvailable"
                      :variant="available(s)"
                      @click="setSlot(s)"
                      class="sm my-1 "
                    >
                      {{ $moment(s.date).format("HH:mm") }}
                    </b-button>
                  </b-button-group>
                </b-card>
              </b-container>
            </b-row>
          </b-card>
        </b-container>
        <!-- /Calendar -->
        <!-- Select Teachers -->
        <b-container v-if="selectedSlot != null">
          <b-card>
            <b-button
              v-for="t of selectedSlot.teachers"
              :key="t.id"
              @click="setTeacher(t)"
            >
              {{ t.name }}
            </b-button>
          </b-card>
        </b-container>
        <!-- /Select Teachers -->
        <!-- Riepilogo e prenota-->
        <b-card v-if="selectedTeacher != null">
          <p>{{ selectedSubject.name }}</p>
          <p>{{ $moment(selectedSlot.date).format("ddd DD/MM/YY HH:mm") }}</p>
          <p>{{ selectedTeacher.name }}</p>
          <b-button @click="bookSlot">
            Prenota
          </b-button>
        </b-card>
        <!-- /Riepilogo -->
      </b-col>
    </b-row>
  </div>
</template>

<script>
import axios from "axios";
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
import subjectsComponent from "../components/subjects-component.vue";
// TODO maybe redo component
// import calendarComponent from "../components/calendar-component.vue";

export default {
  layout: "logged",
  components: { subjectsComponent },
  async asyncData({ $axios }) {
    const subjects = await $axios.$get("Subjects");
    return { subjects };
  },
  data() {
    return {
      //table fields
      fields: [
        {
          key: "name"
        }
      ]
    };
  },
  computed: {
    //vuex-persist methods
    ...mapGetters([
      "selectedSubject",
      "selectedSlot",
      "selectedTeacher",
      "slots",
      "user"
    ])
  },
  methods: {
    //vuex-persist methods
    ...mapActions([
      "setSubject",
      "setSlot",
      "setTeacher",
      "bookSlot",
      "clearBooking"
    ]),
    subjectSelected(row) {
      this.setSubject(row);
    },
    // sets buttons style
    //TODO set danger if date expired
    available(s) {
      return s.isAvailable ? "success" : "danger";
    }
  }
};
</script>

<style></style>
