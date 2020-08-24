import React, { useEffect, useState } from 'react';
import Loading from '../../Loading';
import TextField from '@material-ui/core/TextField';
import IconButton from '@material-ui/core/IconButton';
import { Backdrop, Button, Divider, Fade, List, ListItem, ListItemText, Modal, Typography } from '@material-ui/core';
import AddPhotoAlternateIcon from '@material-ui/icons/AddPhotoAlternate';

import { createPost, findSectorsFromPage, savePostImages } from '../../api/API';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import useStyles from './PostingFormStyles';
import SectorApplyButton from '../sector/SectorApplyButton';
import PostingFormImages from './PostingFormImages';

const PostingForm = () => {
  const classes = useStyles();
  const [accessToken] = useState(getAccessTokenFromCookie());

  const [writing, setWriting] = useState('');
  const [sector, setSector] = useState({
    id: null,
    name: '',
  });
  const [area] = useState({
    id: localStorage.getItem('mainAreaId'),
    name: localStorage.getItem('mainAreaName'),
  });
  const [images, setImages] = useState([]);
  const [loading, setLoading] = useState(false);
  const [imageLoading, setImageLoading] = useState(false);
  const [sectors, setSectors] = useState([]);
  const [open, setOpen] = useState(false);

  useEffect(() => {
    const loadSectors = async () => {
      setLoading(true);
      const allSectors = await findSectorsFromPage(0, accessToken);
      setSectors(allSectors);
      setLoading(false);
    };
    loadSectors();
  }, [accessToken]);

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

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
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
          rows={20}
          placeholder='자신의 자랑을 입력해주세요!'
          onChange={onWritingChanged}
          value={writing}
        />
        <Button onClick={handleOpen} className={classes.selectSectorButton}>
          부문 설정
        </Button>
        {sector.id !== null ? <Typography className={classes.sector}>{sector.name}</Typography> : ''}
        <br />
        <Button className={classes.selectAreaButton}>지역 설정</Button>
        {area.id !== null ? <Typography className={classes.area}>{area.name}</Typography> : ''}
        <Typography>
          참가하고 싶은 부문이 없으신가요? <SectorApplyButton />을 해보세요!!
        </Typography>
      </form>
      <Modal
        aria-labelledby='transition-modal-title'
        aria-describedby='transition-modal-description'
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
              <List component='nav' className={classes.list}>
                {sectors.map((sector) => (
                  <>
                    <ListItem
                      key={sector.id}
                      alignItems='flex-start'
                      onClick={() => {
                        setSector({
                          id: sector.id,
                          name: sector.name,
                        });
                        handleClose();
                      }}
                    >
                      <ListItemText primary={sector.name + ' 부문'} secondary={sector.description} />
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
