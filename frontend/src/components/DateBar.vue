<template>
  <div align="center">

    <b-form-datepicker
      v-model="calendarDate"
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

<div class="row justify-content-center" style="--bs-gutter-x: 0;">
    <div class="col-auto">
    <i
      @click="prevWeek"
      class="fa-solid fa-circle-arrow-left arrow leftArrow"
      style="margin-right: 30px"
    ></i>

    <span v-if="startDate && endDate" style="font-weight: bold">
    {{convDateToGerman(startDate)}} - {{convDateToGerman(endDate)}}
    </span>

    <i
      @click="nextWeek"
      class="fa-solid fa-circle-arrow-right arrow rightArrow"
      style="margin-left: 30px"
    ></i>
  </div>
  </div>

  </div>
</template>

<script>

export default {
  name: 'DateBar',
  props: {
    startDate: String,
    endDate: String,
  },

  data() {
    return {
      calendarDate: '',
    }
  },

  watch: {
    calendarDate: function() {
      this.selectedDate = this.calendarDate;
      this.emitDate(this.selectedDate, 'calendar');
    }
  },

  created() {
    if(this.selectedDate == null)
      this.selectedDate = new Date().toString();
    this.emitDate(this.selectedDate, 'bootstrap');
  },

  methods: {
    prevWeek() {
      this.weekNum--;
      let date = new Date(this.selectedDate);
      date.setDate(date.getDate() - 7);
      this.selectedDate = date.toString();
      this.emitDate(this.selectedDate, 'prevBnt');
    },

    nextWeek() {
      this.weekNum++;
      let date = new Date(this.selectedDate);
      date.setDate(date.getDate() + 7);
      this.selectedDate = date.toString();
      this.emitDate(this.selectedDate, 'nextBnt');
    },

    convDateToGerman(date) {
      date = date.split("-");
      return date[2] + "-" + date[1] + "-" + date[0];
    },

    emitDate(date, trigger) {
      this.$emit('dateChanged', {'date': new Date(date), 'trigger': trigger});
    }

  },

  computed: {
    selectedDate: {
      get() {
        return this.$store.getters.selectedDate;
      },
      set(selectedDate) {
        return this.$store.commit("selectedDate", selectedDate);
      }
    },
  }

}

</script>
