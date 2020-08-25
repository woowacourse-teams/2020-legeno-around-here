import { makeStyles } from '@material-ui/core/styles';
import CardMedia from '@material-ui/core/CardMedia';
import HighlightOffIcon from '@material-ui/icons/HighlightOff';
import React from 'react';
import IconButton from '@material-ui/core/IconButton';

const useStyles = makeStyles(() => ({
  button: {
    position: 'absolute',
  },
  image: {
    position: 'relative',
    width: '100%',
    height: '100%',
    backgroundSize: 'contain',
  },
}));

const PostingFormImageItem = ({ handleImageDelete, image }) => {
  const classes = useStyles();

  return (
    <>
      <CardMedia image={image.url} title={`image id : ${image.id}`} className={classes.image}>
        <IconButton
          onClick={() => {
            handleImageDelete(image.id);
          }}
          className={classes.button}
        >
          <HighlightOffIcon />
        </IconButton>
      </CardMedia>
    </>
  );
};

export default PostingFormImageItem;
