import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useRecoilState } from 'recoil';

// import axios from 'axios';
import Category from '../components/Category';
import Nav from '../components/Navigation/Nav';
import Place from '../components/Place';
import { mainDataState, mbtiPlaceDataState } from '../atoms';
import getData from '../hooks/useAsyncGetData';
// import header from '../utils/header';

// ToDo : Mbti 컴포넌트 위치, 요청
export default function Places() {
  const [mainPlaceData, setMainPlaceData] = useRecoilState(mainDataState);
  const [mbtiPlaceData, setMbtiPlaceData] = useRecoilState(mbtiPlaceDataState);

  const query = '?size=20&page=1';
  const url = `/home${query}`;
  // const url = '/home';

  const displayPlaces = async () => {
    const getAllPlaces = await getData(url);
    // const get
    // const getAllPlaces = await axios.get(url, header);

    setMainPlaceData([...getAllPlaces.data.data]);

    const getMbtiPlaces = await getData('/mbti');

    setMbtiPlaceData([...getMbtiPlaces.data.data]);
  };

  useEffect(() => {
    displayPlaces();
  }, []);

  return (
    <MainContainer>
      <Nav />
      <Category />
      <DisplayComponentDiv>
        <div>MBTI 추천</div>
        <MainComponentContainer>
          {mbtiPlaceData &&
            mbtiPlaceData.map(placeData => {
              const { placeId } = placeData;
              return <Place key={`mbti ${placeId}`} placeData={placeData} />;
            })}
        </MainComponentContainer>
        <div>장소</div>
        <MainComponentContainer>
          {mainPlaceData &&
            mainPlaceData.map(placeData => {
              // const { endTime } = placeData;
              const { placeId } = placeData;
              // console.log(placeId);
              return <Place key={`main ${placeId}`} placeData={placeData} />;
            })}
        </MainComponentContainer>
      </DisplayComponentDiv>
    </MainContainer>
  );
}

const DisplayComponentDiv = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;
const MainComponentContainer = styled.div`
  margin: 1 auto;
  width: 1200px;
  flex-wrap: wrap;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-content: flex-start;
  align-items: center;
`;

const MainContainer = styled.div`
  height: 100vh;
  width: 100vw;
`;
