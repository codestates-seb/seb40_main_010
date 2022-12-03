import React from 'react';
import styled from 'styled-components';

function CategoryButton({
  categoryId,
  icon,
  name,
  index,
  onClickCategoryButton,
  focusCategoryID,
}) {
  return (
    <StyledCategoryButton
      type="button"
      key={categoryId}
      onClick={() => onClickCategoryButton(index)}
      className={focusCategoryID === categoryId ? 'focus' : null}
    >
      <i className={icon} />
      {name}
    </StyledCategoryButton>
  );
}

export default CategoryButton;

const StyledCategoryButton = styled.button`
  font-family: inherit;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
  background-color: inherit;
  color: #aaaaaa;
  border: none;
  padding: 5px;
  font-weight: 500;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 5px;
  word-break: keep-all;

  > i {
    font-size: 1.2rem;
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
