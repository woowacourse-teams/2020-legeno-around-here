import axios from 'axios';
import {setAccessTokenCookie} from '../../util/TokenUtils';

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
    .catch((error) => {
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
    .catch((error) => {
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
  } catch (error) {
    redirectLoginWhenUnauthorized(error);
    console.log(error);
  }
};

export const createComment = async (postId, writing, accessToken) => {
  const config = {
    headers: {
      'X-Auth-Token': accessToken,
    },
  };
  try {
    const response = await axios.post(
      DEFAULT_URL + `/posts/${postId}/comments`,
      { writing },
      config,
    );
    if (response.status === 201) {
      alert('댓글이 성공적으로 전송되었습니다!');
      return true;
    }
  } catch (error) {
    redirectLoginWhenUnauthorized(error);
    alert('댓글이 작성되지 않았습니다! 다시 작성해주세요!');
    console.log(error);
  }
  return false;
};

export const pressPostZzang = async (postId, accessToken) => {
  const config = {
    headers: {
      'X-Auth-Token': accessToken,
    },
  };
  try {
    const response = await axios.post(
      DEFAULT_URL + `/posts/${postId}/zzangs`,
      {},
      config,
    );
    if (response.status === 204) {
      return true;
    }
  } catch (error) {
    redirectLoginWhenUnauthorized(error);
    alert('짱이 눌러지지 않았습니다! 다시 작성해주세요!');
    console.log(error);
  }
  return false;
};

export const findMyInfo = ({
  accessToken,
  setEmail,
  setNickname,
  setProfilePhotoUrl,
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
      setEmail(userResponse.email);
      setNickname(userResponse.nickname);
      if (userResponse.image) {
        setProfilePhotoUrl(userResponse.image.url);
      }
    })
    .catch((error) => {
      redirectLoginWhenUnauthorized(error);
      alert(`회원정보를 가져올 수 없습니다.${error}`);
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
  return await axios
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
    .then((response) => response.data.content)
    .catch((error) => {
      redirectLoginWhenUnauthorized(error);
      console.log(`## 최근 글을 가져올 수 없습니다.`);
      console.log(`status code : ${error.response.status}`);
      console.log(`response 전체 : ${error.response}`);
      throw error.response;
    });
};

export const findAllAreas = async (page, accessToken, keyword) => {
  const config = {
    headers: {
      'X-Auth-Token': accessToken,
    },
  };
  return await axios
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
    .then((response) => {
      return response.data.content;
    })
    .catch((error) => {
      redirectLoginWhenUnauthorized(error);
      throw error.response;
    });
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
      redirectLoginWhenUnauthorized(error);
      alert(`부문정보를 가져올 수 없습니다.${error}`);
    });
  return response.data.content;
};

export const findPost = async (accessToken, postId) => {
  const config = {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-Auth-Token': accessToken,
    },
  };
  return await axios
    .get(DEFAULT_URL + '/posts/' + postId, config)
    .then((response) => {
      if (response.status === 200) {
        return response.data;
      }
    })
    .catch((error) => {
      redirectLoginWhenUnauthorized(error);
      alert(`자랑글을 가져올 수 없습니다.${error}`);
      document.location.href = '/';
    });
};

export const findCommentsByPostId = async (accessToken, postId) => {
  const config = {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-Auth-Token': accessToken,
    },
  };
  return await axios
    .get(DEFAULT_URL + `/posts/${postId}/comments`, config)
    .then((response) => {
      if (response.status === 200) {
        console.log('findCommentsByPostId : ' + response.data);
        return response.data;
      }
    })
    .catch((error) => {
      redirectLoginWhenUnauthorized(error);
      alert(`해당 글의 댓글을 가져올 수 없습니다.${error}`);
    });
};

const redirectLoginWhenUnauthorized = (error) => {
  if (error.response && error.response.status === 403) {
    document.location.href = '/login';
  }
};
