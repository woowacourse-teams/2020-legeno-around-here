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

const SectorApplyModal = ({ open, handleClose }) => {
  const NAME_MIN_LENGTH = 2;
  const NAME_MAX_LENGTH = 20;
  const DESCRIPTION_MIN_LENGTH = 2;
  const DESCRIPTION_MAX_LENGTH = 20;

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
                  부문은 관리자 승인 후 등록됩니다. 승인까지 하루 정도
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
                  <div className={classes.grow} />
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
  );
};

export default SectorApplyModal;
