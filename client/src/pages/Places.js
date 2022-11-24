import React, { useEffect } from 'react';
import styled from 'styled-components';
// import { useRecoilState } from 'recoil';

import axios from 'axios';
import Category from '../components/Category';
import Nav from '../components/Navigation/Nav';
// import Place from '../components/Place';
// import { mainDataState } from '../atoms';
// import { getAllPlaces } from '../hooks/getAllPlaces';

// ToDo : Mbti 컴포넌트 위치, 요청
export default function Places() {
  // const [mainPlaceData, setMainPlaceData] = useRecoilState(mainDataState);
  // const mainPlaceData = useRecoilValue(mainDataState);

  // const header = {
  //   headers: {
  //     'ngrok-skip-browser-warning': 'skip',
  //   },
  // };
  const get = async () => {
    try {
      const result = await axios.get('/home');
      console.log(result.data);
      // setMainPlaceData(result);
    } catch (error) {
      console.log(error);
    }
  };
  useEffect(() => {
    get();
  }, []);

  // console.log(mainPlaceData);
  return (
    <MainContainer>
      <Nav />
      <Category />
      <DisplayComponentDiv>
        <MainComponentContainer>
          {/* {mainPlaceData.map(placeData => {
            return <Place key={placeData.placeId} placeData={placeData} />;
          })} */}
        </MainComponentContainer>
      </DisplayComponentDiv>
    </MainContainer>
  );
}

const DisplayComponentDiv = styled.div`
  display: flex;
  flex-direction: row;
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
