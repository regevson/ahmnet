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

      <p>Trainer: {{trainingGroup.trainer.firstName}} {{trainingGroup.trainer.lastName}}</p>

      <p>Anwesenheit Spieler:</p>
      <div class="form-check">
        <div v-for="player in trainingGroup.players" :key="player.id">
          <input class="form-check-input" type="checkbox" :value="player.id" :id="player.id" v-model="presentPlayers">
          <label class="form-check-label" :for="player.id">
            <span> {{player.firstName}} {{player.lastName}} </span>
          </label>
        </div>
      </div>

      <br>

      <p>Schwerpunkte:</p>
      <textarea v-model="bulletPoints" rows="4">
      </textarea>
      <br>
      <p>Kommentare:</p>
      <textarea v-model="comments" rows="4">
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
      trainingGroup: null,
      presentPlayers: [],
      bulletPoints: '',
      comments: '',
      value: ''
    }
  },

  async created() {
    const response = await axios.get('api/training?id=' + this.$route.params.training);
    this.training = response.data;
    this.trainingGroup = this.training.trainingGroup;
    this.presentPlayers = this.training.attendees.map(p => p.id);
    this.bulletPoints = this.training.bulletPoints;
    this.comments = this.training.comment;
  },

  methods: {
    async updateTrainingDetails() {
      const params = new URLSearchParams()
      params.append('id', this.training.id)
      params.append('dateTime', this.training.date + ' ' + this.training.startTime)
      params.append('duration', this.training.durationMinutes)
      params.append('attendees', this.presentPlayers)
      params.append('bulletPoints', this.bulletPoints)
      params.append('comments', this.comments)

      const response = await axios.post('api/updateTrainingDetails?' + params);
      console.log(response);
      this.$router.push({name: 'timetable'});
    },
  }


}


</script>

<style>
</style>

