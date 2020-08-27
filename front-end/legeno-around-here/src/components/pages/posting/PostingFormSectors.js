import React from 'react';
import List from '@material-ui/core/List';
import { makeStyles } from '@material-ui/core/styles';
import PostingFormSectorItem from './PostingFormSectorItem';

const useStyles = makeStyles(() => ({
  list: {
    overflow: 'auto',
  },
}));

const PostingFormSectors = ({ selectSector, sectors }) => {
  const classes = useStyles();

  return (
    <List component='nav' className={classes.list}>
      {sectors.map((sector) => {
        return <PostingFormSectorItem key={sector.id} selectSector={selectSector} sector={sector} />;
      })}
    </List>
  );
};

export default PostingFormSectors;
