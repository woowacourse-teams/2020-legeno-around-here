import React, {useState} from 'react';
import { Divider, ListItem, Typography } from '@material-ui/core';
import { convertDateFormat } from '../../../util/TimeUtils';
import { makeStyles } from '@material-ui/core/styles';
import LinkWithoutStyle from '../../../util/LinkWithoutStyle';
import { MAIN_COLOR } from '../../../constants/Color';
import { DEFAULT_IMAGE_URL } from '../myProfileEdit/MyProfileEditPage';
import {getAccessTokenFromCookie} from "../../../util/TokenUtils";
import {pressCommentZzang} from "../../api/API";

const useStyle = makeStyles({
  photoAndTextsLayout: {
    display: 'flex',
    flexDirection: 'row',
  },
  creatorProfilePhoto: (props) => ({
    width: '35px',
    height: '35px',
    minWidth: '35px',
    minHeight: '35px',
    backgroundRepeat: 'no-repeat',
    backgroundSize: 'cover',
    backgroundPosition: 'center',
    borderRadius: '300px',
    backgroundImage: `url(${props.creatorProfilePhotoUrl})`,
    border: `1px solid ${MAIN_COLOR}`,
    marginTop: '5px',
    marginRight: '3px',
  }),
  textsLayout: {
    display: 'flex',
    flexDirection: 'column',
    marginLeft: '8px',
    width: '98%',
  },
  secondaryInfoSection: {
    width: '90%',
    display: 'flex',
    flexDirection: 'row',
    alignContent: 'center',
    justifyContent: 'center',
  },
  createdTime: {
    display: 'inline',
    margin: 'auto auto auto 0',
  },
  deleteButton: {
    display: 'inline-block',
    margin: 'auto 0 auto auto',
  },
  zzangButton: {
    display: 'inline-block',
    width: '20px',
    height: '28px',
    backgroundImage: 'url(/images/thumb_gray.png)',
    backgroundPosition: 'center-top',
    backgroundSize: 'contain',
    backgroundRepeat: 'no-repeat',
  },
});

const CommentItem = ({ comment, myId, onCommentDelete, history }) => {
  const [accessToken] = useState(getAccessTokenFromCookie());
  const isMyComment = comment.creator.id === myId;
  const classes = useStyle({
    creatorProfilePhotoUrl: comment.creator.image ? comment.creator.image.url : DEFAULT_IMAGE_URL,
  });

  const makeCreatorPhotoUi = () => {
    if (comment.creator.nickname === '탈퇴한 회원') {
      return <div className={classes.creatorProfilePhoto} />;
    }
    return (
      <LinkWithoutStyle
        className={classes.creatorProfilePhoto}
        to={isMyComment ? '/users/me' : '/users/' + comment.creator.id}
      />
    );
  };

  const makeZzangButtonUi = () => {
    return <>
      <div
        className={classes.zzangButton}
        onClick={() => pressCommentZzang(comment.id, accessToken, history)}
      ></div>
      <Typography
        variant='subtitle2'
        color='textSecondary'
        onClick={() => onCommentDelete(accessToken, comment.id)}
      >
        {comment.zzang.count}
      </Typography>
    </>;
  }

  return (
    <>
      <ListItem alignItems='flex-start' className={classes.photoAndTextsLayout}>
        {makeCreatorPhotoUi()}
        <div className={classes.textsLayout}>
          <Typography variant='subtitle2' color='textSecondary'>
            {comment.creator.nickname}
          </Typography>
          <Typography variant='subtitle1'>
            {comment.writing.split('\n').map((line, index) => {
              return (
                <span key={index}>
                  {line}
                  <br />
                </span>
              );
            })}
          </Typography>
          <div className={classes.secondaryInfoSection}>
            <Typography
              variant='subtitle2'
              color='textSecondary'
              className={classes.createdTime}
            >
              {convertDateFormat(comment.createdAt)}
            </Typography> {
              isMyComment ?
                <Typography
                  variant='subtitle2'
                  color='textSecondary'
                  className={classes.deleteButton}
                  onClick={() => onCommentDelete(accessToken, comment.id)}
                >
                  삭제
                </Typography>
              : makeZzangButtonUi()
            }
          </div>
        </div>
      </ListItem>
      <Divider />
    </>
  );
};

export default CommentItem;
