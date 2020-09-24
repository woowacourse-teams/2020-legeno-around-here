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
import { createUser, checkJoined, sendAuthMail, checkAuthNumber } from '../api/API';
import Link from '@material-ui/core/Link';
import Checkbox from '@material-ui/core/Checkbox';
import Copyright from '../Copyright';
import TermsModal from '../signUp/TermsModal';
import PrivacyModal from '../signUp/PrivacyModal';

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
  check: { width: '38%', border: '1px solid gray', marginLeft: '4%', marginTop: '2%' },
}));

const JoinPage = ({ history }) => {
  const classes = useStyles();
  const NICKNAME_MIN_LENGTH = 1;
  const NICKNAME_MAX_LENGTH = 10;
  const PASSWORD_MIN_LENGTH = 8;
  const PASSWORD_MAX_LENGTH = 16;
  const EMAIL_REGEX = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  const [email, setEmail] = useState('');
  const [authNumber, setAuthNumber] = useState('');
  const [nickname, setNickname] = useState('');
  const [password, setPassword] = useState('');
  const [passwordRepeat, setPasswordRepeat] = useState('');
  const [isEmailDisabled, setIsEmailDisabled] = useState(false);
  const [isAuthNumberDisabled, setIsAuthNumberDisabled] = useState(false);
  const [mailAuthToggle, setMailAuthToggle] = useState('인증 메일 전송');
  const [isMailSent, setIsMailSent] = useState(false);
  const [termsModalOpen, setTermsModalOpen] = useState(false);
  const [privacyModalOpen, setPrivacyModalOpen] = useState(false);
  const [termsAgree, setTermsAgree] = useState(false);
  const [privacyAgree, setPrivacyAgree] = useState(false);

  const validateEmail = useMemo(() => {
    return email && !EMAIL_REGEX.test(String(email).toLowerCase());
  }, [EMAIL_REGEX, email]);

  const validateNickname = useMemo(() => {
    return nickname && (nickname.length < NICKNAME_MIN_LENGTH || nickname.length > NICKNAME_MAX_LENGTH);
  }, [nickname]);

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

  const nicknameCheck = useMemo(() => {
    if (validateNickname) {
      return InputCheck('닉네임 길이는 10 자 이하로 해주세요.');
    }
  }, [validateNickname]);

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

  const handleChangeNickname = useCallback(({ target: { value } }) => {
    setNickname(value);
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
    setNickname('');
    setPassword('');
    setPasswordRepeat('');
  }, []);

  const checkEmail = useCallback(() => {
    checkJoined(email);
  }, [email]);

  const mailAuthToggleFunction = useCallback(() => {
    if (isMailSent === false) {
      alert('인증 메일을 전송했습니다.');
      sendAuthMail(email, setIsEmailDisabled, setMailAuthToggle, setIsMailSent);
      return;
    }
    checkAuthNumber(email, authNumber, setIsAuthNumberDisabled);
  }, [email, authNumber, isMailSent]);

  const join = useCallback(() => {
    createUser(email, nickname, password, authNumber, handleReset, history);
  }, [email, nickname, password, authNumber, handleReset, history]);

  const handleSubmit = useCallback(
    (event) => {
      event.preventDefault();
      if (validateEmail || validateNickname || validatePassword || validatePasswordRepeat) {
        alert('입력값을 확인해 주세요.');
        return;
      }
      if (!termsAgree) {
        alert('우리동네캡짱 이용 약관에 동의하셔야 합니다.');
        return;
      }
      if (!privacyAgree) {
        alert('개인정보 수집 및 이용에 동의하셔야 합니다.');
        return;
      }
      join();
    },
    [validateEmail, validateNickname, validatePassword, validatePasswordRepeat, join, termsAgree, privacyAgree],
  );

  const onChangeTerms = (event) => {
    event.preventDefault();
    setTermsAgree(!termsAgree);
  };

  const onChangePrivacy = (event) => {
    event.preventDefault();
    setPrivacyAgree(!privacyAgree);
  };

  const openTermsModal = (event) => {
    event.preventDefault();
    setTermsModalOpen(true);
  };

  const openPrivacyModal = (event) => {
    event.preventDefault();
    setPrivacyModalOpen(true);
  };

  const closeModal = (event) => {
    if (event) {
      event.preventDefault();
    }
    setTermsModalOpen(false);
    setPrivacyModalOpen(false);
  };

  const onClickLogin = (event) => {
    event.preventDefault();
    history.push('/login');
  };

  return (
    <Container component='main' maxWidth='xs'>
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <ThumbUpIcon />
        </Avatar>
        <Typography component='h1' variant='h5'>
          우리동네캡짱 회원가입
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
                메일 중복 확인
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
                id='lastName'
                label='닉네임'
                name='lastName'
                autoComplete='lname'
                type='text'
                value={nickname}
                onChange={handleChangeNickname}
              />
            </Grid>
            <Grid item xs={12}>
              <Typography variant='caption' color='error'>
                {nicknameCheck}
              </Typography>
            </Grid>
            <Grid item xs={12}>
              <TextField
                variant='outlined'
                required
                fullWidth
                name='password'
                label='비밀번호'
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
                label='비밀번호 확인'
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
          <Checkbox
            color='primary'
            inputProps={{ 'aria-label': 'secondary checkbox' }}
            checked={termsAgree}
            onChange={onChangeTerms}
          />
          <Link underline='always' onClick={openTermsModal}>
            우리동네캡짱 이용약관
          </Link>
          에 동의합니다.(필수)
          <br />
          <Checkbox
            color='primary'
            inputProps={{ 'aria-label': 'secondary checkbox' }}
            checked={privacyAgree}
            onChange={onChangePrivacy}
          />
          <Link underline='always' onClick={openPrivacyModal}>
            개인정보 수집 및 이용
          </Link>
          에 동의합니다.(필수)
          <Button type='submit' fullWidth variant='contained' color='primary' className={classes.submit}>
            회원가입
          </Button>
          <Grid container justify='flex-end'>
            <Grid item>
              이미 계정이 있으신가요?&nbsp;
              <Link underline='always' onClick={onClickLogin}>
                로그인
              </Link>
              을 해주세요!
            </Grid>
          </Grid>
        </form>
      </div>
      <Box mt={5}>
        <Copyright />
      </Box>
      {termsModalOpen ? (
        <TermsModal open={termsModalOpen} closeModal={closeModal} agree={termsAgree} setAgree={setTermsAgree} />
      ) : null}
      {privacyModalOpen ? (
        <PrivacyModal open={privacyModalOpen} closeModal={closeModal} agree={privacyAgree} setAgree={setPrivacyAgree} />
      ) : null}
    </Container>
  );
};

export default JoinPage;
