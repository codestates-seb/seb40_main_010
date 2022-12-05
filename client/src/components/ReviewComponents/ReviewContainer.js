import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import { useParams } from 'react-router-dom';
import Review from './Review';

function ReviewContainer() {
  const [reviews, setReviews] = useState([]);

  const { id } = useParams();

  const callReviews = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/review/place/${id}`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
          },
        },
      );
      setReviews([...response.data.data]);
    } catch (err) {
      setReviews([]);
    }
  };

  useEffect(() => {
    callReviews();
  }, []);

  return (
    <ReviewsContainer>
      <ReviewBorder>
        <ReviewHeadContainer>
          <ReviewCount>리뷰 {reviews.length}개</ReviewCount>
          <ReviewFilter>≡</ReviewFilter>
        </ReviewHeadContainer>
        {reviews.map(
          ({ profileImage, nickname, createdAt, score, comment, reviewId }) => {
            return (
              <Review
                profileImage={profileImage}
                nickname={nickname}
                createdAt={createdAt}
                score={score}
                comment={comment}
                key={reviewId}
              />
            );
          },
        )}
      </ReviewBorder>
    </ReviewsContainer>
  );
}

const ReviewsContainer = styled.div`
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  background-color: #f9f9f9;
  flex-direction: column;
  width: 1260px;
  padding-left: 2rem;
  margin-bottom: 3rem;
`;

const ReviewHeadContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  width: 100%;
  margin-bottom: 2.5rem;
  padding-top: 1rem;
`;

const ReviewCount = styled.p`
  font-size: 1.5rem;
`;

const ReviewFilter = styled.div`
  cursor: pointer;
  font-size: 1.5rem;
  font-weight: bold;
  margin-right: 1rem;
`;

const ReviewBorder = styled.div`
  padding: 30px 0px;
  width: 100%;
  border-top: 1px solid #aaa;
  border-bottom: 1px solid #aaa;
`;

export default ReviewContainer;
