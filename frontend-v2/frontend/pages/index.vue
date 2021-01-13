<template>
  <div>
    <b-card v-if="selectedSubject == null">
      <h2>Seleziona una materia</h2>
      <b-card no-body class="m-b1" v-for="s in subjects" :key="s.id">
        <!-- Seleziona Materia -->
        <b-card-header header-tag="header" class="p-1" role="tab">
          <b-button
            block
            v-b-toggle="'accordion-' + s.id"
            variant="info"
            @click="setSubject(s)"
            v-b-modal.modal
          >
            {{ s.name }}
          </b-button>
        </b-card-header>
        <!-- /Seleziona Materia -->
      </b-card>
    </b-card>
    <b-card v-if="selectedSubject != null && selectedSlot == null">
      <b-container fluid>
        <b-button variant="danger" @click="setSubject(null)">
          Back
        </b-button>
        <h3>Seleziona lo slot</h3>
        <b-row>
          <b-col v-for="d of slots" :key="d.date">
            <p class="px-2 pb-1 mb-2 border-bottom border-dark text-center">
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
    </b-card>
    <b-card v-if="selectedSlot != null && selectedTeacher == null">
      <b-button variant="danger" @click="setSlot(null)">
        Back
      </b-button>
      <h3>Seleziona Professore</h3>
      <b-container>
        <b-row>
          <b-button
            v-for="t of selectedSlot.teachers"
            :key="t.id"
            @click="setTeacher(t)"
            class="mr-2"
          >
            {{ t.name }}
          </b-button>
        </b-row>
      </b-container>
    </b-card>
    <b-card v-if="selectedTeacher != null">
      <b-button variant="danger" @click="setTeacher(null)">
        Back
      </b-button>
      <h3>Riepilogo Prenotazione</h3>
      <p><b>Materia: </b>{{ selectedSubject.name }}</p>
      <p>
        <b>Orario: </b
        >{{ $moment(selectedSlot.date).format("ddd DD/MM/YY HH:mm") }}
      </p>
      <p><b>Professore: </b>{{ selectedTeacher.name }}</p>
      <div class="mx-auto mt-5" style="width: 200px;">
        <b-button variant="success" @click="bookSlot" style="width: 200px;">
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
    return {};
  },
  computed: {
    ...mapGetters([
      "selectedSubject",
      "selectedSlot",
      "selectedTeacher",
      "slots",
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
      return s.isAvailable ? "success" : "danger";
    }
  }
};
</script>

<style></style>
