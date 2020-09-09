import React from 'react';
import { Link } from 'react-router-dom';
import { makeStyles } from '@material-ui/core/styles';
import IconButton from '@material-ui/core/IconButton';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';

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

export default function TopBar({ backButtonLink }) {
  const classes = useStyles();

  return (
    <>
      <AppBar position='sticky'>
        <Toolbar>
          <div className={classes.sectionDesktop}>
            <IconButton
              edge='start'
              className={classes.menuButton}
              color='inherit'
              aria-label='open drawer'
              component={Link}
              to={backButtonLink}
              form='posting-form'
            >
              <ArrowBackIcon />
            </IconButton>
          </div>
          <div className={classes.grow} />
        </Toolbar>
      </AppBar>
    </>
  );
}
