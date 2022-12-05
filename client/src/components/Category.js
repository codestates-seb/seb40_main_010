import React from 'react';
import styled from 'styled-components';
import {
  useRecoilState,
  useRecoilValue,
  useResetRecoilState,
  useSetRecoilState,
} from 'recoil';

import {
  navSearchValue,
  categoryFocus,
  mainDataState,
  pageState,
  settingUrl,
  NextPage,
} from '../atoms';
import { categoryData, getURL } from '../utils/categoryData';
import categoryButton from '../utils/categoryButton';

function Category() {
  const [focusCategoryID, setFocusCategoryID] = useRecoilState(categoryFocus);
  const setUrl = useSetRecoilState(settingUrl);
  const search = useRecoilValue(navSearchValue);
  const setPage = useSetRecoilState(pageState);
  const setHasNextPage = useSetRecoilState(NextPage);
  const resetMainPlaceData = useResetRecoilState(mainDataState);

  const categories = categoryData;

  const onClickCategoryButton = async (e, idx) => {
    resetMainPlaceData();
    setUrl(() => getURL(idx, search));
    setPage(1);
    setFocusCategoryID(idx);
    setHasNextPage(true);
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
  padding-top: 110px;
  display: flex;
  flex-wrap: wrap;
  justify-content: center;
  align-items: center;
  margin-bottom: 30px;
`;
