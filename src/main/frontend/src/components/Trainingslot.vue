<template>

  <div>
    <div v-for="training in trainings" :key="training.id" align="center" class="trainingPreview">
      <router-link @click.native="storeSelectedTraining(training)" :to="{name: 'trainingdetails', params: {trainingId: training.id}}" class="link">
        <div>
          {{training.club.name}}<br>
          {{training.date}}<br>
          {{training.timeslot}}<br>
          {{training.court}}<br>
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

  methods: {
    storeSelectedTraining(training) {
      this.$store.commit('selectedTraining', training);
    }
  }

}


</script>

<style>
td {
  border-top: none !important;
}
.trainingPreview {
  background: cadetblue;
  color: white;
  font-weight: bold;
  border-radius: 14px;
  padding: 10px 0 10px 0;
  width: auto;
  margin-top: 20px;
  min-width: 125px;
}
.link {
  color: white;
  font-weight: bold;
}
</style>
