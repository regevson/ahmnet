<template>
  <div>
    <div v-if="players">
      <div v-for="(player, pl_idx) in players" :key="pl_idx">
        <router-link :to="{name: 'playerdetails', params: {clubId: clubId, groupId: player.id}}" class="link">
          <div id="groupSnippet">
            <div class="top">{{player.fullName}}<br /></div>
            <div class="bot">
              <b>Trainer:</b>
              <div v-for="(trainer, tr_idx) in player.trainers" :key="trainer.id+tr_idx">
                <span class="player trainer" style="display: block"> {{trainer.fullName}} </span>
              </div>
              <hr style="margin: 5px 0px 5px 0px" />
              <b>Gruppen:</b>
                <div v-for="group in player.groups" :key="group.id">
                  <router-link :to="{name: 'traininggroupdetails', params: {groupId: group.id}}" class="link">
                    <span class="player" style="display: block"> {{group.clubId}}\Gr{{group.id}} </span>
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
    let players = res.data;
    for(let player of players) {
      player.trainers = [];
      player.groups = [];
      let res_group = await this.$ax.get(player.groups_url);
      for(let group of res_group.data) {
        player.groups.push(group);
        let res_trainer = await this.$ax.get(group.trainer_url);
        res_trainer = res_trainer.data[0];
        player.trainers.push(res_trainer);
      }
    }
    this.players = players;
  },

}
</script>
