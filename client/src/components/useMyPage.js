import { useState } from 'react';
import axios from 'axios';

const useMyPage = () => {
  const [myPageCategory, setMyPageCategory] = useState('등록내역');
  const [memberData, setMemberData] = useState([]);
  const [listData, setListData] = useState([]);
  const [userNickName, setUserNickName] = useState('');
  const [userMBTI, setUserMBTI] = useState('');
  const [editStatus, setEditStatus] = useState(false);

  const changeCategory = e => {
    setMyPageCategory(e.target.textContent);
  };

  // 아래 4개 함수 하나로 합쳐서 관리하기

  const callRegistrationList = async () => {
    // const data = {
    //   headers: {
    //     // refresh 토큰
    //     // access 토큰
    //   },
    // };
    await axios.get(`http://localhost:3001/place`).then(res => {
      setListData([...res.data]);
    });
  };

  const reservationList = async () => {
    // const data = {
    //   headers: {
    //     // refresh 토큰
    //     // access 토큰
    //   },
    // };
    await axios.get(`http://localhost:3001/reserve`).then(res => {
      setListData([...res.data]);
    });
  };

  const bookmarkList = async () => {
    // const data = {
    //   headers: {
    //     // refresh 토큰
    //     // access 토큰
    //   },
    // };
    await axios.get(`http://localhost:3001/bookmark`).then(res => {
      setListData([...res.data]);
    });
  };

  const reviewList = async () => {
    // const data = {
    //   headers: {
    //     // refresh 토큰
    //     // access 토큰
    //   },
    // };
    await axios.get(`http://localhost:3001/review`).then(res => {
      setListData([...res.data]);
    });
  };

  const callUserData = async () => {
    await axios.get('http://localhost:3001/member').then(res => {
      setMemberData(...res.data);
      setUserNickName(res.data[0].nickname);
      setUserMBTI(res.data[0].mbti);
    });
  };

  const editStatusChange = () => {
    setEditStatus(!editStatus);
  };

  const userDataEdit = async () => {
    await axios
      .patch(`http://localhost:3001/member/edit`, {
        nickname: userNickName,
        mbti: userMBTI,
      })
      .then(() => {
        callUserData();
      })
      .catch(() =>
        console.log({
          nickname: userNickName,
          mbti: userMBTI,
        }),
      );
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
