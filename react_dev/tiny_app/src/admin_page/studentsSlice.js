import { createSlice } from '@reduxjs/toolkit'

export const studentsSlice = createSlice({
    name: 'students',
    initialState: {
      value: []
    },
    reducers: {
      addNew: (state, action) => {
        state.value.push(action.payload);
      },
      addAll: (state, action) => {
        state.value = action.payload;
      },
      // addAll: (state, action) => {
      //   const data = Array.isArray(action.payload) ? action.payload : [];
      //   state.value = data;
      // },
    },
});

export const {addNew, addAll } = studentsSlice.actions

export default studentsSlice.reducer