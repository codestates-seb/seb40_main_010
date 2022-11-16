/*
nav : 로그인 된 컴포넌트 완성
검색 기능 카테고리 클릭 시 배열 형태로 input value는 스트링 형태로 api 요청 보낼까요??
ex) search/inputvalue [1,2,3](중간에 띄어있는 형태 붙일 수 있음)

추가할 사항 
+ 한글 마지막 글자가 중복되는 현상 한글 외에는 괜찮음 > keydown이벤트를 keypress로 변경하면서 수정됨 > 한글이 구성되는 시간이 걸려서 생기는 문제
1. 체크박스 checked일때만 배열에 넣게하기 지금은 클릭 on off 중복으로 배열이 들어가는 상태 => 구현 완료 recoil로 카테고리 배열 상태 저장
2. 로그인 상태에 다른 버튼 변화
3. ui 요소
4. 완료시 console.log 없애기, 변수명 정리
*/

import React from 'react';
import styled from 'styled-components';
import { AiOutlineSearch } from 'react-icons/ai';
import { SlHome } from 'react-icons/sl';
import { Link } from 'react-router-dom';
import { useRecoilState } from 'recoil';
import {
  navSearchValue,
  navSearchState,
  navClickState,
  navCategoryCheckState,
} from '../atoms';

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
  background-color: #89bbff;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  .NavLogo {
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

// ToDo: hover 시 색 변환
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

const CATEGORY_LIST = [
  { id: 0, data: '공유오피스' },
  { id: 1, data: '캠핑' },
  { id: 2, data: '바다근처' },
  { id: 3, data: '짐보관' },
  { id: 4, data: '파티룸' },
  { id: 5, data: '게스트하우스' },
  { id: 6, data: '호텔' },
  { id: 7, data: '스터디룸' },
  { id: 8, data: '계곡근처' },
  { id: 9, data: '공연장' },
];

// Todo: recoil사용, searchState 배열 초기화, 변수 이름
function Nav() {
  const [searchValue, setSearchValue] = useRecoilState(navSearchValue);
  const [searchState, setSearchState] = useRecoilState(navSearchState);
  const [isfocus, setIsFocus] = useRecoilState(navClickState);
  const [checkedList, setCheckedList] = useRecoilState(navCategoryCheckState);

  const onCheckedElememt = (checked, item) => {
    if (checked) {
      setCheckedList([...checkedList, item]);
    } else if (!checked) {
      setCheckedList(checkedList.filter(el => el !== item));
    }
  };
  console.log(checkedList);
  const onChangeSearch = e => {
    setSearchValue(e.target.value);
  };
  const onKeypress = e => {
    if (e.key === 'Enter') {
      setSearchState([searchValue, checkedList]);
      setSearchValue('');
      setCheckedList([]);
    }
  };
  const onClickSearch = () => {
    setSearchState([searchValue, checkedList]);
    setSearchValue('');
    setCheckedList([]);
  };

  const onClick = () => {
    setIsFocus(true);
  };

  const result = [...searchState];
  console.log(`[${result[1]}] ${result[0]}`);

  return (
    <NavContainer>
      <NavBg>
        <Link to="/">
          <SlHome className="NavLogo" />
        </Link>
        <div>
          <SearchDiv>
            <SearchInput
              value={searchValue}
              onChange={e => onChangeSearch(e)}
              onKeyPress={onKeypress}
              onClick={onClick}
            />
            <AiOutlineSearch onClick={onClickSearch} className="searchLogo" />
          </SearchDiv>
          {isfocus ? (
            <div>
              {CATEGORY_LIST.map(el => {
                return (
                  <label key={el.id} htmlFor={el.id}>
                    <input
                      type="checkbox"
                      id={el.id}
                      value={el.data}
                      onChange={e => {
                        onCheckedElememt(e.target.checked, e.target.value);
                      }}
                      checked={!!checkedList.includes(el.data)}
                    />
                    {el.data}
                  </label>
                );
              })}
            </div>
          ) : null}
        </div>
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
