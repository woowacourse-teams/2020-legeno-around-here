import React, { useState, useEffect } from 'react';
import AppBar from '../AppBar';
import Bottom from '../Bottom';

import { findAllCurrentPosts } from '../api/API';
import { getAccessTokenFromCookie } from '../../util/TokenUtils';
import { HOME } from '../../constants/BottomItems';
import PostItem from '../post/PostItem';

const Home = () => {
  const [page] = useState(0);
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const accessToken = getAccessTokenFromCookie();
    const mainAreaId = localStorage.getItem('mainAreaId');

    const loadPosts = async () => {
      setLoading(true);
      const allPosts = await findAllCurrentPosts(mainAreaId, page, accessToken);
      setPosts(allPosts);
      setLoading(false);
    };
    loadPosts();
  }, [page]);

  return (
    <>
      <AppBar />
      {posts && posts.map((post) => <PostItem key={post.id} post={post} />)}
      <Bottom selected={HOME} />
      {loading && <div>Loading ...</div>}
    </>
  );
};

export default Home;
