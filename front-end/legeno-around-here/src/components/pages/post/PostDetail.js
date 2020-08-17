import React, { useState, useEffect } from 'react';

import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import Bottom from '../../Bottom';
import Typography from '@material-ui/core/Typography';

const PostDetail = ({ post }) => {
  return (
    <>
      <Typography>
        {post.area.fullName}
        {post.createdAt}
        {post.creator.image.url}
        {post.creator.nickname}
        {post.sector.name}
        {post.writing}
      </Typography>
      <Image>{post.images}</Image>
      <Typography>{post.zzang.count}</Typography>
      {post.zzang.state}
    </>
    /* <PostSpace>
        <PostMetaData>
          <Typography color="textSecondary">{post.areaName}</Typography>
          <ToRight>
            <Typography color="textSecondary">{post.creatorName}</Typography>
          </ToRight>
        </PostMetaData>
        <SectionName>{post.sectorName}</SectionName>
        <Typography>{post.writing}</Typography>
        <Image src="/logo512.png"></Image>
        <Typography color="textSecondary">
          짱이야 {post.zzangCount} &nbsp; 댓글 {post.comments.length}
        </Typography>
        <Line></Line>
      </PostSpace> */
  );
};

export default PostDetail;
