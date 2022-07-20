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
const regex = new RegExp("^[a-zA-Z -]+$"); 
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

<!--
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
-->

<style>
.detailsInput {
  border: 1px solid #b1acac !important;
  border-radius: 0px !important;
  border-bottom-left-radius: 5px !important;
  border-bottom-right-radius: 5px !important;
  border-top: none !important;
  color: #35495e !important;
}

.readonly {
  background: #316286;
  border-radius: 5px;
  color: white;
  padding: 5px;
  font-weight: bold;
  font-size: 13px;
  margin: 3px;
  display: inline-block;
}

.readonlyDigit {
  background: #c28b8b;
  padding: 4px 18px 4px 18px;
}

/* multiselect */
fieldset[disabled] .multiselect {
  pointer-events: none;
}

.multiselect__spinner {
  position: absolute;
  right: 1px;
  top: 1px;
  width: 48px;
  height: 35px;
  background: #fff;
  display: block;
}

.multiselect__spinner:before,
.multiselect__spinner:after {
  position: absolute;
  content: "";
  top: 50%;
  left: 50%;
  margin: -8px 0 0 -8px;
  width: 16px;
  height: 16px;
  border-radius: 100%;
  border-color: #41b883 transparent transparent;
  border-style: solid;
  border-width: 2px;
  box-shadow: 0 0 0 1px transparent;
}

.multiselect__spinner:before {
  animation: spinning 2.4s cubic-bezier(0.41, 0.26, 0.2, 0.62);
  animation-iteration-count: infinite;
}

.multiselect__spinner:after {
  animation: spinning 2.4s cubic-bezier(0.51, 0.09, 0.21, 0.8);
  animation-iteration-count: infinite;
}

.multiselect__loading-enter-active,
.multiselect__loading-leave-active {
  transition: opacity 0.4s ease-in-out;
  opacity: 1;
}

.multiselect__loading-enter,
.multiselect__loading-leave-active {
  opacity: 0;
}

.multiselect,
.multiselect__input,
.multiselect__single {
  font-family: inherit;
  font-size: 16px;
  touch-action: manipulation;
}

.multiselect {
  box-sizing: content-box;
  display: block;
  position: relative;
  width: 100%;
  min-height: 40px;
  text-align: left;
  color: #35495e;
}

.multiselect * {
  box-sizing: border-box;
}

.multiselect:focus {
  outline: none;
}

.multiselect--disabled {
  background: #ededed;
  pointer-events: none;
  opacity: 0.6;
}

.multiselect--active {
  z-index: 50;
}

.multiselect--active:not(.multiselect--above) .multiselect__current,
.multiselect--active:not(.multiselect--above) .multiselect__input,
.multiselect--active:not(.multiselect--above) .multiselect__tags {
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
}

.multiselect--active .multiselect__select {
  transform: rotateZ(180deg);
}

.multiselect--above.multiselect--active .multiselect__current,
.multiselect--above.multiselect--active .multiselect__input,
.multiselect--above.multiselect--active .multiselect__tags {
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}

.multiselect__input,
.multiselect__single {
  position: relative;
  display: inline-block;
  min-height: 20px;
  line-height: 20px;
  border: none;
  border-radius: 5px;
  background: #fff;
  padding: 0 0 0 5px;
  width: calc(100%);
  transition: border 0.1s ease;
  box-sizing: border-box;
  margin-bottom: 8px;
  vertical-align: top;
}

.multiselect__input::placeholder {
  color: #35495e;
}

.multiselect__tag ~ .multiselect__input,
.multiselect__tag ~ .multiselect__single {
  width: auto;
}

.multiselect__input:hover,
.multiselect__single:hover {
  border-color: #cfcfcf;
}

.multiselect__input:focus,
.multiselect__single:focus {
  border-color: #a8a8a8;
  outline: none;
}

.multiselect__single {
  padding-left: 5px;
  margin-bottom: 8px;
}

.multiselect__tags-wrap {
  display: inline;
}

.multiselect__tags {
  min-height: 40px;
  display: block;
  padding: 8px 40px 0 8px;
  /*border-radius: 5px;*/
  border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
  border: 1px solid #b1acac;
  border-top: none;
  background: #fff;
  font-size: 14px;
}

.multiselect__tag {
  position: relative;
  display: inline-block;
  padding: 4px 26px 4px 10px;
  border-radius: 5px;
  margin-right: 10px;
  color: white;
  font-weight: bold;
  line-height: 1;
  background: #316286;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  max-width: 100%;
  text-overflow: ellipsis;
}

.multiselect__tag-icon {
  cursor: pointer;
  margin-left: 7px;
  position: absolute;
  right: 0;
  top: 0;
  bottom: 0;
  font-weight: 700;
  font-style: initial;
  width: 22px;
  text-align: center;
  line-height: 22px;
  transition: all 0.2s ease;
  border-radius: 5px;
}

.multiselect__tag-icon:after {
  content: "×";
  color: white;
  font-weight: bold;
  font-size: 14px;
}

.multiselect__tag-icon:focus,
.multiselect__tag-icon:hover {
  background: #369a6e;
}

.multiselect__tag-icon:focus:after,
.multiselect__tag-icon:hover:after {
  color: white;
}

.multiselect__current {
  line-height: 16px;
  min-height: 40px;
  box-sizing: border-box;
  display: block;
  overflow: hidden;
  padding: 8px 12px 0;
  padding-right: 30px;
  white-space: nowrap;
  margin: 0;
  text-decoration: none;
  border-radius: 5px;
  border: 1px solid #e8e8e8;
  cursor: pointer;
}

.multiselect__select {
  line-height: 16px;
  display: block;
  position: absolute;
  box-sizing: border-box;
  width: 40px;
  height: 38px;
  right: 1px;
  top: 1px;
  padding: 4px 8px;
  margin: 0;
  text-decoration: none;
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s ease;
}

.multiselect__select:before {
  position: relative;
  right: 0;
  top: 65%;
  color: #999;
  margin-top: 4px;
  border-style: solid;
  border-width: 5px 5px 0 5px;
  border-color: #999999 transparent transparent transparent;
  content: "";
}

.multiselect__placeholder {
  color: #adadad;
  display: inline-block;
  margin-bottom: 10px;
  padding-top: 2px;
}

.multiselect--active .multiselect__placeholder {
  display: none;
}

.multiselect__content-wrapper {
  display: block;
  background: #fff;
  width: 100%;
  max-height: 240px;
  overflow: none;
  border-top: 4px solid black !important;
  /*border-bottom-left-radius: 5px;
  border-bottom-right-radius: 5px;
  z-index: 50;*/
  -webkit-overflow-scrolling: touch;
}

.multiselect__content {
  list-style: none;
  display: inline-block;
  padding: 0;
  margin: 0;
  min-width: 100%;
  vertical-align: top;
}

.multiselect--above .multiselect__content-wrapper {
  bottom: 100%;
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
  border-top-left-radius: 5px;
  border-top-right-radius: 5px;
  border-bottom: none;
  border-top: 1px solid #e8e8e8;
}

.multiselect__content::webkit-scrollbar {
  display: none;
}

.multiselect__element {
  display: block;
  border-bottom: 1px dashed black;
}

.multiselect__option {
  display: block;
  padding: 12px;
  min-height: 40px;
  line-height: 16px;
  text-decoration: none;
  text-transform: none;
  vertical-align: middle;
  position: relative;
  cursor: pointer;
  white-space: nowrap;
  font-weight: bold;
}

/* when hovering over non-selected items the div on the right*/
.multiselect__option:after {
  top: 0;
  right: 0;
  position: absolute;
  line-height: 40px;
  padding-right: 12px;
  padding-left: 20px;
  font-size: 13px;
  background: #316286;
  color: white !important;
}

/* when hovering over non-selected elements */
.multiselect__option--highlight {
  /* background: #41b883; */
  background: none;
  color: #316286;
}

.multiselect__option--highlight:after {
  content: attr(data-select);
  color: white;
}

.multiselect__option--selected {
  background: #316286;
  color: white;
  font-weight: bold;
}

.multiselect__option--selected:after {
  content: attr(data-selected);
  color: silver;
}

.multiselect__option--selected.multiselect__option--highlight {
  /*background: #ff6a6a;*/
  color: white;
}

/* when hovering over selected items */
.multiselect__option--selected.multiselect__option--highlight:after {
  background: none;
  content: attr(data-deselect);
  color: red !important;
}

.multiselect--disabled .multiselect__current,
.multiselect--disabled .multiselect__select {
  background: #ededed;
  color: #a6a6a6;
}

.multiselect__option--disabled {
  background: #ededed !important;
  color: #a6a6a6 !important;
  cursor: text;
  pointer-events: none;
}

.multiselect__option--group {
  background: #ededed;
  color: #35495e;
}

.multiselect__option--group.multiselect__option--highlight {
  background: #35495e;
  color: #fff;
}

.multiselect__option--group.multiselect__option--highlight:after {
  background: #35495e;
}

.multiselect__option--disabled.multiselect__option--highlight {
  background: #dedede;
}

.multiselect__option--group-selected.multiselect__option--highlight {
  background: #ff6a6a;
  color: #fff;
}

.multiselect__option--group-selected.multiselect__option--highlight:after {
  background: #ff6a6a;
  content: attr(data-deselect);
  color: #fff;
}

.multiselect-enter-active,
.multiselect-leave-active {
  transition: all 0.15s ease;
}

.multiselect-enter,
.multiselect-leave-active {
  opacity: 0;
}

.multiselect__strong {
  margin-bottom: 8px;
  line-height: 20px;
  display: inline-block;
  vertical-align: top;
}

*[dir="rtl"] .multiselect {
  text-align: right;
}

*[dir="rtl"] .multiselect__select {
  right: auto;
  left: 1px;
}

*[dir="rtl"] .multiselect__tags {
  padding: 8px 8px 0px 40px;
}

*[dir="rtl"] .multiselect__content {
  text-align: right;
}

*[dir="rtl"] .multiselect__option:after {
  right: auto;
  left: 0;
}

*[dir="rtl"] .multiselect__clear {
  right: auto;
  left: 12px;
}

*[dir="rtl"] .multiselect__spinner {
  right: auto;
  left: 1px;
}

@keyframes spinning {
  from {
    transform: rotate(0);
  }
  to {
    transform: rotate(2turn);
  }
}
</style>

