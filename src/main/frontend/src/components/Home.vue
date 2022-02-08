<template>
<div align="center">
  <Nav v-on:newRoute="changeRoute" v-bind:user="user"/>
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
import Nav from "./Nav"
import axios from 'axios'

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
    console.log(response.data);

    this.user = response.data;

    // in case user navigates to '/home', he gets redirected to '/timetable'
    if(this.$route.name === 'home') 
      this.$router.replace({name: 'timetable'});
  },

  methods: {
    changeRoute: function(newRouteName) {
      this.$router.push({name: newRouteName});
    },
  }

  /*
  beforeRouteUpdate(to, from, next) {
    if(to.name === '/') 
      next({name: 'timetable'});
    else
      next();
  },
  */

}


</script>

<style>

body {
  padding: 10px;
}

.ahmBgGreen {
  background: #4b9183;
}
.ahmBgDark {
  background: #1b2730;
}

</style>

