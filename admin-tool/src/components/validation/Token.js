import React, { useEffect } from 'react';
import { useCookies } from 'react-cookie';
import { withRouter } from 'react-router-dom';

const Token = ({ history }) => {
  const [cookies] = useCookies(['accessToken']);

  useEffect(() => {
    if (cookies.accessToken && cookies.accessToken !== 'undefined') {
      return;
    }
    history.push('/login');
  }, [history, cookies.accessToken]);

  return <></>;
};

export default withRouter(Token);
