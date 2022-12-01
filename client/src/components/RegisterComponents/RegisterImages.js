import React, { useRef } from 'react';
import styled from 'styled-components';

import { FaTimes } from 'react-icons/fa';

import useRegisterImages from './useRegisterImages';

function RegisterImages({ images, setImages }) {
  const { handleUploadImage, handleDeleteImage, previewImages } =
    useRegisterImages({
      images,
      setImages,
    });

  const hiddenFileInput = useRef(null);

  const handleImageSelect = () => {
    hiddenFileInput.current.click();
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
    font: inherit;
    font-size: 0.9rem;
    font-weight: 500;
    border: none;
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
