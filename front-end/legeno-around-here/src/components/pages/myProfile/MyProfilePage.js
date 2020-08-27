import React, { useCallback, useMemo, useState } from 'react';

import TopBar from './myProfileTopBar';
import Bottom from '../../Bottom';
import { PROFILE } from '../../../constants/BottomItems';
import { findMyInfo } from '../../api/API';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { Typography } from '@material-ui/core';
import {
  Email,
  Nickname,
  PrivacyBox,
  PrivacyEditBox,
  PrivacyRightBox,
  PrivacySignOutBox,
  ProfilePhoto,
  TopSection,
} from '../../myProfile/PrivacySection';
import { AwardsSection, AwardSummary } from '../../myProfile/AwardSection';
import { NavElement, NavSection } from '../../myProfile/LinksSection';
import { DEFAULT_IMAGE_URL } from '../myProfileEdit/MyProfileEditPage';
import MySectorSection from './MySectorSection';
import BottomBlank from '../../BottomBlank';

function MyProfilePage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [profilePhotoUrl, setProfilePhotoUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [awardsCount, setAwardsCount] = useState(null);

  useMemo(() => {
    setLoading(true);
    findMyInfo(accessToken).then((userResponse) => {
      setEmail(userResponse.email);
      setNickname(userResponse.nickname);
      setProfilePhotoUrl(userResponse.image ? userResponse.image.url : DEFAULT_IMAGE_URL);
      setAwardsCount(userResponse.awardsCount);
    });
    setLoading(false);
  }, [accessToken]);

  const logout = useCallback(() => {
    alert('로그아웃 되었습니다.');
    document.location.href = '/login';
  }, []);

  if (loading) {
    return <Loading />;
  }

  return (
    <>
      <TopBar />
      <TopSection>
        <ProfilePhoto photoUrl={profilePhotoUrl} />
        <PrivacyBox>
          <Typography component='h1' variant='h5'>
            <Nickname>{nickname}</Nickname>
          </Typography>
          <Email>{email}</Email>
        </PrivacyBox>
        <PrivacyRightBox>
          <PrivacyEditBox to='/myProfileEdit'>수정</PrivacyEditBox>
          <PrivacySignOutBox onClick={logout}>로그아웃</PrivacySignOutBox>
        </PrivacyRightBox>
      </TopSection>
      <AwardsSection>
        <AwardSummary awardName='TOP1' awardsCount={awardsCount !== null ? awardsCount.topOne : 0} />
        <AwardSummary awardName='TOP3' awardsCount={awardsCount !== null ? awardsCount.topThree : 0} />
        <AwardSummary awardName='부문 수상' awardsCount={awardsCount !== null ? awardsCount.sector : 0} />
      </AwardsSection>
      <NavSection>
        <NavElement linkTo='/home'>수상내역</NavElement>
        <NavElement linkTo='/my-posts'>작성글</NavElement>
        <NavElement linkTo='/home'>작성 댓글</NavElement>
        <MySectorSection />
      </NavSection>
      <BottomBlank />
      <Bottom selected={PROFILE} />
    </>
  );
}

export default MyProfilePage;
