import React, { useEffect, useState } from 'react';
import BottomBlank from '../../BottomBlank';
import Bottom from '../../Bottom';
import { PROFILE } from '../../../constants/BottomItems';
import { findAllOtherAwards } from '../../api/API';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import Awards from '../myAward/Awards';
import ArrowBackTopBar from '../../topBar/ArrowBackTopBar';

const OtherAwardPage = ({ match, history }) => {
  const accessToken = getAccessTokenFromCookie();
  const [awards, setAwards] = useState([]);
  const [loading, setLoading] = useState(false);
  const userId = match.params.userId;

  useEffect(() => {
    const loadOtherAwards = async () => {
      setLoading(true);
      const foundMyAwards = await findAllOtherAwards(accessToken, userId, history);
      setAwards(foundMyAwards);
      setLoading(false);
    };
    loadOtherAwards();
  }, [accessToken, userId, history]);

  if (loading) {
    return <Loading />;
  }
  return (
    <>
      <ArrowBackTopBar />
      <Awards awards={awards} />
      <BottomBlank />
      <Bottom selected={PROFILE} />
    </>
  );
};

export default OtherAwardPage;
