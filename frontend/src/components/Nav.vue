<template>
  <div>
    <b-navbar toggleable="lg" type="dark" variant="info">
      <a href="timetable" style="display: contents">
        <img
          href="timetable"
          src="../../public/img/logo.png"
          style="
            width: 8%;
            min-width: 110px;
            display: block;
            margin-right: 20px;
          "
        />
      </a>

      <b-navbar-toggle target="nav-collapse"></b-navbar-toggle>

      <b-collapse id="nav-collapse" is-nav>
        <b-navbar-nav>
          <b-nav-item v-if="!isLoggedIn" :to="{name: 'login'}" class="nav-link">
            Login
          </b-nav-item>
          <b-nav-item
            v-if="isLoggedIn"
            :to="{name: 'timetable'}"
            class="nav-link"
          >
            TRAININGS
          </b-nav-item>
          <b-nav-item
            v-if="isLoggedIn && hasRole('TRAINER')"
            :to="{name: 'traininggroups'}"
            class="nav-link"
          >
            GRUPPEN
          </b-nav-item>
          <b-nav-item
            v-if="isLoggedIn && hasRole('TRAINER')"
            :to="{name: 'players'}"
            class="nav-link"
          >
            SPIELER
          </b-nav-item>
          <b-nav-item
            v-if="isLoggedIn && hasRole('TRAINER')"
            :to="{name: 'vacationtable'}"
            class="nav-link"
          >
            URLAUBSTAFEL
          </b-nav-item>
        </b-navbar-nav>

        <!-- Right aligned nav items -->
        <b-navbar-nav class="ml-auto">
          <b-nav-item-dropdown right v-if="user">
            <!-- Using 'button-content' slot -->
            <template #button-content>
              <a class="nav-link" style="display: inline-block">{{user.id}}</a>
            </template>
            <span class="role">{{user.roles[0]}}</span>
            <b-dropdown-item class="dropdownLink" :to="{name: 'login'}">LOG OUT</b-dropdown-item>
            <hr style="margin: 0">
            <b-dropdown-item class="dropdownLink" :to="{name: 'password'}">PASSWORT</b-dropdown-item>
          </b-nav-item-dropdown>
        </b-navbar-nav>
      </b-collapse>
    </b-navbar>
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
      // unused
      this.$emit('newRoute', newRouteName);
    }
  }
}
</script>

<style scoped>
.navbar {
  background: #1b2730 !important;
  color: white;
  font-weight: bold;
}

.nav-link {
  color: white !important;
}

.nav-link:hover {
  color: #bf8a4a !important;
}

.router-link-active {
  color: #bf8a4a !important;
  font-weight: 800;
  font-size: 19px;
}

.role {
  font-weight: bold;
  font-size: 12px;
  margin-left: 5px;
  background: #c28b8b;
  color: white;
  padding: 5px;
  border-radius: 5px;
}

.dropdownLink {
  padding: 5px 0 5px 0;
}
</style>

