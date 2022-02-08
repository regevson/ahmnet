<template>
<div>
  
 <div v-for="(group, idx) in groups" :key="idx">
  <router-link :to="{name: 'traininggroupdetails', params: {groupId: group.id}}">
    <div id="groupSnippet">
      <b>GruppenNr:</b> {{group.id}}<br>
      <b>Trainer:</b> {{group.trainer.firstName}} {{group.trainer.lastName}}<br>
      <b>SpielerInnen:</b>
      <span v-for="player in group.players" :key="player.id">
      {{player.firstName}} {{player.lastName}}, 
      </span>
    </div>
    </router-link>
  </div>

</div>
</template>

<script>
import axios from 'axios'

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
    const response = await axios.get('api/groupsByClub?clubName=' + this.club.name);
    this.groups = response.data;
  },

}

</script>

<style scoped>
#groupSnippet {
  background: green;
  color: white;
  width: 50%;
  border-radius: 15px;
  padding: 10px;
  margin-bottom: 10px;
}
</style>
