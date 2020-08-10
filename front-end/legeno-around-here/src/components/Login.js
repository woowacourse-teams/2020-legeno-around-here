import React, { useState, useCallback } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { setAccessTokenCookie } from "../util/TokenUtils";

import OutBox from "./OutBox"
import Input from "./Input";
import Button from "./Button"

function Login() {
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
      .post("http://localhost:8080/login", {
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
    <OutBox>
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
      <Button type="submit">로그인</Button>
      <Link to="/">
        <Button type="button">홈으로</Button>
      </Link>
    </form>
  </OutBox>
  );
}

export default Login;
