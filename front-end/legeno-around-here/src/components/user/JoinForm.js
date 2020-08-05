import React, { useState, useCallback, useMemo } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

import Input from "./Input";

function JoinForm() {
  const NICKNAME_MIN_LENGTH = 1;
  const NICKNAME_MAX_LENGTH = 10;
  const PASSWORD_MIN_LENGTH = 8;
  const PASSWORD_MAX_LENGTH = 16;
  const EMAIL_REGEX = /^(([^<>()[\]\\.,;:\s@"]+(\.[^<>()[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\])|(([a-zA-Z\-0-9]+\.)+[a-zA-Z]{2,}))$/;

  const [email, setEmail] = useState("");
  const [nickname, setNickname] = useState("");
  const [password, setPassword] = useState("");
  const [passwordRepeat, setPasswordRepeat] = useState("");

  const validateEmail = useMemo(() => {
    return !EMAIL_REGEX.test(String(email).toLowerCase());
  }, [EMAIL_REGEX, email]);

  const validateNickname = useMemo(() => {
    return (
      nickname.length < NICKNAME_MIN_LENGTH ||
      nickname.length > NICKNAME_MAX_LENGTH
    );
  }, [nickname]);

  const validatePassword = useMemo(() => {
    return (
      password.length < PASSWORD_MIN_LENGTH ||
      password.length > PASSWORD_MAX_LENGTH
    );
  }, [password]);

  const validatePasswordRepeat = useMemo(() => {
    return passwordRepeat.length === 0 || password !== passwordRepeat;
  }, [password, passwordRepeat]);

  const emailCheck = useMemo(() => {
    return (
      validateEmail && (
        <p className="alert">올바른 이메일 형식을 입력해주세요.</p>
      )
    );
  }, [validateEmail]);

  const nicknameCheck = useMemo(() => {
    return (
      validateNickname && (
        <p className="alert">닉네임 길이는 1 ~ 10 자로 해주세요.</p>
      )
    );
  }, [validateNickname]);

  const passwordCheck = useMemo(() => {
    return (
      validatePassword && (
        <p className="alert">비밀번호 길이는 8 ~ 16 자로 해주세요.</p>
      )
    );
  }, [validatePassword]);

  const passwordRepeatCheck = useMemo(() => {
    return (
      validatePasswordRepeat && (
        <p className="alert">비밀번호가 일치하지 않습니다.</p>
      )
    );
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
    setEmail("");
    setNickname("");
    setPassword("");
    setPasswordRepeat("");
  }, []);

  const join = useCallback(() => {
    axios
      .post("http://localhost:8080/join", {
        email,
        nickname,
        password,
      })
      .then((response) => {
        alert("회원가입을 축하드립니다.");
        document.location.href = "/login";
      })
      .catch(() => {
        alert("회원가입에 실패하였습니다.");
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
        alert("입력값을 확인해 주세요.");
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
    ]
  );

  return (
    <form onSubmit={handleSubmit}>
      <Input
        type="email"
        placeholder="이메일"
        value={email}
        onChange={handleChangeEmail}
        check={emailCheck}
      />
      <Input
        type="text"
        placeholder="닉네임"
        value={nickname}
        onChange={handleChangeNickname}
        check={nicknameCheck}
      />
      <Input
        type="password"
        placeholder="비밀번호"
        value={password}
        onChange={handleChangePassword}
        check={passwordCheck}
      />
      <Input
        type="password"
        placeholder="비밀번호 확인"
        value={passwordRepeat}
        onChange={handleChangePasswordRepeat}
        check={passwordRepeatCheck}
      />
      <button type="submit">회원가입</button>
      <Link to="/">
        <button type="button">홈으로</button>
      </Link>
    </form>
  );
}

export default JoinForm;
