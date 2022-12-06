import React from 'react';
import styled from 'styled-components';

function GoogleLogin() {
  const GOOGLE_AUTH_URL = `${process.env.REACT_APP_SERVER_BASE_URL}/oauth2/authorization/google`;

  return (
    <Container>
      <a href={GOOGLE_AUTH_URL}>
        <button type="button" className="google-button">
          <img
            src="/btn_google_signin_light_focus_web.png"
            alt="google login"
          />
        </button>
      </a>
    </Container>
  );
}

const Container = styled.div`
  .google-button {
    background-repeat: no-repeat;
    background-color: transparent;
    background-size: cover;
    background-position: 0px 0px;
    border: none;
    padding: 0px;

    :hover {
      cursor: pointer;
    }
  }

  img {
    /* width: 15rem; */
    /* height: 2rem; */
  }
`;

export default GoogleLogin;
