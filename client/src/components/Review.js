import React from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';
import axios from 'axios';

function Review({ reviewData }) {
  const reviewDelete = async () => {
    try {
      await axios.get(`/review/${reviewData.reviewId}`);
    } catch (err) {
      console.log(err);
    }
  };

  return (
    <ReviewContent>
      <UserImage src={reviewData.profileImage} />
      <ReviewBodyContainer>
        <ReviewInfoContainer>
          <UserName value={reviewData.nickname}>{reviewData.nickname}</UserName>
          <ReviewCreatedDate value={reviewData.createdAt}>
            {reviewData.createdAt}
          </ReviewCreatedDate>
          <ReviewDelete onClick={reviewDelete}>삭제하기</ReviewDelete>
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
  border: 1px solid green;
  width: 100%;
  padding: 1rem 0rem;
  display: flex;
  flex-direction: row;
`;

const UserImage = styled.img`
  width: 50px;
  height: 50px;
  border-radius: 30px;
  margin-right: 0.5rem;
`;

const ReviewBodyContainer = styled.div``;

const ReviewInfoContainer = styled.div`
  display: flex;
`;

const UserName = styled.p``;

const ReviewCreatedDate = styled.p``;

const ReviewDelete = styled.p`
  cursor: pointer;
`;

const ReviewRating = styled.div`
  & svg {
    color: #c4c4c4;
  }
  .black {
    color: #ffce31;
  }

  margin-bottom: 0.5rem;
`;

const ReviewComment = styled.div``;

export default Review;
