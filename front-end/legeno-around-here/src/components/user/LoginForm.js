import React, { useState, useCallback } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { setAccessTokenCookie } from "../../util/TokenUtils";

import Input from "./Input";

function LoginForm() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleChangeEmail = useCallback(({ target: { value } }) => {
    setEmail(value);
  }, []);

  const handleChangePassword = useCallback(({ target: { value } }) => {
    setPassword(value);
  }, []);

  const handleReset = useCallback(() => {
    setEmail("");
    setPassword("");
  }, []);

  const login = useCallback(() => {
    axios
      .post("http://13.209.62.31:8080/login", {
        email,
        password,
      })
      .then(async (response) => {
        const tokenResponse = await response.data;
        setAccessTokenCookie(tokenResponse.accessToken);
        alert("로그인되었습니다.");
        document.location.href = "/mypage";
      })
      .catch(() => {
        alert("로그인에 실패하였습니다.");
        handleReset();
      });
  }, [email, password, handleReset]);

  const handleSubmit = useCallback(
    (event) => {
      event.preventDefault();
      login();
    },
    [login]
  );

  return (
    <form onSubmit={handleSubmit}>
      <Input
        type="email"
        placeholder="이메일"
        value={email}
        onChange={handleChangeEmail}
      />
      <Input
        type="password"
        placeholder="비밀번호"
        value={password}
        onChange={handleChangePassword}
      />
      <button type="submit">로그인</button>
      <Link to="/">
        <button type="button">홈으로</button>
      </Link>
    </form>
  );
}

export default LoginForm;
