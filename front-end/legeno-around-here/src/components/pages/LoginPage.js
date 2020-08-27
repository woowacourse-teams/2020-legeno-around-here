import React, { useCallback, useState, useEffect } from 'react';
import { loginUser } from '../api/API';

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
import { removeAccessTokenCookie } from '../../util/TokenUtils';
import LinkWithoutStyle from '../../util/LinkWithoutStyle'

const Copyright = () => {
  return (
    <Typography variant='body2' color='textSecondary' align='center'>
      {'Copyright © Ittabi 2020.'}
    </Typography>
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
    marginTop: theme.spacing(1),
  },
  submit: {
    margin: theme.spacing(3, 0, 2),
  },
}));

function LoginForm() {
  const classes = useStyles();
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

  const handleChangeEmail = useCallback(({ target: { value } }) => {
    setEmail(value);
  }, []);

  const handleChangePassword = useCallback(({ target: { value } }) => {
    setPassword(value);
  }, []);

  const handleReset = useCallback(() => {
    setEmail('');
    setPassword('');
  }, []);

  const login = useCallback(() => {
    loginUser(email, password, handleReset);
  }, [email, password, handleReset]);

  const handleSubmit = useCallback(
    (event) => {
      event.preventDefault();
      login();
    },
    [login],
  );

  useEffect(() => {
    removeAccessTokenCookie();
  }, []);

  return (
    <Container component='main' maxWidth='xs'>
      <CssBaseline />
      <div className={classes.paper}>
        <Avatar className={classes.avatar}>
          <ThumbUpIcon />
        </Avatar>
        <Typography component='h1' variant='h5'>
          우리동네캡짱 로그인
        </Typography>
        <form onSubmit={handleSubmit} className={classes.form} noValidate>
          <TextField
            variant='outlined'
            margin='normal'
            required
            fullWidth
            id='email'
            label='이메일'
            name='email'
            autoComplete='email'
            autoFocus
            type='email'
            value={email}
            onChange={handleChangeEmail}
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
            value={password}
            onChange={handleChangePassword}
          />
          <Button type='submit' fullWidth variant='contained' color='primary' className={classes.submit}>
            로그인
          </Button>
          <Grid container>
            <Grid item>
              <LinkWithoutStyle to='/join' variant='body2'>
                {'처음이신가요? 회원가입을 해주세요!'}
              </LinkWithoutStyle>
            </Grid>
          </Grid>
        </form>
      </div>
      <Box mt={8}>
        <Copyright />
      </Box>
    </Container>
  );
}

export default LoginForm;
