import React, {useMemo, useState} from 'react';

import TopBar from '../myProfile/myProfileTopBar';
import Bottom from '../../Bottom';
import {PROFILE} from '../../../constants/BottomItems';
import {findMyInfo} from '../../api/API';
import Loading from '../../Loading';
import {getAccessTokenFromCookie} from '../../../util/TokenUtils';
import {
  Email,
  Nickname,
  PrivacyBox,
  PrivacyEditBox,
  ProfilePhoto,
  TopSection,
} from '../../myProfile/PrivacySection';
import Typography from '@material-ui/core/Typography';

function MyProfileEditPage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [profilePhotoUrl, setProfilePhotoUrl] = useState(
    '/default-profile.png',
  );
  const [loading, setLoading] = useState(false);

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
      <TopBar backButtonLink="/myProfile" />
      <TopSection>
        <ProfilePhoto photoUrl={profilePhotoUrl} />
        <PrivacyBox>
          <Typography component="h1" variant="h5">
            <Nickname>{nickname}</Nickname>
          </Typography>
          <Email>{email}</Email>
        </PrivacyBox>
        <PrivacyEditBox>수정</PrivacyEditBox>
      </TopSection>
      <Bottom selected={PROFILE} />
    </>
  );
}

export default MyProfileEditPage;
