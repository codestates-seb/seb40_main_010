import React, { useState } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import { useRecoilState } from 'recoil';
import { ImStarFull } from 'react-icons/im';
import { BsFillBookmarkFill } from 'react-icons/bs';
import { useNavigate } from 'react-router-dom';
import Modal from './Modal';
import ReviewWrite from './ReviewWrite';
import { reservationEditData } from '../atoms';
import useMyPage from './useMyPage';
import header from '../utils/header';
import { onClickPaymentButton } from '../utils/payment';

const chargeComponent = (listData, type) => {
  if (type === 'reviews') {
    return null;
  }

  if (type === 'reservation') {
    return (
      <PlaceCharge>
        {new Intl.NumberFormat('ko-KR').format(listData.totalCharge)}원
      </PlaceCharge>
    );
  }
  return (
    <PlaceCharge>
      {new Intl.NumberFormat('ko-KR').format(listData.charge)}원
    </PlaceCharge>
  );
};

function MyPageCategoryList({ listData, type }) {
  const [modalOpen, setModalOpen] = useState(false);
  const [payModalOpen, setPayModalOpen] = useState(false);
  const [reviewModalOpen, setReviewModalOpen] = useState(false);
  const [, setReservationData] = useRecoilState(reservationEditData);
  const { bookmarkList } = useMyPage();

  const navigate = useNavigate();

  const onClickPaymentKaKaoButton = async () => {
    const { reserveId } = listData;
    const response = await axios.get(
      `/place/reserve/${reserveId}/payment`,
      header,
    );
    const paymentUrl = response.data.data;
    onClickPaymentButton(paymentUrl);
    setModalOpen(false);
  };

  const IsPayment = {
    modalText: '결제하시겠습니까?',
    modalActionText: '결제하기',
    modalAction: onClickPaymentKaKaoButton,
    modalOpen: payModalOpen,
    setModalOpen: setPayModalOpen,
  };

  const onClickPayment = () => {
    setPayModalOpen(true);
  };

  const showModal = () => {
    setModalOpen(!modalOpen);
  };

  const showReviewModal = () => {
    setReviewModalOpen(!reviewModalOpen);
  };

  const handleDate = createdAt => {
    if (createdAt === undefined) return null;

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
  };

  const reviewDelete = async () => {
    try {
      await axios.delete(`/review/${listData.reviewId}`, header);
      showModal();
    } catch (err) {
      showModal();
    }
  };

  const bookMarkStatusChange = async () => {
    try {
      await axios.get(`/bookmark/${listData.placeId}`, header);
      // 삭제 완료시 새로고침이나 상태 즉시 변경
      console.log('삭제 성공');
      bookmarkList();
    } catch (err) {
      console.log(err);
    }
  };

  const registerEditDataSend = async () => {
    try {
      const response = await axios.get(`/place/${listData.placeId}`, header);
      response.data.filePath = [];
      response.data.category = [];
      setReservationData(response.data);
      console.log(response.data);
      navigate('/register');
    } catch (err) {
      console.log(err);
    }
  };

  const moveDetailPage = async () => {
    try {
      navigate(`/detail/${listData.placeId}`);
    } catch (err) {
      console.log(err);
    }
  };

  const reservationCancel = async () => {
    try {
      await axios.delete(`/reserve/${listData.reserveId}`, header);
      showModal();
    } catch (err) {
      console.log(err);
    }
  };

  const today = new Date();

  return (
    <CategoryItemList>
      <CategoryContainer>
        <CategoryBodyContainer>
          <PlaceBodyContainer>
            <PlaceTitle onClick={moveDetailPage}>{listData.title}</PlaceTitle>
            <PlaceAddress>{listData.comment}</PlaceAddress>
            <PlaceAddress>{listData.address}</PlaceAddress>
            {type === 'reservation' && (
              <ReservationDate>
                {handleDate(listData.startTime)} ~{' '}
                {handleDate(listData.endTime)}
              </ReservationDate>
            )}
            <ReservationDate>{handleDate(listData.createdAt)}</ReservationDate>
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
            <CategoryButton
              onClick={e => {
                e.stopPropagation();
                registerEditDataSend(listData.id);
              }}
            >
              수정하기
            </CategoryButton>
          )}
          {type === 'reservation' && (
            <>
              {handleDate(today) < handleDate(listData.startTime) ? (
                <CategoryButton onClick={showModal}>취소하기</CategoryButton>
              ) : (
                <CategoryButton onClick={showReviewModal}>
                  리뷰쓰기
                </CategoryButton>
              )}
              {modalOpen && (
                <Modal
                  modalOpen={modalOpen}
                  setModalOpen={setModalOpen}
                  modalText="예악을 취소하시겠습니까?"
                  modalActionText="취소하기"
                  modalAction={reservationCancel}
                />
              )}
              <CategoryButton onClick={onClickPayment}>결제하기</CategoryButton>
              {payModalOpen && <Modal {...IsPayment} />}
            </>
          )}
          {type === 'bookmark' && (
            <CategoryButton onClick={bookMarkStatusChange}>
              <BsFillBookmarkFill size={24} />
            </CategoryButton>
          )}
          {type === 'reviews' && (
            <>
              <CategoryButton onClick={showReviewModal}>
                수정하기
              </CategoryButton>
              <CategoryButton onClick={showModal}>삭제하기</CategoryButton>
              {modalOpen && (
                <Modal
                  modalOpen={modalOpen}
                  setModalOpen={setModalOpen}
                  modalText="리뷰를 삭제하시겠습니까?"
                  modalActionText="삭제하기"
                  modalAction={reviewDelete}
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
          reviewComment={listData.comment}
          reviewScore={listData.score}
          reserveId={listData.reserveId}
          placeId={listData.placeId}
          reviewId={listData.reviewId}
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
  cursor: pointer;
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
  height: 128px;
  border-radius: 15px;
`;

export default MyPageCategoryList;
