<template>
  <div>
    <div align="center">
      <h1>TRAININGSGRUPPEN</h1>
      <div align="center">
        <button class="newBtn" v-on:click="createGroup">
          Neue Gruppe
        </button>
      </div>
    </div>
    <br>

    <div v-for="(club, idx) in clubs" :key="idx">
      <b-card no-body class="mb-1">
        <b-card-header header-tag="header" class="p-1" role="tab">
          <button block class="cardBtn" v-b-toggle="'accordion-'+idx">
            {{club[0]}} ({{club[1]}})
          </button>
        </b-card-header>
        <b-collapse
          :id="'accordion-'+idx"
          accordion="my-accordion"
          role="tabpanel"
        >
          <b-card-body>
            <TrainingGroupSnippet :clubId="club[0]" />
          </b-card-body>
        </b-collapse>
      </b-card>
    </div>
  </div>
</template>

<script>
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
    const response = await this.$ax.get('clubs/actions/count-groups');
    this.clubs = Object.entries(response.data);
  },

  methods: {
    createGroup() {
      this.$router.push({name: 'traininggroupdetails', params: {clubId: this.user.clubName, groupId: -1}});
    },

  }

}
</script>

<style>
.card {
  border: 2px solid #4b9183 !important;
  width: 100%;
  max-width: 700px;
}

.card-header {
  background: #4b9183 !important;
  border: none;
  border-radius: 0 !important;
}

.card-body {
  border: none;
  height: auto;
  padding: 10px 0 5px 5px !important;
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

