import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import NotificationsIcon from '@material-ui/icons/Notifications';
import { getAccessTokenFromCookie } from '../../../util/TokenUtils';
import { findAllSimpleSectors, getUnreadNotificationCount } from '../../api/API';
import { Link } from 'react-router-dom';
import AreaSearch from '../../AreaSearch';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';

const useStyles = makeStyles((theme) => ({
  grow: {
    flexGrow: 1,
  },
  textField: {
    color: 'white',
    fontSize: '1.3rem',
    position: 'relative',
    top: '2px',
  },
}));

const SIMPLE_ALL_SECTOR = {
  id: '',
  name: '전체 부문',
};

const RankingTopBar = ({ setter, sectorId, history }) => {
  const classes = useStyles();
  const mainArea = localStorage.getItem('mainAreaName');
  const accessToken = getAccessTokenFromCookie();
  const [simpleSectors, setSimpleSectors] = useState([SIMPLE_ALL_SECTOR]);
  const [unreadNotification, setUnreadNotification] = useState(0);

  if (!mainArea) {
    localStorage.setItem('mainAreaName', '서울특별시');
  }

  useEffect(() => {
    const loadAllSimpleSectors = async () => {
      findAllSimpleSectors(accessToken, history).then(async (foundSimpleSectors) => {
        if (foundSimpleSectors) {
          await foundSimpleSectors.unshift(SIMPLE_ALL_SECTOR);
          await setSimpleSectors(foundSimpleSectors);
        }
      });
    };
    loadAllSimpleSectors();
  }, [accessToken, history]);

  const changeSector = (optionId) => {
    if (sectorId === optionId) {
      return;
    }
    setter.setPage(0);
    setter.setPosts([]);
    setter.setSectorId(optionId);
    setter.setLocationParams('');
  };

  useEffect(() => {
    getUnreadNotificationCount(accessToken, setUnreadNotification, history);
  }, [accessToken, history]);

  return (
    <AppBar position='sticky'>
      <Toolbar>
        <AreaSearch history={history} selected='ranking' />
        <Autocomplete
          id='sector-search'
          freeSolo
          options={simpleSectors}
          onChange={(event, option) => option && changeSector(option.id)}
          getOptionLabel={(option) => option.name}
          fullWidth
          renderInput={(params) => (
            <TextField
              {...params}
              placeholder='전체 부문 (검색)'
              inputProps={{
                ...params.inputProps,
                className: classes.textField,
              }}
            />
          )}
        />
        <div className={classes.grow} />
        <div className={classes.sectionDesktop}>
          <Link to='/notification'>
            <IconButton aria-label='show 17 new notifications' color='inherit'>
              <Badge badgeContent={unreadNotification} color='secondary'>
                <NotificationsIcon style={{ color: 'white' }} />
              </Badge>
            </IconButton>
          </Link>
        </div>
      </Toolbar>
    </AppBar>
  );
};

export default RankingTopBar;
