import React, { useMemo, useState } from 'react';
import { Typography } from '@material-ui/core';

import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { findOthersProfileById } from '../../api/API';
import { DEFAULT_IMAGE_URL } from '../myProfileEdit/MyProfileEditPage';
import Loading from '../../Loading';
import TopBar from '../myProfile/myProfileTopBar';
import { Nickname, PrivacyBox, ProfilePhoto, TopSection } from '../../myProfile/PrivacySection';
import { AwardsSection, AwardSummary } from '../../myProfile/AwardSection';
import { NavElement, NavSection } from '../../myProfile/LinksSection';
import Bottom from '../../Bottom';
import BottomBlank from '../../BottomBlank';

function OthersProfilePage({ match }) {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [userId, setUserId] = useState(null);
  const [nickname, setNickname] = useState('');
  const [profilePhotoUrl, setProfilePhotoUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [awardsCount, setAwardsCount] = useState(null);

  useMemo(() => {
    setLoading(true);
    findOthersProfileById({
      accessToken: accessToken,
      userId: match.params.userId,
    }).then((userResponse) => {
      setNickname(userResponse.nickname);
      setUserId(userResponse.id);
      setProfilePhotoUrl(userResponse.image ? userResponse.image.url : DEFAULT_IMAGE_URL);
      setAwardsCount(userResponse.awardsCount);
    });
    setLoading(false);
  }, [accessToken, match.params.userId]);

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
        </PrivacyBox>
      </TopSection>
      <AwardsSection>
        <AwardSummary awardName='TOP1' awardsCount={awardsCount !== null ? awardsCount.topOne : 0} />
        <AwardSummary awardName='TOP3' awardsCount={awardsCount !== null ? awardsCount.topThree : 0} />
        <AwardSummary awardName='부문 수상' awardsCount={awardsCount !== null ? awardsCount.sector : 0} />
      </AwardsSection>
      <NavSection>
        <NavElement linkTo={`/users/${userId}/awards`}>수상내역</NavElement>
        <NavElement linkTo={`${match.params.userId}/posts`}>작성글</NavElement>
      </NavSection>
      <BottomBlank />
      <Bottom />
    </>
  );
}

export default OthersProfilePage;
