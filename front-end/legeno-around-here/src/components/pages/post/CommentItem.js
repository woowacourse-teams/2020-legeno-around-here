import React from 'react';
import { ListItem, Typography, ListItemText, Divider } from '@material-ui/core';
import { convertDateFormat } from '../../../util/TimeUtils';

const CommentItem = ({ comment }) => {
  return (
    <>
      <ListItem alignItems='flex-start'>
        <ListItemText primary={comment.writing} secondary={convertDateFormat(comment.createdAt)} />
        <Typography variant='subtitle1'>작성자 : {comment.creator.nickname}</Typography>
      </ListItem>
      <Divider />
    </>
  );
};

export default CommentItem;
