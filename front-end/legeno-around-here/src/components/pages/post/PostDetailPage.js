import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import Typography from '@material-ui/core/Typography';

import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import Bottom from '../../Bottom';
import TopBar from '../../postdetail/TopBar';
import Image from '../../postdetail/Image';
import { findPost } from '../../api/API';
import PostDetailTopBar from './PostDetailTopBar';
import PostDetail from './PostDetail';
import Loading from '../../Loading';

const PostDetailPage = ({ match }) => {
  const postId = match.params.postId;
  const accessToken = getAccessTokenFromCookie();
  const [post, setPost] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadPost = async () => {
      setLoading(true);
      const post = await findPost(accessToken, postId);
      console.log(post);
      setPost(post);
      setLoading(false);
    };
    loadPost();
  }, [accessToken, postId]);

  if (loading) return <Loading />;
  return (
    <>
      <PostDetailTopBar />
      {post && <PostDetail post={post} />}
      <Bottom />
    </>
  );
};

export default PostDetailPage;
