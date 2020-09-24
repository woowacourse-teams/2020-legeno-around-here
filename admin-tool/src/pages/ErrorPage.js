import React from 'react';
import CssBaseline from '@material-ui/core/CssBaseline';
import Typography from '@material-ui/core/Typography';
import { makeStyles } from '@material-ui/core/styles';
import Container from '@material-ui/core/Container';

import Copyright from '../components/Copyright';

const useStyles = makeStyles((theme) => ({
  root: {
    display: 'flex',
    flexDirection: 'column',
    minHeight: '100vh',
  },
  main: {
    marginTop: theme.spacing(8),
    marginBottom: theme.spacing(2),
  },
  footer: {
    padding: theme.spacing(3, 2),
    marginTop: 'auto',
    backgroundColor: theme.palette.type === 'light' ? theme.palette.grey[200] : theme.palette.grey[800],
  },
}));

const ErrorPage = ({ location }) => {
  const classes = useStyles();

  return (
    <div className={classes.root}>
      <CssBaseline />
      <Container component='main' className={classes.main} maxWidth='sm'>
        <Typography variant='h4' component='h2' gutterBottom>
          {`${location.pathname}`}
        </Typography>
        <Typography variant='h4' component='h1' gutterBottom>
          {'페이지를 찾을 수 없습니다.'}
        </Typography>
        <br />
        <Typography variant='h4' component='h1' gutterBottom>
          {'주소를 다시 확인 하신 후,'}
        </Typography>
        <Typography variant='h4' component='h2' gutterBottom>
          {'다른 페이지로 이동해주시기 바랍니다.'}
        </Typography>
      </Container>
      <footer className={classes.footer}>
        <Container maxWidth='sm'>
          <Copyright />
        </Container>
      </footer>
    </div>
  );
};

export default ErrorPage;
