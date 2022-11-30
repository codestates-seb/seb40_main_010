import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';
import axios from 'axios';
import useMyPage from '../../hooks/useMyPage';

function ReviewWrite({
  reviewModalOpen,
  setReviewModalOpen,
  placeName,
  placeImageURL,
  reviewComment,
  reviewScore,
  reserveId,
  placeId,
  reviewId,
}) {
  const [reviewStar, setReviewStar] = useState(null);
  const [reviewText, setReviewText] = useState('');
  const { reviewList } = useMyPage();

  // const header = {
  //   headers: {
  //     Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
  //     RefreshToken: localStorage.getItem('REFRESH'),
  //   },
  // };

  const showReviewModal = () => {
    setReviewModalOpen(!reviewModalOpen);
  };
  const today = new Date();

  const changeReviewText = e => {
    setReviewText(e.target.value);
  };

  const submitReview = async () => {
    const data = {
      score: reviewStar,
      comment: reviewText,
    };
    try {
      await axios.post(`/review/${placeId}/reserve/${reserveId}`, data, {
        headers: {
          'ngrok-skip-browser-warning': '010',
          Authorization: (await localStorage.getItem('ACCESS'))
            ? `Bearer ${localStorage.getItem('ACCESS')}`
            : '',
          RefreshToken: (await localStorage.getItem('REFRESH'))
            ? localStorage.getItem('REFRESH')
            : '',
        },
      });
      showReviewModal();
    } catch (err) {
      console.log(err);
    }
  };

  const submitEditReview = async () => {
    const data = {
      score: reviewStar,
      comment: reviewText,
    };
    try {
      await axios.patch(`/review/${reviewId}/edit`, data, {
        headers: {
          'ngrok-skip-browser-warning': '010',
          Authorization: (await localStorage.getItem('ACCESS'))
            ? `Bearer ${localStorage.getItem('ACCESS')}`
            : '',
          RefreshToken: (await localStorage.getItem('REFRESH'))
            ? localStorage.getItem('REFRESH')
            : '',
        },
      });
      showReviewModal();
      await reviewList();
    } catch (err) {
      console.log(err);
    }
  };

  useEffect(() => {
    setReviewText(reviewComment);
    setReviewStar(reviewScore);
  }, []);

  return (
    <BlurBackground>
      <ReviewContainer>
        <HeadContainer>
          <HeadTextContainer>
            <TextDateContainer>
              <Review>리뷰 작성</Review>
              <CurrentDate>{today.toLocaleDateString()}</CurrentDate>
            </TextDateContainer>
            <PlaceName>{placeName}</PlaceName>
            <RatingBox>
              {[1, 2, 3, 4, 5].map(el => (
                <ImStarFull
                  className={reviewStar >= el && 'black'}
                  key={el}
                  id={el}
                  onClick={() => {
                    setReviewStar(el);
                  }}
                  size="35"
                />
              ))}
            </RatingBox>
          </HeadTextContainer>
          <Image src={placeImageURL} />
        </HeadContainer>
        <ReviewInput
          placeholder="255자 이내로 작성해주세요."
          onChange={changeReviewText}
          value={reviewText}
        >
          {reviewText}
        </ReviewInput>
        <ButtonContainer>
          {reviewId ? (
            <ReviewButton className="blue" onClick={submitEditReview}>
              수정
            </ReviewButton>
          ) : (
            <ReviewButton className="blue" onClick={submitReview}>
              등록
            </ReviewButton>
          )}
          <ReviewButton onClick={showReviewModal}>취소</ReviewButton>
        </ButtonContainer>
      </ReviewContainer>
    </BlurBackground>
  );
}

const BlurBackground = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
  background-color: rgba(255, 255, 255, 0.8);
  z-index: 100;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  width: 100%;
  height: 100%;
`;

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
  background-color: #ffffff;

  z-index: 999;
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
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