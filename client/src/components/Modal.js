import React from 'react';
import styled from 'styled-components';

function Modal() {
  return (
    <ModalContainer>
      <ModalText>예약을 취소하시겠습니까?</ModalText>
      <ButtonContainer>
        <ModalButton className="cancle">예약 취소</ModalButton>
        <ModalButton>돌아가기</ModalButton>
      </ButtonContainer>
    </ModalContainer>
  );
}

const ModalContainer = styled.div`
  box-sizing: border-box;
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-direction: column;
  width: 430px;
  height: 240px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  border-radius: 20px;
  padding: 20px;
`;

const ModalText = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  color: #1b1c1e;
  font-size: 28px;
  font-weight: bold;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100%;
`;

const ModalButton = styled.button`
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  background-color: #e0e0e0;
  color: #303030;
  width: 140px;
  height: 45px;
  border: none;
  font-size: 25px;
  border-radius: 30px;
  font-weight: bold;
  &.cancle {
    background-color: #eb7470;
    color: #ffffff;
  }
`;

export default Modal;
