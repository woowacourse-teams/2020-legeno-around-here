import React from 'react';
import { GITHUB_AUTH_URL, GOOGLE_AUTH_URL } from '../../constants/AuthConstants';
import googleLogo from '../../images/google-logo.png';
import githubLogo from '../../images/github-logo.png';
import Button from '@material-ui/core/Button';
import { makeStyles } from '@material-ui/core/styles';

const useStyles = makeStyles(() => ({
  icon: {
    height: '22px',
    width: '22px',
  },
}));

const SocialLogin = () => {
  const classes = useStyles();

  const onClickGoogleAuth = (event) => {
    event.preventDefault();
    window.location.href = GOOGLE_AUTH_URL;
  };

  const onClickGithubAuth = (event) => {
    event.preventDefault();
    window.location.href = GITHUB_AUTH_URL;
  };

  return (
    <div className='social-login'>
      <br />
      <div>
        <Button variant='outlined' fullWidth onClick={onClickGoogleAuth}>
          <img className={classes.icon} src={googleLogo} alt='Google' />
          &emsp;Google 계정으로 함께하기
        </Button>
      </div>
      <br />
      <div>
        <Button variant='outlined' fullWidth onClick={onClickGithubAuth}>
          <img className={classes.icon} src={githubLogo} alt='Github' />
          &emsp;Github 계정으로 함께하기
        </Button>
      </div>
    </div>
  );
};

export default SocialLogin;
