import React, { useEffect, useState } from 'react';
import Loading from '../../Loading';
import { Typography } from '@material-ui/core';

import { findPost, savePostImages, updatePost } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import useStyles from '../posting/PostingFormStyles';
import PostingFormImages from './PostingFormImages';
import IconButton from '@material-ui/core/IconButton';
import TextField from '@material-ui/core/TextField';
import AddPhotoAlternateIcon from '@material-ui/icons/AddPhotoAlternate';

const PostingUpdateForm = ({ postId }) => {
  const classes = useStyles();
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [writing, setWriting] = useState('');
  const [post, setPost] = useState(null);
  const [images, setImages] = useState([]);
  const [sectorName, setSectorName] = useState(null);
  const [areaName, setAreaName] = useState(null);
  const [imageLoading, setImageLoading] = useState(false);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadPost = async () => {
      setLoading(true);
      const foundPost = await findPost(accessToken, postId);
      setPost(foundPost);
      setWriting(foundPost.writing);
      setImages(foundPost.images);
      setSectorName(foundPost.sector.name);
      setAreaName(localStorage.getItem('mainAreaName'));
      setLoading(false);
    };
    loadPost();
  }, [accessToken, postId]);

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
    return <Typography display='inline'>총 {images.length} 개의 사진이 있습니다!</Typography>;
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

    const postUpdateData = {
      writing: writing,
      imageIds: imageIds,
    };

    const sendUpdatePost = async () => {
      setLoading(true);
      await updatePost(post.id, postUpdateData, accessToken);
      setLoading(false);
    };
    sendUpdatePost();
  };

  const validateForm = () => {
    if (writing === '' && images.length === 0) {
      throw new Error('아무것도 안 쓴 글을 올릴 수 없습니다! 뭔가 써주세요 :)');
    }
  };

  if (loading) {
    return <Loading />;
  }
  return (
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
        helperText={`${writing.length}/2000`}
        multiline
        rows={10}
        placeholder='자신의 자랑을 입력해주세요!'
        onChange={onWritingChanged}
        value={writing}
        variant='outlined'
        inputProps={{ maxLength: 2000 }}
      />
      {sectorName && <Typography className={classes.sector}>{sectorName}</Typography>}
      {areaName && <Typography className={classes.area}>{areaName}</Typography>}
      <Typography>글을 수정할 때는 부문과 지역 설정이 불가합니다!</Typography>
    </form>
  );
};

export default PostingUpdateForm;
