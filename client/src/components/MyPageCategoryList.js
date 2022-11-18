import React from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';
import { BsFillBookmarkFill } from 'react-icons/bs';

const chargeComponent = (listData, type) => {
  if (type === 'reviews') {
    return null;
  }
  if (type === 'reservation') {
    return <PlaceCharge>{listData.totalCharge}원</PlaceCharge>;
  }
  return <PlaceCharge>{listData.charge}원</PlaceCharge>;
};

function MyPageCategoryList({ listData, type }) {
  return (
    <CategoryItemList>
      <CategoryContainer>
        <CategoryBodyContainer>
          <PlaceBodyContainer>
            <PlaceTitle>{listData.title}</PlaceTitle>
            <PlaceAddress>{listData.comment}</PlaceAddress>
            <PlaceAddress>{listData.address}</PlaceAddress>
            {type === 'reservation' && (
              <ReservationDate>
                {listData.startTime}~{listData.endTime}
              </ReservationDate>
            )}
            <ReservationDate>{listData.createdAt}</ReservationDate>
          </PlaceBodyContainer>
          {type === 'reservation' ? null : (
            <RatingStarContainer>
              <ImStarFull size={23} />
              <PlaceRating>{listData.score}</PlaceRating>
            </RatingStarContainer>
          )}
        </CategoryBodyContainer>
        <CategoryActionContainer>
          {type === 'registration' && <CategoryButton>수정하기</CategoryButton>}
          {type === 'reservation' && <CategoryButton>취소하기</CategoryButton>}
          {type === 'bookmark' && (
            <CategoryButton>
              <BsFillBookmarkFill size={24} />
            </CategoryButton>
          )}
          {type === 'reviews' && (
            <>
              <CategoryButton>수정하기</CategoryButton>
              <CategoryButton>삭제하기</CategoryButton>
            </>
          )}
          {chargeComponent(listData, type)}
        </CategoryActionContainer>
      </CategoryContainer>
      <PlaceImage src={listData.image} />
    </CategoryItemList>
  );
}

const CategoryItemList = styled.div`
  box-sizing: border-box;
  padding: 24px 20px 24px 40px;
  width: 680px;
  height: 165px;
  background-color: #ffffff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  border-radius: 15px;
  margin: 16px 0px;
`;

const CategoryContainer = styled.div`
  height: 100%;
  width: 70%;
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: column;
`;

const CategoryBodyContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  flex-direction: row;
  width: 100%;

  & svg {
    color: #ffce31;
    margin-right: 5px;
  }
`;

const PlaceBodyContainer = styled.div`
  width: 100%;
`;

const PlaceTitle = styled.p`
  font-size: 20px;
  font-weight: bold;
  margin-bottom: 6px;
  color: #1b1c1e;
`;

const PlaceAddress = styled.span`
  font-size: 15px;
  color: #2b2b2b;
`;

const RatingStarContainer = styled.div`
  display: flex;
  flex-direction: row;
`;

const PlaceRating = styled.div`
  font-size: 23px;
  font-weight: bold;
`;

const CategoryActionContainer = styled.div`
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  flex-direction: row;
  width: 100%;
`;

const CategoryButton = styled.div`
  font-size: 18px;
  color: #eb7470;

  :hover {
    cursor: pointer;
    font-weight: bold;
  }

  & svg :hover {
    cursor: pointer;
  }
`;

const ReservationDate = styled.div`
  margin-top: 8px;
  color: #9a9a9a;
`;

const PlaceCharge = styled.span`
  font-size: 20px;
  font-weight: bold;
  color: #eb7470;
`;

const PlaceImage = styled.img`
  width: 128px;
  border-radius: 15px;
`;

export default MyPageCategoryList;
