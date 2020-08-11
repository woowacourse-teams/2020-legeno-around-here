import React, { useState } from 'react';
import styled from 'styled-components';
import axios, { post } from 'axios';

import { getAccessTokenFromCookie } from "../util/TokenUtils";
import OutBox from './OutBox';
import TopBarBackground from './posting/TopBarBackground';
import Cancle from './posting/Cancle';
import TextInput from './posting/TextArea';
import ImageInput from './posting/ImageInput';
import SubmitButton from './posting/SubmitButton';

const Form = styled.form`
  width: 100%;
  height: 100%;
`;

const PostingBox = styled.div`
  width: 100%;
  height: 100%;
  background-color: rgba(240, 240, 240, 1);
  text-align: center;
`;

const Posting = () => {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [writing, setWriting] = useState("");
  const [images, setImages] = useState(null);

  const onImagesChanged = e => {
    setImages(e.target.files[0]);
  };

  const onWritingChanged = e => {
    setWriting(e.target.value);
  };

  const submitPost = e => {
    e.preventDefault();

    const url = 'http://localhost:8080/posts';

    const formData = new FormData();
    // formData.append('images', images);
    formData.append('writing', writing);
    
    const config = {
      headers: {
          'content-type': 'multipart/form-data',
          "X-Auth-Token": accessToken
      }
    };

    post(url, formData, config).then(response => {
      alert(response.status);
      document.location.href = "/";
    });
  };

  return (<>
    <OutBox>
      <Form onSubmit={ submitPost }>
        <TopBarBackground>
          <Cancle linkTo="/"></Cancle>
          <div>성북구</div>
          <SubmitButton/>
        </TopBarBackground>

        <PostingBox>
          <TextInput placeholder="자랑거리를 입력해주세요" 
            onChange={ onWritingChanged } value={ writing } />
          <ImageInput type="file" onChange={ onImagesChanged }></ImageInput>
          <button>부문을 추가해주세요</button>
        </PostingBox>
      </Form>
    </OutBox>
  </>);
};

export default Posting;
