<template>
  <b-container>
    <b-form-group id="add-teacher">
      <b-form-group label="Nome:">
        <b-form-input
          id="teacher-name-input"
          v-model="name"
          :state="stateName"
          :aria-describedby="input - live - feedback1"
          trim
        ></b-form-input>
        <b-form-invalid-feedback id="input-live-feedback1">
          Il campo non può essere vuoto
        </b-form-invalid-feedback>
      </b-form-group>

      <b-form-group label="Cognome:">
        <b-form-input
          id="teacher-surname-input"
          v-model="surname"
          :state="stateSurname"
          :aria-describedby="input - live - feedback2"
          trim
        ></b-form-input>
        <b-form-invalid-feedback id="input-live-feedback2">
          Il campo non può essere vuoto
        </b-form-invalid-feedback>
      </b-form-group>

      <b-button variant="success" @click="$emit('clicked', name, surname)">
        Aggiungi
      </b-button>
    </b-form-group>
  </b-container>
</template>

<script>
export default {
  data() {
    return {
      name: "",
      surname: ""
    };
  },
  computed: {
    stateName() {
      return this.name != "" ? true : false;
    },
    stateSurname() {
      return this.surname != "" ? true : false;
    }
  }
};
</script>

<style></style>
