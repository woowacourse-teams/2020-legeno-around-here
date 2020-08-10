import styled from 'styled-components';

import React from 'react';
import OutBox from './OutBox';
import { TopBarBackground, Cancle, Submit } from './PostingTopBar';
import TextInput from './TextArea';
import ImageInput from './ImageInput'

const PostingTopBar = () => {
  return (
    <TopBarBackground>
      <Cancle/>
      <div>성북구</div>
      <Submit/>
    </TopBarBackground>
  );
};

const PostingBox = styled.div`
  width: 100%;
  height: 100%;
  background-color: rgba(240, 240, 240, 1);
  text-align: center;
`;

const Posting = () => {
  return (<>
    <OutBox>
        <PostingTopBar/>
        <PostingBox>
          <TextInput/>
          <ImageInput> + 자랑할 사진을 올려주세요</ImageInput>
          <button>부문을 추가해주세요</button>
        </PostingBox>
    </OutBox>
  </>);
};

export default Posting;
