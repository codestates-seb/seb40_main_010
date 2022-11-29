import { useState } from 'react';

const useLogin = () => {
  const [errorMessage, setErrorMessage] = useState('');
  const [errorStatus, setErrorStatus] = useState('');

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

  return { getErrorType, setErrorException, errorMessage, errorStatus };
};

export default useLogin;
