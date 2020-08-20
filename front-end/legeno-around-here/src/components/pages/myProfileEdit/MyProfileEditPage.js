import React, {useMemo, useState} from 'react';

import TopBar from '../myProfile/myProfileTopBar';
import Bottom from '../../Bottom';
import {PROFILE} from '../../../constants/BottomItems';
import {findMyInfo} from '../../api/API';
import Loading from '../../Loading';
import {getAccessTokenFromCookie} from '../../../util/TokenUtils';
import {Email, Nickname, PrivacyBox,} from '../../myProfile/PrivacySection';
import Typography from '@material-ui/core/Typography';
import PhotoEditSection from './PhotoEditSection';

function MyProfileEditPage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [originalProfilePhotoUrl, setProfilePhotoUrl] = useState(
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
      <PhotoEditSection originalPhotoUrl={originalProfilePhotoUrl} />
      <PrivacyBox>
        <Typography component="h1" variant="h5">
          <Nickname>{nickname}</Nickname>
        </Typography>
        <Email>{email}</Email>
      </PrivacyBox>
      <Bottom selected={PROFILE} />
    </>
  );
}

export default MyProfileEditPage;
