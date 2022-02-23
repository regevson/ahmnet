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
        <span class="filterLabel">Eigene Trainings ausblenden</span>
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
      :userIsAdmin="user.roles.includes('ADMIN')"
      :selectedTrainerId="user.id"
      :highlight="highlight"
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
      highlight: true,
    }
  },

  methods: {
    async getAvailableTrainings(weekNum) {
      let response;
      response = await axiosReq('availableTrainings?weekNum=' + weekNum);
      this.updateData(response.data);
    },

    async getAvailableTrainingsByTrainer(trainer, weekNum) {
      let response;
      response = await axiosReq('availableTrainingsByExcluding?trainerId=' + trainer.id 
                                                + '&weekNum=' + weekNum);
      this.updateData(response.data);
    },

    updateData(data) {
      this.timetable = data;
      console.log('timetable set');
      this.startDate = this.timetable.datesInWeek[0];
      this.endDate = this.timetable.datesInWeek[6];
    },

    setCheckedSlots(checkedSlots) {
      this.checkedSlots = checkedSlots; 
    },

    async changeDate(selectedDate) {
      let newWeekNum = this.$funcs.calcWeekNum(selectedDate.date);
      if(this.weekNum != newWeekNum) {
        this.weekNum = newWeekNum;
        await this.rerenderTrainings(this.weekNum);
      }
      this.highlight = selectedDate.trigger == 'calendar' ? true : false;
      this.selectedDate = selectedDate.date;
      console.log('seldate set');
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

