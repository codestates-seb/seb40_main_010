import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useRecoilState } from 'recoil';

import axios from 'axios';
import Category from '../components/Category';
import Nav from '../components/Navigation/Nav';
import Place from '../components/Place';
import { mainDataState, mbtiPlaceDataState } from '../atoms';
// import { getAllPlaces } from '../hooks/getAllPlaces';

// ToDo : Mbti 컴포넌트 위치, 요청
export default function Places() {
  const [mainPlaceData, setMainPlaceData] = useRecoilState(mainDataState);
  const [mbtiPlaceData, setMbtiPlaceData] = useRecoilState(mbtiPlaceDataState);
  // const mainPlaceData = useRecoilValue(mainDataState);

  const header = {
    headers: {
      'ngrok-skip-browser-warning': '010',
    },
  };
  const get = async () => {
    try {
      const result = await axios.get('/home', header);
      // console.log(result.data.data);
      setMainPlaceData(result.data.data);
    } catch (error) {
      console.log(error);
    }
  };
  const getMbtiPlace = async () => {
    try {
      const result = await axios.get('/mbti', header);
      console.log(result.data.data);
      setMbtiPlaceData(result.data.data);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    get();
    getMbtiPlace();
  }, []);

  // console.log(mainPlaceData);
  return (
    <MainContainer>
      <Nav />
      <Category />
      <DisplayComponentDiv>
        <MainComponentContainer>
          {mbtiPlaceData.map(placeData => {
            return <Place key={placeData.title} placeData={placeData} />;
          })}
        </MainComponentContainer>
        <MainComponentContainer>
          {mainPlaceData.map(placeData => {
            return <Place key={placeData.placeId} placeData={placeData} />;
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
