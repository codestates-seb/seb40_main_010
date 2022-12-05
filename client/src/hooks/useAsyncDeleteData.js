import axios from 'axios';
import header from '../utils/header';

export default async function deleteData(url, data) {
  try {
    if (data) {
      const response = await axios.delete(url, data, header);
      return response;
    }
    const response = await axios.delete(url, header);
    return response;
  } catch (error) {
    return console.log(error);
  }
}
