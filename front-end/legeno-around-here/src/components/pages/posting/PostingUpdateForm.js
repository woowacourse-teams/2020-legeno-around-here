import React, { useEffect, useState } from 'react';
import Loading from '../../Loading';
import { Typography } from '@material-ui/core';

import { createPost, findPost } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import useStyles from './PostingFormStyles';

const PostingUpdateForm = ({ postId }) => {
  const classes = useStyles();
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [writing, setWriting] = useState('');
  const [post, setPost] = useState(null);
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadPost = async () => {
      setLoading(true);
      const foundPost = await findPost(accessToken, postId);
      setPost(foundPost);
      setWriting(foundPost.writing);
      setLoading(false);
    };
    loadPost();
  }, [accessToken]);

  const onImagesChanged = (e) => {
    setImages(e.target.files);
  };

  const onWritingChanged = (e) => {
    setWriting(e.target.value);
  };

  const countImages = () => {
    return (
      <Typography display='inline'>
        총 {images.length} 개의 사진을 새로 올렸습니다!
      </Typography>
    );
  };

  const submitPost = (e) => {
    e.preventDefault();

    const formData = new FormData();
    if (images.length > 0) {
      Array.from(images).forEach((image) => {
        formData.append('images', image);
      });
    }
    formData.append('writing', writing);

    const sendPost = async () => {
      setLoading(true);
      await createPost(formData, accessToken);
      setLoading(false);
    };
    sendPost();
  };
  if (loading) {
    return <Loading />;
  }
  return (
    <>
      {console.log(post)}
      {/*<form onSubmit={submitPost} id='posting-form'>*/}
      {/*  <IconButton className={classes.button}>*/}
      {/*    <label htmlFor='upload-photo'>*/}
      {/*      <AddPhotoAlternateIcon />*/}
      {/*    </label>*/}
      {/*  </IconButton>*/}
      {/*  <input*/}
      {/*    type='file'*/}
      {/*    name='photo'*/}
      {/*    id='upload-photo'*/}
      {/*    className={classes.uploadPhoto}*/}
      {/*    multiple*/}
      {/*    onChange={onImagesChanged}*/}
      {/*  />*/}
      {/*  {countImages()}*/}
      {/*  <TextField*/}
      {/*    type='text'*/}
      {/*    fullWidth*/}
      {/*    id='standard-multiline-static'*/}
      {/*    label=''*/}
      {/*    multiline*/}
      {/*    rows={20}*/}
      {/*    placeholder='자신의 자랑을 입력해주세요!'*/}
      {/*    onChange={onWritingChanged}*/}
      {/*    value={writing}*/}
      {/*  />*/}
      {/*  <Button onClick={handleOpen} className={classes.selectSectorButton}>*/}
      {/*    부문 설정*/}
      {/*  </Button>*/}
      {/*  {sector.id !== null ? (*/}
      {/*    <Typography className={classes.sector}>{sector.name}</Typography>*/}
      {/*  ) : (*/}
      {/*    ''*/}
      {/*  )}*/}
      {/*  <br />*/}
      {/*  <Button className={classes.selectAreaButton}>지역 설정</Button>*/}
      {/*  {area.id !== null ? (*/}
      {/*    <Typography className={classes.area}>{area.name}</Typography>*/}
      {/*  ) : (*/}
      {/*    ''*/}
      {/*  )}*/}
      {/*  <Typography>글을 수정할 때는 부문과 지역 설정이 불가합니다!</Typography>*/}
      {/*</form>*/}
    </>
  );
};

export default PostingUpdateForm;
