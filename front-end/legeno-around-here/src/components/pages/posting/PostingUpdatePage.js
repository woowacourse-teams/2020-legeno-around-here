import React from 'react';
import PostingTopBar from './PostingTopBar';
import Container from '@material-ui/core/Container';
import Bottom from '../../Bottom';
import { WRITING } from '../../../constants/BottomItems';
import PostingUpdateForm from './PostingUpdateForm';

const PostingUpdatePage = ({ match, history }) => {
  return (
    <>
      <PostingTopBar />
      <Container style={{ paddingTop: '60px' }}>
        <PostingUpdateForm postId={match.params.postId} history={history} />
      </Container>
      <Bottom selected={WRITING} />
    </>
  );
};

export default PostingUpdatePage;
