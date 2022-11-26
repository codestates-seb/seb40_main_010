import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useSetRecoilState } from 'recoil';
import styled from 'styled-components';
import { AiOutlineSearch } from 'react-icons/ai';
import { SlHome } from 'react-icons/sl';

import { navSearchValue, categoryFocus, mainDataState } from '../../atoms';
import { NavLeftButtonContainer, NavRightButtonContainer } from './NavButton';
// ToDo : async await 수정
function Nav({ navColor, buttonColor }) {
  const [currentSearch, setCurrentSearch] = useState('');
  const setSearch = useSetRecoilState(navSearchValue);
  const setFocusCategoryID = useSetRecoilState(categoryFocus);
  const setMainPlaceData = useSetRecoilState(mainDataState);

  const navigate = useNavigate();

  const onChangeSearch = e => {
    setCurrentSearch(e.target.value);
  };
  const header = {
    headers: {
      'ngrok-skip-browser-warning': '010',
    },
  };

  // eslint-disable-next-line consistent-return
  const onSubmit = async event => {
    event.preventDefault();

    const trimmedSearch = currentSearch.trim();
    const replacedSearch = trimmedSearch.replace(/ +(?= )/g, '');

    setSearch(replacedSearch);

    if (trimmedSearch) {
      try {
        const response = await axios.get(
          `/search/${encodeURI(replacedSearch)}`,
          header,
        );
        console.log(response.data);
        setFocusCategoryID(0);
        setMainPlaceData(response.data.data);
        setCurrentSearch('');
        navigate('/');

        return response;
      } catch (error) {
        setCurrentSearch('');
        setFocusCategoryID(0);
        setCurrentSearch('');
        navigate('/');

        alert('검색 error');
      }
    } else {
      setCurrentSearch('');
    }
  };

  // eslint-disable-next-line consistent-return
  const onClickSearch = () => {
    const trimmedSearch = currentSearch.trim();
    const replacedSearch = trimmedSearch.replace(/ +(?= )/g, '');

    setSearch(replacedSearch);
    if (!trimmedSearch) return setCurrentSearch('');

    axios
      .get(`/search/${encodeURI(replacedSearch)}`, header)
      .then(res => setMainPlaceData(res.data.data))
      .then(setFocusCategoryID(0), setCurrentSearch(''), navigate('/'));
  };

  const onClickHomeIcon = () => {
    axios
      .get('/home', header)
      .then(
        res => setMainPlaceData(res.data.data),
        setFocusCategoryID(0),
        setSearch(''),
      )
      .catch(err => console.log(err));
  };

  return (
    <NavContainer>
      <NavBackground navColor={navColor}>
        <Link to="/">
          <SlHome onClick={onClickHomeIcon} className="NavLogo" />
          <div className="structure" />
        </Link>
        <SearchContainer onSubmit={onSubmit}>
          <SearchInput value={currentSearch} onChange={onChangeSearch} />
          <AiOutlineSearch onClick={onClickSearch} className="searchIcon" />
        </SearchContainer>
        <ButtonContainer>
          <NavLeftButtonContainer buttonColor={buttonColor} />
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
  height: 70px;
  background-color: ${props => props.navColor || '#89bbff'};
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;

  .structure {
    width: 116px;
  }

  .NavLogo {
    font-size: 2rem;
    /* padding-right: 20px; */
    padding-right: 9vw;
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
  font-family: inherit;
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
  margin-left: 20px;
`;
