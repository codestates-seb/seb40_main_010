/* eslint-disable consistent-return */
import React, { useRef } from 'react';
import styled from 'styled-components';
import { useRecoilState } from 'recoil';
import imageCompression from 'browser-image-compression';
import { FaTimes } from 'react-icons/fa';
import { registerFormPreviewImage } from '../../atoms';

// TODO
// 1. 이미지 업로드 속도가 느림
// 2. for await 해결 못함
function RegisterImages({ images, setImages }) {
  const [previewImages, setPreviewImages] = useRecoilState(
    registerFormPreviewImage,
  );

  const hiddenFileInput = useRef(null);

  const handleImageButtonClick = () => {
    hiddenFileInput.current.click();
  };

  async function handleImageCompress(file) {
    const options = {
      maxSizeMB: 0.01,
      maxWidthOrHeight: 1000,
    };
    try {
      const compressedFile = await imageCompression(file, options);
      const resultFile = new File([compressedFile], compressedFile.name, {
        type: compressedFile.type,
      });
      return resultFile;
    } catch (error) {
      console.log(error);
    }
  }

  async function handlePreviewImagesUrl(compressedFile) {
    // try {
    //   const url = await imageCompression.getDataUrlFromFile(compressedFile);
    //   return url;
    // } catch (error) {
    //   console.log(error);
    // }
    try {
      const url = URL.createObjectURL(compressedFile);
      return url;
    } catch (error) {
      console.log(error);
    }
  }

  const handleUploadImage = async e => {
    const selectedImages = e.target.files;
    const maxImages = selectedImages.length > 3 ? 3 : selectedImages.length;

    if (images.length + maxImages > 3 || selectedImages.length > 3) {
      alert('최대 3장까지만 가능합니다');
      return;
    }

    for (let i = 0; i < maxImages; i += 1) {
      // eslint-disable-next-line no-await-in-loop
      const compressedImage = await handleImageCompress(selectedImages[i]);
      // eslint-disable-next-line no-await-in-loop
      const compressedImageUrl = await handlePreviewImagesUrl(compressedImage);

      setImages(image => [...image, compressedImage]);
      setPreviewImages(url => [...url, compressedImageUrl]);
    }
    e.target.value = '';
  };

  const handleDeleteImage = id => {
    setImages(images.filter((_, index) => index !== id));
    setPreviewImages(previewImages.filter((_, index) => index !== id));
  };

  // console.log(images);
  // console.log(previewImages);

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
          onClick={handleImageButtonClick}
        >
          사진 업로드하기
        </button>
      </div>
      <PreviewImagesWrapper margin={previewImages.length > 0 ? '15px' : null}>
        {previewImages.map((image, id) => (
          <PreviewImageWrapper key={`${image}`}>
            <img src={image} alt={`${image} - ${id}`} />
            <DeleteImageIcon onClick={() => handleDeleteImage(id)} />
          </PreviewImageWrapper>
        ))}
      </PreviewImagesWrapper>
    </Wrapper>
  );
}

export default RegisterImages;

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
  margin-top: ${porps => porps.margin};
  img {
    height: 80px;
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
