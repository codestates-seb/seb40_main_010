/*
1. 카테고리 누르면 id에 따라 api 요청이 가도록 구현 > 실제 api 요청이 가기 때문에 주석처리
2. 분류명 '전체' 추가, 고정 스타일로 구현 (main에서 useEffect로 전체조회로 요청한다고 생각해서 카테고리 컴포넌트에서는 클릭 시 api요청이 가게 구현)
3. 카테고리를 배열로 관리하면서 react-icons 모듈 사용이 어려워 index.html에 cdn fontawesome 사용

아이콘과 버튼태그에 id중복 > 아이콘을 클릭하든 버튼 클릭하든 엔드포인트에 id로 api 요청이 가서 둘 다 값이 필요한 상황
*/

import axios from 'axios';
import React from 'react';
import styled from 'styled-components';
import { useRecoilState, useRecoilValue } from 'recoil';
import { navSearchValue, categoryFocus } from '../atoms';

const categories = [
  { categoryId: 0, name: '전체', icon: 'fa-solid fa-house' },
  { categoryId: 1, name: '공유오피스', icon: 'fa-solid fa-user-group' },
  { categoryId: 2, name: '캠핑', icon: 'fa-solid fa-campground' },
  { categoryId: 3, name: '바다근처', icon: 'fa-solid fa-umbrella-beach' },
  { categoryId: 4, name: '짐보관', icon: 'fa-solid fa-suitcase-rolling' },
  { categoryId: 5, name: '파티룸', icon: 'fa-solid fa-champagne-glasses' },
  { categoryId: 6, name: '게스트하우스', icon: 'fa-solid fa-bed' },
  { categoryId: 7, name: '호텔', icon: 'fa-solid fa-hotel' },
  { categoryId: 8, name: '스터디룸', icon: 'fa-solid fa-pen-to-square' },
  { categoryId: 9, name: '계곡근처', icon: 'fa-solid fa-water' },
  { categoryId: 10, name: '공연장', icon: 'fa-solid fa-microphone' },
];

const CategoryContainer = styled.div`
  margin-top: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const CategoryButton = styled.button`
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-color: inherit;
  color: #aaaaaa;
  border: none;
  padding: 5px;
  font-weight: 600;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 5px;
  word-break: keep-all;

  > i {
    font-size: 1.5rem;
    padding: 5px;
  }

  &:hover {
    color: #eb7470;
    cursor: pointer;
  }
  &.focus {
    color: #eb7470;
  }
`;

// ToDo : axios 사용해서 데이터 받아서 console에 띄워주는걸 주석표시 > recoil로 받아온 데이터를 저장하는 방법 고려
function Category() {
  const [focusCategoryID, setFocusCategoryID] = useRecoilState(categoryFocus);
  const searchState = useRecoilValue(navSearchValue);

  const onClickCategoryButton = (e, idx) => {
    // console.log(typeof idx);
    setFocusCategoryID(idx);
    if (idx === 0) {
      if (searchState) {
        axios
          .get(`{{BACKEND}}/search/${encodeURI(searchState)}`)
          .then(res => console.log(res));
      } else {
        axios.get(`{{BACKEND}}/`).then(res => console.log(res));
      }
    } else if (searchState) {
      axios
        .get(`{{BACKEND}}/category/${idx}/search/${encodeURI(searchState)}`)
        .then(res => console.log(res));
    } else {
      axios
        .get(`{{BACKEND}}/category/${idx}/search`)
        .then(res => console.log(res));
    }
  };

  return (
    <CategoryContainer>
      {categories.map((category, idx) => {
        return (
          <CategoryButton
            type="button"
            key={category.categoryId}
            onClick={e => onClickCategoryButton(e, idx)}
            className={focusCategoryID === category.categoryId ? 'focus' : null}
          >
            <i className={category.icon} />
            {category.name}
          </CategoryButton>
        );
      })}
    </CategoryContainer>
  );
}

export default Category;
