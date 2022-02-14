import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    selectedTrainer: null,
    weekNum: null,
  },

  getters: {
    selectedTrainer(state) {
      return state.selectedTrainer;
    },

    weekNum(state) {
      if(state.weekNum == null)
        state.weekNum = calcCurrentWeekNum();
      return state.weekNum;
    },

  },

  mutations: {
    selectedTrainer(state, trainer) {
      state.selectedTrainer = trainer;
    },

    weekNum(state, weekNum) {
      state.weekNum = weekNum;
    },
  }

});

function calcCurrentWeekNum() {
  let currentDate = new Date();
  var oneJan = new Date(currentDate.getFullYear(),0,1);
  var numberOfDays = Math.floor((currentDate - oneJan) / (24 * 60 * 60 * 1000));
  return Math.ceil(( currentDate.getDay() + 1 + numberOfDays) / 7) - 1;
}
