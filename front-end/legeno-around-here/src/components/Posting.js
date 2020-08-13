import React, { useState } from 'react';
import styled from 'styled-components';
import axios from 'axios';
import { getAccessTokenFromCookie } from '../util/TokenUtils';
import OutBox from './OutBox';
import TopBarBackground from './posting/TopBarBackground';
import Cancle from './posting/Cancle';
import TextInput from './posting/TextInput';
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
  const [writing, setWriting] = useState('');
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(false);

  const onImagesChanged = (e) => {
    setImages(e.target.files);
  };

  const onWritingChanged = (e) => {
    setWriting(e.target.value);
  };

  const submitPost = (e) => {
    e.preventDefault();

    const url = 'http://localhost:8080/posts';

    const formData = new FormData();
    if (images.length > 0) {
      Array.from(images).forEach((image) => {
        formData.append('images', image);
      });
    }
    formData.append('writing', writing);
    formData.append('areaId', 1);
    formData.append('sectorId', 1);

    const config = {
      headers: {
        'X-Auth-Token': accessToken,
      },
    };

    const sendPost = async () => {
      setLoading(true);
      try {
        const response = await axios.post(url, formData, config);
        if (response.status === 201) {
          alert('전송에 성공했습니다!');
          document.location.href = response.headers.location;
        }
      } catch (e) {
        console.log(e);
      }
      setLoading(false);
    };
    sendPost();
  };

  if (loading) {
    return <div>현재 전송중입니다 :) 조금만 기다려주세요!</div>;
  }

  return (
    <>
      <OutBox>
        <Form onSubmit={submitPost}>
          <TopBarBackground>
            <Cancle linkTo="/"></Cancle>
            <div>성북구</div>
            <SubmitButton />
          </TopBarBackground>

          <PostingBox>
            <TextInput
              placeholder="자랑거리를 입력해주세요"
              onChange={onWritingChanged}
              value={writing}
            />
            <ImageInput
              type="file"
              multiple
              onChange={onImagesChanged}
            ></ImageInput>
            <button>부문을 추가해주세요</button>
          </PostingBox>
        </Form>
      </OutBox>
    </>
  );
};

export default Posting;
