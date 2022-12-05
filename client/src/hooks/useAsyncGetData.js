import axios from 'axios';
import header from '../utils/header';

export default async function getData(url, data) {
  try {
    if (data) {
      const response = await axios.get(url, data, header);
      return response;
    }
    const response = await axios.get(url, header);
    return response;
  } catch (error) {
    return console.log(error);
  }
}
