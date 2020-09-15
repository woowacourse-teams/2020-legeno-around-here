import { makeStyles } from '@material-ui/core/styles';
import React from 'react';
import Paper from '@material-ui/core/Paper';

const useStyles = makeStyles(() => ({
  background: {
    width: '100%',
    height: '100%',
    backgroundSize: 'contain',
  },
}));

const PostImageItem = ({ image }) => {
  const classes = useStyles();

  return (
    <Paper variant='outlined'>
      <img className={classes.background} src={image.url} alt='이미지를 불러오지 못했습니다!' />
    </Paper>
  );
};

export default PostImageItem;
