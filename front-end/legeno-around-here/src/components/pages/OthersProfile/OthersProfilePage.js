import React, { useCallback, useEffect, useMemo, useState } from 'react'
import { getAccessTokenFromCookie, removeAccessTokenCookie } from '../../../util/TokenUtils'
import { findAllMySector, findMyInfo } from '../../api/API'
import { DEFAULT_IMAGE_URL } from '../myProfileEdit/MyProfileEditPage'
import Loading from '../../Loading'
import TopBar from '../myProfile/myProfileTopBar'
import {
  Email,
  Nickname,
  PrivacyBox,
  ProfilePhoto,
  TopSection
} from '../../myProfile/PrivacySection'
import { Divider, Typography } from '@material-ui/core'
import { AwardsSection, AwardSummary } from '../../myProfile/AwardSection'
import { NavElement, NavSection } from '../../myProfile/LinksSection'
import MySectors from '../myProfile/MySectors'
import Bottom from '../../Bottom'
import { PROFILE } from '../../../constants/BottomItems'

function OthersProfilePage({ match }) {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [profilePhotoUrl, setProfilePhotoUrl] = useState('');
  const [loading, setLoading] = useState(false);

  useMemo(() => {
    setLoading(true);
    // findMyInfo(accessToken).then((userResponse) => {
    //   setEmail(userResponse.email);
    //   setNickname(userResponse.nickname);
      setProfilePhotoUrl(/*userResponse.image ? userResponse.image.url : */DEFAULT_IMAGE_URL);
    // });
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
            {/*<Nickname>{nickname}</Nickname>*/}닉네임
          </Typography>
          {/*<Email>{email}</Email>*/}이메일
        </PrivacyBox>
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
      </NavSection>
      <Bottom />
    </>
  );
}

export default OthersProfilePage;
