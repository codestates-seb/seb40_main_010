import React from 'react';
import { useRecoilState } from 'recoil';
import styled from 'styled-components';
import axios from 'axios';
import ReactDaumPost from 'react-daumpost-hook';
import { FaCaretRight, FaCaretLeft } from 'react-icons/fa';

import {
  registerFormTitle,
  registerFormAddress,
  registerFormMaxCapacity,
  registerFormDetailedAddress,
  registerFormDetailedInformation,
  registerFormCharge,
  registerFormItemsCheckedState,
  registerFormImage,
  reservationEditData,
} from '../atoms';

import Nav from '../components/Nav';
import RegisterImages from '../components/RegisterComponents/RegisterImages';
import RegisterCategory from '../components/RegisterComponents/RegisterCategory';

export default function Register() {
  const [title, setTitle] = useRecoilState(registerFormTitle);
  const [maxCapacity, setMaxCapacity] = useRecoilState(registerFormMaxCapacity);
  const [address, setAddress] = useRecoilState(registerFormAddress);
  const [detailedAddress, setDetailedAddress] = useRecoilState(
    registerFormDetailedAddress,
  );
  const [detailedInformation, setDetailedInformation] = useRecoilState(
    registerFormDetailedInformation,
  );
  const [charge, setCharge] = useRecoilState(registerFormCharge);
  const [checkedList, setCheckedList] = useRecoilState(
    registerFormItemsCheckedState,
  );
  const [images, setImages] = useRecoilState(registerFormImage);

  const [editData, setEditData] = useRecoilState(reservationEditData);

  const handler = {
    title: setTitle,
    maxCapacity: setMaxCapacity,
    detailedAddress: setDetailedAddress,
    detailedInformation: setDetailedInformation,
    charge: setCharge,
  };

  const handleChange = event => {
    const { name, value } = event.target;
    handler[name](value);
  };

  const editHandleChange = event => {
    if (event.target.id === 'capacityMinus' && editData.capacity > 1) {
      return setEditData({ ...editData, capacity: editData.capacity - 1 });
    }
    if (event.target.id === 'capacityPlus') {
      return setEditData({ ...editData, capacity: editData.capacity + 1 });
    }
    const { name, value } = event.target;
    return setEditData({ ...editData, [name]: value });
  };

  const plusCapacity = () => {
    setMaxCapacity(maxCapacity + 1);
  };

  const minusCapacity = () => {
    if (maxCapacity > 1) setMaxCapacity(maxCapacity - 1);
  };

  const postConfig = {
    onComplete: data => {
      setAddress(data.address);
    },
  };

  const postConfigEdit = {
    onComplete: data => {
      setEditData({ ...editData, address: data.address });
    },
  };

  const postCodeEdit = ReactDaumPost(postConfigEdit);

  const postCode = ReactDaumPost(postConfig);

  const handleSubmit = () => {
    const formData = new FormData();
    formData.append('title', title);
    formData.append('category', checkedList);
    formData.append('maxCapacity', maxCapacity);
    formData.append('address', address);
    formData.append('detailedAddress', detailedAddress);
    formData.append('detailInfo', detailedInformation);
    formData.append('charge', charge);
    images.forEach(file => {
      formData.append('image', file, file.name);
    });

    axios({
      method: 'post',
      url: `http://localhost:3001/place`,
      data: formData,
      headers: { 'Content-Type': 'multipart/form-data' },
    });
  };

  return (
    <>
      <Nav />
      <Container>
        <FormContainer>
          <Wrapper>
            <Title>제목</Title>
            {editData ? (
              <Input
                onChange={editHandleChange}
                value={editData.title}
                name="title"
              />
            ) : (
              <Input onChange={handleChange} name="title" />
            )}
          </Wrapper>
          <Wrapper>
            <Title>카테고리</Title>
            <RegisterCategory
              checkedList={checkedList}
              setCheckedList={setCheckedList}
            />
          </Wrapper>
          <Wrapper>
            <Title>최대 인원</Title>
            <div className="capacity-wrapper">
              {editData ? (
                <LeftIcon onClick={editHandleChange} id="capacityMinus" />
              ) : (
                <LeftIcon onClick={minusCapacity} />
              )}
              {editData ? (
                <SmallInput
                  width="20px"
                  type="number"
                  name="capacity"
                  onChange={editHandleChange}
                  value={editData.capacity}
                  readOnly
                />
              ) : (
                <SmallInput
                  width="20px"
                  type="number"
                  onChange={handleChange}
                  name="maxCapacity"
                  value={maxCapacity}
                  readOnly
                />
              )}
              {editData ? (
                <RightIcon onClick={editHandleChange} id="capacityPlus" />
              ) : (
                <RightIcon onClick={plusCapacity} />
              )}
            </div>
          </Wrapper>
          <Wrapper>
            <Title>주소</Title>
            {editData ? (
              <Input
                type="text"
                onClick={() => postCodeEdit()}
                value={editData.address}
                onChange={editHandleChange}
                readOnly
                name="address"
              />
            ) : (
              <Input
                type="text"
                onClick={() => postCode()}
                value={address}
                readOnly
              />
            )}
            <Title marginTop="20px">상세주소</Title>
            <Input type="text" onChange={handleChange} name="detailedAddress" />
          </Wrapper>
          <Wrapper>
            <Title>상세정보</Title>
            {editData ? (
              <Textarea
                type="text"
                onChange={editHandleChange}
                value={editData.detailInfo}
                name="detailInfo"
              />
            ) : (
              <Textarea
                type="text"
                onChange={handleChange}
                name="detailedInformation"
              />
            )}
          </Wrapper>
          <Wrapper>
            <Title>사진</Title>
            <RegisterImages images={images} setImages={setImages} />
          </Wrapper>
          <Wrapper>
            <Title>금액 설정</Title>
            <div className="set-charge">
              <div className="hour-description">1시간 / </div>
              {editData ? (
                <SmallInput
                  type="number"
                  width="100px"
                  onChange={editHandleChange}
                  value={editData.charge}
                  name="charge"
                />
              ) : (
                <SmallInput
                  type="number"
                  width="100px"
                  onChange={handleChange}
                  name="charge"
                />
              )}
              <div className="hour-description">원</div>
            </div>
          </Wrapper>
          <ButtonWrapper>
            <button
              type="submit"
              className="form-register-button"
              onClick={handleSubmit}
            >
              등록하기
            </button>
          </ButtonWrapper>
        </FormContainer>
      </Container>
    </>
  );
}

const Container = styled.div`
  margin-top: 70px;
  width: 100vw;
  height: fit-content;
  background-color: #e5f0ff;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 50px 0px;

  .form-register-button {
    width: 300px;
    height: 55px;
    margin-top: 25px;
    background-color: #ffda77;
    border-radius: 40px;
    color: #2b2b2b;
    font-size: 1.5rem;
    font-weight: 600;
    border: none;
    box-shadow: rgba(0, 0, 0, 0.35) 3px 3px 3px;

    :active {
      box-shadow: none;
    }

    :disabled {
      cursor: not-allowed;
      opacity: 0.7;
      box-shadow: none;
    }
  }

  .set-charge {
    width: 95%;
    display: flex;
    align-items: center;
  }

  .hour-description {
    width: fit-content;
    font-size: 1.1rem;
    color: #2b2b2b;
  }

  .capacity-wrapper {
    width: 97%;
    display: flex;
    align-items: center;
  }
`;

const Wrapper = styled.div`
  height: fit-content;
  margin-bottom: 25px;
  background-color: #ffffff;
  border-radius: 10px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 25px 10px;
  box-shadow: rgba(0, 0, 0, 0.3) 0px 5px 12px;
`;

const FormContainer = styled.div`
  width: 47rem;
  height: fit-content;
  padding: 0px 2.5em;
`;

const Title = styled.div`
  width: 95%;
  height: fit-content;
  font-size: 1.05rem;
  font-weight: 600;
  color: #2b2b2b;
  margin-bottom: 15px;
  margin-top: ${({ marginTop }) => marginTop};
`;

const ButtonWrapper = styled.div`
  height: fit-content;
  display: flex;
  align-items: center;
  justify-content: center;
`;

const Input = styled.input`
  width: 94%;
  height: 1.1rem;
  font-size: 0.8rem;
  outline: none;
  border: 3px solid #96c2ff;
  border-radius: 5px;
  color: #2b2b2b;
  padding: 5px;
`;

const SmallInput = styled.input`
  width: ${({ width }) => width};
  height: 1.1rem;
  font-size: 0.8rem;
  outline: none;
  border: 3px solid #96c2ff;
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

const Textarea = styled.textarea`
  width: 94%;
  height: 200px;
  font-size: 1rem;
  outline: none;
  border: 3px solid #96c2ff;
  border-radius: 5px;
  color: #2b2b2b;
  padding: 5px;
  resize: none;
`;

const LeftIcon = styled(FaCaretLeft)`
  font-size: 25px;
  color: #eb7470;

  path {
    z-index: -100;
    pointer-events: none;
  }

  :hover {
    cursor: pointer;
  }
`;

const RightIcon = styled(FaCaretRight)`
  font-size: 25px;
  color: #eb7470;

  path {
    z-index: -100;
    pointer-events: none;
  }

  :hover {
    cursor: pointer;
  }
`;
