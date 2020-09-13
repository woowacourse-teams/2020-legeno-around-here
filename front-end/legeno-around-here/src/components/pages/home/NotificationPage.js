import React, { useState, useEffect } from 'react';

import Bottom from '../../Bottom';

import { getMyNotification } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { HOME } from '../../../constants/BottomItems';
import BottomBlank from '../../BottomBlank';
import Container from '@material-ui/core/Container';
import TopBar from './NotificationTopBar';
import NotificationItem from './NotificationItem';
import Typography from '@material-ui/core/Typography'
import makeStyles from '@material-ui/core/styles/makeStyles'

const useStyle = makeStyles({
  nothingToNotice: {
    margin: '30px auto',
    textAlign: 'center',
    fontSize: '20px',
  },
});

const NotificationPage = ({history}) => {
  const classes = useStyle();
  const accessToken = getAccessTokenFromCookie();
  const [notifications, setNotifications] = useState([]);

  useEffect(() => {
    getMyNotification(accessToken, setNotifications);
  }, [accessToken]);

  return (
    <>
      <TopBar backButtonLink='/home' />
      <Container>
        {(notifications.length === 0) ?
          <Typography className={classes.nothingToNotice}>확인하실 알림이 없습니다!</Typography> : ""}
        {notifications.map((notification) => (
          <NotificationItem key={notification.id} notification={notification} history={history} />
        ))}
        <BottomBlank />
      </Container>
      <Bottom selected={HOME} />
    </>
  );
};

export default NotificationPage;
