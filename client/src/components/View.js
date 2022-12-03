import React from 'react';
import { useRecoilValue } from 'recoil';
import styled from 'styled-components';

import { DetailInformation } from '../atoms';
import FadeCarousel from './Fade';
import Location from './Map';

function View() {
  const detailData = useRecoilValue(DetailInformation);

  const { title, category, detailInfo, address, phoneNumber } = detailData;

  return (
    <ViewContainer>
      <InformationContainer>
        <InformationTitle>{title && title}</InformationTitle>
        <CarouselImageContainer>
          <FadeCarousel />
        </CarouselImageContainer>
        <DetailTitle>상세 정보</DetailTitle>
        <DetailTagContainer>
          {category &&
            category.map(placeTag => {
              return <DetailTag key={placeTag}>{placeTag}</DetailTag>;
            })}
        </DetailTagContainer>
        <MoreInformation>{detailInfo && detailInfo}</MoreInformation>
        <NormalStyleDetailTitle>위치</NormalStyleDetailTitle>
        <AddressInfo>{address}</AddressInfo>
        <InformationLocation address={address && address} />
        <NormalStyleDetailTitle>호스트 연락처</NormalStyleDetailTitle>
        <DetailPhoneNumber>{phoneNumber && phoneNumber}</DetailPhoneNumber>
      </InformationContainer>
    </ViewContainer>
  );
}

export default View;

const ViewContainer = styled.div`
  margin-top: 80px;
  margin: auto;
  width: 900px;
  padding-bottom: 10px;
`;

const InformationContainer = styled.div`
  margin-top: 140px;
  width: 900px;
  height: auto;
  margin-right: auto;
  padding-left: 15px;
`;

const InformationTitle = styled.div`
  font-size: 2rem;
  font-weight: 500;
  margin: 10px 0px 5px 20px;
`;

const CarouselImageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 800px;
  margin-left: 20px;
  padding: 0px;
  flex-direction: row;
`;

const DetailTitle = styled.div`
  color: black;
  margin-top: 80px;
  padding: 15px 0px;
  font-size: 1.3rem;
  font-weight: 500;
  margin-left: 20px;
  width: 800px;
`;

const NormalStyleDetailTitle = styled(DetailTitle)`
  border: none;
  margin-top: 40px;
`;

const DetailTagContainer = styled.div`
  margin-left: 20px;
  width: 800px;
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
  border-radius: 13px;
  color: #ffffff;
  background-color: #eb7470;
  padding-top: 8px;
  padding-left: 10px;
  padding-bottom: 8px;
  padding-right: 10px;
  font-size: 0.65rem;
  font-weight: 500;
  margin-bottom: 10px;
  margin-top: 15px;
`;

const MoreInformation = styled.div`
  color: #2b2b2b;
  margin-left: 20px;
  width: 800px;
  font-size: 0.85rem;
  font-weight: 500;
  line-height: 22px;
  white-space: pre-wrap;
`;

const AddressInfo = styled.div`
  color: #2b2b2b;
  margin-left: 20px;
  width: 800px;
  font-size: 0.85rem;
  font-weight: 500;
  line-height: 22px;
  white-space: pre-wrap;
`;

const InformationLocation = styled(Location)`
  width: 800px;
  height: 200px;
`;

const DetailPhoneNumber = styled(MoreInformation)`
  font-size: 0.85rem;
  margin-bottom: 50px;
`;
