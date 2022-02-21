<template>
  <div align="left">
    <form v-if="training" @submit.prevent="">

      <div align="center" v-if="!isNewTraining()">
        <p class="entry" style="background: #1b2730; border-radius: 5px">
          Training {{training.id}}
          <i
            v-if="isFree()"
            class="fa-solid fa-lock fa-sm"
            style="color: orange; margin-left: 5px"
          ></i>
          <i
            v-if="training.lastDate"
            class="fa-solid fa-repeat fa-sm"
            style="color: orange; margin-left: 5px"
          ></i>
        </p>
          <span v-if="training.lastDate" style="font-size: 13px; float: right; margin-top: 3px;"> (bis {{training.lastDate}})</span>

      </div>

      <p v-if="isNewTraining()" class="entry" style="background: #1b2730; border-radius: 5px; text-align: center;">
        Neues Training
      </p>
      <br />

      <p class="entry">Club:</p>
      <multiselect
        :disabled="isFree()"
        :allowEmpty="false"
        v-model="training.club"
        :options="allClubs"
        placeholder="Club suchen"
        label="name"
        track-by="name"
        deselectLabel=""
        selectLabel=""
      />
      <br />

      <p class="entry">Gruppe:</p>
      <multiselect
        :disabled="isFree()"
        :allowEmpty="false"
        v-model="training.group"
        :options="allGroups"
        placeholder="Gruppe suchen"
        label="combinedInfo"
        track-by="combinedInfo"
        deselectLabel=""
        selectLabel=""
      />
      <br />

      <p class="entry">Datum:</p>
      <b-form-datepicker
        :disabled="isFree()"
        v-model="training.date"
        class="mb-2 detailsInput"
        locale="de"
        :date-format-options="{ year: 'numeric', month: '2-digit', day: '2-digit'}"
        :showDecadeNav="false"
        :start-weekday="1"
        :hide-header="true"
        calendar-width="100%"
        label-help=""
        menu-class="w-100"
      >
      </b-form-datepicker>
      <br />

      <p class="entry">Zeit:</p>
      <b-form-timepicker
        :disabled="isFree()"
        v-model="training.startTime"
        class="detailsInput"
        locale="de"
        :minutes-step="15"
        :hide-header="true"
        :start-time="10"
      >
      </b-form-timepicker>
      <br />

      <p class="entry" :class="{'errorBg': $v.durationMinutes.$invalid}">
        Dauer:
      </p>
      <b-form-input
        :disabled="isFree()"
        type="number"
        v-model="durationMinutes"
        step="15"
        min="0"
        placeholder="Dauer in Minuten"
        :class="[$v.durationMinutes.$invalid ? 'form-error' : 'detailsInput']"
      ></b-form-input>
      <div class="errorText" v-if="!$v.durationMinutes.required">
        Bitte ausfüllen!
      </div>
      <br />

      <p class="entry" :class="{'errorBg': $v.court.$invalid}">Court:</p>
      <b-form-input
        :disabled="isFree()"
        type="number"
        v-model="court"
        min="1"
        :class="[$v.court.$invalid ? 'form-error' : 'detailsInput']"
      ></b-form-input>
      <div class="errorText" v-if="!$v.court.required">Bitte ausfüllen!</div>
      <br />

      <p class="entry">TrainerIn:</p>
      <multiselect
        :disabled="!isAdmin || isFree()"
        :allowEmpty="false"
        v-model="training.trainer"
        :options="allTrainers"
        placeholder="TrainerIn suchen"
        label="fullName"
        track-by="fullName"
        deselectLabel=""
        selectLabel=""
      />
      <br />

      <div v-if="training.id != -1">
        <p class="entry">Anwesenheit SpielerInnen:</p>
        <div style="margin-top: 10px" class="form-check">
          <div v-for="player in training.players" :key="player.id">
            <input
              class="form-check-input"
              :disabled="isFree()"
              type="checkbox"
              :value="player.id"
              :id="player.id"
              v-model="training.attendees"
            />
            <label class="form-check-label" :for="player.id">
              <span> {{player.firstName}} {{player.lastName}} </span>
            </label>
          </div>
        </div>
        <br />
      </div>

      <p class="entry">Schwerpunkte:</p>
      <textarea
        :disabled="isFree()"
        v-model="training.bulletPoints"
        rows="4"
        style="width: 100%; padding: 5px"
        class="detailsInput"
      >
      </textarea>
      <br />
      <br />

      <p class="entry">Kommentare:</p>
      <textarea
        :disabled="isFree()"
        v-model="training.comments"
        rows="4"
        style="width: 100%; padding: 5px"
        class="detailsInput"
      >
      </textarea>
      <br />

    <div v-if="isNewTraining()" style="margin-top: 10px" class="form-check">
      <input
        class="form-check-input"
        :disabled="isFree()"
        type="checkbox"
        v-model="isRecurring"
        id="recurring"
      />
      <label class="form-check-label" for="recurring">
        <span>Regelmäßiges Training?</span>
      </label>
      <br>
    </div>


      <div v-if="isNewTraining() && isRecurring">
        <p class="entry" :class="{'errorBg': $v.lastDate.$invalid}">Bis wann eintragen? <span style="font-size: 13px; color: silver;">(Datum letztes Training)</span></p>
        <b-form-datepicker
          :disabled="isFree()"
          v-model="lastDate"
          class="mb-2 detailsInput"
          locale="de"
          :date-format-options="{ year: 'numeric', month: '2-digit', day: '2-digit'}"
          :showDecadeNav="false"
          :start-weekday="1"
          :hide-header="true"
          :class="[$v.lastDate.$invalid ? 'form-error' : 'detailsInput']"
          calendar-width="100%"
          label-help=""
          menu-class="w-100"
        >
        </b-form-datepicker>
        <div class="errorText" v-if="!$v.lastDate.required">Bitte ausfüllen!</div>
      </div>




      <div align="center" v-if="training.id == -1">
        <input
          class="changeBtn fourth"
          type="submit"
          @click="setDialogTxt('create')"
          v-b-modal="'createDialog'"
          value="Erstellen"
        />
      </div>

      <div align="center" v-if="training.id != -1">
        <input
          v-if="!isFree()"
          class="changeBtn fourth"
          type="submit"
          @click="setDialogTxt('change')"
          v-b-modal="'changeDialog'"
          value="Anpassen"
        />
        <button
          v-if="!isFree()"
          v-b-modal="'deleteDialog'"
          @click="setDialogTxt('delete')"
          class="deleteBtn"
        >
          Löschen
        </button>
        <button
          v-if="!isFree()"
          v-b-modal="'freeDialog'"
          @click="setDialogTxt('free')"
          class="freeBtn"
        >
          Freigeben
        </button>
        <button
          v-if="isFree()"
          v-b-modal="'grabDialog'"
          @click="setDialogTxt('grab')"
          class="freeBtn"
        >
          Übernehmen
        </button>
      </div>
    </form>

    <b-modal
      centered
      @ok="updateTrainingDetails"
      id="createDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >

    <b-modal
      centered
      @ok="updateTrainingDetails"
      id="changeDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >

    <b-modal
      centered
      @ok="deleteTraining"
      id="deleteDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >

    <b-modal
      centered
      @ok="freeTraining"
      id="freeDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >

    <b-modal
      centered
      @ok="grabTraining"
      id="grabDialog"
      title="Bestätigung"
      >{{dialogTxt}}</b-modal
    >

    <h5 v-if="!training" class="loading">LOADING...</h5>
  </div>
</template>

<script>
import { axiosReq } from '../axios'
import Multiselect from 'vue-multiselect'
import { required, requiredIf, minValue } from 'vuelidate/lib/validators'
import qs from 'qs'

export default {
  name: 'TrainingDetails',
  components: {Multiselect},
  data() {
    return {
      user: null,
      isAdmin: false,
      training: null,
      allClubs: [],
      allGroups: [],
      allTrainers: [],

      // fields to validate
      durationMinutes: 60,
      court: 1,

      dialogTxt: '',
      isRecurring: false,
      lastDate: '',
    }
  },

  validations: {
    durationMinutes: {
      required,
      minValue: minValue(1),
    },
    court: {
      required,
      minValue: minValue(1),
    },
    lastDate: {
      required: requiredIf(function() {
        return this.isRecurring;
      }),
      minValue(lastDate) {
        return !this.isRecurring || new Date(lastDate) > new Date();
      }
    }
  },

  async created() {
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
      await this.getAllGroups();
      if(this.isAdmin)
        await this.getAllTrainer();

      if(this.$route.params.trainingId == -1) // is new training
        await this.setupNewTraining();
      else
        await this.setupTraining();
    },

    async getAllClubs() {
      const res = await axiosReq('allClubs');
      this.allClubs = res.data;
    },

    async getAllGroups() {
      const res = await axiosReq('allGroups');
      this.allGroups = res.data;
      this.allGroups.map(this.combineGroupInfo);
    },

    async getAllTrainer() {
      const res = await axiosReq('allTrainers');
      this.allTrainers = res.data;
    },

    async setupNewTraining() {
      const res = await axiosReq('newTraining');
      this.training = res.data;
      this.prepopulate();
    },

    prepopulate() {
      this.training.startTime = "10:30";
      this.training.durationMinutes = 60;
      this.training.free = false;
      const currDate = this.getCurrentDate();
      this.training.date = currDate;
      this.training.club = this.allClubs[0];
      this.training.group = this.allGroups[0];
      this.training.trainer = this.user;
    },

    getCurrentDate() {
      let curr = new Date();
      curr.setMinutes(curr.getMinutes()-curr.getTimezoneOffset())
      return curr;
    },

    async setupTraining() {
      const res = await axiosReq('training?id=' + this.$route.params.trainingId);
      this.training = res.data;
      this.combineGroupInfo(this.training.group);
      this.setValidationFields();
    },

    // group should also display info about participants in multiselect-row
    combineGroupInfo(group) {
      let fullNames = [];
      group.players.forEach((player) => { fullNames.push(player.fullName); });
      group.combinedInfo = 'Gruppe' + group.id + ': (' + group.club.name + ') [' + fullNames + ']';
    },

    setValidationFields() {
      this.durationMinutes = this.training.durationMinutes;
      this.court = this.training.court;
    },

    async updateTrainingDetails() {
      if(!this.validate())
        return;

      this.getValidationFields();

      const config = {headers: {'Content-Type': 'application/json'}}
      let params = JSON.stringify(this.training);
      await axiosReq('updateTrainingDetails', params, config);

      this.$router.push({name: 'timetable'});
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
    getValidationFields() {
      this.training.durationMinutes = this.durationMinutes;
      this.training.court = this.court;
      this.training.lastDate = this.lastDate;
    },

    async deleteTraining() {
      const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      let params = qs.stringify({'id': this.training.id});
      await axiosReq('deleteTraining', params, config);
      this.$router.push({name: 'timetable'});
    },

    async freeTraining() {
      const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      let params = qs.stringify({'id': this.training.id});
      await axiosReq('freeTraining', params, config);
      this.$router.push({name: 'timetable'});
    },

    async grabTraining() {
      const config = {headers: {'Content-Type': 'application/x-www-form-urlencoded'}}
      let params = qs.stringify({'id': this.training.id});
      await axiosReq('grabTraining', params, config);
      this.$router.push({name: 'vacationtable'});
    },

    isFree() {
      return this.training.free;
    },

    isNewTraining() {
      return this.training.id == -1;
    },

    setDialogTxt(cmd) {
      if(cmd === 'create')
        this.dialogTxt = "Training erstellen?";
      else if(cmd === 'change')
        this.dialogTxt = "Training verändern?";
      else if(cmd === 'delete')
        this.dialogTxt = "Training löschen?";
      else if(cmd === 'free')
        this.dialogTxt = "Training freigeben?";
      else if(cmd === 'grab')
        this.dialogTxt = "Training übernehmen?";
    },

  }


}
</script>

<style>
.entry {
  background: #499082;
  color: white;
  font-weight: bold;
  padding: 10px 5px 10px 5px;
  border-top-left-radius: 4px;
  border-top-right-radius: 4px;
  margin-bottom: 0px;
}

.form-error {
  border-color: #bf0000 !important;
  border-top-left-radius: 0px !important;
  border-top-right-radius: 0px !important;
  border-top: none !important;
}

.errorText {
  color: #bf0000;
  font-size: 13px;
  font-weight: bold;
  padding-left: 15px;
  margin-top: 5px;
}

.errorBg {
  background: #bf0000;
}
</style>

