import React from 'react';
import Button from '@material-ui/core/Button';
import { Link } from 'react-router-dom';

const UpdatePostButton = ({ post }) => {
  return (
    <Button>
      <Link to={`/posts/${post.id}/update`}>글 수정</Link>
    </Button>
  );
};

export default UpdatePostButton;
