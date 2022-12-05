import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { useSetRecoilState, useResetRecoilState, useRecoilState } from 'recoil';
import styled from 'styled-components';
import { AiOutlineSearch } from 'react-icons/ai';

import {
  navSearchValue,
  categoryFocus,
  mainDataState,
  settingUrl,
  pageState,
  NextPage,
} from '../../atoms';
import { NavLeftButtonContainer, NavRightButtonContainer } from './NavButton';

function Nav() {
  const [currentSearch, setCurrentSearch] = useState('');

  const setSearch = useSetRecoilState(navSearchValue);
  const [focusCategoryID, setFocusCategoryID] = useRecoilState(categoryFocus);
  const setPage = useSetRecoilState(pageState);
  const resetMainPlaceData = useResetRecoilState(mainDataState);
  const setUrl = useSetRecoilState(settingUrl);
  const setHasNextPage = useSetRecoilState(NextPage);

  const navigate = useNavigate();

  const invalidate = () => {
    setCurrentSearch('');
    resetMainPlaceData();
    setHasNextPage(true);
    setPage(() => 1);
    // setFocusCategoryID(0);
    navigate('/');
  };

  const onChangeSearch = event => {
    setCurrentSearch(event.target.value);
  };

  // eslint-disable-next-line consistent-return
  const onSubmit = event => {
    event.preventDefault();

    const trimmedSearch = currentSearch.trim();
    const replacedSearch = trimmedSearch.replace(/ +(?= )/g, '');

    setSearch(replacedSearch);

    if (!trimmedSearch) {
      alert('검색어를 입력해주세요');
      invalidate();
      setUrl(() => `/home?size=20&page=`);
      return setCurrentSearch('');
    }

    const encoded = encodeURI(replacedSearch);

    if (focusCategoryID === 0) {
      const url = `/search/${encoded}?size=20&page=`;
      setUrl(() => url);
      invalidate();
    }
    if (focusCategoryID !== 0) {
      const url = `/category/${focusCategoryID}/search/${encoded}?size=20&page=`;
      setUrl(() => url);
      invalidate();
    }

    // setUrl(() => url);
    // invalidate();
  };

  // eslint-disable-next-line consistent-return
  const onClickSearch = async () => {
    const trimmedSearch = currentSearch.trim();
    const replacedSearch = trimmedSearch.replace(/ +(?= )/g, '');

    setSearch(replacedSearch);

    if (!trimmedSearch) {
      alert('검색어를 입력해주세요');
      invalidate();
      setUrl(() => `/home?size=20&page=`);
      return setCurrentSearch('');
    }

    const encoded = encodeURI(replacedSearch);

    if (focusCategoryID === 0) {
      const url = `/search/${encoded}?size=20&page=`;
      setUrl(() => url);
      invalidate();
    }
    if (focusCategoryID !== 0) {
      const url = `/category/${focusCategoryID}/search/${encoded}?size=20&page=`;
      setUrl(() => url);
      invalidate();
    }

    // const url = `/search/${encoded}?size=20&page=`;

    // setUrl(() => url);
    invalidate();
  };

  const onClickHomeIcon = () => {
    setSearch('');
    invalidate();
    setFocusCategoryID(0);
    setUrl(() => `/home?size=20&page=`);
  };

  return (
    <NavContainer>
      <NavBackground>
        <Img src="/logo1.png" alt="logo" onClick={onClickHomeIcon} />
        <SearchContainer onSubmit={onSubmit}>
          <SearchInput value={currentSearch} onChange={onChangeSearch} />
          <AiOutlineSearch onClick={onClickSearch} className="searchIcon" />
        </SearchContainer>
        <ButtonContainer>
          <NavLeftButtonContainer />
          <NavRightButtonContainer />
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
  height: 85px;
  border-bottom: 1px solid #d7d7d7;
  background-color: #ffffff;

  .NavLogo {
    font-size: 2rem;
    margin-right: 9vw;
    margin-left: 20px;
    margin-top: 10px;
    margin-bottom: 10px;
    color: #2b2b2b;
    &:hover {
      cursor: pointer;
    }
  }
`;

const Img = styled.img`
  margin-left: 40px;
  width: 140px;
  &:hover {
    cursor: pointer;
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
  font-family: inherit;
  margin: 10px 0;
  padding: 10px 45px 10px 25px;
  width: 100%;
  border-radius: 20px;
  border: 1px solid #d7d7d7;
  background-color: #ffffff;
  caret-color: #89bbff;
  &:focus {
    outline: none;
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: right;
  align-items: center;
  margin-right: 20px;
  margin-left: 20px;
  width: 188px;
`;
