<template>
  <div align="left">

    <form v-if="training" @submit.prevent="updateTrainingDetails">

      <p>Club: {{training.club.name}}</p>
      <p>Datum: {{training.date}}</p>
      <p>Zeit: {{training.timeslot}}</p>
      <p>Dauer: {{training.durationMinutes}}</p>

      <p>Trainer: {{trainingGroup.trainer.firstName}} {{trainingGroup.trainer.lastName}}</p>

      <p>Anwesenheit Spieler:</p>
      <div class="form-check">
        <div v-for="player in trainingGroup.players" :key="player.id">
          <input class="form-check-input" type="checkbox" :value="player.id" :id="player.id" v-model="checkedNames">
          <label class="form-check-label" :for="player.id">
            <span> {{player.firstName}} {{player.lastName}} </span>
          </label>
        </div>
      </div>
      {{checkedNames}}

      <br>

      <p>Schwerpunkte:</p>
      <textarea id="bullet" :value="bulletpoints" rows="4">
      </textarea>
      <br>
      <p>Kommentare:</p>
      <textarea id="comment" :value="comments" rows="4">
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
      checkedNames: []
    }
  },

  computed: {
    training: function() {
      return this.$store.getters.selectedTraining;
    },
    trainingGroup: function() {
      return this.training.trainingGroup;
    },
    comments: function() {
      return this.training.comment;
    },
    bulletpoints: function() {
      return this.training.bulletPoints;
    },
  },

  mounted() {
    //window.onbeforeunload = function() {return '';}
  },

  watch: {
    training: function() {
      this.checkedNames = this.training.attendance.map(p => p.id);
    }
  },

  methods: {
    async updateTrainingDetails() {
      const params = new URLSearchParams()
      params.append('id', this.training.id)
      params.append('attendees', this.checkedNames)
      params.append('bulletPoints', document.getElementById('bullet').value)
      params.append('comments', document.getElementById('comment').value)

      const response = await axios.post('api/updateTrainingDetails?' + params);
      console.log(response);
      this.$router.push({name: 'timetable'});
    },
  }

}


</script>

<style>
</style>
