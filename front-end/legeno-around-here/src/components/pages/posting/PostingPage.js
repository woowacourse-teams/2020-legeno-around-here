import React from 'react';

import PostingTopBar from './PostingTopBar';
import PostingForm from './PostingForm';
import Bottom from '../../Bottom';
import { WRITING } from '../../../constants/BottomItems';
import Container from '@material-ui/core/Container';

const PostingPage = () => {
  return (
    <>
      <PostingTopBar />
      <Container style={{ paddingTop: '55px' }}>
        <PostingForm />
      </Container>
      <Bottom selected={WRITING} />
    </>
  );
};

export default PostingPage;
