import React from 'react';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import FavoriteIcon from '@material-ui/icons/Favorite';
import CommentIcon from '@material-ui/icons/Comment';
import { convertDateFormat } from '../../../util/TimeUtils';
import useStyle from './RankingItemStyle';
import LinesEllipsis from 'react-lines-ellipsis';

const FIRST_PRIZE_IMAGE_URL = '/images/gold.png';
const SECOND_PRIZE_IMAGE_URL = '/images/silver.png';
const THIRD_PRIZE_IMAGE_URL = '/images/bronze.png';

const RankingItem = ({ post, rank, whetherToPrintZzangCount, history }) => {
  const matchRankToPrizeUrl = (rank) => {
    if (rank === 1) {
      return FIRST_PRIZE_IMAGE_URL;
    }
    if (rank === 2) {
      return SECOND_PRIZE_IMAGE_URL;
    }
    if (rank === 3) {
      return THIRD_PRIZE_IMAGE_URL;
    }
    return '';
  };

  const props = {
    whetherToPrintZzangCount: whetherToPrintZzangCount,
    prizeImageUrl: matchRankToPrizeUrl(rank),
  };
  const classes = useStyle(props);

  const { area, commentsCount, createdAt, creator, id, zzang, sector, writing } = post;

  return (
    <Card className={classes.card} data-id={id} onClick={() => history.push(`/posts/${id}`)}>
      <div className={classes.rank}>
        {rank > 3 ? (
          <Typography component='h5' variant='h5'>
            {rank}
          </Typography>
        ) : (
          ''
        )}
      </div>
      <div className={classes.details}>
        <CardContent className={classes.content}>
          <Typography component='h6' variant='h5'>
            {sector.name} 부문
          </Typography>
          <Typography variant='subtitle1' color='textSecondary'>
            {convertDateFormat(createdAt)}
          </Typography>
          <Typography variant='subtitle1' color='textSecondary'>
            작성자 : {creator.nickname}
          </Typography>
          <Typography variant='body2' color='textSecondary'>
            작성 지역 : {area.fullName}
          </Typography>
          <Typography variant='subtitle2' color='textSecondary' className={classes.writing}>
            <LinesEllipsis text={writing} maxLine='3' ellipsis='...' trimRight basedOn='letters' />
          </Typography>
        </CardContent>
        <CardActions className={classes.reactions} disableSpacing>
          <IconButton className={classes.reactionIconSpace}>
            {zzang.activated === true ? <FavoriteIcon className={classes.reactionIcon} /> : <FavoriteBorderIcon />}
            {zzang.count}
          </IconButton>
          <IconButton>
            <CommentIcon className={classes.reactionIcon} />
            {commentsCount}
          </IconButton>
        </CardActions>
      </div>
    </Card>
  );
};

export default RankingItem;
