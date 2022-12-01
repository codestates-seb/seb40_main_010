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
      console.log(url, page);
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

      // if (!mainPlaceData) {
      //   setMainPlaceData([...data.data]);
      // }
      // if (mainPlaceData) {
      //   setMainPlaceData(prevPosts => [...prevPosts, ...data.data]);
      // }

      if (page === 1) {
        setMainPlaceData([...data.data]);
      }

      if (page !== 1 && mainPlaceData) {
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
        {isLogIn ? <MbtiPlaces /> : null}
        <MainDiv>
          <Div>전체 게시글</Div>
          <MainComponentContainer>
            <Structure>
              {mainPlaceData &&
                mainPlaceData.map(placeData => {
                  const { placeId } = placeData;
                  return (
                    <Place key={`main ${placeId}`} placeData={placeData} />
                  );
                })}
            </Structure>
          </MainComponentContainer>
        </MainDiv>
        <div ref={observerTargetElement} />
      </DisplayComponentDiv>
    </MainContainer>
  );
}

const MainDiv = styled.div`
  width: 1225px;
`;

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
  width: 1225px;
  justify-content: center;
  align-items: center;
`;

const Structure = styled.div`
  width: 1225px;
  display: flex;
  flex-wrap: wrap;
  justify-content: flex-start;
  align-content: flex-start;
`;

const MainContainer = styled.div`
  display: block;
  background-color: #f9f9f9;
`;

const Div = styled.div`
  align-self: start;
  padding-left: 12px;
  margin-bottom: 10px;
  font-weight: 500;
  font-size: 1.2rem;
`;
