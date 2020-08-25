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
import { MAIN_COLOR } from '../../../constants/Color';

const useStyle = makeStyles({
  grow: {
    flexGrow: 1,
  },
  card: {
    display: 'flex',
  },
  rank: {
    width: '65px',
    height: '65px',
    borderRadius: '300px',
    border: `1px solid ${MAIN_COLOR}`,
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    alignContent: 'center',
    textAlign: 'center',
    margin: 'auto 8px',
  },
  details: {
    display: 'flex',
    flexDirection: 'column',
    width: '80%',
  },
  content: {
    flex: '1 0 auto',
    // border: '1px solid red',
    padding: '5px',
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
  reactions: (props) => ({
    display: `${props.whetherToPrintZzangCount? 'block' : 'none'}`,
    paddingTop: 0,
  }),
  reactionIconSpace: {
    margin: 0,
    padding: 0,
  },
  reactionIcon: {
    width: '20px',
    height: '20px',
  },
});

const RankingItem = ({ post, rank, whetherToPrintZzangCount}) => {
  const props = {
    whetherToPrintZzangCount: whetherToPrintZzangCount,
  };
  const classes = useStyle(props);

  const {
    area,
    commentsCount,
    createdAt,
    creator,
    images,
    id,
    zzang,
    sector,
  } = post;

  return (
    <Card
      className={classes.card}
      data-id={id}
      onClick={() => (document.location.href = `/posts/${id}`)}
    >
      <div className={classes.rank}>
        <Typography component="h5" variant="h5">{rank}</Typography>
      </div>
      <div className={classes.details}>
        <CardContent className={classes.content}>
          <Typography component="h6" variant="h5">
            {sector.name} 부문
          </Typography>
          <Typography variant="subtitle1" color="textSecondary">
            {convertDateFormat(createdAt)}
          </Typography>
          <Typography variant="subtitle1" color="textSecondary">
            작성자 : {creator.nickname}
          </Typography>
          <Typography variant="body2" color="textSecondary" component="p">
            작성 지역 : {area.fullName}
          </Typography>
        </CardContent>
        <CardActions className={classes.reactions} disableSpacing>
          <IconButton className={classes.reactionIconSpace}>
            {zzang.activated === true ? (
              <FavoriteIcon className={classes.reactionIcon} />
            ) : (
              <FavoriteBorderIcon />
            )}
            {zzang.count}
          </IconButton>
          <IconButton>
            <CommentIcon className={classes.reactionIcon}/>
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
