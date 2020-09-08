import React from 'react';
import Button from '@material-ui/core/Button';
import { deletePost } from '../../api/API'

const onDeletePost = (accessToken, postId) => {
  deletePost(accessToken, postId);
};

const DeletePostButton = ({accessToken, postId}) => {
  console.log(postId)
  return (
    <Button onClick={() => onDeletePost(accessToken, postId)}>
      글 삭제
    </Button>
  );
};

export default DeletePostButton;
