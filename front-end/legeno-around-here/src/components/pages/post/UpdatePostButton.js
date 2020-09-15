import React from 'react';
import Button from '@material-ui/core/Button';
import LinkWithoutStyle from '../../../util/LinkWithoutStyle';

const UpdatePostButton = ({ post }) => {
  return (
    <Button>
      <LinkWithoutStyle to={`/posts/${post.id}/update`}>글 수정</LinkWithoutStyle>
    </Button>
  );
};

export default UpdatePostButton;
