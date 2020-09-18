import axios from 'axios';

const API = axios.create({
  baseURL: `${process.env.REACT_APP_API_SERVER_HOST}`,
  timeout: 1000,
});

export const makeHeader = (cookies) => {
  const accessToken = cookies['accessToken'];
  return {
    headers: {
      'X-Auth-Token': accessToken,
    },
  };
};

export const redirectInvalidToken = (history, status, removeCookie) => {
  if (status === 403 || status === 401) {
    alert('권한 없음! 다시 로그인해주시기 바랍니다.');
    removeCookie('accessToken');
    history.push('/');
  }
};

export default API;
