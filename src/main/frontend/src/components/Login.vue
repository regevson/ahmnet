<template>
<div class="wrapper fadeInDown">
  <div id="formContent">
    <!-- Tabs Titles -->

    <!-- Icon -->
    <div class="fadeIn first" style="margin-top:20px; margin-bottom:10px">
      <h3>ahmNet-Login</h3>
    </div>

    <!-- Login Form -->
    <form @submit.prevent="handleSubmit">
      <input v-model="username" type="text" id="login" class="fadeIn second" name="login" placeholder="Username">
      <input v-model="password" type="text" id="password" class="fadeIn third" name="login" placeholder="Password">
      <input type="submit" class="fadeIn fourth" value="Log In" style="margin-top: 30px">
    </form>

    <!-- Remind Passowrd -->
    <div id="formFooter">
      <a class="underlineHover" href="#">Passwort vergessen?</a>
    </div>

  </div>
</div>
</template>

<script>
import axios from 'axios'


export default {
  name: 'Login',
  data() {
    return {
      username: '',
      password: '',
    }
  },

  methods: {
    async handleSubmit() {

      const params = new URLSearchParams()
      params.append('username', this.username)
      params.append('password', this.password)
  
      const config = {
        headers: {
          'Content-Type': 'application/x-www-form-urlencoded'
        }
      }
  
      const response = await axios.post('http://10.0.0.242:8080/login', params, config);
      axios.defaults.headers.common['Authorization'] = 'Bearer ' + response.data.access_token;
      localStorage.setItem('accessToken', response.data.access_token);
      localStorage.setItem('username', this.username);
      this.$router.push('/timetable');
    }
  }
}
</script>
