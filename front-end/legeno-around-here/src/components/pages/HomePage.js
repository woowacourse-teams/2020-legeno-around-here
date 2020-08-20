import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';

import AppBar from '../AppBar';
import Bottom from '../Bottom';

import { findCurrentPostsFromPage } from '../api/API';
import { getAccessTokenFromCookie } from '../../util/TokenUtils';
import { HOME } from '../../constants/BottomItems';
import PostItem from '../post/PostItem';
import Loading from '../Loading';
import BottomBlank from '../BottomBlank';

const HomePage = () => {
  const [page, setPage] = useState(0);
  const [posts, setPosts] = useState([]);
  const [hasMore, setHasMore] = useState(true);

  const accessToken = getAccessTokenFromCookie();
  const mainAreaId = localStorage.getItem('mainAreaId');

  /* 처음에 보여줄 최근글 목록을 가져옴 */
  useEffect(() => {
    findCurrentPostsFromPage(mainAreaId, 0, accessToken).then((firstPosts) => {
      if (firstPosts.length === 0) {
        setHasMore(false);
        return;
      }
      setPosts(firstPosts);
    });
    setPage(1);
  }, [mainAreaId, accessToken]);

  const fetchNextPosts = () => {
    findCurrentPostsFromPage(mainAreaId, page, accessToken)
      .then((nextPosts) => {
        if (nextPosts.length === 0) {
          setHasMore(false);
          return;
        }
        setPosts(posts.concat(nextPosts));
        setPage(page + 1);
      })
      .catch((e) => {
        console.log(e);
        setHasMore(false);
      });
  };

  return (
    <>
      <AppBar />
      <InfiniteScroll
        next={fetchNextPosts}
        hasMore={hasMore}
        loader={<Loading />}
        dataLength={posts.length}
        endMessage={<h3>모두 읽으셨습니다!</h3>}
      >
        {posts.map((post) => (
          <PostItem key={post.id} post={post} />
        ))}
      </InfiniteScroll>
      <BottomBlank />
      <Bottom selected={HOME} />
    </>
  );
};

export default HomePage;
