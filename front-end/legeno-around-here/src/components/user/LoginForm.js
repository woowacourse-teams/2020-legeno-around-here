import React, { useState, useCallback } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { setAccessTokenCookie } from "../../util/TokenUtils";
import styled from "styled-components";

import Input from "./Input";

const FrameStyle = styled.div`
  width: 360px;
  height: 640px;
  margin: 0 auto;
  border: 1px solid #bcbcbc;
`;

const FontStyle = styled.div`
  width: 340px;
  height: 18px;
  font-family: NotoSansCJKkr;
  font-size: 12px;
  font-weight: 500;
  font-stretch: normal;
  font-style: normal;
  line-height: normal;
  letter-spacing: normal;
  color: #3f3f3f;
  font-weight: bold;
`;

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

const CheckStyle = styled.p`
  width: 320px;
  height: 18px;
  font-size: 10px;
  color: red;
  line-height: 18px;
  margin: 0px 0px;
  margin: 0 auto;
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
    <FrameStyle>
      <WrapperStyle>
        <HeaderStyle>우리동네 캡짱</HeaderStyle>
      </WrapperStyle>
      <form onSubmit={handleSubmit}>
        <WrapperStyle>
          <FontStyle>아이디 (E-mail)</FontStyle>
        </WrapperStyle>
        <Input
          type="email"
          placeholder="이메일"
          value={email}
          onChange={handleChangeEmail}
        />
        <WrapperStyle>
          <FontStyle>비밀번호</FontStyle>
        </WrapperStyle>
        <Input
          type="password"
          placeholder="비밀번호"
          value={password}
          onChange={handleChangePassword}
        />
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
        <br></br>
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
      </form>
    </FrameStyle>
  );
}

export default LoginForm;
