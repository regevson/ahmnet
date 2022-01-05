<template>
<div align="center">
  <h1>Welcome Home Brother! </h1>
  <h2 v-if="user">This is your data: <br>{{user.username}}<br>{{user.firstName}}<br>{{user.lastName}}</h2>
  <h2 v-if="!user">But sth. went wrong!</h2>
</div>
</template>

<script>
import axios from 'axios'



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
    console.log(username);
    const response = await axios.get('http://localhost:8080/api/user?username=' + username, config);
    this.user = response.data;

    var tmp = document.getElementById('secret');
    var arr = this.user.roles;
    if(arr[0] == "PLAYER")
      tmp.style.display = "block";
  }
}


</script>
