import React, { useEffect, useState } from 'react';
import { useCookies } from 'react-cookie';
import { findAllSectors } from '../api/SectorApi';
import { withRouter } from 'react-router-dom';

const SectorsTable = ({ history }) => {
  const [data, setData] = useState(null);
  const [loading, setLoading] = useState(false);
  const [cookies, removeCookie] = useCookies(['accessToken']);

  useEffect(() => {
    const fetchData = async () => await findAllSectors(history, cookies, removeCookie, setData, setLoading);
    fetchData();
  }, [cookies, history]);

  if (loading) {
    return <>로딩중</>;
  }

  if (!data) {
    return null;
  }

  return (
    <>
      <div>부문 관리 - 부문 승인 요청을 승인/반려하는 페이지</div>
      {data && <textarea rows={10} value={JSON.stringify(data, null, 20)} readOnly={true} />}
    </>
  );
};

export default withRouter(SectorsTable);
