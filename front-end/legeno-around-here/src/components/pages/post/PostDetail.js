import React, { useState } from 'react';
import { createComment, findCommentsByPostId, pressPostZzang } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import Typography from '@material-ui/core/Typography';
import { Grid, IconButton, TextField } from '@material-ui/core';
import FavoriteIcon from '@material-ui/icons/Favorite';
import FavoriteBorderIcon from '@material-ui/icons/FavoriteBorder';
import CommentIcon from '@material-ui/icons/Comment';
import AddIcon from '@material-ui/icons/Add';
import Comments from './Comments';
import PostImages from './PostImages';
import { convertDateFormat } from '../../../util/TimeUtils';
import UpdatePostButton from './UpdatePostButton';
import PostReportSection from './PostReportSection';
import LinkWithoutStyle from '../../../util/LinkWithoutStyle';
import DeletePostButton from './DeletePostButton';
import { makeStyles } from '@material-ui/core/styles';
import { MAIN_COLOR } from '../../../constants/Color';
import { DEFAULT_IMAGE_URL } from '../myProfileEdit/MyProfileEditPage';

const useStyle = makeStyles({
  postTopSection: {
    height: '41px',
    display: 'flex',
    flexDirection: 'row',
    alignContent: 'center',
    marginTop: '4px',
  },
  postAreaNameSection: {
    display: 'flex',
    flexDirection: 'row',
    alignContent: 'center',
  },
  postAuthorNicknameSection: {
    display: 'inline',
    margin: 'auto 5px auto auto',
  },
  authorProfilePhotoUrl: (props) => ({
    width: '35px',
    height: '35px',
    backgroundRepeat: 'no-repeat',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    borderRadius: '300px',
    backgroundImage: `url(${props.authorProfilePhotoUrl})`,
    border: `1px solid ${MAIN_COLOR}`,
  }),
  addButton: {
    padding: '0px',
  },
});

const PostDetail = ({ post, myInfo, history }) => {
  const accessToken = getAccessTokenFromCookie();
  const [writing, setWriting] = useState('');
  const [comments, setComments] = useState(post.comments);
  const [loading, setLoading] = useState(false);
  const [zzang, setZzang] = useState(post.zzang.activated);
  const [zzangCount, setZzangCount] = useState(post.zzang.count);
  const isMyPost = post && myInfo && post.creator.id === myInfo.id;
  const authorProfilePhotoUrl = post.creator.image ? post.creator.image.url : DEFAULT_IMAGE_URL;
  const props = { authorProfilePhotoUrl: authorProfilePhotoUrl };
  const classes = useStyle(props);

  const onWritingChanged = (e) => {
    setWriting(e.target.value);
  };

  const pressZzang = async () => {
    const isPressed = await pressPostZzang(post.id, accessToken, history);
    if (isPressed) {
      if (zzang) {
        setZzangCount(zzangCount - 1);
        setZzang(!zzang);
        return;
      }
      setZzangCount(zzangCount + 1);
      setZzang(!zzang);
    }
  };

  const submitForm = () => {
    const sendComment = async () => {
      setLoading(true);
      const isCommentCreated = await createComment(post.id, writing, accessToken, history);
      if (isCommentCreated) {
        await loadComments();
        setWriting('');
      }
      setLoading(false);
    };
    sendComment();
  };

  const loadComments = async () => {
    const foundComments = await findCommentsByPostId(accessToken, post.id, history);
    setComments(foundComments);
  };

  const makeCreatorName = () => {
    if (post.creator.nickname === '탈퇴한 회원') {
      return <Typography className={classes.postAuthorNicknameSection}>{post.creator.nickname}</Typography>;
    }
    return (
      <Typography
        component={LinkWithoutStyle}
        to={isMyPost ? '/users/me' : '/users/' + post.creator.id}
        className={classes.postAuthorNicknameSection}
      >
        {post.creator.nickname}
      </Typography>
    );
  };

  const makeCreatorPhoto = () => {
    if (post.creator.nickname === '탈퇴한 회원') {
      return <div className={classes.authorProfilePhotoUrl} />;
    }
    return (
      <LinkWithoutStyle
        className={classes.authorProfilePhotoUrl}
        to={isMyPost ? '/users/me' : '/users/' + post.creator.id}
      />
    );
  };

  return (
    <>
      <Grid container className={classes.postTopSection}>
        <Grid container item xs={6} className={classes.postAreaNameSection}>
          <Typography>{post.area.fullName}</Typography>
        </Grid>
        <Grid container item xs={6}>
          {makeCreatorName()}
          {makeCreatorPhoto()}
        </Grid>
      </Grid>
      <Typography variant='h5'>{post.sector.name} 부문</Typography>
      {post.images.length > 0 && <PostImages images={post.images} />}
      <Typography variant='h6'>
        {post.writing.split('\n').map((line, index) => {
          return (
            <span key={index}>
              {line}
              <br />
            </span>
          );
        })}
      </Typography>
      <Grid container>
        <Grid container item xs={6}>
          <IconButton onClick={pressZzang}>
            {zzang === true ? <FavoriteIcon /> : <FavoriteBorderIcon />}
            {zzangCount}
          </IconButton>
          <IconButton>
            <CommentIcon />
            {post.comments.length}
          </IconButton>
        </Grid>
        <Grid container item xs={6} alignItems='flex-start' justify='flex-end' direction='row'>
          <Typography display='inline'>{convertDateFormat(post.createdAt)}</Typography>
          <Typography display='inline'>
            {isMyPost && <UpdatePostButton post={post} />}
            {isMyPost && <DeletePostButton postId={post.id} accessToken={accessToken} history={history} />}
          </Typography>
          <PostReportSection post={post} myInfo={myInfo} history={history} />
        </Grid>
      </Grid>

      <form
        onSubmit={(e) => {
          e.preventDefault();
          submitForm();
        }}
      >
        <Grid container>
          <Grid container item xs={11}>
            <TextField
              type='text'
              id='standard-multiline-static'
              fullWidth
              multiline
              placeholder='댓글을 입력해주세요!'
              onChange={onWritingChanged}
              value={writing}
              inputProps={{ maxLength: 200 }}
              helperText={`${writing.length}/200`}
            />
          </Grid>
          <Grid container item xs={1}>
            <IconButton type='submit' className={classes.addButton}>
              <AddIcon />
            </IconButton>
          </Grid>
        </Grid>
      </form>
      {comments.length > 0 && myInfo && <Comments comments={comments} loading={loading} myId={myInfo.id} />}
    </>
  );
};

export default PostDetail;
