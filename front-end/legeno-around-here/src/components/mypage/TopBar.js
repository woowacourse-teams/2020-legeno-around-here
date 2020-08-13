import React from 'react';
import styled from 'styled-components';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';

import { MAIN_COLOR } from '../../constants/Color';
import BackButton from './BackButton';

const useStyles = makeStyles(() => ({
  style: {
    background: MAIN_COLOR,
  },
}));

const TopBar = ({ backButtonLink }) => {
  const classes = useStyles();

  return (
    <AppBar position="sticky" className={classes.style}>
      <Toolbar>
        <BackButton linkTo={backButtonLink} />
      </Toolbar>
    </AppBar>
  );
};

export default TopBar;
