import React, { useState, useCallback } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import styled from 'styled-components';

import { setAccessTokenCookie } from '../../util/TokenUtils';
import Title from '../login/Title';
import InputSection from '../login/InputSection';
import Input from '../login/Input';
import Label from '../login/Label';
import Button from '../login/Button';

const ButtonSection = styled.div`
  width: 100%;
  margin-top: 50px;
  text-align: center;
  display: flex;
  flex-direction: column;
`;

function LoginForm() {
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
    axios
      .post('http://capzzang.co.kr:8080/login', {
        email,
        password,
      })
      .then(async (response) => {
        const tokenResponse = await response.data;
        setAccessTokenCookie(tokenResponse.accessToken);
        alert('로그인되었습니다.');
        document.location.href = '/mypage';
      })
      .catch(() => {
        alert('로그인에 실패하였습니다.');
        handleReset();
      });
  }, [email, password, handleReset]);

  const handleSubmit = useCallback(
    (event) => {
      event.preventDefault();
      login();
    },
    [login],
  );

  return (
    <>
      <Title>우리동네 캡짱</Title>
      <form onSubmit={handleSubmit}>
        <InputSection>
          <Label>아이디 (E-mail)</Label>
          <Input
            type="email"
            placeholder="이메일"
            value={email}
            onChange={handleChangeEmail}
          />
        </InputSection>
        <InputSection>
          <Label>비밀번호</Label>
          <Input
            type="password"
            placeholder="비밀번호"
            value={password}
            onChange={handleChangePassword}
          />
        </InputSection>
        <ButtonSection>
          <Button type="submit">로그인</Button>
          <Link to="/join" style={{ textDecoration: 'none' }}>
            <Button type="button">회원가입</Button>
          </Link>
          <Link to="/" style={{ textDecoration: 'none' }}>
            <Button type="button">홈으로</Button>
          </Link>
        </ButtonSection>
      </form>
    </>
  );
}

export default LoginForm;
