<template>

  <div align="center">
    <div v-if="user">
      <h1>VERFÜGBARE TRAININGS</h1><br>

      <Table :isVacationTable="true"/>
    </div>

    <h5 v-if="!user" class="loading">LOADING...</h5>

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

</style>

