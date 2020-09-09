import React from 'react';
import { makeStyles } from '@material-ui/core/styles';
import IconButton from '@material-ui/core/IconButton';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import SendIcon from '@material-ui/icons/Send';
import BottomBlank from '../../BottomBlank';

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(0),
  },
  sectionDesktop: {
    display: 'flex',
  },
}));

const PostingTopBar = () => {
  const classes = useStyles();

  return (
    <>
      <AppBar position='absolute'>
        <Toolbar>
          <div className={classes.grow} />
          <div className={classes.sectionDesktop}>
            <IconButton
              edge='start'
              className={classes.menuButton}
              color='inherit'
              aria-label='open drawer'
              type='submit'
              form='posting-form'
            >
              <SendIcon />
            </IconButton>
          </div>
        </Toolbar>
      </AppBar>
      <BottomBlank />
    </>
  );
};

export default PostingTopBar;
