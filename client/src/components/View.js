import axios from 'axios';
import React, { useEffect } from 'react';
import { useRecoilState } from 'recoil';
import styled from 'styled-components';
import { DetailInformation } from '../atoms';
import Fade from './Fade';
import Location from './Map';

const TotalContainer = styled.div`
  margin-top: 80px;
  margin: auto;
  width: 720px;
  border-bottom: 2px solid #89bbff;
  margin-bottom: 10px;
`;

const InfoContainer = styled.div`
  margin-top: 100px;
  width: 700px;
  height: auto;
  margin-right: auto;
  padding-left: 15px;
`;

const InfoTitle = styled.div`
  font-size: 1.3rem;
  font-weight: 600;
`;

const InfoMiniImageContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
  margin-left: 20px;
  margin-bottom: 15px;
  width: 500px;
`;

const InfoMiniImage = styled.img`
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
  width: 500px;
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

const DetailTagContainer = styled.div`
  margin-left: 20px;
  width: 600px;
  display: flex;
  flex-direction: row;
  align-items: center;
`;

const DetailTag = styled.div`
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

const DetailInfo = styled.div`
  margin-left: 20px;
  width: 600px;
  font-size: 0.9rem;
  font-weight: 400;
  line-height: 22px;
`;

const DetailNonStyleTitle = styled(DetailTitle)`
  border: none;
`;

const InfoLocation = styled(Location)`
  width: 600px;
  height: 200px;
`;

const DetailNumber = styled(DetailInfo)`
  font-size: 1rem;
  margin-bottom: 20px;
`;

function View() {
  const [detailInformation, setDetailInformation] =
    useRecoilState(DetailInformation);
  const callDetailData = async () => {
    await axios
      .get('http://localhost:3001/detaildata')
      .then(res => {
        setDetailInformation(...res.data);
        // setDetailInformation(...res.data);
      })
      .catch(err => console.log(err));
  };
  useEffect(() => {
    callDetailData();
  }, []);

  return (
    <TotalContainer>
      <InfoContainer>
        <InfoTitle>{detailInformation.title}</InfoTitle>
        <CarouselImageContainer>
          <Fade />
        </CarouselImageContainer>
        <InfoMiniImageContainer>
          {detailInformation.image &&
            detailInformation.image.map(el => {
              return <InfoMiniImage key={el} src={el} />;
            })}
        </InfoMiniImageContainer>
        <DetailTitle>상세 정보</DetailTitle>
        <DetailTagContainer>
          {detailInformation.category &&
            detailInformation.category.map(el => {
              return <DetailTag key={el}>{el}</DetailTag>;
            })}
        </DetailTagContainer>
        <DetailInfo>{detailInformation.detailInfo}</DetailInfo>
        <DetailNonStyleTitle>위치</DetailNonStyleTitle>
        <InfoLocation address={detailInformation.address} />
        <DetailNonStyleTitle>호스트 연락처</DetailNonStyleTitle>
        <DetailNumber>{detailInformation.phoneNumber}</DetailNumber>
      </InfoContainer>
    </TotalContainer>
  );
}

export default View;
