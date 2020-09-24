import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';

import TopBar from './OtherPorfileTopBar';
import Bottom from '../../Bottom';
import { PROFILE } from '../../../constants/BottomItems';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { findOtherPostsFromPage } from '../../api/API';
import PostItem from '../../PostItem';
import BottomBlank from '../../BottomBlank';
import EndMessage from '../../EndMessage';

function OtherPosts({ match, history }) {
  const [page, setPage] = useState(0);
  const [posts, setPosts] = useState([]);
  const [hasMore, setHasMore] = useState(true);
  const otherUserId = match.params.userId;

  const accessToken = getAccessTokenFromCookie();

  useEffect(() => {
    findOtherPostsFromPage(otherUserId, 0, accessToken, history).then((firstPosts) => {
      if (firstPosts.length === 0) {
        setHasMore(false);
        return;
      }
      setPosts(firstPosts);
    });
    setPage(1);
  }, [otherUserId, accessToken, history]);

  const fetchNextPosts = () => {
    findOtherPostsFromPage(otherUserId, page, accessToken, history)
      .then((nextPosts) => {
        if (nextPosts.length === 0) {
          setHasMore(false);
          return;
        }
        setPosts(posts.concat(nextPosts));
        setPage(page + 1);
      })
      .catch((e) => {
        setHasMore(false);
      });
  };

  return (
    <>
      <TopBar />
      <InfiniteScroll
        next={fetchNextPosts}
        hasMore={hasMore}
        loader={<Loading />}
        dataLength={posts.length}
        endMessage={<EndMessage message={'모두 읽으셨습니다!'} />}
      >
        {posts.map((post) => (
          <PostItem key={post.id} post={post} history={history} />
        ))}
      </InfiniteScroll>
      <BottomBlank />
      <Bottom selected={PROFILE} />
    </>
  );
}

export default OtherPosts;
