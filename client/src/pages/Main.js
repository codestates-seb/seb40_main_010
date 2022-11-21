import axios from 'axios';
import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useRecoilState } from 'recoil';
import Category from '../components/Category';
import Nav from '../components/Nav';
import MainCompo from '../components/MainCompo';
import { mainDataState } from '../atoms';

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

// ToDo : Mbti 컴포넌트 위치, 요청
function Main() {
  const [mainPlaceData, setMainPlaceData] = useRecoilState(mainDataState);

  useEffect(() => {
    axios
      .get('http://localhost:3001/maindata/')
      .then(res => {
        setMainPlaceData([...res.data[0].data]);
      })
      .catch(err => console.log(err));
  }, []);

  return (
    <MainContainer>
      <Nav />
      <Category />
      <DisplayComponentDiv>
        <MainComponentContainer>
          {mainPlaceData.map(placeData => {
            return (
              <MainCompo
                key={placeData.placeId}
                placeData={placeData}
                // id={placeData.placeId}
              />
            );
          })}
        </MainComponentContainer>
      </DisplayComponentDiv>
    </MainContainer>
  );
}

export default Main;
