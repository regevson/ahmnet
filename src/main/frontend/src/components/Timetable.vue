<template>

  <div align="center">
    <h3>Your TimeTable:</h3>

    <table class="table table-responsive">
      <thead>
        <tr align="center">
          <th scope="col">Mo</th>
          <th scope="col">Di</th>
          <th scope="col">Mi</th>
          <th scope="col">Do</th>
          <th scope="col">Fr</th>
          <th scope="col">Sa</th>
          <th scope="col">So</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>
            <Trainingslot :trainings="trainings.MONDAY" :user="user"/>
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
      trainings: null
    }
  },

  async created() {
    const accessToken = 'Bearer ' + localStorage.getItem('accessToken');
    const config = {
      headers: {
      'Authorization': accessToken
      }
    }
    const username = localStorage.getItem('username');
    //var weekNum = 1;
    const response = await axios.get('http://10.0.0.242:8080/api/trainingsByWeek?username=' + username + '&weekNum=1', config);

    this.trainings = response.data;
  },

}


</script>

<style>
td {
  border-top: none !important;
}
.trainingPreview {
  background: cadetblue;
  color: white;
  font-weight: bold;
  border-radius: 14px;
  padding: 10px 0 10px 0;
  width: auto;
}
</style>
