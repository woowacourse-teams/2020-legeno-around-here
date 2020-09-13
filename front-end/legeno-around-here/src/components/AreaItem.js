import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles } from '@material-ui/core/styles';

function refreshHome(history, selected) {
  history.replace('/' + selected + '-reload');
}

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    maxWidth: 360,
    backgroundColor: theme.palette.background.paper,
  },
}));

const setMainArea = (area, history, selected) => {
  localStorage.setItem('mainAreaId', area.id);
  localStorage.setItem('mainAreaName', area.lastDepthName);
  refreshHome(history, selected);
};

const AreaItem = ({ area, history, selected }) => {
  const classes = useStyles();

  return (
    <>
      <div className={classes.root}>
        <ListItem button alignItems='center' onClick={() => setMainArea(area, history, selected)}>
          <ListItemText primary={area.fullName} />
        </ListItem>
      </div>
    </>
  );
};

export default AreaItem;
