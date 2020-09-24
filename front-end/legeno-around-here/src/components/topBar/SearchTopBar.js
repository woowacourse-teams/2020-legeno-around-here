import React, { useEffect, useState } from 'react';
import { makeStyles } from '@material-ui/core/styles';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import IconButton from '@material-ui/core/IconButton';
import Badge from '@material-ui/core/Badge';
import NotificationsIcon from '@material-ui/icons/Notifications';
import TextField from '@material-ui/core/TextField';
import Autocomplete from '@material-ui/lab/Autocomplete';
import { getAccessTokenFromCookie } from '../../util/TokenUtils';
import { findAllSimpleSectors, getUnreadNotificationCount } from '../api/API';
import AreaSearch from '../AreaSearch';
import LinkWithoutStyle from '../../util/LinkWithoutStyle';
import { getMainSectorName, setMainSectorId, setMainSectorName } from '../../util/localStorageUtils';

const useStyles = makeStyles(() => ({
  grow: {
    flexGrow: 1,
  },
  textField: {
    color: 'white',
    fontSize: '1.3rem',
    position: 'relative',
    top: '1px',
  },
}));

const SIMPLE_ALL_SECTOR = {
  id: '',
  name: '전체 부문',
};

const SearchTopBar = ({ setter, getter, history }) => {
  const classes = useStyles();
  const accessToken = getAccessTokenFromCookie();
  const [simpleSectors, setSimpleSectors] = useState([SIMPLE_ALL_SECTOR]);
  const [unreadNotification, setUnreadNotification] = useState(0);

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

  const changeSector = (optionId, optionName) => {
    if (getter.sectorId === optionId) {
      return;
    }
    setter.removeContent();
    setter.setSectorId(optionId);
    setMainSectorId(optionId);
    setMainSectorName(optionName);
  };

  useEffect(() => {
    getUnreadNotificationCount(accessToken, setUnreadNotification, history);
  }, [accessToken, history]);

  return (
    <>
      <AppBar position='sticky'>
        <Toolbar>
          <AreaSearch setter={setter} getter={getter} />
          <Autocomplete
            id='sector-search'
            freeSolo
            options={simpleSectors}
            onChange={(event, option) => option && changeSector(option.id, option.name)}
            getOptionLabel={(option) => option.name}
            fullWidth
            renderInput={(params) => (
              <TextField
                {...params}
                placeholder={getMainSectorName() + ' (검색)'}
                inputProps={{
                  ...params.inputProps,
                  className: classes.textField,
                }}
              />
            )}
          />
          <div className={classes.grow} />
          <div>
            <LinkWithoutStyle to='/notification'>
              <IconButton aria-label='show 17 new notifications' color='inherit'>
                <Badge badgeContent={unreadNotification} color='secondary'>
                  <NotificationsIcon style={{ color: 'white' }} />
                </Badge>
              </IconButton>
            </LinkWithoutStyle>
          </div>
        </Toolbar>
      </AppBar>
    </>
  );
};

export default SearchTopBar;
