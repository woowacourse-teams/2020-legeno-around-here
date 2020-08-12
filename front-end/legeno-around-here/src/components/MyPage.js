import React, { useState, useMemo } from "react";
import axios from "axios";

import TopBarBackground from "./mypage/TopBarBackground";
import OutBox from "./OutBox"
import { getAccessTokenFromCookie } from "../util/TokenUtils";
import { ProfilePhoto, Nickname, Email, TopSection, PrivacyBox, PrivacyEditBox } from "./mypage/PrivacySection";
import { AwardsSection, AwardSummary } from './mypage/AwardSection';
import GoBack from './mypage/GoBack';
import { NavSection, NavElement } from './mypage/LinksSection';

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
      <TopBarBackground>
        <GoBack linkTo="/">홈으로</GoBack>
      </TopBarBackground>
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
      <NavSection>
        <NavElement linkTo="/">수상내역</NavElement>
        <NavElement linkTo="/">작성글</NavElement>
        <NavElement linkTo="/">작성 댓글</NavElement>
      </NavSection>
    </OutBox>
  );
}

export default MyPage;
