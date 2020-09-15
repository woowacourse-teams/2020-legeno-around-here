import React, { useEffect, useState } from 'react';
import InfiniteScroll from 'react-infinite-scroll-component';

import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import Sectors from './Sectors';
import { findSectorsFromPage } from '../../api/API';
import SectorTopBar from './SectorTopBar';
import Bottom from '../../Bottom';
import Loading from '../../Loading';
import { Container, Typography } from '@material-ui/core';
import BottomBlank from '../../BottomBlank';

const SectorPage = ({ history }) => {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [sectors, setSectors] = useState([]);
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(false);

  const loadNextSectors = async () => {
    try {
      const nextSectors = await findSectorsFromPage(page, accessToken, history);
      if (nextSectors.length === 0) {
        setHasMore(false);
        return;
      }
      setSectors(sectors.concat(nextSectors));
      setPage(page + 1);
    } catch (error) {
      setHasMore(false);
    }
  };

  useEffect(() => {
    setHasMore(true);
    loadNextSectors();
    // eslint-disable-next-line
  }, []);

  return (
    <>
      <SectorTopBar />
      <Container>
        <InfiniteScroll
          next={loadNextSectors}
          hasMore={hasMore}
          loader={<Loading />}
          dataLength={sectors.length}
          endMessage={<Typography>모든 부문을 확인하셨습니다!</Typography>}
        >
          {sectors && <Sectors sectors={sectors} />}
        </InfiniteScroll>
        <BottomBlank />
      </Container>
      <Bottom selected='sector' />
    </>
  );
};

export default SectorPage;
