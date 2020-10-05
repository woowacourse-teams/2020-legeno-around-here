import { makeStyles } from '@material-ui/core/styles';
import React from 'react';

const useStyles = makeStyles(() => ({
  background: {
    width: 'auto',
    height: 'auto',
    maxHeight: '400px',
    maxWidth: '-webkit-fill-available',
    backgroundSize: 'contain',
  },
}));

const PostImageItem = ({ image }) => {
  const classes = useStyles();

  return (
    <div align='center'>
      <img className={classes.background} src={image.url} alt='이미지를 불러오지 못했습니다!' />
    </div>
  );
};

export default PostImageItem;
