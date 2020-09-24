import React from 'react';
import Button from '@material-ui/core/Button';
import { deletePost } from '../../api/API';

const onDeletePost = (accessToken, postId, history) => {
  if (window.confirm("정말 삭제하시겠습니까?")) {
    deletePost(accessToken, postId, history)
  }
};

const DeletePostButton = ({ accessToken, postId, history }) => {
  return <Button onClick={() => onDeletePost(accessToken, postId, history)}>글 삭제</Button>;
};

export default DeletePostButton;
