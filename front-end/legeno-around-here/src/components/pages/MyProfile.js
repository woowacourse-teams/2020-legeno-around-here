import React, {useMemo, useState} from 'react';

import TopBar from './myProfile/myProfileTopBar'
import Bottom from '../Bottom';
import {PROFILE} from '../../constants/BottomItems';
import {findMyInfo} from '../api/API';
import Loading from '../Loading';
import {getAccessTokenFromCookie} from '../../util/TokenUtils';
import {
  Email,
  Nickname,
  PrivacyBox,
  PrivacyEditBox,
  ProfilePhoto,
  TopSection,
} from '../myProfile/PrivacySection';
import {AwardsSection, AwardSummary} from '../myProfile/AwardSection';
import {NavElement, NavSection} from '../myProfile/LinksSection';

function MyProfile() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [loading, setLoading] = useState(false);

  useMemo(() => {
    setLoading(true);
    findMyInfo({
      accessToken: accessToken,
      setEmailState: setEmail,
      setNicknameState: setNickname,
    });
    setLoading(false);
  }, [accessToken]);

  if (loading) {
    return <Loading />;
  }

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
