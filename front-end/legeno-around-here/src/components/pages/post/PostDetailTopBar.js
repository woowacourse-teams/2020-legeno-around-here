import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import NotificationsIcon from '@material-ui/icons/Notifications';

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  sectionDesktop: {
    display: 'flex',
  },
}));

const PostDetailTopBar = () => {
  const classes = useStyles();

  return (
    <AppBar position="sticky">
      <Toolbar>
        <IconButton
          color="inherit"
          edge="start"
          onClick={() => (document.location.href = '/')}
        >
          <ArrowBackIosIcon />
        </IconButton>
        <div className={classes.grow} />
        <div className={classes.sectionDesktop}>
          <IconButton
            aria-label="show 17 new notifications"
            color="inherit"
            onClick={() => {
              alert('아직 알람기능이 완성되지 않았습니다!');
            }}
          >
            <Badge badgeContent={0} color="secondary">
              <NotificationsIcon />
            </Badge>
          </IconButton>
        </div>
      </Toolbar>
    </AppBar>
  );
};

export default PostDetailTopBar;
