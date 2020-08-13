import React, { useState, useEffect } from 'react';
import AppBar from '../AppBar';
import BottomBar from '../BottomBar';

import { getAllCurrentPosts } from '../api/API';
import { getAccessTokenFromCookie } from '../../util/TokenUtils';
import { HOME } from '../../constants/BottomItems';
import PostItem from '../post/PostItem';

const HomePage = () => {
  const [page] = useState(1);
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const accessToken = getAccessTokenFromCookie();
    const loadPosts = async () => {
      setLoading(true);
      const allPosts = await getAllCurrentPosts(page, accessToken);
      setPosts(allPosts);
      setLoading(false);
    };
    loadPosts();
  }, [page]);

  return (
    <>
      <AppBar />
      {posts && posts.map((post) => <PostItem key={post.id} post={post} />)}
      <BottomBar selected={HOME} />
      {loading && <div>Loading ...</div>}
    </>
  );
};

export default HomePage;
