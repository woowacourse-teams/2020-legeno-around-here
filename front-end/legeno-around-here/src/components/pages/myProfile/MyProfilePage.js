import React, { useCallback, useMemo, useState } from 'react';
import makeStyles from '@material-ui/core/styles/makeStyles';
import { Container, Typography } from '@material-ui/core';
import Bottom from '../../Bottom';
import { PROFILE } from '../../../constants/BottomItems';
import { findMyInfo } from '../../api/API';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import {
  Email,
  Nickname,
  PrivacyBox,
  PrivacyEditBox,
  PrivacyRightEditLinks,
  PrivacySignOutBox,
  ProfilePhoto,
  TopSection,
  TopSectionWithoutPhoto,
} from '../../myProfile/PrivacySection';
import { AwardsSection, AwardSummary } from '../../myProfile/AwardSection';
import { NavElement, NavSection } from '../../myProfile/LinksSection';
import { DEFAULT_IMAGE_URL } from '../myProfileEdit/MyProfileEditPage';
import MySectorSection from './MySectorSection';
import BottomBlank from '../../BottomBlank';
import LinkWithoutStyle from '../../../util/LinkWithoutStyle';
import ArrowBackTopBar from '../../topBar/ArrowBackTopBar';

const useStyle = makeStyles({
  withdrawButtonSection: {
    width: '97%',
    margin: '0 auto',
    height: '50px',
    display: 'flex',
    flexDirection: 'column',
    justifyContent: 'center',
    color: '#888888',
    textAlign: 'right',
  },
});

const MyProfilePage = ({ history }) => {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [profilePhotoUrl, setProfilePhotoUrl] = useState('');
  const [loading, setLoading] = useState(false);
  const [awardsCount, setAwardsCount] = useState(null);
  const classes = useStyle();

  useMemo(() => {
    setLoading(true);
    findMyInfo(accessToken, history).then((userResponse) => {
      setEmail(userResponse.email);
      setNickname(userResponse.nickname);
      setProfilePhotoUrl(userResponse.image ? userResponse.image.url : DEFAULT_IMAGE_URL);
      setAwardsCount(userResponse.awardsCount);
    });
    setLoading(false);
  }, [accessToken, history]);

  const logout = useCallback(() => {
    alert('로그아웃 되었습니다.');
    history.push('/login');
  }, [history]);

  if (loading) {
    return <Loading />;
  }

  return (
    <>
      <ArrowBackTopBar />
      <Container>
        <TopSection>
          <ProfilePhoto photoUrl={profilePhotoUrl} />
          <TopSectionWithoutPhoto>
            <PrivacyBox>
              <Typography component='h1' variant='h5'>
                <Nickname>{nickname}</Nickname>
              </Typography>
              <Email>{email}</Email>
            </PrivacyBox>
            <PrivacyRightEditLinks>
              <Typography>
                <PrivacyEditBox to='/myProfileEdit'>수정</PrivacyEditBox>
                <PrivacySignOutBox onClick={logout}>로그아웃</PrivacySignOutBox>
              </Typography>
            </PrivacyRightEditLinks>
          </TopSectionWithoutPhoto>
        </TopSection>
        <AwardsSection>
          <AwardSummary awardName='TOP1' awardsCount={awardsCount !== null ? awardsCount.topOne : 0} />
          <AwardSummary awardName='TOP3' awardsCount={awardsCount !== null ? awardsCount.topThree : 0} />
          <AwardSummary awardName='부문 수상' awardsCount={awardsCount !== null ? awardsCount.sector : 0} />
        </AwardsSection>

        <NavSection>
          <NavElement linkTo='/my-awards'>수상내역</NavElement>
          <NavElement linkTo='/my-posts'>작성글</NavElement>
          {/*<NavElement linkTo='/home'>작성 댓글</NavElement>*/}
          <MySectorSection history={history} />
        </NavSection>
        <Typography className={classes.withdrawButtonSection} component={LinkWithoutStyle} to='/confirm-withdraw'>
          회원탈퇴
        </Typography>
        <BottomBlank />
      </Container>
      <Bottom selected={PROFILE} />
    </>
  );
};

export default MyProfilePage;
