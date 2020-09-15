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
import { List } from 'immutable';

const PostingForm = ({ history }) => {
  const classes = useStyles();
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [writing, setWriting] = useState('');
  const [sector, setSector] = useState(null);
  const [area] = useState({
    id: localStorage.getItem('mainAreaId'),
    name: localStorage.getItem('mainAreaName'),
  });
  const [images, setImages] = useState(List([]));
  const [loading, setLoading] = useState(false);
  const [imageLoading, setImageLoading] = useState(false);

  const maxImagesLength = 10;
  const maxImageMBSize = 5;

  const validateImages = (localImages) => {
    if (isOverImagesLength(localImages)) {
      throw new Error(`이미지는 ${maxImagesLength}개까지 등록 가능합니다!`);
    }
    if (isOverImagesVolume(localImages)) {
      throw new Error(`이미지는 ${maxImageMBSize}MB 이내로 등록 가능합니다!`);
    }
  };

  const isOverImagesLength = (localImages) => {
    return localImages.length > maxImagesLength;
  };

  const isOverImagesVolume = (localImages) => {
    const images = Array.from(localImages);
    for (let image of images) {
      const imageMBSize = image.size / 1024 / 1024; // MB Size로 바꿈
      const parsingImageSize = parseFloat(imageMBSize.toFixed(2));
      if (parsingImageSize > maxImageMBSize) {
        return true;
      }
    }
    return false;
  };

  const onImagesChanged = async (e) => {
    const localImages = e.target.files;

    try {
      validateImages(localImages);
    } catch (e) {
      alert(e.message);
      return;
    }

    const formData = new FormData();
    if (localImages.length > 0) {
      Array.from(localImages).forEach((localImage) => {
        formData.append('images', localImage);
      });
    }

    setImageLoading(true);
    await savePostImages(formData, accessToken, history).then((response) => {
      setImages(images.concat(List(response.data)));
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
    return <Typography display='inline'>{`${images.size}/${maxImagesLength}`}</Typography>;
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
      await createPost(postData, accessToken, history);
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
    if (sector === null || sector.id === null) {
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
        {images.size > 0 && <PostingFormImages handleImageDelete={handleImageDelete} images={images.toJS()} />}
        <TextField
          type='text'
          fullWidth
          id='standard-multiline-static'
          helperText={`${writing.length}/2000`}
          multiline
          rows={10}
          placeholder='자신의 자랑을 입력해주세요!'
          onChange={onWritingChanged}
          value={writing}
          variant='outlined'
          inputProps={{ maxLength: 2000 }}
        />
        <PostingFormSectorSearch setSector={setSector} history={history} />
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
