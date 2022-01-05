<template>
<div align="center">
  <h1>Welcome Home Brother! </h1>
  <h2 v-if="user">This is your data: <br>{{user.username}}<br>{{user.firstName}}<br>{{user.lastName}}</h2>
  <h2 v-if="!user">But sth. went wrong!</h2>
</div>
</template>

<script>
import axios from 'axios'
import {bus} from '../main'

export default {
  name: 'Home',

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
    const response = await axios.get('http://localhost:8080/api/user?username=' + username, config);

    this.user = response.data;
    this.updateNav(this.user.roles);
  },

  methods: {
    updateNav: function(roles) {
      const loggedInfo = {loggedIn:true, roles:roles};
      bus.$emit('updateNav', loggedInfo);
    }
  }

}


</script>
