import React from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';
import { Link } from 'react-router-dom';
import { useSetRecoilState } from 'recoil';
import { PlaceIDState } from '../atoms';

function MainCompo({ placeData }) {
  const setFocusPlaceID = useSetRecoilState(PlaceIDState);

  const { address, charge, image, score, title, placeId } = placeData;

  const onClickPlaceComponent = () => {
    setFocusPlaceID(placeId);
  };

  return (
    <MainContainer onClick={e => e.stopPropagation()}>
      <Link to="/detail">
        <MainComponent onClick={onClickPlaceComponent}>
          <Image src={image} />
          <TitleContainer>
            <PlaceName>{title.slice(0, 15)}</PlaceName>
            <ImStarFull className="starIcon" />
            <PlaceScore>{score}</PlaceScore>
          </TitleContainer>
          <PlaceAddress>{address}</PlaceAddress>
          <PlaceCharge>{charge}원</PlaceCharge>
        </MainComponent>
      </Link>
    </MainContainer>
  );
}

const MainContainer = styled.div`
  a {
    text-decoration: none;
    color: #2b2b2b;
  }
`;

const MainComponent = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  width: 280px;
  /* border: 1px solid green; */
  padding: 10px;
  z-index: 10px;
`;

const Image = styled.img`
  width: 280px;
  height: 280px;
  border-radius: 15px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  margin-bottom: 10px;
`;

const TitleContainer = styled.div`
  width: 100%;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 6px;
  .starIcon {
    color: #ffce31;
    padding-bottom: 2px;
  }
`;

const PlaceName = styled.div`
  width: 83%;
  font-weight: bold;
  font-size: 15px;
  word-break: keep-all;
`;

const PlaceAddress = styled.div`
  width: 100%;
  font-size: 15px;
  padding-bottom: 5px;
`;

const PlaceScore = styled.div`
  margin-top: 1px;
`;

const PlaceCharge = styled.div`
  text-align: right;
  width: 100%;
  color: #eb7470;
  font-weight: bold;
`;

export default MainCompo;
