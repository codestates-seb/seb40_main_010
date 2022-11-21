import React from 'react';
// import { useEffect } from 'react'
import styled from 'styled-components';
// import axios from 'axios';
// import { useRecoilValue } from 'recoil';
import ReservationAsideBar from '../components/ReservationComponets/ReservationAsideBar';
import Nav from '../components/Nav';
import View from '../components/View';
// import { PlaceIDState } from '../atoms';

const DetailContainer = styled.div`
  display: flex;
  flex-direction: row;
  justify-content: center;
`;
const DetailViewContainer = styled.div``;
function Detail() {
  // const [detailInformation, setDetailInformation] =
  //   useRecoilState(DetailInformation);
  // const placeId = useRecoilValue(PlaceIDState);
  // useEffect(() => {
  //   axios
  //     .get(`{{BACKEND}}/place/${placeId}`)
  //     .then(res => console.log(res))
  //     .catch(err => console.log(err));
  // }, []); // > 실제 api

  return (
    <DetailContainer>
      <DetailViewContainer>
        <Nav />
        <View />
      </DetailViewContainer>
      <ReservationAsideBar />
    </DetailContainer>
  );
}

export default Detail;
