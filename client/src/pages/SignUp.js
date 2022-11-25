import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import axios from 'axios';
import { useForm, Controller } from 'react-hook-form';
import Select from 'react-select';

import Nav from '../components/Navigation/Nav';

function SignUp() {
  const navigator = useNavigate();
  const [errorMessage, setErrorMessage] = useState('');
  const [isNicknameDuplicated, setIsNicknameDuplicated] = useState(false);
  const [isEmailDuplicated, setIsEmailDuplicated] = useState(false);

  const {
    register,
    handleSubmit,
    watch,
    control,
    formState: { isSubmitting, errors },
  } = useForm({ mode: 'onChange' });

  const onSubmit = async data => {
    try {
      const response = await axios.post(`/member/join`, data);
      navigator('/log-in');
      console.log(response.data);
    } catch (err) {
      console.log('Error >>', err);
      if (err.response.data.message === '이미 존재하는 닉네임입니다.') {
        setErrorMessage(err.response.data.message);
        setIsNicknameDuplicated(true);
      }
      if (err.response.data.message === '이미 존재하는 이메일입니다.') {
        setErrorMessage(err.response.data.message);
        setIsEmailDuplicated(true);
      }
    }
  };

  const mbtiList = [
    { value: 'NONE', label: '없음' },
    { value: 'ISTJ', label: 'ISTJ' },
    { value: 'ISFJ', label: 'ISFJ' },
    { value: 'INFJ', label: 'INFJ' },
    { value: 'INTJ', label: 'INTJ' },
    { value: 'ISTP', label: 'ISTP' },
    { value: 'ISFP', label: 'ISFP' },
    { value: 'INFP', label: 'INFP' },
    { value: 'INTP', label: 'INTP' },
    { value: 'ESTJ', label: 'ESTJ' },
    { value: 'ESFJ', label: 'ESFJ' },
    { value: 'ENFJ', label: 'ENFJ' },
    { value: 'ENTJ', label: 'ENTJ' },
    { value: 'ESTP', label: 'ESTP' },
    { value: 'ESFP', label: 'ESFP' },
    { value: 'ENFP', label: 'ENFP' },
    { value: 'ENTP', label: 'ENTP' },
  ];

  return (
    <div className="test">
      <Nav navColor="#FFDA77" buttonColor="#89BBFF" />
      <Container>
        <form onSubmit={handleSubmit(onSubmit)} autoComplete="off">
          <div className="signup-container">
            <div className="wrapper">
              <div className="title">Email</div>
              <Input
                type="email"
                placeholder="code@gmail.com"
                {...register('email', {
                  required: '필수 정보입니다.',
                  pattern: {
                    value: /\S+@\S+\.\S+/,
                    message: '이메일 형식에 맞지 않습니다.',
                  },
                })}
              />
              {errors.email && (
                <div className="alert">{errors.email.message}</div>
              )}
              {errorMessage && isEmailDuplicated && (
                <div className="alert">{errorMessage}</div>
              )}
            </div>
            <div className="wrapper">
              <div className="title">Password</div>
              <Input
                placeholder="8자 이상 영문, 숫자, 특수문자를 사용하세요."
                type="password"
                {...register('password', {
                  required: '필수 정보입니다.',
                  minLength: {
                    value: 8,
                    message: '8자리 이상 비밀번호를 사용하세요.',
                  },
                  validate: {
                    spaceCheck: value =>
                      (value && value.search(/\s/) === -1) ||
                      '공백을 제거해주세요',
                    numCheck: value =>
                      (value && /[0-9]/g.test(value)) || '숫자를 추가해주세요',
                    letterCheck: value =>
                      (value && /[a-z]/gi.test(value)) || '영어를 추가해주세요',
                    specialCheck: value =>
                      (value && /[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi.test(value)) ||
                      '특수문자를 추가해주세요',
                    // spacecheck: value =>
                    //   (value && /\s/gi.test(value)) || '공백을 제거해주세요',
                  },
                })}
              />
              {errors.password && errors.password.type === 'required' && (
                <div className="alert">{errors.password.message}</div>
              )}
              {errors.password && errors.password.type === 'minLength' && (
                <div className="alert">{errors.password.message}</div>
              )}
              {errors.password && errors.password.type === 'numCheck' && (
                <div className="alert">{errors.password.message}</div>
              )}
              {errors.password && errors.password.type === 'letterCheck' && (
                <div className="alert">{errors.password.message}</div>
              )}
              {errors.password && errors.password.type === 'specialCheck' && (
                <div className="alert">{errors.password.message}</div>
              )}
              {errors.password && errors.password.type === 'spaceCheck' && (
                <div className="alert">{errors.password.message}</div>
              )}
            </div>
            <div className="wrapper">
              <div className="title">Re-Enter Password</div>
              <Input
                placeholder="비밀번호를 다시 입력해주세요."
                type="password"
                {...register('confirmPassword', {
                  required: '필수 정보입니다.',
                  validate: val => {
                    if (watch('password') !== val) {
                      return '비밀번호가 정확하지 않습니다.';
                    }
                    return null;
                  },
                })}
              />
              {errors.confirmPassword && (
                <div className="alert">{errors.confirmPassword.message}</div>
              )}
            </div>
            <div className="wrapper">
              <div className="title">Nickname</div>
              <Input
                placeholder="1자 이상의 한글, 영문, 숫자만 사용할 수 있습니다"
                {...register('nickname', {
                  required: '필수 정보입니다.',
                  pattern: {
                    value: /[0-9]|[a-z]|[A-Z]|[가-힣]/,
                    message: '이름 형식에 맞지 않습니다.',
                  },
                  validate: {
                    spaceCheck: value =>
                      (value && value.search(/\s/) === -1) || '공백이 있습니다',
                  },
                })}
              />
              {errors.nickname && (
                <div className="alert">{errors.nickname.message}</div>
              )}
              {errorMessage && isNicknameDuplicated && (
                <div className="alert">{errorMessage}</div>
              )}
            </div>
            <div className="wrapper">
              <div className="title">Phone number</div>
              <Input
                placeholder="010-1234-5678"
                {...register('phoneNumber', {
                  required: '필수 정보입니다.',
                  pattern: {
                    value: /^01([0|1|6|7|8|9])-?([0-9]{3,4})-?([0-9]{4})$/,
                    message: '전화번호 형식에 맞지 않습니다.',
                  },
                })}
              />
              {errors.phoneNumber && (
                <div className="alert">{errors.phoneNumber.message}</div>
              )}
            </div>
            <div className="wrapper">
              <div className="title">MBTI</div>
              <Controller
                control={control}
                name="mbti"
                rules={{ required: '필수 정보입니다.' }}
                render={({ field: { onChange, value, ref } }) => (
                  <MbtiSelect
                    inputRef={ref}
                    classNamePrefix="Select"
                    options={mbtiList}
                    placeholder="mbti"
                    value={mbtiList.find(c => c.value === value)}
                    onChange={val => onChange(val.value)}
                  />
                )}
              />
              {errors.mbti && (
                <div className="alert">{errors.mbti.message}</div>
              )}
            </div>
            <button
              type="submit"
              className="submit-button"
              disabled={isSubmitting}
            >
              Sign Up
            </button>
            <LogInLink to="/login">Log In</LogInLink>
          </div>
        </form>
      </Container>
    </div>
  );
}

export default SignUp;

const Container = styled.div`
  width: 100vw;
  height: 800px;
  background-color: #96c2ff;
  display: flex;
  justify-content: center;
  align-items: center;

  .signup-container {
    margin-top: 30px;
    width: 33rem;
    height: flex;
    padding: 50px 0px;
    background-color: #ffffff;
    border-radius: 20px;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  }
  .wrapper {
    width: 70%;
    height: fit-content;
    margin-bottom: 20px;
  }

  .title {
    width: 100%;
    height: fit-content;
    font-size: 1rem;
    font-weight: 500;
    color: #2b2b2b;
    margin-bottom: 10px;
  }

  .submit-button {
    width: 15rem;
    height: 55px;
    margin-top: 15px;
    margin-bottom: 15px;
    background-color: #ffda77;
    border-radius: 40px;
    color: #2b2b2b;
    font-size: 1.5rem;
    font-weight: 600;
    border: none;
    box-shadow: rgba(0, 0, 0, 0.35) 3px 3px 3px;

    :active {
      box-shadow: none;
    }

    :disabled {
      cursor: not-allowed;
      opacity: 0.7;
      box-shadow: none;
    }
  }

  .description {
    width: 100%;
    margin-top: 10px;
    color: #666666;
  }

  .alert {
    margin-top: 10px;
    color: #eb7470;
    font-size: 0.8rem;
    font-weight: 500;
  }
`;

const Input = styled.input`
  width: 99%;
  height: fit-content;
  font-size: 0.85rem;
  outline: none;
  border: none;
  border-bottom: 2px solid #96c2ff;
  color: #2b2b2b;
`;

const MbtiSelect = styled(Select)`
  font-size: 0.85rem;
  width: 100%;

  .Select__control {
    height: 30px;
    width: 100%;
    border: 2px solid #96c2ff;
    border-radius: 5px;
    cursor: pointer;
  }

  .Select__indicator-separator {
    display: none;
  }

  .Select__menu {
    color: #2b2b2b;
  }

  .Select__menu-list {
    ::-webkit-scrollbar {
      display: none;
    }
  }
`;

const LogInLink = styled(Link)`
  color: #2b2b2b;
  font-size: 1rem;
  font-weight: 500;
`;
