import React from 'react';
import styled from 'styled-components';
import Select from 'react-select';
import { useForm, Controller } from 'react-hook-form';

const Container = styled.div`
  width: 100vw;
  height: 100vh;
  background-color: #96c2ff;
  display: flex;
  justify-content: center;
  align-items: center;

  .signup-container {
    width: 700px;
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
    width: 65%;
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
    background-color: #ffda77;
    border-radius: 40px;
    color: #2b2b2b;
    font-size: 1.5rem;
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

  .link {
    color: #2b2b2b;
    font-size: 1rem;
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

const StyledSelect = styled(Select)`
  font-size: 1rem;
  width: 100%;

  .Select__control {
    height: 40px;
    width: 100%;
    /* border: none; */
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

function SignUp() {
  const {
    register,
    handleSubmit,
    watch,
    control,
    formState: { isSubmitting, errors },
  } = useForm();

  const onSubmit = data => console.log(data);
  console.log(isSubmitting);

  const mbtiList = [
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

  // /^(?=.*[A-Za-z])(?=.*\d)(?=.*[$@$!%*#?&])[A-Za-z\d$@$!%*#?&]{8,}$/;

  return (
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
          </div>
          <div className="wrapper">
            <div className="title">Password</div>
            <Input
              placeholder="8자 이상 영문, 숫자, 특수문자를 사용하세요."
              {...register('password', {
                required: '필수 정보입니다.',
                minLength: {
                  value: 8,
                  message: '8자리 이상 비밀번호를 사용하세요.',
                },
                validate: {
                  numcheck: value =>
                    (value && /[0-9]/g.test(value)) || '숫자를 추가해주세요',
                  lettercheck: value =>
                    (value && /[a-z]/gi.test(value)) || '영어를 추가해주세요',
                  specialcheck: value =>
                    (value && /[`~!@@#$%^&*|₩₩₩'₩";:₩/?]/gi.test(value)) ||
                    '특수문자를 추가해주세요',
                },
              })}
            />
            {errors.password && errors.password.type === 'required' && (
              <div className="alert">{errors.password.message}</div>
            )}
            {errors.password && errors.password.type === 'minLength' && (
              <div className="alert">{errors.password.message}</div>
            )}
            {errors.password && errors.password.type === 'numcheck' && (
              <div className="alert">{errors.password.message}</div>
            )}
            {errors.password && errors.password.type === 'lettercheck' && (
              <div className="alert">{errors.password.message}</div>
            )}
            {errors.password && errors.password.type === 'specialcheck' && (
              <div className="alert">{errors.password.message}</div>
            )}
          </div>
          <div className="wrapper">
            <div className="title">Re-Enter Password</div>
            <Input
              placeholder="비밀번호를 다시 입력해주세요."
              {...register('confirm_password', {
                required: '필수 정보입니다.',
                validate: val => {
                  if (watch('password') !== val) {
                    return '비밀번호가 정확하지 않습니다.';
                  }
                  return null;
                },
              })}
            />
            {errors.confirm_password && (
              <div className="alert">{errors.confirm_password.message}</div>
            )}
          </div>
          <div className="wrapper">
            <div className="title">Username</div>
            <Input
              placeholder="1자 이상의 한글, 영문, 숫자만 사용할 수 있습니다"
              {...register('userName', {
                required: '필수 정보입니다.',
                pattern: {
                  value: /[0-9]|[a-z]|[A-Z]|[가-힣]/,
                  message: '이름 형식에 맞지 않습니다.',
                },
              })}
            />
            {errors.userName && (
              <div className="alert">{errors.userName.message}</div>
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
              // defaultValue={default_value}
              name="mbti"
              render={({ field: { onChange, value, ref } }) => (
                <StyledSelect
                  inputRef={ref}
                  classNamePrefix="Select"
                  options={mbtiList}
                  placeholder="mbti"
                  value={mbtiList.find(c => c.value === value)}
                  onChange={val => onChange(val.value)}
                />
              )}
            />
          </div>
          <button type="submit" className="btn" disabled={isSubmitting}>
            Sign Up
          </button>
          <a className="link" href="/login">
            Log In
          </a>
        </div>
      </form>
    </Container>
  );
}

export default SignUp;
