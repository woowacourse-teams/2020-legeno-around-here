import React, { useState, useEffect } from 'react';

import OutBox from './OutBox';
import BottomBar from './BottomBar';
import TopBar from './postdetail/TopBar';
import PostBox from './postdetail/PostBox';

import { getAccessTokenFromCookie } from '../util/TokenUtils';
import { findPost } from './postdetail/PostFinder';

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
        {/* {postId} */}
        <PostBox writing={post.writing}></PostBox>
      </OutBox>
      <BottomBar></BottomBar>
    </>
  );
};

export default PostDetail;
