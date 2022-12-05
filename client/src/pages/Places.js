import React, { useEffect, useRef, useCallback } from 'react';
import styled from 'styled-components';
import { useRecoilState, useRecoilValue } from 'recoil';
import axios from 'axios';

import Category from '../components/Category';
import Nav from '../components/Navigation/Nav';
import Place from '../components/Place';
import MbtiPlaces from '../components/MbtiPlaces';
import callHeader from '../utils/header';
import {
  mainDataState,
  settingUrl,
  pageState,
  NextPage,
  HasRefresh,
  navSearchValue,
  tokenAtom,
} from '../atoms';

export default function Places() {
  const [mainPlaceData, setMainPlaceData] = useRecoilState(mainDataState);
  const [hasNextPage, setHasNextPage] = useRecoilState(NextPage);
  const [page, setPage] = useRecoilState(pageState);
  const url = useRecoilValue(settingUrl);
  const isLogIn = useRecoilValue(HasRefresh);
  const search = useRecoilValue(navSearchValue);
  const tokenState = useRecoilValue(tokenAtom);

  const observerTargetElement = useRef(null);
  const header = callHeader();

  const getPageData = useCallback(async () => {
    try {
      const { data } = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}${url}${page}`,
        header,
      );

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
  }, [page, url, hasNextPage]);

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

  console.log(tokenState);

  return (
    <MainContainer>
      <Nav />
      <Category page={page} />
      <DisplayComponentDiv>
        {isLogIn ? <MbtiPlaces /> : null}
        <MainDiv>
          {search ? (
            <Div>&quot;{search}&#34; 에 대한 게시글</Div>
          ) : (
            <Div>전체게시글</Div>
          )}
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
        <div ref={observerTargetElement} className="observer" />
      </DisplayComponentDiv>
    </MainContainer>
  );
}

const MainDiv = styled.div`
  width: 1225px;
  margin: 1 auto;
`;

const DisplayComponentDiv = styled.div`
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 1 auto;
  .observer {
    padding: 2px;
  }
`;
const MainComponentContainer = styled.div`
  display: flex;
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
  height: 100%;
`;

const Div = styled.div`
  align-self: start;
  padding-left: 12px;
  margin-bottom: 10px;
  font-weight: 500;
  font-size: 1.2rem;
`;
