import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import { HasRefresh, userMbtiValue } from '../../atoms';
import Loading from '../Loading';

function OAuth2RedirectHandler() {
  const navigator = useNavigate();
  const setIsLogIn = useSetRecoilState(HasRefresh);
  const setUserMbti = useSetRecoilState(userMbtiValue);

  const Access = new URL(window.location.href).searchParams.get(
    'authorization',
  );
  const Refresh = new URL(window.location.href).searchParams.get(
    'refreshtoken',
  );

  const params = new URLSearchParams(window.location.href);

  const Mbti = params.get('mbti');

  console.log(Mbti);

  useEffect(() => {
    localStorage.setItem('ACCESS', Access);
    localStorage.setItem('REFRESH', Refresh);
    setUserMbti(Mbti);
    setIsLogIn(true);
    navigator('/');
  }, []);

  return <Loading />;
}

export default OAuth2RedirectHandler;
