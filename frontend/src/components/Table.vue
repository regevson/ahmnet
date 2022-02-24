<template>

<div class="row justify-content-center" style="--bs-gutter-x: 0;">
    <div class="col-auto">

    <table ref="tab" v-if="timetable" class="timetable table table-responsive">

      <thead>
        <tr align="center">
          <th 
            :ref="'thday' + dayIdx" 
            scope="col" 
            :class="{activeth: isCurrentDay(dayIdx)}" 
            v-for="(dayName, dayIdx) in weekDays" :key="dayIdx"
          >

            <span style="font-weight: normal; font-size: 13px">
              {{convDateToGerman(timetable.datesInWeek[dayIdx])}}
            </span>
            <br/>
            {{dayName}}
          </th>
        </tr>
      </thead>

      <tbody>
        <tr v-if="timetable.trainings.length > 0">
          <td 
            v-for="idx in 7" 
            :key="idx" 
            :class="{activecol: isCurrentDay(idx-1), 
                     selectedcol: isSelectedDay(idx-1)}"
            >
            <Trainingslot
              :userIsAdmin="userIsAdmin"
              :selectedTrainerId="selectedTrainerId"
              :trainings="timetable.trainings[idx-1]"
              @checkedSlot="changeChecked"
            />
          </td>
        </tr>
      </tbody>

    </table>

    <h5 v-if="!timetable" class="loading">LOADING...</h5>

  </div>
  </div>
</template>

<script>
import Trainingslot from "./Trainingslot";

export default {
  name: 'Table',
  components: {Trainingslot},
  props: {
    userIsAdmin: Boolean,
    selectedTrainerId: String,
    timetable: Object,
    selectedDate: Date,
    highlight: Boolean,
  },

  data() {
    return {
      weekDays: ['Mo', 'Di', 'Mi', 'Do', 'Fr', 'Sa', 'So'],
      checkedSlots: [],
      highlightLocal: false,
    }
  },

  watch: {
    timetable: function() {
      this.checkedSlots = []; // reset checkboxes when table is rerendered
      this.highlightLocal = false; // never highlight anything when timetable is updated
    },
    selectedDate: function() {
      this.highlightLocal = this.highlight; // only highlight when date is updated and highlight is set
    }
  },

  async updated() {
    if(this.highlightLocal) {
      await this.wait();
      this.highlightChosenDay(this.selectedDate);
    }
    this.highlightLocal = false;
  },

  methods: {
    isCurrentDay(dayIdx) {
      const dayDate = new Date(this.timetable.datesInWeek[dayIdx]);
      const today = new Date();
      return this.isToday(dayDate) && this.getDay(today) == dayIdx;
    },

    isToday(someDate) {
      const today = new Date();
      return someDate.getDate() == today.getDate() 
        && someDate.getMonth() == today.getMonth() 
        && someDate.getFullYear() == today.getFullYear();
    },

    getDay(date) {
      return (date.getDay() + 6) % 7;
    },

    convDateToGerman(date) {
      let d = date.split("-");
      return d[2] + "-" + d[1] + "-" + d[0];
    },

    isSelectedDay(dayIdx) {
      return this.getDay(this.selectedDate) == dayIdx && !this.isToday(this.selectedDate);
    },

    changeChecked(id) {
      if(this.checkedSlots.includes(id))
        this.checkedSlots = this.checkedSlots.filter(slotId => slotId != id);
      else
        this.checkedSlots.push(id)
      this.$emit('checkedSlots', this.checkedSlots)
    },

    async highlightChosenDay(selectedDate) {
      let th = this.$refs["thday" + this.getDay(selectedDate)][0];
      th.scrollIntoView({behavior: 'smooth', inline: 'center'});
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

    wait() {
      return new Promise((resolve) => {
        setTimeout(() => {resolve();}, 400);
      })
    },

  }
}
</script>

<style>
.timetable {
}

.timetable td {
  border-top: none !important;
}
.timetable th {
  min-width: 110px;
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

