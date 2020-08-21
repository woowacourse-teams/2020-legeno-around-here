import React from 'react';
import IconButton from '@material-ui/core/IconButton';
import { saveProfilePhoto } from '../../api/API';
import Typography from '@material-ui/core/Typography';
import useStyle from './PhotoEditSectionStyle';

const PhotoEditSection = ({
  originalPhotoUrl,
  setProfilePhoto,
  accessToken,
}) => {
  const props = {
    photoUrl: originalPhotoUrl,
  };
  const classes = useStyle(props);

  const onImagesChanged = (e) => {
    const formData = new FormData();
    formData.append('image', e.target.files[0]);
    saveProfilePhoto(formData, accessToken).then((responseData) => {
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
        <IconButton className={classes.button}>
          <label htmlFor="upload-photo">
            <Typography className={classes.photoEditButton}>수정</Typography>
          </label>
        </IconButton>
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
