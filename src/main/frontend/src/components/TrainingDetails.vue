<template>
  <div align="left">

    <form v-if="training" @submit.prevent="">

      Club:
      <multiselect v-model="club" :options="allClubs" placeholder="Auswählen/Suchen" label="name" track-by="name" deselectLabel="" selectLabel="" />
      <br>

      Gruppe:
      <multiselect v-model="group" :options="allGroups" placeholder="Auswählen/Suchen" label="combinedInfo" track-by="combinedInfo" deselectLabel="" selectLabel="" />
      <br>

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
            :start-time="10"
            >
      </b-form-timepicker>
      <br>


      <p>Dauer: {{training.durationMinutes}}</p>
      <b-form-input type="number" v-model="training.durationMinutes" placeholder="Dauer in Minuten"></b-form-input>
      <br>

      <p>Court:</p>
      <b-form-input v-model="training.court" placeholder="Platz 3"></b-form-input>
      <br>


      TrainerIn:
      <multiselect v-model="trainer" :options="allTrainer" placeholder="Select one" label="fullName" track-by="fullName" deselectLabel="" selectLabel=""/>
      <br>

      <div v-if="training.id != -1">
        <p>Anwesenheit Spieler:</p>
        <div class="form-check">
          <div v-for="player in training.players" :key="player.id">
            <input class="form-check-input" type="checkbox" :value="player.id" :id="player.id" v-model="training.attendees">
            <label class="form-check-label" :for="player.id">
              <span> {{player.firstName}} {{player.lastName}} </span>
            </label>
          </div>
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

      <div align="center" v-if="training.id == -1">
        <input class="changeBtn fourth" type="submit" @click="updateTrainingDetails" value="Erstellen">
      </div>

      <div align="center" v-if="training.id != -1">
        <input class="changeBtn fourth" type="submit" @click="updateTrainingDetails" value="Anpassen">
        <button v-if="training.id != -1" @click="deleteTraining" class="deleteBtn">Löschen</button>
      </div>


      
    </form>



    <h3 v-if="!training">Oops... sth. went wrong!</h3>

  </div>
</template>

<script>
import axios from 'axios'
import Multiselect from 'vue-multiselect'

export default {
  name: 'TrainingDetails',
  components: {Multiselect},
  data() {
    return {
      training: null,
      allClubs: [],
      club: null,
      allGroups: [],
      group: null,
      allTrainer: [],
      trainer: null,
    }
  },

  async created() {
    let response;

    if(this.$route.params.trainingId == -1) { // create new training
      response = await axios.get('api/newTraining'); // fetch an empty new training
      this.training = response.data;
      //set defaults
      this.training.startTime = "10:30";
      this.training.durationMinutes = 60;
    }
    else {
      response = await axios.get('api/training?id=' + this.$route.params.trainingId);
      // set defaults
      this.training = response.data;
      this.club = this.training.club;
      this.trainer = this.training.trainer;
      this.group = this.combineGroupInfo(this.training.group);
    }

    response = await axios.get('api/allClubs');
    this.allClubs = response.data;

    response = await axios.get('api/allGroups');
    this.allGroups = response.data;
    this.allGroups.map(this.combineGroupInfo);

    response = await axios.get('api/allTrainer');
    this.allTrainer = response.data;
  },

  methods: {
    combineGroupInfo(group) {
      let fullNames = [];
      group.players.forEach((player) => { fullNames.push(player.fullName); });
      group.combinedInfo = 'Gruppe' + group.id + ': (' + group.club.name + ') [' + fullNames + ']';
      return group;
    },
    removeCombinedGroupInfo(group) {
      delete group.combinedGroupInfo
      return group;
    },

    updateTraining() {
      this.training.group = this.removeCombinedGroupInfo(this.group);
      this.training.players = this.players;
      this.training.club = this.club;
      this.training.trainer = this.trainer;
    },

    async updateTrainingDetails() {
      this.updateTraining();

      const config = {headers: {'Content-Type': 'application/json'}}
      let params = JSON.stringify(this.training);

      const response = await axios.post('api/updateTrainingDetails', params, config);
      console.log(response);
      this.$router.push({name: 'timetable'});
    },

    async deleteTraining() {
      const response = await axios.get('api/deleteTraining?id=' + this.training.id);
      console.log(response);
      this.$router.push({name: 'timetable'});
    }

  }


}


</script>
