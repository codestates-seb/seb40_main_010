import React from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';
import { Link } from 'react-router-dom';
import { useSetRecoilState, useResetRecoilState } from 'recoil';

import { DetailInformation, PlaceIDState } from '../atoms';

function Place({ placeData }) {
  const setFocusPlaceID = useSetRecoilState(PlaceIDState);
  const resetDetailInformation = useResetRecoilState(DetailInformation);

  const { address, charge, image, score, title, placeId } = placeData;

  // const slicedTitle = title.slice(0, 15);

  const addressSlice = address.slice(0, 20);

  const onClickPlaceComponent = () => {
    resetDetailInformation();
    setFocusPlaceID(placeId);
  };

  const onClickPlaceContainer = e => {
    e.stopPropagation();
  };

  const chargePerHour = new Intl.NumberFormat('ko-KR').format(charge);

  return (
    <MainContainer onClick={onClickPlaceContainer}>
      <Link to={`/detail/${placeId}`}>
        <MainComponent onClick={onClickPlaceComponent}>
          <Image src={image} />
          <TitleContainer>
            <PlaceName>{title}</PlaceName>
          </TitleContainer>
          <PlaceAddress>{addressSlice}</PlaceAddress>
          <Div>
            <StarContainer>
              <ImStarFull className="starIcon" />
              <PlaceScore>{score}</PlaceScore>
            </StarContainer>
            <PlaceCharge>{chargePerHour}원</PlaceCharge>
          </Div>
        </MainComponent>
      </Link>
    </MainContainer>
  );
}

const MainContainer = styled.div`
  display: flex;
  flex-direction: row;
  margin-bottom: 20px;
  width: 300px;
  margin-left: 3px;
  margin-right: 3px;
  a {
    text-decoration: none;
    color: #2b2b2b;
  }
`;

const Div = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
`;

const MainComponent = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  width: 280px;
  padding: 10px;
  z-index: 10px;
`;

const Image = styled.img`
  width: 280px;
  height: 250px;
  border-radius: 0px;
  margin-bottom: 10px;
  &:hover {
    transition: 0.3s;
    filter: grayscale(70%);
    opacity: 0.7;
  }
`;

const StarContainer = styled.div`
  display: flex;
  flex-direction: row;
  .starIcon {
    color: #ffce31;
    padding-bottom: 2px;
  }
`;

const TitleContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  margin-top: 4px;
  padding-bottom: 8px;
`;

const PlaceName = styled.div`
  width: 100%;
  font-weight: 500;
  font-size: 17px;
  word-break: keep-all;
`;

const PlaceAddress = styled.div`
  width: 100%;
  font-size: 13px;
  color: #aaaaaa;
  padding-bottom: 12px;
`;

const PlaceScore = styled.div`
  margin-top: 1px;
  margin-left: 3px;
  font-family: inherit;
`;

const PlaceCharge = styled.div`
  text-align: right;
  width: 100%;
  color: #eb7470;
  font-weight: bold;
`;

export default Place;
