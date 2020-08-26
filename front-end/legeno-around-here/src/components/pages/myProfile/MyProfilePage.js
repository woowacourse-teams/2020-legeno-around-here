import React, { useCallback, useMemo, useState } from 'react';

import TopBar from './myProfileTopBar';
import Bottom from '../../Bottom';
import { PROFILE } from '../../../constants/BottomItems';
import { findMyInfo } from '../../api/API';
import Loading from '../../Loading';
import { getAccessTokenFromCookie, removeAccessTokenCookie } from '../../../util/TokenUtils';
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
import BottomBlank from '../../BottomBlank';
import MySectorSection from './MySectorSection';

function MyProfilePage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [profilePhotoUrl, setProfilePhotoUrl] = useState('');
  const [loading, setLoading] = useState(false);

  useMemo(() => {
    setLoading(true);
    findMyInfo(accessToken).then((userResponse) => {
      setEmail(userResponse.email);
      setNickname(userResponse.nickname);
      setProfilePhotoUrl(userResponse.image ? userResponse.image.url : DEFAULT_IMAGE_URL);
    });
    setLoading(false);
  }, [accessToken]);

  const logout = useCallback(() => {
    removeAccessTokenCookie();
    alert('로그아웃 되었습니다.');
  }, []);

  if (loading) {
    return <Loading />;
  }

  return (
    <>
      <TopBar backButtonLink='/' />
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
          <PrivacySignOutBox onClick={logout} to='/login'>
            로그아웃
          </PrivacySignOutBox>
        </PrivacyRightBox>
      </TopSection>
      <AwardsSection>
        <AwardSummary awardName='TOP3' awardCount={1} />
        <AwardSummary awardName='TOP10' awardCount={0} />
        <AwardSummary awardName='TOP50' awardCount={12} />
      </AwardsSection>
      <NavSection>
        <NavElement linkTo='/'>수상내역</NavElement>
        <NavElement linkTo='/'>작성글</NavElement>
        <NavElement linkTo='/'>작성 댓글</NavElement>
        <MySectorSection />
      </NavSection>
      <BottomBlank />
      <Bottom selected={PROFILE} />
    </>
  );
}

export default MyProfilePage;
