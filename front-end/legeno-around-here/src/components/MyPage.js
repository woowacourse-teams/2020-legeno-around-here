import React, { useState, useMemo } from "react";
import axios from "axios";
import { Link } from "react-router-dom";

import OutBox from "./OutBox"
import Button from "./Button"
import { getAccessTokenFromCookie } from "../util/TokenUtils";
import { ProfilePhoto, Nickname, Email, TopSection, PrivacyBox, PrivacyEditBox } from "./mypage/TopSection";
import { AwardsSection, AwardSummary } from './mypage/AwardSection';

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
        document.location.href = "/";
      });
  }, [accessToken]);

  return (
    <OutBox>
      <TopSection>
        <ProfilePhoto></ProfilePhoto>
        <PrivacyBox>
          <Nickname>{ nickname }</Nickname>
          <Email>{ email }</Email>
        </PrivacyBox>
        <PrivacyEditBox>수정</PrivacyEditBox>
      </TopSection>
      <AwardsSection>
        <AwardSummary awardName="TOP3" awardCount={1}></AwardSummary>
        <AwardSummary awardName="TOP10" awardCount={0}></AwardSummary>
        <AwardSummary awardName="TOP50" awardCount={12}></AwardSummary>
      </AwardsSection>
      <Link to="/">
        <Button type="button">홈으로</Button>
      </Link>
    </OutBox>
  );
}

export default MyPage;
