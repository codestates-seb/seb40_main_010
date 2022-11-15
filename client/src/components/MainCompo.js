import React from 'react';
import styled from 'styled-components';
import { ImStarFull } from 'react-icons/im';

function MainCompo() {
  return (
    <MainComponent>
      <Image src="https://a0.muscache.com/im/pictures/miso/Hosting-46566256/original/7225b27d-fb5d-431b-9fc4-40be31efea23.jpeg?im_w=1200" />
      <TitleContainer>
        <PlaceName>인테리어가 아름다운 리빙형 대관 공간</PlaceName>
        <ImStarFull style={{ color: '#ffce31' }} />
        <PlaceScore>4.8</PlaceScore>
      </TitleContainer>
      <PlaceAddress>경기도 남양주시</PlaceAddress>
      <PlaceCharge>30,000원</PlaceCharge>
    </MainComponent>
  );
}

const MainComponent = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: flex-start;
  width: 300px;
  border: 1px solid green;
  padding: 10px;
`;

const Image = styled.img`
  width: 280px;
  height: 280px;
  border-radius: 15px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  margin-bottom: 10px;
`;

const TitleContainer = styled.div`
  display: flex;
  flex-direction: row;
  padding: 0 10px;
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
`;

const PlaceScore = styled.div`
  margin-left: 8px;
`;

const PlaceCharge = styled.div`
  text-align: right;
  width: 100%;
  color: #eb7470;
  font-weight: bold;
`;

export default MainCompo;
