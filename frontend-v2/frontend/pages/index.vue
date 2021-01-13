<template>
  <div id="index">
    <b-progress :value="value" :max="max" animated></b-progress>
    <b-card v-if="getSelectedSubject == null">
      <h2>Seleziona una materia</h2>
      <b-card no-body class="m-b1" v-for="s in subjects" :key="s.id">
        <!-- Seleziona Materia -->
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button block variant="info" @click="setSubject(s), state()">
            {{ s.name }}
          </b-button>
        </b-card-header>
        <!-- /Seleziona Materia -->
      </b-card>
    </b-card>
    <b-card v-if="this.getSelectedSubject != null && getSelectedSlot == null">
      <b-button
        variant="danger"
        @click="$store.commit('SET_SUBJECT', null), state()"
      >
        Back
      </b-button>
      <h3>Seleziona lo slot</h3>
      <b-container fluid>
        <b-row>
          <b-col v-for="d of getSlots" :key="d.date">
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
      <b-container>
        <b-row>
          <b-button
            v-for="t of getSelectedSlot.teachers"
            :key="t.id"
            @click="setTeacher(t), state()"
            class="mr-2"
          >
            {{ t.name }}
          </b-button>
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
