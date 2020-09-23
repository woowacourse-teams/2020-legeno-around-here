import React from 'react';
import { GITHUB_AUTH_URL, GOOGLE_AUTH_URL } from '../../constants/AuthConstants';
import googleLogo from '../../images/google-logo.png';
import githubLogo from '../../images/github-logo.png';

const SocialLogin = () => {
  return (
    <div className='social-login'>
      <a className='btn btn-block social-btn google' href={GOOGLE_AUTH_URL}>
        <img src={googleLogo} alt='Google' /> Log in with Google
      </a>
      <a className='btn btn-block social-btn github' href={GITHUB_AUTH_URL}>
        <img src={githubLogo} alt='Github' /> Log in with Github
      </a>
    </div>
  );
};

export default SocialLogin;
