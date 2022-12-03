import React from 'react';
import styled from 'styled-components';
import { FaCaretRight, FaCaretLeft } from 'react-icons/fa';
import { useRecoilValue } from 'recoil';
import { placeMaxCapacity } from '../../atoms';

function ReservationCapacityHandler({ capacity, setCapacity }) {
  const maxCapacity = useRecoilValue(placeMaxCapacity);
  const handleCapacity = event => {
    setCapacity(event.target.value);
  };

  const plusCapacity = event => {
    event.stopPropagation();
    if (capacity >= maxCapacity) return;
    setCapacity(capacity + 1);
  };

  const minusCapacity = event => {
    event.stopPropagation();
    if (capacity > 1) setCapacity(capacity - 1);
  };

  return (
    <Container>
      <LeftIcon onClick={minusCapacity} />
      <SmallInput
        type="number"
        onChange={handleCapacity}
        value={capacity}
        readOnly
      />
      <RightIcon onClick={plusCapacity} />
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  align-items: center;
`;

const LeftIcon = styled(FaCaretLeft)`
  font-size: 25px;
  color: #eb7470;
  width: 10px;
  margin-right: 3px;

  :hover {
    cursor: pointer;
  }
`;

const RightIcon = styled(FaCaretRight)`
  font-size: 25px;
  color: #eb7470;
  width: 10px;
  padding-left: 3px;

  :hover {
    cursor: pointer;
  }
`;

const SmallInput = styled.input`
  width: 1rem;
  height: 1rem;
  font: inherit;
  font-size: 0.75rem;
  outline: none;
  border: 2px solid #aaa;
  border-radius: 5px;
  color: #2b2b2b;
  padding: 5px 5px 3px 5px;
  margin: 0px 5px;
  text-align: center;

  ::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  ::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
`;

export default ReservationCapacityHandler;
