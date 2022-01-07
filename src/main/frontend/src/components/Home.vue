<template>
<div align="center">
  <h2 v-if="user">Welcome Home {{user.firstName}}! </h2>
  <h3 v-if="!user">Oops... sth. went wrong!</h3>
  <Timetable :user="user" />
</div>
</template>

<script>
import Timetable from "./Timetable";
import axios from 'axios'

export default {
  name: 'Home',
  components: {Timetable},

  data() {
    return {
      user: null
    }
  },

  async created() {
    const accessToken = 'Bearer ' + localStorage.getItem('accessToken');
    const config = {
      headers: {
      'Authorization': accessToken
      }
    }
    const username = localStorage.getItem('username');
    const response = await axios.get('http://10.0.0.242:8080/api/user?username=' + username, config);

    this.user = response.data;
    this.updateRoles(this.user.roles);
  },

  methods: {
    updateRoles: function(roles_arr) {
      this.$store.commit('isLoggedIn', true);
      var roles = {isAdmin: roles_arr.includes("ADMIN"), isTrainer: roles_arr.includes("TRAINER"), isPlayer: roles_arr.includes("PLAYER")};
      this.$store.commit('roles', roles);
    }
  }

}


</script>
