import { useRecoilValue } from 'recoil';
import { tokenAtom } from '../atoms';

const callHeader = () => {
  const tokenState = useRecoilValue(tokenAtom);

  const header = {
    headers: {
      Authorization: tokenState.ACCESS,
      RefreshToken: tokenState.REFRESH,
    },
  };
  return header;
};

export default callHeader;
