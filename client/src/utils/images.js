import imageCompression from 'browser-image-compression';

const handleImageCompress = async file => {
  const options = {
    maxSizeMB: 0.1,
    maxWidthOrHeight: 1000,
  };
  try {
    const compressedFile = await imageCompression(file, options);
    const resultFile = new File([compressedFile], compressedFile.name, {
      type: compressedFile.type,
    });
    return resultFile;
  } catch (error) {
    return console.log(error);
  }
};

const handleGetPreviewImagesUrl = async compressedFile => {
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
    return console.log(error);
  }
};

export { handleImageCompress, handleGetPreviewImagesUrl };
