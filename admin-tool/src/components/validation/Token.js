import React, { useEffect } from 'react';
import { useCookies } from 'react-cookie';

const Token = ({ history }) => {
  const [cookies] = useCookies(['accessToken']);

  useEffect(() => {
    if (cookies.accessToken && cookies.accessToken !== 'undefined') {
      return;
    }
    history.push('/login');
  }, [history, cookies.accessToken]);

  console.log(cookies);
  return <></>;
};

export default Token;
