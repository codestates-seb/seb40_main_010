import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import { useRecoilState, useResetRecoilState, useSetRecoilState } from 'recoil';
import { useParams } from 'react-router-dom';

import axios from 'axios';
import {
  DetailInformation,
  bookmarkState,
  mainDataState,
  reservationStartDate,
  reservationEndDate,
} from '../atoms';
import ReservationAsideBar from '../components/ReservationComponents/ReservationAsideBar';
import Nav from '../components/Navigation/Nav';
import View from '../components/View';
import ReviewContainer from '../components/ReviewComponents/ReviewContainer';

function Detail() {
  const { id } = useParams();
  const resetPlaces = useResetRecoilState(mainDataState);
  const [detailInformation, setDetailInformation] =
    useRecoilState(DetailInformation);
  const [isBookmark, setIsBookmark] = useRecoilState(bookmarkState);
  const setStartDate = useSetRecoilState(reservationStartDate);
  const setEndDate = useSetRecoilState(reservationEndDate);
  const [slots, setSlots] = useState([{}]);

  const getDetailData = async () => {
    try {
      if (id) {
        const response = await axios.get(`/place/${id}`, {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        });
        setDetailInformation({ ...response.data });
        setIsBookmark(response.data.bookmark);
        setSlots(prev => [...prev, ...response.data.reserves]);
        setStartDate(false);
        setEndDate(false);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    resetPlaces();
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
  display: flex;
  flex-direction: row;
  justify-content: center;
`;
const DetailViewContainer = styled.div``;

const DetailReviewContainer = styled.div`
  display: flex;
  align-items: center;
  flex-direction: column;
  justify-content: center;
`;
