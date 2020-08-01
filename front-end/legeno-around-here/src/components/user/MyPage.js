import React, { useState, useMemo } from "react";
import axios from "axios";
import { Link } from "react-router-dom";
import { getAccessTokenFromCookie } from "../../util/TokenUtils";

function MyPage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState("");
  const [nickname, setNickname] = useState("");

  useMemo(() => {
    const config = {
      headers: {
        "Content-Type": "application/x-www-form-urlencoded",
        "X-Auth-Token": accessToken,
      },
    };
    axios
      .get("http://13.209.62.31:8080/users/myinfo", config)
      .then(async (response) => {
        const userResponse = await response.data;
        setEmail(userResponse.email);
        setNickname(userResponse.nickname);
      })
      .catch((error) => {
        alert(`회원정보를 가져올 수 없습니다.${error}`);
      });
  }, [accessToken]);

  return (
    <div>
      <p>메일: {email}</p>
      <p>닉네임: {nickname}</p>
      <button type="submit">정보 수정</button>
      <Link to="/">
        <button type="button">홈으로</button>
      </Link>
    </div>
  );
}

export default MyPage;
