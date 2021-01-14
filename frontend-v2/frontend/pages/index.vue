<template>
  <div id="index">
    <b-progress :value="value" :max="max" animated></b-progress>
    <b-card v-if="getSelectedSubject == null">
      <b-container fluid>
        <h2>Seleziona una materia</h2>

        <!-- Seleziona Materia -->

        <b-row>
          <b-col
            cols="12"
            sm="auto"
            class="mt-1 mr-1"
            v-for="s in subjects"
            :key="s.id"
          >
            <b-button
              block
              variant="info"
              @click="setSubject(s), state()"
              style="width: 190px"
            >
              {{ s.name }}
            </b-button>
          </b-col>
        </b-row>
      </b-container>
      <!-- /Seleziona Materia -->
    </b-card>

    <b-card v-if="this.getSelectedSubject != null && getSelectedSlot == null">
      <b-button
        class="ml-4 mb-2"
        variant="danger"
        @click="$store.commit('SET_SUBJECT', null), state()"
      >
        Back
      </b-button>
      <h3 class="ml-4">Seleziona lo slot per {{ getSelectedSubject.name }}</h3>
      <b-container fluid>
        <b-row>
          <b-col
            cols="12"
            xl="2"
            v-for="d of getSlots"
            :key="d.date"
            class="mx-auto"
          >
            <p class="px-2 pb-1 mb-2 border-bottom border-dark text-center">
              {{ $moment(d.date).format("ddd DD/MM/YY") }}
            </p>
            <div v-for="sl of d.daySlots" :key="sl.date">
              <b-button
                class="w-100 mb-1"
                :disabled="
                  !sl.isAvailable || $moment(sl.date).isBefore($moment.now())
                "
                :variant="available(sl)"
                @click="setSlot(sl), state()"
              >
                {{ $moment(sl.date).format("HH:mm") }}
              </b-button>
            </div>
          </b-col>
        </b-row>
      </b-container>
    </b-card>
    <b-card v-if="getSelectedSlot != null && getSelectedTeacher == null">
      <b-button variant="danger" @click="setSlot(null), state()">
        Back
      </b-button>
      <h3>Seleziona Professore</h3>
      <b-container fluid>
        <b-row>
          <b-col
            class="mt-1 mr-1"
            cols="12"
            sm="auto"
            v-for="t of getSelectedSlot.teachers"
            :key="t.id"
          >
            <b-button @click="setTeacher(t), state()" style="width: 200px;">
              {{ t.name }}
            </b-button>
          </b-col>
        </b-row>
      </b-container>
    </b-card>
    <b-card v-if="getSelectedTeacher != null">
      <b-button variant="danger" @click="setTeacher(null), state()">
        Back
      </b-button>
      <h3>Riepilogo Prenotazione</h3>
      <p><b>Materia: </b>{{ getSelectedSubject.name }}</p>
      <p>
        <b>Orario: </b
        >{{ $moment(getSelectedSlot.date).format("ddd DD/MM/YY HH:mm") }}
      </p>
      <p><b>Professore: </b>{{ getSelectedTeacher.name }}</p>
      <div class="mx-auto mt-5" style="width: 200px;">
        <b-button
          variant="success"
          @click="bookSlot(), (value = 0)"
          style="width: 200px;"
        >
          Prenota!
        </b-button>
      </div>
    </b-card>
  </div>
</template>

<script>
import axios from "axios";
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  layout: "logged",
  async asyncData({ $axios }) {
    const subjects = await $axios.$get("Subjects");
    return { subjects };
  },
  data() {
    return {
      value: 0,
      max: 100
    };
  },
  computed: {
    ...mapGetters([
      "getSelectedSubject",
      "getSelectedSlot",
      "getSelectedTeacher",
      "getSlots",
      "user"
    ])
  },
  methods: {
    ...mapActions([
      "setSubject",
      "setSlot",
      "setTeacher",
      "bookSlot",
      "clearBooking"
    ]),
    available(s) {
      return !s.isAvailable || this.$moment(s.date).isBefore(this.$moment.now())
        ? "danger"
        : "success";
    },
    state() {
      if (
        this.getSelectedSubject == null &&
        this.getSelectedSlot == null &&
        this.getSelectedTeacher == null
      ) {
        this.value = 0;
      } else if (
        this.getSelectedSubject != null &&
        this.getSelectedSlot == null &&
        this.getSelectedTeacher == null
      ) {
        this.value = 33;
      } else if (
        this.getSelectedSubject != null &&
        this.getSelectedSlot != null &&
        this.getSelectedTeacher == null
      ) {
        this.value = 66;
      } else if (
        this.getSelectedSubject != null &&
        this.getSelectedSlot != null &&
        this.getSelectedTeacher != null
      ) {
        this.value = 100;
      }
    }
  },
  beforeMount() {
    this.state();
  }
};
</script>

<style></style>
