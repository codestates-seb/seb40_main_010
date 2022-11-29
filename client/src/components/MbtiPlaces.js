import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useRecoilState } from 'recoil';

import Place from './Place';
import { mbtiPlaceDataState } from '../atoms';
import getData from '../hooks/useAsyncGetData';

function MbtiPlaces() {
  const [mbtiPlaceData, setMbtiPlaceData] = useRecoilState(mbtiPlaceDataState);

  // eslint-disable-next-line consistent-return
  const setMbtiPlaces = async () => {
    try {
      const getMbtiPlaces = await getData('/mbti');

      if (getMbtiPlaces.data.data) {
        setMbtiPlaceData([...getMbtiPlaces.data.data]);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    setMbtiPlaces();
  }, []);

  return (
    <MbtiContainer>
      {mbtiPlaceData.length > 0 && (
        <>
          <MbtiTitle>나와 같은 mbti유형의 사람들이 예약한 장소 best4</MbtiTitle>
          <MainComponentContainer>
            {mbtiPlaceData.map(placeData => {
              const { placeId } = placeData;
              return <Place key={`mbti ${placeId}`} placeData={placeData} />;
            })}
          </MainComponentContainer>
        </>
      )}
    </MbtiContainer>
  );
}

export default MbtiPlaces;

const MbtiTitle = styled.div`
  margin-left: 10px;
`;

const MbtiContainer = styled.div`
  display: flex;
  flex-direction: column;
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
