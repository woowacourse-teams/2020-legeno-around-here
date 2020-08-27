import React, { useState, useEffect } from 'react';

import Bottom from '../../Bottom';

import { getMyNotice } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { HOME } from '../../../constants/BottomItems';
import BottomBlank from '../../BottomBlank';
import Container from '@material-ui/core/Container';
import TopBar from './NoticeTopBar';
import NoticeItem from './NoticeItem';

const Notice = () => {
  const accessToken = getAccessTokenFromCookie();
  const [notices, setNotices] = useState([]);

  useEffect(() => {
    getMyNotice(accessToken, setNotices);
  }, [accessToken]);

  return (
    <>
      <TopBar backButtonLink='/home' />
      <Container>
        {notices.map((notice) => (
          <NoticeItem key={notice.id} notice={notice} />
        ))}
        <BottomBlank />
      </Container>
      <Bottom selected={HOME} />
    </>
  );
};

export default Notice;
