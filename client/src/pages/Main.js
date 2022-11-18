import axios from 'axios';
import React, { useEffect } from 'react';
import Category from '../components/Category';
import Nav from '../components/Nav';

function Main() {
  useEffect(() => {
    axios
      .get('{{BACKEND}}/')
      .then(res => console.log(res))
      .catch(err => console.log(err));
  }, []);

  return (
    <div>
      <Nav />
      <Category />
    </div>
  );
}

export default Main;
