<template>
  <div>
    <div align="center">
      <h1>SPIELERÜBERSICHT</h1>
      <div align="center">
        <button class="newBtn" v-on:click="createPlayer">
          Neuer Spieler
        </button>
      </div>
    </div>
    <br>

    <div v-for="(club, idx) in clubs" :key="idx">
      <b-card no-body class="mb-1" @click="clicked(club[0])">
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
            <PlayerSnippet v-if="isActive(club[0])" :clubId="club[0]" />
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
      activeClubId: '',
    }
  },

  async created() {
    const response = await this.$ax.get('clubs/actions/count-players');
    this.clubs = Object.entries(response.data);
  },

  methods: {
    createPlayer() {
      this.$router.push({name: 'playerdetails', params: {playerId: -1}});
    },

    isActive(clubId) {
      return this.activeClubId == clubId;
    },

    clicked(club) {
      this.activeClubId = club;
    }

  }

}
</script>
