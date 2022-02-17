<template>
  <div>
    <div align="center">
      <h1>TRAININGSGRUPPEN</h1>
      <div align="center">
        <button class="newBtn" v-on:click="createGroup">
          Neue Gruppe erstellen
        </button>
      </div>
    </div>

    <div v-for="(club, idx) in clubs" :key="idx">
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <button block class="cardBtn" v-b-toggle="'accordion-'+idx">
            {{club.name}}
          </button>
        </b-card-header>
        <b-collapse
          :id="'accordion-'+idx"
          accordion="my-accordion"
          role="tabpanel"
        >
          <b-card-body>
            <TrainingGroupSnippet :club="club" />
          </b-card-body>
        </b-collapse>
      </b-card>
    </div>
  </div>
</template>

<script>
import { axiosReq } from '../axios'
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
    const response = await axiosReq('allClubs');
    this.clubs = response.data;
  },

  methods: {
    createGroup() {
      this.$router.push({name: 'traininggroupdetails', params: {groupId: -1}});
    },

  }

}
</script>

<style scoped>
.card {
  border: 2px solid #3e6d63;
}

.card-header {
  background: #3e6d63;
  border: none;
  border-radius: 0 !important;
}

.card-body {
  border: none;
  height: auto;
  padding: 5px;
}

.cardBtn {
  background: none;
  border: none;
  color: white;
  font-weight: bold;
  padding: 5px;
  width: 100%;
}
</style>

