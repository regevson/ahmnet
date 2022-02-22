import Vue from 'vue';
import Vuex from 'vuex';

Vue.use(Vuex);

export const store = new Vuex.Store({
  state: {
    selectedTrainer: null,
    selectedDate: null,
    isFiltered: false,
  },

  getters: {
    selectedTrainer(state) {
      return state.selectedTrainer;
    },

    selectedDate(state) {
      return state.selectedDate;
    },

    isFiltered(state) {
      return state.isFiltered;
    },

  },

  mutations: {
    selectedTrainer(state, trainer) {
      state.selectedTrainer = trainer;
    },

    selectedDate(state, selectedDate) {
      state.selectedDate = selectedDate;
    },

    isFiltered(state, isFiltered) {
      state.isFiltered = isFiltered;
    },
  }

});

