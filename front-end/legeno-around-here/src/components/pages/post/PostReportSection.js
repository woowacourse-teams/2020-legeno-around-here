import React, { useState } from 'react';
import { IconButton, TextField } from '@material-ui/core';
import ReportIcon from '@material-ui/icons/Report';
import Modal from '@material-ui/core/Modal';
import Backdrop from '@material-ui/core/Backdrop';
import Fade from '@material-ui/core/Fade';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import { makeStyles } from '@material-ui/core/styles';
import Button from '@material-ui/core/Button';
import Grid from '@material-ui/core/Grid';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { createPostReport } from '../../api/API';
import Loading from '../../Loading';
import LinesEllipsis from 'react-lines-ellipsis';
import Typography from '@material-ui/core/Typography';

const useStyles = makeStyles((theme) => ({
  center: {
    textAlign: 'center',
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
  grow: {
    flexGrow: 1,
  },
}));

const PostReportSection = ({ post }) => {
  const classes = useStyles();
  const accessToken = getAccessTokenFromCookie();
  const [value, setValue] = useState('부적절한 홍보 게시글');
  const [open, setOpen] = useState(false);
  const [writing, setWriting] = useState('');
  const [loading, setLoading] = useState(false);

  const reportText = [
    { id: 1, value: '부적절한 홍보 게시글' },
    { id: 2, value: '음란성 또는 청소년에게 부적합한 내용' },
    { id: 3, value: '명예훼손/사생활 침해 및 저작권 침해등' },
    { id: 4, value: '기타' },
  ];

  const submitReport = async () => {
    const data = {
      targetId: post.id,
      writing: writing,
    };
    setLoading(true);
    await createPostReport(post.id, data, accessToken);
    setLoading(false);
    handleClose();
  };

  const onWritingChanged = (e) => {
    setWriting(e.target.value);
  };

  const handleChange = (event) => {
    setValue(event.target.value);
    if (event.target.value === '기타') {
      setWriting('');
      return;
    }
    setWriting(value);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setWriting('부적절한 홍보 게시글');
    setOpen(false);
  };

  return (
    <>
      <IconButton onClick={handleOpen}>
        <ReportIcon />
      </IconButton>
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
            {loading === true ? (
              <Loading />
            ) : (
              <>
                <Typography variant='subtitle1'>
                  <div className={classes.center}>내용</div>
                  <LinesEllipsis text={post.writing} maxLine='2' ellipsis='...' trimRight basedOn='letters' />
                </Typography>
                <Typography variant='subtitle1'>
                  <div className={classes.center}>작성자</div>
                  <div className={classes.center}>{post.creator.nickname}</div>
                </Typography>
                <FormControl component='fieldset'>
                  <FormLabel component='legend'>사유선택</FormLabel>
                  <Typography variant='subtitle2'>
                    여러 사유에 해당하는 경우, 대표적인 사유 1개를 선택해주세요!
                  </Typography>
                  <RadioGroup aria-label='gender' value={value} onChange={handleChange}>
                    {reportText.map((report) => (
                      <FormControlLabel key={report.id} value={report.value} control={<Radio />} label={report.value} />
                    ))}
                  </RadioGroup>
                </FormControl>
                {value === '기타' ? (
                  <TextField
                    type='text'
                    id='standard-multiline-static'
                    fullWidth
                    multiline
                    rows={4}
                    placeholder='신고할 내용을 입력해주세요!'
                    onChange={onWritingChanged}
                    value={writing}
                  />
                ) : (
                  ''
                )}
                <Grid container>
                  <Button onClick={() => submitReport()}>신고</Button>
                  <div className={classes.grow} />
                  <Button onClick={() => handleClose()}>취소</Button>
                </Grid>
              </>
            )}
          </div>
        </Fade>
      </Modal>
    </>
  );
};

export default PostReportSection;
