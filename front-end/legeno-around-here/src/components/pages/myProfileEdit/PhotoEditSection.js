import React from 'react';
import { saveProfilePhoto } from '../../api/API';
import Typography from '@material-ui/core/Typography';
import useStyle from './PhotoEditSectionStyle';
import Button from '@material-ui/core/Button'
import { DEFAULT_IMAGE_URL } from './MyProfileEditPage'

const PhotoEditSection = ({
  profilePhoto,
  setProfilePhoto,
  accessToken,
  history,
}) => {
  const props = {
    profilePhoto: profilePhoto,
  };
  const classes = useStyle(props);

  const applyDefaultImage = () => {
    setProfilePhoto({
      id: null,
      url: DEFAULT_IMAGE_URL
    });
  }

  const onImagesChanged = (e) => {
    const formData = new FormData();
    formData.append('image', e.target.files[0]);
    saveProfilePhoto(formData, accessToken, history).then((responseData) => {
      if (responseData) {
        setProfilePhoto({
          id: responseData.id,
          url: responseData.url,
        });
      }
    });
  };

  return (
    <div className={classes.photoEditSection}>
      <div className={classes.photo} />
      <div>
        <Button className={classes.button}>
          <label htmlFor="upload-photo">
            <Typography className={classes.photoEditButton}>수정</Typography>
          </label>
        </Button>
        <Button className={classes.button} onClick={applyDefaultImage}>
          <label>
            <Typography className={classes.usingDefaultPhotoButton}>기본이미지 사용</Typography>
          </label>
        </Button>
        <input
          type="file"
          name="photo"
          id="upload-photo"
          className={classes.uploadPhoto}
          onChange={onImagesChanged}
        />
      </div>
    </div>
  );
};

export default PhotoEditSection;
