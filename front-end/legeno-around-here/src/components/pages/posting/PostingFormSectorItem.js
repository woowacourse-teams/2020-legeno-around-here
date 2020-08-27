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

const PostingFormSectorItem = ({ selectSector, sector }) => {
  const classes = useStyles();

  return (
    <>
      <div className={classes.root}>
        <ListItem button alignItems='center' onClick={() => selectSector(sector)}>
          <ListItemText primary={sector.name + ' 부문'} secondary={sector.description} />
        </ListItem>
      </div>
    </>
  );
};

export default PostingFormSectorItem;
