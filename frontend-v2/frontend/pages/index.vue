<template>
  <div class="container">
    <div v-for="s of subjects" :key="s.id">
      <toggleButtonVue>{{s.name}}</toggleButtonVue>
    </div>

    <div class="d-flex flex-column mr-2ay" v-for="d of getWeek()" :key="d.day">
        <h3 >{{ $moment(d.day, 'YYYY-MM-DD').format('dd DD MMM YYYY') }}</h3>
        <button class="px-2 pb-1 mb-2 border-bottom border-dark text-center" v-for="t of d.timeSlot" :key="t.id">
          {{t}}
        </button>
    </div>
      
    
  </div>
</template>

<script>
import axios from 'axios'
import toggleButtonVue from '../components/toggle-button.vue'
export default {
  props:['toggleButtonVue'],
  async asyncData({ $axios }) {
    const subjects = await $axios.$get('http://localhost:8080/backend-v2/api/Subjects')
    return { subjects }
  },
  methods:{
    getWeek(){
      var curr = new Date; // get current date
      var first = curr.getDate() - curr.getDay() + (curr.getDay() === 0 ? -6:1); // First day is the day of the month - the day of the week
      var week = new Array()
      for(var i = 0 ; i <= 4; i++){
        var slot = {
          timeSlot : ["14:00","15:00","16:00","17:00","18:00"],
          day : new Date(curr.setDate(first+i))
        }
        week.push(slot)
      }
      return week
    }
  }
}
</script>

<style></style>
