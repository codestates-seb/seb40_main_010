import React from 'react';
import styled from 'styled-components';

export default function NotFound() {
  return <InVaild>404 Not Found</InVaild>;
}

const InVaild = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 20vh;
  font-weight: 700;
  font-size: 4rem;
`;
