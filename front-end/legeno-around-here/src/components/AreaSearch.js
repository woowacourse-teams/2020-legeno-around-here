import React, { useState } from 'react';
import Modal from '@material-ui/core/Modal';
import Backdrop from '@material-ui/core/Backdrop';
import Fade from '@material-ui/core/Fade';
import TextField from '@material-ui/core/TextField';
import Button from '@material-ui/core/Button';
import SearchIcon from '@material-ui/icons/Search';
import IconButton from '@material-ui/core/IconButton';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Typography from '@material-ui/core/Typography';
import { getAccessTokenFromCookie } from '../util/TokenUtils';
import { findAreasFromPage } from './api/API';
import Loading from './Loading';
import { makeStyles } from '@material-ui/core/styles';
import InfiniteScroll from 'react-infinite-scroll-component';
import Areas from './Areas';

const useStyles = makeStyles((theme) => ({
  menuButton: {
    marginRight: theme.spacing(0),
  },
  title: {
    display: 'block',
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
  internal: {
    paddingTop: '5px',
  },
}));

const DEFAULT_SIZE = 10;

const AreaSearch = ({ history, selected }) => {
  const mainArea = localStorage.getItem('mainAreaName');

  if (!mainArea) {
    localStorage.setItem('mainAreaName', '전체');
  }

  const classes = useStyles();
  const accessToken = getAccessTokenFromCookie();
  const [page, setPage] = useState(0);
  const [hasMore, setHasMore] = useState(false);
  const [open, setOpen] = useState(false);
  const [listOpen, setListOpen] = useState(false);
  const [areas, setAreas] = useState([]);
  const [areaKeyword, setAreaKeyword] = useState(mainArea);

  const loadNextAreas = async () => {
    try {
      const nextAreas = await findAreasFromPage(page, accessToken, areaKeyword, history);
      if (nextAreas.length < DEFAULT_SIZE) {
        setAreas(areas.concat(nextAreas));
        setPage(page + 1);
        setHasMore(false);
        return;
      }
      setAreas(areas.concat(nextAreas));
      setPage(page + 1);
    } catch (error) {
      setHasMore(false);
    }
  };

  const searchAreaKeyWord = () => {
    setHasMore(true);
    loadNextAreas();
    setListOpen(true);
  };

  const initAreaSearch = () => {
    setHasMore(false);
    setPage(0);
    setAreas([]);
  };

  const getInputArea = (event) => {
    initAreaSearch();
    setAreaKeyword(event.target.value);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    initAreaSearch();
    setAreaKeyword('');
    setListOpen(false);
    setOpen(false);
  };

  function refreshPage(history, selected) {
    history.replace('/' + selected + '-reload');
  }

  const setMainAreaAll = () => {
    localStorage.setItem('mainAreaId', '');
    localStorage.setItem('mainAreaName', '전체');
    refreshPage(history, selected);
  };

  return (
    <>
      <IconButton edge='start' className={classes.menuButton} color='inherit' onClick={handleOpen}>
        <ExpandMoreIcon />
        <Typography className={classes.title} variant='h6' noWrap>
          {mainArea}
        </Typography>
      </IconButton>
      <Modal
        aria-labelledby='transition-modal-title'
        aria-describedby='transition-modal-description'
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
            <h2 id='transition-modal-title'>지역을 검색해주세요!</h2>
            <div>
              <TextField
                id='outlined-search'
                label='Search field'
                type='search'
                variant='outlined'
                onChange={(event) => getInputArea(event)}
                inputProps={{ maxLength: 40 }}
              />
              <Button>
                <SearchIcon onClick={() => searchAreaKeyWord()} />
              </Button>
            </div>
            <div className={classes.internal}>
              <Button variant='contained' color='primary' onClick={() => setMainAreaAll()}>
                모든 지역 글 보기
              </Button>
            </div>
            {listOpen && (
              <InfiniteScroll
                next={loadNextAreas}
                hasMore={hasMore}
                loader={<Loading />}
                dataLength={areas.length}
                height={'400px'}
                endMessage={<Typography>모든 지역을 확인하셨습니다!</Typography>}
              >
                {areas && areas.length > 0 && <Areas areas={areas} history={history} selected={selected} />}
              </InfiniteScroll>
            )}
          </div>
        </Fade>
      </Modal>
    </>
  );
};

export default AreaSearch;
