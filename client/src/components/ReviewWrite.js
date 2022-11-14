import React, { useState } from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';

const array = [0, 1, 2, 3, 4];

function ReviewWrite() {
  const [clicked, setClicked] = useState([false, false, false, false, false]);

  const today = new Date();

  const handleStarClick = index => {
    const clickStates = [...clicked];
    for (let i = 0; i < 5; i += 1) {
      clickStates[i] = i <= index;
    }
    setClicked(clickStates);
  };
  const score = clicked.filter(Boolean).length;
  console.log(score);

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
            {array.map(el => (
              <ImStarFull
                key={el}
                onClick={() => handleStarClick(el)}
                className={clicked[el] && 'black'}
                size="35"
              />
            ))}
          </RatingBox>
        </HeadTextContainer>
        <Image src="https://a0.muscache.com/im/pictures/miso/Hosting-46566256/original/7225b27d-fb5d-431b-9fc4-40be31efea23.jpeg?im_w=1200" />
      </HeadContainer>
      <ReviewInput placeholder="255자 이내로 작성해주세요." />
      <ButtonContainer>
        <ReviewButton className="blue">등록</ReviewButton>
        <ReviewButton>취소</ReviewButton>
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
