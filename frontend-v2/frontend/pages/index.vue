<template>
  <div id="index">
    <div>
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
              @click="setSubject(s)"
              style="width: 190px"
            >
              {{ s.name }}
            </b-button>
          </b-col>
        </b-row>
      </b-container>
      <!-- /Seleziona Materia -->
    </div>

    <div v-if="this.getSelectedSubject != null">
      <h3 class="ml-4 mt-5">Seleziona lo slot</h3>
      <b-container fluid>
        <b-row>
          <b-col
            cols="12"
            md="2"
            v-for="d of getSlots"
            :key="d.date"
            class="mx-auto"
            style="maxWidth: 200px;"
          >
            <p class="px-2 pb-1 mb-2 border-bottom border-info text-center">
              {{ $moment(d.date).format("ddd DD/MM/YY") }}
            </p>
            <div v-for="sl of d.daySlots" :key="sl.date">
              <b-button
                class="w-100 mb-1"
                :disabled="!sl.isAvailable"
                :variant="available(sl)"
                @click="setSlot(sl)"
              >
                {{ $moment(sl.date).format("HH:mm") }}
              </b-button>
            </div>
          </b-col>
        </b-row>
      </b-container>
    </div>
    <div class="mt-5" v-if="getSelectedSlot != null">
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
            <b-button
              variant="info"
              @click="setTeacher(t)"
              style="width: 200px;"
            >
              {{ t.name }}
            </b-button>
          </b-col>
        </b-row>
      </b-container>
    </div>
    <div class="mt-5" v-if="getSelectedTeacher != null">
      <h3>Riepilogo Prenotazione</h3>
      <p><b>Materia: </b>{{ getSelectedSubject.name }}</p>
      <p>
        <b>Orario: </b
        >{{ $moment(getSelectedSlot.date).format("ddd DD/MM/YY HH:mm") }}
      </p>
      <p><b>Professore: </b>{{ getSelectedTeacher.name }}</p>
      <div class="mx-auto mt-5" style="width: 200px;">
        <b-button variant="success" @click="bookSlot()" style="width: 200px;">
          Prenota!
        </b-button>
      </div>
    </div>
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
    return {};
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
      return !s.isAvailable //|| this.$moment(s.date).isBefore(this.$moment.now())
        ? "danger"
        : "success";
    }
  }
};
</script>

<style></style>
