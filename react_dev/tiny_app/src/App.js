import React from 'react';
import { Routes, Route } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import Home from './Pages/Home';
import AddStud from './Pages/AddStud';
import StudentsTable from './Pages/StudentsTable';
import ErrorPage from './Pages/ErrorPage';
import NavBar from './admin_page/NavBar';

const App = () => {
 
  return (
    <div>
      <NavBar/>
      <Routes>
        <Route path="/" element={<Home />} />
        <Route path="/addStud" element={<AddStud/>} />
        <Route path="/studentsTable" element={<StudentsTable/>} />
        <Route path="*" element={<ErrorPage />} />
      </Routes>
      
    </div>
  );
};

export default App;
