import React from 'react';
import styled from 'styled-components';

function KakaoLogin() {
  const KAKAO_AUTH_URL = `${process.env.REACT_APP_SERVER_BASE_URL}/oauth2/authorization/kakao`;
  return (
    <Container>
      <a href={KAKAO_AUTH_URL}>
        <button type="button" className="kakao-button">
          <img
            className="kakao"
            alt="kakao"
            src="https://images-ext-2.discordapp.net/external/sVYggvT-UJIu06acazxnhxbW0TMzlcXQIKIutnnfdqQ/%3F20151013114543/https/upload.wikimedia.org/wikipedia/commons/thumb/d/dd/Kakao_Corp._symbol_-_2012.svg/100px-Kakao_Corp._symbol_-_2012.svg.png"
          />
        </button>
      </a>
    </Container>
  );
}

const Container = styled.div`
  a {
    text-decoration: none;
  }

  .kakao-button {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 3rem;
    height: 3rem;
    background-color: #fee500;
    border-radius: 40px;
    border: none;
    color: #2b2b2b;
    font-weight: 600;
    font-family: system ui;

    .kakao {
      height: 18px;
      padding-bottom: 1.2px;
    }

    &:hover {
      cursor: pointer;
    }
  }
`;

export default KakaoLogin;
