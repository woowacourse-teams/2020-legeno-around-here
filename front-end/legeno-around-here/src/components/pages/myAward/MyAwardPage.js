import React, { useEffect, useState } from 'react';
import BottomBlank from '../../BottomBlank';
import Bottom from '../../Bottom';
import { PROFILE } from '../../../constants/BottomItems';
import { findMyAwards } from '../../api/API';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import Awards from './Awards';
import Container from '@material-ui/core/Container';
import ArrowBackTopBar from '../../topBar/ArrowBackTopBar';

const MyAwardPage = ({ history }) => {
  const accessToken = getAccessTokenFromCookie();
  const [awards, setAwards] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadMyAwards = async () => {
      setLoading(true);
      const foundMyAwards = await findMyAwards(accessToken, history);
      setAwards(foundMyAwards);
      setLoading(false);
    };
    loadMyAwards();
  }, [accessToken, history]);

  if (loading) {
    return <Loading />;
  }
  return (
    <>
      <ArrowBackTopBar />
      <Container>
        <Awards awards={awards} />
        <BottomBlank />
        <Bottom selected={PROFILE} />
      </Container>
    </>
  );
};

export default MyAwardPage;
