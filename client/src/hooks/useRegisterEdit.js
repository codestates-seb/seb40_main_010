import { useNavigate } from 'react-router-dom';
import axios from 'axios';
import { useRecoilState, useSetRecoilState } from 'recoil';
import ReactDaumPost from 'react-daumpost-hook';

import {
  reservationEditData,
  registerFormDetailedAddress,
  registerFormItemsCheckedState,
  registerFormImage,
  registerFormPreviewImage,
} from '../atoms';

const useRegisterEdit = () => {
  const navigator = useNavigate();

  const [checkedList, setCheckedList] = useRecoilState(
    registerFormItemsCheckedState,
  );
  const [images, setImages] = useRecoilState(registerFormImage);
  const setPreviewImages = useSetRecoilState(registerFormPreviewImage);
  const detailedAddress = useRecoilState(registerFormDetailedAddress);

  const [editData, setEditData] = useRecoilState(reservationEditData);

  const editHandleChange = event => {
    if (event.target.id === 'capacityMinus' && editData.maxCapacity > 1) {
      return setEditData({
        ...editData,
        maxCapacity: editData.maxCapacity - 1,
      });
    }
    if (event.target.id === 'capacityPlus') {
      return setEditData({
        ...editData,
        maxCapacity: editData.maxCapacity + 1,
      });
    }
    const { name, value } = event.target;
    return setEditData({ ...editData, [name]: value });
  };

  const postConfigEdit = {
    onComplete: data => {
      setEditData({ ...editData, address: data.address });
    },
  };

  const postCodeEdit = ReactDaumPost(postConfigEdit);

  const handleEditSubmit = async () => {
    const json = JSON.stringify({
      title: editData.title,
      maxCapacity: editData.maxCapacity,
      categoryList: checkedList,
      address: editData.address,
      detailedAddress,
      detailInfo: editData.detailInfo,
      charge: editData.charge,
    });
    const blob = new Blob([json], { type: 'application/json' });

    const formData = new FormData();
    formData.append('key', blob);
    images.forEach(file => {
      formData.append('file', file, file.name);
    });

    try {
      await axios.post(
        `${process.env.REACT_APP_SERVER_BASE_URL}/place/${editData.placeId}/edit`,
        formData,
        {
          headers: {
            'Content-Type': 'multipart/form-data',
            Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
            RefreshToken: localStorage.getItem('REFRESH'),
          },
        },
      );
      setCheckedList([]);
      setImages([]);
      setPreviewImages([]);
      setEditData(null);
      navigator(`/detail/${editData.placeId}`);
    } catch (err) {
      console.log('Error >>', err);
    }
  };

  return {
    editHandleChange,
    postCodeEdit,
    handleEditSubmit,
    editData,
  };
};

export default useRegisterEdit;
