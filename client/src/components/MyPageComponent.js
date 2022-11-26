import React, { useEffect } from 'react';
import styled from 'styled-components';
import Select from 'react-select';
import { BiTimeFive, BiPencil } from 'react-icons/bi';
import { CgMenuRound } from 'react-icons/cg';
import { IoHeartCircleOutline } from 'react-icons/io5';
import { AiOutlineDollarCircle } from 'react-icons/ai';
import MyPageCategoryList from './MyPageCategoryList';
import useMyPage from './useMyPage';

function MyPageComponent() {
  const {
    callRegistrationList,
    callUserData,
    editStatus,
    editStatusChange,
    listData,
    mbti,
    myPageCategory,
    nickname,
    profileImage,
    userDataEdit,
    userMBTI,
    userNickName,
    onChange,
    onClickCancel,
    onClickCategory,
    onChangeNickName,
  } = useMyPage();

  useEffect(() => {
    callUserData();
    callRegistrationList();
  }, []);

  const mbtiList = [
    { value: 'null', label: '없음' },
    { value: 'ISTJ', label: 'ISTJ' },
    { value: 'ISFJ', label: 'ISFJ' },
    { value: 'INFJ', label: 'INFJ' },
    { value: 'INTJ', label: 'INTJ' },
    { value: 'ISTP', label: 'ISTP' },
    { value: 'ISFP', label: 'ISFP' },
    { value: 'INFP', label: 'INFP' },
    { value: 'INTP', label: 'INTP' },
    { value: 'ESTJ', label: 'ESTJ' },
    { value: 'ESFJ', label: 'ESFJ' },
    { value: 'ENFJ', label: 'ENFJ' },
    { value: 'ENTJ', label: 'ENTJ' },
    { value: 'ESTP', label: 'ESTP' },
    { value: 'ESFP', label: 'ESFP' },
    { value: 'ENFP', label: 'ENFP' },
    { value: 'ENTP', label: 'ENTP' },
  ];

  return (
    <MyPageComponentContainer>
      <MyProfileImage src={profileImage} />
      <NameAndEditIconContainer>
        {!editStatus && <MyNickName>{nickname}</MyNickName>}
        {editStatus && (
          <UserNickNameChange
            onChange={onChangeNickName}
            placeholder={userNickName}
          />
        )}
        {!editStatus && <BiPencil onClick={editStatusChange} size="24" />}
        {editStatus && <EditText onClick={userDataEdit}>수정하기</EditText>}
        {editStatus && <EditText onClick={onClickCancel}>취소</EditText>}
      </NameAndEditIconContainer>
      {!editStatus && <MyMBTI>{mbti}</MyMBTI>}
      {editStatus && (
        <MbtiSelect
          classNamePrefix="Select"
          options={mbtiList}
          placeholder={userMBTI}
          onChange={onChange}
        />
      )}
      <MyPageContentCategory>
        <MyPageCategoryItem onClick={onClickCategory} value="register">
          <AiOutlineDollarCircle size="35" />
          등록내역
        </MyPageCategoryItem>
        <MyPageCategoryItem onClick={onClickCategory} value="reservation">
          <BiTimeFive size="35" />
          예약내역
        </MyPageCategoryItem>
        <MyPageCategoryItem onClick={onClickCategory} value="bookmark">
          <IoHeartCircleOutline size="35" />
          관심장소
        </MyPageCategoryItem>
        <MyPageCategoryItem onClick={onClickCategory} value="review">
          <CgMenuRound size="35" />
          리뷰내역
        </MyPageCategoryItem>
      </MyPageContentCategory>
      <MyPageCategoryItemTitle>{myPageCategory}</MyPageCategoryItemTitle>
      <MyPageCategoryItemList>
        {myPageCategory === '등록내역' &&
          listData.map(el => {
            return (
              <MyPageCategoryList
                listData={el}
                key={el.id}
                type="registration"
              />
            );
          })}
        {myPageCategory === '예약내역' &&
          listData.map(el => {
            return (
              <MyPageCategoryList
                listData={el}
                key={el.reserveId}
                type="reservation"
              />
            );
          })}
        {myPageCategory === '관심장소' &&
          listData.map(el => {
            return (
              <MyPageCategoryList
                listData={el}
                key={el.bookmarkId}
                type="bookmark"
              />
            );
          })}
        {myPageCategory === '리뷰내역' &&
          listData.map(el => {
            return (
              <MyPageCategoryList
                listData={el}
                key={el.reviewId}
                type="reviews"
              />
            );
          })}
      </MyPageCategoryItemList>
    </MyPageComponentContainer>
  );
}

const MyPageComponentContainer = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  border-radius: 20px;
  width: 840px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  background-color: #ffffff;
  padding: 35px;
  box-sizing: border-box;
`;

const MyProfileImage = styled.img`
  height: 64px;
  border-radius: 35px;
  margin-bottom: 8px;
`;

const NameAndEditIconContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  margin-bottom: 6px;

  & svg {
    color: #89bbff;
    margin-left: 8px;
    cursor: pointer;
  }
`;

const EditText = styled.p`
  color: #89bbff;
  margin: 0px 4px;
`;

const MyNickName = styled.div`
  font-size: 24px;
  font-weight: bold;
  margin-left: 30px;
`;

const MyMBTI = styled.div`
  font-size: 20px;
`;

const MyPageContentCategory = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
`;

const MyPageCategoryItem = styled.button`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  font-size: 18px;
  font-weight: bold;
  border-bottom: 2px solid #ffffff;
  margin: 16px 0;
  color: #2b2b2b;
  background-color: #ffffff;
  border: none;

  & svg {
    color: #9a9a9a;
    pointer-events: none;
    margin-bottom: 8px;
  }

  :focus {
    color: #89bbff;
    & svg {
      color: #89bbff;
    }
  }

  :hover {
    color: #89bbff;
    & svg {
      color: #89bbff;
    }
  }
`;

const MyPageCategoryItemTitle = styled.div`
  width: 680px;
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 12px;
`;

const MyPageCategoryItemList = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  width: 750px;
  height: 400px;
  overflow: auto;

  ::-webkit-scrollbar {
    width: 0px;
  }
  ::-webkit-scrollbar-thumb {
    background: #b9b9b9;
    border-radius: 25px;
  }
`;

const MbtiSelect = styled(Select)`
  font-size: 1rem;
  width: 7rem;

  .Select__control {
    height: 40px;
    width: 100%;
    /* border: none; */
    border: 2px solid #96c2ff;
    border-radius: 5px;
    cursor: pointer;
  }

  .Select__indicator-separator {
    display: none;
  }

  .Select__menu {
    color: #2b2b2b;
  }

  .Select__menu-list {
    ::-webkit-scrollbar {
      display: none;
    }
  }
`;

const UserNickNameChange = styled.input`
  font-size: 1rem;
  width: 6.5rem;
  border: 2px solid #96c2ff;
  border-radius: 5px;
  cursor: pointer;
  color: #2b2b2b;
  margin-left: 99px;
`;

export default MyPageComponent;
