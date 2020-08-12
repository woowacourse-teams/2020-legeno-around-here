import React, { useState, useCallback } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import styled from "styled-components";

import { setAccessTokenCookie } from "../util/TokenUtils";
import OutBox from "./OutBox";
import Input from "./Input";
import Label from "./login/Label";

const HeaderStyle = styled.div`
  width: 340px;
  height: 36px;
  font-family: NotoSansCJKkr;
  font-size: 24px;
  font-weight: bold;
  font-stretch: normal;
  font-style: normal;
  line-height: normal;
  letter-spacing: normal;
  color: #222222;
`;

const ButtonStyle = styled.button`
  width: 320px;
  height: 40px;
  font-size: 16px;
  font-weight: bold;
  line-height: 40px;
  background-color: #bcbcbc;
  border: 0;
  outline: 0;
  margin: 5px auto;
`;

const WrapperStyle = styled.div`
  display: flex;
  align-items: center;
  justify-content: center;
`;

const ButtonSection = styled.div`
  margin-top: 50px;
`;

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
      <WrapperStyle>
        <HeaderStyle>우리동네 캡짱</HeaderStyle>
      </WrapperStyle>
      <form onSubmit={handleSubmit}>
        <WrapperStyle>
          <Label>아이디 (E-mail)</Label>
        </WrapperStyle>
        <Input
          type="email"
          placeholder="이메일"
          value={email}
          onChange={handleChangeEmail}
        />
        <WrapperStyle>
          <Label>비밀번호</Label>
        </WrapperStyle>
        <Input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={handleChangePassword}
        />
        <ButtonSection>
          <WrapperStyle>
            <ButtonStyle type="submit">로그인</ButtonStyle>
          </WrapperStyle>
          <Link to="/join" style={{ textDecoration: "none" }}>
            <WrapperStyle>
              <ButtonStyle type="button">회원가입</ButtonStyle>
            </WrapperStyle>
          </Link>
          <Link to="/" style={{ textDecoration: "none" }}>
            <WrapperStyle>
              <ButtonStyle type="button">홈으로</ButtonStyle>
            </WrapperStyle>
          </Link>
        </ButtonSection>
      </form>
    </OutBox>
  );
}

export default LoginForm;
