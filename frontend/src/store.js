import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    selectedTrainer: null,
    selectedDate: null,
  },

  getters: {
    selectedTrainer(state) {
      return state.selectedTrainer;
    },

    selectedDate(state) {
      return state.selectedDate;
    },

  },

  mutations: {
    selectedTrainer(state, trainer) {
      state.selectedTrainer = trainer;
    },

    selectedDate(state, selectedDate) {
      state.selectedDate = selectedDate;
    },
  }

});

