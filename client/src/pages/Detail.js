import React from 'react';
import styled from 'styled-components';
import ReservationAsideBar from '../components/ReservationAsideBar';
import Nav from '../components/Nav';
import View from '../components/View';

const DetailContainer = styled.div`
  display: flex;
  flex-direction: row;
`;
const DetailViewContainer = styled.div``;
function Detail() {
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
