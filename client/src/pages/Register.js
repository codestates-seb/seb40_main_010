import React from 'react';
import { useNavigate } from 'react-router-dom';
import { useRecoilState, useSetRecoilState } from 'recoil';
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
  registerFormPreviewImage,
  reservationEditData,
} from '../atoms';

import Nav from '../components/Navigation/Nav';
import RegisterImages from '../components/RegisterComponents/RegisterImages';
import RegisterCategory from '../components/RegisterComponents/RegisterCategory';

export default function Register() {
  // const ACCESS = localStorage.getItem('ACCESS');
  const navigator = useNavigate();
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
  const setPreviewImages = useSetRecoilState(registerFormPreviewImage);

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
    if (event.target.id === 'capacityMinus' && editData.maxCapacity > 1) {
      return setEditData({
        ...editData,
        maxCapacity: editData.maxCapacity - 1,
      });
    }
    if (event.target.id === 'capacityPlus') {
      return setEditData({
        ...editData,
        maxCapacity: editData.maxCapacity + 1,
      });
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

  const handleSubmit = async () => {
    const json = JSON.stringify({
      title,
      maxCapacity,
      categoryList: checkedList,
      address,
      detailedAddress,
      detailInfo: detailedInformation,
      charge,
    });
    const blob = new Blob([json], { type: 'application/json' });

    const formData = new FormData();
    formData.append('key', blob);
    images.forEach(file => {
      formData.append('file', file, file.name);
    });

    try {
      await axios.post(`/place/post`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
          RefreshToken: localStorage.getItem('REFRESH'),
        },
      });
      setCheckedList([]);
      setAddress('');
      setImages([]);
      setPreviewImages([]);
      navigator('/');
    } catch (err) {
      console.log('Error >>', err);
    }
  };

  const handleEditSubmit = async () => {
    const json = JSON.stringify({
      title: editData.title,
      maxCapacity: editData.maxCapacity,
      categoryList: checkedList,
      address: editData.address,
      detailedAddress,
      detailInfo: editData.detailInfo,
      charge: editData.charge,
    });
    const blob = new Blob([json], { type: 'application/json' });

    const formData = new FormData();
    formData.append('key', blob);
    images.forEach(file => {
      formData.append('file', file, file.name);
    });

    try {
      await axios.patch(`/place/${editData.placeId}/edit`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
          RefreshToken: localStorage.getItem('REFRESH'),
        },
      });
      setCheckedList([]);
      setAddress('');
      setImages([]);
      setPreviewImages([]);
      navigator(`/detail/${editData.placeId}`);
    } catch (err) {
      console.log('Error >>', err);
    }
  };

  console.log(editData);
  console.log(images);
  return (
    <>
      <Nav />
      <Container>
        <FormContainer>
          <Wrapper>
            <Title>제목</Title>
            {editData ? (
              <>
                <Input
                  onChange={editHandleChange}
                  value={editData.title}
                  name="title"
                />
                {editData.title.length > 20 && (
                  <Validation>제목을 20글자 이내로 작성해주세요</Validation>
                )}
              </>
            ) : (
              <>
                <Input onChange={handleChange} name="title" />
                {title.length > 20 && (
                  <Validation>제목을 20글자 이내로 작성해주세요</Validation>
                )}
                {title.trim().length < 1 && (
                  <Validation>제목을 작성해주세요</Validation>
                )}
              </>
            )}
          </Wrapper>
          <Wrapper>
            <Title>카테고리</Title>
            <RegisterCategory
              checkedList={checkedList}
              setCheckedList={setCheckedList}
            />
            {checkedList.length < 1 && (
              <Validation>1개 이상 선택해주세요</Validation>
            )}
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
                  value={editData.maxCapacity}
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
              <>
                <Input
                  type="text"
                  onClick={() => postCodeEdit()}
                  value={editData.address}
                  onChange={editHandleChange}
                  readOnly
                  name="address"
                />
                {!editData.address && (
                  <Validation>주소를 입력해주세요</Validation>
                )}
              </>
            ) : (
              <>
                <Input
                  type="text"
                  onClick={() => postCode()}
                  value={address}
                  readOnly
                />
                {!address && <Validation>주소를 입력해주세요</Validation>}
              </>
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
            {images.length < 1 && (
              <Validation>이미지를 업로드해주세요</Validation>
            )}
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
            {charge < 1 && !editData && (
              <Validation>금액을 설정해주세요</Validation>
            )}
          </Wrapper>
          <ButtonWrapper>
            {editData ? (
              <button
                type="submit"
                className="form-register-button"
                onClick={handleEditSubmit}
                disabled={
                  !(
                    editData.title.length < 20 &&
                    checkedList.length > 0 &&
                    editData.address &&
                    images.length > 0 &&
                    editData.charge > 0
                  )
                }
              >
                수정하기
              </button>
            ) : (
              <button
                type="submit"
                className="form-register-button"
                onClick={handleSubmit}
                disabled={
                  !(
                    title.length < 20 &&
                    checkedList.length > 0 &&
                    address &&
                    images.length > 0 &&
                    charge > 0
                  )
                }
              >
                등록하기
              </button>
            )}
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
      box-shadow: rgba(0, 0, 0, 0.35) 3px 3px 3px;
    }
  }

  .set-charge {
    width: 95%;
    display: flex;
    align-items: center;
  }

  .hour-description {
    width: fit-content;
    font-size: 0.9rem;
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

const Validation = styled.div`
  width: 95%;
  font-size: 0.8rem;
  font-weight: 500;
  color: #eb7470;
  margin-top: 6px;
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
  font-size: 0.8rem;
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
