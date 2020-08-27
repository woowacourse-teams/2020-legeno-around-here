import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import ArrowBackIosIcon from '@material-ui/icons/ArrowBackIos';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';

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
    <AppBar position='sticky'>
      <Toolbar>
        <IconButton color='inherit' edge='start' onClick={() => (document.location.href = '/home')}>
          <ArrowBackIosIcon />
        </IconButton>
        <div className={classes.grow} />
      </Toolbar>
    </AppBar>
  );
};

export default PostDetailTopBar;
