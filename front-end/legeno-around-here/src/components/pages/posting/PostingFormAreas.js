import React from 'react';
import List from '@material-ui/core/List';
import { makeStyles } from '@material-ui/core/styles';
import PostingFormAreaItem from './PostingFormAreaItem';

const useStyles = makeStyles(() => ({
  list: {
    overflow: 'auto',
  },
}));

const PostingFormAreas = ({ areas, setArea, handleClose }) => {
  const classes = useStyles();

  return (
    <List component='nav' className={classes.list}>
      {areas.map((area) => {
        return <PostingFormAreaItem key={area.id} area={area} setArea={setArea} handleClose={handleClose} />;
      })}
    </List>
  );
};

export default PostingFormAreas;
