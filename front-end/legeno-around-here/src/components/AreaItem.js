import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles } from '@material-ui/core/styles';

function refreshHome(history) {
  history.push('/home-reload');
}

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    maxWidth: 360,
    backgroundColor: theme.palette.background.paper,
  },
}));

const setMainArea = (area, history) => {
  localStorage.setItem('mainAreaId', area.id);
  localStorage.setItem('mainAreaName', area.lastDepthName);
  refreshHome(history);
};

const AreaItem = ({ area, history }) => {
  const classes = useStyles();

  return (
    <>
      <div className={classes.root}>
        <ListItem button alignItems='center' onClick={() => setMainArea(area, history)}>
          <ListItemText primary={area.fullName} />
        </ListItem>
      </div>
    </>
  );
};

export default AreaItem;
