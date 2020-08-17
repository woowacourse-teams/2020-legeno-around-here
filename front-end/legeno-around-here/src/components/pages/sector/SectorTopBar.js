import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import SectorApplyButton from './SectorApplyButton';

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
      <AppBar position="sticky">
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
