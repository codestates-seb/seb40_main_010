import React from 'react';
import styled from 'styled-components';
import { useForm } from 'react-hook-form';

const Container = styled.div`
  width: 100vw;
  height: 100vh;
  background-color: #ffda77;
  display: flex;
  justify-content: center;
  align-items: center;

  .login-container {
    width: 400px;
    height: flex;
    padding: 60px 0px;
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
    /* border: 3px solid black; */
  }

  .title {
    width: 100%;
    height: fit-content;
    font-size: 1.1rem;
    font-weight: 500;
    color: #2b2b2b;
    margin-bottom: 10px;
    /* border: 10px solid red; */
  }

  .btn {
    width: 300px;
    height: 55px;
    margin-top: 15px;
    margin-bottom: 15px;
    background-color: #96c2ff;
    border-radius: 40px;
    color: #2b2b2b;
    font-size: 1.7rem;
    font-weight: 600;
    border: none;
    box-shadow: rgba(0, 0, 0, 0.35) 3px 3px 3px;
    /* transition-duration: 0.3s; */

    :active {
      box-shadow: none;
    }

    :disabled {
      cursor: not-allowed;
      opacity: 0.7;
      box-shadow: none;
    }
  }

  .link {
    color: #2b2b2b;
    font-size: 1rem;
    font-weight: 500;
  }

  .description {
    width: 100%;
    margin-top: 10px;
    color: #666666;
  }

  .alert {
    margin-top: 10px;
    color: #eb7470;
    font-weight: 500;
  }
`;

const Input = styled.input`
  width: 99%;
  height: fit-content;
  font-size: 1rem;
  outline: none;
  border: none;
  border-bottom: 2px solid #96c2ff;
  color: #2b2b2b;
`;

function LogIn() {
  const {
    register,
    handleSubmit,
    // watch,
    formState: { isSubmitting, errors },
  } = useForm();

  const onSubmit = data => console.log(data);

  return (
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
          </div>
          <button type="submit" className="btn">
            Log In
          </button>
          <a className="link" href="/signup" disabled={isSubmitting}>
            Sign Up
          </a>
        </div>
      </form>
    </Container>
  );
}

export default LogIn;
