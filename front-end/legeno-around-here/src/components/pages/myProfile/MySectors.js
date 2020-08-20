import React from 'react';
import List from '@material-ui/core/List';
import { makeStyles } from '@material-ui/core/styles';
import MySectorItem from './MySectorItem';

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    backgroundColor: theme.palette.background.paper,
  },
}));

const MySectors = ({ mySectors }) => {
  const classes = useStyles();

  return (
    <List className={classes.root}>
      {mySectors.map((mySector) => (
        <MySectorItem key={mySector.id} mySector={mySector} />
      ))}
    </List>
  );
};

export default MySectors;
