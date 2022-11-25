import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import { useRecoilValue } from 'recoil';
import Review from './Review';
import { PlaceIDState } from '../atoms';

function ReviewContainer() {
  const [reviews, setReviews] = useState([]);
  const placeId = useRecoilValue(PlaceIDState);

  const header = {
    headers: {
      'ngrok-skip-browser-warning': '010',
      Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
      RefreshToken: localStorage.getItem('REFRESH'),
    },
  };

  // const temp = [
  //   {
  //     reviewId: '1',
  //     profileImage: 'https://picsum.photos/200/50',
  //     nickname: '홍길동',
  //     score: '4',
  //     comment: '편의점 가깝고 좋아요',
  //     createdAt: '2022-10-03',
  //   },
  //   {
  //     reviewId: '2',
  //     profileImage: 'https://picsum.photos/201/50',
  //     nickname: '둘리',
  //     score: '5',
  //     comment: '완전 좋아요',
  //     createdAt: '2022-08-03',
  //   },
  //   {
  //     reviewId: '3',
  //     profileImage: 'https://picsum.photos/202/50',
  //     nickname: '고길동',
  //     score: '3',
  //     comment: '우 구려요',
  //     createdAt: '2022-09-21',
  //   },
  // ];

  const callReviews = async () => {
    try {
      const response = await axios.get(`/review/${placeId}`, header);
      setReviews([...response.data.data]);
    } catch (err) {
      console.log(err);
      setReviews([]);
    }
  };

  useEffect(() => {
    callReviews();
    // setReviews([...temp]);
  }, []);

  return (
    <ReviewsContainer>
      <ReviewBorder>
        <ReviewHeadContainer>
          <ReviewCount>리뷰 {reviews.length}개</ReviewCount>
          <ReviewFilter>≡</ReviewFilter>
        </ReviewHeadContainer>
        {reviews.map(el => {
          return <Review reviewData={el} key={el.reviewId} />;
        })}
      </ReviewBorder>
    </ReviewsContainer>
  );
}

const ReviewsContainer = styled.div`
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  background-color: #ffffff;
  flex-direction: column;
  width: 1070px;
  padding-left: 35px;
  margin-bottom: 3rem;
`;

const ReviewHeadContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 1.5rem;
  padding-top: 1rem;
`;

const ReviewCount = styled.p`
  font-size: 24px;
`;

const ReviewFilter = styled.div`
  cursor: pointer;
  font-size: 24px;
  font-weight: bold;
  margin-right: 1rem;
`;

const ReviewBorder = styled.div`
  width: 100%;
  border-top: 2px solid #89bbff;
  border-bottom: 2px solid #89bbff;
`;

export default ReviewContainer;
