import React, { useEffect, useState } from 'react';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import Bottom from '../../Bottom';
import { findPost, findMyInfo } from '../../api/API';
import PostDetailTopBar from './PostDetailTopBar';
import PostDetail from './PostDetail';
import Loading from '../../Loading';
import { Container } from '@material-ui/core';
import BottomBlank from '../../BottomBlank';

const PostDetailPage = ({ match, history }) => {
  const postId = match.params.postId;
  const accessToken = getAccessTokenFromCookie();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(false);
  const [myInfo, setMyInfo] = useState(null);

  useEffect(() => {
    const loadMyInfo = async () => {
      setLoading(true);
      const foundMyInfo = await findMyInfo(accessToken, history);
      setMyInfo(foundMyInfo);
      setLoading(false);
    };

    const loadPost = async () => {
      setLoading(true);
      const post = await findPost(accessToken, postId, history);
      setPost(post);
      setLoading(false);
    };
    loadMyInfo();
    loadPost();
  }, [accessToken, postId, history]);

  if (loading) return <Loading />;
  return (
    <>
      <PostDetailTopBar />
      <Container>
        {post && <PostDetail post={post} myInfo={myInfo} history={history} />}
        <BottomBlank />
      </Container>
      <Bottom />
    </>
  );
};

export default PostDetailPage;
