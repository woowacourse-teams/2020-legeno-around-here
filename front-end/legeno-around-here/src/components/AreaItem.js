import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles } from '@material-ui/core/styles';

function refreshPage() {
  window.location.reload(false);
}

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    maxWidth: 360,
    backgroundColor: theme.palette.background.paper,
  },
}));

const setMainArea = (area) => {
  console.log('mainAreaId : ' + area.id);
  console.log('mainAreaName : ' + area.lastDepthName);
  localStorage.setItem('mainAreaId', area.id);
  localStorage.setItem('mainAreaName', area.lastDepthName);
  refreshPage();
};

const AreaItem = ({ area }) => {
  const classes = useStyles();

  return (
    <>
      <div className={classes.root}>
        <ListItem button alignItems='center' onClick={() => setMainArea(area)}>
          <ListItemText primary={area.fullName} />
        </ListItem>
      </div>
    </>
  );
};

export default AreaItem;
