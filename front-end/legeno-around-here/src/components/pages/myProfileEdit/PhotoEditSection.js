import React from 'react';
import { MAIN_COLOR } from '../../../constants/Color';
import makeStyles from '@material-ui/core/styles/makeStyles';
import { Button } from '@material-ui/core';

const useStyle = makeStyles({
  originalPhoto: (props) => ({
    width: '100px',
    height: '100px',
    backgroundRepeat: 'no-repeat',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    borderRadius: '300px',
    backgroundImage: `url('${props.originalPhotoUrl}')`,
    border: '1px solid' + MAIN_COLOR,
  }),
  photoEditSection: {
    width: '90%',
    display: 'flex',
    alignItems: 'center',
    margin: '20px auto',
  },
});

const PhotoEditSection = ({ originalPhotoUrl }) => {
  const props = {
    originalPhotoUrl: originalPhotoUrl,
  };
  const classes = useStyle(props);

  return (
    <div className={classes.photoEditSection}>
      <div className={classes.originalPhoto} />
      <Button>수정</Button>
    </div>
  );
};

export default PhotoEditSection;
