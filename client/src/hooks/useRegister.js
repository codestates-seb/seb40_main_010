import { useNavigate } from 'react-router-dom';
import { useRecoilState, useSetRecoilState } from 'recoil';
import axios from 'axios';

import ReactDaumPost from 'react-daumpost-hook';

import {
  registerFormTitle,
  registerFormAddress,
  registerFormMaxCapacity,
  registerFormDetailedAddress,
  registerFormDetailedInformation,
  registerFormCharge,
  registerFormItemsCheckedState,
  registerFormImage,
  registerFormPreviewImage,
} from '../atoms';

const useRegister = () => {
  const navigator = useNavigate();

  const [title, setTitle] = useRecoilState(registerFormTitle);
  const [maxCapacity, setMaxCapacity] = useRecoilState(registerFormMaxCapacity);
  const [address, setAddress] = useRecoilState(registerFormAddress);
  const [detailedAddress, setDetailedAddress] = useRecoilState(
    registerFormDetailedAddress,
  );
  const [detailedInformation, setDetailedInformation] = useRecoilState(
    registerFormDetailedInformation,
  );
  const [charge, setCharge] = useRecoilState(registerFormCharge);
  const [checkedList, setCheckedList] = useRecoilState(
    registerFormItemsCheckedState,
  );
  const [images, setImages] = useRecoilState(registerFormImage);
  const setPreviewImages = useSetRecoilState(registerFormPreviewImage);

  const handler = {
    title: setTitle,
    maxCapacity: setMaxCapacity,
    detailedAddress: setDetailedAddress,
    detailedInformation: setDetailedInformation,
    charge: setCharge,
  };

  const handleChange = event => {
    const { name, value } = event.target;
    handler[name](value);
  };

  const addItem = category => {
    setCheckedList(prev => [...prev, category]);
  };

  const removeItem = category => {
    setCheckedList(prev => prev.filter(item => item !== category));
  };

  const plusCapacity = () => {
    setMaxCapacity(maxCapacity + 1);
  };

  const minusCapacity = () => {
    if (maxCapacity > 1) setMaxCapacity(maxCapacity - 1);
  };

  const postConfig = {
    onComplete: data => {
      setAddress(data.address);
    },
  };

  const postCode = ReactDaumPost(postConfig);

  const initialize = () => {
    setCheckedList([]);
    setAddress('');
    setImages([]);
    setPreviewImages([]);
  };

  const handleSubmit = async () => {
    const json = JSON.stringify({
      title,
      maxCapacity,
      categoryList: checkedList,
      address,
      detailedAddress,
      detailInfo: detailedInformation,
      charge,
    });
    const blob = new Blob([json], { type: 'application/json' });

    const formData = new FormData();
    formData.append('key', blob);
    images.forEach(file => {
      formData.append('file', file, file.name);
    });

    try {
      await axios.post(
        `${process.env.REACT_APP_SERVER_BASE_URL}/place/post`,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
            RefreshToken: localStorage.getItem('REFRESH'),
          },
        },
      );

      initialize();
      navigator('/');
    } catch (err) {
      console.log('Error >>', err);
    }
  };

  return {
    title,
    checkedList,
    images,
    handleChange,
    addItem,
    removeItem,
    plusCapacity,
    minusCapacity,
    postCode,
    handleSubmit,
    setImages,
    maxCapacity,
    address,
    charge,
    detailedInformation,
  };
};

export default useRegister;
