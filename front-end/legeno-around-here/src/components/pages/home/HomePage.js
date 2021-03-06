import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import Bottom from '../../Bottom';

import { findCurrentPostsFromPage } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { HOME } from '../../../constants/BottomItems';
import Loading from '../../Loading';
import BottomBlank from '../../BottomBlank';
import Container from '@material-ui/core/Container';
import PostItem from '../../PostItem';
import EndMessage from '../../EndMessage';
import SearchTopBar from '../../topBar/SearchTopBar';
import { getMainAreaId, getMainSectorId } from '../../../util/localStorageUtils';

const HomePage = ({ history }) => {
  const accessToken = getAccessTokenFromCookie();

  const [page, setPage] = useState(0);
  const [posts, setPosts] = useState([]);
  const [hasMore, setHasMore] = useState(false);
  const [areaId, setAreaId] = useState(getMainAreaId());
  const [sectorId, setSectorId] = useState(getMainSectorId());

  const removeContent = () => {
    setPage(0);
    setPosts([]);
  };

  const topBarSetters = { setAreaId, setSectorId, removeContent };
  const topBarGetters = { areaId, sectorId };

  useEffect(() => {
    setHasMore(true);
    loadNextPosts();
    // eslint-disable-next-line
  }, [areaId, sectorId]);

  const loadNextPosts = async () => {
    try {
      const nextPosts = await findCurrentPostsFromPage(page, accessToken, areaId, sectorId, history);
      if (nextPosts.length === 0) {
        setHasMore(false);
        return;
      }
      setPosts(posts.concat(nextPosts));
      setPage(page + 1);
    } catch (error) {
      setHasMore(false);
    }
  };

  return (
    <>
      <SearchTopBar setter={topBarSetters} getter={topBarGetters} history={history} selected={'ranking'} />
      <Container>
        <InfiniteScroll
          style={{ overflowY: 'hidden' }}
          next={loadNextPosts}
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
      </Container>
      <Bottom selected={HOME} />
    </>
  );
};

export default HomePage;
