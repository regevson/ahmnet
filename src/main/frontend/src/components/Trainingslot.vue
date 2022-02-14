<template>

  <div>
    <div v-for="(training,tr_idx) in trainings" :key="training.id" align="center" :class="{currentslot: checkSlot(tr_idx) === 'currentslot', pastslot: checkSlot(tr_idx) === 'pastslot', vacationslot: checkVacation(tr_idx) == 'vacationslot'}" class="trainingPreview">
      <router-link :to="{name: 'trainingdetails', params: {trainingId: training.id}}" class="link">
          <span style="font-size: 13px; display: block; font-weight: 600;">{{training.date}}</span>
        <div id="inner" class="trainingPreviewInner">
          <span>{{training.club.name}}</span><br>
          <hr>
          <span>{{training.timeslot}}</span><br>
        </div>
          <span style="font-size: 13px; display: block; font-weight: 600;">{{training.court}}</span>
      </router-link>
    </div>
  </div>

</template>

<script>
//import axios from 'axios'

export default {
  name: 'TrainingSlot',
  props: {
    trainings: Array,
    user: Object,
    selectedTrainer: Object
  },

  data() {
    return {
    }
  },

  methods: {
    checkSlot(tr_idx) {
      const tr = this.trainings[tr_idx];
      let startDateTime = this.convertToDate(tr.date);
      let startTime = tr.startTime.split(':');
      startDateTime.setHours(startTime[0], startTime[1], startTime[2]);
      let endDateTime = new Date(+startDateTime + 60000 * tr.durationMinutes);

      //let currDateTime = new Date();
      let currDateTime = new Date("2022-02-12");
      currDateTime.setHours(14, 10, 0);
      if(currDateTime >= startDateTime && currDateTime <= endDateTime)
        return "currentslot";
      else if(startDateTime < currDateTime)
        return "pastslot";
      else
        return "";
    },
    checkVacation(tr_idx) {
      const tr = this.trainings[tr_idx];
      if(tr.free || tr.originalTrainerId != this.selectedTrainer.id)
        return 'vacationslot';
    },
    convertToDate(dateStr) {
      var parts = dateStr.split("-");
      return new Date(parseInt(parts[2], 10), parseInt(parts[1], 10) - 1, parseInt(parts[0], 10));
    }
  },

}


</script>

<style>


body {
  font-family: 'Open Sans', sans-serif !important;
}


td {
  border-top: none !important;
}
.trainingPreview {
  font-weight: 700;
  padding: 5px 0 5px 0;
  width: auto;
  margin-top: 20px;
  min-width: 125px;
  transition: all 0.5s ease-in-out;
  background: #4b9183;
  border: 2px solid #4b9183;
  border-radius: 14px;
  text-align: center;
}

.trainingPreviewInner {
  padding: 5px 5px 5px 5px;
  background: #6bab9f;
  margin: 5px 0 5px 0;
  border-radius: 14px;
}

.trainingPreview:hover {
  border-radius: 5px;
  transition: all 0.5s ease-in-out;
}

.link {
  color: white;
  text-decoration: none;
}
.link:hover {
  color: white;
  text-decoration: none;
}

.trainingPreview hr {
  margin: 0;
  background: white;
  height: 3px !important;
}

.currentslot {
  color: white !important;
  border: 4px outset orange !important;
}

.currentslot #inner {
}

.currentslot > .link {
  color: white !important;
}

.pastslot {
  /*opacity: 0.7;*/
  opacity: 1;
}

.vacationslot {
  background: #fdd663 !important;
  border-color: #fdd663;
}

.vacationslot #inner {
  background: #ffffff6b;
  color: #774e00 !important;
}

.vacationslot > .link {
  color: black !important;
}

.vacationslot hr {
  background: #8c6903;
}

</style>

