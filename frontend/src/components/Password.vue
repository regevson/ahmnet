<template>
  <div>
    <div align="center">
      <h1>PASSWORT</h1>
      <br>
      <br>

      <p class="entry" :class="{'errorBg': $v.password.$invalid}" style="width: 60%">Neues Passwort:</p>
      <b-form-input
        type="text"
        v-model="password"
        style="width: 60%"
        :class="[$v.password.$invalid ? 'form-error' : 'detailsInput']"
      ></b-form-input>
      <div class="errorText" v-if="!$v.password.required">Bitte ausfüllen!</div>
      <br />

        <button
          v-b-modal="'changeDialog'"
          @click="setDialogTxt()"
          class="changeBtn"
        >
          Ändern
        </button>

    <b-modal
      centered
      @ok="changePassword"
      id="changeDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >


    </div>

  </div>
</template>

<script>
import { required } from 'vuelidate/lib/validators'

export default {
  name: 'Password',

  data() {
    return {
      password: '',
      dialogTxt: '',
    }
  },

  validations: {
    password: {
      required,
    }
  },

  methods: {
    async changePassword() {
      if(!this.validate())
        return;
      await this.$ax.post('users/' + sessionStorage.username + '/actions/change-password', {"password": this.password});
      this.$router.push({name: 'login'});
    },

    setDialogTxt() {
      this.dialogTxt = "Passwort wirklich ändern?";
    },

    validate() {
      this.$v.$touch();
      if(this.$v.$invalid) {
        alert('Bitte füllen Sie das Formular korrekt aus!');
        return false;
      }
      else
        return true;
    },
  },
}

</script>
