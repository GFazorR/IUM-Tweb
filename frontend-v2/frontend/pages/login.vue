<template>
  <div class="cont">
    
      
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
  data() {
    return {
      form: {
        username: '',
        password: ''
      }
    }
  },
  methods: {
    async login() {
      try {
        const { data } = await this.$axios.post('login', null, {
          params: this.form
        })
        this.$store.commit('SET_USER', data)
        this.$router.push('/')
      } catch (e) {}
    }
  }
}
</script>
