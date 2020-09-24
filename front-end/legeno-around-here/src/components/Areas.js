import React from 'react';
import List from '@material-ui/core/List';
import { makeStyles } from '@material-ui/core/styles';
import AreaItem from './AreaItem';

const useStyles = makeStyles(() => ({
  list: {
    overflow: 'auto',
  },
}));

const Areas = ({ areas, setter, getter, closeModal }) => {
  const classes = useStyles();

  return (
    <List component='nav' className={classes.list}>
      {areas.map((area) => {
        return <AreaItem key={area.id} area={area} setter={setter} getter={getter} closeModal={closeModal} />;
      })}
    </List>
  );
};

export default Areas;
