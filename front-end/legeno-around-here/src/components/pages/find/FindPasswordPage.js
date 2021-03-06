import React, { useCallback, useMemo, useState } from 'react';
import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Grid from '@material-ui/core/Grid';
import Box from '@material-ui/core/Box';
import ThumbUpIcon from '@material-ui/icons/ThumbUp';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import {
  checkAuthNumber,
  checkExistEmail,
  findPasswordAuthMail,
  resetPassword,
} from '../../api/API';
import Copyright from '../../Copyright';

const InputCheck = (input) => {
  return (
    <Grid item xs={12}>
      <Typography variant='caption' color='error'>
        {input}
      </Typography>
    </Grid>
  );
};

const useStyles = makeStyles((theme) => ({
  paper: {
    marginTop: theme.spacing(8),
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
  form: {
    width: '100%', // Fix IE 11 issue.
    marginTop: theme.spacing(3),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
  input: { width: '58%' },
  check: {
    width: '38%',
    border: '1px solid gray',
    marginLeft: '4%',
    marginTop: '2%',
  },
}));

const FindPasswordPage = ({ history }) => {
  const classes = useStyles();
  const PASSWORD_MIN_LENGTH = 8;
  const PASSWORD_MAX_LENGTH = 16;
  const EMAIL_REGEX = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  const [email, setEmail] = useState('');
  const [authNumber, setAuthNumber] = useState('');
  const [password, setPassword] = useState('');
  const [passwordRepeat, setPasswordRepeat] = useState('');
  const [isEmailDisabled, setIsEmailDisabled] = useState(false);
  const [isAuthNumberDisabled, setIsAuthNumberDisabled] = useState(false);
  const [mailAuthToggle, setMailAuthToggle] = useState('인증 메일 전송');
  const [isMailSent, setIsMailSent] = useState(false);

  const validateEmail = useMemo(() => {
    return email && !EMAIL_REGEX.test(String(email).toLowerCase());
  }, [EMAIL_REGEX, email]);

  const validatePassword = useMemo(() => {
    return password && (password.length < PASSWORD_MIN_LENGTH || password.length > PASSWORD_MAX_LENGTH);
  }, [password]);

  const validatePasswordRepeat = useMemo(() => {
    return passwordRepeat && (passwordRepeat.length === 0 || password !== passwordRepeat);
  }, [password, passwordRepeat]);

  const emailCheck = useMemo(() => {
    if (validateEmail) {
      return InputCheck('올바른 이메일 형식을 입력해주세요.');
    }
  }, [validateEmail]);

  const passwordCheck = useMemo(() => {
    if (validatePassword) {
      return InputCheck('비밀번호 길이는 8 ~ 16 자로 해주세요.');
    }
  }, [validatePassword]);

  const passwordRepeatCheck = useMemo(() => {
    if (validatePasswordRepeat) {
      return InputCheck('비밀번호가 일치하지 않습니다.');
    }
  }, [validatePasswordRepeat]);

  const handleChangeEmail = useCallback(({ target: { value } }) => {
    setEmail(value);
  }, []);

  const handleChangeAuthNumber = useCallback(({ target: { value } }) => {
    setAuthNumber(value);
  }, []);

  const handleChangePassword = useCallback(({ target: { value } }) => {
    setPassword(value);
  }, []);

  const handleChangePasswordRepeat = useCallback(({ target: { value } }) => {
    setPasswordRepeat(value);
  }, []);

  const handleReset = useCallback(() => {
    setEmail('');
    setAuthNumber('');
    setPassword('');
    setPasswordRepeat('');
    setIsEmailDisabled(false);
    setIsAuthNumberDisabled(false);
    setMailAuthToggle('인증 메일 전송');
    setIsMailSent(false);
  }, []);

  const checkEmail = useCallback(() => {
    checkExistEmail(email);
  }, [email]);

  const mailAuthToggleFunction = useCallback(() => {
    if (isAuthNumberDisabled) {
      alert('인증이 완료됐습니다.');
      return;
    }
    if (!isMailSent) {
      alert('인증 메일을 전송했습니다.');
      findPasswordAuthMail(email, setIsEmailDisabled, setMailAuthToggle, setIsMailSent);
      return;
    }
    checkAuthNumber(email, authNumber, setIsAuthNumberDisabled);
  }, [email, authNumber, isMailSent, isAuthNumberDisabled]);

  const handleSubmit = useCallback(
    (event) => {
      event.preventDefault();
      if (validateEmail || validatePassword || validatePasswordRepeat) {
        alert('입력값을 확인해 주세요.');
        return;
      }
      resetPassword(email, password, handleReset, history);
    },
    [validateEmail, validatePassword, validatePasswordRepeat, email, password, handleReset, history],
  );

  return (
    <Container component='main' maxWidth='xs'>
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <ThumbUpIcon />
        </Avatar>
        <Typography component='h1' variant='h5'>
          우리동네캡짱 비밀번호 찾기
        </Typography>
        <form onSubmit={handleSubmit} className={classes.form} noValidate>
          <Grid container spacing={2}>
            <Grid item xs={12}>
              <TextField
                className={classes.input}
                variant='outlined'
                required
                fullWidth
                id='email'
                label='이메일'
                name='email'
                autoComplete='email'
                type='email'
                value={email}
                disabled={isEmailDisabled}
                onChange={handleChangeEmail}
              />
              <Button variant='contained' color='primary' className={classes.check} onClick={checkEmail}>
                메일 가입 확인
              </Button>
            </Grid>
            <Grid item xs={12}>
              <Typography variant='caption' color='error'>
                {emailCheck}
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                className={classes.input}
                variant='outlined'
                required
                fullWidth
                id='authNumber'
                label='인증 번호'
                name='authNumber'
                autoComplete='authNumber'
                type='authNumber'
                value={authNumber}
                disabled={isAuthNumberDisabled}
                onChange={handleChangeAuthNumber}
              />
              <Button variant='contained' color='primary' className={classes.check} onClick={mailAuthToggleFunction}>
                {mailAuthToggle}
              </Button>
            </Grid>
            <Grid item xs={12}>
              <Typography variant='caption' color='error' />
            </Grid>
            <Grid item xs={12}>
              <TextField
                variant='outlined'
                required
                fullWidth
                name='password'
                placeholder='새로운 비밀번호 입력'
                id='password'
                autoComplete='current-password'
                type='password'
                value={password}
                onChange={handleChangePassword}
              />
            </Grid>
            <Grid item xs={12}>
              <Typography variant='caption' color='error'>
                {passwordCheck}
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                variant='outlined'
                required
                fullWidth
                name='passwordRepeat'
                placeholder='새로운 비밀번호 확인'
                id='passwordRepeat'
                autoComplete='current-password'
                type='password'
                value={passwordRepeat}
                onChange={handleChangePasswordRepeat}
              />
            </Grid>
            <Grid item xs={12}>
              <Typography variant='caption' color='error'>
                {passwordRepeatCheck}
              </Typography>
            </Grid>
          </Grid>
          <Button type='submit' fullWidth variant='contained' color='primary' className={classes.submit}>
            비밀번호 변경
          </Button>
        </form>
      </div>
      <Box mt={5}>
        <Copyright />
      </Box>
    </Container>
  );
};

export default FindPasswordPage;
