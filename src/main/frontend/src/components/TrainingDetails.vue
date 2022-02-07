<template>
  <div align="left">

    <form v-if="training" @submit.prevent="updateTrainingDetails">

      <p>Club: {{training.club.name}}</p>

      <p>Datum: {{training.date}}</p>

      <b-form-datepicker v-model="training.date" class="mb-2" 
            locale="de" 
            :date-format-options="{ year: 'numeric', month: '2-digit', day: '2-digit'}" 
            :showDecadeNav="false" 
            :start-weekday="1"
            :hide-header="true"
            calendar-width="100%"
            menu-class="w-100"
            >
      </b-form-datepicker>
      <br>


      <p>Zeit: {{training.timeslot}}</p>
      <b-form-timepicker v-model="training.startTime" 
            locale="de"
            :minutes-step="15"
            :hide-header="true"
            >
      </b-form-timepicker>
      <br>


      <p>Dauer: {{training.durationMinutes}}</p>
      <b-form-input type="number" v-model="training.durationMinutes" placeholder="Dauer in Minuten"></b-form-input>
      <br>

      <p>Trainer: {{training.trainerFn}} {{training.trainerLn}}</p>

      <p>Anwesenheit Spieler:</p>
      <div class="form-check">
        <div v-for="player in training.players" :key="player.id">
          <input class="form-check-input" type="checkbox" :value="player.id" :id="player.id" v-model="training.attendees">
          <label class="form-check-label" :for="player.id">
            <span> {{player.firstName}} {{player.lastName}} </span>
          </label>
        </div>
      </div>

      <br>

      <p>Schwerpunkte:</p>
      <textarea v-model="training.bulletPoints" rows="4">
      </textarea>
      <br>
      <p>Kommentare:</p>
      <textarea v-model="training.comments" rows="4">
      </textarea>
      <br>

      <input type="submit" class="fourth" value="Anpassen" style="margin-top: 30px">
      
    </form>

    <h3 v-if="!training">Oops... sth. went wrong!</h3>

  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TrainingDetails',
  data() {
    return {
      training: null,
    }
  },

  async created() {
    const response = await axios.get('api/training?id=' + this.$route.params.training);
    this.training = response.data;
  },

  methods: {
    async updateTrainingDetails() {

      const config = {headers: {'Content-Type': 'application/json'}}
      let params = JSON.stringify(this.training);

      const response = await axios.post('api/updateTrainingDetails', params, config);
      console.log(response);
      this.$router.push({name: 'timetable'});
    },
  }


}


</script>

<style>
</style>

