import React, { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';

import { HasRefresh } from '../../atoms';
import Loading from '../Loading';

function OAuth2RedirectHandler() {
  const navigator = useNavigate();
  const setIsLogIn = useSetRecoilState(HasRefresh);

  const Access = new URL(window.location.href).searchParams.get(
    'authorization',
  );
  const Refresh = new URL(window.location.href).searchParams.get(
    'refreshtoken',
  );
  useEffect(() => {
    localStorage.setItem('ACCESS', Access);
    localStorage.setItem('REFRESH', Refresh);
    setIsLogIn(true);
    navigator('/');
  }, []);

  return <Loading />;
}

export default OAuth2RedirectHandler;
