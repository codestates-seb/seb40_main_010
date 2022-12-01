import React from 'react';
import { useRecoilValue } from 'recoil';
import styled from 'styled-components';

import { DetailInformation } from '../atoms';
import FadeCarousel from './Fade';
import Location from './Map';

function View() {
  const detailData = useRecoilValue(DetailInformation);

  const {
    title,
    // filePath: images,
    category,
    detailInfo,
    address,
    phoneNumber,
  } = detailData;

  return (
    <ViewContainer>
      <InformationContainer>
        <InformationTitle>{title && title}</InformationTitle>
        <CarouselImageContainer>
          <FadeCarousel />
        </CarouselImageContainer>
        {/* <InformationMiniImageContainer>
          {images &&
            images.map(placeImage => {
              return <InformationMiniImage key={placeImage} src={placeImage} />;
            })}
        </InformationMiniImageContainer> */}
        <DetailTitle>상세 정보</DetailTitle>
        <DetailTagContainer>
          {category &&
            category.map(placeTag => {
              return <DetailTag key={placeTag}>{placeTag}</DetailTag>;
            })}
        </DetailTagContainer>
        <MoreInformation>{detailInfo && detailInfo}</MoreInformation>
        <NormalStyleDetailTitle>위치</NormalStyleDetailTitle>
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
  /* width: 720px; */
  width: 900px;
  padding-bottom: 10px;
`;

// const Dummy = styled.div`
//   padding-left: 15px;
// `;

const InformationContainer = styled.div`
  margin-top: 140px;
  /* width: 600px; */
  width: 900px;
  height: auto;
  margin-right: auto;
  padding-left: 15px;
`;

const InformationTitle = styled.div`
  font-size: 2rem;
  font-weight: 500;
  margin: 10px 0px 5px 20px;
  /* margin-left: 20px; */
`;

// const InformationMiniImageContainer = styled.div`
//   display: flex;
//   flex-direction: row;
//   justify-content: center;
//   margin-left: 20px;
//   margin-top: 10px;
//   margin-bottom: 15px;
//   width: 600px;
// `;

// const InformationMiniImage = styled.img`
//   width: 40px;
//   height: 40px;
//   border-radius: 50px;
//   margin: 5px;
//   /* box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px; */
// `;

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
  /* border-top: 1px solid #c9c9c9; */
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
  /* box-shadow: rgba(0, 0, 0, 0.35) 3px 3px 3px; */
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

const InformationLocation = styled(Location)`
  width: 800px;
  height: 200px;
`;

const DetailPhoneNumber = styled(MoreInformation)`
  font-size: 0.85rem;
  margin-bottom: 50px;
`;
