import { configureStore } from '@reduxjs/toolkit'
import studentsReducer from '../admin_page/studentsSlice';

export default configureStore({
  reducer: {
      students: studentsReducer,
  },
})