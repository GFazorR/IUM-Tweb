<template>
  <b-card>
    <!-- Table Subjects -->
    <b-table
      striped
      hover
      borderless
      sticky-header
      selectable
      select-mode="single"
      :items="subjects"
      :fields="fields"
      @row-clicked="setSubject"
    >
    </b-table>
  </b-card>
</template>

<script>
import { mapActions } from "vuex";
export default {
  data() {
    return {
      fields: [
        {
          key: "name"
        }
      ]
    };
  },
  props: ["subjects"],
  methods: {
    ...mapActions(["setSubject"])
  }
};
</script>

<style></style>
