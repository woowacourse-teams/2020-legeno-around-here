import React from 'react';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    maxWidth: 360,
    backgroundColor: theme.palette.background.paper,
  },
}));

const setMainArea = (area, setArea, handleClose) => {
  setArea({
    id: area.id,
    name: area.lastDepthName,
  });
  handleClose();
};

const PostingFormAreaItem = ({ area, setArea, handleClose }) => {
  const classes = useStyles();

  return (
    <>
      <div className={classes.root}>
        <ListItem button alignItems='center' onClick={() => setMainArea(area, setArea, handleClose)}>
          <ListItemText primary={area.fullName} />
        </ListItem>
      </div>
    </>
  );
};

export default PostingFormAreaItem;
