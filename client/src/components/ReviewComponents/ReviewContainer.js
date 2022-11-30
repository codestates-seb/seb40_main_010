import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import axios from 'axios';
// import { useRecoilValue } from 'recoil';
import { useParams } from 'react-router-dom';
import Review from './Review';
// import { PlaceIDState } from '../atoms';

function ReviewContainer() {
  const [reviews, setReviews] = useState([]);
  // const placeId = useRecoilValue(PlaceIDState);

  const { id } = useParams();
  const header = {
    headers: {
      'ngrok-skip-browser-warning': '010',
      Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
      RefreshToken: localStorage.getItem('REFRESH'),
    },
  };

  const callReviews = async () => {
    try {
      const response = await axios.get(`/review/${id}`, header);
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
  width: 67rem;
  padding-left: 2rem;
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
  font-size: 1.5rem;
`;

const ReviewFilter = styled.div`
  cursor: pointer;
  font-size: 1.5rem;
  font-weight: bold;
  margin-right: 1rem;
`;

const ReviewBorder = styled.div`
  width: 100%;
  border-top: 2px solid #89bbff;
  border-bottom: 2px solid #89bbff;
`;

export default ReviewContainer;
