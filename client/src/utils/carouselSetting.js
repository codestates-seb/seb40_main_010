import React from 'react';

import { NextTo, Pre } from '../components/carouselButton';

export const carouselSettings = {
  dots: true,
  lazyLoad: true,
  dotsClass: 'slick-dots',
  fade: true,
  infinite: true,
  arrows: true,
  autoplay: true,
  autoplaySpeed: 2000,
  slidesToShow: 1,
  slidesToScroll: 1,
  pauseOnHover: true,
  centerMode: false,
  centerPadding: '0px',
  nextArrow: <NextTo>＞</NextTo>,
  prevArrow: <Pre>＜</Pre>,
};
