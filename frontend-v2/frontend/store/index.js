export const state = () => ({
  user: null,
  loadingAvaiablility: false,
  slots: [],
  selectedSubject: null,
  selectedSlot: null,
  selectedTeacher: null
});

export const getters = {
  user(state) {
    return state.user;
  },
  isAdmin(state) {
    return state.user != null && state.user.isAdmin;
  },
  jwt() {
    return state.user != null ? state.user.token : null;
  },
  selectedSubject(state) {
    return state.selectedSubject;
  },
  selectedSlot(state) {
    return state.selectedSlot;
  },
  selectedTeacher(state) {
    return state.selectedTeacher;
  },
  slots(state) {
    return state.slots;
  }
};

export const mutations = {
  SET_USER(state, user) {
    state.user = user;
  },
  CLEAR_BOOKING(state) {
    state.selectedSlot = null;
    state.selectedTeacher = null;
  },
  SET_SUBJECT(state, subject) {
    state.selectedSubject = subject;
  },
  SET_SLOT(state, slot) {
    state.selectedSlot = slot;
  },
  SET_TEACHER(state, teacher) {
    state.selectedTeacher = teacher;
  },
  async SET_SLOTS(state) {
    const slots = await this.$axios.get("Slot", {
      params: {
        subjectId: state.selectedSubject.id
      }
    });
    var tmp = Object.keys(slots.data).map(d => {
      return { date: d, daySlots: slots.data[d] };
    });
    state.slots = tmp;
  }
};

export const actions = {
  async setSubject({ commit }, subject) {
    commit("SET_SUBJECT", subject);
    commit("SET_TEACHER", null);
    commit("SET_SLOT", null);
    commit("SET_SLOTS");
  },
  setSlot({ commit }, slot) {
    commit("SET_TEACHER", null);
    commit("SET_SLOT", slot);
    // commit('CLEAR_BOOKING')
  },
  setTeacher({ commit }, teacher) {
    commit("SET_TEACHER", teacher);
  },
  async bookSlot({ commit, state }) {
    await this.$axios.post("Bookings", null, {
      params: {
        subject: state.selectedSubject.id,
        teacher: state.selectedTeacher.id,
        date: state.selectedSlot.date
      }
    });

    commit("SET_SLOTS");
    commit("CLEAR_BOOKING");
    this.$toast.show("Prenotazione effettuata");
  }
};
