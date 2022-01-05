<template>
  <div>

    <nav class="navbar navbar-expand-lg navbar-light bg-light">
      <a class="navbar-brand" href="#">ahmNet</a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>

      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav mr-auto">
          <li v-if="is_logged_in" class="nav-item active">
            <a class="nav-link" href="#">Logged In <span class="sr-only">(current)</span></a>
          </li>
          <li v-if="is_admin" class="nav-item active">
            <a class="nav-link" href="#">Admin-Functions</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Link</a>
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
          <li class="nav-item">
            <a class="nav-link disabled" href="#">Disabled</a>
          </li>
        </ul>
        <form class="form-inline my-2 my-lg-0">
          <input class="form-control mr-sm-2" type="search" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-success my-2 my-sm-0" type="submit">Search</button>
        </form>
      </div>
    </nav>

  </div>
</template>

<script>
import {bus} from '../main'

export default {
  name: 'Nav',

  data() {
    return {
      is_logged_in: false,
      is_admin: false,
      is_trainer: false,
      is_player: false
    }
  },

  created() {
    bus.$on('updateNav', (data) => {
      this.is_logged_in = data["loggedIn"];
      if(this.is_logged_in) {
        this.is_admin = data["roles"].includes("ADMIN");
        this.is_trainer = data["roles"].includes("TRAINER");
        this.is_player = data["roles"].includes("PLAYER");
      }
      else
        this.is_admin = this.is_trainer = this.is_player = false;
    })
  }

}

</script>
