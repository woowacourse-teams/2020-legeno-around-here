import React from 'react';
import { ListItem, Typography, ListItemText, Divider } from '@material-ui/core';
import { convertDateFormat } from '../../../util/TimeUtils';
import {makeStyles} from "@material-ui/core/styles";
import LinkWithoutStyle from "../../../util/LinkWithoutStyle";
import {MAIN_COLOR} from "../../../constants/Color";

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

const CommentItem = ({ comment, myUserId }) => {
  console.log(comment)
  const isMyComment = comment.creator.id === myUserId;
  const classes = useStyle({
    creatorProfilePhotoUrl: comment.creator.image.url
  });

  return (
    <>
      <ListItem alignItems='flex-start' className={classes.photoAndTextsLayout}>
        <LinkWithoutStyle
          className={classes.creatorProfilePhoto}
          to={isMyComment ? '/users/me' : '/users/' + comment.creator.id}
        />
        <div className={classes.textsLayout}>
          <Typography variant='subtitle1'>작성자 : {comment.creator.nickname}</Typography>
          <ListItemText primary={comment.writing} secondary={convertDateFormat(comment.createdAt)} />
        </div>
      </ListItem>
      <Divider />
    </>
  );
};

export default CommentItem;
