import React, { useState, useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';
// import axios from 'axios';

import Places from './pages/Places';
import Detail from './pages/Detail';
import LogIn from './pages/LogIn';
import MyPage from './pages/MyPage';
import Register from './pages/Register';
import SignUp from './pages/SignUp';
import NotFound from './components/NotFound';
import getData from './hooks/useAsyncGetData';
// ToDo : refresh 주석 풀면 작동 > 개발 하고 ctr+s 할 때마다 요청가서 막아둠 , console.log(refresh)지워야
function App() {
  // const login = useState(localStorage.getItem('REFRESH'));
  const [ref, setRef] = useState(false);

  const refresh = async () => {
    try {
      const response = await getData('/auth/re-issue');
      console.log(response.headers.authorization);
      // localStorage.removeItem('ACCESS');
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
  console.log(refresh);
  useEffect(() => {
    // refresh();
    setTimeout(() => {
      setRef(!ref);
      // console(ref);
    }, 1500000 - 60000);
  }, [ref]);

  return (
    <div>
      <Routes>
        <Route path="/" element={<Places />} />
        <Route path="/detail/" element={<Detail />} />
        <Route path="/detail/:id" element={<Detail />} />
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
