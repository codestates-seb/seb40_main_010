import React from 'react';
import styled from 'styled-components';
import Nav from '../components/Navigation/Nav';
import MyPageComponent from '../components/MyPageComponent';

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
  background-color: #ffda77;
  display: flex;
  justify-content: center;
  align-items: center;
`;

export default MyPage;
