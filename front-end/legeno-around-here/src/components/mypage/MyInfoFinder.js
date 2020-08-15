import axios from 'axios';
import {SERVER_ADDRESS} from "../../constants/BackendAddress";

export const findMyInto = ({
  accessToken,
  setEmailState,
  setNicknameState,
}) => {
  const config = {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-Auth-Token': accessToken,
    },
  };
  axios
    .get(SERVER_ADDRESS + 'users/myinfo', config)
    .then(async (response) => {
      const userResponse = await response.data;
      setEmailState(userResponse.email);
      setNicknameState(userResponse.nickname);
    })
    .catch((error) => {
      alert(`회원정보를 가져올 수 없습니다.${error}`);
      document.location.href = '/';
    });
};
