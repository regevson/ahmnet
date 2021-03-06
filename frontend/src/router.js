import Vue from 'vue'
import Router from 'vue-router'
import Login from './components/Login.vue'
import Home from './components/Home.vue'
import Timetable from './components/Timetable.vue'
import Vacationtable from './components/Vacationtable.vue'
import TrainingDetails from './components/TrainingDetails.vue'
import TrainingGroups from './components/TrainingGroups.vue'
import Players from './components/Players.vue'
import PlayerDetails from './components/PlayerDetails.vue'
import TrainingGroupDetails from './components/TrainingGroupDetails.vue'
import Password from './components/Password.vue'

Vue.use(Router)

export default new Router ({
  mode: 'history',
  routes: [
    {path: '/', component: Login},
    {path: '/login', name: 'login', component: Login},
    {path: '/home', name: 'home', component: Home,
      children: [
        { path: '/timetable', name: 'timetable', component: Timetable },
        { path: '/vacationtable', name: 'vacationtable', component: Vacationtable },
        { path: '/traininggroups', name: 'traininggroups', component: TrainingGroups },
        { path: '/players', name: 'players', component: Players },
        { path: '/group/:groupId', name: 'traininggroupdetails', components: {default: TrainingGroupDetails} },
        { path: '/training/:trainingId', name: 'trainingdetails', components: {default: TrainingDetails}},
        { path: '/player/:playerId', name: 'playerdetails', components: {default: PlayerDetails}},
        { path: '/password', name: 'password', components: {default: Password}},
      ]
    }
  ]
})
