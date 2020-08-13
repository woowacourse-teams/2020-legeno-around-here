import React from 'react';

import OutBox from './OutBox';
import BottomBar from './BottomBar';

const PostDetail = ({ match }) => {
  const { postId } = match.params;
  return (
    <>
      <OutBox>{postId}</OutBox>
      <BottomBar></BottomBar>
    </>
  );
};

export default PostDetail;
