import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles } from '@material-ui/core/styles';
import { setMainAreaId, setMainAreaName } from '../util/localStorageUtils';

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    maxWidth: 360,
    backgroundColor: theme.palette.background.paper,
  },
}));

const AreaItem = ({ area, setter, getter, closeModal }) => {
  const classes = useStyles();

  const setMainArea = () => {
    if (getter.areaId === area.id) {
      return;
    }
    setMainAreaId(area.id);
    setMainAreaName(area.lastDepthName);
    setter.removeContent();
    setter.setAreaId(area.id);
    closeModal();
  };

  return (
    <>
      <div className={classes.root}>
        <ListItem button alignItems='center' onClick={() => setMainArea()}>
          <ListItemText primary={area.fullName} />
        </ListItem>
      </div>
    </>
  );
};

export default AreaItem;
