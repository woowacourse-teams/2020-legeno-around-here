import axios from 'axios';
import { setAccessTokenCookie } from '../../util/TokenUtils';

const DEFAULT_SIZE = 10;
const DEFAULT_SORTED_BY = 'id';
const DEFAULT_DIRECTION = 'desc';
const DEFAULT_URL = 'https://back.capzzang.co.kr';

export const loginUser = (email, password, handleReset) => {
  axios
    .post(DEFAULT_URL + '/login', {
      email,
      password,
    })
    .then(async (response) => {
      const tokenResponse = await response.data;
      setAccessTokenCookie(tokenResponse.accessToken);
      alert('로그인되었습니다.');
      document.location.href = '/home';
    })
    .catch(() => {
      alert('로그인에 실패하였습니다.');
      handleReset();
    });
};

export const createUser = (email, nickname, password, handleReset) => {
  axios
    .post(DEFAULT_URL + '/join', {
      email,
      nickname,
      password,
    })
    .then((response) => {
      alert('회원가입을 축하드립니다.');
      document.location.href = '/login';
    })
    .catch(() => {
      alert('회원가입에 실패하였습니다.');
      handleReset();
    });
};

export const createPost = async (formData, accessToken) => {
  const config = {
    headers: {
      'X-Auth-Token': accessToken,
    },
  };
  try {
    const response = await axios.post(DEFAULT_URL + '/posts', formData, config);
    if (response.status === 201) {
      alert('전송에 성공했습니다!');
      document.location.href = response.headers.location;
    }
  } catch (e) {
    console.log(e);
  }
};

export const findMyInfo = ({
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
    .get(DEFAULT_URL + '/users/myinfo', config)
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

export const findCurrentPostsFromPage = async (
  mainAreaId,
  page,
  accessToken,
) => {
  const config = {
    headers: {
      'X-Auth-Token': accessToken,
    },
  };
  const response = await axios
    .get(
      DEFAULT_URL +
        `/posts?` +
        `page=${page}&` +
        `size=${DEFAULT_SIZE}&` +
        `sortedBy=${DEFAULT_SORTED_BY}&` +
        `direction=${DEFAULT_DIRECTION}&` +
        `areaIds=${mainAreaId}&` +
        `sectorIds=`,
      config,
    )
    .catch((error) => {
      console.log(error);
      console.log(`최근 글을 가져올 수 없습니다! error : ${error}`);
      document.location.href = '/login';
    });
  return response.data.content;
};

export const findAllAreas = async (page, accessToken, keyword) => {
  const config = {
    headers: {
      'X-Auth-Token': accessToken,
    },
  };
  const response = await axios
    .get(
      DEFAULT_URL +
        `/areas?` +
        `page=${page}&` +
        `size=${DEFAULT_SIZE}&` +
        `sortedBy=${DEFAULT_SORTED_BY}&` +
        `direction=${DEFAULT_DIRECTION}&` +
        `keyword=${keyword}`,
      config,
    )
    .catch(() => {
      alert(`해당하는 지역이 없습니다!`);
    });
  return response.data.content;
};

export const findAllSectors = async (accessToken) => {
  const config = {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-Auth-Token': accessToken,
    },
  };
  const response = await axios
    .get(DEFAULT_URL + '/sectors?size=50', config)
    .catch((error) => {
      alert(`부문정보를 가져올 수 없습니다.${error}`);
    });
  return response.data.content;
};

export const findPost = ({ accessToken, postId, setPostState }) => {
  const config = {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-Auth-Token': accessToken,
    },
  };
  axios
    .get(DEFAULT_URL + '/posts/' + postId, config)
    .then(async (response) => {
      const postResponse = await response.data;
      console.log(postResponse);
      setPostState({
        id: postResponse.id,
        writing: postResponse.writing,
        images: postResponse.images,
        areaName: postResponse.area.fullName,
        sectorName: postResponse.sector.name,
        creatorName: postResponse.creator.nickname,
        zzangCount: postResponse.zzang.count,
        comments: postResponse.comments,
      });
    })
    .catch((error) => {
      alert(`자랑글을 가져올 수 없습니다.${error}`);
      // document.location.href = '/';
    });
};
