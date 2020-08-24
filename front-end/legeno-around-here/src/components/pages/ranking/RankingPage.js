import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';

import TopBar from './RankingTopBar';
import Bottom from '../../Bottom'
import BottomBlank from '../../BottomBlank'
import Loading from '../../Loading'
import { findRankedPostsFromPage } from '../../api/API'
import { getAccessTokenFromCookie } from '../../../util/TokenUtils'
import { RANKING } from '../../../constants/BottomItems'
import RankingItem from './RankingItem'

const RankingPage = () => {
  const [page, setPage] = useState(0);
  const [posts, setPosts] = useState([]);
  const [hasMore, setHasMore] = useState(true);
  const [criteria] = useState('total');

  const accessToken = getAccessTokenFromCookie();
  const mainAreaId = localStorage.getItem('mainAreaId');

  /* 처음에 보여줄 글 목록을 가져옴 */
  useEffect(() => {
    findRankedPostsFromPage(mainAreaId, criteria,0, accessToken).then((firstPosts) => {
      console.log(firstPosts)
      if (firstPosts.length === 0) {
        setHasMore(false);
        return;
      }
      setPosts(firstPosts);
    });
    setPage(1);
  }, [mainAreaId, accessToken]);

  const fetchNextPosts = () => {
    findRankedPostsFromPage(mainAreaId, criteria, page, accessToken)
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

  let zzangCountOfBeforePost = 0;
  let rankOfBeforePost = 0;

  return (
    <>
      <TopBar />
      <InfiniteScroll
        next={fetchNextPosts}
        hasMore={hasMore}
        loader={<Loading />}
        dataLength={posts.length}
        endMessage={<h3>모두 읽으셨습니다!</h3>}
      >
        {posts.map((post, index) => {
          const rank = (post.zzang.count === zzangCountOfBeforePost)? rankOfBeforePost : (index + 1);
          console.log(post.zzang.count === zzangCountOfBeforePost)
          zzangCountOfBeforePost = post.zzang.count;
          rankOfBeforePost = rank;
          console.log("###")
          return <RankingItem key={post.id} post={post} rank={rank} />;
        })}
      </InfiniteScroll>
      <BottomBlank />
      <Bottom selected={RANKING} />
    </>
  );
};

export default RankingPage;
