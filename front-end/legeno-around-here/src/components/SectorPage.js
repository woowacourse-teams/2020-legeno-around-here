import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { getAccessTokenFromCookie } from '../util/TokenUtils';
import Sectors from './sector/Sectors';
import Pagination from './sector/Pagination';
import OutBox from './OutBox';

function SectorPage() {
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
    const config = {
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
        'X-Auth-Token': accessToken,
      },
    };
    axios
      .get('http://localhost:8080/sectors?size=50', config)
      .then(async (response) => {
        const userResponse = await response.data;
        setSectors(userResponse.content);
        console.log(userResponse.content);
      })
      .catch((error) => {
        alert(`부문정보를 가져올 수 없습니다.${error}`);
      });
    setLoading(false);
  }, [accessToken]);
  if (loading) return <div>Loading...</div>;
  return (
    <OutBox>
      <Sectors sectors={currentSectors} />
      <Pagination
        sectorsPerPage={sectorsPerPage}
        totalSectors={sectors.length}
        paginate={paginate}
      />
    </OutBox>
  );
}

export default SectorPage;
