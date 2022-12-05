import styled from 'styled-components';

export const Pre = styled.div`
  width: 30px;
  height: 30px;
  position: absolute;
  left: 2%;
  z-index: 3;
  &::before {
    color: white;
  }
`;

export const NextTo = styled.div`
  width: 30px;
  height: 30px;
  position: absolute;
  right: 0%;
  z-index: 3;
  &::before {
    color: white;
  }
`;
