import React, { useRef } from 'react';
import styled from 'styled-components';
import { useRecoilState, useRecoilValue } from 'recoil';
import axios from 'axios';

import { FaRegBookmark, FaLink, FaBookmark } from 'react-icons/fa';
import { PlaceIDState, bookmarkState } from '../../atoms';
import header from '../../utils/header';

// TODO
// window를 지원하지 않을 수 있다.
function ReservationBottomButtons() {
  const placeId = useRecoilValue(PlaceIDState);
  // const detailInformation = useRecoilValue(DetailInformation);
  // const [bookmark, setBookmark] = useState(detailInformation.bookmark);
  // const { bookmark } = detailInformation;
  // console.log(bookmark);
  const [isBookmark, setIsBookmark] = useRecoilState(bookmarkState);
  const copyLinkRef = useRef();

  // let test = bookmark;

  const handleCopyLink = () => {
    if (!document.queryCommandSupported('copy')) {
      return alert('복사 기능이 지원되지 않는 브라우저입니다.');
    }

    copyLinkRef.current.focus();
    copyLinkRef.current.select();
    document.execCommand('copy');

    return alert('링크를 복사했습니다.');
  };

  const handleBookmark = async () => {
    try {
      const response = await axios.get(`/bookmark/${placeId}`, header);
      // console.log(response);
      setIsBookmark(response.data);
      // console.log('북마크', detailInformation);
      // test = response.data;
      // console.log(test);
    } catch (error) {
      console.log(error);
    }
  };

  return (
    <Container>
      <button type="button" className="button" onClick={handleBookmark}>
        {!isBookmark && <EmptyBookmarkIcon />}
        {isBookmark && <FullBookmarkIcon />}
        관심장소
      </button>
      <button type="button" className="button" onClick={handleCopyLink}>
        <LinkCopyIcon />
        링크복사
      </button>
      <TextArea ref={copyLinkRef} value={window.location.href} readOnly />
    </Container>
  );
}

const Container = styled.div`
  display: flex;
  justify-content: space-around;

  .button {
    width: 120px;
    height: 40px;
    display: flex;
    align-items: center;
    justify-content: center;
    border-radius: 10px;
    color: #2b2b2b;
    font-weight: 500;
    box-shadow: rgba(0, 0, 0, 0.35) 3px 3px 3px;
    border: none;
    background-color: white;
    position: relative;

    :active {
      box-shadow: none;
      position: relative;
      box-shadow: rgba(0, 0, 0, 0.35) 1px 1px 1px;
    }
  }
`;

const EmptyBookmarkIcon = styled(FaRegBookmark)`
  font-size: 1rem;
  margin-right: 8px;
  color: #eb7470;

  :hover {
    cursor: pointer;
  }
`;

const FullBookmarkIcon = styled(FaBookmark)`
  font-size: 1rem;
  margin-right: 8px;
  color: #eb7470;

  :hover {
    cursor: pointer;
  }
`;

const LinkCopyIcon = styled(FaLink)`
  font-size: 1rem;
  margin-right: 8px;
  color: #eb7470;

  :hover {
    cursor: pointer;
  }
`;

const TextArea = styled.textarea`
  position: absolute;
  width: 0px;
  height: 0px;
  bottom: 0;
  left: 0;
  opacity: 0;
`;

export default ReservationBottomButtons;
