import { API } from './Api';

export const loginAdmin = ({ email, password }, initPassword, setCookie, history) => {
  console.log(email, password);

  API.post('/login', {
    email,
    password,
  })
    .then((response) => {
      const tokenResponse = response.data;
      setCookie('accessToken', tokenResponse.accessToken);
      history.push('/');
    })
    .catch(() => {
      alert('로그인에 실패했습니다. 계정을 다시 확인해주세요.');
      initPassword();
    });
};
