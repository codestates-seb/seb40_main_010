import React from 'react';
import { Link, useMatch, useNavigate } from 'react-router-dom';
import { BsFillPersonFill } from 'react-icons/bs';
import styled from 'styled-components';
import {
  useSetRecoilState,
  useRecoilState,
  useRecoilValue,
  useResetRecoilState,
} from 'recoil';
import axios from 'axios';
import {
  HasRefresh,
  NextPage,
  categoryFocus,
  pageState,
  mainDataState,
  settingUrl,
  navSearchValue,
} from '../../atoms';

export function NavLeftButtonContainer({ buttonColor }) {
  const logInUrl = useMatch('/log-in');
  const registerUrl = useMatch('/register');

  const isLogIn = useRecoilValue(HasRefresh);
  const setHasNextPage = useSetRecoilState(NextPage);
  const setFocusCategoryID = useSetRecoilState(categoryFocus);
  const setPage = useSetRecoilState(pageState);
  const resetMainPlaceData = useResetRecoilState(mainDataState);
  const setUrl = useSetRecoilState(settingUrl);
  const setSearch = useSetRecoilState(navSearchValue);

  const navigate = useNavigate();

  const invalidate = () => {
    resetMainPlaceData();
    setHasNextPage(true);
    setPage(1);
    setFocusCategoryID(0);
    navigate('/');
  };

  if (registerUrl || logInUrl) return null;

  const onClickAnotherPage = () => {
    setSearch('');
    invalidate();
    setUrl(() => `/home?size=20&page=`);
  };

  if (isLogIn) {
    return (
      <Link to="/register">
        <NavLeftButton buttonColor={buttonColor} onClick={onClickAnotherPage}>
          장소등록
        </NavLeftButton>
      </Link>
    );
  }
  return (
    <Link to="/log-in">
      <NavLeftButton buttonColor={buttonColor} onClick={onClickAnotherPage}>
        Log In
      </NavLeftButton>
    </Link>
  );
}

export function NavRightButtonContainer() {
  const signUpUrl = useMatch('/sign-up');
  const myPageUrl = useMatch('/my-page');

  const setHasNextPage = useSetRecoilState(NextPage);
  const setFocusCategoryID = useSetRecoilState(categoryFocus);
  const setPage = useSetRecoilState(pageState);
  const resetMainPlaceData = useResetRecoilState(mainDataState);
  const setUrl = useSetRecoilState(settingUrl);
  const setSearch = useSetRecoilState(navSearchValue);

  const navigate = useNavigate();

  const invalidate = () => {
    resetMainPlaceData();
    setHasNextPage(true);
    setPage(1);
    setFocusCategoryID(0);
    navigate('/');
    setUrl(() => `/home?size=20&page=`);
  };

  const [isLogIn, setIsLogIn] = useRecoilState(HasRefresh);

  const onClickLogOutButton = async () => {
    await axios.delete('/auth/logout', {
      headers: {
        'ngrok-skip-browser-warning': '010',
        Authorization: (await localStorage.getItem('ACCESS'))
          ? `Bearer ${localStorage.getItem('ACCESS')}`
          : '',
        RefreshToken: (await localStorage.getItem('REFRESH'))
          ? localStorage.getItem('REFRESH')
          : '',
      },
    });
    await localStorage.removeItem('ACCESS');
    await localStorage.removeItem('REFRESH');

    await setIsLogIn(false);
    await invalidate();
    alert('로그아웃 되셨습니다');
  };

  const onClickAnotherPage = () => {
    setSearch('');
    invalidate();
    setUrl(() => `/home?size=20&page=`);
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
        <NavRightButton onClick={onClickAnotherPage}>
          <MyPageDiv>
            <BsFillPersonFill />
          </MyPageDiv>
        </NavRightButton>
      </Link>
    );
  }

  return (
    <Link to="/sign-up">
      <NavRightButton onClick={onClickAnotherPage}>Sign Up</NavRightButton>
    </Link>
  );
}

const NavLeftButton = styled.button`
  width: 80px;
  font-family: inherit;
  margin: 10px 7px;
  padding: 8px;
  border-radius: 20px;
  border: none;
  background-color: ${props => props.buttonColor || '#ffda77'};
  font-size: 0.8rem;
  font-weight: 500;
  line-height: 20px;
  text-align: center;
  color: #2b2b2b;
  &:hover {
    background-color: #fff9eb;
    transition: 0.7s;
    cursor: pointer;
  }
  &:focus {
    box-shadow: none;
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
