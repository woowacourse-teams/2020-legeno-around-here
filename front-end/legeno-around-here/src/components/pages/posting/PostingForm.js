import React, { useState } from 'react';
import Loading from '../../Loading';
import TextField from '@material-ui/core/TextField';
import IconButton from '@material-ui/core/IconButton';
import { Button, Typography } from '@material-ui/core';
import AddPhotoAlternateIcon from '@material-ui/icons/AddPhotoAlternate';

import { createPost, savePostImages } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import useStyles from './PostingFormStyles';
import SectorApplyButton from '../sector/SectorApplyButton';
import PostingFormImages from './PostingFormImages';
import PostingFormSectorSearch from './PostingFormSectorSearch';

const PostingForm = () => {
  const classes = useStyles();
  const [accessToken] = useState(getAccessTokenFromCookie());

  const [writing, setWriting] = useState('');
  const [sector, setSector] = useState(null);
  const [area] = useState({
    id: localStorage.getItem('mainAreaId'),
    name: localStorage.getItem('mainAreaName'),
  });
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [imageLoading, setImageLoading] = useState(false);

  const onImagesChanged = async (e) => {
    const localImages = e.target.files;

    const formData = new FormData();
    if (localImages.length > 0) {
      Array.from(localImages).forEach((localImage) => {
        formData.append('images', localImage);
      });
    }

    setImageLoading(true);
    await savePostImages(formData, accessToken).then((response) => {
      setImages(response.data);
    });
    setImageLoading(false);
  };

  const handleImageDelete = (id) => {
    const deletedImages = images.filter((image) => image.id !== id);
    setImages(deletedImages);
  };

  const onWritingChanged = (e) => {
    setWriting(e.target.value);
  };

  const countImages = () => {
    return <Typography display='inline'>총 {images.length} 개의 사진을 올렸습니다!</Typography>;
  };

  const submitPost = (e) => {
    e.preventDefault();

    try {
      validateForm();
    } catch (e) {
      alert(e.message);
      return;
    }

    const imageIds = images.map((image) => image.id);

    const postData = {
      writing: writing,
      imageIds: imageIds,
      areaId: area.id,
      sectorId: sector.id,
    };

    const sendPost = async () => {
      setLoading(true);
      await createPost(postData, accessToken);
      setLoading(false);
    };
    sendPost();
  };

  const validateForm = () => {
    if (area.id === null) {
      throw new Error('지역을 선택해주세요!');
    }
    if (writing === '' && images.length === 0) {
      throw new Error('아무것도 안 쓴 글을 올릴 수 없습니다! 뭔가 써주세요 :)');
    }
    if (sector.id === null) {
      throw new Error('부문을 선택해주세요!');
    }
  };

  if (loading) {
    return <Loading />;
  }
  return (
    <>
      <form onSubmit={submitPost} id='posting-form'>
        <IconButton className={classes.button}>
          <label htmlFor='upload-photo'>
            <AddPhotoAlternateIcon />
          </label>
        </IconButton>
        <input
          type='file'
          name='photo'
          id='upload-photo'
          className={classes.uploadPhoto}
          multiple
          onChange={onImagesChanged}
        />
        {countImages()}
        {imageLoading && <Loading />}
        {images.length > 0 && <PostingFormImages handleImageDelete={handleImageDelete} images={images} />}
        <TextField
          type='text'
          fullWidth
          id='standard-multiline-static'
          label=''
          multiline
          rows={10}
          placeholder='자신의 자랑을 입력해주세요!'
          onChange={onWritingChanged}
          value={writing}
          inputProps={{ maxLength: 2000 }}
        />
        <PostingFormSectorSearch setSector={setSector} />
        {sector && sector.id !== null ? <Typography className={classes.sector}>{sector.name}</Typography> : ''}
        <br />
        <Button className={classes.selectAreaButton}>지역 설정</Button>
        {area && area.id !== null ? <Typography className={classes.area}>{area.name}</Typography> : ''}
        <Typography>
          참가하고 싶은 부문이 없으신가요? <SectorApplyButton />을 해보세요!!
        </Typography>
      </form>
    </>
  );
};

export default PostingForm;
