<template>

  <div>
    <div v-for="training in trainings" :key="training.id" align="center" class="trainingPreview">
      <router-link :to="{name: 'trainingdetails', params: {trainingId: training.id}}" class="link">
        <div>
          <span style="font-size: 13px; display: block; font-weight: 600;">{{training.date}}</span>
          <hr>
          <span>{{training.club.name}}</span><br>
          <span>{{training.timeslot}}</span><br>
          <hr>
          <span style="font-size: 13px; display: block; font-weight: 600;">{{training.court}}</span>
        </div>
      </router-link>
    </div>
  </div>

</template>

<script>
//import axios from 'axios'

export default {
  name: 'TrainingSlot',
  props: {
    trainings: Array,
    user: Object
  },

  data() {
    return {
    }
  },

}


</script>

<style>


body {
  font-family: 'Open Sans', sans-serif !important;
}


td {
  border-top: none !important;
}
.trainingPreview {
  font-weight: 700;
  border-radius: 14px;
  padding: 10px 5px 10px 5px;
  width: auto;
  margin-top: 20px;
  min-width: 125px;
  transition: all 0.5s ease-in-out;
  background: #d1fdf53d;
  border: 2px solid #4b9183;
  text-align: center;
}

.trainingPreview:hover {
  border-radius: 5px;
  transition: all 0.5s ease-in-out;
}

.link {
  color: #4b9183;
  text-decoration: none;
}
.link:hover {
  color: #4b9183;
  text-decoration: none;
}

.trainingPreview hr {
  margin: 0;
  background: #4b9183;
}

</style>

