import React from 'react';
import styled from 'styled-components';
import Nav from '../components/Navigation/Nav';
import MyPageComponent from '../components/MyPageComponents/MyPageComponent';

function MyPage() {
  return (
    <Container>
      <Nav />
      <MyPageComponent />
    </Container>
  );
}

const Container = styled.div`
  width: 100vw;
  height: 100vh;
  background-color: #f9f9f9;
  position: absolute;
  overflow: scroll;

  ::-webkit-scrollbar {
    display: none;
  }
`;

export default MyPage;
