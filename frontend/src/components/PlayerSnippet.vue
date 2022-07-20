<template>
  <div>
    <div v-if="players" style="display: flex; flex-wrap:wrap; justify-content: center;">
      <div v-for="(player, pl_idx) in players" :key="pl_idx">
        <router-link :to="{name: 'playerdetails', params: {playerId: player.id}}" class="link">
          <div id="groupSnippet">
            <div class="top">{{player.fullName}}<br /></div>
            <div class="bot">
                <span class="tag">Tags:</span>
                <span class="player trainer"> {{new Date().getFullYear() - player.birthYear}}J </span>
                <div v-for="groupId in player.trainingGroupIds" :key="groupId" style="display: inline-block">
                  <router-link :to="{name: 'traininggroupdetails', params: {groupId: groupId}}" class="link">
                    <span class="player" style="margin-left: 7px"> Gr{{groupId}} </span>
                  </router-link>
                </div>
            </div>
          </div>
        </router-link>
      </div>
      <div style="clear: both"></div>
    </div>
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
    let res = await this.$ax.get('clubs/' + this.clubId + '/users?role=PLAYER');
    this.players = res.data;
  },

}
</script>

<style>
.tag {
  background: rgb(193, 131, 16);
  padding: 3px;
  border-radius: 3px;
  margin-right: 5px;
  font-weight: 600;
}
</style>
