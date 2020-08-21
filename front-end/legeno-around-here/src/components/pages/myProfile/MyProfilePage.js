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
import { DEFAULT_IMAGE_URL } from '../myProfileEdit/MyProfileEditPage';

function MyProfilePage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [mySectors, setMySectors] = useState([]);
  const [profilePhotoUrl, setProfilePhotoUrl] = useState('');
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
    }).then((userResponse) => {
      setEmail(userResponse.email);
      setNickname(userResponse.nickname);
      setProfilePhotoUrl(
        userResponse.image ? userResponse.image.url : DEFAULT_IMAGE_URL,
      );
    });
    setLoading(false);
  }, [accessToken]);

  if (loading) {
    return <Loading />;
  }

  return (
    <>
      <TopBar backButtonLink="/" />
      <TopSection>
        <ProfilePhoto photoUrl={profilePhotoUrl} />
        <PrivacyBox>
          <Typography component="h1" variant="h5">
            <Nickname>{nickname}</Nickname>
          </Typography>
          <Email>{email}</Email>
        </PrivacyBox>
        <PrivacyEditBox to="/myProfileEdit">수정</PrivacyEditBox>
      </TopSection>
      <AwardsSection>
        <AwardSummary awardName="TOP3" awardCount={1} />
        <AwardSummary awardName="TOP10" awardCount={0} />
        <AwardSummary awardName="TOP50" awardCount={12} />
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
      <Bottom selected={PROFILE} />
    </>
  );
}

export default MyProfilePage;
