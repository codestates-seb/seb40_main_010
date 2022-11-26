import React from 'react';
// import axios from 'axios';
import styled from 'styled-components';
// import { useRecoilState, useRecoilValue } from 'recoil';

// import { DetailInformation, PlaceIDState } from '../atoms';
import FadeCarousel from './Fade';
import Location from './Map';

function View({ detailInformation }) {
  return (
    <ViewContainer>
      <InformationContainer>
        <InformationTitle>{detailInformation.title}</InformationTitle>
        <CarouselImageContainer>
          <FadeCarousel />
        </CarouselImageContainer>
        <InformationMiniImageContainer>
          {detailInformation.filePath &&
            detailInformation.filePath.map(placeImage => {
              return <InformationMiniImage key={placeImage} src={placeImage} />;
            })}
        </InformationMiniImageContainer>
        <DetailTitle>상세 정보</DetailTitle>
        <DetailTagContainer>
          {detailInformation.category &&
            detailInformation.category.map(placeTag => {
              return <DetailTag key={placeTag}>{placeTag}</DetailTag>;
            })}
        </DetailTagContainer>
        <MoreInformation>{detailInformation.detailInfo}</MoreInformation>
        <NormalStyleDetailTitle>위치</NormalStyleDetailTitle>
        <InformationLocation address={detailInformation.address} />
        <NormalStyleDetailTitle>호스트 연락처</NormalStyleDetailTitle>
        <DetailPhoneNumber>{detailInformation.phoneNumber}</DetailPhoneNumber>
      </InformationContainer>
    </ViewContainer>
  );
}

export default View;

const ViewContainer = styled.div`
  margin-top: 80px;
  margin: auto;
  width: 720px;
  padding-bottom: 10px;
`;

const InformationContainer = styled.div`
  margin-top: 100px;
  width: 600px;
  height: auto;
  margin-right: auto;
  padding-left: 15px;
`;

const InformationTitle = styled.div`
  font-size: 2rem;
  font-weight: 600;
  /* font-weight: 17.3%; */
  margin-left: 20px;
`;

const InformationMiniImageContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  margin-left: 20px;
  margin-bottom: 15px;
  width: 600px;
`;

const InformationMiniImage = styled.img`
  width: 50px;
  height: 50px;
  border-radius: 5px;
  margin: 5px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
`;

const CarouselImageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 600px;
  margin-left: 20px;
  padding: 0px;
  flex-direction: row;
`;

const DetailTitle = styled.div`
  border-top: 2px solid #89bbff;
  padding: 15px 0px;
  font-size: 1.1rem;
  font-weight: 600;
  margin-left: 20px;
  width: 600px;
`;

const NormalStyleDetailTitle = styled(DetailTitle)`
  border: none;
`;

const DetailTagContainer = styled.div`
  margin-left: 20px;
  width: 600px;
  display: flex;
  flex-wrap: wrap;
  display: flex;
  flex-direction: row;
  align-items: center;
`;

const DetailTag = styled.div`
  white-space: nowrap;
  text-align: center;
  margin-right: 10px;
  width: auto;
  border-radius: 15px;
  color: #ffffff;
  background-color: #eb7470;
  padding-top: 9px;
  padding-left: 8px;
  padding-bottom: 8px;
  padding-right: 8px;
  font-size: 0.7rem;
  font-weight: 400;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
  margin-bottom: 20px;
`;

const MoreInformation = styled.div`
  margin-left: 20px;
  width: 600px;
  font-size: 0.9rem;
  font-weight: 400;
  line-height: 22px;
`;

const InformationLocation = styled(Location)`
  width: 600px;
  height: 200px;
`;

const DetailPhoneNumber = styled(MoreInformation)`
  font-size: 1rem;
  margin-bottom: 20px;
`;
