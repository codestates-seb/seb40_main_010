import React from 'react';
import styled from 'styled-components';
import Slider from 'react-slick';
import { useRecoilValue } from 'recoil';

import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';

import { DetailInformation } from '../atoms';

function Fade() {
  const detailInformation = useRecoilValue(DetailInformation);

  const settings = {
    dots: true,
    lazyLoad: true,
    dotsClass: 'slick-dots',
    fade: true,
    infinite: true,
    arrows: true,
    autoplay: true,
    autoplaySpeed: 5000,
    slidesToShow: 1,
    slidesToScroll: 1,
    pauseOnHover: true,
    centerMode: false,
    centerPadding: '0px',
    nextArrow: <NextTo>＞</NextTo>,
    prevArrow: <Pre>＜</Pre>,
  };

  return (
    <div>
      <StyledSlide {...settings}>
        {detailInformation.filePath &&
          detailInformation.filePath.map(el => {
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
  width: 600px;
  height: 600px;
  .slick-slider {
    z-index: -1;
  }
  ul {
    width: 600px;
    position: absolute;
    bottom: 3%;
    /* right: 0%; */
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
    width: 600px;
    height: 600px;
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

const Pre = styled.div`
  width: 30px;
  height: 30px;
  position: absolute;
  left: 2%;
  z-index: 3;
  &::before {
    color: white;
  }
`;

const NextTo = styled.div`
  width: 30px;
  height: 30px;
  position: absolute;
  right: 0%;
  z-index: 3;
  &::before {
    color: white;
  }
`;

const SlickImage = styled.img`
  width: 600px;
  height: 600px;
  border-radius: 5px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
`;
const SlickImageContainer = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 600px;
`;
