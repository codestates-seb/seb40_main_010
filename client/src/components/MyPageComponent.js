import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { BiTimeFive } from 'react-icons/bi';
import { CgMenuRound } from 'react-icons/cg';
import { IoHeartCircleOutline } from 'react-icons/io5';
import { AiOutlineDollarCircle } from 'react-icons/ai';
import axios from 'axios';
import MyPageCategoryList from './MyPageCategoryList';

// 할 것
// 마이페이지 페이지에서 데이터 보내고 응답 받아오는 것 프롭스로 내려주기
// 마이페이지 컴포넌트별 프롭스 받은 것들 렌더링 완성하기
// 닉네임 수정하기 제작
// 각종 모달창들 모달 프롭스 등 연결하기
// 프롭스로 함수 넘겨주기
// 프로필 사진 변경 모달창 제작하기
// 등록내역에 쓰일 컴포넌트 제작해서 추가하기 거기에 프롭스로 넘겨주기

function MyPageComponent() {
  const [myPageCategory, setMyPageCategory] = useState('등록내역');
  const [memberData, setMemberData] = useState([]);
  const [listData, setListData] = useState([]);

  const changeCategory = e => {
    setMyPageCategory(e.target.textContent);
  };

  const callRegistrationList = async () => {
    // const data = {
    //   headers: {
    //     // refresh 토큰
    //     // access 토큰
    //   },
    // };
    await axios.get(`http://localhost:3001/place`).then(res => {
      setListData([...res.data]);
    });
  };

  const reservationList = async () => {
    // const data = {
    //   headers: {
    //     // refresh 토큰
    //     // access 토큰
    //   },
    // };
    await axios.get(`http://localhost:3001/reserve`).then(res => {
      setListData([...res.data]);
    });
  };

  const bookmarkList = async () => {
    // const data = {
    //   headers: {
    //     // refresh 토큰
    //     // access 토큰
    //   },
    // };
    await axios.get(`http://localhost:3001/bookmark`).then(res => {
      setListData([...res.data]);
    });
  };

  const reviewList = async () => {
    // const data = {
    //   headers: {
    //     // refresh 토큰
    //     // access 토큰
    //   },
    // };
    await axios.get(`http://localhost:3001/review`).then(res => {
      setListData([...res.data]);
    });
  };

  const callUserData = async () => {
    await axios
      .get('http://localhost:3001/member')
      .then(res => setMemberData(...res.data));
  };

  useEffect(() => {
    callUserData();
    callRegistrationList();
  }, []);

  return (
    <MyPageComponentContainer>
      <MyProfileImage src={memberData.profileImage} />
      <MyNickName>{memberData.nickname}</MyNickName>
      <MyMBTI>{memberData.mbti}</MyMBTI>
      <MyPageContentCategory>
        <MyPageCategoryItem
          onClick={e => {
            changeCategory(e);
            callRegistrationList();
          }}
        >
          <AiOutlineDollarCircle size="35" />
          등록내역
        </MyPageCategoryItem>
        <MyPageCategoryItem
          onClick={e => {
            changeCategory(e);
            reservationList();
          }}
        >
          <BiTimeFive size="35" />
          예약내역
        </MyPageCategoryItem>
        <MyPageCategoryItem
          onClick={e => {
            changeCategory(e);
            bookmarkList();
          }}
        >
          <IoHeartCircleOutline size="35" />
          관심장소
        </MyPageCategoryItem>
        <MyPageCategoryItem
          onClick={e => {
            changeCategory(e);
            reviewList();
          }}
        >
          <CgMenuRound size="35" />
          리뷰내역
        </MyPageCategoryItem>
      </MyPageContentCategory>
      <MyPageCategoryItemTitle>{myPageCategory}</MyPageCategoryItemTitle>
      <MyPageCategoryItemList>
        {myPageCategory === '등록내역' &&
          listData.map(el => {
            return <MyPageCategoryList listData={el} type="registration" />;
          })}
        {myPageCategory === '예약내역' &&
          listData.map(el => {
            return <MyPageCategoryList listData={el} type="reservation" />;
          })}
        {myPageCategory === '관심장소' &&
          listData.map(el => {
            return <MyPageCategoryList listData={el} type="bookmark" />;
          })}
        {myPageCategory === '리뷰내역' &&
          listData.map(el => {
            return <MyPageCategoryList listData={el} type="reviews" />;
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

const MyNickName = styled.div`
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 6px;
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

export default MyPageComponent;
