import { API } from './Api';
import { setAccessTokenCookie } from '../util/TokenUtil';

export const loginAdmin = ({ email, password }, initPassword, history ) => {
  console.log(email, password);

  API.post('/login', {
    email,
    password,
  })
    .then(response => {
      const tokenResponse = response.data;
      setAccessTokenCookie(tokenResponse.accessToken);
      history.push('/');
    })
    .catch(() => {
        alert('로그인에 실패했습니다. 계정을 다시 확인해주세요.');
        initPassword();
      },
    );
};