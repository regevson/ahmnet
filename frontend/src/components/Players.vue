<template>
  <div>
    <div align="center">
      <h1>SPIELERÜBERSICHT</h1>
      <div align="center">
        <button class="newBtn" v-on:click="createGroup">
          Neuer Spieler
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
            <PlayerSnippet :clubId="club[0]" />
          </b-card-body>
        </b-collapse>
      </b-card>
    </div>
  </div>
</template>

<script>
import PlayerSnippet from "./PlayerSnippet";

export default {
  name: 'Players',
  components: {PlayerSnippet},
  props: {
    user: Object
  },

  data() {
    return {
      clubs: null,
    }
  },

  async created() {
    const response = await this.$ax.get('clubs/actions/count-players');
    this.clubs = Object.entries(response.data);
  },

  methods: {
    createGroup() {
      this.$router.push({name: 'traininggroupdetails', params: {clubId: this.user.clubName, groupId: -1}});
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
  padding: 5px 0 5px 5px;
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

