import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useRecoilState } from 'recoil';

// import axios from 'axios';
import Category from '../components/Category';
import Nav from '../components/Navigation/Nav';
import Place from '../components/Place';
import { mainDataState, mbtiPlaceDataState } from '../atoms';
import getData from '../hooks/useAsyncGetData';

// import { getAllPlaces } from '../hooks/getAllPlaces';

// ToDo : Mbti 컴포넌트 위치, 요청
export default function Places() {
  const [mainPlaceData, setMainPlaceData] = useRecoilState(mainDataState);
  const [mbtiPlaceData, setMbtiPlaceData] = useRecoilState(mbtiPlaceDataState);
  // const mainPlaceData = useRecoilValue(mainDataState);
  // const header = {
  //   headers: {
  //     'ngrok-skip-browser-warning': '010',
  //     Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
  //     RefreshToken: localStorage.getItem('REFRESH'),
  //   },
  // };
  // const get = async () => {
  //   const query = '?size=20&page=2';
  //   try {
  //     const result = await axios.get(`/home/${query}`, header);
  //     console.log(result.data);
  //     setMainPlaceData(result.data.data);
  //   } catch (error) {
  //     console.log(error);
  //   }
  // };

  // const query = '?size=20&page=2';
  // const url = `/home${query}`;
  const url = '/home';

  const displayPlaces = async () => {
    try {
      const getAllPlaces = getData(url);
      setMainPlaceData([...getAllPlaces.data.data]);

      const getMbtiPlaces = getData('/mbti');
      setMbtiPlaceData([...getMbtiPlaces.data.data]);
    } catch (error) {
      console.log(error);
    }
  };
  // const getMbtiPlace = async () => {
  //   try {
  //     const result = await axios.get('/mbti', header);
  //     console.log(result.data.data);
  //     setMbtiPlaceData(result.data.data);
  //   } catch (error) {
  //     console.log(error);
  //   }
  // };

  useEffect(() => {
    // get();
    // getMbtiPlace();
    displayPlaces();
  }, []);

  // console.log(mainPlaceData);
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
              return <Place key={placeId} placeData={placeData} />;
            })}
        </MainComponentContainer>
        <div>장소</div>
        <MainComponentContainer>
          {mainPlaceData &&
            mainPlaceData.map(placeData => {
              const { placeId } = placeData;
              return <Place key={placeId} placeData={placeData} />;
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
