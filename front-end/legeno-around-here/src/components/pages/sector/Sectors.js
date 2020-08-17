import React from 'react';
import List from '@material-ui/core/List';
import { makeStyles } from '@material-ui/core/styles';
import SectorItem from './SectorItem';

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    backgroundColor: theme.palette.background.paper,
  },
}));

const Sectors = ({ sectors }) => {
  const classes = useStyles();

  return (
    <List className={classes.root}>
      {sectors.map((sector) => (
        <SectorItem key={sector.id} sector={sector} />
      ))}
    </List>
  );
};

export default Sectors;
