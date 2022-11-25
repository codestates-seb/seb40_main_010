import { useState } from 'react';
import axios from 'axios';

const useMyPage = () => {
  const [myPageCategory, setMyPageCategory] = useState('등록내역');
  const [memberData, setMemberData] = useState([]);
  const [listData, setListData] = useState([]);
  const [userNickName, setUserNickName] = useState('');
  const [userMBTI, setUserMBTI] = useState('');
  const [editStatus, setEditStatus] = useState(false);

  const header = {
    headers: {
      'ngrok-skip-browser-warning': '010',
      Authorization: `Bearer ${localStorage.getItem('ACCESS')}`,
      RefreshToken: localStorage.getItem('REFRESH'),
    },
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
      const response = await axios.get(`/place`, header);
      console.log('place', response.data.data);
      setListData([...response.data.data]);
    } catch (err) {
      console.log(err);
    }
  };

  const reservationList = async () => {
    try {
      const response = await axios.get(`/reserve`, header);
      console.log('reserve', response.data.data);
      setListData([...response.data.data]);
    } catch (err) {
      console.log(err);
    }
  };

  const bookmarkList = async () => {
    try {
      const response = await axios.get(`/bookmark`, header);
      console.log('bookmark', response.data.data);
      setListData([...response.data.data]);
    } catch (err) {
      console.log(err);
    }
  };

  const reviewList = async () => {
    try {
      const response = await axios.get(`/review`, header);
      console.log('review', response);
      setListData([...response.data.data]);
    } catch (err) {
      console.log(err);
    }
  };

  const callUserData = async () => {
    try {
      const response = await axios.get('/member', header);
      console.log(response.data);
      setMemberData(response.data);
      setUserNickName(response.data.nickname);
      setUserMBTI(response.data.mbti);
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
    }
  };

  const onChange = event => {
    setUserMBTI(event.value);
  };

  const onClickCancel = () => {
    editStatusChange();
    setUserNickName(memberData.nickname);
    setUserMBTI(memberData.mbti);
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
    event.stopPropagation();
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
  };
};

export default useMyPage;
