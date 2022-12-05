import { useRecoilState } from 'recoil';

import { registerFormPreviewImage } from '../../atoms';
import {
  handleImageCompress,
  handleGetPreviewImagesUrl,
} from '../../utils/images';

const useRegisterImages = ({ images, setImages }) => {
  const [previewImages, setPreviewImages] = useRecoilState(
    registerFormPreviewImage,
  );

  const handleUploadImage = async event => {
    const selectedImages = event.target.files;
    const maxImage = selectedImages.length > 3 ? 3 : selectedImages.length;

    if (images.length + maxImage > 3 || selectedImages.length > 3) {
      alert('최대 3장까지만 가능합니다');
      return;
    }

    Array.from(selectedImages).forEach(async selectedImage => {
      const compressedImage = await handleImageCompress(selectedImage);
      const compressedImageUrl = await handleGetPreviewImagesUrl(
        compressedImage,
      );

      setImages(image => [...image, compressedImage]);
      setPreviewImages(url => [...url, compressedImageUrl]);
    });
  };

  const handleDeleteImage = id => {
    setImages(images.filter((_, index) => index !== id));
    setPreviewImages(previewImages.filter((_, index) => index !== id));
  };

  return { handleUploadImage, handleDeleteImage, previewImages };
};

export default useRegisterImages;
