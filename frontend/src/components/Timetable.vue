<template>
  <div>
    <div v-if="user" align="center">
      <h1>MEINE TRAININGS</h1>

      <div align="center">
        <button class="newBtn" @click="createTraining">
          Neues Training
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

      <div v-if="user.roles.includes('ADMIN')" align="left">
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

      <DateBar
        :startDate="startDate" 
        :endDate="endDate" 
        @dateChanged="changeDate" 
      />

      <Table
        :userIsAdmin="user.roles.includes('ADMIN')"
        :selectedTrainerId="selectedTrainer.id"
        :highlight="highlight"
        :timetable="timetable"
        :selectedDate="selectedDate"
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


    <h5 v-if="!user" class="loading">LOADING...</h5>
  </div>
</template>

<script>
import DateBar from "./DateBar";
import Table from "./Table";
import Multiselect from 'vue-multiselect'
import { axiosReq } from '../axios'

export default {
  name: 'Timetable',
  components: {Multiselect, DateBar, Table},
  props: {
    user: Object
  },

  data() {
    return {
      allTrainers: [],
      timetable: null,
      selectedDate: new Date(),
      weekNum: null,
      checkedSlots: [],
      dialogTxt: '',
      startDate: null,
      endDate: null,
      highlight: false,
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

    async getTrainings(weekNum) {
      let response;
      response = await axiosReq('trainingsByWeek?trainer='
                            + this.selectedTrainer.id + '&weekNum=' + weekNum);
      this.updateData(response.data);
    },

    updateData(data) {
      this.timetable = data;
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
        await this.getTrainings(this.weekNum);
      }
      this.highlight = selectedDate.trigger == 'calendar' ? true : false;
      this.selectedDate = selectedDate.date;
    },

    async deleteTrainings() {
      const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      let params = "trainingIds=" + this.checkedSlots.toString();
      await axiosReq('deleteTrainings', params, config);
      this.getTrainings(this.weekNum);
    },

    async freeTrainings() {
      const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      let params = "trainingIds=" + this.checkedSlots.toString();
      await axiosReq('freeTrainings', params, config);
      this.getTrainings(this.weekNum);
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
        if(this.$store.getters["selectedTrainer"] == null) {
          let user = JSON.parse(sessionStorage.user);
          this.$store.commit("selectedTrainer", user);
        }
        return this.$store.getters["selectedTrainer"];
      },
      set(selectedTrainer) {
        this.$store.commit("selectedTrainer", selectedTrainer);
        this.getTrainings(this.weekNum);
      }
    },

  },

}
</script>

