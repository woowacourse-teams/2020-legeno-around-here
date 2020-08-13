import axios from 'axios';

export const findPost = ({ accessToken, postId, setPostState }) => {
  const config = {
    headers: {
      'Content-Type': 'application/x-www-form-urlencoded',
      'X-Auth-Token': accessToken,
    },
  };
  axios
    .get('http://capzzang.co.kr/posts/' + postId, config)
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
