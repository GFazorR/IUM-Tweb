<template>
  <b-card>
    <b-form-group
      id="add-teacher"
      label-cols-lg="3"
      label="Aggiungi un Professore"
    >
      <b-form-group label="Nome:">
        <b-form-input
          id="teacher-name-input"
          v-model="name"
          trim
        ></b-form-input>
      </b-form-group>

      <b-form-group label="Cognome:">
        <b-form-input
          id="teacher-surname-input"
          v-model="surname"
          trim
        ></b-form-input>
      </b-form-group>

      <b-button @click="$emit('clicked', name, surname)">
        Aggiungi
      </b-button>
    </b-form-group>
  </b-card>
</template>

<script>
export default {};
</script>

<style></style>
