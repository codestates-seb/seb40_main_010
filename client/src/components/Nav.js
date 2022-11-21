/*
+ 한글 마지막 글자가 중복되는 현상 한글 외에는 괜찮음 > keydown이벤트를 keypress로 변경하면서 수정됨 > 한글이 구성되는 시간이 걸려서 생기는 문제
추가할 사항 
3. ui 요소
4. 완료시 console.log 없애기, 변수명 정리
*/

import React, { useState } from 'react';
import styled from 'styled-components';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { AiOutlineSearch } from 'react-icons/ai';
import { SlHome } from 'react-icons/sl';
import { BsFillPersonFill } from 'react-icons/bs';
import { useSetRecoilState } from 'recoil';
import { navSearchValue, categoryFocus } from '../atoms';

const NavContainer = styled.div`
  position: relative;
  z-index: 100;
`;

const NavBackground = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  width: 100vw;
  height: 70px;
  background-color: ${props => props.navColor || '#89bbff'};
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;

  & > div {
    margin-left: 136px;
  }
  .NavLogo {
    font-size: 2rem;
    padding-right: 20px;
    padding-left: 20px;
    padding-top: 10px;
    padding-bottom: 10px;
    color: #2b2b2b;
    &:hover {
      cursor: pointer;
    }
  }
`;

const SearchContainer = styled.div`
  width: 40%;
  display: flex;
  justify-content: row;
  position: relative;
  margin: 0;
  text-align: center;
  align-items: center;
  .searchIcon {
    position: absolute;
    right: 0;
    margin: 0;
    font-size: 1.4rem;
    color: #515151;
    margin-right: 20px;
    padding-top: 10px;
    padding-bottom: 10px;
    &:hover {
      font-size: 1.8rem;
      color: #89bbff;
      cursor: pointer;
    }
  }
`;

const SearchInput = styled.input`
  margin: 10px 0;
  padding: 10px 45px 10px 25px;
  width: 100%;
  border-radius: 20px;
  border: none;
  background-color: #fff9eb;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  caret-color: #89bbff;
  &:focus {
    outline: none;
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-right: 20px;
`;

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

function Nav({ navColor, buttonColor }) {
  const [temporarySearchValue, setTemporarySearchValue] = useState('');
  const setSearchValue = useSetRecoilState(navSearchValue);
  const setFocusCategoryID = useSetRecoilState(categoryFocus);

  const location = useLocation();
  const navigate = useNavigate();

  window.localStorage.setItem('Access_token', 'test');
  // window.localStorage.removeItem('Access_token');
  // window.sessionStorage.setItem('Access_token', 'test');

  const isLogIn = window.localStorage.getItem('Access_token');

  const onChangeSearch = e => {
    setTemporarySearchValue(e.target.value);
  };

  const onKeypress = e => {
    if (e.key === 'Enter') {
      setSearchValue(temporarySearchValue.trim().replace(/ +(?= )/g, ''));
      if (temporarySearchValue.trim()) {
        axios
          .get(
            `{{BACKEND}}/search/${encodeURI(
              temporarySearchValue.trim().replace(/ +(?= )/g, ''),
            )}`,
          )
          .then(res => console.log(res))
          .then(
            setFocusCategoryID(0),
            setTemporarySearchValue(''),
            navigate('/'),
          );
      } else {
        setTemporarySearchValue('');
      }
    }
  };
  const onClickSearch = () => {
    setSearchValue(temporarySearchValue.trim().replace(/ +(?= )/g, ''));
    if (temporarySearchValue.trim()) {
      axios
        .get(
          `{{BACKEND}}/search/${encodeURI(
            temporarySearchValue.trim(' ').replace(/ +(?= )/g, ''),
          )}`,
        )
        .then(res => console.log(res))
        .then(
          setFocusCategoryID(0),
          setTemporarySearchValue(''),
          navigate('/'),
        );
    } else {
      setTemporarySearchValue('');
    }
  };

  const onClickLogOutButton = () => {
    window.localStorage.removeItem('Access_token');
  };

  const onClickHomeIcon = () => {
    axios
      .get('{{backend}}/')
      .then(res => console.log(res), setFocusCategoryID(0), setSearchValue(''))
      .catch(err => console.log(err));
  };
  return (
    <NavContainer>
      <NavBackground navColor={navColor}>
        <Link to="/">
          <SlHome onClick={onClickHomeIcon} className="NavLogo" />
          <div />
        </Link>
        <SearchContainer>
          <SearchInput
            value={temporarySearchValue}
            onChange={e => onChangeSearch(e)}
            onKeyPress={onKeypress}
          />
          <AiOutlineSearch onClick={onClickSearch} className="searchIcon" />
        </SearchContainer>
        <ButtonContainer>
          {location.pathname === '/register' ||
          location.pathname === '/login' ? null : (
            <Link to={isLogIn ? '/register' : '/login'}>
              <NavLeftButton buttonColor={buttonColor}>
                {isLogIn ? '장소 등록' : 'Log In'}
              </NavLeftButton>
            </Link>
          )}
          {location.pathname === '/signup' ? null : (
            <Link
              to={
                !isLogIn
                  ? '/signup'
                  : location.pathname === '/mypage'
                  ? '/'
                  : '/mypage'
              }
            >
              <NavRightButton
                onClick={
                  location.pathname === '/mypage' && isLogIn
                    ? onClickLogOutButton
                    : null
                }
              >
                {!isLogIn ? (
                  'Sign Up'
                ) : location.pathname === '/mypage' ? (
                  'Log Out'
                ) : (
                  <MyPageDiv>
                    <BsFillPersonFill />
                  </MyPageDiv>
                )}
              </NavRightButton>
            </Link>
          )}
        </ButtonContainer>
      </NavBackground>
    </NavContainer>
  );
}

export default Nav;
