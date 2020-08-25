import { makeStyles } from '@material-ui/core/styles';
import { List } from '@material-ui/core';
import React from 'react';
import PostingFormImageItem from './PostingFormImageItem';

const useStyles = makeStyles(() => ({
  list: {
    width: '100%',
    height: 100,
    display: 'flex',
    flexDirection: 'row',
    padding: 0,
  },
}));

const PostingFormImages = ({ handleImageDelete, images }) => {
  const classes = useStyles();

  return (
    <List className={classes.list}>
      {images.map((image) => (
        <PostingFormImageItem key={image.id} handleImageDelete={handleImageDelete} image={image} />
      ))}
    </List>
  );
};

export default PostingFormImages;
