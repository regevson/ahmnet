<template>
  <div align="center">

      <b-form-datepicker
        v-model="date"
        locale="de"
        :showDecadeNav="false"
        :start-weekday="1"
        :hide-header="true"
        size="sm"
        button-only
        label-help=""
        class="btnOnlyPicker"
      />
      <br>

    <i
      @click="prevWeek"
      class="fa-solid fa-circle-arrow-left arrow leftArrow"
      style="margin-right: 60px"
    ></i>
    <span v-if="dates.length == 7" style="font-weight: bold">{{convDateToGerman(dates[0])}} - {{convDateToGerman(dates[6])}}</span>
    <i
      @click="nextWeek"
      class="fa-solid fa-circle-arrow-right arrow rightArrow"
      style="margin-left: 60px"
    ></i>
    <table v-if="dates.length == 7" class="timetable table table-responsive">
      <thead>
        <tr align="center">
          <th :ref="'thday' + day_idx" scope="col" :class="{activeth: isCurrentDay(day_idx)}" v-for="(dayName, day_idx) in weekDays" :key="day_idx">
            <span style="font-weight: normal; font-size: 13px">{{convDateToGerman(dates[day_idx])}}</span><br/>{{dayName}}
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="trainings">
          <td v-for="idx in 7" :key="idx" :class="{activecol: isCurrentDay(idx-1), selectedcol: isSelectedDay(idx-1)}" >

            <Trainingslot
              :selectedTrainer="selectedTrainer"
              :trainings="trainings[idx-1]"
            />
          </td>
        </tr>
      </tbody>
    </table>

    <h5 v-if="!trainings" class="loading">LOADING...</h5>
  </div>
</template>

<script>
import Trainingslot from "./Trainingslot";
import { axiosReq } from '../axios'

export default {
  name: 'Table',
  components: {Trainingslot},
  props: {
    selectedTrainer: Object,
    isVacationTable: Boolean,
  },

  data() {
    return {
      trainings: null,
      weekDays: ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'],
      dates: [],
      date: '', 
    }
  },

  watch: {
    selectedTrainer: function() {
      this.getTrainingsByWeekNum(this.weekNum);
    },

    date: async function(date) {
      if(date === '') 
        return;
      date = new Date(date);
      this.weekNum = this.calcWeekNum(date);
      console.log(this.weekNum);
      await this.getTrainingsByWeekNum(this.weekNum);
      this.highlightChosenDay(date);
    }
  },

  mounted() {
    this.getTrainingsByWeekNum(this.weekNum);
    this.highlightChosenDay(new Date());
  },

  methods: {
    calcWeekNum(date) {
      console.log(date);
      const onejan = new Date(date.getFullYear(), 0, 1);
      return Math.ceil((((date.getTime() - onejan.getTime()) / 86400000) + onejan.getDay()-1) / 7) - 1;
    },

    async getTrainingsByWeekNum(weekNum) {
      let response;
      if(this.isVacationTable)
        response = await axiosReq('availableTrainings?weekNum=' + this.weekNum);
      else
        response = await axiosReq('trainingsByWeek?trainer='
                              + this.selectedTrainer.id + '&weekNum=' + weekNum);
      if(response == null)
        return;

      const timetable = response.data
      this.trainings = timetable.trainings;
      this.dates = timetable.datesInWeek;
    },

    prevWeek() {
      this.getTrainingsByWeekNum(--this.weekNum);
      this.date = '';
    },

    nextWeek() {
      this.getTrainingsByWeekNum(++this.weekNum);
      this.date = '';
    },

    isCurrentDay(day_idx) {
      const dayDate = new Date(this.dates[day_idx]);
      const today = new Date();
      return this.isToday(dayDate) && this.getDay(today) == day_idx;
    },

    isToday(someDate) {
      const today = new Date();
      return someDate.getDate() == today.getDate() && someDate.getMonth() == today.getMonth() && someDate.getFullYear() == today.getFullYear();
    },

    isSelectedDay(day_idx) { // 0 -> monday, ..., 6 -> sunday 
      const date = new Date(this.date);
      return this.getDay(date) == day_idx;
    },

    async highlightChosenDay(chosenDate) {
      if(this.dates.length != 7)
        return;
      let th = this.$refs["thday" + this.getDay(chosenDate)][0];
      th.scrollIntoView({block: 'center'});
      // let it blink
      for(let i = 0; i < 3; i++) {
        await this.blink(th, "#e1dddd");
        await this.blink(th, "white");
      }
    },

    blink(obj, bg) {
      return new Promise((resolve) => {
        setTimeout(() => {obj.style.background = bg; resolve();}, 100);
      })
    },

    getDay(date) {
      return (date.getDay() + 6) % 7;
    },

    convDateToGerman(date) {
      let d = date.split("-");
      return d[2] + "-" + d[1] + "-" + d[0];
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
  border-right: 2px solid #4b9183;
  border-left: 2px solid #4b9183;
  color: white;
}

.timetable .activecol {
  background: #0080002b;
  border-right: 2px solid #4b9183;
  border-left: 2px solid #4b9183;
  box-sizing: border-box;
}

.timetable .selectedcol {
  background: #8080803d;
  border-right: 2px solid gray;
  border-left: 2px solid gray;
}

.btnOnlyPicker .btn-secondary {
  background-color: #4b9183 !important;
  border-radius: 5px;
  width: 100% !important;
  border: none !important;
  padding: 3px 10px 5px 10px;
}

/* date-button */
.btnOnlyPicker .dropdown-menu {
  transform: translate3d(-126px, 34px, 0px) !important;
  outline: none !important;
}

/* dates-grid inside datepicker */
.b-calendar-grid {
  padding: 0 10px 0 10px !important;
}

/* selected date circle */
.btn-primary:not(:disabled):not(.disabled).active {
 background: #4b9183; 
 outline: none;
}

/* all date-circles */
.b-calendar-grid-body .btn {
}


</style>

