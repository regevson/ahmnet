<template>
<div align="center">
  <Nav v-bind:user="user"/>
  <!--
  <h2 v-if="user">Welcome Home {{user.firstName}}! </h2>
  <h3 v-if="!user">Oops... sth. went wrong!</h3>
  -->
  <router-view :user="user"/>
  <!--
  -->
</div>
</template>

<script>
import Nav from "./Nav";
import axios from 'axios';

export default {
  name: 'Home',
  components: {Nav},

  data() {
    return {
      user: null,
    }
  },

  async created() {
    const username = localStorage.getItem('username');
    const response = await axios.get('api/user?username=' + username);

    this.user = response.data;

    if(this.$route.name !== 'timetable') // so there are no unnecessary redirects
      this.$router.push({name: 'timetable'});
  },

  beforeRouteUpdate(to, from, next) {
    if(to.name === 'home') // home gets redirected to timetable
      next({name: 'timetable'});
    else
      next();
  },

}


</script>
