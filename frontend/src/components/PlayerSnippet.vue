<template>
  <div>
    <div v-for="(player, idx) in players" :key="idx">
      <router-link
        :to="{name: 'playerdetails', params: {clubId: clubId, groupId: player.id}}"
        class="link"
      >
        <div id="groupSnippet">
          <div class="top">{{player.fullName}}<br /></div>
          <div class="bot">
            <b>Trainer:</b>
            <span
              class="player trainer"
              style="display: block"
              v-for="group in player.groups"
              :key="group.id"
            >
              {{group.trainer.fullName}}
            </span>
            <hr style="margin: 5px 0px 5px 0px" />
            <b>Gruppen:</b>
            <span
              class="player"
              style="display: block"
              v-for="group in player.groups"
              :key="group.id"
            >
              Gruppe {{group.id}}
            </span>
          </div>
        </div>
      </router-link>
    </div>
    <div style="clear: both"></div>
  </div>
</template>

<script>

export default {
  name: 'PlayerSnippet',
  props: {
    clubId: null,
  },
  data() {
    return {
      players: null,
    }
  },

  async created() {
    const response = await this.$ax.get('clubs/' + this.clubId + '/players');
    this.players = response.data;
    console.log(this.players);
  },

}
</script>
