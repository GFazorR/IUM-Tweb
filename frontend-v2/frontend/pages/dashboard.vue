<template>
  <div>
    <!-- Table Bookings -->
    <div v-if="bookings.length == 0">
      <h2 class="text-center">
        Non ci sono prenotazioni
      </h2>
      <h4 class="text-center">
        Torna alla Home per prenotare
      </h4>
    </div>
    <b-table v-else responsive :items="bookings" :fields="fields">
      <!-- Action Col -->
      <template v-slot:cell(actions)="booking">
        <b-button-group>
          <!-- Confirm Button -->
          <b-button
            v-if="
              $moment(booking.item.date).isBefore($moment()) &&
                booking.item.status == 10
            "
            variant="success"
            class="ml-auto"
            style="width: 150px;"
            @click="confirmBooking(booking.item.id)"
          >
            Conferma
          </b-button>
          <!-- Cancel Button -->
          <b-button
            v-if="
              $moment(booking.item.date).isAfter($moment()) &&
                booking.item.status == 10
            "
            variant="danger"
            class="ml-auto"
            style="width: 150px;"
            @click="cancelBooking(booking.item.id)"
          >
            Cancella
          </b-button>
        </b-button-group>
      </template>
      <!-- Status Column -->
      <template v-slot:cell(status)="booking">
        <b v-if="booking.item.status == 10" class>Creata</b>
        <b v-if="booking.item.status == 20" class="text-danger">Annullata</b>
        <b v-if="booking.item.status == 30" class="text-success">Confermata</b>
      </template>
      <!-- Date Column -->
      <template v-slot:cell(date)="booking">
        <p>{{ $moment(booking.item.date).format("ddd DD/MM/YY HH:mm") }}</p>
      </template>
    </b-table>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
export default {
  layout: "logged",
  middleware: "logged",
  data() {
    return {
      fields: [
        {
          key: "subject",
          label: "Materia",
          sortable: false
        },
        {
          key: "teacher",
          label: "Professore",
          sortable: false
        },
        {
          key: "date",
          label: "Data",
          sortable: false
        },
        {
          key: "status",
          label: "Stato prenotazione",
          sortable: false
        },
        {
          key: "actions",
          label: "Azioni",
          sortable: false
        }
      ]
    };
  },
  //gets bookings pre-rendering
  async asyncData({ $axios }) {
    const bookings = (await $axios.get("Bookings")).data;
    return { bookings };
  },
  //vuex-persist methods
  computed: {
    ...mapGetters(["isAdmin", "user"])
  },
  methods: {
    //confirm booking in backend and updates data
    async confirmBooking(b) {
      await this.$axios.put("Bookings", null, {
        params: {
          id: b
        }
      });

      this.bookings = this.bookings.map(a => {
        if (b == a.id) {
          a.status = 30;
        }
        return a;
      });
    },
    //cancel booking in backend and updates data
    async cancelBooking(b) {
      console.log(b);
      await this.$axios.delete("Bookings", {
        params: {
          id: b
        }
      });
      this.bookings = this.bookings.map(a => {
        if (b == a.id) {
          a.status = 20;
        }
        return a;
      });
    }
  }
};
</script>

<style></style>
