import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import { HasRefresh, userMbtiValue } from '../../atoms';
import Loading from '../Loading';

function OAuth2RedirectHandler() {
  const navigator = useNavigate();
  const setIsLogIn = useSetRecoilState(HasRefresh);
  const setUserMbti = useSetRecoilState(userMbtiValue);

  const params = new URLSearchParams(window.location.href);

  const Access = params.get('authorization');
  const Refresh = params.get('refreshtoken');
  const Mbti = params.get('mbti');

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
