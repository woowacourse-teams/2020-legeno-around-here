import React, { useEffect } from 'react';

import Avatar from '@material-ui/core/Avatar';
import CssBaseline from '@material-ui/core/CssBaseline';
import Box from '@material-ui/core/Box';
import ThumbUpIcon from '@material-ui/icons/ThumbUp';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';
import { getAccessTokenFromCookie } from '../../util/TokenUtils';

const Copyright = () => {
  return (
    <Typography variant='body2' color='textSecondary' align='center'>
      {'Copyright © Ittabi 2020.'}
    </Typography>
  );
};

const useStyles = makeStyles((theme) => ({
  logo: {
    width: '300px',
    height: '300px',
    padding: '0px 0px',
    margin: '0px 0px',
    position: 'absolute',
    left: '50%',
    top: '50%',
    marginLeft: '-150px',
    marginTop: '-150px',
  },
  paper: {
    display: 'flex',
    flexDirection: 'column',
    alignItems: 'center',
  },
  avatar: {
    margin: theme.spacing(1),
    backgroundColor: theme.palette.secondary.main,
  },
}));

function InitPage() {
  const classes = useStyles();

  const sleep = (milliseconds) => {
    return new Promise((resolve) => setTimeout(resolve, milliseconds));
  };

  useEffect(() => {
    const accessToken = getAccessTokenFromCookie();
    sleep(2000).then(() => {
      if (accessToken === '') {
        document.location.href = '/login';
        return;
      }
      document.location.href = '/home';
    });
  }, []);

  return (
    <div className={classes.logo}>
      <Container component='main' maxWidth='xs'>
        <CssBaseline />
        <div className={classes.paper}>
          <Avatar className={classes.avatar}>
            <ThumbUpIcon />
          </Avatar>
          <Typography component='h1' variant='h5'>
            우리동네캡짱
          </Typography>
        </div>
        <Box mt={8}>
          <Copyright />
        </Box>
      </Container>
    </div>
  );
}

export default InitPage;
