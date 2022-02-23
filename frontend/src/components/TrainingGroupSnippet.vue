<template>
  <div>
    <div v-for="(group, idx) in groups" :key="idx">
      <router-link
        :to="{name: 'traininggroupdetails', params: {groupId: group.id}}"
        class="link"
      >
        <div id="groupSnippet">
          <div class="top">Gruppe {{group.id}}<br /></div>
          <div class="bot">
            <b>Trainer: </b>
            <span class="player trainer"
              >{{group.trainer.firstName}} {{group.trainer.lastName}}</span
            ><br />
            <hr style="margin: 5px 0px 5px 0px" />
            <b>SpielerInnen:</b><br />
            <span
              class="player"
              v-for="player in group.players"
              :key="player.id"
            >
              {{player.firstName}} {{player.lastName}}
            </span>
          </div>
        </div>
      </router-link>
    </div>
    <div style="clear: both"></div>
  </div>
</template>

<script>
import { axiosReq } from '../axios'

export default {
  name: 'TrainingGroupSnippet',
  props: {
    club: null,
  },
  data() {
    return {
      groups: null,
    }
  },

  async created() {
    const response = await axiosReq('groupsByClub?clubName=' + this.club);
    this.groups = response.data;
  },

}
</script>

<style scoped>
#groupSnippet {
  float: left;
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
}

#groupSnippet .bot {
  padding: 10px;
}

#groupSnippet .player {
  display: inline-block;
  background: #4b9183;
  color: white;
  border-radius: 5px;
  margin-bottom: 2px;
  margin-right: 2px;
  padding: 3px;
  font-size: 11px;
}

#groupSnippet .trainer {
  background: #c28b8b;
  font-weight: bold;
}
</style>

