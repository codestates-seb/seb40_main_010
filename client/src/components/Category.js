// import axios from 'axios';
import React from 'react';
import styled from 'styled-components';
import { useRecoilState, useRecoilValue } from 'recoil';

import { navSearchValue, categoryFocus, mainDataState } from '../atoms';
import { categoryData, getURL } from '../utils/categoryData';
import getData from '../hooks/useAsyncGetData';
import categoryButton from '../utils/categoryButton';

function Category() {
  const [focusCategoryID, setFocusCategoryID] = useRecoilState(categoryFocus);
  const [, setMainPlaceData] = useRecoilState(mainDataState);

  const searchState = useRecoilValue(navSearchValue);

  // const header = {
  //   headers: {
  //     'ngrok-skip-browser-warning': '010',
  //   },
  // };
  const categories = categoryData;

  const onClickCategoryButton = async (e, idx) => {
    // console.log(typeof idx);
    setFocusCategoryID(idx);

    // const getURL = (index, search) => {
    //   if (index === 0 && search) {
    //     return `/search/${encodeURI(search)}`;
    //   }
    //   if (index === 0 && !search) {
    //     return `/home`;
    //   }
    //   if (index !== 0 && search) {
    //     return `/category/${index}/search/${encodeURI(search)}`;
    //   }
    //   if (index !== 0 && !search) {
    //     return `/category/${index}`;
    //   }
    //   return '';
    // };

    const url = getURL(idx, searchState);

    // try {
    const response = await getData(url);
    // console.log(response);
    setMainPlaceData(response.data.data);
    // console.log(mainPlaceData);
    // } catch (error) {
    // console.log('Error', error);
    // }
  };

  return (
    <CategoryContainer>
      {categories.map((category, idx) => {
        return categoryButton({
          category,
          idx,
          onClickCategoryButton,
          focusCategoryID,
        });
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

// const CategoryButton = styled.button`
//   font-family: inherit;
//   -webkit-appearance: none;
//   -moz-appearance: none;
//   appearance: none;
//   background-color: inherit;
//   color: #aaaaaa;
//   border: none;
//   padding: 5px;
//   font-weight: 600;
//   display: flex;
//   flex-direction: column;
//   justify-content: center;
//   align-items: center;
//   margin: 5px;
//   word-break: keep-all;

//   > i {
//     font-size: 1.5rem;
//     padding: 5px;
//   }

//   &:hover {
//     color: #eb7470;
//     cursor: pointer;
//   }
//   &.focus {
//     color: #eb7470;
//   }
// `;
