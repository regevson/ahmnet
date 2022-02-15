<template>
<div>

  <div v-if="selectedTrainer" align="center">
    <h1>DEINE STUNDENTAFEL</h1>

    <div align="center">
      <button class="newBtn" @click="createTraining" >Neues Training erstellen</button>
    </div>

    <div v-if="hasRole('TRAINER')" align="left">
      <p class="entry">TrainerIn auswählen:</p>
      <multiselect v-model="selectedTrainer" :options="allTrainer" placeholder="Trainer auswählen" label="fullName" track-by="fullName" deselectLabel="" selectLabel=""/>
    </div>
    <br>
    
    <Table :selectedTrainer="selectedTrainer" :isVacationTable="false"/>

  </div>

  <h3 v-if="!selectedTrainer">Oops... sth. went wrong!</h3> 
</div>
</template>

<script>
import Table from "./Table";
import Multiselect from 'vue-multiselect'
import axios from 'axios'

export default {
  name: 'Timetable',
  components: {Multiselect, Table},
  props: {
    user: Object
  },

  data() {
    return {
      allTrainer: [],
    }
  },

  created() {
    this.getAllTrainer();
  },

  methods: {
    createTraining() {
      this.$router.push({name: 'trainingdetails', params: {trainingId: -1}});
    },
    async getAllTrainer() {
      const response = await axios.get('api/allTrainer');
      this.allTrainer = response.data;
    },
    hasRole: function(role) {
      return this.user.roles.includes(role);
    }
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
