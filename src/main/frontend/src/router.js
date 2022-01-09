import Vue from 'vue'
import Router from 'vue-router'
import Login from './components/Login.vue'
import Home from './components/Home.vue'
import Timetable from './components/Timetable.vue'
import TrainingDetails from './components/TrainingDetails.vue'

Vue.use(Router)

export default new Router ({
  mode: 'history',
  routes: [
    {path: '/', component: Login},
    {path: '/login', component: Login},
    {path: '/home', name: 'home', component: Home,
      children: [
        { path: '/timetable', name: 'timetable', component: Timetable },
        { path: '/trainingdetails/training:training', name: 'trainingdetails', components: {default: TrainingDetails}}
      ]
    }
  ]
})

