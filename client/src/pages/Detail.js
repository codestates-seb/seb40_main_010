import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useRecoilState, useResetRecoilState, useSetRecoilState } from 'recoil';
import { useParams } from 'react-router-dom';

import {
  DetailInformation,
  bookmarkState,
  mainDataState,
  reservationStartDate,
  reservationEndDate,
  reservationSlots,
} from '../atoms';
import ReservationAsideBar from '../components/ReservationComponents/ReservationAsideBar';
import Nav from '../components/Navigation/Nav';
import View from '../components/View';
import ReviewContainer from '../components/ReviewComponents/ReviewContainer';
import getData from '../hooks/useAsyncGetData';

// ToDo api 3개 불러오기
function Detail() {
  const { id } = useParams();
  const resetPlaces = useResetRecoilState(mainDataState);
  const [detailInformation, setDetailInformation] =
    useRecoilState(DetailInformation);
  const [isBookmark, setIsBookmark] = useRecoilState(bookmarkState);
  const setStartDate = useSetRecoilState(reservationStartDate);
  const setEndDate = useSetRecoilState(reservationEndDate);
  const setSlots = useSetRecoilState(reservationSlots);

  const getDetailData = async () => {
    try {
      if (id) {
        const response = await getData(`/place/${id}`);
        console.log(response);
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
          slots={detailInformation.reserves}
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
