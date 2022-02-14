<template>
  <div align="left">

    <form v-if="training" @submit.prevent="">

      <p class="entry">Club:</p>
      <multiselect v-model="club" :options="allClubs" placeholder="Club suchen" label="name" track-by="name" deselectLabel="" selectLabel="" />
      <br>

      <p class="entry">Gruppe:</p>
      <multiselect v-model="group" :options="allGroups" placeholder="Gruppe suchen" label="combinedInfo" track-by="combinedInfo" deselectLabel="" selectLabel="" />
      <br>

      <p class="entry">Datum:</p>
      <b-form-datepicker v-model="training.date" class="mb-2 detailsInput"
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

      <p class="entry">Zeit:</p>
      <b-form-timepicker v-model="training.startTime" class="detailsInput"
            locale="de"
            :minutes-step="15"
            :hide-header="true"
            :start-time="10"
            >
      </b-form-timepicker>
      <br>


      <p class="entry">Dauer:</p>
      <b-form-input type="number" class="detailsInput" v-model="training.durationMinutes" placeholder="Dauer in Minuten"></b-form-input>
      <br>

      <p class="entry">Court:</p>
      <b-form-input v-model="training.court" class="detailsInput"></b-form-input>
      <br>


      <p class="entry">TrainerIn:</p>
      <multiselect v-model="trainer" :options="allTrainer" placeholder="TrainerIn suchen" label="fullName" track-by="fullName" deselectLabel="" selectLabel=""/>
      <br>

      <div v-if="training.id != -1">
        <p class="entry">Anwesenheit SpielerInnen:</p>
        <div style="margin-top: 10px" class="form-check">
          <div v-for="player in training.players" :key="player.id">
            <input class="form-check-input" type="checkbox" :value="player.id" :id="player.id" v-model="training.attendees">
            <label class="form-check-label" :for="player.id">
              <span> {{player.firstName}} {{player.lastName}} </span>
            </label>
          </div>
        </div>
      </div>
      <br>

      <p class="entry">Schwerpunkte:</p>
      <textarea v-model="training.bulletPoints" rows="4" style="width: 100%; padding: 5px;" class="detailsInput">
      </textarea>
      <br>

      <p class="entry">Kommentare:</p>
      <textarea v-model="training.comments" rows="4" style="width: 100%; padding: 5px;" class="detailsInput">
      </textarea>
      <br>

      <div align="center" v-if="training.id == -1">
        <input class="changeBtn fourth" type="submit" @click="updateTrainingDetails" value="Erstellen">
      </div>

      <div align="center" v-if="training.id != -1">
        <input class="changeBtn fourth" type="submit" @click="updateTrainingDetails" value="Anpassen">
        <button @click="deleteTraining" class="deleteBtn">Löschen</button>
        <button v-if="isFreeable(training) == true" @click="freeTraining" class="freeBtn">Freigeben</button>
        <button v-if="isFreeable(training) == false" @click="grabTraining" class="freeBtn">Übernehmen</button>
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
      this.training.free = false;
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
    },

    async freeTraining() {
      const response = await axios.get('api/freeTraining?id=' + this.training.id);
      console.log(response);
      this.$router.push({name: 'timetable'});
    },

    async grabTraining() {
      const response = await axios.get('api/grabTraining?id=' + this.training.id);
      console.log(response);
      this.$router.push({name: 'timetable'});
    },

    isFreeable(training) {
      return !training.free;
    },

  }


}


</script>

<style>
.entry {
  background: #499082;
  color: white;
  font-weight: bold;
  padding: 10px 5px 10px 5px;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  margin-bottom: 0px;
}

</style>
