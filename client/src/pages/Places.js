import React, { useEffect, useState, useRef, useCallback } from 'react';
import styled from 'styled-components';
import { useRecoilState, useRecoilValue } from 'recoil';

import Category from '../components/Category';
import Nav from '../components/Navigation/Nav';
import Place from '../components/Place';
import MbtiPlaces from '../components/MbtiPlaces';
import { mainDataState, settingUrl, pageState, NextPage } from '../atoms';
import getData from '../hooks/useAsyncGetData';

// ToDo : Mbti 컴포넌트 위치, 요청
export default function Places() {
  const [mainPlaceData, setMainPlaceData] = useRecoilState(mainDataState);
  const [clickedNav, setClickedNav] = useState(false);
  const [hasNextPage, setHasNextPage] = useRecoilState(NextPage);
  const [page, setPage] = useRecoilState(pageState);
  const url = useRecoilValue(settingUrl);

  const observerTargetElement = useRef(null);

  // Todo 무한스크롤 고치기
  const getPageData = useCallback(async () => {
    try {
      const { data } = await getData(`${url}${page}`);

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
  }, [getPageData, hasNextPage, url]);

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
        <div>장소</div>
        <MainComponentContainer>
          {mainPlaceData &&
            mainPlaceData.map(placeData => {
              const { placeId } = placeData;
              return <Place key={`main ${placeId}`} placeData={placeData} />;
            })}
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
  margin: 1 auto;
  width: 1200px;
  flex-wrap: wrap;
  display: flex;
  flex-direction: row;
  justify-content: flex-start;
  align-content: flex-start;
  align-items: center;
`;

const MainContainer = styled.div`
  height: 100vh;
  width: 100vw;
`;
