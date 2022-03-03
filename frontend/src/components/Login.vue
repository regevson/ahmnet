<template>
  <div class="container" style="margin-top: 20px; max-width: 700px">
    <div class="card card-login mx-auto text-center bg-dark" style="border-radius: 20px;">
      <div class="card-header mx-auto bg-dark">
        <br />
        <span>
          <img src="../../public/img/logo.png" class="w-75" alt="Logo" /> </span
        ><br />
      </div>
      <div class="card-body">
        <form @submit.prevent="handleSubmit">
          <div class="input-group form-group">
            <div class="input-group-prepend">
              <span
                class="loginIcon"
                ><i class="fas fa-user"></i
              ></span>
            </div>
            <b-form-input
              v-on:click="resetErrorLabel"
              type="text"
              v-model="username"
              placeholder="Username"
            ></b-form-input>
          </div>

          <div class="input-group form-group">
            <div class="input-group-prepend">
              <span
                class="loginIcon"
                ><i class="fas fa-key"></i
              ></span>
            </div>
            <b-form-input
              v-on:click="resetErrorLabel"
              type="password"
              v-model="password"
              placeholder="Password"
            ></b-form-input>
          </div>


      <div style="color: red; font-size: 15px; text-align: left; position: absolute;" class="errorText" v-if="wrongCredentials">Login fehlgeschlagen!</div>

          <div align="center">
            <input type="submit" class="changeBtn" value="Log In" />
          </div>
      <div style="color: white; font-size: 15px; text-align: left;" >Passwort vergessen?<br>- wenden Sie sich bitte an 069910983630.</div>
        </form>
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
      password: 'passwd',
      wrongCredentials: false,
    }
  },

  created() {
    // login with clean vuex-state
    this.$store.commit("selectedTrainer", null);
  },

  methods: {
    async handleSubmit() {
      const params = new URLSearchParams()
      params.append('username', this.username)
      params.append('password', this.password)

      let response;
      try {
        response = await axios.post('http://10.0.0.242:8080/login', params);
      } catch(err) { 
          this.wrongCredentials = true;
          return null; 
      }

      axios.defaults.headers.common['Authorization'] = 'Bearer ' + response.data.access_token;
      sessionStorage.setItem('accessToken', response.data.access_token);
      sessionStorage.setItem('username', this.username);
      this.$router.push('/timetable');
    },

    resetErrorLabel() {
      this.wrongCredentials = false;  
    }

  }
}
</script>

<style scoped>
.loginIcon {
  border-radius: 5px; 
  padding: 7px 15px 0 15px; 
  background: #4b9183; 
  color: white;
  border-top-right-radius: 0;
  border-bottom-right-radius: 0;
}
</style>

