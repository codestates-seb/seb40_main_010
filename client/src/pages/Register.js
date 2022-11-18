import React, { useRef } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import ReactDaumPost from 'react-daumpost-hook';
import { FaCaretRight, FaCaretLeft } from 'react-icons/fa';
import { useRecoilState } from 'recoil';
import {
  registerFormTitle,
  registerFormAddress,
  registerFormMaxCapacity,
  registerFormDetailedAddress,
  registerFormDetailedInformation,
  registerFormCharge,
  registerFormItemsCheckedState,
  registerFormPreviewImage,
  registerFormImage,
} from '../atoms';

function Register() {
  const [title, setTitle] = useRecoilState(registerFormTitle);
  const [maxCapacity, setMaxCapacity] = useRecoilState(registerFormMaxCapacity);
  const [address, setAdress] = useRecoilState(registerFormAddress);
  const [detailedAddress, setDetailedAddress] = useRecoilState(
    registerFormDetailedAddress,
  );
  const [deatiledInformation, setDetailedInformation] = useRecoilState(
    registerFormDetailedInformation,
  );
  const [charge, setCharge] = useRecoilState(registerFormCharge);
  const [isItemsClicked, setIsItemsClicked] = useRecoilState(
    registerFormItemsCheckedState,
  );
  const [showImages, setShowImages] = useRecoilState(registerFormPreviewImage);
  const [images, setImages] = useRecoilState(registerFormImage);

  const handleOnChange = position => {
    const updatedCheckedState = isItemsClicked.map((isItemClicked, index) =>
      index === position ? !isItemClicked : isItemClicked,
    );
    setIsItemsClicked(updatedCheckedState);
  };

  const handleTitle = e => {
    setTitle(e.target.value);
  };

  const handleMaxCapacity = e => {
    setMaxCapacity(e.target.value);
  };

  const plusCapacity = () => {
    setMaxCapacity(maxCapacity + 1);
  };

  const minusCapacity = () => {
    if (maxCapacity > 1) setMaxCapacity(maxCapacity - 1);
  };

  const postConfig = {
    onComplete: data => {
      setAdress(data.address);
    },
  };

  const postCode = ReactDaumPost(postConfig);

  const handleDedatiledAddress = e => {
    setDetailedAddress(e.target.value);
  };

  const handleDedatiledInformation = e => {
    setDetailedInformation(e.target.value);
  };

  const handleCharge = e => {
    setCharge(e.target.value);
  };

  const hiddenFileInput = useRef(null);
  const handleImageButtonClick = () => {
    hiddenFileInput.current.click();
  };

  const handleUploadImage = e => {
    const selectedFIles = [];
    const maxLength = 3;
    if (e.target.files.length > maxLength) {
      const transfer = new DataTransfer();
      Array.from(e.target.files)
        .slice(0, maxLength)
        .forEach(file => {
          transfer.items.add(file);
        });
      e.target.files = transfer.files;
      setImages([...e.target.files]);
    } else {
      const transfer = new DataTransfer();
      Array.from(e.target.files)
        .slice(0, maxLength)
        .forEach(file => {
          transfer.items.add(file);
        });
      e.target.files = transfer.files;
      setImages([...e.target.files]);
    }

    const targetFilesObject = [...e.target.files];
    targetFilesObject.map(file => {
      return selectedFIles.push(URL.createObjectURL(file));
    });
    setShowImages(selectedFIles);
  };

  const categories = [
    { id: 0, place: '공유오피스' },
    { id: 1, place: '캠핑' },
    { id: 2, place: '바다근처' },
    { id: 3, place: '짐보관' },
    { id: 4, place: '파티룸' },
    { id: 5, place: '게스트하우스' },
    { id: 6, place: '호텔' },
    { id: 7, place: '스터디룸' },
    { id: 8, place: '계곡근처' },
    { id: 9, place: '공연장' },
  ];

  for (let i = 0; i < categories.length; i += 1) {
    categories[i].isClicked = isItemsClicked[i];
  }

  const clickedCategories = categories
    .filter(category => category.isClicked === true)
    .map(category => category.place);

  const handleSubmit = () => {
    const formData = new FormData();
    formData.append('title', title);
    formData.append('category', clickedCategories);
    formData.append('maxCapacity', maxCapacity);
    formData.append('address', address);
    formData.append('detailedAddress', detailedAddress);
    formData.append('detailInfo', deatiledInformation);
    formData.append('charge', charge);
    // formData.append('image', images[0]);
    images.forEach(file => {
      formData.append('image', file);
    });

    axios({
      method: 'post',
      url: `http://localhost:3001/place`,
      data: formData,
      headers: { 'Content-Type': 'multipart/form-data' },
    });

    // formData.append('image', images);
    // // eslint-disable-next-line no-restricted-syntax
    // for (const key of formData.keys()) {
    //   // eslint-disable-next-line no-restricted-syntax
    //   for (const value of formData.values()) {
    //     console.log(key, value);
    //   }
    // }
  };

  return (
    <Container>
      {/* <form> */}
      <div className="register-container">
        <div className="wrapper">
          <div className="title">제목</div>
          <Input onChange={handleTitle} />
        </div>
        <div className="wrapper">
          <div className="title">카테고리</div>
          <div className="category-wrapper">
            {categories.map((category, index) => (
              <div className="category">
                <input
                  type="checkbox"
                  id={category.id}
                  value={category.place}
                  key={category.place}
                  checked={isItemsClicked[index]}
                  onChange={() => handleOnChange(index)}
                />
                <label
                  htmlFor={category.id}
                  className="label"
                  key={category.id}
                >
                  {category.place}
                </label>
              </div>
            ))}
          </div>
        </div>
        <div className="wrapper">
          <div className="title">최대 인원</div>
          <div className="capa-wrapper">
            <LeftIcon onClick={minusCapacity} />
            <SmallInput
              width="28px"
              type="number"
              onChange={handleMaxCapacity}
              value={maxCapacity}
              readOnly
            />
            <RightIcon onClick={plusCapacity} />
          </div>
        </div>
        <div className="wrapper">
          <div className="title">주소</div>
          <Input
            type="text"
            onClick={() => postCode()}
            value={address}
            readOnly
          />
          <div className="title detailed-address">상세주소</div>
          <Input type="text" onChange={handleDedatiledAddress} />
        </div>
        <div className="wrapper">
          <div className="title">상세정보</div>
          <Textarea type="text" onChange={handleDedatiledInformation} />
        </div>
        <div className="wrapper">
          <div className="title">사진</div>
          <div className="capa-wrapper">
            <input
              type="file"
              accept="image/*"
              style={{ display: 'none' }}
              ref={hiddenFileInput}
              onChange={handleUploadImage}
              multiple
            />
            <button
              className="image-upload-button"
              label="이미지 업로드"
              type="button"
              onClick={handleImageButtonClick}
            >
              사진 업로드하기
            </button>
          </div>
          <div className="preview-image-wrapper">
            {showImages.map((image, id) => (
              <img key={`${image}`} src={image} alt={`${image} - ${id}`} />
            ))}
          </div>
        </div>
        <div className="wrapper">
          <div className="title">금액 설정</div>
          <div className="set-charge">
            <div className="hour-description">1시간 / </div>
            <SmallInput type="number" width="100px" onChange={handleCharge} />
            <div className="hour-description">원</div>
          </div>
        </div>
        <div className="btn-wrapper">
          <button
            type="submit"
            className="form-register-button"
            onClick={handleSubmit}
          >
            등록하기
          </button>
        </div>
      </div>
      {/* </form> */}
    </Container>
  );
}

export default Register;

const Container = styled.div`
  width: 100vw;
  height: fit-content;
  background-color: #e5f0ff;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 50px 0px;

  .register-container {
    width: 900px;
    height: fit-content;
  }

  .wrapper {
    height: fit-content;
    margin-bottom: 25px;
    background-color: #ffffff;
    border-radius: 10px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    padding: 25px 20px;
    box-shadow: rgba(0, 0, 0, 0.3) 0px 5px 12px;
  }

  .btn-wrapper {
    height: fit-content;
    display: flex;
    align-items: center;
    justify-content: center;
  }

  .title {
    width: 95%;
    height: fit-content;
    font-size: 1.2rem;
    font-weight: 600;
    color: #2b2b2b;
    margin-bottom: 15px;
    /* border: 1px solid red; */
  }

  .form-register-button {
    width: 300px;
    height: 55px;
    margin-top: 25px;
    background-color: #ffda77;
    border-radius: 40px;
    color: #2b2b2b;
    font-size: 1.5rem;
    font-weight: 600;
    border: none;
    box-shadow: rgba(0, 0, 0, 0.35) 3px 3px 3px;

    :active {
      box-shadow: none;
    }

    :disabled {
      cursor: not-allowed;
      opacity: 0.7;
      box-shadow: none;
    }
  }

  .image-upload-button {
    width: fit-content;
    height: 35px;
    background-color: #ffda77;
    border-radius: 40px;
    color: #2b2b2b;
    font-size: 0.9rem;
    font-weight: 500;
    border: none;
    box-shadow: rgba(0, 0, 0, 0.35) 3px 3px 3px;
    padding: 0px 10px;

    :active {
      box-shadow: none;
    }

    :disabled {
      cursor: not-allowed;
      opacity: 0.7;
      box-shadow: none;
    }
  }

  .detailed-address {
    margin-top: 20px;
  }

  .category-wrapper {
    height: 1.5rem;
    display: flex;
    /* border: 1px solid red; */
    /* width: 95%; */
  }

  .category {
    padding: 0px 5px;
    font-size: 1.05rem;
    display: flex;
    align-items: center;
    /* border: 1px solid black; */
  }

  .label {
    color: #2b2b2b;
  }

  .set-charge {
    width: 95%;
    display: flex;
    align-items: center;
    /* border: 1px solid red; */
  }

  .hour-description {
    width: fit-content;
    font-size: 1.1rem;
    color: #2b2b2b;
    /* border: 1px solid red; */
  }

  .capa-wrapper {
    width: 97%;
    display: flex;
    align-items: center;
    /* border: 10px solid red; */
  }

  .preview-image-wrapper {
    width: 97%;
    display: flex;
    align-items: center;
    margin-top: 15px;
    /* border: 5px solid red; */
  }

  img {
    /* width: 80px; */
    height: 80px;
    margin-left: 10px;
    border-radius: 10px;
  }
`;

const Input = styled.input`
  width: 94%;
  height: 1.5rem;
  font-size: 1rem;
  outline: none;
  /* border: none; */
  border: 3px solid #96c2ff;
  border-radius: 5px;
  color: #2b2b2b;
  padding: 5px;
`;

const SmallInput = styled.input`
  width: ${porps => porps.width};
  height: 1.5rem;
  font-size: 1.1rem;
  outline: none;
  border: 3px solid #96c2ff;
  border-radius: 5px;
  color: #2b2b2b;
  padding: 5px 5px 3px 5px;
  margin: 0px 5px;
  text-align: center;

  ::-webkit-inner-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
  ::-webkit-outer-spin-button {
    -webkit-appearance: none;
    margin: 0;
  }
`;

const Textarea = styled.textarea`
  width: 95%;
  height: 200px;
  font-size: 1rem;
  outline: none;
  border: 3px solid #96c2ff;
  border-radius: 5px;
  color: #2b2b2b;
  padding: 5px;
  resize: none;
`;

const LeftIcon = styled(FaCaretLeft)`
  font-size: 30px;
  color: #eb7470;

  :hover {
    cursor: pointer;
  }
`;

const RightIcon = styled(FaCaretRight)`
  font-size: 30px;
  color: #eb7470;

  :hover {
    cursor: pointer;
  }
`;
