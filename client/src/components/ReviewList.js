import React from 'react';
import styled from 'styled-components';
// import { ImStarFull } from 'react-icons/im';

const reviewData = [
  {
    reviewId: '0',
    profileImage: 'https://picsum.photos/200',
    nickname: '홍길동',
    score: '4',
    comment: '준비가 잘 되어있어 편히 쉬고 시설을 사용할 수 있었습니다.',
    createdAt: '2022.11.08',
  },
  {
    reviewId: '1',
    profileImage: 'https://picsum.photos/201',
    nickname: '배추도사',
    score: '3',
    comment: '가격도 싸고 좋아요, 또 오겠습니다.',
    createdAt: '2021.03.24',
  },
  {
    reviewId: '2',
    profileImage: 'https://picsum.photos/202',
    nickname: '무도사',
    score: '5',
    comment: '좋았어요.',
    createdAt: '2022.08.02',
  },
];

function ReviewList({
  // data,
  reviewDeleteAction,
}) {
  return (
    <ReviewContainer>
      <ReviewHeadContainer>
        <ReviewAmountText>리뷰 {reviewData.length}개</ReviewAmountText>
        <ReviewFilter />
      </ReviewHeadContainer>
      {/* 더미데이터로 구현해놓은 것을 데이터로 변경하기 */}
      {reviewData.map(el => (
        <ReviewBodyContainer key={el.reviewId}>
          <ReviewerProfileImage src={el.profileImage} />
          <ReviewBodyTextContainer>
            <ReviewerName>{el.nickname}</ReviewerName>
            <ReviewDate>{el.createdAt}</ReviewDate>
            <ReviewDelete onClick={reviewDeleteAction} />
          </ReviewBodyTextContainer>
          <ReviewRatingStars>
            {/* score 숫자로 받아온 것 별 갯수로 나타내도록 구현하기 */}
          </ReviewRatingStars>
          <ReviewText>{el.comment}</ReviewText>
        </ReviewBodyContainer>
      ))}
      {/* 여기까지 */}
    </ReviewContainer>
  );
}

const ReviewContainer = styled.div``;

const ReviewHeadContainer = styled.div``;

const ReviewAmountText = styled.div``;

const ReviewFilter = styled.div``;

const ReviewBodyContainer = styled.div``;

const ReviewerProfileImage = styled.img`
  width: 150px;
`;

const ReviewBodyTextContainer = styled.div``;

const ReviewerName = styled.div``;

const ReviewDate = styled.div``;

const ReviewDelete = styled.div``;

const ReviewRatingStars = styled.div`
  & svg {
    color: #ffce31;
    cursor: pointer;
    width: 30px;
  }
`;

const ReviewText = styled.div``;

export default ReviewList;
