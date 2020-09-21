import React from 'react';
import { ListItem, Typography, ListItemText, Divider } from '@material-ui/core';
import { convertDateFormat } from '../../../util/TimeUtils';
import { makeStyles } from "@material-ui/core/styles";
import LinkWithoutStyle from "../../../util/LinkWithoutStyle";
import { MAIN_COLOR } from "../../../constants/Color";
import { DEFAULT_IMAGE_URL } from "../myProfileEdit/MyProfileEditPage";

const useStyle = makeStyles({
  photoAndTextsLayout: {
    display: 'flex',
    flexDirection: 'row',
  },
  creatorProfilePhoto: (props) => ({
    width: '35px',
    height: '35px',
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
  }
});

const CommentItem = ({ comment, myId }) => {
  const isMyComment = comment.creator.id === myId;
  const classes = useStyle({
    creatorProfilePhotoUrl: comment.creator.image ? comment.creator.image.url : DEFAULT_IMAGE_URL
  });

  const makeCreatorPhotoUi = () => {
    if (comment.creator.nickname === "탈퇴한 회원") {
      return <div className={classes.creatorProfilePhoto} />
    }
    return <LinkWithoutStyle
      className={classes.creatorProfilePhoto}
      to={isMyComment ? '/users/me' : '/users/' + comment.creator.id}
    />;
  };

  return (
    <>
      <ListItem alignItems='flex-start' className={classes.photoAndTextsLayout}>
        {makeCreatorPhotoUi()}
        <div className={classes.textsLayout}>
          <Typography variant='subtitle1' color='textSecondary'>
            {comment.creator.nickname}
          </Typography>
          <ListItemText primary={comment.writing} secondary={convertDateFormat(comment.createdAt)} />
        </div>
      </ListItem>
      <Divider />
    </>
  );
};

export default CommentItem;
