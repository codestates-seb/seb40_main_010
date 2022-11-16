import React, { useEffect, useState } from 'react';
import Slider from 'react-slick';
import 'slick-carousel/slick/slick.css';
import 'slick-carousel/slick/slick-theme.css';
import styled from 'styled-components';

const StyledSlide = styled(Slider)`
  width: 500px;
  height: 500px;
  .slick-slider {
    z-index: -1;
  }
  ul {
    width: 200px;
    position: absolute;
    right: 160px;
    top: 470px;
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
    width: 500px;
    height: 500px;
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
  left: -6%;
  z-index: 3;
  &::before {
    color: black;
  }
`;

const NextTo = styled.div`
  width: 30px;
  height: 30px;
  position: absolute;
  right: -8%;
  z-index: 3;
  &::before {
    color: black;
  }
`;

const SlickImage = styled.img`
  width: 500px;
  height: 500px;
  border-radius: 5px;
  box-shadow: rgba(0, 0, 0, 0.35) 0px 5px 15px;
`;
const SlickImageDiv = styled.div`
  display: flex;
  justify-content: center;
  align-items: center;
  width: 600px;
`;

function Fade(miniInformationImage) {
  // console.log();
  const [imageArr, setImageArr] = useState([]);
  useEffect(() => {
    const { data } = { ...miniInformationImage };
    setImageArr(data);
    // console.log(data);
  }, [miniInformationImage]);
  const settings = {
    dots: true,
    slide: <SlickImageDiv />,
    dotsClass: 'slick-dots',
    fade: true,
    infinite: true,
    arrows: true,
    autoplay: true,
    autoplaySpeed: 500,
    speed: 5000,
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
        {imageArr.map(el => {
          return (
            <SlickImageDiv key={el.id}>
              <SlickImage src={el.download_url} />
            </SlickImageDiv>
          );
        })}
      </StyledSlide>
    </div>
  );
}

export default Fade;
