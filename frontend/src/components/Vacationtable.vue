<template>

  <div align="center">
    <div v-if="user">
      <h1>VERFÜGBARE TRAININGS</h1>

          <button 
            :disabled="checkedSlots.length < 1"
            :class="{disabledBtn: checkedSlots.length < 1}"
            class="freeBulkBtn" 
            style="display: block;"
            v-b-modal="'grabDialog'"
            @click="setDialogTxt('grab')"
          >
            Übernehmen
          </button>
          <br>


    <DateBar
      :startDate="startDate" 
      :endDate="endDate" 
      @dateChanged="changeDate" 
    />

    <Table
      :isAdmin="user.roles.includes('ADMIN')"
      :timetable="timetable"
      :selectedDate="selectedDate"
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
import DateBar from "./DateBar";
import Table from "./Table";
import { axiosReq } from '../axios'

export default {
  name: 'VacationTable',
  components: {DateBar, Table},
  props: {
    user: Object
  },

  data() {
    return {
      timetable: null,
      selectedDate: new Date(),
      weekNum: null,
      checkedSlots: [],
      dialogTxt: '',
      startDate: null,
      endDate: null,
    }
  },

  methods: {
    async getTrainings(weekNum) {
      let response;
      response = await axiosReq('availableTrainings?weekNum=' + weekNum);
      if(response == null)
        return;

      this.timetable = response.data
      this.startDate = this.timetable.datesInWeek[0];
      this.endDate = this.timetable.datesInWeek[6];
    },

    setCheckedSlots(checkedSlots) {
      this.checkedSlots = checkedSlots; 
    },

    changeDate(selectedDate) {
      this.selectedDate = selectedDate;
      this.weekNum = this.$funcs.calcWeekNum(this.selectedDate);
      this.getTrainings(this.weekNum);
    },

    async grabTrainings() {
      const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      let params = "trainingIds=" + this.checkedSlots.toString();
      await axiosReq('grabTrainings', params, config);
      this.getTrainings(this.weekNum);
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

