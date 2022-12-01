import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useRecoilState } from 'recoil';
import axios from 'axios';

import Place from './Place';
import { HasRefresh, mbtiPlaceDataState } from '../atoms';

function MbtiPlaces() {
  const [mbtiPlaceData, setMbtiPlaceData] = useRecoilState(mbtiPlaceDataState);
  const [isLogIn] = useRecoilState(HasRefresh);
  // const [userMbti,setUserMbti] = useRecoilState(userMbtiValue)

  // eslint-disable-next-line consistent-return
  const setMbtiPlaces = async () => {
    if (!isLogIn) return setMbtiPlaceData([]);

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
    setMbtiPlaces();
  }, [isLogIn]);

  return (
    <MbtiContentsContainer>
      {mbtiPlaceData.length > 0 && (
        <>
          {/* <MbtiTitle>{userMbti}가 방문한 장소</MbtiTitle> */}
          <MbtiTitle>
            <span>Mbti</span>가 방문한 장소
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
  font-weight: 500;
  font-size: 1.2rem;
  margin-top: 30px;
  margin-left: 10px;
  margin-bottom: 20px;
  & > span {
    color: #96c2ff;
    padding-right: 3px;
  }
`;

const MbtiContentsContainer = styled.div`
  display: flex;
  flex-direction: column;
`;

const MbtiPlacesContainer = styled.div`
  //rem
  /* box-shadow: rgba(50, 50, 93, 0.25) 0px 2px 5px -1px,
    rgba(0, 0, 0, 0.3) 0px 1px 3px -1px; */
  width: 1230px;
  flex-wrap: wrap;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-content: flex-start;
  align-items: center;
  border-radius: 15px;
  margin-bottom: 50px;
`;
