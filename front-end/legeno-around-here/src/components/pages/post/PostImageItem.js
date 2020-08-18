import { makeStyles } from '@material-ui/core/styles';
import CardMedia from '@material-ui/core/CardMedia';
import React from 'react';

const useStyles = makeStyles((theme) => ({
  image: {
    width: '100%',
    height: '100%',
    backgroundSize: 'contain',
  },
}));

const PostImageItem = ({ image }) => {
  const classes = useStyles();

  return (
    <CardMedia
      image={image.url}
      title={`image id : ${image.id}`}
      className={classes.image}
    ></CardMedia>
  );
};

export default PostImageItem;
