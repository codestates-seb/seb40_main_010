import { useState } from 'react';
import axios from 'axios';
import { useRecoilState, useResetRecoilState, useSetRecoilState } from 'recoil';
import { useNavigate } from 'react-router-dom';
import {
  categoryFocus,
  HasRefresh,
  mainDataState,
  NextPage,
  pageState,
  userMbtiValue,
  tokenAtom,
} from '../atoms';

const useLogin = () => {
  const [errorMessage, setErrorMessage] = useState('');
  const [errorStatus, setErrorStatus] = useState('');
  const setIsLogIn = useSetRecoilState(HasRefresh);
  const setHasNextPage = useSetRecoilState(NextPage);
  const setFocusCategoryID = useSetRecoilState(categoryFocus);
  const setPage = useSetRecoilState(pageState);
  const resetMainPlaceData = useResetRecoilState(mainDataState);
  const [, setUserMbti] = useRecoilState(userMbtiValue);
  const setToken = useSetRecoilState(tokenAtom);

  const navigator = useNavigate();

  const getErrorType = status => {
    if (status === 403) {
      return 'incorrectPassword';
    }
    if (status === 504) {
      return 'invalidEmail';
    }
    return 'regular';
  };

  const setErrorException = type => {
    if (type === 'incorrectPassword') {
      setErrorStatus(403);
      setErrorMessage('비밀번호를 잘못 입력했습니다');
      return null;
    }
    if (type === 'invalidEmail') {
      setErrorStatus(504);
      setErrorMessage('존재하지 않는 계정입니다.');
      return null;
    }
    return null;
  };

  const invalidate = () => {
    resetMainPlaceData();
    setHasNextPage(true);
    setPage(1);
    setFocusCategoryID(0);
    navigator('/');
  };

  const onSubmit = async data => {
    try {
      const response = await axios.post(
        `${process.env.REACT_APP_SERVER_BASE_URL}/auth/login`,
        data,
      );
      if (response.headers.authorization) {
        setToken({
          ACCESS: `Bearer ${response.headers.authorization}`,
          REFRESH: response.headers.refreshtoken,
        });
      }
      await localStorage.setItem('ACCESS', response.headers.authorization);
      await localStorage.setItem('REFRESH', response.headers.refreshtoken);

      setUserMbti(response.data.mbti);

      setIsLogIn(true);
      invalidate();
    } catch (error) {
      const validationType = getErrorType(error.response.data.status);

      if (validationType !== 'regular')
        return setErrorException(validationType);
    }
    return null;
  };
  return {
    getErrorType,
    setErrorException,
    errorMessage,
    errorStatus,
    onSubmit,
  };
};

export default useLogin;
