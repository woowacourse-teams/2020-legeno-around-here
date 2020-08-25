import React, { useMemo, useState } from 'react';
import Typography from '@material-ui/core/Typography';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';

import TopBar from '../myProfile/myProfileTopBar';
import Bottom from '../../Bottom';
import { PROFILE } from '../../../constants/BottomItems';
import { findMyInfo, updateUser } from '../../api/API';
import Loading from '../../Loading';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import PhotoEditSection from './PhotoEditSection';
import useStyle from './MyProfileEditStyles';

export const DEFAULT_IMAGE_URL = '/images/default-profile.png';

function MyProfileEditPage() {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const [email, setEmail] = useState('');
  const [nickname, setNickname] = useState('');
  const [profilePhoto, setProfilePhoto] = useState({
    id: null,
    url: DEFAULT_IMAGE_URL,
  });

  const [originalNickname, setOriginalNickname] = useState('');
  const [loading, setLoading] = useState(false);

  const classes = useStyle();

  useMemo(() => {
    setLoading(true);
    findMyInfo({
      accessToken: accessToken,
    }).then((userResponse) => {
      setEmail(userResponse.email);
      setNickname(userResponse.nickname);
      if (userResponse.image) {
        setProfilePhoto({
          id: userResponse.image.id,
          url: userResponse.image.url,
        });
      }
      setOriginalNickname(userResponse.nickname);
    });
    setLoading(false);
  }, [accessToken]);

  if (loading) {
    return <Loading />;
  }

  const handleNicknameInputChanging = (e) => {
    if (!e.target.value || e.target.value === '') {
      setNickname(originalNickname);
      return;
    }
    setNickname(e.target.value);
  };

  const onSubmit = (event) => {
    event.preventDefault();
    updateUser(nickname, profilePhoto.id, accessToken);
  };

  return (
    <>
      <TopBar backButtonLink="/myProfile" />
      <form className={classes.basicLayout} onSubmit={onSubmit}>
        <PhotoEditSection
          profilePhoto={profilePhoto}
          setProfilePhoto={setProfilePhoto}
          accessToken={accessToken}
        />
        <div className={classes.infoEditSection}>
          <Typography component="div">{email}</Typography>
          <div className={classes.nicknameEditSection}>
            <Typography component="h1" variant="h5">
              {originalNickname}
            </Typography>
            <Typography component="h1" variant="h5">
              <TextField
                id="standard-helperText"
                label="새 닉네임"
                className={classes.newNicknameInput}
                onChange={handleNicknameInputChanging}
                helperText="공란이면 닉네임이 바뀌지 않습니다."
              />
            </Typography>
          </div>
        </div>
        <Button
          type="submit"
          variant="contained"
          color="primary"
          className={classes.submit}
        >
          저장
        </Button>
      </form>
      <Bottom selected={PROFILE} />
    </>
  );
}

export default MyProfileEditPage;
