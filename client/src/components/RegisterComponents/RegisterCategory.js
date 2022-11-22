import React from 'react';
import styled from 'styled-components';

const categories = [
  { id: 0, place: '공유오피스' },
  { id: 1, place: '캠핑' },
  { id: 2, place: '바다근처' },
  { id: 3, place: '짐보관' },
  { id: 4, place: '파티룸' },
  { id: 5, place: '게스트하우스' },
  { id: 6, place: '호텔' },
  { id: 7, place: '스터디룸' },
  { id: 8, place: '계곡근처' },
  { id: 9, place: '공연장' },
];

function RegisterCategory({ checkedList, setCheckedList }) {
  const handleCategory = event => {
    const isCategoryChecked = event.target.checked;
    const category = event.target.value;
    if (isCategoryChecked) {
      setCheckedList([...checkedList, category]);
    } else if (!isCategoryChecked) {
      setCheckedList(checkedList.filter(item => item !== category));
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
  color: #2b2b2b;
  margin-left: 4px;
`;

export default RegisterCategory;
