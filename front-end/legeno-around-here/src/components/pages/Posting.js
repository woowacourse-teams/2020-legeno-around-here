import React, { useState } from 'react';
import styled from 'styled-components';

import TopBar from '../posting/TopBar';
import { getAccessTokenFromCookie } from '../../util/TokenUtils';
import TextInput from '../posting/TextInput';
import ImageInput from '../posting/ImageInput';
import Bottom from '../Bottom';
import { WRITING } from '../../constants/BottomItems';
import { createPost } from '../api/API';

const Form = styled.form`
  width: 100%;
  height: 100%;
`;

const PostingBox = styled.div`
  width: 100%;
  height: 100%;
  background-color: white;
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

    const mainAreaId = localStorage.getItem('mainAreaId');

    const formData = new FormData();
    if (images.length > 0) {
      Array.from(images).forEach((image) => {
        formData.append('images', image);
      });
    }
    formData.append('writing', writing);
    formData.append('areaId', mainAreaId);
    formData.append('sectorId', 1);

    const sendPost = async () => {
      setLoading(true);
      await createPost(formData, accessToken);
      setLoading(false);
    };
    sendPost();
  };

  if (loading) {
    return <div>현재 전송중입니다 :) 조금만 기다려주세요!</div>;
  }

  return (
    <>
      <Form onSubmit={submitPost}>
        <TopBar cancelButtonLink="/" />
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
          <button onClick={(e) => e.preventDefault()}>
            부문을 추가해주세요
          </button>
        </PostingBox>
      </Form>
      <Bottom selected={WRITING} />
    </>
  );
};

export default Posting;
