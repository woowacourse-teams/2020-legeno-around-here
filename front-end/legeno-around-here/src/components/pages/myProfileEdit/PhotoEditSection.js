import React, { useState } from 'react'
import {MAIN_COLOR} from '../../../constants/Color';
import makeStyles from '@material-ui/core/styles/makeStyles';
import AddPhotoAlternateIcon from '@material-ui/core/SvgIcon/SvgIcon'
import IconButton from '@material-ui/core/IconButton'
import { saveProfilePhoto } from '../../api/API'
import Typography from '@material-ui/core/Typography'

const useStyle = makeStyles({
  photo: (props) => ({
    width: '100px',
    height: '100px',
    backgroundRepeat: 'no-repeat',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    borderRadius: '300px',
    backgroundImage: `url('${props.photoUrl}')`,
    border: '1px solid' + MAIN_COLOR,
  }),
  photoEditSection: {
    width: '90%',
    display: 'flex',
    alignItems: 'center',
    margin: '20px auto',
  },
  uploadPhoto: {
    opacity: 0,
    position: 'absolute',
    zIndex: -1,
    pointerEvents: 'none',
  },
  photoEditButton: {
    color: 'black',
  }
});

const PhotoEditSection = ({ originalPhotoUrl, accessToken }) => {
  const [photoUrl, setPhotoUrl] = useState(originalPhotoUrl);

  const props = {
    photoUrl: photoUrl,
  };
  const classes = useStyle(props);

  const onImagesChanged = (e) => {
    const formData = new FormData();
    formData.append('image', e.target.files[0]);
    saveProfilePhoto(formData, accessToken)
      .then(newPhotoUrl => {
        setPhotoUrl(newPhotoUrl);
        alert(newPhotoUrl);
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
