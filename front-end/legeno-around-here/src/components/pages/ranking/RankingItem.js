import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardMedia from '@material-ui/core/CardMedia';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import FavoriteIcon from '@material-ui/icons/Favorite';
import CommentIcon from '@material-ui/icons/Comment';
import { convertDateFormat } from '../../../util/TimeUtils';

const useStyles = makeStyles(() => ({
  grow: {
    flexGrow: 1,
  },
  root: {
    display: 'flex',
  },
  details: {
    display: 'flex',
    flexDirection: 'column',
  },
  content: {
    flex: '1 0 auto',
  },
  cover: {
    flex: '1 0 auto',
    opacity: 0.7,
    backgroundSize: 'contain',
  },
  photoText: {
    textAlign: 'center',
    marginTop: 100,
  },
}));

const RankingItem = ({ post }) => {
  const classes = useStyles();

  const {
    area,
    commentsCount,
    createdAt,
    creator,
    images,
    id,
    zzang,
    sector,
    writing,
  } = post;

  return (
    <Card
      className={classes.root}
      data-id={id}
      onClick={() => (document.location.href = `/posts/${id}`)}
    >
      <div className={classes.details}>
        <CardContent className={classes.content}>
          <Typography component="h6" variant="h5">
            {sector.name} 부문
          </Typography>
          <Typography variant="subtitle1" color="textSecondary">
            {convertDateFormat(createdAt)}
          </Typography>

          <Typography
            variant="body2"
            color="textSecondary"
            component="p"
            style={{
              overflow: 'hidden',
              textOverflow: 'ellipsis',
              width: '190px',
            }}
          >
            {writing}
          </Typography>
          <Typography variant="subtitle1" color="textSecondary">
            작성자 : {creator.nickname}
          </Typography>
          <Typography variant="body2" color="textSecondary" component="p">
            작성 지역 : {area.fullName}
          </Typography>
        </CardContent>
        <CardActions disableSpacing>
          <IconButton>
            {zzang.activated === true ? (
              <FavoriteIcon />
            ) : (
              <FavoriteBorderIcon />
            )}
            {zzang.count}
          </IconButton>
          <IconButton>
            <CommentIcon />
            {commentsCount}
          </IconButton>
        </CardActions>
      </div>
      {images.length > 0 && (
        <CardMedia
          className={classes.cover}
          image={images[0].url}
          title="Live from space album cover"
        >
          {images.length > 1 && (
            <div className={classes.photoText}>+{images.length - 1}</div>
          )}
        </CardMedia>
      )}
    </Card>
  );
};

export default RankingItem;
