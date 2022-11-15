import React, { useState } from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';

function ReviewWrite({ reviewModalOpen, setReviewModalOpen }) {
  // 모달창 여닫는 상태
  const showReviewModal = () => {
    setReviewModalOpen(!reviewModalOpen);
  };

  // TODO: 별점 관리 for문 없애고 코드 정리하기
  const ratings = [0, 1, 2, 3, 4];

  const [ratedStars, setRatedStars] = useState([
    false,
    false,
    false,
    false,
    false,
  ]);

  const today = new Date();

  const handleStarClick = index => {
    const clickStates = [...ratedStars];
    for (let i = 0; i < 5; i += 1) {
      clickStates[i] = i <= index;
    }
    setRatedStars(clickStates);
  };

  const score = ratedStars.filter(Boolean).length;

  // TODO : 작성 내용 상태로 저장해서 등록 클릭시 데이터 담아서 보내는 것까지 구현하기
  const [reviewText, setReviewText] = useState('');

  const changeReviewText = e => {
    setReviewText(e.target.value);
  };

  const submitReview = () => {
    const data = {
      score,
      comment: reviewText,
    };
    console.log(data);
  };

  return (
    <ReviewContainer>
      <HeadContainer>
        <HeadTextContainer>
          <TextDateContainer>
            <Review>리뷰 작성</Review>
            <CurrentDate>{today.toLocaleDateString()}</CurrentDate>
          </TextDateContainer>
          <PlaceName>인테리어가 아름다운 리빙형 대관 공간</PlaceName>
          <RatingBox>
            {ratings.map(el => (
              <ImStarFull
                key={el}
                onClick={() => handleStarClick(el)}
                className={ratedStars[el] && 'black'}
                size="35"
              />
            ))}
          </RatingBox>
        </HeadTextContainer>
        <Image src="https://picsum.photos/200" />
      </HeadContainer>
      <ReviewInput
        placeholder="255자 이내로 작성해주세요."
        onChange={changeReviewText}
      >
        {reviewText}
      </ReviewInput>
      <ButtonContainer>
        <ReviewButton className="blue" onClick={submitReview}>
          등록
        </ReviewButton>
        <ReviewButton onClick={showReviewModal}>취소</ReviewButton>
      </ButtonContainer>
    </ReviewContainer>
  );
}

const ReviewContainer = styled.div`
  box-sizing: border-box;
  padding: 24px 16px 24px 16px;
  display: flex;
  justify-content: space-around;
  align-items: center;
  flex-direction: column;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  width: 570px;
  height: 420px;
  border-radius: 20px;
`;

const HeadContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 90%;
`;

const HeadTextContainer = styled.div`
  box-sizing: border-box;
  display: flex;
  justify-content: center;
  align-items: flex-start;
  flex-direction: column;
  height: 100%;
`;

const TextDateContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: flex-end;
`;

const Review = styled.div`
  font-size: 24px;
  font-weight: bold;
`;

const CurrentDate = styled.div`
  font-size: 16px;
  color: #616161;
  margin-left: 12px;
`;

const PlaceName = styled.div`
  display: flex;
  justify-content: center;
  align-items: top;
  font-size: 18px;
  font-weight: bold;
  padding-top: 8px;
  height: 50px;
`;

const Image = styled.img`
  width: 130px;
  height: 130px;
  border-radius: 15px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
`;

const ReviewInput = styled.textarea`
  box-sizing: border-box;
  width: 90%;
  height: 130px;
  border-radius: 15px;
  border: 1px solid #d9d9d9;
  padding: 1em;
  opacity: 1;
  resize: none;

  :focus {
    outline: 1px solid #89bbff;
  }
`;

const ButtonContainer = styled.div`
  display: flex;
  justify-content: right;
  align-items: center;
  width: 90%;
`;

const ReviewButton = styled.button`
  border: none;
  border-radius: 25px;
  font-size: 25px;
  font-weight: bold;
  width: 115px;
  height: 45px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  margin-left: 16px;

  &.blue {
    background-color: #89bbff;
    color: white;
  }

  :hover {
    cursor: pointer;
  }
`;

const RatingBox = styled.div`
  & svg {
    color: #c4c4c4;
    cursor: pointer;
    width: 30px;
  }
  :hover svg {
    color: #ffce31;
  }
  & svg:hover ~ svg {
    color: #c4c4c4;
  }
  .black {
    color: #ffce31;
  }
`;

export default ReviewWrite;
