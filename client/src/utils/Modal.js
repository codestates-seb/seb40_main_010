import React from 'react';
import styled from 'styled-components';

function Modal({
  modalText,
  modalActionText,
  modalAction,
  modalOpen,
  setModalOpen,
}) {
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
  height: 500%;
`;

const ModalContainer = styled.div`
  box-sizing: border-box;
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-direction: column;
  width: 25rem;
  height: 15rem;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  border-radius: 1.25rem;
  padding: 1.25rem;
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
  font-size: 1.75rem;
  font-weight: bold;
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: space-around;
  align-items: center;
  width: 100%;
`;

const ModalButton = styled.button`
  background-color: #e0e0e0;
  color: #303030;
  width: 8.75rem;
  height: 3rem;
  border: none;
  font-size: 1.3rem;
  border-radius: 2rem;
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
