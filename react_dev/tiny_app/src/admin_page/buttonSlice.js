import { createSlice } from '@reduxjs/toolkit'

export const buttonSlice = createSlice({
    name: 'button',
    initialState: {
      value: 'initial state',
    },
    reducers: {
      showStudents: (state) => {
        state.value = 'Show students';
      },
      addNew: (state) => {
        state.value = 'Add new students';
      },
    },
});

export const { showStudents, addNew } = buttonSlice.actions

export const buttonState = (state) => state.button.value

export default buttonSlice.reducer