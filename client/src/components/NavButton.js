import React from 'react';
import { Link, useMatch } from 'react-router-dom';
import { BsFillPersonFill } from 'react-icons/bs';
import styled from 'styled-components';

export function NavLeftButtonContainer({ buttonColor }) {
  const logInUrl = useMatch('/log-in');
  const registerUrl = useMatch('/register');

  const isLogIn = localStorage.getItem('AccessToken');

  if (registerUrl || logInUrl) return null;

  if (isLogIn) {
    return (
      <Link to="/register">
        <NavLeftButton buttonColor={buttonColor}>장소등록</NavLeftButton>
      </Link>
    );
  }
  return (
    <Link to="/log-in">
      <NavLeftButton buttonColor={buttonColor}>Log In</NavLeftButton>
    </Link>
  );
}

export function NavRightButtonContainer() {
  const signUpUrl = useMatch('/sign-up');
  const myPageUrl = useMatch('/my-page');

  const isLogIn = localStorage.getItem('AccessToken');

  const onClickLogOutButton = () => {
    localStorage.removeItem('AccessToken');
  };

  if (signUpUrl) return null;

  if (isLogIn && myPageUrl) {
    return (
      <Link to="/">
        <NavRightButton onClick={onClickLogOutButton}>Log Out</NavRightButton>
      </Link>
    );
  }

  if (isLogIn && !myPageUrl) {
    return (
      <Link to="/my-page">
        <NavRightButton>
          <MyPageDiv>
            <BsFillPersonFill />
          </MyPageDiv>
        </NavRightButton>
      </Link>
    );
  }

  return (
    <Link to="/sign-up">
      <NavRightButton>Sign Up</NavRightButton>
    </Link>
  );
}

const NavLeftButton = styled.button`
  width: 80px;
  margin: 10px 7px;
  padding: 8px;
  border-radius: 20px;
  border: none;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  background-color: ${props => props.buttonColor || '#ffda77'};
  font-size: 0.9rem;
  font-weight: 600;
  line-height: 20px;
  text-align: center;
  color: #2b2b2b;
  &:hover {
    background-color: #fff9eb;
    transition: 0.7s;
    cursor: pointer;
  }
`;

const NavRightButton = styled(NavLeftButton)`
  &:hover {
    background-color: #eb7470;
    transition: 0.5s;
  }
`;

const MyPageDiv = styled.div`
  border-radius: 50%;
`;
