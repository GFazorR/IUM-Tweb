<template>
  <b-container class="text-center mt-5 pt-5">
    <h1>Error</h1>
    <h2 class="text-danger my-2" variant="danger">
      <b>{{ message }}</b>
    </h2>
    <nuxt-link v-if="status === 401" to="/login">Vai al login</nuxt-link>
    <nuxt-link v-else to="/">Home</nuxt-link>
  </b-container>
</template>

<script>
export default {
  props: ["error"],

  computed: {
    status() {
      if (this.error.response) return this.error.response.status;
      else return 0;
    },
    message() {
      console.log(this.error);
      if (this.error.response && this.error.response.data)
        return this.error.response.data.error;
      else return this.error.message || this.error.toString();
    }
  },
  mounted() {}
};
</script>
