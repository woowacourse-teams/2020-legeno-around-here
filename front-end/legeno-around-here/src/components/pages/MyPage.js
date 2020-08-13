import React, { useState, useMemo } from 'react';

import TopBar from '../mypage/TopBar';
import Bottom from '../Bottom';
import { PROFILE } from '../../constants/BottomItems';
import { findMyInto } from '../mypage/MyInfoFinder';
import OutBox from '../OutBox';
import { getAccessTokenFromCookie } from '../../util/TokenUtils';
import {
  ProfilePhoto,
  Nickname,
  Email,
  TopSection,
  PrivacyBox,
  PrivacyEditBox,
} from '../mypage/PrivacySection';
import { AwardsSection, AwardSummary } from '../mypage/AwardSection';
import { NavSection, NavElement } from '../mypage/LinksSection';

function MyPage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');

  useMemo(() => {
    findMyInto({
      accessToken: accessToken,
      setEmailState: setEmail,
      setNicknameState: setNickname,
    });
  }, [accessToken]);

  return (
    <>
      <OutBox>
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
          <NavElement linkTo="/">그외</NavElement>
          <NavElement linkTo="/">뀨?</NavElement>
          <NavElement linkTo="/">뀨뀨?</NavElement>
        </NavSection>
      </OutBox>
      <Bottom selected={PROFILE}></Bottom>
    </>
  );
}

export default MyPage;
