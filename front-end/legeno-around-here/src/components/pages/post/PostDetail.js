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

const PostDetail = ({ post, myInfo }) => {
  const accessToken = getAccessTokenFromCookie();
  const [writing, setWriting] = useState('');
  const [comments, setComments] = useState(post.comments);
  const [loading, setLoading] = useState(false);
  const [zzang, setZzang] = useState(post.zzang.activated);
  const [zzangCount, setZzangCount] = useState(post.zzang.count);
  const isMyPost = post && myInfo && post.creator.id === myInfo.id;

  let keyValue = 0;

  const onWritingChanged = (e) => {
    setWriting(e.target.value);
  };

  const pressZzang = async () => {
    const isPressed = await pressPostZzang(post.id, accessToken);
    if (isPressed) {
      if (zzang) {
        setZzangCount(zzangCount - 1);
        setZzang(!zzang);
        return;
      }
      setZzangCount(zzangCount + 1);
      setZzang(!zzang);
      return;
    }
  };

  const submitForm = () => {
    const sendComment = async () => {
      setLoading(true);
      const isCommentCreated = await createComment(post.id, writing, accessToken);
      if (isCommentCreated) {
        const loadedComments = await loadComments();
        setComments(loadedComments);
      }
      setLoading(false);
    };
    sendComment();
  };

  const loadComments = async () => {
    const foundComments = await findCommentsByPostId(accessToken, post.id);
    setComments(foundComments);
  };

  return (
    <>
      <Grid container>
        <Grid container item xs={6}>
          <Typography>{post.area.fullName}</Typography>
        </Grid>
        <Grid container item xs={6} alignItems='flex-start' justify='flex-end' direction='row'>
          <Typography component={LinkWithoutStyle} to={isMyPost ? '/users/me' : '/users/' + post.creator.id}>
            {post.creator.nickname}
          </Typography>
        </Grid>
      </Grid>
      <Typography variant='h5'>{post.sector.name} 부문</Typography>
      {post.images.length > 0 && <PostImages images={post.images} />}
      <Typography variant='h6'>
        {post.writing.split('\n').map((line) => {
          keyValue += 1;
          return (
            <span key={keyValue}>
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
          <Typography display='inline'>{isMyPost && <UpdatePostButton post={post} />}</Typography>
          <PostReportSection post={post} myInfo={myInfo} />
        </Grid>
      </Grid>

      <form onSubmit={submitForm}>
        <Grid container>
          <Grid container item xs={11}>
            <TextField
              type='text'
              id='standard-multiline-static'
              fullWidth
              multiline
              rows={2}
              placeholder='댓글을 입력해주세요!'
              onChange={onWritingChanged}
              value={writing}
              inputProps={{ maxLength: 200 }}
            />
          </Grid>
          <Grid container item xs={1}>
            <IconButton type='submit'>
              <AddIcon />
            </IconButton>
          </Grid>
        </Grid>
      </form>
      {comments.length > 0 && <Comments comments={comments} loading={loading} />}
    </>
  );
};

export default PostDetail;
