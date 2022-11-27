import React, { useRef } from 'react';
import { useRecoilState } from 'recoil';
import styled from 'styled-components';

import { FaTimes } from 'react-icons/fa';
import { registerFormPreviewImage } from '../../atoms';
import {
  handleImageCompress,
  handleGetPreviewImagesUrl,
} from '../../utils/images';

// TODO:
// 1. 105번째줄 onClick 바꾸기
// 2. for eslint 오류 수정
function RegisterImages({ images, setImages }) {
  const [previewImages, setPreviewImages] = useRecoilState(
    registerFormPreviewImage,
  );

  const hiddenFileInput = useRef(null);

  const handleImageSelect = () => {
    hiddenFileInput.current.click();
  };

  const handleUploadImage = async event => {
    const selectedImages = event.target.files;
    const maxImages = selectedImages.length > 3 ? 3 : selectedImages.length;

    if (images.length + maxImages > 3 || selectedImages.length > 3) {
      alert('최대 3장까지만 가능합니다');
      return;
    }

    for (let i = 0; i < maxImages; i += 1) {
      // eslint-disable-next-line no-await-in-loop
      const compressedImage = await handleImageCompress(selectedImages[i]);
      // eslint-disable-next-line no-await-in-loop
      const compressedImageUrl = await handleGetPreviewImagesUrl(
        compressedImage,
      );

      setImages(image => [...image, compressedImage]);
      setPreviewImages(url => [...url, compressedImageUrl]);
    }
  };

  const handleDeleteImage = id => {
    setImages(images.filter((_, index) => index !== id));
    setPreviewImages(previewImages.filter((_, index) => index !== id));
  };

  return (
    <Wrapper>
      <div>
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
          onClick={handleImageSelect}
        >
          사진 업로드하기
        </button>
      </div>
      <PreviewImagesWrapper margin={previewImages.length > 0 && '15px'}>
        {previewImages.map((image, index) => (
          <PreviewImageWrapper key={`${image}`}>
            <img src={image} alt={`${image} - ${index}`} />
            <DeleteImageIcon onClick={() => handleDeleteImage(index)} />
          </PreviewImageWrapper>
        ))}
      </PreviewImagesWrapper>
    </Wrapper>
  );
}

const Wrapper = styled.div`
  width: 95%;

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
    margin-bottom: 5px;

    :active {
      box-shadow: none;
    }

    :disabled {
      cursor: not-allowed;
      opacity: 0.7;
      box-shadow: none;
    }
  }
`;

const PreviewImagesWrapper = styled.div`
  display: flex;
  align-items: center;
  margin-top: ${({ margin }) => margin};

  img {
    height: 80px;
    aspect-ratio: 1/1;
    border-radius: 10px;
  }
`;

const PreviewImageWrapper = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-right: 10px;
`;

const DeleteImageIcon = styled(FaTimes)`
  margin-top: 5px;
  font-size: 0.6rem;
  border-radius: 50%;
  background-color: #eb7470;
  color: white;
  padding: 2px;

  :hover {
    cursor: pointer;
  }
`;

export default RegisterImages;
