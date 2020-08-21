import React from 'react';
import { makeStyles } from '@material-ui/core';

const useStyles = makeStyles(() => ({
  blankMarginBottom: {
    marginBottom: 60,
  },
}));

const BottomBlank = () => {
  const classes = useStyles();

  return <div className={classes.blankMarginBottom}></div>;
};

export default BottomBlank;
