import React, { useState } from 'react';
import { Backdrop, Button, Fade, Grid, Modal, TextField, Typography } from '@material-ui/core';
import useStyles from './SectorApplyButtonStyles';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { createPendingSector } from '../../api/API';
import Loading from '../../Loading';

const SectorApplyModal = ({ open, handleClose, history }) => {
  const classes = useStyles();
  const accessToken = getAccessTokenFromCookie();
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [loading, setLoading] = useState(false);

  const onNameChanged = (e) => {
    setName(e.target.value);
  };

  const onDescriptionChanged = (e) => {
    setDescription(e.target.value);
  };

  const submitSector = (e) => {
    e.preventDefault();

    const sendSector = async () => {
      setLoading(true);
      await createPendingSector({ name, description }, accessToken, history);
      setLoading(false);
    };
    sendSector();
    handleClose();
  };

  if (loading) return <Loading />;
  return (
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
          <h2>부문 신청</h2>
          <form onSubmit={submitSector} id='posting-form'>
            <Grid container spacing={2}>
              <Grid item xs={12}>
                <TextField
                  label='부문명'
                  type='text'
                  fullWidth
                  multiline
                  rows={1}
                  placeholder='부문명을 입력해주세요! (20자 이내 작성)'
                  onChange={onNameChanged}
                  value={name}
                  inputProps={{ maxLength: 20 }}
                  helperText={`${name.length}/20`}
                  variant='outlined'
                />
              </Grid>
              <Grid item xs={12}>
                <TextField
                  label='부문 설명'
                  type='text'
                  fullWidth
                  multiline
                  rows={2}
                  placeholder='부문을 간단하게 설명해주세요! (40자 이내 작성)'
                  onChange={onDescriptionChanged}
                  value={description}
                  inputProps={{ maxLength: 40 }}
                  helperText={`${description.length}/40`}
                  variant='outlined'
                />
              </Grid>
              <Grid item xs={12}>
                <Typography variant='caption' color='secondary' noWrap>
                  부문은 관리자 승인 후 등록됩니다. 승인까지 하루 정도 소요됩니다.
                </Typography>
              </Grid>
              <Grid item xs={12}>
                <div className={classes.flex}>
                  <Button type='submit'>
                    <Typography variant='h6' noWrap>
                      신청하기
                    </Typography>
                  </Button>
                  <div className={classes.grow} />
                  <Button onClick={handleClose}>
                    <Typography variant='h6' noWrap>
                      취소
                    </Typography>
                  </Button>
                </div>
              </Grid>
            </Grid>
          </form>
        </div>
      </Fade>
    </Modal>
  );
};

export default SectorApplyModal;
