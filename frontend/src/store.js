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
  const date = new Date();
  const onejan = new Date(date.getFullYear(), 0, 1);
  return Math.ceil((((date.getTime() - onejan.getTime()) / 86400000) + onejan.getDay()-1) / 7) - 1;
}
