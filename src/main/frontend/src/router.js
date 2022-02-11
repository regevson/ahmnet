import Vue from 'vue'
import Router from 'vue-router'
import Login from './components/Login.vue'
import Home from './components/Home.vue'
import Timetable from './components/Timetable.vue'
import TrainingDetails from './components/TrainingDetails.vue'
import TrainingGroups from './components/TrainingGroups.vue'
import TrainingGroupDetails from './components/TrainingGroupDetails.vue'

Vue.use(Router)

export default new Router ({
  mode: 'history',
  routes: [
    {path: '/', component: Login},
    {path: '/login', name: 'login', component: Login},
    {path: '/home', name: 'home', component: Home,
      children: [
        { path: '/timetable', name: 'timetable', component: Timetable },
        { path: '/traininggroups', name: 'traininggroups', component: TrainingGroups },
        { path: '/traininggroupdetails/group:groupId', name: 'traininggroupdetails', components: {default: TrainingGroupDetails} },
        { path: '/trainingdetails/training:trainingId', name: 'trainingdetails', components: {default: TrainingDetails}}
      ]
    }
  ]
})
