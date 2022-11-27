// import axios from 'axios';
import React from 'react';
import styled from 'styled-components';
import { useRecoilState, useRecoilValue, useSetRecoilState } from 'recoil';

import { navSearchValue, categoryFocus, mainDataState } from '../atoms';
import { categoryData, getURL } from '../utils/categoryData';
import getData from '../hooks/useAsyncGetData';
import categoryButton from '../utils/categoryButton';

function Category() {
  const [focusCategoryID, setFocusCategoryID] = useRecoilState(categoryFocus);
  const setMainPlaceData = useSetRecoilState(mainDataState);
  const searchState = useRecoilValue(navSearchValue);

  const categories = categoryData;

  const onClickCategoryButton = async (e, idx) => {
    setFocusCategoryID(idx);

    const url = getURL(idx, searchState);

    const response = await getData(url);

    setMainPlaceData([...response.data.data]);
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
