import axios from 'axios';
import { useEffect, useState } from 'react';

export default function useFetch(url) {
  const [data, setData] = useState(null);
  const [isPending, setIsPending] = useState(true);
  const [error, setError] = useState(null);

  useEffect(() => {
    const abortCont = new AbortController();

    setTimeout(async () => {
      try {
        const response = axios.get(url, { signal: abortCont.signal });
        setIsPending(false);
        setData(response);
        setError(null);
      } catch (err) {
        throw Error(err);
      }
    }, 1000);
    return () => abortCont.abort();
  }, [url]);

  return { data, isPending, error };
}
