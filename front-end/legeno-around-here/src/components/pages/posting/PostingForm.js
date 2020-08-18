import React, { useState, useEffect } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Loading from '../../Loading';
import TextField from '@material-ui/core/TextField';
import IconButton from '@material-ui/core/IconButton';
import {
  Typography,
  Backdrop,
  Button,
  Modal,
  Fade,
  List,
  ListItem,
  ListItemText,
  Divider,
} from '@material-ui/core';
import { createPost } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { findAllSectors } from '../../api/API';
import AddPhotoAlternateIcon from '@material-ui/icons/AddPhotoAlternate';
import SectorApplyButton from '../sector/SectorApplyButton';

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(0),
  },
  title: {
    display: 'block',
  },
  sectionDesktop: {
    display: 'flex',
  },
  modal: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  paper: {
    backgroundColor: theme.palette.background.paper,
    border: '2px solid #000',
    boxShadow: theme.shadows[5],
    padding: theme.spacing(2, 4, 3),
  },
  list: {
    height: 400,
    overflow: 'auto',
  },
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
  const [sectorId, setSectorId] = useState('');
  const [sectors, setSectors] = useState([]);
  const [open, setOpen] = useState(false);

  useEffect(() => {
    const loadSectors = async () => {
      setLoading(true);
      const allSectors = await findAllSectors(accessToken);
      setSectors(allSectors);
      setLoading(false);
    };
    loadSectors();
  }, [accessToken]);

  const onImagesChanged = (e) => {
    setImages(e.target.files);
  };

  const onWritingChanged = (e) => {
    setWriting(e.target.value);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
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
    formData.append('sectorId', sectorId);

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
      <form onSubmit={submitPost} id="posting-form">
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
        <Typography>
          아직 부문을 정하지 않으셨나요? <SectorApplyButton />을 해보세요!!
        </Typography>
        <Button onClick={handleOpen}>부문 목록</Button>
      </form>
      <Modal
        aria-labelledby="transition-modal-title"
        aria-describedby="transition-modal-description"
        className={classes.modal}
        open={open}
        onClose={handleClose}
        closeAfterTransition
        BackdropComponent={Backdrop}
        BackdropProps={{
          timeout: 500,
        }}
      >
        <Fade in={open}>
          <div className={classes.paper}>
            {sectors.length > 0 && (
              <List component="nav" className={classes.list}>
                {sectors.map((sector) => (
                  <>
                    <ListItem
                      key={sector.id}
                      alignItems="flex-start"
                      onClick={() => setSectorId(sector.id)}
                    >
                      <ListItemText
                        primary={sector.name + ' 부문'}
                        secondary={sector.description}
                      />
                    </ListItem>
                    <Divider />
                  </>
                ))}
              </List>
            )}
          </div>
        </Fade>
      </Modal>
    </>
  );
};

export default PostingForm;
