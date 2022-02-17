<template>
  <div>
    <div
      v-for="(training,tr_idx) in trainings"
      :key="training.id"
      align="center"
      :class="{currentslot: getSlotType(tr_idx) === 'currentslot', pastslot: getSlotType(tr_idx) === 'pastslot', vacationslot: checkVacation(tr_idx) == 'vacationslot'}"
      class="trainingPreview"
    >
      <router-link
        :to="{name: 'trainingdetails', params: {trainingId: training.id}}"
      >
        <router-link
          :to="{name: 'traininggroupdetails', params: {groupId: training.groupId}}"
        >
          <div align="center">
            <span class="groupLink">Gruppe {{training.groupId}}</span>
          </div>
        </router-link>
        <div id="inner" class="trainingPreviewInner">
          <span>{{training.club.name}}</span
          ><br />
          <hr />
          <span>{{training.timeslot}}</span
          ><br />
        </div>
        <span class="court">Court {{training.court}}</span>
      </router-link>
    </div>
  </div>
</template>

<script>

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
    getSlotType(tr_idx) {
      const tr = this.trainings[tr_idx];
      let startDateTime = this.getStartDateTime(tr);
      let endDateTime = this.getEndDateTime(startDateTime, tr.durationMinutes);
      let currDateTime = this.getCurrentDateTime();

      if(currDateTime >= startDateTime && currDateTime <= endDateTime)
        return "currentslot";
      else if(startDateTime < currDateTime)
        return "pastslot";
      else
        return "";
    },

    getStartDateTime(tr) {
      let startDateTime = this.convertToDate(tr.date);
      let startTime = tr.startTime.split(':');
      startDateTime.setHours(startTime[0], startTime[1], startTime[2]);
      return startDateTime;
    },

    convertToDate(dateStr) {
      var parts = dateStr.split("-");
      return new Date(parseInt(parts[2], 10), parseInt(parts[1], 10) - 1, parseInt(parts[0], 10));
    },

    getEndDateTime(start, duration) {
      return new Date(+start + 60000 * duration);
    },

    getCurrentDateTime() {
      //return new Date();
      let curr = new Date("2022-02-16");
      curr.setHours(21, 37, 0);
      return curr;
    },

    checkVacation(tr_idx) {
      const tr = this.trainings[tr_idx];
      if(tr.free || tr.originalTrainerId != this.selectedTrainer.id)
        return 'vacationslot';
    },

  },

}
</script>

<style>
body {
  font-family: "Open Sans", sans-serif !important;
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

.trainingPreview:hover {
  border-radius: 5px;
  transition: all 0.5s ease-in-out;
}

.trainingPreview a {
  text-decoration: none;
  color: white;
}

.trainingPreview a:hover {
  color: white;
}

.trainingPreview hr {
  margin: 0;
  background: white;
  height: 3px !important;
}

.groupLink {
  background: #6bab9f;
  font-size: 13px;
  display: block;
  font-weight: 600;
  font-weight: bold;
  border-radius: 10px;
  color: white;
  text-decoration: none !important;
  padding: 2px 5px 2px 5px;
  display: inline-block;
  width: auto;
}

.trainingPreviewInner {
  padding: 5px 5px 5px 5px;
  background: #6bab9f;
  margin: 5px 0 5px 0;
  border-radius: 14px;
}

.court {
  font-size: 13px;
  display: block;
  font-weight: 600;
}

.currentslot {
  border: 4px outset orange !important;
}

.pastslot {
  opacity: 0.7;
}

.vacationslot {
  background: #fdd663;
  border-color: #fdd663;
}

.vacationslot .trainingPreviewInner {
  background: #ffffff6b;
  color: #774e00;
}

.vacationslot a {
  color: black;
}

.vacationslot a:hover {
  color: black;
}

.vacationslot hr {
  background: #8c6903;
}

.vacationslot .groupLink {
  background: #ffffff6b;
  color: #774e00;
}
</style>

