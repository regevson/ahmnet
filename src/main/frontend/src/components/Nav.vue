<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <img src="../../public/img/logo.png" style="width: 20%;">
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li v-if="!isLoggedIn" class="nav-item active">
            <router-link to="./login"><a class="nav-link" href="#">Log In <span class="sr-only">(current)</span></a></router-link>
          </li>
          <li v-if="isLoggedIn && hasRole('ADMIN')" class="nav-item active">
            <a class="nav-link" href="#">Admin-Functions</a>
          </li>
           <b-nav-item-dropdown v-if="isLoggedIn && hasRole('TRAINER')" text="Trainer-Funktionen" right>
            <b-dropdown-item href="#"><a href="#" v-on:click="returnRoute('traininggroups')">Trainingsgruppen</a></b-dropdown-item>
           </b-nav-item-dropdown>
        </ul>
      </div>
    </nav>

  </div>
</template>

<script>

export default {
  name: 'Nav',
  props: {
    user: Object
  },

  computed: {
    isLoggedIn: function() {
      return this.user != null;
    }
  },

  methods: {
    hasRole: function(role) {
      return this.user.roles.includes(role);
    },
    returnRoute: function(newRouteName) {
      this.$emit('newRoute', newRouteName);
    }
  }
}


</script>

