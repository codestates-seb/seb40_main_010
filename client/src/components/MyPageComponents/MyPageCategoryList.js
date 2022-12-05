import React, { useState } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import { useSetRecoilState } from 'recoil';
import { ImStarFull } from 'react-icons/im';
import { BsFillBookmarkFill } from 'react-icons/bs';
import { useNavigate } from 'react-router-dom';
import dayjs from 'dayjs';
import Modal from '../../utils/Modal';
import ReviewWrite from '../ReviewComponents/ReviewWrite';
import { reservationEditData } from '../../atoms';
import useMyPage from '../../hooks/useMyPage';
import { onClickPaymentButton } from '../../utils/payment';

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
  const [paymentModalOpen, setPaymentModalOpen] = useState(false);

  const [reviewModalOpen, setReviewModalOpen] = useState(false);
  const setEditData = useSetRecoilState(reservationEditData);
  const { bookmarkList, reviewList, reservationList } = useMyPage();

  const navigate = useNavigate();

  const showModal = () => {
    setModalOpen(!modalOpen);
  };

  const showPaymentModal = () => {
    setPaymentModalOpen(!paymentModalOpen);
  };

  const showReviewModal = () => {
    setReviewModalOpen(!reviewModalOpen);
  };

  const handleDate = createdAt => {
    if (createdAt === undefined) return null;

    const date = dayjs(createdAt).subtract(2000, 'y').add(9, 'hour');
    if (date.$H > 12) {
      date.$H -= 12;
      return `${date.format('YY.MM.DD hh')}PM`;
    }
    return `${date.format('YY.MM.DD hh')}AM`;
  };

  const handleReviewDate = createdAt => {
    if (createdAt === undefined) return null;

    const date = dayjs(createdAt);

    return date.format('YYYY.MM.DD');
  };

  const reviewDelete = async () => {
    try {
      await axios.delete(
        `${process.env.REACT_APP_SERVER_BASE_URL}/review/${listData.reviewId}`,
        {
          headers: {
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      showModal();
      await reviewList();
    } catch (err) {
      showModal();
    }
  };

  const onClickPaymentKaKaoButton = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/place/reserve/${listData.reserveId}/payment`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      const paymentUrl = response.data.data;
      onClickPaymentButton(paymentUrl);
      await setPaymentModalOpen(false);
    } catch (error) {
      console.log('Error', error);
    }
  };

  const bookMarkStatusChange = async () => {
    try {
      await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/bookmark/${listData.placeId}`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      await bookmarkList();
    } catch (err) {
      console.log(err);
    }
  };

  const registerEditDataSend = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/place/${listData.placeId}`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      // TODO : 다른 방식으로 바꿔보기, 직접 할당 X
      response.data.filePath = [];
      response.data.category = [];
      setEditData(response.data);
      navigate('/register');
    } catch (err) {
      console.log(err);
    }
  };

  const moveDetailPage = () => {
    navigate(`/detail/${listData.placeId}`);
  };

  const reservationCancel = async () => {
    try {
      await axios.delete(
        `${process.env.REACT_APP_SERVER_BASE_URL}/reserve/${listData.reserveId}`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      showModal();
      await reservationList();
    } catch (err) {
      console.log(err);
    }
  };

  const handleDateComparison = () => {
    const today = new Date();
    return (
      Number(dayjs(today).format('YYYYMMDDhh')) <
      Number(dayjs(listData.startTime).add(9, 'hour').format('YYYYMMDDhh'))
    );
  };

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
            <ReservationDate>
              {handleReviewDate(listData.createdAt)}
            </ReservationDate>
          </PlaceBodyContainer>
          {/* url이나 search를 사용해서 바꿔보기 */}
          {type === 'reservation' ? (
            <CategoryPaymentSuccessContainer>
              <CategoryPaymentSuccess>{listData.status}</CategoryPaymentSuccess>
            </CategoryPaymentSuccessContainer>
          ) : (
            <RatingStarContainer>
              <ImStarFull size={22} />
              <PlaceRating>{listData.score}</PlaceRating>
            </RatingStarContainer>
          )}
        </CategoryBodyContainer>
        <CategoryActionContainer>
          <ButtonContainer>
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
                {handleDateComparison() ? (
                  <>
                    <CategoryButton onClick={showModal}>
                      취소하기
                    </CategoryButton>
                    {listData.status !== '결제 완료' ? (
                      <CategoryButton onClick={showPaymentModal}>
                        결제 하기
                      </CategoryButton>
                    ) : null}
                  </>
                ) : (
                  <CategoryButton onClick={showReviewModal}>
                    리뷰쓰기
                  </CategoryButton>
                )}
                {modalOpen && (
                  <Modal
                    modalOpen={modalOpen}
                    setModalOpen={setModalOpen}
                    modalText="예약을 취소하시겠습니까?"
                    modalActionText="취소하기"
                    modalAction={reservationCancel}
                  />
                )}
                {paymentModalOpen && (
                  <Modal
                    modalOpen={paymentModalOpen}
                    setModalOpen={setPaymentModalOpen}
                    modalText="결제 하시겠습니까?"
                    modalActionText="결제하기"
                    modalAction={onClickPaymentKaKaoButton}
                  />
                )}
              </>
            )}
            {type === 'bookmark' && (
              <CategoryButton onClick={bookMarkStatusChange}>
                <BsFillBookmarkFill size={20} />
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
          </ButtonContainer>
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
  padding: 1.5rem 1.25rem 1.5rem 2.5rem;
  width: 42.5rem;
  height: 10rem;
  background-color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #aaa;

  @media (max-width: 840px) {
    width: 20rem;
    height: 8rem;
    padding: 1rem 0.75rem 1rem 1rem;
  }
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
  font-weight: 500;
  font-size: 18px;
  margin-bottom: 6px;
  color: #1b1c1e;
  cursor: pointer;

  @media (max-width: 840px) {
    font-size: 1rem;
  }
`;

const PlaceAddress = styled.span`
  font-size: 13px;
  color: #666666;

  @media (max-width: 840px) {
    font-size: 14px;
  }
`;

const RatingStarContainer = styled.div`
  display: flex;
  flex-direction: row;
`;

const PlaceRating = styled.div`
  font-size: 18px;
  font-weight: bold;
  padding-top: 3px;
`;

const CategoryActionContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  flex-direction: row;
  width: 100%;
`;

const ButtonContainer = styled.div`
  width: 10rem;
  display: flex;
  justify-content: space-between;

  @media (max-width: 840px) {
    width: 6rem;
  }
`;

const CategoryButton = styled.div`
  font-size: 1rem;
  color: #eb7470;

  @media (max-width: 840px) {
    font-size: 14px;
  }

  :hover {
    cursor: pointer;
    font-weight: bold;
  }

  & svg :hover {
    cursor: pointer;
  }
`;

const CategoryPaymentSuccess = styled.div`
  /* width: fit-content; */
  text-align: end;
  width: 4rem;
  font-size: 0.6rem;
  color: #eb7470;
  @media (max-width: 840px) {
    font-size: 12px;
  }
`;

const CategoryPaymentSuccessContainer = styled.div`
  width: fit-content;
  display: flex;
  justify-content: flex-end;
`;

const ReservationDate = styled.div`
  margin-top: 0.5rem;
  color: #9a9a9a;

  @media (max-width: 840px) {
    font-size: 13px;
  }
`;

const PlaceCharge = styled.span`
  font-size: 18px;
  font-weight: bold;
  color: #eb7470;

  @media (max-width: 840px) {
    font-size: 14px;
  }
`;

const PlaceImage = styled.img`
  width: 8rem;
  height: 8rem;

  @media (max-width: 840px) {
    width: 5rem;
    height: 5rem;
  }
`;

export default MyPageCategoryList;
