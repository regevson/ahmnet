<template>

  <div align="center">
    <i @click="prevWeek" class="fa-solid fa-circle-arrow-left arrow leftArrow" style="margin-right: 60px;"></i>
    <span style="font-weight: bold;">{{dates[0]}} - {{dates[6]}}</span>
    <i @click="nextWeek" class="fa-solid fa-circle-arrow-right arrow rightArrow" style="margin-left: 60px;"></i>
    <table v-if="trainings" class="timetable table table-responsive">
      <thead>
        <tr align="center">
          <th :class="{activeth: isCurrentDay(1)}" scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[0]}}</span><br>Mo</th>
          <th :class="{activeth: isCurrentDay(2)}" scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[1]}}</span><br>Di</th>
          <th :class="{activeth: isCurrentDay(3)}" scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[2]}}</span><br>Mi</th>
          <th :class="{activeth: isCurrentDay(4)}" scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[3]}}</span><br>Do</th>
          <th :class="{activeth: isCurrentDay(5)}" scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[4]}}</span><br>Fr</th>
          <th :class="{activeth: isCurrentDay(6)}" scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[5]}}</span><br>Sa</th>
          <th :class="{activeth: isCurrentDay(0)}" scope="col"><span style="font-weight:normal; font-size:13px;">{{dates[6]}}</span><br>So</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>
            <Trainingslot :selectedTrainer="trainer" :trainings="trainings[0]"/>
          </td>
          <td>
            <Trainingslot :selectedTrainer="trainer" :trainings="trainings[1]"/>
          </td>
          <td>
            <Trainingslot :selectedTrainer="trainer" :trainings="trainings[2]"/>
          </td>
          <td>
            <Trainingslot :selectedTrainer="trainer" :trainings="trainings[3]"/>
          </td>
          <td>
            <Trainingslot :selectedTrainer="trainer" :trainings="trainings[4]"/>
          </td>
          <td>
            <Trainingslot :selectedTrainer="trainer" :trainings="trainings[5]"/>
          </td>
          <td>
            <Trainingslot :selectedTrainer="trainer" :trainings="trainings[6]"/>
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
    selectedTrainer: Object,
    isVacationTable: Boolean,
  },

  data() {
    return {
      trainings: null,
      trainer: null,
      dates: [],
    }
  },

  watch: {
    selectedTrainer: function() {
      this.trainer = this.selectedTrainer;
      this.getWeek(this.weekNum);
    }
  },

  created() {
    if(!this.isVacationTable)
      this.trainer = this.selectedTrainer;
    this.getWeek(this.weekNum);
  },

  methods: {
    async getWeek(weekNum) {
      let response;
      let timetable;
      if(this.isVacationTable) {
        response = await axios.get('api/availableTrainings?weekNum=' + this.weekNum);
        timetable = response.data
      }
      else {
        response = await axios.get('api/trainingsByWeek?trainer=' + this.trainer.id + '&weekNum=' + weekNum);
        timetable = response.data
      }
      this.trainings = timetable.trainings;
      this.dates = timetable.datesInWeek;
    },
    prevWeek() {
      this.getWeek(--this.weekNum);
    },
    nextWeek() {
      this.getWeek(++this.weekNum);
    },
    isCurrentDay(day_idx) { // 0 -> sunday, 1 -> monday, ...
      const d = new Date();
      return d.getDay() == day_idx;
    },
  },

  computed: {
    weekNum: {
      get() {
        return this.$store.getters["weekNum"];
      },
      set(weekNum) {
        return this.$store.commit("weekNum", weekNum);
      }
    },

  },

}


</script>

<style>
.timetable td {
  border-top: none !important;
}
.timetable th {
  min-width: 86px;
  border-bottom: 2px solid #4b9183 !important;
  border-top: 2px solid #4b9183 !important;
}
.timetable .activeth {
  background: #4b9183 !important;
  color: white;
}

</style>
