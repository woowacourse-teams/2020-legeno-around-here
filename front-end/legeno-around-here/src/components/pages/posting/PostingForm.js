import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Loading from '../../Loading';
import TextField from '@material-ui/core/TextField';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import { createPost } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import AddPhotoAlternateIcon from '@material-ui/icons/AddPhotoAlternate';

const useStyles = makeStyles(() => ({
  uploadPhoto: {
    opacity: 0,
    position: 'absolute',
    zIndex: -1,
    pointerEvents: 'none',
  },
}));

const PostingForm = () => {
  const classes = useStyles();
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

  const countImages = () => {
    return (
      <Typography display="inline">
        총 {images.length} 개의 사진을 올렸습니다!
      </Typography>
    );
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
    return <Loading />;
  }

  return (
    <form onSubmit={submitPost}>
      <IconButton className={classes.button}>
        <label htmlFor="upload-photo">
          <AddPhotoAlternateIcon />
        </label>
      </IconButton>
      <input
        type="file"
        name="photo"
        id="upload-photo"
        className={classes.uploadPhoto}
        multiple
        onChange={onImagesChanged}
      />
      {countImages()}
      <TextField
        type="text"
        fullWidth
        id="standard-multiline-static"
        label=""
        multiline
        rows={20}
        placeholder="자신의 자랑을 입력해주세요!"
        onChange={onWritingChanged}
        value={writing}
      />
    </form>
  );
};

export default PostingForm;
