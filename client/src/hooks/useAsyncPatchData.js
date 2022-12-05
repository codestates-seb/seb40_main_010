import axios from 'axios';
import header from '../utils/header';

export default async function patchData(url, data) {
  try {
    if (data) {
      const response = await axios.patch(url, data, header);
      return response;
    }
    const response = await axios.patch(url, header);
    return response;
  } catch (error) {
    return console.log(error);
  }
}
