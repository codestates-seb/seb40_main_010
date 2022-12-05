import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useRecoilState, useSetRecoilState } from 'recoil';
import { useParams } from 'react-router-dom';
import axios from 'axios';

import {
  DetailInformation,
  bookmarkState,
  reservationStartDate,
  reservationEndDate,
  placeMaxCapacity,
  reservationMaxCapacity,
} from '../atoms';
import ReservationAsideBar from '../components/ReservationComponents/ReservationAsideBar';
import Nav from '../components/Navigation/Nav';
import View from '../components/View';
import ReviewContainer from '../components/ReviewComponents/ReviewContainer';

function Detail() {
  const { id } = useParams();
  const [detailInformation, setDetailInformation] =
    useRecoilState(DetailInformation);
  const [isBookmark, setIsBookmark] = useRecoilState(bookmarkState);
  const setStartDate = useSetRecoilState(reservationStartDate);
  const setEndDate = useSetRecoilState(reservationEndDate);
  const [slots, setSlots] = useState([{}]);
  const setMaxCapacity = useSetRecoilState(placeMaxCapacity);
  const setCapacity = useSetRecoilState(reservationMaxCapacity);

  const getDetailData = async () => {
    try {
      if (id) {
        const response = await axios.get(
          `${process.env.REACT_APP_SERVER_BASE_URL}/place/${id}`,
          {
            headers: {
              'ngrok-skip-browser-warning': '010',
              Authorization: (await localStorage.getItem('ACCESS'))
                ? `Bearer ${localStorage.getItem('ACCESS')}`
                : '',
              RefreshToken: (await localStorage.getItem('REFRESH'))
                ? localStorage.getItem('REFRESH')
                : '',
            },
          },
        );

        // const reservationResponse = await axios.get(
        //   `${process.env.REACT_APP_SERVER_BASE_URL}/reserve/place/${id}`,
        //   {
        //     headers: {
        //       'ngrok-skip-browser-warning': '010',
        //       Authorization: (await localStorage.getItem('ACCESS'))
        //         ? `Bearer ${localStorage.getItem('ACCESS')}`
        //         : '',
        //       RefreshToken: (await localStorage.getItem('REFRESH'))
        //         ? localStorage.getItem('REFRESH')
        //         : '',
        //     },
        //   },
        // );
        // console.log(reservationResponse);
        setCapacity(1);
        setDetailInformation({ ...response.data });
        setIsBookmark(response.data.bookmark);
        setSlots(prev => [...prev, ...response.data.reserves]);
        setMaxCapacity(response.data.maxCapacity);
        setStartDate(false);
        setEndDate(false);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    getDetailData();
  }, [isBookmark, id]);

  return (
    <DetailReviewContainer>
      <DetailContainer>
        <DetailViewContainer>
          <Nav />
          <View
            detailInformation={detailInformation.title && detailInformation}
          />
        </DetailViewContainer>
        <ReservationAsideBar
          charge={detailInformation.charge || '0'}
          slots={slots}
        />
      </DetailContainer>
      <ReviewContainer />
    </DetailReviewContainer>
  );
}

export default Detail;

const DetailContainer = styled.div`
  width: 1280px;
  display: flex;
  flex-direction: row;
  justify-content: space-around;
`;
const DetailViewContainer = styled.div``;

const DetailReviewContainer = styled.div`
  background-color: #f9f9f9;
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
`;
