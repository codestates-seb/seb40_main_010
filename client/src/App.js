import React, { useState, useEffect } from 'react';
import { Route, Routes } from 'react-router-dom';
import axios from 'axios';

// eslint-disable-next-line camelcase
import jwt_decode from 'jwt-decode';
import { useResetRecoilState, useSetRecoilState } from 'recoil';

import Places from './pages/Places';
import Detail from './pages/Detail';
import LogIn from './pages/LogIn';
import MyPage from './pages/MyPage';
import Register from './pages/Register';
import SignUp from './pages/SignUp';
import NotFound from './components/NotFound';
import {
  HasRefresh,
  mbtiPlaceDataState,
  userMbtiValue,
  tokenAtom,
} from './atoms';

function App() {
  const [timer, setTimer] = useState(false);
  const setLogIn = useSetRecoilState(HasRefresh);
  const resetMbti = useSetRecoilState(mbtiPlaceDataState);
  const resetUserMbti = useResetRecoilState(userMbtiValue);
  const setToken = useSetRecoilState(tokenAtom);

  const token = localStorage.getItem('ACCESS');
  const isLogIn = localStorage.getItem('REFRESH');

  const reIssue = async () => {
    if (!isLogIn) return;

    const { exp } = jwt_decode(token);
    const currentTime = Math.ceil(Date.now() / 1000);

    if (exp - currentTime > 60 * 3) return;

    if (currentTime > exp) {
      alert('다시 로그인 해주세요');

      await localStorage.removeItem('ACCESS');
      await localStorage.removeItem('REFRESH');

      await setLogIn(false);

      resetMbti([]);
      resetUserMbti();
    }

    if (exp - currentTime <= 60 * 3) {
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/auth/re-issue`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      localStorage.setItem('ACCESS', `${response.headers.authorization}`);
    }
  };

  useEffect(() => {
    reIssue();
    setTimeout(() => {
      setTimer(!timer);
    }, 60000);
  }, [isLogIn, timer]);

  useEffect(() => {
    setToken(prev => {
      if (token)
        return {
          ...prev,
          ACCESS: `Bearer ${localStorage.getItem('ACCESS')}`,
          REFRESH: localStorage.getItem('REFRESH'),
        };
      return { ...prev };
    });
  }, []);

  return (
    <div>
      <Routes>
        <Route path="/" element={<Places />} />
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
