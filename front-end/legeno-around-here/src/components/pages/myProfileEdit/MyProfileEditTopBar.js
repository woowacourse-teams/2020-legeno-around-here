import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import handleBackButtonClicked from '../../../util/BackButtonHandler';

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  sectionDesktop: {
    display: 'flex',
  },
}));

const MyProfileEditTopBar = () => {
  const classes = useStyles();

  return (
    <AppBar position='sticky'>
      <Toolbar>
        <IconButton edge='start' color='inherit' aria-label='open drawer' onClick={handleBackButtonClicked}>
          <ArrowBackIcon />
        </IconButton>
        <div className={classes.grow} />
      </Toolbar>
    </AppBar>
  );
};

export default MyProfileEditTopBar;
