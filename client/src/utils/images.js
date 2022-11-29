import imageCompression from 'browser-image-compression';

const handleImageCompress = async file => {
  const options = {
    maxSizeMB: 0.1,
    maxWidthOrHeight: 1000,
  };
  const compressedFile = await imageCompression(file, options);
  const resultFile = new File([compressedFile], compressedFile.name, {
    type: compressedFile.type,
  });
  return resultFile;
};

const handleGetPreviewImagesUrl = async compressedFile => {
  const url = await imageCompression.getDataUrlFromFile(compressedFile);
  return url;
};

export { handleImageCompress, handleGetPreviewImagesUrl };
