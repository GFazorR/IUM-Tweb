<template>
  <div>
    <b-navbar sticky toggleable="lg" type="dark" variant="dark">
      <b-container>
        <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>
        <b-navbar-brand>Ripetizioni</b-navbar-brand>

        <b-collapse id="nav-collapse" is-nav>
          <b-navbar-nav>
            <b-nav-item to="/" exact exact-active-class="active"
              >Calendario</b-nav-item
            >
            <b-nav-item
              v-if="user"
              to="/dashboard"
              exact
              exact-active-class="active"
              >Prenotazioni</b-nav-item
            >
            <b-nav-item
              v-if="isAdmin"
              to="/subjects"
              exact
              exact-active-class="active"
              >Materie</b-nav-item
            >
            <b-nav-item
              v-if="isAdmin"
              to="/teachers"
              exact
              exact-active-class="active"
              >Professori</b-nav-item
            >
          </b-navbar-nav>

          <!-- Right aligned nav items -->
          <b-navbar-nav class="ml-auto">
            <b-nav-item-dropdown v-if="user" right>
              <!-- Using 'button-content' slot -->
              <template v-slot:button-content>
                <b class="text-white pr-2">{{ user.username }}</b>
              </template>
              <template
                ><b-dropdown-item @click="logout"
                  >Esci</b-dropdown-item
                ></template
              >
            </b-nav-item-dropdown>
            <b-nav-item v-else to="/login" exact exact-active-class="active">
              <b class="text-white pr-2">Login</b>
            </b-nav-item>
          </b-navbar-nav>
        </b-collapse>
      </b-container>
    </b-navbar>
    <b-container>
      <nuxt-child class="mt-4"></nuxt-child>
    </b-container>
  </div>
</template>

<style></style>

<script>
import { mapGetters } from "vuex";
export default {
  computed: {
    ...mapGetters(["user", "isAdmin"])
  },
  methods: {
    async logout() {
      if (this.$store.getters.user != null) {
        await this.$axios.post("Logout");
        this.$store.commit("SET_USER", null);
      }
      this.$router.push("/");
    }
  }
};
</script>
