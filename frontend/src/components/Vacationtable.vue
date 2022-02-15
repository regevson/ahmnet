<template>

  <div align="center">
    <h1>URLAUBSTAFEL</h1><br>

    
    <Table :isVacationTable="true"/>


<!--
    <h3 v-if="!trainings">Oops... sth. went wrong!</h3> 
-->

  </div>

</template>



<script>
import Table from "./Table";

export default {
  name: 'VacationTable',
  components: {Table},
  props: {
    user: Object
  },

  data() {
    return {
    }
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

