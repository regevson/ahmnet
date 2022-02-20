<template>
  <div>
    <div v-if="user && selectedTrainer" align="center">
      <h1>MEINE TRAININGS</h1>

      <div align="center">
        <button class="newBtn" @click="createTraining">
          Neues Training erstellen
        </button>

        <button 
          v-if="checkedSlots.length > 0" 
          class="deleteBulkBtn" 
          style="margin-left: 5px;" 
          v-b-modal="'deleteDialog'"
          @click="setDialogTxt('delete')"
        >
          Löschen
        </button>

        <button 
          v-if="checkedSlots.length > 0" 
          class="freeBulkBtn" 
          style="margin-left: 5px;"
          v-b-modal="'freeDialog'"
          @click="setDialogTxt('free')"
        >
          Freigeben
        </button>
      </div>
      <br>

      <div v-if="hasRole('ADMIN')" align="left">
        <p class="entry">TrainerIn auswählen:</p>
        <multiselect
          v-model="selectedTrainer"
          :options="allTrainers"
          placeholder="Trainer auswählen"
          label="fullName"
          track-by="fullName"
          deselectLabel=""
          selectLabel=""
        />
        <br>
      </div>

      <Table 
        :selectedTrainer="selectedTrainer" 
        :isVacationTable="false"
        @checkedSlots="setCheckedSlots"
      />
    </div>


    <b-modal
      centered
      @ok="deleteTrainings"
      id="deleteDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >

    <b-modal
      centered
      @ok="freeTrainings"
      id="freeDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >




    <h5 v-if="!user || !selectedTrainer" class="loading">LOADING...</h5>
  </div>
</template>

<script>
import Table from "./Table";
import Multiselect from 'vue-multiselect'
import { axiosReq } from '../axios'
//import qs from 'qs'

export default {
  name: 'Timetable',
  components: {Multiselect, Table},
  props: {
    user: Object
  },

  data() {
    return {
      allTrainers: [],
      checkedSlots: [],
      dialogTxt: '',
    }
  },

  created() {
    this.getAllTrainers();
  },

  methods: {
    createTraining() {
      this.$router.push({name: 'trainingdetails', params: {trainingId: -1}});
    },

    async getAllTrainers() {
      const response = await axiosReq('allTrainers');
      this.allTrainers = response.data;
    },

    hasRole: function(role) {
      return this.user.roles.includes(role);
    },

    setCheckedSlots(checkedSlots) {
      this.checkedSlots = checkedSlots; 
    },

    async deleteTrainings() {
      const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      let params = "trainingIds=" + this.checkedSlots.toString();
      await axiosReq('deleteTrainings', params, config);
      this.$router.go();
    },

    async freeTrainings() {
      const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      let params = "trainingIds=" + this.checkedSlots.toString();
      await axiosReq('freeTrainings', params, config);
      this.$router.go();
    },

    setDialogTxt(cmd) {
      if(cmd === 'delete')
        this.dialogTxt = "Ausgewählte Trainings löschen?";
      else if(cmd === 'free')
        this.dialogTxt = "Ausgewählte Trainings freigeben?";
    },

  },

  computed: {
    selectedTrainer: {
      get() {
        if(this.$store.getters["selectedTrainer"] == null)
          this.$store.commit("selectedTrainer", this.user);
        return this.$store.getters["selectedTrainer"];
      },
      set(selectedTrainer) {
        return this.$store.commit("selectedTrainer", selectedTrainer);
      }
    },

  },

}
</script>

