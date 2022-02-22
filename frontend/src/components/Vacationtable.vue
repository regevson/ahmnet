<template>

  <div align="center">
    <div v-if="user">
      <h1 style="color: #1b2730; background: #fdd663;">VERFÜGBARE TRAININGS</h1>

      <br>

       <b-form-checkbox
        class="checkedSlotBox"
        style="margin-right: 5px;"
        v-model="isFiltered"
        switch
        size="lg"
        v-on:change="rerenderTrainings(weekNum)"
      >
        <span class="filterLabel">Nur meine Trainings anzeigen</span>
    </b-form-checkbox>

    <button 
      :disabled="checkedSlots.length < 1"
      :class="{disabledBtn: checkedSlots.length < 1}"
      class="freeBulkBtn" 
      style="display: block; margin-bottom: 20px;"
      v-b-modal="'grabDialog'"
      @click="setDialogTxt('grab')"
    >
      Übernehmen
    </button>



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
    async getAvailableTrainings(weekNum) {
      let response;
      response = await axiosReq('availableTrainings?weekNum=' + weekNum);
      this.timetable = response.data
      this.startDate = this.timetable.datesInWeek[0];
      this.endDate = this.timetable.datesInWeek[6];
    },

    async getAvailableTrainingsByTrainer(trainer, weekNum) {
      let response;
      response = await axiosReq('availableTrainingsByTrainer?trainerId=' + trainer.id 
                                                + '&weekNum=' + weekNum);
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
      this.rerenderTrainings(this.weekNum);
    },

    rerenderTrainings(weekNum) {
      if(this.isFiltered)
        this.getAvailableTrainingsByTrainer(this.user, weekNum);
      else
        this.getAvailableTrainings(weekNum);
    },

    async grabTrainings() {
      const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      let params = "trainingIds=" + this.checkedSlots.toString();
      await axiosReq('grabTrainings', params, config);
      this.rerenderTrainings(this.weekNum);
    },

    setDialogTxt(cmd) {
      if(cmd === 'grab')
        this.dialogTxt = "Ausgewählte Trainings übernehmen?";
    },
  },

  computed: {
    isFiltered: {
      get() {
        return this.$store.getters.isFiltered;
      },
      set(isFiltered) {
        this.$store.commit("isFiltered", isFiltered);
      }
    }
  }

}


</script>

<style>
.filterLabel {
  background: #fdd663;
  padding: 3px 5px 3px 5px;
  border-radius: 5px;
  font-weight: bold;
  font-size: 0.8em;
  color: #1b2730;
}

.custom-control-input ~ .custom-control-label::before {
  border-color: gray !important;
}

.custom-control-input:checked ~ .custom-control-label::before {
	color: #fff;
	border-color: #fdd663 !important;
	background-color: #fdd663;
}

.custom-control-input:focus ~ .custom-control-label::before {
  box-shadow: none !important;
}


</style>

