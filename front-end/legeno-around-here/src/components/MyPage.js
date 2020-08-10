import React, { useState, useMemo } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

import OutBox from "./OutBox"
import Button from "./Button"
import { getAccessTokenFromCookie } from "../util/TokenUtils";

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
      .get("http://localhost:8080/users/myinfo", config)
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
    <OutBox>
      <div>
        <p>메일: {email}</p>
        <p>닉네임: {nickname}</p>
        <Button type="submit">정보 수정</Button>
        <Link to="/">
          <Button type="button">홈으로</Button>
        </Link>
      </div>
    </OutBox>
  );
}

export default MyPage;
