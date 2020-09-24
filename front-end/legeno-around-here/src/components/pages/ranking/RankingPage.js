import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';
import { makeStyles } from '@material-ui/core/styles';

import Bottom from '../../Bottom';
import BottomBlank from '../../BottomBlank';
import Loading from '../../Loading';
import { findRankedPostsFromPage } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { RANKING } from '../../../constants/BottomItems';
import RankingItem from './RankingItem';
import FormControl from '@material-ui/core/FormControl';
import Select from '@material-ui/core/Select';
import Container from '@material-ui/core/Container';
import SearchTopBar from '../../topBar/SearchTopBar';
import EndMessage from '../../EndMessage';
import Typography from '@material-ui/core/Typography';
import { getMainAreaId } from '../../../util/localStorageUtils';

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
}));

const RankingPage = ({ location, history }) => {
  const classes = useStyle();
  const accessToken = getAccessTokenFromCookie();

  const [page, setPage] = useState(0);
  const [posts, setPosts] = useState([]);
  const [hasMore, setHasMore] = useState(true);
  const [areaId, setAreaId] = useState(getMainAreaId());
  const [sectorId, setSectorId] = useState('none');
  const [locationParams, setLocationParams] = useState(location.search);
  const [criteria, setCriteria] = useState('total');

  const removeContent = () => {
    setPage(0);
    setPosts([]);
    setLocationParams('');
  };

  const topBarSetters = { setAreaId, setSectorId, removeContent };
  const topBarGetters = { areaId, sectorId };

  useEffect(() => {
    setHasMore(true);
    loadNextPosts();
    // eslint-disable-next-line
  }, [areaId, sectorId, criteria]);

  const loadNextPosts = () => {
    let selectedSectorId;
    if (locationParams.includes('?sectorId=')) {
      selectedSectorId = locationParams.split('?sectorId=')[1];
    } else if (sectorId === 'none') {
      selectedSectorId = '';
    } else {
      selectedSectorId = sectorId;
    }

    findRankedPostsFromPage(areaId, selectedSectorId, criteria, page, accessToken, history)
      .then((nextPosts) => {
        if (!nextPosts || nextPosts.length === 0) {
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

  const handleChange = (event) => {
    const targetValue = event.target.value;
    if (criteria === targetValue) {
      return;
    }
    removeContent();
    setCriteria(targetValue);
  };

  /* 공동순위 처리를 위해서 필요한 변수들 */
  let zzangCountOfBeforePost = -1;
  let rankOfBeforePost = 0;

  return (
    <>
      <SearchTopBar setter={topBarSetters} getter={topBarGetters} history={history} selected={'ranking'} />
      <Container>
        <div className={classes.filterSection}>
          <FormControl className={classes.durationFilter}>
            <Select native value={criteria} onChange={handleChange}>
              <option value='total'>역대</option>
              <option value='month'>월간</option>
              <option value='week'>주간</option>
              <option value='yesterday'>어제 하루</option>
            </Select>
            <Typography className={classes.durationFilter}>&nbsp;짱을 가장 많이 받은 자랑글</Typography>
          </FormControl>
        </div>
        <InfiniteScroll
          next={loadNextPosts}
          hasMore={hasMore}
          loader={<Loading />}
          dataLength={posts.length}
          endMessage={<EndMessage message={'모두 읽으셨습니다!'} />}
        >
          {posts.map((post, index) => {
            const rank = post.zzang.count === zzangCountOfBeforePost ? rankOfBeforePost : index + 1;
            zzangCountOfBeforePost = post.zzang.count;
            rankOfBeforePost = rank;
            return (
              <RankingItem
                key={post.id}
                post={post}
                rank={rank}
                whetherToPrintZzangCount={criteria === `total`}
                history={history}
              />
            );
          })}
        </InfiniteScroll>
        <BottomBlank />
      </Container>
      <Bottom selected={RANKING} />
    </>
  );
};

export default RankingPage;
