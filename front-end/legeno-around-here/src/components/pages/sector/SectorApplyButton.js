import React, { useState } from 'react';
import {
  Backdrop,
  Button,
  Fade,
  TextField,
  Modal,
  Typography,
  Grid,
} from '@material-ui/core';
import useStyles from './SectorApplyButtonStyles';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { createPendingSector } from '../../api/API';
import Loading from '../../Loading';
import ErrorTypography from '../../ErrorTypography';

const SectorApplyButton = () => {
  const NAME_MIN_LENGTH = 2;
  const NAME_MAX_LENGTH = 20;
  const DESCRIPTION_MIN_LENGTH = 2;
  const DESCRIPTION_MAX_LENGTH = 20;

  const classes = useStyles();
  const accessToken = getAccessTokenFromCookie();
  const [name, setName] = useState('');
  const [description, setDescription] = useState('');
  const [open, setOpen] = useState(false);
  const [loading, setLoading] = useState(false);

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  const onNameChanged = (e) => {
    setName(e.target.value);
  };

  const onDescriptionChanged = (e) => {
    setDescription(e.target.value);
  };

  const checkName = () => {
    if (validateInputLength(name, NAME_MIN_LENGTH, NAME_MAX_LENGTH)) {
      return (
        <ErrorTypography
          content={`부문명은 ${NAME_MIN_LENGTH} ~ ${NAME_MAX_LENGTH} 글자 이내로 작성해주세요!`}
        />
      );
    }
  };

  const checkDescription = () => {
    if (
      validateInputLength(
        description,
        DESCRIPTION_MIN_LENGTH,
        DESCRIPTION_MAX_LENGTH,
      )
    ) {
      return (
        <ErrorTypography
          content={`부문설명은 ${DESCRIPTION_MIN_LENGTH} ~ ${DESCRIPTION_MAX_LENGTH} 글자 이내로 작성해주세요!`}
        />
      );
    }
  };

  const validateInputLength = (input, minLength, maxLength) => {
    return input && (input.length < minLength || input.length > maxLength);
  };

  const submitSector = (e) => {
    e.preventDefault();

    const sendSector = async () => {
      setLoading(true);
      await createPendingSector({ name, description }, accessToken);
      setLoading(false);
    };
    sendSector();
    handleClose();
  };

  if (loading) return <Loading />;
  return (
    <>
      <Button
        edge="start"
        color="inherit"
        aria-label="open drawer"
        onClick={handleOpen}
      >
        <Typography variant="h6" noWrap>
          부문신청
        </Typography>
      </Button>
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
            <form onSubmit={submitSector} id="posting-form">
              <Grid container spacing={2}>
                <Grid item xs={12}>
                  <TextField
                    type="text"
                    fullWidth
                    multiline
                    rows={1}
                    placeholder="부문명을 입력해주세요!"
                    onChange={onNameChanged}
                    value={name}
                  />
                </Grid>
                <Grid item xs={12}>
                  {checkName()}
                </Grid>
                <Grid item xs={12}>
                  <TextField
                    type="text"
                    fullWidth
                    multiline
                    rows={2}
                    placeholder="부문을 간단하게 표현해주세요!"
                    onChange={onDescriptionChanged}
                    value={description}
                  />
                </Grid>
                <Grid item xs={12}>
                  {checkDescription()}
                </Grid>
                <Grid item xs={12}>
                  <Typography variant="caption" color="secondary" noWrap>
                    부문은 관리자가 결정합니다. 결정되기까지 하루정도
                    소요됩니다.
                  </Typography>
                </Grid>
                <Grid item xs={12}>
                  <div className={classes.flex}>
                    <Button type="submit">
                      <Typography variant="h6" noWrap>
                        신청하기
                      </Typography>
                    </Button>
                    <div className={classes.grow}></div>
                    <Button onClick={handleClose}>
                      <Typography variant="h6" noWrap>
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
    </>
  );
};

export default SectorApplyButton;
