import React, { useState, useEffect } from 'react';
import Typography from '@material-ui/core/Typography';

import OutBox from './OutBox';
import { getAccessTokenFromCookie } from '../util/TokenUtils';
import { findPost } from './postdetail/PostFinder';
import BottomBar from './BottomBar';
import TopBar from './postdetail/TopBar';
import Image from './postdetail/Image';

const PostDetail = ({ match }) => {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const { postId } = match.params;
  const [post, setPost] = useState({
    id: null,
    writing: null,
    images: null,
    creator: null,
  });

  useEffect(() => {
    findPost({
      accessToken: accessToken,
      postId: postId,
      setPostState: setPost,
    });
  }, []);

  return (
    <>
      <OutBox>
        <TopBar backButtonLink="/"></TopBar>
        <Image src="/logo512.png"></Image>
        <Typography>{post.writing}</Typography>
      </OutBox>
      <BottomBar></BottomBar>
    </>
  );
};

export default PostDetail;
