<template>
  <div>
    <div v-if="groups" style="display: flex; flex-wrap:wrap; justify-content: center; ">
    <div v-for="(group, idx) in groups" :key="idx">
      <router-link :to="{name: 'traininggroupdetails', params: {groupId: group.id}}" class="link">
        <div id="groupSnippet">
          <div class="top">Gruppe {{group.id}}<br /></div>
          <div align="center" class="bot">
            <i class="fa-solid fa-user-tie fa-xl" 
              style="margin: 10px 0px -4px 0; display: block"
            ></i>
            <span style="margin-top: 13px; font-weight: bold" class="player trainer">
              {{$funcs.splitCamelCase(group.trainerId)}}
            </span><br />
            <hr style="margin: 15px 0px 5px 0px" />
            <i class="fa-solid fa-people-group fa-xl" 
              style="margin: -6px 0 -3px 0; text-align: center; display: block; position: relative;
              z-index: 9999"
            ></i><br>
            <div v-for="playerId in group.playerIds" :key="playerId">
              <router-link :to="{name: 'playerdetails', params: {playerId: playerId}}" class="link">
                <span class="player" style="font-weight: 500">{{$funcs.splitCamelCase(playerId)}}</span>
              </router-link>
            </div>
            <hr style="margin: 12px 0px 5px 0px" />
            <div style="margin-top: -15px">
              <div v-for="col in group.ballColors" :key="col" 
                style="display: inline-block; margin-right: 5px; position: relative; z-index: 9999">
                <span>{{getBallEmoji(col)}}</span>
            </div>
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
    let group_res = await this.$ax.get('clubs/' + this.clubId + '/groups');
    this.groups = group_res.data;
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
  font-size: 14px;
}

#groupSnippet .bot {
  padding: 7px;
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
}

.link {
  text-decoration: none;
}
.link:hover {
  text-decoration: none;
}
</style>

