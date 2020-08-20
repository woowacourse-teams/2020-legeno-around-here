import React, { useMemo, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';

import TopBar from '../myProfile/myProfileTopBar';
import Bottom from '../../Bottom';
import { PROFILE } from '../../../constants/BottomItems';
import { findMyInfo } from '../../api/API';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import PhotoEditSection from './PhotoEditSection';
import useStyle from './MyProfileEditStyles';

function MyProfileEditPage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [originalProfilePhotoUrl, setProfilePhotoUrl] = useState(
    '/default-profile.png',
  );
  const [loading, setLoading] = useState(false);

  const classes = useStyle();

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
      <div className={classes.basicLayout}>
        <PhotoEditSection originalPhotoUrl={originalProfilePhotoUrl} />
        <div className={classes.infoEditSection}>
          <Typography component="div">{email}</Typography>
          <Typography component="h1" variant="h5">
            <TextField
              id="standard-basic"
              label="새 닉네임"
              className={classes.newNicknameInput}
            />
          </Typography>
        </div>
        <Button
          type="submit"
          variant="contained"
          color="primary"
          className={classes.submit}
        >
          저장
        </Button>
      </div>
      <Bottom selected={PROFILE} />
    </>
  );
}

export default MyProfileEditPage;
