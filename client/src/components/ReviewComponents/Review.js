import React from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';
import dayjs from 'dayjs';

function Review({ profileImage, nickname, createdAt, score, comment }) {
  const handleDate = createdDate => {
    return dayjs(createdDate).format('YYYY.MM.DD');
  };

  return (
    <ReviewContent>
      <UserImage src={profileImage} />
      <ReviewBodyContainer>
        <ReviewInfoContainer>
          <UserName>{nickname}</UserName>
          <ReviewCreatedDate>{handleDate(createdAt)}</ReviewCreatedDate>
        </ReviewInfoContainer>
        <ReviewRating>
          {[1, 2, 3, 4, 5].map(el => (
            <ImStarFull
              className={score >= el && 'black'}
              key={el}
              id={el}
              size="20"
            />
          ))}
        </ReviewRating>
        <ReviewComment value={comment}>{comment}</ReviewComment>
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
