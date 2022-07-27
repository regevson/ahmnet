<template>
  <div>
    <form class="detailsForm" v-if="player" @submit.prevent="">
      <div align="center" v-if="!isNewPlayer()">
        <p class="entry" style="background: #1b2730; border-radius: 5px">
          {{firstName}} {{lastName}} (@{{playerId}})
          <i
            v-if="!permChangePlayer()"
            class="fa-solid fa-lock fa-sm"
            style="color: orange; margin-left: 5px"
          ></i>
        </p>
      </div>
      <p v-if="isNewPlayer()" class="entry" style="background: #1b2730; border-radius: 5px; text-align: center;">
        Neuer Spieler
      </p>
      <br />

      <p class="entry">Club:</p>
      <multiselect
        :disabled="!permChangePlayer()"
        :allowEmpty="false"
        v-model="player.clubId"
        :options="allClubs"
        placeholder="Auswählen/Suchen"
        deselectLabel=""
        selectLabel=""
      />
      <br />

      <p class="entry" :class="{'errorBg': $v.firstName.$invalid}"> Vorname:</p>
      <b-form-input v-model="firstName" placeholder="z.B. Max" :disabled="!permChangePlayer()"
        :class="[$v.firstName.$invalid ? 'form-error' : 'detailsInput']">
      </b-form-input>
      <div class="errorText" v-if="$v.firstName.$invalid">
        nicht leer & nur Buchstaben
      </div>
      <br />
      
      <p class="entry" :class="{'errorBg': $v.lastName.$invalid}"> Nachname:</p>
      <b-form-input v-model="lastName" placeholder="z.B. Mustermann" :disabled="!permChangePlayer()"
        :class="[$v.lastName.$invalid ? 'form-error' : 'detailsInput']">
      </b-form-input>
      <div class="errorText" v-if="$v.lastName.$invalid">
        nicht leer & nur Buchstaben
      </div>
      <br />

      <p class="entry" :class="{'errorBg': $v.birthYear.$invalid}"> Geburtsjahr:</p>
      <b-form-input v-model="birthYear" type="number" placeholder="z.B. 2015" :disabled="!permChangePlayer()"
        :class="[$v.birthYear.$invalid ? 'form-error' : 'detailsInput']">
      </b-form-input>
      <div class="errorText" v-if="$v.birthYear.$invalid">
        Bitte korrekt ausfüllen!
      </div>
      <br />

      <p class="entry">E-Mail:</p>
      <b-form-input v-model="player.email" type="email" placeholder="z.B. beispiel@gmail.com" :disabled="!permChangePlayer()"></b-form-input>
      <br />

      <p class="entry">Tel-Nummer:</p>
      <b-form-input v-model="player.phone" type="text" placeholder="z.B. 069910983630" :disabled="!permChangePlayer()"></b-form-input>
      <br />

      <div align="center" v-if="isNewPlayer()">
        <input
          class="changeBtn fourth"
          type="submit"
          v-b-modal="'createDialog'"
          value="Erstellen"
          @click="setDialogTxt('create')"
        />
      </div>

      <div align="center" v-if="!isNewPlayer()">
        <input
          v-if="permChangePlayer()"
          class="changeBtn fourth"
          type="submit"
          v-b-modal="'changeDialog'"
          value="Anpassen"
          @click="setDialogTxt('change')"
        />

        <button
          v-if="permChangePlayer()"
          @click="setDialogTxt('delete')"
          v-b-modal="'deleteDialog'"
          class="deleteBtn"
        >
          Löschen
        </button>
      </div>
    </form>

    <b-modal
      centered
      @ok="createNewPlayer"
      id="createDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >

    <b-modal
      centered
      @ok="updatePlayerDetails"
      id="changeDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >

    <b-modal
      centered
      @ok="deletePlayer"
      id="deleteDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >

    <h5 v-if="!player" class="loading">LOADING...</h5>
  </div>
</template>

<script>
import Multiselect from 'vue-multiselect'

import { required, minValue, maxLength } from 'vuelidate/lib/validators'
const regex = new RegExp("^[a-zA-Z,Ö,Ä,Ü,ö,ä,ü,ß -]+$"); 
const alphaAndSpaceValidator = (txt) => regex.test(txt);

export default {
  name: 'PlayerDetails',
  components: {Multiselect},
  data() {
    return {
      user: null,
      isAdmin: false,
      playerId: -1,
      player: null,
      trainers: [],
      allClubs: [],

      // fields to validate
      firstName: '',
      lastName: '',
      birthYear: 2015,

      dialogTxt: '',
    }
  },

  validations: {
    firstName: { required, maxLengthValue: maxLength(30), alphaAndSpaceValidator },
    lastName: { required, maxLengthValue: maxLength(30), alphaAndSpaceValidator },
    birthYear: {
      required,
      minValue: minValue(1920),
    }
  },

  async created() {
    this.playerId = this.$route.params.playerId;
    this.getUserRole();
    this.getFormData();
  },

  methods: {
    getUserRole() {
      this.user = JSON.parse(sessionStorage.getItem('user'));
      this.isAdmin = this.user.roles.includes("ADMIN");
    },

    async getFormData() {
      await this.getAllClubs();

      if(this.playerId == -1) // is new player
        this.setupRequest();
      else {
        let player = await this.getPlayer();
        this.getTrainers(player);
        this.setupRequest(player.firstName, player.lastName, player.birthYear, player.email, player.phone, player.clubId, player.roles);
      }
      this.setValidationFields();
    },

    async getAllClubs() {
      const res = await this.$ax.get('clubs');
      this.allClubs = res.data.map(c => c.id);
    },

    async getPlayer() {
      let player_res = await this.$ax.get('users/' + this.playerId);
      return player_res.data[0];
    },

    setupRequest(firstName='', lastName='', birthYear=2015, email='', phone='', clubId='TC-Breitenbach', roles=['PLAYER']) {
      let player = {};
      player.firstName = firstName;
      player.lastName = lastName;
      player.birthYear = birthYear;
      player.email = email;
      player.phone = phone;
      player.clubId = clubId;
      player.roles = roles;

      this.player = player;
    },

    setValidationFields() {
      this.firstName = this.player.firstName;
      this.lastName = this.player.lastName;
      this.birthYear = this.player.birthYear;
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

    // copy validation-fields into dto to be sent
    copyValidationFields() {
      this.player.firstName = this.firstName;
      this.player.lastName = this.lastName;
      this.player.birthYear = this.birthYear;
    },

    async createNewPlayer() {
      if(!this.validate()) return;
      this.copyValidationFields();
      await this.$ax.post('users', this.player);
      this.$router.push({name: 'players'});
    },

    async updatePlayerDetails() {
      if(!this.validate()) return;
      this.copyValidationFields();
      await this.$ax.put('users/' + this.playerId, this.player);
      this.$router.push({name: 'players'});
    },

    async deletePlayer() {
      await this.$ax.delete('users/' + this.playerId);
      this.$router.push({name: 'players'});
    },

    permChangePlayer() {
      console.log(this.trainers.includes(this.user.id));
      if(this.playerId != -1 && this.user.trainingGroupIds.length < 1)
        return false;
      return this.isAdmin || this.trainers.includes(this.user.id) || this.playerId == -1;
    },

    async getTrainers(player) {
      const groups_res = await this.$ax.get(player.groups_url);
      let groups = groups_res.data;
      this.trainers = groups.map(g => g.trainerId);
    },

    isNewPlayer() {
      return this.playerId == -1;
    },

    setDialogTxt(cmd) {
      if(cmd === 'create')
        this.dialogTxt = "Spieler erstellen?";
      else if(cmd === 'change')
        this.dialogTxt = "Spieler verändern?";
      else if(cmd === 'delete')
        this.dialogTxt = "Spieler löschen?";
    },

  },

}
</script>
