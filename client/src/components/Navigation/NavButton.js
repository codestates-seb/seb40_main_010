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
  reservationEditData,
  registerFormMaxCapacity,
  userMbtiValue,
  tokenAtom,
} from '../../atoms';
import callHeader from '../../utils/header';

export function NavLeftButtonContainer() {
  const logInUrl = useMatch('/log-in');
  const registerUrl = useMatch('/register');

  const isLogIn = useRecoilValue(HasRefresh);
  const setHasNextPage = useSetRecoilState(NextPage);
  const setFocusCategoryID = useSetRecoilState(categoryFocus);
  const setPage = useSetRecoilState(pageState);
  const resetMainPlaceData = useResetRecoilState(mainDataState);
  const setUrl = useSetRecoilState(settingUrl);
  const setSearch = useSetRecoilState(navSearchValue);
  const setEditData = useSetRecoilState(reservationEditData);
  const resetMaxCapacity = useSetRecoilState(registerFormMaxCapacity);

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

  const onClickRegisterPage = () => {
    setEditData(null);
    setSearch('');
    resetMaxCapacity(1);
    invalidate();
    setUrl(() => `/home?size=20&page=`);
  };

  if (isLogIn) {
    return (
      <Link to="/register">
        <NavLeftButton onClick={onClickRegisterPage}>장소등록</NavLeftButton>
      </Link>
    );
  }
  return (
    <Link to="/log-in">
      <NavLeftButton onClick={onClickAnotherPage}>로그인</NavLeftButton>
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
  const resetUserMbti = useResetRecoilState(userMbtiValue);
  const resetTokenState = useResetRecoilState(tokenAtom);

  const navigate = useNavigate();

  const invalidate = () => {
    resetMainPlaceData();
    setHasNextPage(true);
    setPage(1);
    setFocusCategoryID(0);
    navigate('/');
    setUrl(() => `/home?size=20&page=`);
  };

  const header = callHeader();

  const [isLogIn, setIsLogIn] = useRecoilState(HasRefresh);

  const onClickLogOutButton = async () => {
    await axios.delete(
      `${process.env.REACT_APP_SERVER_BASE_URL}/auth/logout`,
      header,
    );
    resetTokenState();
    await localStorage.removeItem('ACCESS');
    await localStorage.removeItem('REFRESH');

    await setIsLogIn(false);
    await invalidate();
    resetUserMbti();
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
        <NavRightButton onClick={onClickLogOutButton}>로그아웃</NavRightButton>
      </Link>
    );
  }

  if (isLogIn && !myPageUrl) {
    return (
      <Link to="/my-page">
        <NavMyPageButton onClick={onClickAnotherPage}>
          <MyPageDiv>
            <BsFillPersonFill />
          </MyPageDiv>
        </NavMyPageButton>
      </Link>
    );
  }

  return (
    <Link to="/sign-up">
      <NavRightButton onClick={onClickAnotherPage}>회원가입</NavRightButton>
    </Link>
  );
}

const NavMyPageButton = styled.button`
  width: 35px;
  height: 35px;
  font-family: inherit;
  margin: 10px 10px;
  padding: 10px 0px 0px 0px;
  border-radius: 20px;
  border: none;
  background-color: #ffda77;
  font-size: 1.8rem;
  font-weight: 500;
  line-height: 20px;
  text-align: center;
  overflow: hidden;
  color: #fff9eb;
  &:hover {
    background-color: #eb7470;
    transition: 0.7s;
    cursor: pointer;
  }
  &:focus {
    box-shadow: none;
  }
`;

const NavLeftButton = styled.button`
  width: 80px;
  font-family: inherit;
  margin: 10px 7px;
  padding: 8px;
  border-radius: 20px;
  border: none;
  background-color: #ffda77;
  font-size: 0.8rem;
  font-weight: 500;
  line-height: 20px;
  text-align: center;
  color: #2b2b2b;
  &:hover {
    background-color: #eb7470;
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
