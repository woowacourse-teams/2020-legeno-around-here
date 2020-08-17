import React, { useState, useEffect } from 'react';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import Bottom from '../../Bottom';
import { findPost } from '../../api/API';
import PostDetailTopBar from './PostDetailTopBar';
import PostDetail from './PostDetail';
import Loading from '../../Loading';
import { makeStyles, Container } from '@material-ui/core';

const useStyles = makeStyles(() => ({
  blankMargin: {
    marginBottom: 60,
  },
}));

const PostDetailPage = ({ match }) => {
  const classes = useStyles();

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
      <Container>
        {post && <PostDetail post={post} />}
        <div className={classes.blankMargin}></div>
      </Container>
      <Bottom />
    </>
  );
};

export default PostDetailPage;
