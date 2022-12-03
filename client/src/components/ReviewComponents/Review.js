import React from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';
import dayjs from 'dayjs';

function Review({ reviewData }) {
  const handleDate = createdAt => {
    return dayjs(createdAt).format('YYYYMMDD');
  };

  return (
    <ReviewContent>
      <UserImage src={reviewData.profileImage} />
      <ReviewBodyContainer>
        <ReviewInfoContainer>
          <UserName>{reviewData.nickname}</UserName>
          <ReviewCreatedDate>
            {handleDate(reviewData.createdAt)}
          </ReviewCreatedDate>
        </ReviewInfoContainer>
        <ReviewRating>
          {[1, 2, 3, 4, 5].map(el => (
            <ImStarFull
              className={reviewData.score >= el && 'black'}
              key={el}
              id={el}
              size="20"
            />
          ))}
        </ReviewRating>
        <ReviewComment value={reviewData.comment}>
          {reviewData.comment}
        </ReviewComment>
      </ReviewBodyContainer>
    </ReviewContent>
  );
}

const ReviewContent = styled.div`
  display: flex;
  width: 100%;
  display: flex;
  flex-direction: row;
  margin-bottom: 3rem;
`;

const UserImage = styled.img`
  width: 3.25rem;
  height: 3.25rem;
  border-radius: 2rem;
  margin-right: 1rem;
`;

const ReviewBodyContainer = styled.div`
  width: 100%;
`;

const ReviewInfoContainer = styled.div`
  width: 100%;
  display: flex;
  padding-right: 1rem;
  box-sizing: border-box;
`;

const UserName = styled.p`
  font-weight: bold;
  flex-grow: 1;
`;

const ReviewCreatedDate = styled.p`
  flex-grow: 30;
  color: #666666;
`;

const ReviewRating = styled.div`
  & svg {
    color: #c4c4c4;
  }
  .black {
    color: #ffce31;
  }

  margin-top: 0.5rem;
  margin-bottom: 0.5rem;
`;

const ReviewComment = styled.div``;

export default Review;
