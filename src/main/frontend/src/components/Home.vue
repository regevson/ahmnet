<template>
<div align="center">
  <Nav v-on:newRoute="changeRoute" v-bind:user="user"/>
  <!--
  <h2 v-if="user">Welcome Home {{user.firstName}}! </h2>
  <h3 v-if="!user">Oops... sth. went wrong!</h3>
  -->
  <div class="wrapper">
    <router-view :user="user"/>
  </div>

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
      // unused
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

.wrapper {
  padding: 10px;
}

h1 {
  font-size: 1.5vw;
  font-weight: bold;
  background: #4b9183;
  color: white;
  width: auto;
  max-width: 700px;
  display: block;
  border-radius: 4px;
  padding: 5px;
}

.ahmBgGreen {
  background: #4b9183;
}
.ahmBgDark {
  background: #1b2730;
}

.newBtn {
  background: none;
  border: 2px solid #4b9183;
  border-radius: 8px;
  padding: 5px;
  font-weight: bold;
  color: green;
  margin: 20px 0 30px 0;
  transition: all 0.3s ease-in-out;
}

.newBtn:hover {
  color: white;
  background: #4b9183;
  transition: all 0.3s ease-in-out;
}

.changeBtn {
  color: white;
  background: #4b9183;
  border: 2px solid #4b9183;
  border-radius: 8px;
  padding: 5px;
  font-weight: bold;
  margin: 20px 10px 30px 0;
  transition: all 0.3s ease-in-out;
}

.changeBtn:hover {
  background: none;
  border: 2px solid #4b9183;
  color: #4b9183;

  transition: all 0.3s ease-in-out;
}

.deleteBtn {
  color: white;
  background: red;
  border: 2px solid red;

  border-radius: 8px;
  padding: 5px;
  font-weight: bold;
  margin: 20px 0 30px 0;
  transition: all 0.3s ease-in-out;
}

.deleteBtn:hover {
  background: none;
  border: 2px solid red;
  color: red;

  transition: all 0.3s ease-in-out;
}

.arrow {
  font-size: 20px;
  color: #4b9183;
}

.arrow:hover {
  cursor: pointer;
  color: green;
}

</style>

