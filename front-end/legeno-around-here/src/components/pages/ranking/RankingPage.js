import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { makeStyles } from '@material-ui/core/styles';

import TopBar from './RankingTopBar';
import Bottom from '../../Bottom';
import BottomBlank from '../../BottomBlank';
import Loading from '../../Loading';
import { findRankedPostsFromPage } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { RANKING } from '../../../constants/BottomItems';
import RankingItem from './RankingItem';
import Typography from '@material-ui/core/Typography';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';

const useStyle = makeStyles(() => ({
  filterSection: {
    display: 'flex',
    width: '95%',
    height: '30px',
    margin: 'auto',
    marginTop: '10px',
    marginBottom: '10px',
    fontSize: '15px',
    color: '#555555',
  },
  durationFilter: {
    display: 'inline-block',
    marginRight: 'auto',
  },
  sectorFilter: {
    display: 'inline-block',
    marginLeft: 'auto',
  },
}));

const RankingPage = () => {
  const [page, setPage] = useState(0);
  const [posts, setPosts] = useState([]);
  const [hasMore, setHasMore] = useState(true);
  const [criteria, setCriteria] = useState('total');

  const accessToken = getAccessTokenFromCookie();
  const mainAreaId = localStorage.getItem('mainAreaId');
  const classes = useStyle();

  /* 처음에 보여줄 글 목록을 가져옴 */
  useEffect(() => {
    findRankedPostsFromPage(mainAreaId, criteria, 0, accessToken)
      .then((firstPosts) => {
        if (!firstPosts || firstPosts.length === 0) {
          setHasMore(false);
          return;
        }
        setPosts(firstPosts);
      })
      .catch((e) => {
        console.log(e);
        setHasMore(false);
      });
    setPage(1);
  }, [mainAreaId, accessToken, criteria]);

  const fetchNextPosts = () => {
    findRankedPostsFromPage(mainAreaId, criteria, page, accessToken)
      .then((nextPosts) => {
        if (!nextPosts || nextPosts.length === 0) {
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

  const handleChange = (event) => {
    setCriteria(event.target.value);
  };

  /* 공동순위 처리를 위해서 필요한 변수들 */
  let zzangCountOfBeforePost = -1;
  let rankOfBeforePost = 0;

  return (
    <>
      <TopBar />
      <div className={classes.filterSection}>
        <FormControl className={classes.durationFilter}>
          <Select native value={criteria} onChange={handleChange}>
            <option value="total">역대</option>
            <option value="month">월간</option>
            <option value="week">주간</option>
            <option value="yesterday">어제</option>
          </Select>
        </FormControl>

        <Typography className={classes.sectorFilter}>부문 전체</Typography>
      </div>
      <InfiniteScroll
        next={fetchNextPosts}
        hasMore={hasMore}
        loader={<Loading />}
        dataLength={posts.length}
        endMessage={<h3>모두 읽으셨습니다!</h3>}
      >
        {posts.map((post, index) => {
          const rank =
            post.zzang.count === zzangCountOfBeforePost
              ? rankOfBeforePost
              : index + 1;
          zzangCountOfBeforePost = post.zzang.count;
          rankOfBeforePost = rank;
          return (
            <RankingItem
              key={post.id}
              post={post}
              rank={rank}
              whetherToPrintZzangCount={criteria === `total`}
            />
          );
        })}
      </InfiniteScroll>
      <BottomBlank />
      <Bottom selected={RANKING} />
    </>
  );
};

export default RankingPage;
