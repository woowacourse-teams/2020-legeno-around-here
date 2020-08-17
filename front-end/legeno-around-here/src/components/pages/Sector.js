import React, { useState, useEffect } from 'react';

import { getAccessTokenFromCookie } from '../../util/TokenUtils';
import Sectors from '../sector/Sectors';
import Pagination from '../sector/Pagination';
import Bottom from '../Bottom';
import { SECTOR } from '../../constants/BottomItems';
import { findAllSectors } from '../api/API';

function Sector() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [sectors, setSectors] = useState([]);
  const [loading, setLoading] = useState(false);
  const [currentPage, setCurrentPage] = useState(1);
  const [sectorsPerPage] = useState(9);

  const indexOfLastPost = currentPage * sectorsPerPage;
  const indexOfFirstPost = indexOfLastPost - sectorsPerPage;
  const currentSectors = sectors.slice(indexOfFirstPost, indexOfLastPost);
  const paginate = (pageNumber) => setCurrentPage(pageNumber);

  useEffect(() => {
    setLoading(true);
    findAllSectors(setSectors, accessToken);
    setLoading(false);
  }, [accessToken]);

  if (loading) return <div>Loading...</div>;

  return (
    <>
      <Sectors sectors={currentSectors} />
      <Pagination
        sectorsPerPage={sectorsPerPage}
        totalSectors={sectors.length}
        paginate={paginate}
      />
      <br />
      <br />
      <br />
      <Bottom selected={SECTOR}></Bottom>
    </>
  );
}

export default Sector;
