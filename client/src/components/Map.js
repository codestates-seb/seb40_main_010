// /* global kakao */
import React, { useEffect } from 'react';
import styled from 'styled-components';

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

    if (address) {
      geocoder.addressSearch(address, function (result, status) {
        if (status === kakao.maps.services.Status.OK) {
          const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
          const marker = new kakao.maps.Marker({
            map,
            position: coords,
          });

          marker.setMap(map);
          map.setCenter(coords);
        }
      });
    }
  }, [address]);

  return (
    <div>
      <MapContainer id="map" />
    </div>
  );
}

export default Location;

const MapContainer = styled.div`
  width: 800px;
  height: 400px;
  margin-left: 20px;
`;
