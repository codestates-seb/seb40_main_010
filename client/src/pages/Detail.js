import React, { useEffect } from 'react';
import styled from 'styled-components';
import { useRecoilState, useSetRecoilState } from 'recoil';
import { useParams, useMatch, useNavigate } from 'react-router-dom';

import {
  DetailInformation,
  bookmarkState,
  reservationStartDate,
  reservationEndDate,
} from '../atoms';
import ReservationAsideBar from '../components/ReservationComponents/ReservationAsideBar';
import Nav from '../components/Navigation/Nav';
import View from '../components/View';
import ReviewContainer from '../components/ReviewComponents/ReviewContainer';
import getData from '../hooks/useAsyncGetData';

// ToDo api 3개 불러오기
function Detail() {
  const { id } = useParams();
  const isDetailUrl = useMatch('/detail');
  const navigate = useNavigate();

  const [detailInformation, setDetailInformation] =
    useRecoilState(DetailInformation);
  const [isBookmark, setIsBookmark] = useRecoilState(bookmarkState);
  const setStartDate = useSetRecoilState(reservationStartDate);
  const setEndDate = useSetRecoilState(reservationEndDate);

  const callDetailData = async () => {
    try {
      if (id) {
        const response = await getData(`/place/${id}`);
        setDetailInformation({ ...response.data });
        setIsBookmark(response.data.bookmark);
        setStartDate(false);
        setEndDate(false);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    if (isDetailUrl) {
      navigate('/');
    }
    callDetailData();
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
          charge={detailInformation.charge && detailInformation.charge}
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
