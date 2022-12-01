import React from 'react';
import styled from 'styled-components';
import Slider from 'react-slick';
import { useRecoilValue } from 'recoil';

import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

import { DetailInformation } from '../atoms';
import { carouselSettings } from '../utils/carouselSetting';

function Fade() {
  const detailInformation = useRecoilValue(DetailInformation);

  const { filePath: images } = detailInformation;

  const settings = carouselSettings;

  return (
    <div>
      <StyledSlide {...settings}>
        {images &&
          images.map(el => {
            return (
              <SlickImageContainer key={el}>
                <SlickImage src={el} />
              </SlickImageContainer>
            );
          })}
      </StyledSlide>
    </div>
  );
}

export default Fade;

const StyledSlide = styled(Slider)`
  width: 800px;
  height: 800px;
  .slick-slider {
    z-index: -1;
  }
  ul {
    width: 800px;
    position: absolute;
    bottom: -5%;
  }

  .slick-dots li button:before {
    font-family: 'slick';
    font-size: 15px;
    line-height: 20px;

    position: absolute;
    top: 0;
    left: 0;

    width: 20px;
    height: 20px;

    content: '•';
    text-align: center;

    opacity: 0.35;
    color: #2b2b2b;

    -webkit-font-smoothing: antialiased;
    -moz-osx-font-smoothing: grayscale;
  }
  .slick-dots li.slick-active button:before {
    opacity: 0.75;
    color: #89bbff;
  }
  .slick-list {
    width: 800px;
    height: 800px;
    margin-top: 20px;
    overflow: hidden;
  }
  .slick-slide img {
    margin: 0px;
  }
  .slick-track {
    display: flex;
    height: 100%;
  }
`;

const SlickImage = styled.img`
  width: 800px;
  height: 800px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
`;
const SlickImageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 800px;
`;
