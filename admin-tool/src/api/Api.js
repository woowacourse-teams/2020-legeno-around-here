import axios from 'axios'

/*
    axios 인스턴스를 생성합니다.
    생성할때 사용하는 옵션들 (baseURL, timeout, headers 등)은 다음 URL에서 확인할 수 있습니다.
    https://github.com/axios/axios 의 Request Config 챕터 확인
*/
export const API = axios.create({
  baseURL: `${process.env.REACT_APP_API_SERVER_HOST}`,
  timeout: 1000
});
