<template>
<div>
  <div align="center">
    <h3>TrainingGroups</h3>
    <br>
  </div>

 <div v-for="(club, idx) in clubs" :key="idx">
    <b-card no-body class="mb-1">
      <b-card-header header-tag="header" class="p-1" role="tab">
        <b-button block v-b-toggle="'accordion-'+idx" variant="info">{{club.name}} - Gruppen</b-button>
      </b-card-header>
      <b-collapse :id="'accordion-'+idx" visible accordion="my-accordion" role="tabpanel">
        <b-card-body>
          <TrainingGroupSnippet :club="club"/>
        </b-card-body>
      </b-collapse>
    </b-card>
</div>


</div>
</template>

<script>
import axios from 'axios'
import TrainingGroupSnippet from "./TrainingGroupSnippet";

export default {
  name: 'TrainingGroups',
  components: {TrainingGroupSnippet},
  props: {
    user: Object
  },

  data() {
    return {
      clubs: null,
    }
  },

  async created() {
    const response = await axios.get('api/allClubs');
    this.clubs = response.data;
  },

}


</script>

<style scoped>
.card-header {
  background: #4b9183;
  border: none;
}
.card-header .btn{
  background: #4b9183;
  border: none;
  color: white;
  font-weight: bold;
  box-shadow: none;
}

.card-body {
  /*background: #1b2730;*/
  border: 2px solid #4b9183;
  border-radius: 5px;
  border-top: none;
  height: auto;
  padding: 5px;
}


</style>
