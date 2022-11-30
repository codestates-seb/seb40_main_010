import React from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';

function Review({ reviewData }) {
  const handleDate = createdAt => {
    if (createdAt === undefined) return null;

    let date = new Date(createdAt);
    const month = date.getMonth() + 1;
    const utc = date.getTime() + date.getTimezoneOffset() * 60 * 1000;
    const KR_TIME_DIFF = 9 * 60 * 60 * 1000;
    const krCurr = utc + KR_TIME_DIFF;

    date = new Date(krCurr).toString();

    const splitDate = date.split(' ');
    const day = splitDate[2];
    const year = splitDate[3];

    return `${year}.${month}.${day}`;
  };

  return (
    <ReviewContent>
      <UserImage src={reviewData.profileImage} />
      <ReviewBodyContainer>
        <ReviewInfoContainer>
          <UserName value={reviewData.nickname}>{reviewData.nickname}</UserName>
          <ReviewCreatedDate value={reviewData.createdAt}>
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
  margin-bottom: 2rem;
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
