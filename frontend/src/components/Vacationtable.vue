<template>

  <div align="center">
    <div v-if="user">
      <h1>VERFÜGBARE TRAININGS</h1>

        <button 
          v-if="checkedSlots.length > 0" 
          class="freeBulkBtn" 
          style="display: block"
          v-b-modal="'grabDialog'"
          @click="setDialogTxt('grab')"
        >
          Übernehmen
        </button>
        <br>

      <Table 
        :trainer="user"
        :isVacationTable="true"
        @checkedSlots="setCheckedSlots"
      />
    </div>

    <b-modal
      centered
      @ok="grabTrainings"
      id="grabDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >

    <h5 v-if="!user" class="loading">LOADING...</h5>

  </div>

</template>



<script>
import Table from "./Table";
import { axiosReq } from '../axios'

export default {
  name: 'VacationTable',
  components: {Table},
  props: {
    user: Object
  },

  data() {
    return {
      checkedSlots: [],
      dialogTxt: '',
    }
  },

  methods: {
    setCheckedSlots(checkedSlots) {
      this.checkedSlots = checkedSlots; 
    },

    async grabTrainings() {
      const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      let params = "trainingIds=" + this.checkedSlots.toString();
      await axiosReq('grabTrainings', params, config);
      this.$router.go();
    },

    setDialogTxt(cmd) {
      if(cmd === 'grab')
        this.dialogTxt = "Ausgewählte Trainings übernehmen?";
    },
  }

}


</script>

<style>

</style>

