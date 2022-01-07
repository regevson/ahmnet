<template>
  <div align="left">

    <form @submit.prevent="updateTrainingDetails">

      <p>Club: {{training.club.name}}</p>
      <p>Datum: {{training.date}}</p>
      <p>Zeit: {{training.timeslot}}</p>
      <p>Dauer: {{training.durationMinutes}}</p>

      <p>Trainer: {{trainingGroup.trainer.firstName}} {{trainingGroup.trainer.lastName}}</p>

      <p>Anwesenheit Spieler:</p>
      <div class="form-check">
        <div v-for="player in trainingGroup.players" :key="player.id">
          <input class="form-check-input" type="checkbox" value="" id="flexCheckChecked" checked>
          <label class="form-check-label" for="flexCheckChecked">
            <span> {{player.firstName}} {{player.lastName}} </span>
          </label>
        </div>
      </div>

      <br>


      <p>Schwerpunkte:</p>
      <textarea id="bullet" v-model="bulletpoints" rows="4">
      </textarea>
      <br>
      <p>Kommentare:</p>
      <textarea id="comment" v-model="comments" rows="4">
      </textarea>
      <br>

      <input type="submit" class="fadeIn fourth" value="Anpassen" style="margin-top: 30px">
    </form>


  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'TrainingDetails',

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
    }
  },

  mounted() {
    /*
    window.addEventListener('beforeunload', function(event) {
      event.returnValue = 'Write something'
    })
    */
  },

  methods: {
    async updateTrainingDetails() {
      const params = new URLSearchParams()
      params.append('id', this.training.id)
      params.append('bulletPoints', document.getElementById('bullet').value)
      params.append('comments', document.getElementById('comment').value)

      const accessToken = 'Bearer ' + localStorage.getItem('accessToken');
      const config = {
        headers: {
          'Authorization': accessToken
        }
      }

      const response = await axios.post('http://10.0.0.242:8080/api/updateTrainingDetails?' + params, config);
      console.log(response);
      this.$router.push('/home');
    }
  }

}


</script>

<style>
</style>
