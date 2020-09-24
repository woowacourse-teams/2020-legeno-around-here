import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import FavoriteIcon from '@material-ui/icons/Favorite';
import CommentIcon from '@material-ui/icons/Comment';
import { convertDateFormat } from '../util/TimeUtils';
import Grid from '@material-ui/core/Grid';
import LinesEllipsis from 'react-lines-ellipsis';

const useStyles = makeStyles(() => ({
  root: {
    borderBottom: '1.5px solid darkgray',
    padding: '10px',
    width: '100%',
  },
  cover: {
    position: 'relative',
    opacity: 0.7,
    backgroundSize: 'contain',
  },
  photoText: {
    position: 'absolute',
    top: '50%',
    left: '50%',
    transform: 'translate(-50%, -50%)',
    color: 'black',
    fontSize: '2rem',
  },
  img: {
    margin: 'auto',
    display: 'block',
    width: 'auto',
    height: 'auto',
    maxHeight: '350px',
    maxWidth: '100%',
  },
}));

const PostItem = ({ post, history }) => {
  const classes = useStyles();
  const [isLoaded, setIsLoaded] = useState(false);

  const { area, commentsCount, createdAt, creator, images, id, zzang, sector, writing } = post;

  return (
    <Grid
      container
      direction='row'
      justify='flex-start'
      alignItems='center'
      spacing={2}
      onClick={() => history.push(`/posts/${id}`)}
      className={classes.root}
    >
      <Grid item xs>
        <Grid item xs>
          <Typography variant='h6'>{sector.name} 부문</Typography>
          <Typography variant='subtitle1'>
            <LinesEllipsis text={writing} maxLine='3' ellipsis='...' trimRight basedOn='letters' />
          </Typography>
        </Grid>
        <Grid container direction='column' justify='space-between'>
          <Grid item xs>
            <Typography variant='body2' color='textSecondary'>
              {convertDateFormat(createdAt)}
            </Typography>
          </Grid>
          <Grid item xs>
            <Typography variant='body2' color='textSecondary' component='p'>
              <span>'{area.fullName}'에서</span>
            </Typography>
          </Grid>
          <Grid item xs>
            <Typography variant='body2' color='textSecondary'>
              <span>'{creator.nickname}'님이 작성하셨습니다.</span>
            </Typography>
          </Grid>
          <Grid item xs>
            <IconButton size='small'>
              {zzang.activated === true ? <FavoriteIcon /> : <FavoriteBorderIcon />}
              {zzang.count}
            </IconButton>
            <IconButton size='small'>
              <CommentIcon />
              {commentsCount}
            </IconButton>
          </Grid>
        </Grid>
      </Grid>
      {images.length > 0 && (
        <Grid item xs className={classes.cover}>
          <img
            className={classes.img}
            alt='이미지를 불러오지 못했습니다!'
            onLoad={() => setIsLoaded(true)}
            src={images[0].url}
          />
          {images.length > 1 && isLoaded && <Typography className={classes.photoText}>+{images.length - 1}</Typography>}
        </Grid>
      )}
    </Grid>
  );
};

export default PostItem;
