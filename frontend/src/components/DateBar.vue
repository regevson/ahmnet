<template>
  <div align="center">

    <b-form-datepicker
      v-model="selectedDate"
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

    <span v-if="startDate && endDate" style="font-weight: bold">
    {{convDateToGerman(startDate)}} - {{convDateToGerman(endDate)}}
    </span>

    <i
      @click="nextWeek"
      class="fa-solid fa-circle-arrow-right arrow rightArrow"
      style="margin-left: 60px"
    ></i>

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
    }
  },

  created() {
    if(this.selectedDate == null)
      this.selectedDate = new Date().toString();
    else
      this.$emit('dateChanged', new Date(this.selectedDate));
  },

  methods: {
    prevWeek() {
      this.weekNum--;
      let date = new Date(this.selectedDate);
      date.setDate(date.getDate() - 7);
      this.selectedDate = date.toString();
    },

    nextWeek() {
      this.weekNum++;
      let date = new Date(this.selectedDate);
      date.setDate(date.getDate() + 7);
      this.selectedDate = date.toString();
    },

    convDateToGerman(date) {
      date = date.split("-");
      return date[2] + "-" + date[1] + "-" + date[0];
    },

  },

  computed: {
    selectedDate: {
      get() {
        return this.$store.getters.selectedDate;
      },
      set(selectedDate) {
        this.$emit('dateChanged', new Date(selectedDate));
        return this.$store.commit("selectedDate", selectedDate);
      }
    },
  }

}

</script>
