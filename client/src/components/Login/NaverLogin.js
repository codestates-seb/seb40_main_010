import React from 'react';
import styled from 'styled-components';
import { SiNaver } from 'react-icons/si';

function NaverLogin() {
  const NAVER_AUTH_URL = `${process.env.REACT_APP_SERVER_BASE_URL}/oauth2/authorization/naver`;

  return (
    <Container>
      <a href={NAVER_AUTH_URL}>
        <button type="button" className="naver-button">
          <SiNaver className="naverLogo" />
        </button>
      </a>
    </Container>
  );
}

const Container = styled.div`
  a {
    text-decoration: none;
  }
  .naver-button {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 3rem;
    height: 3rem;
    background-color: #2db400;
    border-radius: 40px;
    border: none;
    color: #2b2b2b;
    font-weight: 600;
    font-family: system ui;

    .naverLogo {
      height: 20px;
      padding-bottom: 1.2px;
      color: white;
      font-size: 1rem;
    }

    &:hover {
      cursor: pointer;
    }
  }
`;

export default NaverLogin;
