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

export default function Places() {
  const [mainPlaceData, setMainPlaceData] = useRecoilState(mainDataState);
  const [clickedNav, setClickedNav] = useState(false);
  const [hasNextPage, setHasNextPage] = useRecoilState(NextPage);
  const [page, setPage] = useRecoilState(pageState);
  const url = useRecoilValue(settingUrl);
  const isLogIn = useRecoilValue(HasRefresh);

  const observerTargetElement = useRef(null);

  const getPageData = useCallback(async () => {
    try {
      const { data } = await axios.get(`${url}${page}`, {
        headers: {
          'ngrok-skip-browser-warning': '010',
          Authorization: localStorage.getItem('ACCESS')
            ? `Bearer ${localStorage.getItem('ACCESS')}`
            : '',
          RefreshToken: localStorage.getItem('REFRESH')
            ? localStorage.getItem('REFRESH')
            : '',
        },
      });

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
        <MainComponentContainer>
          <Structure>
            {mainPlaceData &&
              mainPlaceData.map(placeData => {
                const { placeId } = placeData;
                return <Place key={`main ${placeId}`} placeData={placeData} />;
              })}
          </Structure>
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

const Structure = styled.div`
  width: 1200px;
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-content: flex-start;
`;

const MainContainer = styled.div`
  display: block;
`;
