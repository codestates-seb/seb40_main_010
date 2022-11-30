import React, { useEffect, useState, useRef, useCallback } from 'react';
import styled from 'styled-components';
import { useRecoilState, useRecoilValue } from 'recoil';
import axios from 'axios';

import Category from '../components/Category';
import Nav from '../components/Navigation/Nav';
import Place from '../components/Place';
import MbtiPlaces from '../components/MbtiPlaces';
import {
  mainDataState,
  settingUrl,
  pageState,
  NextPage,
  HasRefresh,
} from '../atoms';
// import getData from '../hooks/useAsyncGetData';
import header from '../utils/header';

// ToDo : Mbti 컴포넌트 위치, 요청
export default function Places() {
  const [mainPlaceData, setMainPlaceData] = useRecoilState(mainDataState);
  const [clickedNav, setClickedNav] = useState(false);
  const [hasNextPage, setHasNextPage] = useRecoilState(NextPage);
  const [page, setPage] = useRecoilState(pageState);
  const url = useRecoilValue(settingUrl);
  const isLogIn = useRecoilValue(HasRefresh);
  // const isLogIn = localStorage.getItem('REFRESH');

  const observerTargetElement = useRef(null);

  // Todo 무한스크롤 고치기
  const getPageData = useCallback(async () => {
    try {
      // const { data } = await getData(`${url}${page}`);
      const { data } = await axios.get(`${url}${page}`, header);

      if (!mainPlaceData) {
        setMainPlaceData([...data.data]);
      }

      if (mainPlaceData) {
        setMainPlaceData(prevPosts => [...prevPosts, ...data.data]);
      }

      if (data.data.length === 20) {
        setPage(prev => prev + 1);
      }
      setHasNextPage(data.data.length === 20);
    } catch (err) {
      console.log(err);
    }
  }, [page, url]);

  useEffect(() => {
    if (!observerTargetElement.current || !hasNextPage) return;

    const observation = new IntersectionObserver(entries => {
      if (entries[0].isIntersecting) {
        getPageData();
      }
    });
    observation.observe(observerTargetElement.current);
    // eslint-disable-next-line consistent-return
    return () => {
      observation.disconnect();
    };
  }, [getPageData, hasNextPage, url, isLogIn]);

  return (
    <MainContainer>
      <Nav
        setClickedNav={setClickedNav}
        clickedNav={clickedNav}
        setPage={setPage}
      />
      <Category page={page} />
      <DisplayComponentDiv>
        <MbtiPlaces />
        <PlacesTitle>대여가 대여</PlacesTitle>
        <MainComponentContainer>
          <Distructure>
            {mainPlaceData &&
              mainPlaceData.map(placeData => {
                const { placeId } = placeData;
                return <Place key={`main ${placeId}`} placeData={placeData} />;
              })}
          </Distructure>
        </MainComponentContainer>
      </DisplayComponentDiv>
      <div ref={observerTargetElement} />
    </MainContainer>
  );
}

const DisplayComponentDiv = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
`;
const MainComponentContainer = styled.div`
  display: flex;
  margin: 1 auto;
  //rem
  width: 1200px;
  justify-content: center;
  align-items: center;
`;

const Distructure = styled.div`
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-content: flex-start;
`;

const MainContainer = styled.div`
  display: block;
`;

const PlacesTitle = styled.div`
  font-size: 1.6rem;
`;
