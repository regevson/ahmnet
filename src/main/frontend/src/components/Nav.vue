<template>
  <div>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <a class="navbar-brand" href="#">ahmNet</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li v-if="!isLoggedIn" class="nav-item active">
            <router-link to="./Login"><a class="nav-link" href="#">Log In <span class="sr-only">(current)</span></a></router-link>
          </li>
          <li v-if="isLoggedIn && hasRole('ADMIN')" class="nav-item active">
            <a class="nav-link" href="#">Admin-Functions</a>
          </li>
          <li v-if="isLoggedIn && hasRole('TRAINER')" class="nav-item active">
            <a class="nav-link" href="#">Trainer-Functions</a>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
              Dropdown
            </a>
            <div class="dropdown-menu" aria-labelledby="navbarDropdown">
              <a class="dropdown-item" href="#">Action</a>
              <a class="dropdown-item" href="#">Another action</a>
              <div class="dropdown-divider"></div>
              <a class="dropdown-item" href="#">Something else here</a>
            </div>
          </li>
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
    }
  }
}


</script>

