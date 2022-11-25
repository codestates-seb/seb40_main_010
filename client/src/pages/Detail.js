import React, { useEffect } from 'react';
import styled from 'styled-components';
import axios from 'axios';
// import { useRecoilValue } from 'recoil';
import { useRecoilState, useRecoilValue } from 'recoil';

import { DetailInformation, PlaceIDState, bookmarkState } from '../atoms';
import ReservationAsideBar from '../components/ReservationComponents/ReservationAsideBar';
import Nav from '../components/Navigation/Nav';
import View from '../components/View';
import ReviewContainer from '../components/ReviewContainer';
// import { PlaceIDState } from '../atoms';

// ToDo api 3개 불러오기
function Detail() {
  // ToDo : 예약, 북마크, 상세정보 get

  // const [detailInformation, setDetailInformation] =
  //   useRecoilState(DetailInformation);
  // const placeId = useRecoilValue(PlaceIDState);
  // useEffect(() => {
  //   axios
  //     .get(`{{BACKEND}}/place/${placeId}`)
  //     .then(res => console.log(res))
  //     .catch(err => console.log(err));
  // }, []); // > 실제 api

  const [detailInformation, setDetailInformation] =
    useRecoilState(DetailInformation);
  const placeId = useRecoilValue(PlaceIDState);
  const [isBookmark, setIsBookmark] = useRecoilState(bookmarkState);
  const header = {
    headers: {
      'ngrok-skip-browser-warning': '010',
      Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
      RefreshToken: localStorage.getItem('REFRESH'),
    },
  };

  const callDetailData = async () => {
    try {
      const response = await axios.get(`/place/${placeId}`, header);
      setDetailInformation({ ...response.data });
      setIsBookmark(response.data.bookmark);
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
    callDetailData();
    console.log(isBookmark);
  }, [placeId, isBookmark]);

  return (
    <DetailReviewContainer>
      <DetailContainer>
        <DetailViewContainer>
          <Nav />
          <View detailInformation={detailInformation} />
        </DetailViewContainer>
        <ReservationAsideBar charge={detailInformation.charge} />
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
