import React, { useEffect, useState } from 'react';

import Avatar from '@material-ui/core/Avatar';
import Button from '@material-ui/core/Button';
import CssBaseline from '@material-ui/core/CssBaseline';
import TextField from '@material-ui/core/TextField';
import Box from '@material-ui/core/Box';
import ThumbUpIcon from '@material-ui/icons/ThumbUp';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import { findPassword } from '../../api/API';
import { removeAccessTokenCookie } from '../../../util/TokenUtils';
import Grid from '@material-ui/core/Grid';
import LinkWithoutStyle from '../../../util/LinkWithoutStyle';

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

const FindPasswordPage = ({ history }) => {
  const classes = useStyles();
  const [nickname, setNickname] = useState('');
  const [email, setEmail] = useState('');

  const handleChangeNickname = ({ target: { value } }) => {
    setNickname(value);
  };

  const handleChangeEmail = ({ target: { value } }) => {
    setEmail(value);
  };

  const handleReset = () => {
    setNickname('');
    setEmail('');
  };

  const submit = (event) => {
    event.preventDefault();
    findPassword(nickname, email, handleReset, history);
  };

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
          우리동네캡짱 비밀번호 찾기
        </Typography>
        <form onSubmit={submit} className={classes.form}>
          <TextField
            variant='outlined'
            margin='normal'
            required
            fullWidth
            name='nickname'
            label='닉네임'
            type='text'
            id='nickname'
            autoComplete='current-password'
            value={nickname}
            onChange={handleChangeNickname}
          />
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
          <Button type='submit' fullWidth variant='contained' color='primary' className={classes.submit}>
            재설정 메일 발송
          </Button>
          <Grid container direction='row-reverse' justify='space-between'>
            <Grid item>
              <LinkWithoutStyle to='/login' variant='body2'>
                {'뒤로 가기'}
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
};

export default FindPasswordPage;
