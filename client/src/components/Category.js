/*
1. 카테고리 누르면 id에 따라 api 요청이 가도록 구현 > 실제 api 요청이 가기 때문에 주석처리
2. 분류명 '전체' 추가, 고정 스타일로 구현 (main에서 useEffect로 전체조회로 요청한다고 생각해서 카테고리 컴포넌트에서는 클릭 시 api요청이 가게 구현)
3. focus된 id가 전역 상태로 관리될 필요가 없다 생각하여 useState로 관리
4. 카테고리를 배열로 관리하면서 react-icons 모듈 사용이 어려워 index.html에 cdn fontawesome 사용
5. icon 클릭시 api 요청 2번 감(div-글자는 1번만 감) > z-index 조절하면 되기는 하는데,,, 
*/

// import axios from 'axios';
import React, { useState } from 'react';
import styled from 'styled-components';

const categories = [
  { id: 0, name: '전체', icon: 'fa-solid fa-house' },
  { id: 1, name: '공유오피스', icon: 'fa-solid fa-user-group' },
  { id: 2, name: '캠핑', icon: 'fa-solid fa-campground' },
  { id: 3, name: '바다근처', icon: 'fa-solid fa-umbrella-beach' },
  { id: 4, name: '짐보관', icon: 'fa-solid fa-suitcase-rolling' },
  { id: 5, name: '파티룸', icon: 'fa-solid fa-champagne-glasses' },
  { id: 6, name: '게스트하우스', icon: 'fa-solid fa-bed' },
  { id: 7, name: '호텔', icon: 'fa-solid fa-hotel' },
  { id: 8, name: '스터디룸', icon: 'fa-solid fa-pen-to-square' },
  { id: 9, name: '계곡근처', icon: 'fa-solid fa-water' },
  { id: 10, name: '공연장', icon: 'fa-solid fa-microphone' },
];

const CategoryContainer = styled.div`
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

  > i {
    font-size: 1.5rem;
    padding: 5px;
    z-index: -1;
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
  const [focusCategoryID, setFocusCategoryID] = useState([0]);
  const onClickCategoryButton = event => {
    setFocusCategoryID([event.target.id]);
    // if (focusCategoryID[0] === 0) {
    //   axios.get(`{{BACKEND}}/`).then(res => console.log(res));
    // } else {
    //   axios
    //     .get(`{{BACKEND}}/category/${event.target.id}`)
    //     .then(res => console.log(res));
    // }
    // console.log(event.target.id);
    // console.log(event.target.id);
  };

  return (
    <CategoryContainer>
      {categories.map(category => {
        return (
          <CategoryButton
            type="button"
            key={category.id}
            id={category.id}
            onClick={onClickCategoryButton}
            className={
              Number(focusCategoryID[0]) === category.id ? 'focus' : null
            }
          >
            <i
              role="presentation"
              id={category.id}
              className={category.icon}
              onClick={e => onClickCategoryButton(e)}
            />
            {category.name}
          </CategoryButton>
        );
      })}
    </CategoryContainer>
  );
}

export default Category;
