import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    selectedTraining: null,
  },

  getters: {
    selectedTraining(state) {
      return state.selectedTraining;
    },
  },

  mutations: {
    selectedTraining(state, training) {
      state.selectedTraining = training;
    },
  }

});
