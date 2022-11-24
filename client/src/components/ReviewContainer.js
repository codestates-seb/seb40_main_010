import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import Review from './Review';

function ReviewContainer(placeId) {
  const [reviews, setReviews] = useState([]);

  const temp = [
    {
      reviewId: '1',
      profileImage: 'https://picsum.photos/200/50',
      nickname: '홍길동',
      score: '4',
      comment: '편의점 가깝고 좋아요',
      createdAt: '2022-10-03',
    },
    {
      reviewId: '2',
      profileImage: 'https://picsum.photos/201/50',
      nickname: '둘리',
      score: '5',
      comment: '완전 좋아요',
      createdAt: '2022-08-03',
    },
    {
      reviewId: '3',
      profileImage: 'https://picsum.photos/202/50',
      nickname: '고길동',
      score: '3',
      comment: '우 구려요',
      createdAt: '2022-09-21',
    },
  ];

  const callReviews = async () => {
    try {
      const response = await axios.get(`/review/${placeId}`);
      setReviews([...response.data]);
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    callReviews();
    setReviews([...temp]);
  }, []);

  return (
    <ReviewsContainer>
      <ReviewHeadContainer>
        <ReviewCount>리뷰 {reviews.length}개</ReviewCount>
        <ReviewFilter>≡</ReviewFilter>
      </ReviewHeadContainer>
      {reviews.map(el => {
        return <Review reviewData={el} key={el.reviewId} />;
      })}
    </ReviewsContainer>
  );
}

const ReviewsContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: flex-start;
  background-color: #ffffff;
  flex-direction: column;
  border: 1px solid red;
  width: 1000px;
`;

const ReviewHeadContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
`;

const ReviewCount = styled.p`
  font-size: 24px;
`;

const ReviewFilter = styled.div`
  cursor: pointer;
  font-size: 24px;
  font-weight: bold;
  margin-right: 0.5rem;
`;

export default ReviewContainer;
