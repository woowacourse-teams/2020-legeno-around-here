import React, { useEffect, useState } from 'react';
import { produce } from 'immer';
import { loginAdmin } from '../api/LoginApi';

import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Box from '@material-ui/core/Box';
import LockOutlinedIcon from '@material-ui/icons/LockOutlined';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import { isEmail } from '../util/Validation';
import { useCookies } from 'react-cookie';
import Copyright from '../components/Copyright';

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
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

const LoginPage = ({ history }) => {
  const classes = useStyles();

  const [form, setForm] = useState({
    email: '',
    password: '',
  });

  const [cookies, setCookie] = useCookies(['accessToken']);

  useEffect(() => {
    if (cookies.accessToken && cookies.accessToken !== 'undefined') {
      history.push('/');
    }
  }, [history, cookies.accessToken]);

  const onChange = (event) => {
    const { name, value } = event.target;
    setForm(
      produce(form, (draft) => {
        draft[name] = value;
      }),
    );
  };

  const onSubmit = (event) => {
    event.preventDefault();
    const { email, password } = form;
    if ([email, password].includes('')) {
      alert('빈 칸을 모두 입력하세요.');
      return;
    }
    if (!isEmail(email)) {
      alert('올바른 이메일 형식이 아닙니다.');
      return;
    }
    login();
  };

  const login = () => {
    loginAdmin(form, initPassword, setCookie, history);
  };

  const initPassword = () => {
    setForm(
      produce(form, (draft) => {
        draft['password'] = '';
      }),
    );
  };

  return (
    <Container component='main' maxWidth='xs'>
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <LockOutlinedIcon />
        </Avatar>
        <Typography component='h1' variant='h5'>
          우리동네캡짱 관리도구
        </Typography>
        <form className={classes.form} onSubmit={onSubmit} noValidate>
          <TextField
            variant='outlined'
            margin='normal'
            required
            fullWidth
            id='email'
            label='관리자 이메일'
            name='email'
            autoComplete='email'
            autoFocus
            onChange={onChange}
            value={form.email}
          />
          <TextField
            variant='outlined'
            margin='normal'
            required
            fullWidth
            name='password'
            label='비밀번호'
            type='password'
            id='password'
            autoComplete='current-password'
            onChange={onChange}
            value={form.password}
          />
          <Button type='submit' fullWidth variant='contained' color='primary' className={classes.submit}>
            로그인
          </Button>
        </form>
      </div>
      <Box mt={8}>
        <Copyright />
      </Box>
    </Container>
  );
};

export default LoginPage;
