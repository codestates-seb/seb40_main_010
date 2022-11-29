import React from 'react';
import { Link, useNavigate } from 'react-router-dom';
import styled from 'styled-components';
import axios from 'axios';

import { useForm } from 'react-hook-form';

import Nav from '../components/Navigation/Nav';
import useLogin from '../hooks/useLogin';

export default function LogIn() {
  const navigator = useNavigate();

  const {
    register,
    handleSubmit,
    formState: { isSubmitting, errors },
  } = useForm({ mode: 'onChange' });

  const { errorMessage, errorStatus, getErrorType, setErrorException } =
    useLogin();

  const onSubmit = async data => {
    try {
      const response = await axios.post(`/auth/login`, data);
      await localStorage.setItem('ACCESS', response.headers.authorization);
      await localStorage.setItem('REFRESH', response.headers.refreshtoken);
      navigator('/');
    } catch (error) {
      const validationType = getErrorType(error.response.data.status);

      if (validationType !== 'regular')
        return setErrorException(validationType);
    }
    return null;
  };

  return (
    <>
      <Nav />
      <Container>
        <form onSubmit={handleSubmit(onSubmit)} autoComplete="off">
          <div className="login-container">
            <div className="wrapper">
              <div className="title">Email</div>
              <Input
                placeholder="code@gmail.com"
                {...register('email', {
                  required: '이메일을 입력해주세요',
                  pattern: {
                    value: /\S+@\S+\.\S+/,
                    message: '이메일 형식에 맞지 않습니다.',
                  },
                })}
              />
              {errors.email && (
                <div className="alert">{errors.email.message}</div>
              )}
              {errorMessage && errorStatus === 504 && (
                <div className="alert">{errorMessage}</div>
              )}
            </div>
            <div className="wrapper">
              <div className="title">Password</div>
              <Input
                type="password"
                {...register('password', {
                  required: '비밀번호를 입력해주세요',
                })}
              />
              {errors.password && (
                <div className="alert">{errors.password.message}</div>
              )}
              {errorMessage && errorStatus === 403 && (
                <div className="alert">{errorMessage}</div>
              )}
            </div>
            <button
              type="submit"
              className="submit-button"
              disabled={isSubmitting}
            >
              Log In
            </button>
            <SignUpLink to="/sign-up">Sign Up</SignUpLink>
          </div>
        </form>
      </Container>
    </>
  );
}

const Container = styled.div`
  width: 100vw;
  height: 100vh;
  background-color: #ffda77;
  display: flex;
  justify-content: center;
  align-items: center;

  .login-container {
    margin-top: 70px;
    width: 22rem;
    height: flex;
    padding: 3rem 0rem;
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
    margin-bottom: 25px;
  }

  .title {
    width: 100%;
    height: fit-content;
    font-size: 0.95rem;
    font-weight: 600;
    color: #2b2b2b;
    margin-bottom: 10px;
  }

  .submit-button {
    width: 15rem;
    height: 3rem;
    margin-top: 15px;
    margin-bottom: 15px;
    background-color: #96c2ff;
    border-radius: 40px;
    color: #2b2b2b;
    font: inherit;
    font-size: 1.3rem;
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
  font-size: 0.82rem;
  font: inherit;
  outline: none;
  border: none;
  border-bottom: 2px solid #96c2ff;
  color: #2b2b2b;
  padding: 3px 0px;
`;

const SignUpLink = styled(Link)`
  color: #2b2b2b;
  font-size: 1rem;
  font-weight: 500;
`;
