<template>
  <div>
    <div v-if="players" style="display: flex; flex-wrap:wrap; justify-content: center;">
      <div v-for="(player, pl_idx) in players" :key="pl_idx">
        <router-link :to="{name: 'playerdetails', params: {playerId: player.id}}" class="link">
          <div id="groupSnippet">
            <div class="top">{{player.fullName}}<br /></div>
                <div align="center" class="tag"><i class="fa-solid fa-tags fa-xs"></i></div>
            <div align="center" style="width: 100%; padding-top: 0" class="bot">
                <span class="player trainer"> 
                  {{new Date().getFullYear() - player.birthYear}}J 
                </span>
                <div v-for="groupId in player.trainingGroupIds" :key="groupId" 
                style="display: inline-block">
                  <router-link :to="{name: 'traininggroupdetails', params: {groupId: groupId}}" 
                  class="link">
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
  margin-top: -7px !important;
  color: #c8c2c2;
  background: #273a48;
  padding: 0px;
  width: 25px;
  height: fit-content;
  border-radius: 50%;
  border: 0px solid white;
  margin: 0 auto;
}
</style>
