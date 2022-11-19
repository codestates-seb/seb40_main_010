import React, { useState } from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';
import { BsFillBookmarkFill } from 'react-icons/bs';
import { Link } from 'react-router-dom';
import Modal from './Modal';
import ReviewWrite from './ReviewWrite';

const chargeComponent = (listData, type) => {
  if (type === 'reviews') {
    return null;
  }
  if (type === 'reservation') {
    return <PlaceCharge>{listData.totalCharge / 1000},000원</PlaceCharge>;
    // 시간 계산법 천 단위로 나뉘도록 바꾸기
  }
  return <PlaceCharge>{listData.charge / 1000},000원</PlaceCharge>;
};

function MyPageCategoryList({ listData, type }) {
  const [modalOpen, setModalOpen] = useState(false);
  const [reviewModalOpen, setReviewModalOpen] = useState(false);

  const showModal = () => {
    setModalOpen(!modalOpen);
  };

  const showReviewModal = () => {
    setReviewModalOpen(!reviewModalOpen);
  };

  const handleDate = createdAt => {
    if (createdAt !== undefined) {
      let date = new Date(createdAt);
      const month = date.getMonth() + 1;
      const utc = date.getTime() + date.getTimezoneOffset() * 60 * 1000;
      const KR_TIME_DIFF = 9 * 60 * 60 * 1000; // 한국 시간(KST)은 UTC시간보다 9시간 더 빠르므로 9시간을 밀리초 단위로 변환.
      const krCurr = utc + KR_TIME_DIFF;

      date = new Date(krCurr).toString();

      const splitDate = date.split(' ');
      const day = splitDate[2];
      const year = splitDate[3].slice(2);
      let time = splitDate[4].slice(0, 2);

      if (time > 12) {
        time -= 12;
        return `${year}.${month}.${day} ${time}PM`;
      }

      return `${year}.${month}.${day} ${time}AM`;
    }
    return '';
  };

  return (
    <CategoryItemList>
      <CategoryContainer>
        <CategoryBodyContainer>
          <PlaceBodyContainer>
            <PlaceTitle>{listData.title}</PlaceTitle>
            <PlaceAddress>{listData.comment}</PlaceAddress>
            <PlaceAddress>{listData.address}</PlaceAddress>
            {type === 'reservation' && (
              <ReservationDate>
                {handleDate(listData.startTime)} ~{' '}
                {handleDate(listData.endTime)}
              </ReservationDate>
            )}
            <ReservationDate>{listData.createdAt}</ReservationDate>
          </PlaceBodyContainer>
          {type === 'reservation' ? null : (
            <RatingStarContainer>
              <ImStarFull size={23} />
              <PlaceRating>{listData.score}</PlaceRating>
            </RatingStarContainer>
          )}
        </CategoryBodyContainer>
        <CategoryActionContainer>
          {type === 'registration' && (
            <Link to="/detail" style={{ textDecoration: 'none' }}>
              <CategoryButton>수정하기</CategoryButton>
            </Link>
          )}
          {type === 'reservation' && (
            <>
              <CategoryButton onClick={showModal}>취소하기</CategoryButton>
              {modalOpen && (
                <Modal
                  modalOpen={modalOpen}
                  setModalOpen={setModalOpen}
                  modalText="예악을 취소하시겠습니까?"
                  modalActionText="취소하기"
                  modalAction="취소하는 함수 만들어서 넣기"
                />
              )}
              <CategoryButton onClick={showReviewModal}>
                리뷰쓰기
              </CategoryButton>
            </>
          )}
          {type === 'bookmark' && (
            <CategoryButton>
              <BsFillBookmarkFill size={24} />
            </CategoryButton>
          )}
          {type === 'reviews' && (
            <>
              <CategoryButton>수정하기</CategoryButton>
              <CategoryButton onClick={showModal}>삭제하기</CategoryButton>
              {modalOpen && (
                <Modal
                  modalOpen={modalOpen}
                  setModalOpen={setModalOpen}
                  modalText="리뷰를 삭제하시겠습니까?"
                  modalActionText="삭제하기"
                  modalAction="삭제하는 함수 만들어서 넣기"
                />
              )}
            </>
          )}
          {chargeComponent(listData, type)}
        </CategoryActionContainer>
      </CategoryContainer>
      <PlaceImage src={listData.image} />

      {reviewModalOpen && (
        <ReviewWrite
          reviewModalOpen={reviewModalOpen}
          setReviewModalOpen={setReviewModalOpen}
          placeName={listData.title}
          placeImageURL={listData.image}
        />
      )}
    </CategoryItemList>
  );
}

const CategoryItemList = styled.div`
  box-sizing: border-box;
  padding: 24px 20px 24px 40px;
  width: 680px;
  height: 165px;
  background-color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  border-radius: 15px;
  margin: 18px 0px;
`;

const CategoryContainer = styled.div`
  height: 100%;
  width: 70%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;
`;

const CategoryBodyContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-direction: row;
  width: 100%;

  & svg {
    color: #ffce31;
    margin-right: 5px;
  }
`;

const PlaceBodyContainer = styled.div`
  width: 100%;
`;

const PlaceTitle = styled.p`
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 6px;
  color: #1b1c1e;
`;

const PlaceAddress = styled.span`
  font-size: 15px;
  color: #2b2b2b;
`;

const RatingStarContainer = styled.div`
  display: flex;
  flex-direction: row;
`;

const PlaceRating = styled.div`
  font-size: 23px;
  font-weight: bold;
`;

const CategoryActionContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  flex-direction: row;
  width: 100%;
`;

const CategoryButton = styled.div`
  font-size: 18px;
  color: #eb7470;

  :hover {
    cursor: pointer;
    font-weight: bold;
  }

  & svg :hover {
    cursor: pointer;
  }
`;

const ReservationDate = styled.div`
  margin-top: 8px;
  color: #9a9a9a;
`;

const PlaceCharge = styled.span`
  font-size: 20px;
  font-weight: bold;
  color: #eb7470;
`;

const PlaceImage = styled.img`
  width: 128px;
  border-radius: 15px;
`;

export default MyPageCategoryList;
