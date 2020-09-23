import React from 'react';
import Redirect from 'react-router-dom/es/Redirect';
import { setAccessTokenCookie } from '../../util/TokenUtils';
import * as qs from 'qs';

const OAuth2RedirectHandler = ({ location }) => {
  const query = qs.parse(location.search, {
    ignoreQueryPrefix: true,
  });

  const token = query.token;
  const error = query.error;

  if (token) {
    console.log('TOKEN EXISTS');
    setAccessTokenCookie(token);
    return <Redirect to='/' />;
  }

  console.log('TOKEN NOT EXISTS');
  return (
    <>
      <div>{error}</div>
      <div>로그인에 실패하셨습니다.</div>
    </>
  );
};

export default OAuth2RedirectHandler;
