import React from 'react';
import styled from 'styled-components';

export default function NotFound() {
  return <InValid>404 Not Found</InValid>;
}

const InValid = styled.div`
  display: flex;
  justify-content: center;
  margin-top: 20vh;
  font-weight: 700;
  font-size: 4rem;
`;
