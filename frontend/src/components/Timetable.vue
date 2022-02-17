<template>
  <div>
    <div v-if="user && selectedTrainer" align="center">
      <h1>MEINE TRAININGS</h1>

      <div align="center">
        <button class="newBtn" @click="createTraining">
          Neues Training erstellen
        </button>
      </div>

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
      </div>
      <br />

      <Table :selectedTrainer="selectedTrainer" :isVacationTable="false" />
    </div>

    <h5 v-if="!user || !selectedTrainer" class="loading">LOADING...</h5>
  </div>
</template>

<script>
import Table from "./Table";
import Multiselect from 'vue-multiselect'
import { axiosReq } from '../axios'

export default {
  name: 'Timetable',
  components: {Multiselect, Table},
  props: {
    user: Object
  },

  data() {
    return {
      allTrainers: [],
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

