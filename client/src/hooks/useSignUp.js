import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import axios from 'axios';

const useSignUp = () => {
  const navigator = useNavigate();
  const [errorMessage, setErrorMessage] = useState('');

  const getErrorType = message => {
    if (message === '이미 존재하는 닉네임입니다.') {
      return setErrorMessage('이미 존재하는 닉네임입니다.');
    }
    if (message === '이미 존재하는 이메일입니다.') {
      return setErrorMessage('이미 존재하는 이메일입니다.');
    }
    return null;
  };

  const onSubmit = async data => {
    try {
      await axios.post(
        `${process.env.REACT_APP_SERVER_BASE_URL}/member/join`,
        data,
      );
      alert('회원가입이 완료되었습니다.');
      navigator('/log-in');
    } catch (err) {
      getErrorType(err.response.data.message);
    }
  };

  return { errorMessage, onSubmit };
};

export default useSignUp;
