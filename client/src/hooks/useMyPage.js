import { useState } from 'react';
import axios from 'axios';
import { useRecoilState, useSetRecoilState } from 'recoil';
import {
  handleImageCompress,
  handleGetPreviewImagesUrl,
} from '../utils/images';
import { userMbtiValue, wholeData } from '../atoms';

const useMyPage = () => {
  const [myPageCategory, setMyPageCategory] = useState('등록내역');
  const [memberData, setMemberData] = useState([]);
  const [listData, setListData] = useRecoilState(wholeData);
  const [userNickName, setUserNickName] = useState('');
  const [userMBTI, setUserMBTI] = useState('');
  const [editStatus, setEditStatus] = useState(false);
  const [profileImage, setProfileImage] = useState([]);
  const [previewProfileImage, setPreviewProfileImage] = useState([]);
  const [nickNameCheck, setNickNameCheck] = useState(false);
  const [nickNameValidationMessage, setNickNameValidationMessage] =
    useState('');
  const [changeProfileImage, setChangeProfileImage] = useState(false);
  const setUserMbti = useSetRecoilState(userMbtiValue);

  // TODO: 더 리팩터링 생각해보기
  const getValidationType = nickname => {
    const checkNameForm = name => {
      return /[0-9]|[a-z]|[A-Z]|[가-힣]/.test(name);
    };
    const checkNameGap = name => {
      return /\s/g.test(name);
    };

    if (!checkNameForm(nickname) && nickname.length > 0) {
      return 'invalidNickName';
    }
    if (nickname.length === 0) {
      return 'emptyNickName';
    }
    if (checkNameGap(nickname)) {
      return 'existSpace';
    }
    return 'regular';
  };

  const setValidationException = type => {
    if (type === 'invalidNickName') {
      setNickNameCheck(false);
      setNickNameValidationMessage('이름 형식에 맞지 않습니다.');
      return null;
    }
    if (type === 'emptyNickName') {
      setNickNameCheck(false);
      setNickNameValidationMessage('필수 정보입니다.');
      return null;
    }
    if (type === 'existSpace') {
      setNickNameCheck(false);
      setNickNameValidationMessage('공백이 있습니다.');
      return null;
    }
    return null;
  };

  const checkNickNameValidation = nickname => {
    const validationType = getValidationType(nickname);
    if (validationType !== 'regular')
      return setValidationException(validationType);

    setNickNameCheck(true);
    setNickNameValidationMessage('');
    return null;
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
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/place`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      setListData([...response.data.data]);
    } catch (err) {
      clearCategory();
      console.log(err);
    }
  };

  const reservationList = async () => {
    try {
      clearCategory();
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/reserve`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      setListData([...response.data.data]);
    } catch (err) {
      clearCategory();
      console.log(err);
    }
  };

  const bookmarkList = async () => {
    try {
      clearCategory();
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/bookmark`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );

      setListData(() => [...response.data.data]);
    } catch (err) {
      clearCategory();
      console.log(err);
    }
  };

  const reviewList = async () => {
    try {
      clearCategory();
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/review`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      setListData([...response.data.data]);
    } catch (err) {
      clearCategory();
      console.log(err);
    }
  };

  const callUserData = async () => {
    try {
      const response = await axios.get(
        `${process.env.REACT_APP_SERVER_BASE_URL}/member`,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
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

  const userImageEdit = async () => {
    const formData = new FormData();
    formData.append('file', profileImage);

    try {
      await axios.post(
        `${process.env.REACT_APP_SERVER_BASE_URL}/member/profile`,
        formData,
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      callUserData();
      setChangeProfileImage(false);
    } catch (err) {
      console.log(err);
      setChangeProfileImage(false);
    }
  };

  const handleUploadImage = async event => {
    const selectedImages = event.target.files[0];

    const compressedImage = await handleImageCompress(selectedImages);
    const compressedImageUrl = await handleGetPreviewImagesUrl(compressedImage);

    setProfileImage(compressedImage);
    setPreviewProfileImage(compressedImageUrl);
  };

  const onChange = event => {
    setUserMBTI(event.value);
  };

  const onClickCancel = () => {
    editStatusChange();
    setUserNickName(memberData.nickname);
    setUserMBTI(memberData.mbti);
    setNickNameCheck(false);
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
    setUserNickName(event.target.value);
  };

  const userDataEdit = async () => {
    try {
      await axios.patch(
        `${process.env.REACT_APP_SERVER_BASE_URL}/member/edit`,
        {
          nickname: userNickName,
          mbti: userMBTI,
        },
        {
          headers: {
            'ngrok-skip-browser-warning': '010',
            Authorization: (await localStorage.getItem('ACCESS'))
              ? `Bearer ${localStorage.getItem('ACCESS')}`
              : '',
            RefreshToken: (await localStorage.getItem('REFRESH'))
              ? localStorage.getItem('REFRESH')
              : '',
          },
        },
      );
      callUserData();
      editStatusChange();
      setUserMbti(userMBTI);
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
    bookmarkList,
    reviewList,
    reservationList,
    userImageEdit,
    changeProfileImage,
    setChangeProfileImage,
  };
};

export default useMyPage;
