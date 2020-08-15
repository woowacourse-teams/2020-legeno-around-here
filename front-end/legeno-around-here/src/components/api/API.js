import axios from 'axios';
import {SERVER_ADDRESS} from "../../constants/BackendAddress";

const DEFAULT_SIZE = 10;
const DEFAULT_SORTED_BY = 'id';
const DEFAULT_DIRECTION = 'desc';

export const getAllCurrentPosts = async (mainAreaId, page, accessToken) => {
  const config = {
    headers: {
      'X-Auth-Token': accessToken,
    },
  };
  const response = await axios
    .get(
      SERVER_ADDRESS + `posts?` +
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

export const getAllAreas = async (page, accessToken, keyword) => {
  const config = {
    headers: {
      'X-Auth-Token': accessToken,
    },
  };
  const response = await axios
    .get(
      SERVER_ADDRESS + `areas?` +
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
