// /* global kakao */
import React, { useEffect } from 'react';
import styled from 'styled-components';

const MapContainer = styled.div`
  width: 600px;
  height: 300px;
  margin-left: 20px;
`;
// ToDo: 지도 마커 & infowindow 설정 고르기
function Location({ address }) {
  const { kakao } = window;
  useEffect(() => {
    const mapContainer = document.getElementById('map');
    const mapOption = {
      center: new kakao.maps.LatLng(33.450701, 126.570667), // 지도의 중심좌표
      level: 3,
    };
    const map = new kakao.maps.Map(mapContainer, mapOption);
    const geocoder = new kakao.maps.services.Geocoder();
    geocoder.addressSearch(address, function (result, status) {
      if (status === kakao.maps.services.Status.OK) {
        const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
        const marker = new kakao.maps.Marker({
          map,
          position: coords,
        });
        // const infowindow = new kakao.maps.InfoWindow({
        //   content:
        //     '<div class="info-subTitle" style="width:150px;text-align:center;padding:6px 0;">상세 위치</div>',
        // });
        // infowindow.open(map, marker);
        marker.setMap(map);
        map.setCenter(coords);
      }
    });
  }, []);

  return (
    <div>
      <MapContainer id="map" />
    </div>
  );
}

export default Location;
