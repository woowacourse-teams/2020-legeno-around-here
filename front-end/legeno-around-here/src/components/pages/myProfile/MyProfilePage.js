import React, { useMemo, useState, useEffect } from 'react';

import TopBar from './myProfileTopBar';
import Bottom from '../../Bottom';
import { PROFILE } from '../../../constants/BottomItems';
import { findMyInfo, findAllMySector } from '../../api/API';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { Divider, Typography } from '@material-ui/core';
import {
  Email,
  Nickname,
  PrivacyBox,
  PrivacyEditBox,
  ProfilePhoto,
  TopSection,
} from '../../myProfile/PrivacySection';
import { AwardsSection, AwardSummary } from '../../myProfile/AwardSection';
import { NavElement, NavSection } from '../../myProfile/LinksSection';
import MySectors from './MySectors';

function MyProfilePage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [profilePhotoUrl, setProfilePhotoUrl] = useState(null);
  const [mySectors, setMySectors] = useState([]);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    const loadMySectors = async () => {
      setLoading(true);
      const sectors = await findAllMySector(accessToken);
      setMySectors(sectors);
      setLoading(false);
    };
    loadMySectors();
  }, [accessToken]);

  useMemo(() => {
    setLoading(true);
    findMyInfo({
      accessToken: accessToken,
      setEmail: setEmail,
      setNickname: setNickname,
      setProfilePhotoUrl: setProfilePhotoUrl,
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
        <ProfilePhoto photoUrl={profilePhotoUrl}></ProfilePhoto>
        <PrivacyBox>
          <Typography component="h1" variant="h5">
            <Nickname>{nickname}</Nickname>
          </Typography>
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
        <Typography>현재 신청중인 부문</Typography>
        <Divider />
        {mySectors ? (
          <MySectors mySectors={mySectors} />
        ) : (
          <Typography>현재 신청중인 부문이 없습니다!</Typography>
        )}
      </NavSection>
      <Bottom selected={PROFILE}></Bottom>
    </>
  );
}

export default MyProfilePage;
