import React from 'react';

import PostingTopBar from './PostingTopBar';
import PostingForm from './PostingForm';
import Bottom from '../../Bottom';
import { WRITING } from '../../../constants/BottomItems';
import Container from '@material-ui/core/Container';
import SectorApplyButton from '../sector/SectorApplyButton';
import { Typography } from '@material-ui/core';

const PostingPage = () => {
  return (
    <>
      <PostingTopBar />
      <Container>
        <PostingForm />
        <Typography>
          아직 부문을 정하지 않으셨나요? <SectorApplyButton />을 해보세요!!
        </Typography>
      </Container>
      <Bottom selected={WRITING} />
    </>
  );
};

export default PostingPage;
