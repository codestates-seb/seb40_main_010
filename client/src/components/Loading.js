import React from 'react';
import styled, { keyframes } from 'styled-components';

function Loading() {
  return (
    <Container>
      <Wrapper>
        <LoadingTitle>Loading...</LoadingTitle>
        <ProgressBar>
          <ProgressBarGauge />
        </ProgressBar>
      </Wrapper>
    </Container>
  );
}

export default Loading;

const Container = styled.div`
  width: 100vw;
  height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
`;

const Wrapper = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  width: 500px;
  height: 216px;
`;

const flicker = keyframes`
from{
        opacity: 1
    }
    to{
        opacity: 0
    }
`;
const loadingBar = keyframes`
    0%{
        width: 0;
        opacity: 1;
    }
    80%{
        width: 100%;
        opacity: 1;
    }
    100%{
        width: 100%;
        opactiy: 0;
    }
`;

const LoadingTitle = styled.h1`
  margin-bottom: 20px;
  text-align: center;
  font-size: 1.5rem;
  line-height: 1.333;
  color: #2b2b2b;
  animation-name: ${flicker};
  animation-duration: 1600ms;
  animation-iteration-count: infinite;
  animation-direction: alternate;
`;

const ProgressBar = styled.div`
  position: relative;
  width: 300px;
  height: 12px;
  border-radius: 100px;
  background-color: rgb(167, 167, 167);
  overflow: hidden;
`;

const ProgressBarGauge = styled.span`
  position: absolute;
  top: 0;
  left: 0;
  width: 20px;
  height: 12px;
  border-radius: 100px;
  background-color: #eb7470;
  animation-name: ${loadingBar};
  animation-duration: 1600ms;
  animation-iteration-count: infinite;
  animation-direction: ease-out;
`;
