import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import Badge from '@material-ui/core/Badge';
import NotificationsIcon from '@material-ui/icons/Notifications';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { getUnreadNotificationCount } from '../../api/API';
import { Link } from 'react-router-dom';
import AreaSearch from '../../AreaSearch';

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  sectionDesktop: {
    display: 'flex',
  },
}));

const RankingTopBar = ({ history }) => {
  const classes = useStyles();
  const accessToken = getAccessTokenFromCookie();
  const [unreadNotification, setUnreadNotification] = useState(0);

  useEffect(() => {
    getUnreadNotificationCount(accessToken, setUnreadNotification, history);
  }, [accessToken, history]);

  return (
    <AppBar position='sticky'>
      <Toolbar>
        <AreaSearch history={history} selected='ranking' />
        <Typography>캡짱은 누구?</Typography>
        <div className={classes.grow} />
        <div className={classes.sectionDesktop}>
          <Link to='/notification'>
            <IconButton aria-label='show 17 new notifications' color='inherit'>
              <Badge badgeContent={unreadNotification} color='secondary'>
                <NotificationsIcon style={{ color: 'white' }} />
              </Badge>
            </IconButton>
          </Link>
        </div>
      </Toolbar>
    </AppBar>
  );
};

export default RankingTopBar;
