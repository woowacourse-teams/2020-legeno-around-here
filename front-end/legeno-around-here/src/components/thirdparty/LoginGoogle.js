import React, { useState } from 'react';
import GoogleLogin from 'react-google-login';
import Container from '@material-ui/core/Container';

const LoginGoogle = () => {
  const [account, setAccount] = useState({
    email: null,
    nickname: null,
  });

  const responseGoogle = (response) => {
    console.log(response);
    const profile = response.profileObj;
    setAccount({
      email: profile.email,
      nickname: profile.givenName,
    });
  };

  const responseFail = () => {
    alert('Google 로그인에 실패하였습니다.');
  };

  return (
    <Container>
      <GoogleLogin
        clientId={process.env.REACT_APP_GOOGLE}
        buttonText='Google Login'
        onSuccess={responseGoogle}
        onFailure={responseFail}
        cookiePolicy={'single_host_origin'}
      />
    </Container>
  );
};
export default LoginGoogle;
