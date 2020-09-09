import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import SectorApplyButton from './SectorApplyButton';

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  sectionDesktop: {
    display: 'flex',
  },
}));

export default function PrimarySearchAppBar() {
  const classes = useStyles();

  return (
    <>
      <AppBar position='sticky'>
        <Toolbar>
          <div className={classes.grow} />
          <div className={classes.sectionDesktop}>
            <SectorApplyButton />
          </div>
        </Toolbar>
      </AppBar>
    </>
  );
}
