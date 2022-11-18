import axios from 'axios';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import Fade from './Fade';
import Location from './Map';

const TotalContainer = styled.div`
  margin-top: 80px;
  margin: auto;
  width: 900px;
  /* border: 1px solid teal; */
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

const detailTagName = [
  { num: 1, name: '#공유오피소' },
  { num: 2, name: '#파티룸' },
  { num: 3, name: '#계곡근처' },
  { num: 4, name: '#ESTP' },
];

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
  const address = '서울 송파구 올림픽로 240';
  const [detailInformation, setDetailInformation] = useState({});
  const [miniInformationImage, setMiniInformationImage] = useState({
    data: [],
  });
  const detailId = 1;
  useEffect(() => {
    axios
      .get(`https://koreanjson.com/posts/${detailId}`)
      .then(res => {
        const { id, title, content, createAt } = res.data;
        setDetailInformation({ id, title, content, createAt });
      })
      .catch(err => console.log(err));

    axios
      .get(`https://picsum.photos/v2/list?page=10&limit=4`)
      .then(res => {
        setMiniInformationImage({
          ...miniInformationImage,
          data: [...res.data],
        });
      })
      // .then(console.log(miniInformationImage))
      .catch(err => console.log(err));
  }, []);
  return (
    <TotalContainer>
      <InfoContainer>
        <InfoTitle>{detailInformation.title}</InfoTitle>
        <CarouselImageContainer>
          <Fade {...miniInformationImage} />
        </CarouselImageContainer>
        <InfoMiniImageContainer>
          {miniInformationImage.data.map(el => {
            return <InfoMiniImage key={el.id} src={el.download_url} />;
          })}
        </InfoMiniImageContainer>
        <DetailTitle>상세 정보</DetailTitle>
        <DetailTagContainer>
          {detailTagName.map(el => {
            return <DetailTag key={el.num}>{el.name}</DetailTag>;
          })}
        </DetailTagContainer>
        <DetailInfo>{detailInformation.content}</DetailInfo>
        <DetailNonStyleTitle>위치</DetailNonStyleTitle>
        <InfoLocation address={address} />
        <DetailNonStyleTitle>호스트 연락처</DetailNonStyleTitle>
        <DetailNumber>010-1234-5678</DetailNumber>
      </InfoContainer>
    </TotalContainer>
  );
}

export default View;
