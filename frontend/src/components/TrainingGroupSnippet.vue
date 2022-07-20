<template>
  <div>
    <div v-if="groups" style="display: flex; flex-wrap:wrap;">
    <div v-for="(group, idx) in groups" :key="idx">
      <router-link :to="{name: 'traininggroupdetails', params: {groupId: group.id}}" class="link">
        <div id="groupSnippet">
          <div class="top">Gruppe {{group.id}}<br /></div>
          <div class="bot">
            <b>Trainer: </b>
            <span class="player trainer"
              >{{group.trainer.fullName}}</span
            ><br />
            <hr style="margin: 5px 0px 5px 0px" />
            <b>SpielerInnen:</b><br />
            <div v-for="player in group.players" :key="player.id">
              <router-link :to="{name: 'playerdetails', params: {playerId: player.id}}" class="link">
                <span class="player">{{player.fullName}}</span>
              </router-link>
            </div>
            <hr style="margin: 5px 0px 5px 0px" />
            <b>Ballfarben:</b><br />
            <div v-for="col in group.ballColors" :key="col" style="display: inline-block; margin-right: 5px;">
              <span>{{getBallEmoji(col)}}</span>
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
  name: 'TrainingGroupSnippet',
  props: {
    clubId: null,
  },
  data() {
    return {
      groups: null,
    }
  },

  async created() {
    let response = await this.$ax.get('clubs/' + this.clubId + '/groups');
    let groups = response.data;
    
    for(let group of groups) {
      response = await this.$ax.get(group.trainer_url);
      group.trainer = response.data[0];

      response = await this.$ax.get(group.players_url);
      group.players = response.data;
    }

    this.groups = groups;
  },

  methods: {
    getBallEmoji(col) {
      if(col === 'RED') return '🔴';
      else if(col === 'ORANGE') return '🟠';
      else if(col === 'GREEN') return '🟢';
      else return '🟡';
    }
  }

}
</script>

<style>
#groupSnippet {
  background: #273a48;
  color: white;
  max-width: 181px;
  border-radius: 15px;
  padding: 0px;
  overflow: auto;
  margin-bottom: 10px;
  text-align: left;
  margin-right: 4px;
}

#groupSnippet .top {
  background: #4b9183;
  padding: 5px;
  text-align: center;
  font-weight: 800;
  color: white;
  font-size: 15px;
}

#groupSnippet .bot {
  padding: 10px;
  font-size: 13px;
}

#groupSnippet .player {
  display: inline-block;
  background: #4b9183;
  color: white;
  border-radius: 5px;
  margin-bottom: 2px;
  padding: 3px;
  font-size: 11px;
}

#groupSnippet .trainer {
  background: #c28b8b;
  font-weight: bold;
}

.link {
  text-decoration: none;
}
.link:hover {
  text-decoration: none;
}
</style>

