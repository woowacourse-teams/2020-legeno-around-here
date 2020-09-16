import React, { useEffect } from 'react';
import { useCookies } from 'react-cookie';

const MainPage = ({ history }) => {
  const [cookies] = useCookies(['accessToken']);

  useEffect(() => {
    if (cookies.accessToken && cookies.accessToken !== 'undefined') {
      return;
    }
    history.push('/login');
  }, [history, cookies.accessToken]);

  console.log(cookies);

  return (
    <>
      <div>메인 페이지</div>
    </>
  );
};

export default MainPage;
