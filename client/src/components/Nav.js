import React, { useState } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useSetRecoilState } from 'recoil';
import styled from 'styled-components';
import { AiOutlineSearch } from 'react-icons/ai';
import { SlHome } from 'react-icons/sl';
import { BsFillPersonFill } from 'react-icons/bs';

import { navSearchValue, categoryFocus } from '../atoms';

function Nav({ navColor, buttonColor }) {
  const [currentSearch, setCurrentSearch] = useState('');
  const setSearch = useSetRecoilState(navSearchValue);
  const setFocusCategoryID = useSetRecoilState(categoryFocus);

  const location = useLocation();
  const navigate = useNavigate();

  const isLogIn = localStorage.getItem('Access_token');

  const onChangeSearch = e => {
    setCurrentSearch(e.target.value);
  };

  const onSubmit = async event => {
    event.preventDefault();

    const trimmedSearch = currentSearch.trim();
    const replacedSearch = trimmedSearch.replace(/ +(?= )/g, '');

    setSearch(replacedSearch);

    if (trimmedSearch) {
      try {
        const response = await axios.get(
          `{{BACKEND}}/search/${encodeURI(replacedSearch)}`,
        );
        response.then(
          setFocusCategoryID(0),
          setCurrentSearch(''),
          navigate('/'),
        );
      } catch (Error) {
        console.error(Error);
        setCurrentSearch('');
      }
      // .then(res => console.log(res))
      // .then(setFocusCategoryID(0), setCurrentSearch(''), navigate('/'))
      // .catch(err => console.log(err));
    } else {
      setCurrentSearch('');
    }
  };

  const onClickSearch = () => {
    const trimmedSearch = currentSearch.trim();
    const replacedSearch = trimmedSearch.replace(/ +(?= )/g, '');

    setSearch(replacedSearch);

    if (trimmedSearch) {
      axios
        .get(`{{BACKEND}}/search/${encodeURI(replacedSearch)}`)
        .then(res => console.log(res))
        .then(setFocusCategoryID(0), setCurrentSearch(''), navigate('/'));
    } else {
      setCurrentSearch('');
    }
  };

  const onClickLogOutButton = async () => {
    if (!isLogIn) return null;
    if (location.pathname !== '/my-page') return null;

    await localStorage.removeItem('Access_token');
    return null;
  };

  const onClickHomeIcon = () => {
    axios
      .get('{{backend}}/')
      .then(res => console.log(res), setFocusCategoryID(0), setSearch(''))
      .catch(err => console.log(err));
  };

  return (
    <NavContainer>
      <NavBackground navColor={navColor}>
        <Link to="/">
          <SlHome onClick={onClickHomeIcon} className="NavLogo" />
          <div />
        </Link>
        <SearchContainer onSubmit={onSubmit}>
          <SearchInput value={currentSearch} onChange={onChangeSearch} />
          <AiOutlineSearch onClick={onClickSearch} className="searchIcon" />
        </SearchContainer>
        <ButtonContainer>
          {location.pathname === '/register' ||
          location.pathname === '/log-in' ? null : (
            <Link to={isLogIn ? '/register' : '/log-in'}>
              <NavLeftButton buttonColor={buttonColor}>
                {isLogIn ? '장소 등록' : 'Log In'}
              </NavLeftButton>
            </Link>
          )}
          {location.pathname === '/sign-up' ? null : (
            <Link
              to={
                !isLogIn
                  ? '/sign-up'
                  : location.pathname === '/my-page'
                  ? '/'
                  : '/my-page'
              }
            >
              <NavRightButton onClick={onClickLogOutButton}>
                {!isLogIn ? (
                  'Sign Up'
                ) : location.pathname === '/my-page' ? (
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

const SearchContainer = styled.form`
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
