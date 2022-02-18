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
    <span style="font-weight: bold">{{dates[0]}} - {{dates[6]}}</span>
    <i
      @click="nextWeek"
      class="fa-solid fa-circle-arrow-right arrow rightArrow"
      style="margin-left: 60px"
    ></i>
    <table class="timetable table table-responsive">
      <thead>
        <tr align="center">
          <th :ref="'day' + day_idx" :class="{activeth: isCurrentDay(day_idx)}" scope="col" v-for="(dayName, day_idx) in weekDays" :key="day_idx">
            <span style="font-weight: normal; font-size: 13px">{{dates[day_idx]}}</span><br/>{{dayName}}
          </th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="trainings">
          <td v-for="idx in 7" :key="idx">
            <Trainingslot
              :selectedTrainer="trainer"
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
      trainer: null,
      weekDays: ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'],
      dates: [],
      date: '', 
    }
  },

  watch: {
    selectedTrainer: function() {
      this.trainer = this.selectedTrainer;
      this.getTrainingsByWeekNum(this.weekNum);
    },

    date: async function(date) {
      date = this.convertToDate(date);
      this.weekNum = this.calcWeekNum(date);
      await this.getTrainingsByWeekNum(this.weekNum);
      this.highlightChosenDay(date);
    }
  },

  mounted() {
    this.trainer = this.selectedTrainer;
    this.getTrainingsByWeekNum(this.weekNum);
    this.highlightChosenDay(new Date());
  },

  methods: {
    calcWeekNum(date) {
      const onejan = new Date(date.getFullYear(), 0, 1);
      return Math.ceil((((date.getTime() - onejan.getTime()) / 86400000) + onejan.getDay()) / 7) - 1;
    },

    convertToDate(dateStr) {
      var parts = dateStr.split("-");
      return new Date(parseInt(parts[0], 10), parseInt(parts[1], 10) - 1, parseInt(parts[2], 10));
    },

    async getTrainingsByWeekNum(weekNum) {
      let response;
      if(this.isVacationTable)
        response = await axiosReq('availableTrainings?weekNum=' + this.weekNum);
      else
        response = await axiosReq('trainingsByWeek?trainer='
                              + this.trainer.id + '&weekNum=' + weekNum);
      if(response == null)
        return;

      const timetable = response.data
      this.trainings = timetable.trainings;
      this.dates = timetable.datesInWeek;
    },

    prevWeek() {
      this.getTrainingsByWeekNum(--this.weekNum);
    },

    nextWeek() {
      this.getTrainingsByWeekNum(++this.weekNum);
    },

    isCurrentDay(day_idx) { // 0 -> monday, ..., 6 -> sunday 
      const d = new Date();
      return this.getDay(d) == day_idx;
    },

    async highlightChosenDay(chosenDate) {
      let obj = this.$refs["day" + this.getDay(chosenDate)][0];
      obj.scrollIntoView(true);
      // let it blink
      for(let i = 0; i < 3; i++) {
        await this.blink(obj, "#e1dddd");
        await this.blink(obj, "white");
      }
    },

    blink(obj, bg) {
      return new Promise((resolve) => {
        setTimeout(() => {obj.style.background = bg; resolve();}, 100);
      })
    },

    getDay(date) {
      return (date.getDay() + 6) % 7;
    }

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

