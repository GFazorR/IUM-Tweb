<template>
  <div class="cont">
    <b-card class="test">
      <!-- Form-group Username -->
      <b-form-group id="usename-input" label="Username" label-for="input-1">
        <b-form-input
          id="input-1"
          v-model="form.username"
          placeholder="Username"
          required
          trim
        />
      </b-form-group>
      <!-- Form-group Password -->
      <b-form-group id="password-input" label="Password" label-for="input-2">
        <b-form-input
          id="input-2"
          v-model="form.password"
          placeholder="Password"
          type="password"
          required
          trim
        />
      </b-form-group>
      <!-- Login Button -->
      <b-button class="btn" id="loginButton" @click="login" type="button">
        Login
      </b-button>
      <!-- Continue as Guest -->
      <div class="text-center mb-2 w-100">
        <nuxt-link to="/">Continua come Ospite</nuxt-link>
      </div>
    </b-card>
  </div>
</template>

<style lang="css">
.cont {
  display: flex;
  justify-content: center;
  margin-top: 5rem;
}
.card {
  min-width: 300px;
}
</style>

<script>
export default {
  computed: {},
  data() {
    return {
      form: {
        username: "",
        password: ""
      }
    };
  },
  methods: {
    async login() {
      try {
        const { data } = await this.$axios.post("Login", null, {
          params: this.form
        });
        this.$store.commit("SET_USER", data);
        this.$router.push("/");
      } catch (e) {}
    }
  }
};
</script>
