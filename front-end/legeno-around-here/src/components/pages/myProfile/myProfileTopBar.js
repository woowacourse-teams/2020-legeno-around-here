import React from 'react';
import {makeStyles} from '@material-ui/core/styles';
import IconButton from '@material-ui/core/IconButton';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';

import handleBackButtonClicked from '../../../util/BackButtonHandler'

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

export default function TopBar() {
  const classes = useStyles();

  return (
    <>
      <AppBar position="sticky">
        <Toolbar>
          <div className={classes.sectionDesktop}>
            <IconButton
              edge="start"
              className={classes.menuButton}
              color="inherit"
              aria-label="open drawer"
              onClick={handleBackButtonClicked}
              form="posting-form"
            >
              <ArrowBackIcon />
            </IconButton>
          </div>
          <div className={classes.grow} />
        </Toolbar>
      </AppBar>
    </>
  );
}
