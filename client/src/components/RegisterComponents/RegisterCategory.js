import React from 'react';
import styled from 'styled-components';

import { categories } from '../../utils/categoryData';

function RegisterCategory({ addItem, removeItem }) {
  const handleCategory = event => {
    const isCategoryChecked = event.target.checked;
    const category = event.target.value;

    if (isCategoryChecked) {
      addItem(category);
    }
    if (!isCategoryChecked) {
      removeItem(category);
    }
  };

  return (
    <Wrapper>
      {categories.map(category => (
        <Category key={category.id}>
          <input
            type="checkbox"
            id={category.id}
            value={category.place}
            onChange={handleCategory}
          />
          <Label htmlFor={category.id}>{category.place}</Label>
        </Category>
      ))}
    </Wrapper>
  );
}

const Wrapper = styled.div`
  width: 95%;
  height: fit-content;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  flex-direction: row;

  input {
    margin: 0px;
  }
`;

const Category = styled.div`
  font-size: 0.85rem;
  display: flex;
  align-items: center;
  word-break: keep-all;
  margin-bottom: 3px;
`;

const Label = styled.label`
  color: #2b2b2b !important;
  margin-left: 4px;
  font-weight: 500;
`;

export default RegisterCategory;
