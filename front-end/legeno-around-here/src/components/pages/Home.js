import React, {useEffect, useState} from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';

import AppBar from '../AppBar';
import Bottom from '../Bottom';
import {getAllCurrentPosts} from '../api/API';
import {getAccessTokenFromCookie} from '../../util/TokenUtils';
import {HOME} from '../../constants/BottomItems';
import PostItem from '../post/PostItem';

const Home = () => {
  const [page, setPage] = useState(0);
  const [posts, setPosts] = useState([]);
  const [hasMore, setHasMore] = useState(true);

  const accessToken = getAccessTokenFromCookie();
  const mainAreaId = localStorage.getItem('mainAreaId');

  /* 처음부터 보여줄 최근글 목록을 가져옴 */
  useEffect(() => {
    getAllCurrentPosts(mainAreaId, 0, accessToken)
      .then(firstPosts => {
        setPosts(firstPosts);
      });
    setPage(1);
  }, [mainAreaId, accessToken]);

  const fetchNextPosts = () => {
    getAllCurrentPosts(mainAreaId, page, accessToken)
      .then(nextPosts => {
        setPosts(posts.concat(nextPosts));
        setPage(page + 1);
      })
      .catch(e => {
        console.log(e);
        setHasMore(false);
      });

    const nextPage = page + 1;
    setPage(nextPage);
  };

  return (
    <>
      <AppBar />
      <InfiniteScroll
        next={fetchNextPosts}
        hasMore={hasMore}
        loader={<div>Loading ...</div>}
        dataLength={posts.length}
      >
        {posts.map((post) => <PostItem key={post.id} post={post} />)}
      </InfiniteScroll>
      <br /><br /><br /><br />
      <Bottom selected={HOME} />
    </>
  );
};

export default Home;
