import React, { useEffect, useState } from 'react';
import { findAllMySector } from '../../api/API';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { Divider, Typography } from '@material-ui/core';
import MySectors from './MySectors';

const MySectorSection = () => {
  const accessToken = getAccessTokenFromCookie();
  const [mySectors, setMySectors] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadMySectors = async () => {
      setLoading(true);
      const sectors = await findAllMySector(accessToken);
      setMySectors(sectors);
      setLoading(false);
    };
    loadMySectors();
  }, [accessToken]);

  if (loading) {
    return <Loading />;
  }
  return (
    <>
      <Typography align={'center'}>나의 부문 관리</Typography>
      <Divider />
      {mySectors ? <MySectors mySectors={mySectors} /> : <Typography>현재 신청중인 부문이 없습니다!</Typography>}
    </>
  );
};

export default MySectorSection;
