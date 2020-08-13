import React from 'react';

import OutBox from './OutBox';
import BottomBar from './BottomBar';
import TopBar from './postdetail/TopBar';
import PostBox from './postdetail/PostBox';

const PostDetail = ({ match }) => {
  const { postId } = match.params;
  return (
    <>
      <OutBox>
        <TopBar backButtonLink="/"></TopBar>
        {/* {postId} */}
        <PostBox></PostBox>
      </OutBox>
      <BottomBar></BottomBar>
    </>
  );
};

export default PostDetail;
