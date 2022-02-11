<template>

  <div align="center">
    <h1>DEINE STUNDENTAFEL</h1>

    <div align="center">
      <button class="newBtn" @click="createTraining" >Neues Training erstellen</button>
    </div>

    <i @click="prevWeek" class="fa-solid fa-circle-arrow-left arrow leftArrow" style="margin-right: 60px;"></i>
    <span style="font-weight: bold;">{{dates[0]}} - {{dates[6]}}</span>
    <i @click="nextWeek" class="fa-solid fa-circle-arrow-right arrow rightArrow" style="margin-left: 60px;"></i>
    <table v-if="trainings" class="timetable table table-responsive">
      <thead>
        <tr align="center">
          <th scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[0]}}</span><br>Mo</th>
          <th scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[1]}}</span><br>Di</th>
          <th scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[2]}}</span><br>Mi</th>
          <th scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[3]}}</span><br>Do</th>
          <th scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[4]}}</span><br>Fr</th>
          <th scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[5]}}</span><br>Sa</th>
          <th scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[6]}}</span><br>So</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>
            <Trainingslot :trainings="trainings.MONDAY"/>
          </td>
          <td>
            <Trainingslot :trainings="trainings.TUESDAY"/>
          </td>
          <td>
            <Trainingslot :trainings="trainings.WEDNESDAY"/>
          </td>
          <td>
            <Trainingslot :trainings="trainings.THURSDAY"/>
          </td>
          <td>
            <Trainingslot :trainings="trainings.FRIDAY"/>
          </td>
          <td>
            <Trainingslot :trainings="trainings.SATURDAY"/>
          </td>
          <td>
            <Trainingslot :trainings="trainings.SUNDAY"/>
          </td>
        </tr>
      </tbody>
    </table>

    <h3 v-if="!trainings">Oops... sth. went wrong!</h3> 

  </div>

</template>

<script>
import Trainingslot from "./Trainingslot";
import axios from 'axios'

export default {
  name: 'Timetable',
  components: {Trainingslot},
  props: {
    user: Object
  },

  data() {
    return {
      trainings: null,
      dates: [],
      weekNum: 0,
    }
  },

  created() {
    this.calcCurrentWeekNum();
    this.getWeek(this.weekNum);
  },

  methods: {
    calcCurrentWeekNum() {
      let currentDate = new Date();
      var oneJan = new Date(currentDate.getFullYear(),0,1);
      var numberOfDays = Math.floor((currentDate - oneJan) / (24 * 60 * 60 * 1000));
      this.weekNum = Math.ceil(( currentDate.getDay() + 1 + numberOfDays) / 7) - 1;
    },
    async getWeek(weekNum) {
      const trainer = localStorage.getItem('username');
      const response = await axios.get('api/trainingsByWeek?trainer=' + trainer + '&weekNum=' + weekNum);
      const timetable = response.data
      this.trainings = timetable.trainings;
      this.dates = timetable.datesInWeek;
    },
    createTraining() {
      this.$router.push({name: 'trainingdetails', params: {trainingId: -1}});
    },
    prevWeek() {
      this.getWeek(--this.weekNum);
    },
    nextWeek() {
      this.getWeek(++this.weekNum);
    },
  }

}


</script>

<style>
.timetable td {
  border-top: none !important;
}
.timetable th {
  min-width: 86px;
}

</style>

