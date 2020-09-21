import React from 'react';
import Loading from '../../Loading';
import { makeStyles } from '@material-ui/core/styles';
import { List } from '@material-ui/core';
import CommentItem from './CommentItem';

const useStyles = makeStyles((theme) => ({
  root: {
    width: '100%',
    backgroundColor: theme.palette.background.paper,
  },
}));

const Comments = ({ comments, loading, myId }) => {
  const classes = useStyles();

  if (loading) return <Loading />;
  return (
    <List className={classes.root}>
      {comments.map((comment) => (
        <CommentItem key={comment.id} comment={comment} myId={myId} />
      ))}
    </List>
  );
};

export default Comments;
