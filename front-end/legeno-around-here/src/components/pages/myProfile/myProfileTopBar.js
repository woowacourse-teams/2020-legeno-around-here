import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
}));

export default function TopBar() {
  const classes = useStyles();

  return (
    <>
      <AppBar position='sticky'>
        <Toolbar>
          <div className={classes.grow} />
        </Toolbar>
      </AppBar>
    </>
  );
}
