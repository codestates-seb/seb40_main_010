import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useRecoilState } from 'recoil';
import axios from 'axios';

import Place from './Place';
import { HasRefresh, mbtiPlaceDataState } from '../atoms';
// import header from '../utils/header';
// import getData from '../hooks/useAsyncGetData';

function MbtiPlaces() {
  const [mbtiPlaceData, setMbtiPlaceData] = useRecoilState(mbtiPlaceDataState);
  const [isLogIn] = useRecoilState(HasRefresh);

  // const header = {
  //   headers: {
  //     Authorization: localStorage.getItem('ACCESS')
  //       ? `Bearer ${localStorage.getItem('ACCESS')}`
  //       : '',
  //     RefreshToken: localStorage.getItem('REFRESH')
  //       ? localStorage.getItem('REFRESH')
  //       : '',
  //   },
  // };

  // eslint-disable-next-line consistent-return
  const setMbtiPlaces = async () => {
    try {
      const getMbtiPlaces = await axios.get('/mbti', {
        headers: {
          'ngrok-skip-browser-warning': '010',
          Authorization: (await localStorage.getItem('ACCESS'))
            ? `Bearer ${localStorage.getItem('ACCESS')}`
            : '',
          RefreshToken: (await localStorage.getItem('REFRESH'))
            ? localStorage.getItem('REFRESH')
            : '',
        },
      });
      if (!getMbtiPlaces.data.data) {
        setMbtiPlaceData([]);
      }
      if (getMbtiPlaces.data.data) {
        setMbtiPlaceData([...getMbtiPlaces.data.data]);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    // setTimeout(() => {
    setMbtiPlaces();
    // }, 2000);
  }, [isLogIn]);

  return (
    <MbtiContentsContainer>
      {mbtiPlaceData.length > 0 && (
        <>
          <MbtiTitle>
            나와 같은 mbti유형의 사람들이 예약한 장소 best 4
          </MbtiTitle>
          <MbtiPlacesContainer>
            {mbtiPlaceData.map(placeData => {
              const { placeId } = placeData;
              return <Place key={`mbti ${placeId}`} placeData={placeData} />;
            })}
          </MbtiPlacesContainer>
        </>
      )}
    </MbtiContentsContainer>
  );
}

export default MbtiPlaces;

const MbtiTitle = styled.div`
  margin-top: 10px;
  margin-left: 10px;
  margin-bottom: 20px;
`;

const MbtiContentsContainer = styled.div`
  display: flex;
  flex-direction: column;
`;

const MbtiPlacesContainer = styled.div`
  margin: 1 auto;
  //rem
  /* background-color: #c9c9c9; */
  width: 1200px;
  flex-wrap: wrap;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-content: flex-start;
  align-items: center;
  border-radius: 15px;
  margin-bottom: 100px;
`;
