import React, { useState, useEffect } from 'react';

import Bottom from '../../Bottom';

import { getMyNotification } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { HOME } from '../../../constants/BottomItems';
import BottomBlank from '../../BottomBlank';
import Container from '@material-ui/core/Container';
import TopBar from './NotificationTopBar';
import NotificationItem from './NotificationItem';

const Notification = () => {
  const accessToken = getAccessTokenFromCookie();
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    getMyNotification(accessToken, setNotifications);
  }, [accessToken]);

  return (
    <>
      <TopBar backButtonLink='/home' />
      <Container>
        {notifications.map((notification) => (
          <NotificationItem key={notification.id} notification={notification} />
        ))}
        <BottomBlank />
      </Container>
      <Bottom selected={HOME} />
    </>
  );
};

export default Notification;
