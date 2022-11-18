/*

+ 한글 마지막 글자가 중복되는 현상 한글 외에는 괜찮음 > keydown이벤트를 keypress로 변경하면서 수정됨 > 한글이 구성되는 시간이 걸려서 생기는 문제
추가할 사항 
3. ui 요소
4. 완료시 console.log 없애기, 변수명 정리

title 없을 때 전체 카테고리 클릭 시
mypage 갔다가 nav 홈 로고 누르면 카테고리 요청이 그대로 유지되는 현상 > 속도 때문
*/

import React, { useState } from 'react';
import styled from 'styled-components';
import { AiOutlineSearch } from 'react-icons/ai';
import { SlHome } from 'react-icons/sl';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { BsFillPersonFill } from 'react-icons/bs';
import axios from 'axios';
import { navSearchValue, categoryFocus } from '../atoms';

const NavContainer = styled.div`
  position: relative;
  z-index: 100;
`;

const NavBg = styled.div`
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

const SearchDiv = styled.div`
  width: 40%;
  display: flex;
  justify-content: row;
  position: relative;
  margin: 0;
  text-align: center;
  align-items: center;
  .searchLogo {
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

const NavButton = styled.button`
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

const NavLogOutButton = styled(NavButton)`
  &:hover {
    background-color: #eb7470;
    transition: 0.5s;
  }
`;

const MyPageDiv = styled.div`
  background-color: aliceblue;
  border-radius: 50px;
`;

function Nav({ navColor, buttonColor }) {
  const [testSearchValue, setTestSearchValue] = useState('');
  const setSearchValue = useSetRecoilState(navSearchValue);
  const setFocusCategoryID = useSetRecoilState(categoryFocus);

  const location = useLocation();
  const navigate = useNavigate();

  window.localStorage.setItem('Access_token', 'test');
  // window.localStorage.removeItem('Access_token');
  // window.sessionStorage.setItem('Access_token', 'test');

  const isLogIn = window.localStorage.getItem('Access_token');

  const onChangeSearch = e => {
    setTestSearchValue(e.target.value);
  };
  const onKeypress = e => {
    if (e.key === 'Enter') {
      setTestSearchValue('');
      setSearchValue(testSearchValue.trim().replace(/ +(?= )/g, ''));
      if (testSearchValue.trim()) {
        axios
          .get(
            `{{BACKEND}}/search/${encodeURI(
              testSearchValue.trim().replace(/ +(?= )/g, ''),
            )}`,
          )
          .then(res => console.log(res))
          .then(setFocusCategoryID('0'), navigate('/'));
      }
    }
  };
  const onClickSearch = () => {
    setTestSearchValue('');
    setSearchValue(testSearchValue.trim().replace(/ +(?= )/g, ''));
    if (testSearchValue.trim()) {
      axios
        .get(
          `{{BACKEND}}/search/${encodeURI(
            testSearchValue.trim(' ').replace(/ +(?= )/g, ''),
          )}`,
        )
        .then(res => console.log(res))
        .then(setFocusCategoryID('0'), navigate('/'));
    }
  };

  const onClickLogOutButton = () => {
    window.localStorage.removeItem('Access_token');
  };

  const onClickHomeIcon = () => {
    axios
      .get('{{backend}}/')
      .then(
        res => console.log(res),
        setFocusCategoryID('0'),
        setSearchValue(''),
      );
    // .catch(err => console.log(err));
  };
  return (
    <NavContainer>
      <NavBg navColor={navColor}>
        <Link to="/">
          <SlHome onClick={onClickHomeIcon} className="NavLogo" />
          <div />
        </Link>
        <SearchDiv>
          <SearchInput
            value={testSearchValue}
            onChange={e => onChangeSearch(e)}
            onKeyPress={onKeypress}
          />
          <AiOutlineSearch onClick={onClickSearch} className="searchLogo" />
        </SearchDiv>
        <ButtonContainer>
          {location.pathname === '/register' ||
          location.pathname === '/login' ? null : (
            <Link to={isLogIn ? '/register' : '/login'}>
              <NavButton buttonColor={buttonColor}>
                {isLogIn ? '장소 등록' : 'Log In'}
              </NavButton>
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
              <NavLogOutButton
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
              </NavLogOutButton>
            </Link>
          )}
        </ButtonContainer>
      </NavBg>
    </NavContainer>
  );
}

export default Nav;
