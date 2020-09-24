import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';

import ArrowBackTopBar from '../../topBar/ArrowBackTopBar';
import Bottom from '../../Bottom';
import { PROFILE } from '../../../constants/BottomItems';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { findMyPostsFromPage } from '../../api/API';
import PostItem from '../../PostItem';
import BottomBlank from '../../BottomBlank';
import Container from '@material-ui/core/Container';
import EndMessage from '../../EndMessage';

function MyPosts({ history }) {
  const [page, setPage] = useState(0);
  const [posts, setPosts] = useState([]);
  const [hasMore, setHasMore] = useState(true);

  const accessToken = getAccessTokenFromCookie();
  const mainAreaId = localStorage.getItem('mainAreaId');

  useEffect(() => {
    findMyPostsFromPage(mainAreaId, 0, accessToken, history).then((firstPosts) => {
      if (firstPosts.length === 0) {
        setHasMore(false);
        return;
      }
      setPosts(firstPosts);
    });
    setPage(1);
  }, [mainAreaId, accessToken, history]);

  const fetchNextPosts = () => {
    findMyPostsFromPage(mainAreaId, page, accessToken, history)
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
      <ArrowBackTopBar backButtonLink='/users/me' />
      <Container>
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
      </Container>
    </>
  );
}

export default MyPosts;
