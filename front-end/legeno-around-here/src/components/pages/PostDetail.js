import React, { useState, useEffect } from 'react';
import styled from 'styled-components';
import Typography from '@material-ui/core/Typography';

import { getAccessTokenFromCookie } from '../../util/TokenUtils';
import Bottom from '../Bottom';
import TopBar from '../postdetail/TopBar';
import Image from '../postdetail/Image';
import { findPost } from '../api/API';

const Line = styled.div`
  width: 100%;
  height: 1px;
  background-color: #bbbbbb;
`;

const SectionName = styled.div`
  width: 80%;
  border-radius: 100px;
  background-color: #eeeeee;
  display: flex;
  justify-content: center;
  align-content: center;
  font-size: 13px;
  padding-top: 3px;
  padding-bottom: 3px;
  margin-bottom: 10px;
`;

const PostSpace = styled.div`
  margin: 10px auto;
  width: 95%;
`;

const PostMetaData = styled.div`
  display: flex;
  flex-direction: row;
`;

const ToRight = styled.div`
  margin-right: 0;
  margin-left: auto;
`;

const PostDetail = ({ match }) => {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const { postId } = match.params;
  const [post, setPost] = useState({
    id: null,
    writing: '',
    images: null,
    areaName: '',
    sectionName: '',
    creatorName: '',
    zzangCount: 0,
    comments: [],
  });

  useEffect(() => {
    findPost({
      accessToken: accessToken,
      postId: postId,
      setPostState: setPost,
    });
  }, [accessToken, postId]);

  return (
    <>
      <TopBar backButtonLink="/"></TopBar>
      <PostSpace>
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
      </PostSpace>
      <Bottom></Bottom>
    </>
  );
};

export default PostDetail;
