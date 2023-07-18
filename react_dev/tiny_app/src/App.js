import React from 'react';
import Button from 'react-bootstrap/Button';
import 'bootstrap/dist/css/bootstrap.min.css';
import './App.css';
import { Buttons } from './admin_page/Buttons';

const App = () => {

  return (
    <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
      <h1>Admin's Home Page</h1>
      <Buttons/>
    </div>
  );
}

export default App;