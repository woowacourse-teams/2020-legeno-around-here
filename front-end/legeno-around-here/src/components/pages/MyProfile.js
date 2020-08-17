import React, { useState, useMemo } from 'react';

import TopBar from '../myProfile/TopBar';
import Bottom from '../Bottom';
import { PROFILE } from '../../constants/BottomItems';
import { findMyInfo } from '../api/API';
import { getAccessTokenFromCookie } from '../../util/TokenUtils';
import {
  ProfilePhoto,
  Nickname,
  Email,
  TopSection,
  PrivacyBox,
  PrivacyEditBox,
} from '../myProfile/PrivacySection';
import { AwardsSection, AwardSummary } from '../myProfile/AwardSection';
import { NavSection, NavElement } from '../myProfile/LinksSection';

function MyProfile() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');

  useMemo(() => {
    findMyInfo({
      accessToken: accessToken,
      setEmailState: setEmail,
      setNicknameState: setNickname,
    });
  }, [accessToken]);

  return (
    <>
      <TopBar backButtonLink="/"></TopBar>
      <TopSection>
        <ProfilePhoto></ProfilePhoto>
        <PrivacyBox>
          <Nickname>{nickname}</Nickname>
          <Email>{email}</Email>
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
      <Bottom selected={PROFILE}></Bottom>
    </>
  );
}

export default MyProfile;
