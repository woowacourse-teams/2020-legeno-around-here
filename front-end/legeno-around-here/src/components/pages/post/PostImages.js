import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import { List } from '@material-ui/core';
import PostImageItem from './PostImageItem';

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    height: 300,
    display: 'flex',
    flexDirection: 'row',
    padding: 0,
  },
}));

const PostImages = ({ images }) => {
  const classes = useStyles();

  return (
    // <div>
    //   asdfasd
    <List className={classes.root}>
      {images.map((image) => (
        <PostImageItem key={image.id} image={image} />
      ))}
    </List>
    // </div>
  );
};

export default PostImages;
