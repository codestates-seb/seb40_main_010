import React, { useEffect, useRef } from 'react';
import styled from 'styled-components';
import Select from 'react-select';
import { BiTimeFive, BiPencil } from 'react-icons/bi';
import { CgMenuRound } from 'react-icons/cg';
import { IoHeartCircleOutline } from 'react-icons/io5';
import { AiOutlineDollarCircle } from 'react-icons/ai';
import MyPageCategoryList from './MyPageCategoryList';
import useMyPage from '../../hooks/useMyPage';
import mbtiList from '../../utils/mbtiList';

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
    userDataEdit,
    userMBTI,
    userNickName,
    onChange,
    onClickCancel,
    onClickCategory,
    onChangeNickName,
    handleUploadImage,
    previewProfileImage,
    nickNameCheck,
    nickNameValidationMessage,
    checkNickNameValidation,
    userImageEdit,
    changeProfileImage,
    setChangeProfileImage,
  } = useMyPage();

  const hiddenFileInput = useRef(null);

  const handleImageSelect = () => {
    hiddenFileInput.current.click();
    setChangeProfileImage(true);
  };

  useEffect(() => {
    callUserData();
    callRegistrationList();
  }, []);

  const myPageCategories = [
    {
      title: '등록내역',
      value: 'register',
      icon: <AiOutlineDollarCircle size="35" />,
    },
    {
      title: '예약내역',
      value: 'reservation',
      icon: <BiTimeFive size="35" />,
    },
    {
      title: '관심장소',
      value: 'bookmark',
      icon: <IoHeartCircleOutline size="35" />,
    },
    {
      title: '리뷰내역',
      value: 'review',
      icon: <CgMenuRound size="35" />,
    },
  ];

  return (
    <MyPageComponentContainer>
      <MyProfileImage src={previewProfileImage} onClick={handleImageSelect} />
      {changeProfileImage && (
        <EditProfile onClick={userImageEdit}>적용하기</EditProfile>
      )}
      <NameAndEditIconContainer>
        {!editStatus && <MyNickName>{nickname}</MyNickName>}
        {editStatus && (
          <UserNickNameChange
            onChange={event => {
              onChangeNickName(event);
              checkNickNameValidation(event.target.value);
            }}
            placeholder={userNickName}
          />
        )}
        {!editStatus && <BiPencil onClick={editStatusChange} size="24" />}
        <input
          type="file"
          accept="image/*"
          style={{ display: 'none' }}
          ref={hiddenFileInput}
          onChange={handleUploadImage}
        />
        {editStatus && nickNameCheck ? (
          <EditText onClick={userDataEdit}>수정하기</EditText>
        ) : (
          editStatus && <EditTextBlock>수정하기</EditTextBlock>
        )}
        {editStatus && <EditText onClick={onClickCancel}>취소</EditText>}
      </NameAndEditIconContainer>
      {!nickNameCheck && (
        <ValidationErrorMessage>
          {nickNameValidationMessage}
        </ValidationErrorMessage>
      )}
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
        {myPageCategories.map(catogoryItem => (
          <MyPageCategoryItem
            onClick={onClickCategory}
            value={catogoryItem.value}
            clicked={myPageCategory}
            key={catogoryItem.value}
          >
            {catogoryItem.icon}
            {catogoryItem.title}
          </MyPageCategoryItem>
        ))}
      </MyPageContentCategory>
      <MyPageCategoryItemTitle>{myPageCategory}</MyPageCategoryItemTitle>
      {listData.length > 0 && <BolderLine />}
      <MyPageCategoryItemList>
        {myPageCategory === '등록내역' &&
          listData.map(el => {
            return (
              <MyPageCategoryList
                listData={el}
                key={el.placeId}
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
  box-shadow: rgba(0, 0, 0, 0.1) 0px 5px 15px;
  background-color: #ffffff;
  padding: 35px;
  box-sizing: border-box;
  margin: 0 auto;
  margin-top: 140px;
  margin-bottom: 60px;

  @media (max-width: 840px) {
    width: 480px;
  }
`;

const MyProfileImage = styled.img`
  width: 64px;
  height: 64px;
  overflow: hidden;
  border-radius: 35px;
  margin-bottom: 8px;
  border: 2px solid rgba(255, 255, 255, 0);

  :hover {
    cursor: pointer;
    border: 2px solid #ffda77;
  }
`;

const NameAndEditIconContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: row;
  margin-bottom: 6px;

  & svg {
    color: #ffda77;
    margin-left: 8px;
    cursor: pointer;
  }
`;

const EditText = styled.p`
  color: #eb7470;
  margin: 0px 4px;
  cursor: pointer;
`;

const EditProfile = styled.p`
  color: #ffda77;
  cursor: pointer;
`;

const EditTextBlock = styled.p`
  color: #666666;
  margin: 0px 4px;
`;

const MyNickName = styled.div`
  font-size: 1.3rem;
  font-weight: 700;
  margin-left: 30px;
`;

const MyMBTI = styled.div`
  font-size: 1rem;
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
  font-size: 1rem;
  font-weight: 500;
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
    color: #ffda77;
    & svg {
      color: #ffda77;
    }
  }

  :hover {
    color: #ffda77;
    & svg {
      color: #ffda77;
    }
  }
`;

const MyPageCategoryItemTitle = styled.div`
  width: 680px;
  font-size: 1rem;
  font-weight: 500;

  @media (max-width: 840px) {
    width: 20rem;
  }
`;

const BolderLine = styled.div`
  margin-top: 0.5rem;
  width: 680px;
  border-bottom: 1px solid #aaa;

  @media (max-width: 840px) {
    width: 20rem;
  }
`;

const MyPageCategoryItemList = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  width: 750px;
  height: 25rem;
  overflow: auto;

  @media (max-width: 840px) {
    width: 323px;
  }

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
    border: 1px solid #ffce31;
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
  width: 106px;
  border: 1px solid #ffce31;
  border-radius: 5px;
  cursor: pointer;
  color: #2b2b2b;
  margin-left: 108px;
`;

const ValidationErrorMessage = styled.p`
  font-size: 14px;
  color: red;
`;

export default MyPageComponent;
