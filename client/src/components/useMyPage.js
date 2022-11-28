import { useState } from 'react';
import axios from 'axios';
import {
  handleImageCompress,
  handleGetPreviewImagesUrl,
} from '../utils/images';

const useMyPage = () => {
  const [myPageCategory, setMyPageCategory] = useState('등록내역');
  const [memberData, setMemberData] = useState([]);
  const [listData, setListData] = useState([]);
  const [userNickName, setUserNickName] = useState('');
  const [userMBTI, setUserMBTI] = useState('');
  const [editStatus, setEditStatus] = useState(false);
  const [profileImage, setProfileImage] = useState([]);
  const [previewProfileImage, setPreviewProfileImage] = useState([]);
  const [nickNameCheck, setNickNameCheck] = useState(false);
  const [nickNameValidationMessage, setNickNameValidationMessage] =
    useState('');

  const checkNickNameValidation = nickname => {
    const checkNameForm = name => {
      return /[0-9]|[a-z]|[A-Z]|[가-힣]/.test(name);
    };
    const checkNameGap = name => {
      return /\s/g.test(name);
    };
    if (!checkNameForm(nickname) && nickname.length > 0) {
      setNickNameCheck(false);
      setNickNameValidationMessage('이름 형식에 맞지 않습니다.');
    } else if (nickname.length === 0) {
      setNickNameCheck(false);
      setNickNameValidationMessage('필수 정보입니다.');
    } else if (checkNameGap(nickname)) {
      setNickNameCheck(false);
      setNickNameValidationMessage('공백이 있습니다.');
    } else {
      setNickNameCheck(true);
      setNickNameValidationMessage('');
    }
  };

  const header = {
    headers: {
      'ngrok-skip-browser-warning': '010',
      Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
      RefreshToken: localStorage.getItem('REFRESH'),
    },
  };

  const clearCategory = () => {
    setListData([]);
  };

  const changeCategory = event => {
    setMyPageCategory(event.target.textContent);
  };

  // 아래 4개 함수 하나로 합쳐서 관리하기!
  // const callCategoryList = async value => {
  //   try {
  //     const response = await axios.get(`/${value}`, header);
  //     setListData([...response.data]);
  //   } catch (err) {
  //     console.log(err);
  //   }
  // };

  const callRegistrationList = async () => {
    try {
      clearCategory();
      const response = await axios.get(`/place`, header);
      console.log('place', response.data.data);
      setListData([...response.data.data]);
    } catch (err) {
      clearCategory();
      console.log(err);
    }
  };

  const reservationList = async () => {
    try {
      clearCategory();
      const response = await axios.get(`/reserve`, header);
      console.log('reserve', response.data.data);
      setListData([...response.data.data]);
    } catch (err) {
      clearCategory();
      console.log(err);
    }
  };

  const bookmarkList = async () => {
    try {
      clearCategory();
      const response = await axios.get(`/bookmark`, header);
      console.log('bookmark', response.data.data);
      setListData([...response.data.data]);
    } catch (err) {
      clearCategory();
      console.log(err);
    }
  };

  const reviewList = async () => {
    try {
      clearCategory();
      const response = await axios.get(`/review`, header);
      console.log('review', response.data.data);
      setListData([...response.data.data]);
    } catch (err) {
      clearCategory();
      console.log(err);
    }
  };

  const callUserData = async () => {
    try {
      const response = await axios.get('/member', header);
      setMemberData(response.data);
      setUserNickName(response.data.nickname);
      setUserMBTI(response.data.mbti);
      setPreviewProfileImage(response.data.profileImage);
    } catch (err) {
      console.log(err);
    }
  };

  const editStatusChange = () => {
    setEditStatus(!editStatus);
  };

  const userDataEdit = async () => {
    try {
      await axios.patch(
        `/member/edit`,
        {
          nickname: userNickName,
          mbti: userMBTI,
        },
        header,
      );
      callUserData();
      editStatusChange();
    } catch (err) {
      console.log(err);
      if (err.response.data.message === '이미 존재하는 닉네임입니다.') {
        setNickNameValidationMessage(err.response.data.message);
        setNickNameCheck(false);
      }
      if (err.response.data.message === '이미 존재하는 이메일입니다.') {
        setNickNameValidationMessage(err.response.data.message);
        setNickNameCheck(false);
      }
    }
  };

  const userImageEdit = async () => {
    const formData = new FormData();
    formData.append('file', profileImage);
    console.log(profileImage);

    try {
      await axios.post(`/member/profile`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
          Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
          RefreshToken: localStorage.getItem('REFRESH'),
        },
      });
      callUserData();
      editStatusChange();
    } catch (err) {
      console.log(err);
    }
  };

  const handleUploadImage = async event => {
    const selectedImages = event.target.files[0];

    const compressedImage = await handleImageCompress(selectedImages);
    const compressedImageUrl = await handleGetPreviewImagesUrl(compressedImage);

    setProfileImage(compressedImage);
    setPreviewProfileImage(compressedImageUrl);
    userImageEdit();
  };

  const onChange = event => {
    setUserMBTI(event.value);
  };

  const onClickCancel = () => {
    editStatusChange();
    setUserNickName(memberData.nickname);
    setUserMBTI(memberData.mbti);
    setPreviewProfileImage([]);
    setNickNameCheck(true);
    setNickNameValidationMessage('');
  };

  const categoryHandler = {
    register: callRegistrationList,
    reservation: reservationList,
    bookmark: bookmarkList,
    review: reviewList,
  };

  const onClickCategory = event => {
    changeCategory(event);
    const type = event.target.value;
    categoryHandler[type]();
  };

  const onChangeNickName = event => {
    // event.stopPropagation();
    setUserNickName(event.target.value);
  };

  return {
    callUserData,
    callRegistrationList,
    profileImage: memberData.profileImage,
    nickname: memberData.nickname,
    editStatus,
    userNickName,
    editStatusChange,
    userDataEdit,
    mbti: memberData.mbti,
    userMBTI,
    myPageCategory,
    listData,
    onChange,
    onClickCancel,
    onClickCategory,
    onChangeNickName,
    clearCategory,
    handleUploadImage,
    previewProfileImage,
    nickNameCheck,
    nickNameValidationMessage,
    checkNickNameValidation,
  };
};

export default useMyPage;
