import axios from 'axios';
import { useEffect } from 'react';
import { useSetRecoilState } from 'recoil';

import { mainDataState } from '../atoms';

export const getAllPlaces = () => {
  const setMainPlaceData = useSetRecoilState(mainDataState);

  useEffect(() => {
    setTimeout(async () => {
      try {
        const response = axios.get('/category/1', {
          headers: new Headers({
            'ngrok-skip-browser-warning': '69420',
          }),
        });
        setMainPlaceData([...response[0]]);
      } catch (error) {
        throw Error(error);
      }
    }, 1000);
  }, []);
};
