import React, { useState, useCallback, useMemo } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import styled from 'styled-components';

import Title from '../join/Title';
import Input from '../join/Input';
import Label from '../join/Label';
import Button from '../join/Button';

const InputCheck = styled.p`
  width: 320px;
  height: 18px;
  font-size: 10px;
  color: red;
  line-height: 18px;
  margin: 0px 0px;
`;

const InputSection = styled.div`
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
`;

function JoinForm() {
  const NICKNAME_MIN_LENGTH = 1;
  const NICKNAME_MAX_LENGTH = 10;
  const PASSWORD_MIN_LENGTH = 8;
  const PASSWORD_MAX_LENGTH = 16;
  const EMAIL_REGEX = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [password, setPassword] = useState('');
  const [passwordRepeat, setPasswordRepeat] = useState('');

  const validateEmail = useMemo(() => {
    return email && !EMAIL_REGEX.test(String(email).toLowerCase());
  }, [EMAIL_REGEX, email]);

  const validateNickname = useMemo(() => {
    return (
      nickname &&
      (nickname.length < NICKNAME_MIN_LENGTH ||
        nickname.length > NICKNAME_MAX_LENGTH)
    );
  }, [nickname]);

  const validatePassword = useMemo(() => {
    return (
      password &&
      (password.length < PASSWORD_MIN_LENGTH ||
        password.length > PASSWORD_MAX_LENGTH)
    );
  }, [password]);

  const validatePasswordRepeat = useMemo(() => {
    return (
      passwordRepeat &&
      (passwordRepeat.length === 0 || password !== passwordRepeat)
    );
  }, [password, passwordRepeat]);

  const emailCheck = useMemo(() => {
    if (validateEmail) {
      return (
        <InputCheck className="alert">
          올바른 이메일 형식을 입력해주세요.
        </InputCheck>
      );
    }
    return <InputCheck className="alert"></InputCheck>;
  }, [validateEmail]);

  const nicknameCheck = useMemo(() => {
    if (validateNickname) {
      return (
        <InputCheck className="alert">
          닉네임 길이는 10 자 이하로 해주세요.
        </InputCheck>
      );
    }
    return <InputCheck className="alert"></InputCheck>;
  }, [validateNickname]);

  const passwordCheck = useMemo(() => {
    if (validatePassword) {
      return (
        <InputCheck className="alert">
          비밀번호 길이는 8 ~ 16 자로 해주세요.
        </InputCheck>
      );
    }
    return <InputCheck className="alert"></InputCheck>;
  }, [validatePassword]);

  const passwordRepeatCheck = useMemo(() => {
    if (validatePasswordRepeat) {
      return (
        <InputCheck className="alert">비밀번호가 일치하지 않습니다.</InputCheck>
      );
    }
    return <InputCheck className="alert"></InputCheck>;
  }, [validatePasswordRepeat]);

  const handleChangeEmail = useCallback(({ target: { value } }) => {
    setEmail(value);
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
    setNickname('');
    setPassword('');
    setPasswordRepeat('');
  }, []);

  const join = useCallback(() => {
    axios
      .post('http://localhost:8080/join', {
        email,
        nickname,
        password,
      })
      .then((response) => {
        alert('회원가입을 축하드립니다.');
        document.location.href = '/login';
      })
      .catch(() => {
        alert('회원가입에 실패하였습니다.');
        handleReset();
      });
  }, [email, nickname, password, handleReset]);

  const handleSubmit = useCallback(
    (event) => {
      event.preventDefault();
      if (
        validateEmail ||
        validateNickname ||
        validatePassword ||
        validatePasswordRepeat
      ) {
        alert('입력값을 확인해 주세요.');
        return;
      }
      join();
    },
    [
      validateEmail,
      validateNickname,
      validatePassword,
      validatePasswordRepeat,
      join,
    ],
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
          <Label>{emailCheck}</Label>
        </InputSection>
        <InputSection>
          <Label>닉네임</Label>
          <Input
            type="text"
            placeholder="닉네임"
            value={nickname}
            onChange={handleChangeNickname}
          />
          <Label>{nicknameCheck}</Label>
        </InputSection>
        <InputSection>
          <Label>비밀번호</Label>
          <Input
            type="password"
            placeholder="비밀번호"
            value={password}
            onChange={handleChangePassword}
          />
          <Label>{passwordCheck}</Label>
          <Input
            type="password"
            placeholder="비밀번호 확인"
            value={passwordRepeat}
            onChange={handleChangePasswordRepeat}
          />
          <Label>{passwordRepeatCheck}</Label>
        </InputSection>
        <InputSection>
          <Button type="submit">회원가입</Button>
        </InputSection>
        <Link to="/" style={{ textDecoration: 'none' }}>
          <InputSection>
            <Button type="button">홈으로</Button>
          </InputSection>
        </Link>
      </form>
    </>
  );
}

export default JoinForm;
