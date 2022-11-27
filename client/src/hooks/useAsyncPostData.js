import axios from 'axios';
import header from '../utils/header';

export default async function postData(url, data) {
  try {
    if (data) {
      const response = await axios.post(url, data, header);
      return response;
    }
    const response = await axios.post(url, header);
    return response;
  } catch (error) {
    return console.log(error);
  }
}
