import React from 'react';
import styled from 'styled-components';
import { AiOutlineSearch } from 'react-icons/ai';
import { SlHome } from 'react-icons/sl';
import { Link } from 'react-router-dom';

const NavContainer = styled.div``;

const NavBg = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  position: fixed;
  top: 0;
  /* width: 100% */
  left: 0;
  right: 0;
  width: 100vw;
  height: 70px;
  background-color: #89bbff;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  .NavLogo {
    /* width: 208px; */
    margin-right: 156px;
    font-size: 2rem;
    margin-left: 20px;
    margin-top: 10px;
    margin-bottom: 10px;
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
  /* height: 30px; */
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
  background-color: #ffda77;
  font-size: 0.9rem;
  font-weight: 600;
  line-height: 20px;
  text-align: center;
  color: #2b2b2b;
  &:hover {
    background-color: #fff9eb; //hover 시 색 변환 어떻게 할까요,,,??
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

function Nav() {
  return (
    <NavContainer>
      <NavBg>
        <Link to="/">
          <SlHome className="NavLogo" />
        </Link>
        <SearchDiv>
          <SearchInput />
          <AiOutlineSearch className="searchLogo" />
        </SearchDiv>
        <ButtonContainer>
          <Link to="/register">
            <NavButton>장소 등록</NavButton>
          </Link>
          <NavLogOutButton>Log out</NavLogOutButton>
        </ButtonContainer>
      </NavBg>
    </NavContainer>
  );
}

export default Nav;
