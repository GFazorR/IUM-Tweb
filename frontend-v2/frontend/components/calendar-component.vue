<template>
  <b-container>
    <b-card class="calendar">
      <h1 class="px-2 pb-1 mb-2 border-bottom border-dark text-center">
        {{ selectedSubject.name }}
      </h1>
      <b-row>
        <b-container>
          <b-card v-for="d of this.slots" :key="d.date" class=" d-inline-block">
            <p class="px-2 pb-1 mb-2 border-bottom border-dark text-center">
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
</template>

<script>
import { mapGetters } from "vuex";
import { mapActions } from "vuex";
export default {
  props: ["slots"],
  computed: {
    ...mapGetters(["selectedSubject", "slots"])
  },
  methods: {
    ...mapActions(["setSlot"]),
    available(s) {
      return s.isAvailable ? "success" : "danger";
    }
  }
};
</script>

<style></style>
