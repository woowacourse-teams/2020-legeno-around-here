import React from 'react';

import PostingTopBar from './PostingTopBar';
import PostingForm from './PostingForm';
import Container from '@material-ui/core/Container';

const PostingPage = (match) => {
  return (
    <>
      <PostingTopBar />
      <Container style={{ paddingTop: '60px' }}>
        <PostingForm history={match.history} />
      </Container>
    </>
  );
};

export default PostingPage;
