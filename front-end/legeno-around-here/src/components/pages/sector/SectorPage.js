import React, { useState, useEffect } from 'react';

import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import Sectors from './Sectors';
import { findAllSectors } from '../../api/API';
import SectorTopBar from './SectorTopBar';
import Bottom from '../../Bottom';

const SectorPage = () => {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [sectors, setSectors] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadSectors = async () => {
      setLoading(true);
      const allSectors = await findAllSectors(accessToken);
      setSectors(allSectors);
      setLoading(false);
    };
    loadSectors();
  }, [accessToken]);

  if (loading) return <div>Loading...</div>;
  return (
    <>
      <SectorTopBar />
      {sectors && <Sectors sectors={sectors} />}
      <Bottom />
    </>
  );
};

export default SectorPage;
