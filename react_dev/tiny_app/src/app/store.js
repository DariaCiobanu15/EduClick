import { configureStore } from '@reduxjs/toolkit'
import buttonReducer from '../admin_page/buttonSlice';

export default configureStore({
  reducer: {
      button: buttonReducer,
  },
})