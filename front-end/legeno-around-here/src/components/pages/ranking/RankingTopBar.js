import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import Badge from '@material-ui/core/Badge';
import NotificationsIcon from '@material-ui/icons/Notifications';
import AreaSearch from '../../AreaSearch';

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(0),
  },
  title: {
    display: 'block',
  },
  sectionDesktop: {
    display: 'flex',
  },
  modal: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  paper: {
    backgroundColor: theme.palette.background.paper,
    border: '2px solid #000',
    boxShadow: theme.shadows[5],
    padding: theme.spacing(2, 4, 3),
  },
  list: {
    height: 400,
    overflow: 'auto',
  },
}));

export default function PrimarySearchAppBar() {
  const classes = useStyles();

  return (
    <>
      <AppBar position='sticky'>
        <Toolbar>
          <AreaSearch />
          <Typography>캡짱은 누구?</Typography>
          <div className={classes.grow} />
          <div className={classes.sectionDesktop}>
            <IconButton
              aria-label='show 17 new notifications'
              color='inherit'
              onClick={() => {
                alert('아직 알람기능이 완성되지 않았습니다!');
              }}
            >
              <Badge badgeContent={0} color='secondary'>
                <NotificationsIcon />
              </Badge>
            </IconButton>
          </div>
        </Toolbar>
      </AppBar>
    </>
  );
}
