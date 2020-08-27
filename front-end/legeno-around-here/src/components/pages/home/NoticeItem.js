import React, { useState, useEffect, useCallback } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import CardActions from '@material-ui/core/CardActions';
import Typography from '@material-ui/core/Typography';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { readNotice } from '../../api/API';

const useStyles = makeStyles(() => ({
  root: {
    display: 'flex',
  },
  details: {
    display: 'flex',
    flexDirection: 'column',
  },
  content: {
    flex: '1 0 auto',
  },
}));

const NoticeItem = ({ notice }) => {
  const accessToken = getAccessTokenFromCookie();
  const classes = useStyles();

  const [cardColor, setCardColor] = useState('#FFFFFF');

  const { id, content, location, isRead } = notice;

  useEffect(() => {
    if (isRead === false) {
      setCardColor('#DCDFF3');
    }
  }, [isRead]);

  const noticeItemOnclick = useCallback(() => {
    readNotice(accessToken, id);
    document.location.href = location;
  }, [accessToken, id, location]);

  return (
    <Card
      className={classes.root}
      data-id={id}
      style={{
        backgroundColor: cardColor,
        margin: '10px auto',
        height: '50px',
      }}
      onClick={noticeItemOnclick}
    >
      <div className={classes.details}>
        <CardContent className={classes.content}>
          <Typography component='h6' variant='body1'>
            {content}
          </Typography>
        </CardContent>
        <CardActions disableSpacing></CardActions>
      </div>
    </Card>
  );
};

export default NoticeItem;
