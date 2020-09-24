import React from 'react';
import Bottom from '../../Bottom';
import BottomBlank from '../../BottomBlank';
import { Typography } from '@material-ui/core';
import makeStyles from '@material-ui/core/styles/makeStyles';
import PostDetailTopBar from '../post/PostDetailTopBar';
import LinkWithoutStyle from '../../../util/LinkWithoutStyle';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { withdraw } from '../../api/API';

const useStyle = makeStyles({
  withdrawInformationSection: {
    width: '97%',
    margin: '0 auto',
    height: '200px',
    display: 'flex',
    flexDirection: 'column',
  },
  buttonWhetherWithdraw: {
    width: '60px',
    height: '30px',
    borderRadius: 0,
    borderBottom: '3px solid #bbbbbb',
    marginTop: '20px',
    textAlign: 'center',
  },
});

const handleWithdraw = (accessToken, history) => {
  withdraw(accessToken, history);
};

const WithdrawConfirmPage = ({ history }) => {
  const accessToken = getAccessTokenFromCookie();
  const classes = useStyle();

  return (
    <>
      <PostDetailTopBar />
      <BottomBlank />
      <div className={classes.withdrawInformationSection}>
        <Typography>
          한 번 탈퇴하시면 다시 복구할 수 없습니다.
          <br />
          정말로 탈퇴하시겠습니까?
          <br />
          탈퇴시 작성하신 글 및 댓글은 자동 제거되지 않습니다.
        </Typography>
        <Typography className={classes.buttonWhetherWithdraw} component={LinkWithoutStyle} to='/users/me'>
          아니요
        </Typography>
        <Typography className={classes.buttonWhetherWithdraw} onClick={(e) => handleWithdraw(accessToken, history)}>
          네
        </Typography>
      </div>
      <BottomBlank />
      <Bottom />
    </>
  );
};

export default WithdrawConfirmPage;
