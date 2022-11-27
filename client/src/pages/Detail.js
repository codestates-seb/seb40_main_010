import React, { useEffect } from 'react';
import styled from 'styled-components';
// import axios from 'axios';
import { useRecoilState } from 'recoil';

import { useParams } from 'react-router-dom';
import { DetailInformation, bookmarkState } from '../atoms';
import ReservationAsideBar from '../components/ReservationComponents/ReservationAsideBar';
import Nav from '../components/Navigation/Nav';
import View from '../components/View';
import ReviewContainer from '../components/ReviewContainer';
import getData from '../hooks/useAsyncGetData';

// ToDo api 3개 불러오기
function Detail() {
  // const placeId = useRecoilValue(PlaceIDState);
  // const navigate = useNavigate();
  const { id } = useParams();
  console.log(id);

  const [detailInformation, setDetailInformation] =
    useRecoilState(DetailInformation);
  // id = useRecoilValue(PlaceIDState);
  const [isBookmark, setIsBookmark] = useRecoilState(bookmarkState);

  const callDetailData = async () => {
    try {
      if (id) {
        // const response = await axios.get(`/place/${placeId}`, header);
        const response = await getData(`/place/${id}`);
        setDetailInformation({ ...response.data });
        setIsBookmark(response.data.bookmark);
      }
    } catch (error) {
      console.log(error);
    }
  };

  useEffect(() => {
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
