import React from 'react';
import styled from 'styled-components';

function Modal({
  modalText,
  modalActionText,
  modalAction,
  modalOpen,
  setModalOpen,
}) {
  // 각 페이지에서 모달창에 필요한 텍스트를 props로 전달

  // 각 페이지에서 모달창에서 필요한 기능을 props로 전달

  const showModal = () => {
    setModalOpen(!modalOpen);
  };

  return (
    <BlurBackground>
      <ModalContainer>
        <ModalText>{modalText}</ModalText>
        <ButtonContainer>
          <ModalButton
            className="cancel"
            onClick={() => {
              modalAction();
            }}
          >
            {modalActionText}
          </ModalButton>
          <ModalButton onClick={showModal}>돌아가기</ModalButton>
        </ButtonContainer>
      </ModalContainer>
    </BlurBackground>
  );
}

const BlurBackground = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.8);
  z-index: 100;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
`;

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
  background-color: #ffffff;

  z-index: 999;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
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
  &.cancel {
    background-color: #eb7470;
    color: #ffffff;
  }

  :hover {
    cursor: pointer;
  }
`;

export default Modal;
