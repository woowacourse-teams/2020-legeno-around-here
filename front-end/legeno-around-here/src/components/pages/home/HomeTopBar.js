import React, { useEffect, useState } from 'react';
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
import List from '@material-ui/core/List';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import Loading from '../../Loading';
import AreaItem from '../../AreaItem';
import { findAllAreas, findAllSimpleSectors } from '../../api/API';

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
  list: {
    height: 400,
    overflow: 'auto',
  },
}));

const HomeTopBar = ({ setSectorId }) => {
  const classes = useStyles();
  const accessToken = getAccessTokenFromCookie();
  const mainArea = localStorage.getItem('mainAreaName');

  const [page] = useState(0);
  const [open, setOpen] = useState(false);
  const [areas, setAreas] = useState([]);
  const [loading, setLoading] = useState(false);
  const [simpleSectors, setSimpleSectors] = useState([]);
  const [areaKeyword, setAreaKeyword] = useState(mainArea);

  if (!mainArea) {
    localStorage.setItem('mainAreaName', '서울특별시');
  }

  useEffect(() => {
    const loadAllSimpleSectors = async () => {
      const foundSimpleSectors = await findAllSimpleSectors(accessToken);
      await setSimpleSectors(foundSimpleSectors);
    };
    loadAllSimpleSectors();
  }, [accessToken]);

  const loadAreas = async () => {
    const accessToken = getAccessTokenFromCookie();
    setLoading(true);
    const allAreas = await findAllAreas(page, accessToken, areaKeyword);
    if (allAreas.length === 0) {
      alert('검색 결과가 없습니다! 다시 검색해주세요!');
    }
    setAreas(allAreas);
    setLoading(false);
  };

  const getInputArea = (event) => {
    setAreaKeyword(event.target.value);
  };

  const findAllArea = () => {
    loadAreas();
  };

  const handleOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
  };

  return (
    <>
      {loading && <Loading />}
      <AppBar position='sticky'>
        <Toolbar>
          <IconButton
            edge='start'
            className={classes.menuButton}
            color='inherit'
            aria-label='open drawer'
            onClick={handleOpen}
          >
            <ExpandMoreIcon />
            <Typography className={classes.title} variant='h6' noWrap>
              {mainArea}
            </Typography>
          </IconButton>
          <Autocomplete
            id='sector-search'
            freeSolo
            options={simpleSectors}
            // null 전달 가능
            onChange={(event, option) => option && setSectorId(option.id)}
            // onChange={(event, option) => option && console.log(option)}
            getOptionLabel={(option) => option.name}
            fullWidth
            renderInput={(params) => <TextField {...params} placeholder='부문을 검색하세요!' />}
          />
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
                <TextField
                  id='outlined-search'
                  label='Search field'
                  type='search'
                  variant='outlined'
                  onChange={(event) => getInputArea(event)}
                />
                <Button>
                  <SearchIcon onClick={() => findAllArea()} />
                </Button>
                {areas.length > 0 && (
                  <List component='nav' className={classes.list}>
                    {areas.map((area) => (
                      <AreaItem key={area.id} area={area} />
                    ))}
                  </List>
                )}
              </div>
            </Fade>
          </Modal>
          <div className={classes.grow} />
          <div className={classes.sectionDesktop}>
            <IconButton
              aria-label='show 17 new notifications'
              color='inherit'
              onClick={() => {
                alert('아직 알람기능이 완성되지 않았습니다!');
              }}
            >
              <Badge badgeContent={0} color='secondary'>
                <NotificationsIcon />
              </Badge>
            </IconButton>
          </div>
        </Toolbar>
      </AppBar>
    </>
  );
};

export default HomeTopBar;
