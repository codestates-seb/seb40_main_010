import axios from 'axios';
import React from 'react';
import styled from 'styled-components';
import { useRecoilState, useRecoilValue } from 'recoil';

import { navSearchValue, categoryFocus } from '../atoms';

function Category() {
  const [focusCategoryID, setFocusCategoryID] = useRecoilState(categoryFocus);
  const searchState = useRecoilValue(navSearchValue);

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

  const onClickCategoryButton = (e, idx) => {
    // console.log(typeof idx);
    setFocusCategoryID(idx);

    const getURL = (index, search) => {
      if (index === 0 && search) {
        return `{{BACKEND}}/search/${encodeURI(search)}`;
      }
      if (index === 0 && !search) {
        return `{{BACKEND}}/`;
      }
      if (index !== 0 && search) {
        return `{{BACKEND}}/category/${index}/search/${encodeURI(search)}`;
      }
      if (index !== 0 && !search) {
        return `{{BACKEND}}/category/${index}/search`;
      }
      return '';
    };

    const url = getURL(idx, searchState);

    axios
      .get(url)
      .then(res => console.log(res))
      .catch(err => console.log(err));
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
