import React, { useState, useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';
import axios from 'axios';

import Places from './pages/Places';
import Detail from './pages/Detail';
import LogIn from './pages/LogIn';
import MyPage from './pages/MyPage';
import Register from './pages/Register';
import SignUp from './pages/SignUp';
import NotFound from './components/NotFound';

function App() {
  const login = useState(localStorage.getItem('REFRESH'));
  const [ref, setRef] = useState(false);

  const refresh = async () => {
    try {
      const response = await axios.get(
        `/auth/re-issue`,
        {},
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
            RefreshToken: `${localStorage.getItem('REFRESH')}`,
          },
        },
      );

      if (response.headers.authorization) {
        localStorage.setItem(
          'ACCESS',
          `Bearer ${response.headers.authorization}`,
        );
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (login) {
      setTimeout(() => {
        refresh();
        setRef(!ref);
      }, 1500000);
    }
  }, [login, ref]);

  return (
    <div>
      <Routes>
        <Route path="/" element={<Places />} />
        <Route path="/detail" element={<Detail />} />
        <Route path="/log-in" element={<LogIn />} />
        <Route path="/my-page" element={<MyPage />} />
        <Route path="/register" element={<Register />} />
        <Route path="/sign-up" element={<SignUp />} />
        <Route path="*" element={<NotFound />} />
      </Routes>
    </div>
  );
}

export default App;
