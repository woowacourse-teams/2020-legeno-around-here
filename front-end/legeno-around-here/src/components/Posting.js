import styled from 'styled-components';

import React from 'react';
import OutBox from './OutBox';
import TopBarBackground from './posting/TopBarBackground';
import Cancle from './posting/Cancle';
import TextInput from './posting/TextArea';
import ImageInput from './posting/ImageInput';
import Submit from './posting/Submit';

const PostingBox = styled.div`
  width: 100%;
  height: 100%;
  background-color: rgba(240, 240, 240, 1);
  text-align: center;
`;

const Posting = () => {
  return (<>
    <OutBox>
      <TopBarBackground>
        <Cancle linkTo="/"></Cancle>
        <div>성북구</div>
        <Submit/>
      </TopBarBackground>
      <PostingBox>
        <TextInput/>
        <ImageInput> + 자랑할 사진을 올려주세요</ImageInput>
        <button>부문을 추가해주세요</button>
      </PostingBox>
    </OutBox>
  </>);
};

export default Posting;
