<template>
  <div align="center">
    <Nav v-on:newRoute="changeRoute" v-bind:user="user" />
    <!--
  <h2 v-if="user">Welcome Home {{user.firstName}}! </h2>
  <h3 v-if="!user">Oops... sth. went wrong!</h3>
  -->
    <div class="wrapper">
      <router-view :user="user" />
    </div>
  </div>
</template>

<script>
import Nav from "./Nav"

export default {
  name: 'Home',
  components: {Nav},

  data() {
    return {
      user: null,
    }
  },

  async created() {
    const username = sessionStorage.getItem('username');
    const response = await this.$ax.get('users/' + username);

    this.user = response.data[0];
    sessionStorage.setItem('user', JSON.stringify(this.user));

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
  padding: 5px;
}

h1 {
  font-size: 28px !important;
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
  color: #499082;
  margin: 20px 0 0px 0;
  transition: all 0.3s ease-in-out;
}

.newBtn:hover {
  color: white;
  background: #4b9183;
  transition: all 0.3s ease-in-out;
}

.deleteBulkBtn {
  background: none;
  border: 2px solid #b90707;
  border-radius: 8px;
  padding: 5px;
  font-weight: bold;
  color: #b90707;
  margin: 20px 0 0px 0;
  transition: all 0.3s ease-in-out;
}

.deleteBulkBtn:hover {
  color: white;
  background: #b90707;
  transition: all 0.3s ease-in-out;
}

.freeBulkBtn {
  background: none;
  border: 2px solid #fdc110;
  border-radius: 8px;
  padding: 5px;
  font-weight: bold;
  color: #fdc110;
  margin: 20px 0 0px 0;
  transition: all 0.3s ease-in-out;
}

.freeBulkBtn:hover {
  color: white;
  background: #fdc110;
  transition: all 0.3s ease-in-out;
}

.disabledBtn {
  background: #fdd663;
  border-color: #fdd663;
  color: white;
  cursor: not-allowed;
  opacity: 0.5;
}

.disabledBtn:hover {
  background: #fdd663r
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
  background: #b90707;
  border: 2px solid #b90707;
  border-radius: 8px;
  padding: 5px;
  font-weight: bold;
  margin: 20px 10px 30px 0;
  transition: all 0.3s ease-in-out;
}

.deleteBtn:hover {
  background: none;
  border: 2px solid #b90707;
  color: #b90707;

  transition: all 0.3s ease-in-out;
}

.freeBtn {
  color: white;
  background: orange;
  border: 2px solid orange;

  border-radius: 8px;
  padding: 5px;
  font-weight: bold;
  margin: 20px 10px 30px 0;
  transition: all 0.3s ease-in-out;
}

.freeBtn:hover {
  background: none;
  border: 2px solid orange;
  color: orange;

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

.loading {
  color: silver;
  margin-top: 100px;
  text-align: center;
}


/* DIALOG */
.modal-content {
  border: 5px solid #499082 !important;
  border-radius: 10px !important;
}

.modal-title {
  font-weight: bold;
}

.modal-body {
  font-weight: bold;
}

.modal-content .btn-primary {
  color: white;
  background: #4b9183;
  border: 2px solid #4b9183;
  border-radius: 8px;
  padding: 5px;
  font-weight: bold;
  width: 60px;
  transition: all 0.3s ease-in-out;
}

.modal-content .btn-primary:hover {
  background: #4b9183;
  border: 2px solid #4b9183;
}

.modal-content .btn-secondary {
  color: white;
  background: gray;
  border: 2px solid gray;
  border-radius: 8px;
  padding: 5px;
  font-weight: bold;
  width: 80px;
  transition: all 0.3s ease-in-out;
}

.modal-content .btn-secondary:hover {
  background: gray;
  border: 2px solid gray;
}

</style>

