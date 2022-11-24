import React from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';

function Review({ reviewData }) {
  return (
    <ReviewContent>
      <UserImage src={reviewData.profileImage} />
      <ReviewBodyContainer>
        <ReviewInfoContainer>
          <UserName value={reviewData.nickname}>{reviewData.nickname}</UserName>
          <ReviewCreatedDate value={reviewData.createdAt}>
            {reviewData.createdAt}
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
  margin-bottom: 2rem;
`;

const UserImage = styled.img`
  width: 50px;
  height: 50px;
  border-radius: 30px;
  margin-right: 1rem;
`;

const ReviewBodyContainer = styled.div`
  width: 100%;
`;

const ReviewInfoContainer = styled.div`
  width: 100%;
  display: flex;
  padding-right: 16px;
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
