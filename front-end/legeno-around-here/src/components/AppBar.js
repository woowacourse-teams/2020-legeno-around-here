import React, { useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Typography from '@material-ui/core/Typography';
import Badge from '@material-ui/core/Badge';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import NotificationsIcon from '@material-ui/icons/Notifications';
import Modal from '@material-ui/core/Modal';
import Backdrop from '@material-ui/core/Backdrop';
import Fade from '@material-ui/core/Fade';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import SearchIcon from '@material-ui/icons/Search';
import { getAllAreas } from './api/API';
import { getAccessTokenFromCookie } from '../util/TokenUtils';

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  menuButton: {
    marginRight: theme.spacing(0),
  },
  title: {
    display: 'block',
  },
  sectionDesktop: {
    display: 'flex',
  },
  modal: {
    display: 'flex',
    alignItems: 'center',
    justifyContent: 'center',
  },
  paper: {
    backgroundColor: theme.palette.background.paper,
    border: '2px solid #000',
    boxShadow: theme.shadows[5],
    padding: theme.spacing(2, 4, 3),
  },
}));

export default function PrimarySearchAppBar() {
  const classes = useStyles();
  const mainArea = localStorage.getItem('mainArea');

  const [page] = useState(1);
  const [open, setOpen] = useState(false);
  const [areas, setAreas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [areaKeyword, setAreaKeyword] = useState(mainArea);

  if (!mainArea) {
    localStorage.setItem('mainArea', '서울특별시');
  }

  const loadPosts = async () => {
    const accessToken = getAccessTokenFromCookie();
    setLoading(true);
    const allAreas = await getAllAreas(page, accessToken, areaKeyword);
    setAreas(allAreas);
    setLoading(false);
  };

  const getInputArea = (event) => {
    setAreaKeyword(event.target.value);
  };

  const findAllArea = () => {
    loadPosts();
    console.log(areaKeyword);
    console.log(areas);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <>
      {loading && <div>Loading ...</div>}
      <AppBar position="sticky">
        <Toolbar>
          <IconButton
            edge="start"
            className={classes.menuButton}
            color="inherit"
            aria-label="open drawer"
            onClick={handleOpen}
          >
            <ExpandMoreIcon />
            <Typography className={classes.title} variant="h6" noWrap>
              {mainArea}
            </Typography>
          </IconButton>
          <Modal
            aria-labelledby="transition-modal-title"
            aria-describedby="transition-modal-description"
            className={classes.modal}
            open={open}
            onClose={handleClose}
            closeAfterTransition
            BackdropComponent={Backdrop}
            BackdropProps={{
              timeout: 500,
            }}
          >
            <Fade in={open}>
              <div className={classes.paper}>
                <h2 id="transition-modal-title">지역을 검색해주세요!</h2>
                <TextField
                  id="outlined-search"
                  label="Search field"
                  type="search"
                  variant="outlined"
                  onChange={(event) => getInputArea(event)}
                />
                <Button>
                  <SearchIcon onClick={() => findAllArea()} />
                </Button>
              </div>
            </Fade>
          </Modal>
          <div className={classes.grow} />
          <div className={classes.sectionDesktop}>
            <IconButton
              aria-label="show 17 new notifications"
              color="inherit"
              onClick={() => {
                alert('아직 신고기능이 완성되지 않았습니다!');
              }}
            >
              <Badge badgeContent={0} color="secondary">
                <NotificationsIcon />
              </Badge>
            </IconButton>
          </div>
        </Toolbar>
      </AppBar>
    </>
  );
}
